<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#-- Use statusTitleFi and statusInfoFi for TEXT message -->
<#include "../includes/status-info.ftl" />

<#-- FINNISH -->
<@b.initiativeDetails "text" />

${statusInfoFi!""}

---------------------------------------

<#-- SWEDISH -->
${statusTitleSv!""}
      
<@b.initiativeDetails "text" />

${statusInfoSv!""}

