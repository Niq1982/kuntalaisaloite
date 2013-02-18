<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#include "../includes/styles.ftl" />

<#escape x as x?html> 

<#assign title><@u.message "email.initiative" /> - ${emailInfo.municipalityName!""}</#assign>

<@l.emailHtml "municipality-not-collectable" title>

    <@b.mainContentBlock title>
        <@b.initiativeDetails "html" />
    </@b.mainContentBlock>
    
    <@u.spacer "15" />
    
    <@b.contentBlock "html">
        <@b.contactInfo "html" />
    </@b.contentBlock>
    
    <@u.spacer "15" />

</@l.emailHtml>

</#escape>  