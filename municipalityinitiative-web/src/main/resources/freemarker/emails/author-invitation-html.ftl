<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="html" />

<#escape x as x?html>

<#assign title><@u.message "email.author.invitation.title" /></#assign>
<#assign url = urls.invitation(initiative.id, authorInvitation.confirmationCode) />
<#assign urlSv = urls.alt().invitation(initiative.id, authorInvitation.confirmationCode) />

<@l.emailHtml template="author-invitation" title=title footer=false>

    <#-- Finnish part -->

    <@b.mainContentBlock title> 
        <@b.initiativeDetails type=type showProposal=false />
    
        <p style="${pBothMargins!""}"><@u.message "email.author.invitation.description" /></p>
        
        <#assign label><@u.message "email.invitationViewLink" /></#assign>
        <@u.button label url "green" />
        
        <p style="${pBothMargins!""}"><@u.message "email.author.invitation.expiration" /></p>
    </@b.mainContentBlock>
    
    <@u.spacer "15" />
    
    <@b.emailFooter type />
    
    <@u.spacer "15" />
    
    <#-- Swedish part -->
    
    <#global switchLocale = altLocale />

    <#assign title><@u.message "email.author.invitation.title" /></#assign>
    
    <@b.mainContentBlock title>
        <@b.initiativeDetails type=type showProposal=false />
    
        <p style="${pBothMargins!""}"><@u.message "email.author.invitation.description" /></p>
        
        <#assign label><@u.message "email.invitationViewLink" /></#assign>
        <@u.button label urlSv "green" />
        
        <p style="${pBothMargins!""}"><@u.message "email.author.invitation.expiration" /></p>
    </@b.mainContentBlock>
    
    <@u.spacer "15" />
    
    <@b.emailFooter type />
    
    <@u.spacer "15" />
    
    <#-- Switch to default locale -->
    <#global switchLocale = locale />

</@l.emailHtml>

</#escape>