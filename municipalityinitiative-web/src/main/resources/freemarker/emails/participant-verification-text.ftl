<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="text" />

<@b.initiativeDetails type />

<@u.message "email.verifyParticipate.title" />

<@b.initiativeDetails type=type showProposal=false showDate=false />

----

<@u.message "email.verifyParticipate.description" />
        
${urls.confirmParticipant(participantId, confirmationCode)}

----
        
<@u.message "email.verifyParticipate.why.title" />

<@u.message "email.verifyParticipate.why.description" />

----

<@b.emailFooter type />