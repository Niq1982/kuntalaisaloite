<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="html" />

<#include "../includes/styles.ftl" />

<#escape x as x?html>

<#assign title><@u.message "email.initiative" /> - <#-- ${initiative.municipality.getLocalizedName(locale)!""} --></#assign>

<@l.emailHtml "municipality-collectable" title>

    <#if (initiative.comment)?has_content>
        <@b.comment type initiative.comment "email.commentToMunicipality" />
        <@u.spacer "15" />
    </#if>
<#--
    <@b.mainContentBlock title>
        <@b.initiativeDetails type />
    </@b.mainContentBlock>
    
    <@u.spacer "15" />
    
    <@b.contentBlock type>
        <@b.contactInfo type />
    </@b.contentBlock>
    
    <@u.spacer "15" />
    
    <@b.contentBlock type>
        <@b.participants type />
    </@b.contentBlock>
    
    <@u.spacer "15" />
-->

</@l.emailHtml>

</#escape>