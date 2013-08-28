<#escape x as x?html>
<!DOCTYPE HTML>
<html>
<head></head>
<body>

<#function rand min max>
  <#local now = .now?long?c />
  <#local randomNum = _rand +
    ("0." + now?substring(now?length-1) + now?substring(now?length-2))?number />
  <#if (randomNum > 1)>
    <#assign _rand = randomNum % 1 />
  <#else>
    <#assign _rand = randomNum />
  </#if>
  <#return (min + ((max - min) * _rand))?round />
</#function>
<#assign _rand = 0.36 />
<#assign randomSSN = rand(1000, 9999)?c />

<script>

function randomSSN() {
    document.getElementById('hetu').value='HETU=${rand(10,28)}${rand(10,12)}${rand(10,90)}-${randomSSN}';
    return false;
}

</script>

<h2>Tämän sivun tilalla tulee olemaan <a href="http://www.suomi.fi/suomifi/tyohuone/yhteiset_palvelut/verkkotunnistaminen_ja_maksaminen_vetuma/">Vetuma-palvelu</a>, jolla kirjaudutaan palveluun pankkitunnuksilla.</h2>
<p>Testivaiheessa Vetuma on korvattu tällä sivulla, jonka avulla voit syöttää vapaasti Vetumasta saatavaa tietoa.<br/>
Huomaathan, että voit myös antaa minkä tahansa henkilötunnuksen vapaasti. Antamaasi henkilötunnusta ei näytetä palvelussa eikä sitä tallenneta selkokielisenä. <br/>
Aloitteet ja osallistumiset liitetään yhteen henkilötunnukseen. Jos haluat palata luomasi aloitteen hallintaan myöhemmin, joudut käyttämään samaa henkilötunnusta kuin aloitetta luodessa.</p>

<p>Ä, ö ja å -kirjaimet nimessä aiheuttavat tällä hetkellä epäonnistuneen kirjautumisen.</p>

<form action="/vetumamockreturn" method="post">

    <input type="hidden" name="RCVID" value="${vetumaRequest.RCVID}"/>
    <input type="hidden" name="APPID" value="${vetumaRequest.APPID!'kansalaisaloite.fi'}"/>
    <input type="hidden" name="TIMESTMP" value="${vetumaRequest.TIMESTMP}"/>
    <input type="hidden" name="SO" value="${vetumaRequest.SO}"/>
    <input type="hidden" name="LG" value="${vetumaRequest.LG}"/>
    <input type="hidden" name="RETURL" value="${vetumaRequest.RETURL}"/>
    <input type="hidden" name="CANURL" value="${vetumaRequest.CANURL}"/>
    <input type="hidden" name="ERRURL" value="${vetumaRequest.ERRURL}"/>
    <input type="hidden" name="TRID" value="${vetumaRequest.TRID!''}"/>

    <input type="text" name="first_name" value="Matti Petteri"/><br>
    <input type="text" name="last_name" value="Meikalainen"/><br/>
    <select name="municipalityCode">
    <option value="">Ei kuntaa (Turvakielto)</option>
    <option value="-3">Palvelulle tuntematon kuntakoodi</option>
    <#list municipalities as municipality>
        <option value="${municipality.id}">${municipality.nameFi}</option>
    </#list>
    </select>
    <br>
    <input id="hetu" type="text" name="EXTRADATA" value="HETU=010190-0000"/><a href="#" onclick="return randomSSN();">random</a>

    <br/><label><input type="checkbox" checked="checked" name="fi" value="1"/> Suomen kansalainen</label><br>
    <br/>
    Kirjautumiskoodi:
    <select name="STATUS"/>
        <option value="SUCCESSFUL">SUCCESSFUL</option>
        <option value="CANCELLED">CANCELLED</option>
        <option value="REJECTED">REJECTED</option>
        <option value="ERROR">ERROR</option>
        <option value="FAILURE">FAILURE</option>
    </select>
    <br/>
    <input id="formsubmit" type="submit" value="Leiki vetumaa"/>

</form>

</body>
</html>

</#escape>