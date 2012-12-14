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
    <#assign prodSite>https://www.kansalaisaloite.fi</#assign>
    <#assign prodTestSite>https://testi.kansalaisaloite.fi</#assign>
    <#assign betaIpSite>https://80.69.172.30</#assign>
    <#assign testSite>https://80-69-172-30.fi-hel1.host.upcloud.com</#assign>
    <#assign labsSite>https://labs.solita.fi</#assign>         <#-- TODO: not in use, remove (with all references) when permanently abandoned -->
    <#assign devSite>https://localhost:8443</#assign>

    <#-- For testing ribbons locally:
    <#assign betaIpSite>${devSite}</#assign>
    <#assign devSite>https://localhost:8443x</#assign>
    -->
    
    <#assign blueColor>ribbon-default</#assign> <#-- BLUE gradient -->
    <#assign redColor>ribbon-red</#assign> <#-- RED gradient -->
    <#assign turquoiseColor>ribbon-turquoise</#assign> <#-- TURQUOISE gradient for PROD SITE's infos -->
    <#assign ribbonColor>${(urls.baseUrl?starts_with(betaIpSite) || urls.baseUrl?starts_with(prodTestSite))?string(redColor,blueColor)}</#assign>
</#macro>


<#macro topRibbon>
    <@assignSiteVars/>

    <#if urls.baseUrl?starts_with(prodSite)>
        <#-- no ribbon in production -->
        
    <#elseif urls.baseUrl?starts_with(devSite)>
        <#-- no ribbon in localhost (should look like production) -->
        
    <#else>
        <div class="debug-ribbon top fixed ${ribbonColor}">
            <div class="container">
                Tämä on oikeusministeriön kansalaisaloiteverkkopalvelun testisivusto, joka on kehityksen alla. <a href="http://bit.ly/kansalaisaloite-testauslomake" target="_blank">Anna palautetta tästä sivusta</a>
                <#--<a href="mailto:kansalaisaloite_testaus.om@om.fi?subject=Aloitepalvelu%3A%20palaute%20sivulta%20%22${currentPage!""}%22&amp;body=%0A%0APalaute%20l%C3%A4hetetty%20sivulta%3A%20${currentPage!""}%0A${urls.baseUrl}${springMacroRequestContext.requestUri!""}">Anna palautetta tästä sivusta</a>-->

                <#if urls.baseUrl?starts_with(betaIpSite) || urls.baseUrl?starts_with(testSite) || urls.baseUrl?starts_with(prodTestSite)>
                    <br/>Huom! Testausten helpottamiseksi aikarajat on säädetty lyhyemmiksi. Kannatusilmoitusten keräyksen kesto 2 päivää ja muut 1 päivä.
                    <br/>Palveluun syötetyt tiedot voidaan tyhjentää ilman erillistä varoitusta.
                <#elseif urls.baseUrl?starts_with(labsSite)>
                    <br/>Huom! Johtuen samalla palvelimella suoritettavista muista testeistä, pyydämme toistaiseksi <strong>rajoittamaan käytön klo 9-17 välille</strong>.
                </#if>

            </div>
        </div>
        <div class="container relative"><div class="test-padge <#if infoRibbon??>has-info-ribbon</#if>"> </div></div>
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
        
    <#elseif urls.baseUrl?starts_with(devSite)>
        <#-- no ribbon in localhost (should look like production) -->

    <#elseif urls.baseUrl?starts_with(labsSite) || urls.baseUrl?starts_with(testSite)>
        <div class="debug-ribbon bottom ${ribbonColor}">
            <div class="container">
                Eri tiloissa olevia valmiita testialoitteita pääset luomaan täältä: <a href="${urls.testDataGeneration()}">Testidatan generointi</a>
            </div>
        </div>
    </#if>
</#macro>

</#escape> 