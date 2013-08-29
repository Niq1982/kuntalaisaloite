<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="html" />

<#escape x as x?html>

<#assign title><@u.message "email.author.invitation.accepted.title" /></#assign>

<@l.emailHtml title=title footer=false>

    <@b.mainContentBlock title> 
        <@b.initiativeDetails type=type showDate=true />
    
        <p style="${pBothMargins!""}"><@u.message "email.author.invitation.accepted.description" /></p>
    </@b.mainContentBlock>
    
    <@u.spacer "15" />
    
    <@b.contentBlock type>
        <@b.adminViewLink type=type verified=initiative.type.verifiable />
    </@b.contentBlock>
    
    <@u.spacer "15" />
    
    <@b.emailFooter type />
    
    <@u.spacer "15" />

</@l.emailHtml>

</#escape>