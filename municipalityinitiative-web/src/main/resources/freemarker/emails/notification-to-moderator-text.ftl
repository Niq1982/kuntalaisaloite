<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="text" />

<@u.message "email.notification.to.moderator.title" />


<@b.initiativeDetails type=type showProposal=true showDate=true showExtraInfo=true />
    
<@b.separator />
<@b.authorList type />
<@b.separator />
    
<@u.message "email.notification.to.moderator.moderate.title" />
    
<@u.message "email.notification.to.moderator.moderate.linkLabel" />:
${urls.moderation(initiative.id)}

<@b.separator />

<@b.emailFooter type />