<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="text" />

<@b.initiativeDetails type />

<@u.message "email.verifyParticipate.title" />

<@b.initiativeDetails type />

<@b.separator />

<@u.message "email.verifyParticipate.description" />
        
${urls.confirmParticipant(participantId, confirmationCode)}

<@b.separator />

<@u.message "email.verifyParticipate.why.title" />

<@u.message "email.verifyParticipate.why.description" />

<@b.separator />

<@b.emailFooter type />