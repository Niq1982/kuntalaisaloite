<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="text" />

<@u.message "email.author.invitation.accepted.title" />


<@b.initiativeDetails type=type showDate=true />

<@u.message "email.author.invitation.accepted.description" />


<@b.adminViewLink type initiative.type.verifiable />


<@b.separator />

<@b.emailFooter type />