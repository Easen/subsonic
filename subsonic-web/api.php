<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<?php
    $current = 'api';
    include("header.php");
?>

<body>

<a name="top"/>

<div id="container">
    <?php include("menu.php");?>

    <div id="content">
        <div id="main-col">
            <h1>Subsonic API</h1>
            <p>
                The Subsonic API allows anyone to build their own programs using Subsonic as the server, whether they're
                on the web, the desktop or on mobile devices. As an example, the Subsonic Android app is built using the
                Subsonic API.
            </p>

            <h2 class="div">Introduction</h2>
            <p>
                The Subsonic API allows you to call methods that respond in <a href="http://en.wikipedia.org/wiki/Representational_State_Transfer">REST</a> style xml.
                Individual methods are detailed below.
            </p>
            <p>
                Please note that all methods require the following parameters for authentication and protocol verification:
            </p>
            <p>
                <code>u</code> - The username.<br/>
                <code>p</code> - The password, either in clear text or hex-encoded with a "enc:" prefix.<br/>
                <code>v</code> - The protocol version implemented by the client, i.e., the version of the
                <code>subsonic-rest-api.xsd</code> schema used (see below).
            </p>
            <p>
                For example:
            </p>
            <p>
                <code>http://your-server/rest/getIndexes.view?u=joe&p=sesame&v=1.0.0</code>, or<br/>
                <code>http://your-server/rest/getIndexes.view?u=joe&p=enc:736573616d65&v=1.0.0</code>
            </p>
            <p>
                Also note that UTF-8 should be used when sending parameters to API methods. The XML returned
                will also be encoded with UTF-8.
            </p>

            <p>
                All methods (except those that return binary data) returns XML documents conforming to the
                <code>subsonic-rest-api.xsd</code> schema. This schema (as well as example XML documents) can be found
                at <code>http://your-server/xsd/</code>
            </p>

            <h2 class="div">Error handling</h2>
            <p>
                If a method fails it will return an error code and message in an <code>&lt;error&gt;</code> element.
                In addition, the <code>status</code> attribute of the <code>&lt;subsonic-response&gt;</code> root element
                will be set to <code>failed</code> instead of <code>ok</code>. For example:
            </p>

            <pre>
   &lt;?xml version="1.0" encoding="UTF-8"?&gt;
   &lt;subsonic-response xmlns="http://subsonic.sourceforge.net/restapi"
                      status="failed" version="1.0.0"&gt;
       &lt;error code="11" message="Wrong username or password"/&gt;
   &lt;/subsonic-response&gt;

            </pre>

            <h2 class="div">ping</h2>
            <p>
                <code>http://your-server/rest/ping.view</code>
            </p>
            <p>
                Used to test connectivity with the server.  Takes no extra parameters.
            </p>
            <p>
                Returns an empty <code>&lt;subsonic-response&gt;</code> element on success.
            </p>

            <h2 class="div">getMusicFolders</h2>
            <p>
                <code>http://your-server/rest/getMusicFolders.view</code>
            </p>
            <p>
                Returns all configured music folders. Takes no extra parameters.
            </p>
            <p>
                Returns a <code>&lt;subsonic-response&gt;</code> element with a nested <code>&lt;musicFolders&gt;</code>
                element on success.
            </p>

            <h2 class="div">getIndexes</h2>
            <p>
                <code>http://your-server/rest/getIndexes.view</code>
            </p>
            <p>
                Returns an indexed structure of all artists.
            </p>
            <table width="100%" class="bottomspace">
                <tr>
                    <th class="param-heading">Parameter</th>
                    <th class="param-heading">Required</th>
                    <th class="param-heading">Default</th>
                    <th class="param-heading">Comment</th>
                </tr>
                <tr class="table-altrow">
                    <td><code>musicFolderId</code></td>
                    <td>No</td>
                    <td></td>
                    <td>If specified, only return artists in the music folder with the given ID.</td>
                </tr>
            </table>
            <p>
                Returns a <code>&lt;subsonic-response&gt;</code> element with a nested <code>&lt;indexes&gt;</code>
                element on success.
            </p>

            <h2 class="div">getMusicDirectory</h2>
            <p>
                <code>http://your-server/rest/getMusicDirectory.view</code>
            </p>
            <p>
                Returns a listing of all files in a music directory. Typically used to get list of albums for an artist,
                or list of songs for an album.
            </p>
            <table width="100%" class="bottomspace">
                <tr>
                    <th class="param-heading">Parameter</th>
                    <th class="param-heading">Required</th>
                    <th class="param-heading">Default</th>
                    <th class="param-heading">Comment</th>
                </tr>
                <tr class="table-altrow">
                    <td><code>id</code></td>
                    <td>Yes</td>
                    <td></td>
                    <td>A string which uniquely identifies the music folder. Obtained by calls to getIndexes or getMusicDirectory.</td>
                </tr>
            </table>
            <p>
                Returns a <code>&lt;subsonic-response&gt;</code> element with a nested <code>&lt;musicFolder&gt;</code>
                element on success.
            </p>

            <h2 class="div">download</h2>
            <p>
                <code>http://your-server/rest/download.view</code>
            </p>
            <p>
                Downloads a given music file.
            </p>
            <table width="100%" class="bottomspace">
                <tr>
                    <th class="param-heading">Parameter</th>
                    <th class="param-heading">Required</th>
                    <th class="param-heading">Default</th>
                    <th class="param-heading">Comment</th>
                </tr>
                <tr class="table-altrow">
                    <td><code>id</code></td>
                    <td>Yes</td>
                    <td></td>
                    <td>A string which uniquely identifies the file to download. Obtained by calls to getMusicDirectory.</td>
                </tr>
            </table>
            <p>
                Returns binary data on success.
            </p>

            <h2 class="div">getCoverArt</h2>
            <p>
                <code>http://your-server/rest/getCoverArt.view</code>
            </p>
            <p>
                Returns a cover art image.
            </p>
            <table width="100%" class="bottomspace">
                <tr>
                    <th class="param-heading">Parameter</th>
                    <th class="param-heading">Required</th>
                    <th class="param-heading">Default</th>
                    <th class="param-heading">Comment</th>
                </tr>
                <tr class="table-altrow">
                    <td><code>id</code></td>
                    <td>Yes</td>
                    <td></td>
                    <td>A string which uniquely identifies the cover art file to download. Obtained by calls to getMusicDirectory.</td>
                </tr>
                <tr>
                    <td><code>size</code></td>
                    <td>No</td>
                    <td></td>
                    <td>If specified, scale image to this size.</td>
                </tr>
            </table>
            <p>
                Returns binary data on success.
            </p>

            <h2 class="div">createUser</h2>
            <p>
                <code>http://your-server/rest/createUser.view</code>
            </p>
            <p>
                Creates a new Subsonic user, using the following parameters:
            </p>
            <table width="100%" class="bottomspace">
                <tr>
                    <th class="param-heading">Parameter</th>
                    <th class="param-heading">Required</th>
                    <th class="param-heading">Default</th>
                    <th class="param-heading">Comment</th>
                </tr>
                <tr class="table-altrow">
                    <td><code>username</code></td>
                    <td>Yes</td>
                    <td></td>
                    <td>The name of the new user.</td>
                </tr>
                <tr>
                    <td><code>password</code></td>
                    <td>Yes</td>
                    <td></td>
                    <td>The password of the new user, either in clear text of hex-encoded (see above).</td>
                </tr>
                <tr class="table-altrow">
                    <td><code>ldapAuthenticated</code></td>
                    <td>No</td>
                    <td>false</td>
                    <td>Whether the user is authenicated in LDAP.</td>
                </tr>
                <tr>
                    <td><code>adminRole</code></td>
                    <td>No</td>
                    <td>false</td>
                    <td>Whether the user is administrator.</td>
                </tr>
                <tr class="table-altrow">
                    <td><code>settingsRole</code></td>
                    <td>No</td>
                    <td>true</td>
                    <td>Whether the user is allowed to change settings and password.</td>
                </tr>
                <tr>
                    <td><code>streamRole</code></td>
                    <td>No</td>
                    <td>true</td>
                    <td>Whether the user is allowed to play files.</td>
                </tr>
                <tr class="table-altrow">
                    <td><code>jukeboxRole</code></td>
                    <td>No</td>
                    <td>false</td>
                    <td>Whether the user is allowed to play files in jukebox mode.</td>
                </tr>
                <tr>
                    <td><code>downloadRole</code></td>
                    <td>No</td>
                    <td>false</td>
                    <td>Whether the user is allowed to download files.</td>
                </tr>
                <tr class="table-altrow">
                    <td><code>uploadRole</code></td>
                    <td>No</td>
                    <td>false</td>
                    <td>Whether the user is allowed to upload files.</td>
                </tr>
                <tr>
                    <td><code>playlistRole</code></td>
                    <td>No</td>
                    <td>false</td>
                    <td>Whether the user is allowed to create and delete playlists.</td>
                </tr>
                <tr class="table-altrow">
                    <td><code>coverArtRole</code></td>
                    <td>No</td>
                    <td>false</td>
                    <td>Whether the user is allowed to change cover art and tags.</td>
                </tr>
                <tr>
                    <td><code>commentRole</code></td>
                    <td>No</td>
                    <td>false</td>
                    <td>Whether the user is allowed to create and edit comments and ratings.</td>
                </tr>
                <tr class="table-altrow">
                    <td><code>podcastRole</code></td>
                    <td>No</td>
                    <td>false</td>
                    <td>Whether the user is allowed to administrate Podcasts.</td>
                </tr>
            </table>


            <p>
                Returns an empty <code>&lt;subsonic-response&gt;</code> element on success.
            </p>

        </div>

        <div id="side-col">

            <?php include("donate.php"); ?>
            <?php include("merchandise.php"); ?>

        </div>

        <div class="clear">
        </div>
    </div>
    <hr/>
    <?php include("footer.php"); ?>
</div>


</body>
</html>