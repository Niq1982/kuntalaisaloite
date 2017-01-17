<#import "/spring.ftl" as spring />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/wysiwyg-editor.ftl" as editor />

<#escape x as x?html>


<#--
 * Layout parameters for HTML-title and navigation.
-->
    <@l.main "page.notification.moderation" >

    <h1><@u.message "page.notification.moderation" /></h1>

    <div class="msg-block">
        <p>Tältä sivulta voit muokata tiedotetekstiä, mikä näkyy jokaiselle käyttäjälle palvelun yläreunassa.</p>
        <p>Esikatselu päivittyy automaattisesti kun muokkaat tekstikenttää. Muutokset tallentuvat käyttäjille vasta painaessasi tallenna.</p>
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
            <input id="notificationEnabled" type="checkbox" checked="${notificationStatus.enabled?string("checked", "")}" name="enabled"/>
        </label>

        <br/>

        <input type="submit" value="Tallenna">





    </form>


    </@l.main>

<script>

    $(document).ready(function() {

        $("#wrapper").prepend(
                    <#noescape>'<div class="debug-ribbon top fixed ribbon-red" style="position: fixed;"><div class="container"><div class="container"><span id="esikatselu_teksti"></span>&nbsp;<a id="esikatselu_url" href="#" target="_blank"></a></div></div></div>'</#noescape>);

        var setPreview = function (text, url, linkText) {
            $("#esikatselu_teksti").html($("<div>").text(text).html());
            $("#esikatselu_url")
                    .html($("<div>").text(linkText).html())
                    .attr("href", $("<div>").text(url).html());
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