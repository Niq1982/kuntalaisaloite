<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<@u.message "email.notification.to.moderator.title" />

<@b.initiativeDetails "text" />
    
----
    
<@b.contactInfo "text" />

----
    
<@u.message "email.notification.to.moderator.moderateLink" />:
${urls.moderation(initiative.id)}

----

<@b.emailFooter "text" />