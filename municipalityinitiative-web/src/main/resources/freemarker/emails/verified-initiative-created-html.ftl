<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="html" />

<#include "../includes/styles.ftl" />

<#escape x as x?html>

<#assign title><@u.message "email.verified.initiative.created.title" /></#assign>

<@l.emailHtml title>

    <@b.mainContentBlock title>
        <p style="${pBothMargins!""}"><@u.message "email.verified.initiative.created.description" /></p>

        <p style="${smallFont!""}"><@u.link urls.loginToManagement(initiative.id) urls.loginToManagement(initiative.id) /></p>

    </@b.mainContentBlock>

    <@u.spacer "15" />

</@l.emailHtml>

</#escape>