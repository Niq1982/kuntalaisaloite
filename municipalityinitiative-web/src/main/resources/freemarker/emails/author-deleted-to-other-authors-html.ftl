<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#include "../includes/styles.ftl" />

<#assign type = "html">
<#assign messageKeyPrefix = "email.author.deleted.to.other.authors" />
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
    
    <h4 style="${h4!""}"><@u.message key=messageKeyPrefix+".deletedAuthor" /></h4>
    <@b.contactInfo contactInfo type />
    </@b.mainContentBlock>
    
    <@u.spacer "15" />
    
    <@b.emailFooter type />
    
    <@u.spacer "15" />
</#macro>

</#escape>