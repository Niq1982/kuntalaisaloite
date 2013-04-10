<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#include "../includes/styles.ftl" />

<#escape x as x?html>

<#assign title><@u.message "email.notification.to.moderator.title" /></#assign>

<@l.emailHtml "notification-to-moderator" title>

    <@b.mainContentBlock title>
        <@b.initiativeDetails "html" />
    </@b.mainContentBlock>
    
    <@u.spacer "15" />
    
    <@b.contentBlock "html">
        <@b.contactInfo "html" />
    </@b.contentBlock>
    
    <@u.spacer "15" />
    
    <@b.contentBlock "html">
        <h4 style="${h4!""}"><@u.message "email.notification.to.moderator.moderateLink" /></h4>
        <p style="${pBottomMargin}"><@u.link urls.moderation(initiative.id) /></p>
    </@b.contentBlock>

    <@u.spacer "15" />

</@l.emailHtml>

</#escape>