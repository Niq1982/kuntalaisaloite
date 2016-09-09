<#import "/spring.ftl" as spring />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />

<#escape x as x?html>

<#--
 * Layout parameters for HTML-title and navigation.
 *
 * @param page is "page.authenticate"
 * @param pageTitle can be assigned as custom HTML title
-->
<#assign page="page.authenticate" />

<@l.main page>

    <div class="msg-block cf">
        <h1><@u.message "authenticate.title" /></h1>

        <p><@u.message "authenticate.paragraph1" /></p>
        <ul>
            <li><@u.message "authenticate.listItem1" /></li>
            <li><@u.message "authenticate.listItem2" /></li>
        </ul>

        <p><@u.message "authenticate.paragraph2" /></p>
            
        <p><a href="<#if samlEnabled>${urls.samlLogin(target)}<#else>${urls.vetumaLogin(target)}</#if>" class="small-button"><span class="small-icon next"><@u.message "common.continueToAuthenticate" /></span></a></p>
    </div>

</@l.main>
</#escape>