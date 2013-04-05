<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#escape x as x?html>

<#-- Use statusTitleHTMLFi and statusInfoHTMLFi for HTML message -->
<#include "../includes/status-info.ftl" />

<@l.emailHtml template="status-info-to-author" title=statusTitleHTMLFi!"" footerLink=false>

    <@b.mainContentBlock statusTitleHTMLFi>
        <#noescape>${statusInfoHTMLFi!""}</#noescape>
    </@b.mainContentBlock>
    
    <@u.spacer "15" />

    <#-- TODO: stateComment -->
    <#if stateComment??>
        <@b.comment "html" stateComment />
        <@u.spacer "15" />
    </#if>

    <#-- FINNISH -->
    <#--
    <@eb.emailTemplate "fi" statusTitleHTMLFi!"">
        <@eb.initiativeDetails "fi" "html" />
        <#noescape>${statusInfoHTMLFi!""}</#noescape>
        <@eb.emailBottom "fi" "html" />
    </@eb.emailTemplate>-->
    
    <#-- SWEDISH -->      
    <#--<@eb.emailTemplate "sv" statusTitleHTMLSv!"">
        <@eb.initiativeDetails "sv" "html" />
        <#noescape>${statusInfoHTMLSv!""}</#noescape>
        <@eb.emailBottom "sv" "html" />
    </@eb.emailTemplate>-->
    
</@l.emailHtml>

</#escape> 