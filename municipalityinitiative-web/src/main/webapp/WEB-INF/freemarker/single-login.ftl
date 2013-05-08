<#import "/spring.ftl" as spring />
<#import "components/forms.ftl" as f />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/edit-blocks.ftl" as edit />

<#escape x as x?html>

<#--
 * Layout parameters for HTML-title and navigation.
 *
 * @param page is "page.prepare"
 * @param pageTitle can be assigned as custom HTML title
-->
<#assign page="page.prepare" />

<#--
 * Get current municipality as request parameter.
 * - Request parameter is for iFrame create-link
 * - We could want to try to guess user's municipality later on with different methods like GEO location, user's history etc..
-->
<#assign currentMunicipality = RequestParameters['municipality']!"" />

<@l.main page pageTitle!"">

    <form action="${urls.login(id)}" method="post">
        <h1>Olet kirjautumassa sisään yksittäisen aloitteen tekijäksi</h1>
        <input type="hidden" name="CSRFToken" value="${CSRFToken!""}"/>
        <input type="hidden" name="management" value="${managementHash}"/>
        
        <button type="submit" name="Login" value="Kirjaudu" class="small-button">Kirjaudu</button>
    </form>

</@l.main>
</#escape>
