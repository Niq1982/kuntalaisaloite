<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#escape x as x?html>

<#assign title><@u.message "email.status.info."+emailMessageType+".title" /></#assign>

<#include "../includes/status-info.ftl" />

<@l.emailHtml template="status-info-to-author" title=title>

    <@b.mainContentBlock title>
        <#noescape>${statusInfoHTML!""}</#noescape>
    </@b.mainContentBlock>
    
    <@u.spacer "15" />

    <#-- TODO: stateComment -->
    <#if stateComment??>
        <@b.comment "html" stateComment />
        <@u.spacer "15" />
    </#if>

</@l.emailHtml>

</#escape> 