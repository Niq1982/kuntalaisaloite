<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="text" />
<#assign url = urls.invitation(initiative.id, authorInvitation.confirmationCode) />
<#assign urlSv = urls.alt().invitation(initiative.id, authorInvitation.confirmationCode) />


<@u.message "email.author.invitation.title" />
<#if initiative.type.verifiable>
    <@u.message "email.author.invitation.title."+initiative.type />
</#if>


<@b.initiativeDetails type=type showDate=true />

<@u.message "email.author.invitation.description" />


<#if initiative.type.verifiable><@u.message "email.author.invitation.authenticate" /></#if>


<@u.message "email.invitationViewLink" />

${url}

<@u.message "email.author.invitation.expiration" />


<@b.separator />

<@b.emailFooter type=type showManagement=false />


<@b.separator />
<#global switchLocale = altLocale />

<@u.message "email.author.invitation.title" />
<#if initiative.type.verifiable>
    <@u.message "email.author.invitation.title."+initiative.type />
</#if>


<@b.initiativeDetails type=type showDate=true />

<@u.message "email.author.invitation.description" />


<#if initiative.type.verifiable><@u.message "email.author.invitation.authenticate" /></#if>


<@u.message "email.invitationViewLink" />

${urlSv}

<@u.message "email.author.invitation.expiration" />


<@b.separator />

<@b.emailFooter type=type showManagement=false />

<#global switchLocale = locale />
