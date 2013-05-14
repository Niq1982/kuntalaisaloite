<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="html" />

<#include "../includes/styles.ftl" />

<#escape x as x?html> 

<#assign title><@u.message "email.initiative" /> - ${initiative.municipality.getLocalizedName(locale)}</#assign>

<@l.emailHtml "municipality-not-collectable" title>

    <#if (initiative.sentComment)?has_content>
        <@b.comment type initiative.sentComment "email.sentComment" />
        <@u.spacer "15" />
    </#if>

    <@b.mainContentBlock title>
        <@b.initiativeDetails type=type showProposal=true showDate=true showExtraInfo=true />
    </@b.mainContentBlock>
    
    <@u.spacer "15" />
    
    <@b.contentBlock type>
        <@b.contactInfo type />
    </@b.contentBlock>
    
    <@u.spacer "15" />

    <@b.contentBlock type>
        <@b.publicViewLink type />
    </@b.contentBlock>
    
    <@u.spacer "15" />

</@l.emailHtml>

</#escape>  