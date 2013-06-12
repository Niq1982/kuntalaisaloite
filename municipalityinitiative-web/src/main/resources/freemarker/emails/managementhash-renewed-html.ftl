<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="html" />

<#include "../includes/styles.ftl" />

<#escape x as x?html>

<#assign title><@u.message "email.managementhash.renewed.title" /></#assign>

<@l.emailHtml title=title footer=false>

    <#-- Finnish part -->

    <@b.mainContentBlock title>
        <@b.initiativeDetails type=type showDate=true />
    
        <p style="${pBothMargins!""}"><@u.message "email.managementhash.renewed.description" /></p>

        <@b.adminViewLink type />
    </@b.mainContentBlock>

    <@u.spacer "15" />
    
    <@b.emailFooter type />
    
    <@u.spacer "15" />

    <#-- Swedish part -->
    
    <#global switchLocale = altLocale />
    
    <#if statusTitleSv??>
        <#assign title><@u.message "email.managementhash.renewed.title" /></#assign>
    </#if>

    <@b.mainContentBlock title>
        <@b.initiativeDetails type=type showDate=true />
    
        <p style="${pBothMargins!""}"><@u.message "email.managementhash.renewed.description" /></p>

        <@b.adminViewLink type />
    </@b.mainContentBlock>

    <@u.spacer "15" />
    
    <@b.emailFooter type />
    
    <@u.spacer "15" />

</@l.emailHtml>

</#escape>