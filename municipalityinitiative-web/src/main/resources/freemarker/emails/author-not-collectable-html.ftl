<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#escape x as x?html> 

<#assign title><@u.message "email.author.sentToMunicipality.title" /> ${emailInfo.municipalityName!""}</#assign>

<@l.emailHtml "sent-to-municipality" title>

    <@b.emailTemplate title>
        <@b.initiativeDetails "html" />
        
        <@b.contactInfo "html" />

        <p><@u.message "email.author.linkToManagement" /><br/><@u.link emailInfo.url /></p>
    </@b.emailTemplate>

</@l.emailHtml>

</#escape>
