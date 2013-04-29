<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="text" />

<#-- Use statusTitle and statusInfo for TEXT message -->
<#include "../includes/status-info.ftl" />

<@u.message "email.status.info."+emailMessageType+".title" />

${statusInfo!""}

----

<@b.emailFooter type />

<#-- Swedish part -->
<#global switchLocale = altLocale />

--------

<@u.message "email.status.info."+emailMessageType+".title" />

${statusInfoSv!""}

----

<@b.emailFooter type />

<#global switchLocale = locale />