<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="html" />

<#escape x as x?html>

<#assign title><@u.message "email.author.invitation.accepted.title" /></#assign>
<#assign url = urls.loginAuthor(initiative.id, managementHash) />
<#assign urlSv = urls.alt().loginAuthor(initiative.id, managementHash) />

<@l.emailHtml template="author-invitation-accepted" title=title footer=false>

    <#-- Finnish part -->

    <@b.mainContentBlock title> 
        <@b.initiativeDetails type=type showProposal=false />
    
        <p style="${pBothMargins!""}"><@u.message "email.author.invitation.accepted.description" /></p>

        <@b.adminViewLink type />
    </@b.mainContentBlock>
    
    <@u.spacer "15" />
    
    <@b.emailFooter type />
    
    <@u.spacer "15" />
    
    <#-- Swedish part -->
    
    <#global switchLocale = altLocale />

    <#assign title><@u.message "email.author.invitation.accepted.title" /></#assign>
    
    <@b.mainContentBlock title>
        <@b.initiativeDetails type=type showProposal=false />
    
        <p style="${pBothMargins!""}"><@u.message "email.author.invitation.accepted.description" /></p>
        
        <@b.adminViewLink type />
    </@b.mainContentBlock>
    
    <@u.spacer "15" />
    
    <@b.emailFooter type />
    
    <@u.spacer "15" />
    
    <#-- Switch to default locale -->
    <#global switchLocale = locale />

</@l.emailHtml>

</#escape>