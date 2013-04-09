<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#-- Use statusTitle and statusInfo for TEXT message -->
<#include "../includes/status-info.ftl" />

<@b.initiativeDetails "text" />

<@u.message "email.status.info."+emailMessageType+".title" />

${statusInfo!""}
