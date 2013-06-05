<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="html" />

<#include "../includes/styles.ftl" />

<#assign keyPrefix="email.author.message.confirmation" />

<#escape x as x?html>

<#assign title><@u.message keyPrefix+".title" /></#assign>

<@l.emailHtml title>

    <@b.mainContentBlock title>
        <@b.initiativeDetails type />

        <p style="${pBothMargins!""}"><@u.message keyPrefix+".description" /></p>
        <p style="${pBothMargins!""}"><@u.message keyPrefix+".description.2" /></p>

        <h4 style="${h4!""}"><@u.message keyPrefix+".sender" /></h4>
        <p style="${pBottomMargin!""}">${authorMessage.contactName}<br/>
        <@u.link "mailto:"+authorMessage.contactEmail authorMessage.contactEmail /></p>
        
        <h4 style="${h4!""}"><@u.message keyPrefix+".message" /></h4>
        <@u.text authorMessage.message pBottomMargin!"" />

        <#assign label><@u.message keyPrefix+".verify" /></#assign>
        <@u.button label urls.confirmAuthorMessage(authorMessage.confirmationCode) "green" />
        <br/><br/>

    </@b.mainContentBlock>

    <@u.spacer "15" />

</@l.emailHtml>

</#escape>