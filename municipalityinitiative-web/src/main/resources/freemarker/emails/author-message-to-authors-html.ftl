<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="html" />

<#include "../includes/styles.ftl" />

<#escape x as x?html>

<#assign title><@u.message "email.author.message.to.authors.title" /></#assign>

<@l.emailHtml title>

    <@b.mainContentBlock title>
        <@b.initiativeDetails type />

        <p style="${pBothMargins!""}"><@u.message "email.author.message.to.authors.description" /></p>

        <h4 style="${h4!""}"><@u.message "email.author.message.to.authors.sender" /></h4>
        <p style="${pBottomMargin!""}">${authorMessage.contactName}<br/>
        <@u.link "mailto:"+authorMessage.contactEmail authorMessage.contactEmail /></p>
        
        <h4 style="${h4!""}"><@u.message "email.author.message.to.authors.message" /></h4>
        <p style="${pBottomMargin!""}">${authorMessage.message}</p>

    </@b.mainContentBlock>

    <@u.spacer "15" />

</@l.emailHtml>

</#escape>