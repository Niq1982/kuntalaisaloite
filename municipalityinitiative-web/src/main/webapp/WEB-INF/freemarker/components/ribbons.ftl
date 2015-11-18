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
    <#assign prodSite>https://www.kuntalaisaloite.fi</#assign>
    
    <#assign prodTestSite>https://testi.kuntalaisaloite.fi</#assign>
    <#assign betaIpSite>https://80.69.172.30XX</#assign>
    <#assign testSite>https://80-69-172-30.fi-hel1.host.upcloud.comXX</#assign>

    <#assign devIpSite>https://80.69.174.113</#assign>
    <#assign devSite>https://80-69-174-113.fi-hel1.host.upcloud.com</#assign>

    <#assign localSite>http://localhost</#assign>

    <#assign mobileTesting= true />
    <#-- For testing ribbons locally:
    <#assign prodTestSite>${localSite}</#assign>
    <#assign localSite>https://localhost:8443x</#assign>
   -->
    
    <#assign blueColor>ribbon-default</#assign> <#-- BLUE gradient -->
    <#assign redColor>ribbon-red</#assign> <#-- RED gradient -->
    <#assign turquoiseColor>ribbon-turquoise</#assign> <#-- TURQUOISE gradient for PROD SITE's infos -->

    <#assign ribbonColor>${(urls.baseUrl?starts_with(betaIpSite) || urls.baseUrl?starts_with(prodTestSite) || urls.baseUrl?starts_with(testSite))?string(redColor,blueColor)}</#assign>
</#macro>


<#macro topRibbon>
    <@assignSiteVars/>

    <#if urls.baseUrl?starts_with(prodSite)>
        <#-- no ribbon in production -->
        
    <#elseif urls.baseUrl?starts_with(localSite)>
        <#-- no ribbon in localhost (should look like production) -->

    <#elseif urls.baseUrl?starts_with(devSite) && mobileTesting >
    <#else>
    	<#if urls.baseUrl?starts_with(testSite) || urls.baseUrl?starts_with(prodTestSite)>
            <div class="test-padge"> </div>
        </#if>
        <div class="debug-ribbon top fixed ${ribbonColor}">
        	
            <div class="container">
                <#if urls.baseUrl?starts_with(testSite) || urls.baseUrl?starts_with(prodTestSite)>

                    T&auml;m&auml; on kuntalaisaloitepalvelun testiversio. Palvelu l&auml;hett&auml;&auml; sinulle s&auml;hk&ouml;posteja, joten annathan toimivan s&auml;hk&ouml;postiosoitteesi. Testipalvelussa tehdyt aloitteet ja muut tiedot poistetaan s&auml;&auml;nn&ouml;llisesti ilman erillist&auml; ilmoitusta, eiv&auml;tk&auml; aloitteet v&auml;lity kunnalle.<br/>
                    Anna palautetta testipalvelusta <a href="mailto:kuntalaisaloite.om@om.fi">kuntalaisaloite.om@om.fi</a>

                <#elseif urls.baseUrl?starts_with(devSite)>
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

    <#if urls.baseUrl?starts_with(prodSite)>
        <#-- no ribbon in production -->
        
    <#elseif urls.baseUrl?starts_with(localSite)>
        <#-- no ribbon in localhost (should look like production) -->

    <#elseif (urls.baseUrl?starts_with(devSite) && !mobileTesting) || urls.baseUrl?starts_with(testSite)>
        <div class="debug-ribbon bottom ${ribbonColor}">
            <div class="container">
                Eri tiloissa olevia valmiita testialoitteita p&auml;&auml;set luomaan t&auml;&auml;lt&auml;: <a href="${urls.testDataGeneration()}">Testidatan generointi</a>
            </div>
        </div>
    </#if>
</#macro>

</#escape> 