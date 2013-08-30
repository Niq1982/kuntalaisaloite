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

<@b.separator />

<#-- Do not add admin-link when initiative is not collectable (eg. is sent straight to municipality) -->
<#if emailMessageType != EmailMessageType.ACCEPTED_BY_OM_AND_SENT>
<@b.adminViewLink type=type verified=initiative.type.verifiable />
<@b.separator />
</#if>

<@b.emailFooter type />

<#-- Swedish part -->
<#global switchLocale = altLocale />

<@b.separator />

<#if statusTitleSv??>
    ${statusTitleSv}
<#else>
    <@u.message "email.status.info."+emailMessageType+".title" />
</#if>


<@s.statusInfo emailMessageType type />
<@b.separator />

<#-- Do not add admin-link when initiative is not collectable (eg. is sent straight to municipality) -->
<#if emailMessageType != EmailMessageType.ACCEPTED_BY_OM_AND_SENT>
<@b.adminViewLink type=type verified=initiative.type.verifiable />
<@b.separator />
</#if>

<@b.emailFooter type />

<#global switchLocale = locale />