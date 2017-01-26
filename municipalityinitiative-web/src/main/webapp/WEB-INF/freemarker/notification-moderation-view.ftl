<#import "/spring.ftl" as spring />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/wysiwyg-editor.ftl" as editor />

<#escape x as x?html>


<#--
 * Layout parameters for HTML-title and navigation.
-->
    <@l.main "page.notification.moderation" >

    <div id="preview">
        <#include "components/notification.ftl"/>
    </div>

    <h1><@u.message "page.notification.moderation" /></h1>

    <div class="msg-block">
        <p>Tältä sivulta voit muokata tiedotetekstiä, mikä näkyy jokaiselle käyttäjälle palvelun yläreunassa.</p>
        <p>Paina "Esikatsele", kun haluat nähdä tuloksia.</p>
    </div>

    <form action="${springMacroRequestContext.requestUri}" method="POST" >
        <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>
        <table style="width:100%">
            <tr>
                <td width="50%">

                    <h1>Suomi</h1>
                    <textarea name="fi" id="notificationFi" style="width:90%">${notificationStatus.fi!""}</textarea>
                    <input name="urlFi" placeholder="www.google.fi" id="notificationUrlFi" value="${notificationStatus.urlFi!""}"</input>
                    <input name="urlFiText" placeholder="Linkin teksti" id="notificationUrlFiText" value="${notificationStatus.urlFiText!""}"</input>
                    <a href="#" id="fiPreview">Esikatsele</a>
                </td>
                <td width="50%">

                    <h1>Ruotsi</h1>
                    <textarea name="sv" id="notificationSv" style="width:90%">${notificationStatus.sv!""}</textarea>
                    <input name="urlSv" placeholder="www.google.fi" id="notificationUrlSv" value="${notificationStatus.urlSv!""}"</input>
                    <input name="urlSvText" placeholder="Linkin teksti" id="notificationUrlSvText" value="${notificationStatus.urlSvText!""}"</input>
                    <a href="#" id="svPreview">Esikatsele</a>
                </td>
            </tr>
        </table>

        <br/>
        <label for="notificationEnabled">
            Tiedote näkyvissä
            <input id="notificationEnabled" type="checkbox" ${notificationStatus.enabled?string('checked="checked"', "")}" name="enabled"/>
        </label>

        <br/>

        <input type="submit" value="Tallenna">





    </form>


    </@l.main>

<script>

    $(document).ready(function() {


        $("#header").prepend('<div id="preview"/>');

        var setPreview = function (text, url, linkText) {

            $.ajax("/generate-notification?text=" + text + "&link=" + url + "&linkText=" + linkText,
                    {
                        success: function (data) {
                            console.log(data);
                            $("#preview").html(data);
                        }
                    });
        };


        $("#fiPreview").click(function (e) {
            setPreview($("#notificationFi").val(),
                    $("#notificationUrlFi").val(),
                    $("#notificationUrlFiText").val());
            e.preventDefault();
        });

        $("#svPreview").click(function (e) {
            setPreview($("#notificationSv").val(),
                    $("#notificationUrlSv").val(),
                    $("#notificationUrlSvText").val());
            e.preventDefault();
        });

    });

</script>

</#escape>