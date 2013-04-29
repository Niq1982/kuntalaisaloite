<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="html" />

<#escape x as x?html>

<#include "../includes/status-info.ftl" />

<#-- Override default title if custom title is defined -->
<#if statusTitle??>
    <#assign title=statusTitle />
<#else>
    <#assign title><@u.message "email.status.info."+emailMessageType+".title" /></#assign>
</#if>

<@l.emailHtml template="status-info-to-author" title=title footer=false>

    <#-- Finnish part -->

    <@b.mainContentBlock title>
        <#noescape>${statusInfoHTML!""}</#noescape>
    </@b.mainContentBlock>
    
    <@u.spacer "15" />
    
    <@b.emailFooter "html" />
    
    <@u.spacer "15" />
    
    <#-- Swedish part -->
    
    <#global switchLocale = altLocale />
    
    <#if !statusTitle??>
        <#assign title><@u.message "email.status.info."+emailMessageType+".title" /></#assign>
    </#if>
    
    <@b.mainContentBlock title>
        <#noescape>${statusInfoHTMLSv!""}</#noescape>
    </@b.mainContentBlock>
    
    <@u.spacer "15" />
    
    <@b.emailFooter "html" />
    
    <@u.spacer "15" />
    
    <#global switchLocale = locale />

</@l.emailHtml>

</#escape> 