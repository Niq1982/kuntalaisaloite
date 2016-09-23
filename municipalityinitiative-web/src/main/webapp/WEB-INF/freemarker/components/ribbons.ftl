<#escape x as x?html>

<#--
 * ribbons.ftl contains top and bottom ribbons to distinguish different test environments, 
 * normally there are no visible ribbons for the production environment 
 *
 * Assign ribbon colors by variables "blueColor" and "redColor"
 * class "ribbon-default" produces blue background
 * class "ribbon-red" produces red background
-->


<#macro assignSiteVars>

    <#-- For testing ribbons locally:
    <#assign prodTestSite>${localSite}</#assign>
    <#assign localSite>https://localhost:8443x</#assign>
   -->
    
    <#assign blueColor>ribbon-default</#assign> <#-- BLUE gradient -->
    <#assign redColor>ribbon-red</#assign> <#-- RED gradient -->
    <#assign turquoiseColor>ribbon-turquoise</#assign> <#-- TURQUOISE gradient for PROD SITE's infos -->

    <#assign ribbonColor>${(appEnvironment == "test")?string(redColor,blueColor)}</#assign>
</#macro>


<#macro topRibbon>
    <@assignSiteVars/>

    <#if (appEnvironment == "prod")>
        <#-- no ribbon in production -->

    <#else>
    	<#if (appEnvironment == "test")>
            <div class="test-padge"> </div>
        </#if>
        <div class="debug-ribbon top fixed ${ribbonColor}">
        	
            <div class="container">
                <#if (appEnvironment == "test")>

                    <#if locale == "sv">
                        Det här är en testversion av tjänsten invånarinitiativ.fi. För att få e-postmeddelandena som skickas från tjänsten ska du ge din aktuella e-postadress. Initiativen som skapas i tjänsten och andra uppgifter som matas in raderas regelbundet utan någon särskild anmälning. Initiativen skickas inte till kommunerna.
                        Skicka dina kommentarer om testversionen till <a href="mailto:kuntalaisaloite.om@om.fi">kuntalaisaloite.om@om.fi</a>
                    <#else>
                        T&auml;m&auml; on kuntalaisaloitepalvelun testiversio. Palvelu l&auml;hett&auml;&auml; sinulle s&auml;hk&ouml;posteja, joten annathan toimivan s&auml;hk&ouml;postiosoitteesi. Testipalvelussa tehdyt aloitteet ja muut tiedot poistetaan s&auml;&auml;nn&ouml;llisesti ilman erillist&auml; ilmoitusta, eiv&auml;tk&auml; aloitteet v&auml;lity kunnalle.<br/>
                        Anna palautetta testipalvelusta <a href="mailto:kuntalaisaloite.om@om.fi">kuntalaisaloite.om@om.fi</a>
                    </#if>

                <#else>
                    T&auml;m&auml; on oikeusministeri&ouml;n kuntalaisaloiteverkkopalvelun kehitysversio.
                    <br/>Sovellus voi toimia arvaamattomasti ja sis&auml;lt&auml;&auml; p&auml;&auml;t&ouml;nt&auml; dataa.
                </#if>
            </div>
        </div>
    </#if>
    
    <#if infoRibbon??>
        <div class="info-ribbon top static ${turquoiseColor}">
            <div class="container">
                <#noescape>${infoRibbon}</#noescape>
            </div>
        </div>
    </#if>    
</#macro>


<#macro bottomRibbon>
    <@assignSiteVars/>

    <#if (appEnvironment == "prod")>
        <#-- no ribbon in production -->

    <#else>
        <div class="debug-ribbon bottom ${ribbonColor}">
            <div class="container">
                Eri tiloissa olevia valmiita testialoitteita p&auml;&auml;set luomaan t&auml;&auml;lt&auml;: <a href="${urls.testDataGeneration()}">Testidatan generointi</a>
            </div>
        </div>
    </#if>
</#macro>

</#escape> 