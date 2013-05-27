<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />
<#import "../components/status-info.ftl" as s />

<#assign type="text" />

<#if statusTitle??>
    ${statusTitle}
<#else>
    <@u.message "email.status.info."+emailMessageType+".title" />
</#if>


<@s.statusInfo emailMessageType type />

----

<@b.emailFooter type />

<#-- Swedish part -->
<#global switchLocale = altLocale />

--------

<#if statusTitleSv??>
    ${statusTitleSv}
<#else>
    <@u.message "email.status.info."+emailMessageType+".title" />
</#if>


<@s.statusInfo emailMessageType type />
----

<@b.emailFooter type />

<#global switchLocale = locale />