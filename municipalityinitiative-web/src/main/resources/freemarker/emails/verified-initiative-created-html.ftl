<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="html" />

<#include "../includes/styles.ftl" />

<#escape x as x?html>

<#assign title><@u.message "email.verified.initiative.created.title" /></#assign>

<@l.emailHtml title>

    <@b.mainContentBlock title>
        <@b.initiativeDetails type=type showDate=true />
        
        <@b.adminViewLink type=type verified=true />

    </@b.mainContentBlock>

    <@u.spacer "15" />

</@l.emailHtml>

</#escape>