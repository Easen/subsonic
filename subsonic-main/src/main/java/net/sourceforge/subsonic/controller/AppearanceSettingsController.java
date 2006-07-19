package net.sourceforge.subsonic.controller;

import org.springframework.web.servlet.mvc.*;
import net.sourceforge.subsonic.service.*;
import net.sourceforge.subsonic.command.*;
import net.sourceforge.subsonic.domain.*;
import net.sourceforge.subsonic.util.*;

import javax.servlet.http.*;
import java.util.*;

/**
 * Controller for the page used to administrate appearance settings.
 *
 * @author Sindre Mehus
 */
public class AppearanceSettingsController extends SimpleFormController {

    private SettingsService settingsService;
    private SecurityService securityService;

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        AppearanceSettingsCommand command = new AppearanceSettingsCommand();

        User user = securityService.getCurrentUser(request);
        UserSettings userSettings = settingsService.getUserSettings(user.getUsername());

        command.setUser(user);
        command.setLocaleIndex("-1");
        command.setThemeIndex("-1");

        Locale currentLocale = userSettings == null ? null : userSettings.getLocale();
        Locale[] locales = settingsService.getAvailableLocales();
        String[] localeStrings = new String[locales.length];
        for (int i = 0; i < locales.length; i++) {
            localeStrings[i] = locales[i].getDisplayLanguage(locales[i]);

            if (locales[i].equals(currentLocale)) {
                command.setLocaleIndex(String.valueOf(i));
            }
        }
        command.setLocales(localeStrings);

        String currentThemeId = userSettings == null ? null : userSettings.getThemeId();
        Theme[] themes = settingsService.getAvailableThemes();
        command.setThemes(themes);
        for (int i = 0; i < themes.length; i++) {
            if (themes[i].getId().equals(currentThemeId)) {
                command.setThemeIndex(String.valueOf(i));
                break;
            }
        }

        return command;
    }

    protected void doSubmitAction(Object comm) throws Exception {
        AppearanceSettingsCommand command = (AppearanceSettingsCommand) comm;

        int localeIndex = Integer.parseInt(command.getLocaleIndex());
        Locale locale = null;
        if (localeIndex != -1) {
            locale = settingsService.getAvailableLocales()[localeIndex];
        }

        int themeIndex = Integer.parseInt(command.getThemeIndex());
        String themeId = null;
        if (themeIndex != -1) {
            themeId = settingsService.getAvailableThemes()[themeIndex].getId();
        }

        String username = command.getUser().getUsername();
        UserSettings userSettings = settingsService.getUserSettings(username);
        if (userSettings == null) {
            userSettings = new UserSettings(username, null, null);
        }

        command.setReloadNeeded(!StringUtil.isEqual(locale, userSettings.getLocale()) ||
                                !StringUtil.isEqual(themeId, userSettings.getThemeId()));

        userSettings.setLocale(locale);
        userSettings.setThemeId(themeId);
        settingsService.updateUserSettings(userSettings);
    }

    public void setSettingsService(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }
}
