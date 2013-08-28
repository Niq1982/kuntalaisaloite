<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />
<#import "../components/status-info.ftl" as s />

<#assign type="html" />

<#escape x as x?html>

<#-- Override default title if custom title is defined -->
<#if statusTitle??>
    <#assign title=statusTitle />
<#else>
    <#assign title><@u.message "email.status.info."+emailMessageType+".title" /></#assign>
</#if>

<@l.emailHtml title=title footer=false>

    <#-- Finnish part -->

    <@b.mainContentBlock title>
        <@s.statusInfo emailMessageType type />
    </@b.mainContentBlock>
    
    <@u.spacer "15" />
    
    <@b.contentBlock type>
        <@b.adminViewLink type=type verified=initiative.type.verifiable />
    </@b.contentBlock>
    
    <@u.spacer "15" />
    
    <@b.emailFooter type />
    
    <@u.spacer "15" />
    
    <#-- Swedish part -->
    
    <#global switchLocale = altLocale />
    
    <#if statusTitleSv??>
        <#assign title=statusTitleSv />
    <#else>
        <#assign title><@u.message "email.status.info."+emailMessageType+".title" /></#assign>
    </#if>
    
    <@b.mainContentBlock title>
        <@s.statusInfo emailMessageType type />
    </@b.mainContentBlock>
    
    <@u.spacer "15" />
    
    <@b.contentBlock type>
        <@b.adminViewLink type=type verified=initiative.type.verifiable />
    </@b.contentBlock>
    
    <@u.spacer "15" />
    
    <@b.emailFooter type />
    
    <@u.spacer "15" />
    
    <#-- Switch to default locale -->
    <#global switchLocale = locale />

</@l.emailHtml>


</#escape> 