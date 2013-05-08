<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="text" />
<#assign url = urls.loginAuthor(initiative.id, managementHash) />
<#assign urlSv = urls.alt().loginAuthor(initiative.id, managementHash) />


<@u.message "email.author.invitation.accepted.title" />


<@b.initiativeDetails type=type showProposal=false />

<@u.message "email.author.invitation.accepted.description" />


<@b.adminViewLink type />


----

<@b.emailFooter type />


--------------------------------------------------
<#global switchLocale = altLocale />

<@u.message "email.author.invitation.accepted.title" />


<@b.initiativeDetails type=type showProposal=false />

<@u.message "email.author.invitation.accepted.description" />


<@b.adminViewLink type />


----

<@b.emailFooter type />

<#global switchLocale = locale />
