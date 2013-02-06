<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#escape x as x?html> 

<#assign title><@u.message "email.initiative" /> - ${emailInfo.municipalityName!""}</#assign>

<@l.emailHtml "municipality-initiative" title>

    <#-- FINNISH -->
    <@b.emailTemplate title>
        <@b.initiativeDetails "html" />
        
        <@b.contactInfo "html" />
        
        <p style="margin:0 0 1em 0;"><@u.message "email.municipality.sendFrom" /><br/><@u.link emailInfo.url /></p>
        <p style="margin:0 0 1em 0;"><@u.message "email.municipality.replyTo" /></p>
    </@b.emailTemplate>

</@l.emailHtml>

</#escape>  