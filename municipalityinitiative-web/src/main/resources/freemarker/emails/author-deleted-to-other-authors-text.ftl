<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign title><@u.message "email.author.invitation.title" /></#assign>

<#escape x as x?html>

<@l.emailHtml template="author-invitation" title=title footer=false>


</@l.emailHtml>

</#escape>