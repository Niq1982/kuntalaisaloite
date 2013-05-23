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
 * @param page is "page.invitation.declined"
 * @param pageTitle can be assigned as custom HTML title
-->
<#assign pageTitle><@u.message "invitation.rejected.title" /></#assign>

<@l.main page="page.invitation.rejected" pageTitle=pageTitle!"" requestMessagesON=false>

<h1><#noescape>${pageTitle!""}</#noescape></h1>

<@u.systemMessage path="invitation.rejected.description" type="success" args=[requestAttribute!""] />

<p><@u.messageHTML key="common.toFrontpage" args=[urls.baseUrl+"/"+locale] /></p>

</@l.main>
</#escape>
