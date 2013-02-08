<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#include "../includes/styles.ftl" />

<#escape x as x?html>

<#assign title><@u.message "email.initiative" /> - ${emailInfo.municipalityName!""}</#assign>

<@l.emailHtml "municipality-collectable" title>

    <#if (emailInfo.comment)?has_content>
        <@b.comment "html" emailInfo.comment />
    </#if>

    <@b.mainContentBlock title>
        <@b.initiativeDetails "html" />
        <@b.contactInfo "html" />
    </@b.mainContentBlock>
    
    <@b.contentBlock "html">
        <p style="${pBothMargins!""}"><@u.message "email.municipality.sendFrom" /><br/><@u.link emailInfo.url /></p>
        <#-- TODO: Reply to not yet done
        <p style="${pBothMargins!""}"><@u.message "email.municipality.replyTo" /></p>
        -->
    </@b.contentBlock>

</@l.emailHtml>

</#escape>