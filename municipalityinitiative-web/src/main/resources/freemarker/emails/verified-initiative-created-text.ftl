<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="text" />

<@u.message "email.verified.initiative.created.title" />


<@b.initiativeDetails type=type showDate=true />


<@b.adminViewLink type=type verified=true />


<@b.separator />

<@b.emailFooter type />