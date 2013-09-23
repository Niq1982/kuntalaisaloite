<#escape x as x?html>
<!DOCTYPE HTML>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

<style type="text/css">

    html, body {
        background:#f0f0f0; margin:0; padding:0;
        font-family: "PT Sans", "Trebuchet MS", Helvetica, sans-serif;
        color:#111;
    }

    .container {
        width:880px;
        margin:100px auto 2em auto;
        padding:40px;
        
        background:#fff;
    }
    
    h1 {
        color: #111111;
        font-size: 2em;
        font-weight: normal;
        line-height: 1.2em;
        margin:0 0 0.67em 0;
    }
    
    p { margin-bottom:1em; }
    
    input[type="text"] {
        height:30px;
        line-height:30px;
        margin-bottom:1em;
        padding:0 0.3em;
    } 
    
    button, #formsubmit {
        height:36px;
        line-height:36px;
        padding:0 12px;
        text-align:center;
        border:1px solid #ccc;
        
    
        background: #e2e2e2;
        background: linear-gradient(top,  #fff, #e2e2e2);
        background: -ms-linear-gradient(top,  #fff, #e2e2e2);
        background: -webkit-gradient(linear, left top, left bottom, from(#fff), to(#e2e2e2));
        background: -moz-linear-gradient(top,  #fff, #e2e2e2);
        background: -o-linear-gradient(top,  #fff, #e2e2e2);
    }
    
    .push { margin-left:1em; }
    
    .hr {
        border-top:1px solid #ccc;
        height:0; line-height:0;
        margin:1em 0;
    }

</style>


</head>

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

<div class="container">

<h1>T&auml;m&auml;n sivun tilalla oikeassa Kuntalaisaloite.fi-palvelussa on vahvan s&auml;hk&ouml;isen tunnistautumisen palvelu <a href="http://www.suomi.fi/suomifi/tyohuone/yhteiset_palvelut/verkkotunnistaminen_ja_maksaminen_vetuma/">Vetuma</a></h1>

<p>Testipalvelussa vahvan s&auml;hk&ouml;inen tunnistautuminen on korvattu t&auml;ll&auml; sivulla, jonka avulla voit sy&ouml;tt&auml;&auml; vapaasti V&auml;est&ouml;tietoj&auml;rjestelm&auml;st&auml; saatavaa tietoa.</p>
<p>Huomaathan, ett&auml; voit antaa henkil&ouml;tunnukseksi mink&auml; tahansa numerosarjan, kunhan siin&auml; on sama merkkim&auml;&auml;r&auml; kuin henkil&ouml;tunnuksessa. Antamaasi henkil&ouml;tunnusta ei n&auml;ytet&auml; palvelussa eik&auml; sit&auml; tallenneta selkokielisen&auml;. Jos haluat my&ouml;hemmin palata yll&auml;pit&auml;m&auml;&auml;n tekem&auml;&auml;si aloitetta tai aloitetta, johon liityt vastuuhenkil&ouml;ksi, tulee sinun k&auml;ytt&auml;&auml; aina samaa henkil&ouml;tunnusta.</p>

<div class="hr"></div>

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
    <input type="text" name="last_name" value="Meikalainen"/>
    <p>
        <select name="municipalityCode">
            <option value="">Ei kuntaa (Turvakielto)</option>
            <option value="-3">Palvelulle tuntematon kuntakoodi</option>
            <#list municipalities as municipality>
                <option value="${municipality.id}">${municipality.nameFi}</option>
            </#list>
        </select>
    </p>
    <input id="hetu" type="text" name="EXTRADATA" value="HETU=010190-0000"/><a href="#" class="push" onclick="return randomSSN();">random</a>

    <br/><label><input type="checkbox" checked="checked" name="fi" value="1"/> Suomen kansalainen</label><br>
    <p>
        Kirjautumiskoodi:
        <select name="STATUS"/>
            <option value="SUCCESSFUL">SUCCESSFUL</option>
            <option value="CANCELLED">CANCELLED</option>
            <option value="REJECTED">REJECTED</option>
            <option value="ERROR">ERROR</option>
            <option value="FAILURE">FAILURE</option>
        </select>
    </p>
    <input id="formsubmit" type="submit" value="Leiki vetumaa"/>

</form>

</div>

</body>
</html>

</#escape>