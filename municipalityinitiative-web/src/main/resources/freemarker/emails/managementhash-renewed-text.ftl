<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="text" />

<@u.message "email.managementhash.renewed.title" />


<@b.initiativeDetails type=type showDate=true />

<@u.message "email.managementhash.renewed.description" />


<@b.adminViewLink type />


<@b.separator />

<@b.emailFooter type />