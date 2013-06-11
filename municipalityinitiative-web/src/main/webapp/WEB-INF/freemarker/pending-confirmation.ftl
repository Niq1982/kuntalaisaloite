<#import "/spring.ftl" as spring />
<#import "components/forms.ftl" as f />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/edit-blocks.ftl" as edit />
<#import "components/some.ftl" as some />

<#escape x as x?html>

<#--
 * Layout parameters for HTML-title and navigation.
 *
 * @param page is "page.pending.confirmation"
 * @param pageTitle can be assigned as custom HTML title
-->
<#assign pageTitle><@u.message "create.pendingConfirmation.title" /></#assign>

<@l.main page="page.pending.confirmation" pageTitle=pageTitle!"" requestMessagesON=false>

<h1><#noescape>${pageTitle!""}</#noescape></h1>
    
<#if requestMessages?? && (requestMessages?size > 0)>
    <@u.requestMessage requestMessages />
</#if>

<p><@u.message "create.pendingConfirmation.linkSentTo" /> <strong>${requestAttribute!""}</strong>.</p>

<p><@u.message "create.pendingConfirmation.readEmail" /></p>

<h3><@u.message "create.pendingConfirmation.emailFail.title" /></h3>
<ul>
    <#assign href=urls.prepare() />
    <li><@u.messageHTML key="create.pendingConfirmation.emailFail.bullet-1" args=[href] /></li>
    <li><@u.message "create.pendingConfirmation.emailFail.bullet-2" /></li>
    <li><@u.message "create.pendingConfirmation.emailFail.bullet-3" /></li>
</ul>

<h2>Ylläpitolinkki testausta varten</h2>
<a href="${urls.loginAuthor(managementHash)}">${urls.loginAuthor(managementHash)}</a>



</@l.main>
</#escape>
