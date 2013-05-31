<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="html" />

<#include "../includes/styles.ftl" />

<#escape x as x?html>

<#assign title><@u.message "email.new.managementhash.generated.title" /></#assign>

<@l.emailHtml title>

    <@b.mainContentBlock title>
        <p style="${pBothMargins!""}"><@u.message "email.new.managementhash.generated.description" /></p>
        <p>${initiative.name}</p>
        <p style="${smallFont!""}"><@u.link urls.loginAuthor(managementHash) urls.loginAuthor(managementHash) /></p>
    </@b.mainContentBlock>

    <@u.spacer "15" />

    </@l.emailHtml>

    </#escape>