<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#include "../includes/styles.ftl" />

<#escape x as x?html>

<#assign title><@u.message "email.initiative" /> - ${emailInfo.municipality.name!""}</#assign>

<@l.emailHtml "municipality-collectable" title>

    <@b.mainContentBlock title>
        TODO: Vahvistusviesti
    </@b.mainContentBlock>
    
    <@u.spacer "15" />

</@l.emailHtml>

</#escape>