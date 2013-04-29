<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="html" />

<#include "../includes/styles.ftl" />

<#escape x as x?html>

<#assign title><@u.message "email.create.title" /></#assign>

<@l.emailHtml "municipality-collectable" title>

    <#-- TODO: localize -->
    <@b.mainContentBlock title>
        <p style="${pBothMargins!""}"><@u.message "email.create.description" /></p>
        
        <p style="${smallFont!""}"><@u.link urls.loginAuthor(initiative.id, initiative.managementHash.value) urls.loginAuthor(initiative.id, initiative.managementHash.value) /></p>

        <p style="${pBothMargins!""}"><@u.message "email.create.description.2" /></p>
        
        <h4 style="${h4}"><@u.message "email.create.instruction.title" /></h4>
        <ol style="margin-top:0.5em;">
            <li style="margin-top:0.5em; margin-bottom:0.5em;"><@u.message "email.create.instruction.bullet-1" /></li>
            <li style="margin-bottom:0.5em;"><@u.message "email.create.instruction.bullet-2" /></li>
            <li style="margin-bottom:0.5em;"><@u.message "email.create.instruction.bullet-3" /></li>
        </ol>
    </@b.mainContentBlock>
    
    <@u.spacer "15" />

</@l.emailHtml>

</#escape>