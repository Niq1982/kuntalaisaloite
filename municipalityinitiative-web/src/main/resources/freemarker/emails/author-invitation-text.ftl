<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="text" />
<#assign url = urls.invitation(initiative.id, authorInvitation.confirmationCode) />
<#assign urlSv = urls.alt().invitation(initiative.id, authorInvitation.confirmationCode) />


<@u.message "email.author.invitation.title" />


<@b.initiativeDetails type=type showProposal=false />

<@u.message "email.author.invitation.description" />


<@u.message "email.invitationViewLink" />

${url}

<@u.message "email.author.invitation.expiration" />


----

<@b.emailFooter type />


--------------------------------------------------
<#global switchLocale = altLocale />

<@u.message "email.author.invitation.title" />


<@b.initiativeDetails type=type showProposal=false />

<@u.message "email.author.invitation.description" />


<@u.message "email.invitationViewLink" />

${urlSv}

<@u.message "email.author.invitation.expiration" />


----

<@b.emailFooter type />

<#global switchLocale = locale />
