<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="html" />

<#include "../includes/styles.ftl" />

<#escape x as x?html>

<#assign title><@u.message "email.initiative" /></#assign>

<@l.emailHtml template="municipality-collectable" title=title footer=false>

    <#if (initiative.sentComment)?has_content>
        <@b.comment type initiative.sentComment "email.sentComment" />
        <@u.spacer "15" />
    </#if>

    <@b.mainContentBlock title>
        <@b.initiativeDetails type />
        
        <#if (initiative.extraInfo)?has_content>
            <h4 style="${h4!""}"><@u.message "email.extraInfo" /></h4>
            <p style="${pBottomMargin!""}">${initiative.extraInfo}</p>
        </#if>
        
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

    <@b.emailFooter type ".sentToMunicipality" />

</@l.emailHtml>

</#escape>