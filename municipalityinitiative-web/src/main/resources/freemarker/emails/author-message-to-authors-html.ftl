<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="html" />

<#include "../includes/styles.ftl" />

<#assign keyPrefix="email.author.message.to.authors" />

<#escape x as x?html>

<#assign title><@u.message keyPrefix+".title" /></#assign>

<@l.emailHtml title=title footer=false>

    <@b.mainContentBlock title>
        <@b.initiativeDetails type />

        <p style="${pBothMargins!""}"><@u.message keyPrefix+".description" /></p>

        <h4 style="${h4!""}"><@u.message keyPrefix+".sender" /></h4>
        <p style="${pBottomMargin!""}">${authorMessage.contactName}<br/>
        <@u.link "mailto:"+authorMessage.contactEmail authorMessage.contactEmail /></p>
        
        <h4 style="${h4!""}"><@u.message keyPrefix+".message" /></h4>
        <@u.text authorMessage.message pBottomMargin!"" />

    </@b.mainContentBlock>

    <@u.spacer "15" />
    
    <@b.emailFooter type=type managementHash=managementHash />
    
    <@u.spacer "15" />
    
    <#-- Swedish part -->
    <#global switchLocale = altLocale />

    <#assign title><@u.message keyPrefix+".title" /></#assign>
    
    <@b.mainContentBlock title>
        <@b.initiativeDetails type />

        <p style="${pBothMargins!""}"><@u.message keyPrefix+".description" /></p>

        <h4 style="${h4!""}"><@u.message keyPrefix+".sender" /></h4>
        <p style="${pBottomMargin!""}">${authorMessage.contactName}<br/>
        <@u.link "mailto:"+authorMessage.contactEmail authorMessage.contactEmail /></p>
        
        <h4 style="${h4!""}"><@u.message keyPrefix+".message" /></h4>
        <@u.text authorMessage.message pBottomMargin!"" />

    </@b.mainContentBlock>

    <@u.spacer "15" />
    
    <@b.emailFooter type=type managementHash=managementHash />
    
    <@u.spacer "15" />
    
    <#-- Switch to default locale -->
    <#global switchLocale = locale />

</@l.emailHtml>

</#escape>