<%@ page import="java.net.URL" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<%! String current = "apps"; %>
<%@ include file="header.jsp" %>

<body>

<a name="top"/>

<div id="container">
    <%@ include file="menu.jsp" %>

    <div id="content">
        <div id="main-col">
            <h1 class="bottomspace">Subsonic Apps</h1>


            <p>In addition to the normal web interface, there are several other Subsonic applications:</p>

            <div class="floatcontainer margin10-t margin10-b">
                <ul class="stars column-left">
                    <li><a href="#android">Subsonic</a> for Android</li>
                    <li><a href="#winphone">Subsonic</a> for Windows Phone 7</li>
                    <li><a href="#subair">SubAir</a> for desktops</li>
                    <li><a href="#thumper">Thumper</a> for Mac</li>
                </ul>
                <ul class="stars column-right">
                    <li><a href="#isub">iSub</a> for iPhone/iPad</li>
                    <li><a href="#zsubsonic">Z-Subsonic</a> for iPhone/iPad</li>
                    <li><a href="#subgadget">SubGadget</a> for Windows</li>
                    <li><a href="#periscope">Periscope</a> for Windows</li>
                </ul>
            </div>

            <p>
                Please note that most of the apps are made by third-party developers, and are not maintained by
                the Subsonic project. Some apps are commercial, while some are available for free.
            </p>
            <p>Also note that after a 30-day trial period you need a license key to use the apps.
                You get a license key by giving a donation to the Subsonic project. The license never expires and is
                valid for all current and future apps. By donating you also get other benefits; see info box on the
                right.
            </p>
            <p>
                Interested in making your own Subsonic app? Check out the <a href="api.jsp">API</a>.
            </p>

            <div class="featureitem">
                <a name="android"></a>

                <div class="heading">Subsonic for Android <a href="#top" class="backtotop" title="Top"><img src="inc/img/top.gif" alt="" height="16" width="16"/></a></div>
                <div class="content">
                    <div class="wide-content">
                        <p>
                            <img src="inc/img/android.png" alt="Android" class="img-left"/>
                            Stream and download music from your home computer to your Android phone.
                            All your music - anywhere, anytime!
                        </p>
                        <p>
                            Developed and maintained by Sindre Mehus, the author of Subsonic. Available for free
                            on Android Market, or download it <a href="http://sourceforge.net/projects/subsonic/files/android">here</a>.
                        </p>

                        <p>
                            Supports streaming, downloading, playlists, album art and searching. For improved performance, music you have
                            listened to is cached on the phone. An offline mode is also available for when you are outside 3G/Wi-Fi coverage.
                        </p>

                        <a href="inc/img/screenshots/screen12.png"><img src="inc/img/screenshots/thumb12.png" alt="" style="padding:3px;padding-left:40px;padding-right:20px"/></a>
                        <a href="inc/img/screenshots/screen13.png"><img src="inc/img/screenshots/thumb13.png" alt="" style="padding:3px;padding-right:20px"/></a>
                        <a href="inc/img/screenshots/screen14.png"><img src="inc/img/screenshots/thumb14.png" alt="" style="padding:3px;padding-right:20px"/></a>
                        <a href="inc/img/screenshots/screen15.png"><img src="inc/img/screenshots/thumb15.png" alt="" style="padding:3px"/></a>

                    </div>
                </div>
            </div>

            <div class="featureitem">
                <a name="isub"></a>

                <div class="heading">iSub for iPhone/iPad <a href="#top" class="backtotop" title="Top"><img src="inc/img/top.gif" alt="" height="16" width="16"/></a></div>
                <div class="content">
                    <div class="wide-content">
                        <p>
                            <img src="inc/img/appstore.png" alt="App Store" class="img-left"/>
                            <a href="http://isubapp.com/">iSub</a> is an iPhone/iPad app developed by Ben Baron, and is
                            available for purchase on the <a href="http://itunes.apple.com/us/app/isub-music-streamer/id362920532?mt=8">App&nbsp;Store</a>.
                        </p>
                        <a href="inc/img/screenshots/screen20.png"><img src="inc/img/screenshots/thumb20.png" alt="" style="padding:15px;padding-left:80px"/></a>
                        <a href="inc/img/screenshots/screen21.png"><img src="inc/img/screenshots/thumb21.png" alt="" style="padding:15px"/></a>
                        <a href="inc/img/screenshots/screen22.png"><img src="inc/img/screenshots/thumb22.png" alt="" style="padding:15px"/></a>

                        <ul class="list">
                            <li>Full support for creating and managing on-the-go playlists.</li>
                            <li>Automatic full song caching for the best network performance with no music drop outs.</li>
                            <li>Manually cache songs (Wifi sync) to listen offline, like on an airplane.</li>
                            <li>Caching of all browsed directories for speedy browsing.</li>
                            <li>Retina display support for beautiful album art while browsing and in the player.</li>
                            <li>Skipping within tracks, even while they are streaming.</li>
                            <li>Resuming music when interrupted by a call or text or when closing the app using the home button while a song is playing.</li>
                            <li>Each music folder is a playlist automatically so when you select a track from an album it will continue to play the rest of the tracks in that album.</li>
                            <li>Shuffle, Repeat 1, and Repeat All when playing an album.</li>
                            <li>Detailed track information by tapping the cover art.</li>
                            <li>Jukebox mode to use your device as a remote control for Subsonic for listening to music around the house.</li>
                        </ul>
                        <p>
                            Support: <a href="mailto:support@isubapp.com">support@isubapp.com</a>
                        </p>
                        <a href="http://itunes.apple.com/us/app/isub-music-streamer/id362920532?mt=8"><img src="inc/img/available_on_appstore.png" alt="" class="img-center"/></a>

                    </div>
                </div>
            </div>

            <div class="featureitem">
                <a name="zsubsonic"></a>

                <div class="heading">Z-Subsonic for iPhone/iPad <a href="#top" class="backtotop" title="Top"><img src="inc/img/top.gif" alt="" height="16" width="16"/></a></div>
                <div class="content">
                    <div class="wide-content">
                        <p>
                            <img src="inc/img/appstore.png" alt="App Store" class="img-left"/>
                            <a href="http://z-subsonic.com/">Z-Subsonic</a> is an iPhone/iPad app developed by Olusola Abiodun, and is
                            available for purchase on the <a href="http://itunes.apple.com/us/app/z-subsonic/id358344265?mt=8">App Store</a>.
                        </p>
                        <a href="inc/img/screenshots/screen17.png"><img src="inc/img/screenshots/thumb17.png" alt="" style="margin-left:30px;padding:3px"/></a>
                        <a href="inc/img/screenshots/screen18.png"><img src="inc/img/screenshots/thumb18.png" alt="" style="padding:3px"/></a>
                        <a href="inc/img/screenshots/screen19.png"><img src="inc/img/screenshots/thumb19.png" alt="" style="padding:3px"/></a>

                        <ul class="list">
                            <li> Access your entire music library from anywhere on your iPhone or iPod regardless of the size of your music collection.</li>
                            <li> Play song formats that the iPhone/iPod will not normally play e.g. wma, flac, ogg.</li>
                            <li> Caches all queued songs as quickly as possible once Wi-fi connection is detected.</li>
                            <li> Browse and manage cached songs by artist/album. Delete entire artist/album from cache at once.</li>
                            <li> Delete or change song priority on the Now Playing list while songs are playing.</li>
                            <li> Double buffering: upcoming song starts to download before the currently playing song finishes.</li>
                            <li> Seeking for fully cached songs.</li>
                            <li> Load playlists stored on the Subsonic server.</li>
                            <li> Multiple server configuration.</li>
                            <li> Random playlist generation using entire song collection.</li>
                            <li> Repeat/shuffle/skip songs.</li>
                            <li> SSL support with prompt to authorize self-signed certificates.</li>
                            <li> Full caching. Z-Subsonic can be used fully disconnected with previously streamed music.</li>
                            <li> Favorites.
                        </ul>

                        <p>
                            See the <a href="http://www.youtube.com/watch?v=yFzM7-rfINM">video on YouTube</a>.
                        </p>

                        <p>
                            Support: <a href="mailto:helpdesk@z-subsonic.com">helpdesk@z-subsonic.com</a> &ndash;
                            Forum: <a href="http://z-subsonic.com/z-subsonic-forum">http://z-subsonic.com/z-subsonic-forum</a>
                        </p>
                        <a href="http://itunes.apple.com/us/app/z-subsonic/id358344265?mt=8"><img src="inc/img/available_on_appstore.png" alt="" class="img-center"/></a>


                    </div>
                </div>
            </div>

            <div class="featureitem">
                <a name="winphone"></a>

                <div class="heading">Subsonic for Windows Phone 7 <a href="#top" class="backtotop" title="Top"><img src="inc/img/top.gif" alt="" height="16" width="16"/></a></div>
                <div class="content">
                    <div class="wide-content">
                        <p>
                            <img src="inc/img/windows-marketplace.png" alt="Windows Marketplace" class="img-left"/>
                            <a href="http://www.avzuylen.com/subsonic-music-streamer.aspx">Subsonic Music Streamer</a> is a Windows Phone 7 app developed by Anton Van Zuylen, and is
                            available for purchase in the <a href="http://redirect.zune.net/redirect?type=phoneApp&id=2eb445a5-ca04-e011-9264-00237de2db9e&source=WP7applist">Windows Marketplace</a>.
                        </p>
                            <a href="inc/img/screenshots/screen23.png"><img src="inc/img/screenshots/thumb23.png" alt="" style="margin-left:30px;padding:3px"/></a>
                        <a href="inc/img/screenshots/screen24.png"><img src="inc/img/screenshots/thumb24.png" alt="" style="padding:3px"/></a>
                        <a href="inc/img/screenshots/screen25.png"><img src="inc/img/screenshots/thumb25.png" alt="" style="padding:3px"/></a>

                        <ul class="list">
                            <li>Always have access to your entire music collection without the need for any third party server or subscription.</li>
                            <li>Supports all popular formats (WMA, FLAC, MP3, AAC, OCG etc).</li>
                            <li>Keep listening when you have no cellular coverage due to full local storage including cover art, lyrics and info.</li>
                            <li>Manage your local stored music in a convenient way.</li>
                            <li>Control the quality of the music by controlling the streaming bit-rate.</li>
                            <li>Create playlists on the device which can contain songs from different servers simultaneously.</li>
                            <li>Add easily your newest music, your recently played albums, or your frequently played albums.</li>
                            <li>Add random albums.</li>
                            <li>Full search support.</li>
                            <li>Keep playing behind locked screen.</li>
                            <li>Full integration with the Music and Video hub.</li>
                            <li>Automatic storage management of local stored songs.</li>
                            <li>Allowing to lock specific local songs.</li>
                        </ul>

                        <p>
                            This application focuses on fast and easy handling with two main pages:
                            "Now playing" where you see and control all what is currently playing including cover art and lyrics; and
                            "Add to now playing" providing different methods for adding albums and songs to your playlist.
                            Start the application and the music is playing (just one click)!
                        </p>
                        <p>
                            Support: <a href="mailto:anton@avzuylen.com">anton@avzuylen.com</a>
                        </p>
                        <p>Go get it on the <a href="http://redirect.zune.net/redirect?type=phoneApp&id=2eb445a5-ca04-e011-9264-00237de2db9e&source=WP7applist">Windows Phone 7 Marketplace</a>!</p>

                    </div>
                </div>
            </div>

            <div class="featureitem">
                <a name="subair"></a>

                <div class="heading">SubAir <a href="#top" class="backtotop" title="Top"><img src="inc/img/top.gif" alt="" height="16" width="16"/></a></div>
                <div class="content">
                    <div class="wide-content">
                        <p>
                            <img src="inc/img/air.png" alt="Adobe AIR" class="img-left"/>
                            <a href="http://www.nonpixel.com/subair/">SubAir</a> is a rich desktop application for Subsonic implemented with Adobe&copy; AIR.
                            Works with Windows, Mac and Linux.
                        </p>
                        <a href="inc/img/screenshots/screen16.png"><img src="inc/img/screenshots/thumb16.png" alt="" class="img-center"/></a>
                        <p>
                            Developed and maintained by <a href="http://www.nonpixel.com/">Jim Resnowski</a>, and
                            <a href="http://www.nonpixel.com/subair/">provided free or charge</a>.
                        </p>
                    </div>
                </div>
            </div>

            <div class="featureitem">
                <a name="thumper"></a>

                <div class="heading">Thumper <a href="#top" class="backtotop" title="Top"><img src="inc/img/top.gif" alt="" height="16" width="16"/></a></div>
                <div class="content">
                    <div class="wide-content">
                        <p>
                            <a href="http://thumperapp.com/">Thumper</a> is a native Subsonic client for Mac.
                        </p>
                        <a href="inc/img/screenshots/screen27.png"><img src="inc/img/screenshots/thumb27.png" alt="" class="img-center"/></a>

                        <ul class="list">
                            <li>A Native Mac Application - designed on the Mac, for the Mac</li>
                            <li>Local Caching of Library Information, Audio, and Album Art</li>
                            <li>Browse Artists, Albums, and Songs</li>
                            <li>Quick Artist Filter</li>
                            <li>Playlists</li>
                            <li>Search</li>
                            <li>Current Playlist Persistence</li>
                            <li>Mac Media Key Integration</li>
                            <li>Keyboard Shortcuts</li>
                            <li>Last.fm Integration</li>
                        </ul>
                        <p>
                            Requires Mac OS X 10.6 or greater. Does not support Subsonic's downsampling (reducing the audio bitrate).
                        </p>
                        <p>
                            <a href="http://itunes.apple.com/us/app/thumper/id436422990?mt=12&uo=4"><img class="img-center" 
                                    src="inc/img/mac-appstore.jpg" alt="Thumper"/></a>
                        </p>
                    </div>
                </div>
            </div>

            <div class="featureitem">
                <a name="subgadget"></a>

                <div class="heading">SubGadget <a href="#top" class="backtotop" title="Top"><img src="inc/img/top.gif" alt="" height="16" width="16"/></a></div>
                <div class="content">
                    <div class="wide-content">
                        <p>
                            <img src="inc/img/windows7.png" alt="Windows 7" class="img-left"/>
                            <a href="http://code.google.com/p/subgadget/">SubGadget</a> is a Windows Sidebar Gadget for Subsonic. Works with Windows 7 and Vista.
                        </p>
                        <img src="inc/img/screenshots/screen26.png" alt="" class="img-center"/>
                        <p>
                            Developed and maintained by Scott Mark, and <a href="http://code.google.com/p/subgadget/">provided free or charge</a>.
                        </p>
                    </div>
                </div>
            </div>

            <div class="featureitem">
                <a name="periscope"></a>

                <div class="heading">Periscope Player<a href="#top" class="backtotop" title="Top"><img src="inc/img/top.gif" alt="" height="16" width="16"/></a></div>
                <div class="content">
                    <div class="wide-content">
                        <p>
                            <img src="inc/img/windows7.png" alt="Windows 7" class="img-left"/>
                            <a href="http://periscopeplayer.com/">Periscope Player</a> is a Windows desktop application for Subsonic.
                        </p>
                        <a href="inc/img/screenshots/screen28.png"><img src="inc/img/screenshots/thumb28.png" alt="" class="img-center"/></a>
                        <p>
                            Developed and maintained by <a href="mailto:support@periscopeplayer.com">Matt Campanile</a>.  Free trial version, $1.99 for
                            a lifetime license.
                        </p>
                    </div>
                </div>
            </div>

        </div>

        <div id="side-col">
            <%@ include file="google-translate.jsp" %>
            <%@ include file="donate.jsp" %>
        </div>

        <div class="clear">
        </div>
    </div>
    <hr/>
    <%@ include file="footer.jsp" %>
</div>

</body>
</html>
