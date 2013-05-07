<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="html" />

<#escape x as x?html>


<#assign title><@u.message "email.author.invitation.subject" /></#assign>
<@l.emailHtml template="author-invitation" title=title footer=false>

    <@u.spacer "15" />
    
    <@b.contentBlock type>

        <#assign url = urls.invitation(initiative.id, authorInvitation.confirmationCode) />
        <#assign urlSv = urls.alt().invitation(initiative.id, authorInvitation.confirmationCode) />
        <p style="${pBothMargins!""}"><@u.message "email.invitationViewLink" /></p>
        <p style="${pBothMargins!""} ${smallFont!""}"><@u.link url url /></p>
        <p style="${pBothMargins!""} ${smallFont!""}"><@u.link urlSv urlSv /></p>
    </@b.contentBlock>

</@l.emailHtml>

</#escape>