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
    <@b.mainContentBlock title>
        <#noescape>${statusInfoHTML!""}</#noescape>
    </@b.mainContentBlock>
    
    <@u.spacer "15" />
    
    <@b.emailFooter "html" />
    
    <@u.spacer "15" />
    
    <#-- TODO: Swap locale to SV -->
    
    <@b.mainContentBlock title>
        <#noescape>${statusInfoHTML!""}</#noescape>
    </@b.mainContentBlock>
    
    <@u.spacer "15" />
    
    <@b.emailFooter "html" "sv" />
    
    <@u.spacer "15" />
    
    <#-- TODO: Swap locale back to FI -->

</@l.emailHtml>

</#escape> 