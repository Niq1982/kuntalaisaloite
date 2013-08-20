<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#include "../includes/styles.ftl" />

<#assign type = "html">
<#assign messageKeyPrefix = "email.author.deleted.to.deleted.author" />
<#assign title><@u.message messageKeyPrefix+".title" /></#assign>

<#escape x as x?html>

<@l.emailHtml title=title footer=false>

    <@content />
    
    <#-- Swedish part -->
    
    <#global switchLocale = altLocale />
    
    <#assign title><@u.message messageKeyPrefix+".title" /></#assign>
    
    <@content />
    
    <#-- Switch to default locale -->
    <#global switchLocale = locale />

</@l.emailHtml>

<#--
 * content
 *
 * Prints macro 2 times with different locales
 -->
<#macro content>
    <@b.mainContentBlock title>
    <@b.initiativeDetails type=type showDate=true />
    
    <p style="${pBothMargins!""}"><@u.message messageKeyPrefix+".description"/></p>
    <p style="${pBothMargins!""}"><@u.message messageKeyPrefix+".noRightsToLogin"/></p>
    </@b.mainContentBlock>
    
    <@u.spacer "15" />
    
    <@b.emailFooter type=type showManagement=false />
    
    <@u.spacer "15" />
</#macro>

</#escape>