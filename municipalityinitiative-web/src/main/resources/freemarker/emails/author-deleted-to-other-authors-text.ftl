<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="text" />
<#assign messageKeyPrefix = "email.author.deleted.to.other.authors" />
<#assign title><@u.message messageKeyPrefix+".title" /></#assign>

<@content />

<#-- Swedish part -->
<#global switchLocale = altLocale />

<@b.separator />

<@content />

<#global switchLocale = locale />

<#--
 * content
 *
 * Prints macro 2 times with different locales
 -->
<#macro content>
    <@b.initiativeDetails type=type showDate=true />
            
    <@u.message messageKeyPrefix+".description"/>
    <@u.message key=messageKeyPrefix+".deletedAuthor" />
    
    
    <@b.contactInfo contactInfo type />
    
    <@b.separator />

    <@b.emailFooter type />
</#macro>