<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="html" />

<#include "../includes/styles.ftl" />

<#escape x as x?html>

<#assign title><@u.message "email.verifyParticipate.title" /></#assign>

<@l.emailHtml title>

    <@b.mainContentBlock title>
        <@b.initiativeDetails type />

        <p style="${pBothMargins!""}"><@u.message "email.verifyParticipate.description" /></p>
        
        <#assign label><@u.message "email.verifyParticipate.verify" /></#assign>
        <@u.button label urls.confirmParticipant(participantId, confirmationCode) "green" />
        
        <h4 style="${h4!""}"><@u.message "email.verifyParticipate.why.title" /></h4>
        <p style="${pBottomMargin!""}"><@u.message "email.verifyParticipate.why.description" /></p>
        
    </@b.mainContentBlock>

    <@u.spacer "15" />

</@l.emailHtml>

</#escape>