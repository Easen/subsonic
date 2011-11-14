package net.sourceforge.subsonic.booter;

import net.sourceforge.subsonic.booter.agent.SettingsPanel;
import net.sourceforge.subsonic.booter.agent.SubsonicAgent;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

/**
 * Application entry point for Subsonic booter.
 * <p/>
 * Use command line argument "-agent" to start the Windows service monitoring agent,
 * or "-mac" to start the Mac version of the deployer.
 *
 * @author Sindre Mehus
 */
public class Main {

    public Main(String contextName, List<String> args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext" + contextName + ".xml");

        SubsonicAgent agent  = (SubsonicAgent) context.getBean("agent");
        SettingsPanel settingsPanel = (SettingsPanel) context.getBean("settingsPanel");

        agent.setElevated(args.contains("-elevated"));

        if (args.contains("-stop")) {
            agent.startOrStopService(false);
            agent.showStatusPanel();
        }
        else if (args.contains("-start")) {
            agent.startOrStopService(true);
            agent.showStatusPanel();
        }

        if (args.contains("-settings")) {
            String[] settings = args.get(args.indexOf("-settings") + 1).split(",");
            try {
                settingsPanel.saveSettings(Integer.valueOf(settings[0]), Integer.valueOf(settings[1]), Integer.valueOf(settings[2]), settings[3]);
                agent.showSettingsPanel();
            } catch (Exception x) {
                JOptionPane.showMessageDialog(settingsPanel, x.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        System.err.println("args: " + Arrays.asList(args));

        String context = "-deployer";
        if (args.length > 0) {
            context = args[0];
        }
        new Main(context, Arrays.asList(args));
    }
}
