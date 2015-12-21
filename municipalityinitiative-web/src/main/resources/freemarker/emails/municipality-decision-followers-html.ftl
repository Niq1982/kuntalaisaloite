<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="html" />

<#include "../includes/styles.ftl" />

<#escape x as x?html>

    <#assign title><@u.message "email.initiative" /></#assign>

    <@l.emailHtml title=title footer=true>
        <@b.mainContentBlock title>
            <@b.municipalityDecision type />
        </@b.mainContentBlock>

        <@u.spacer "15" />

        <@b.contentBlock type>
            <#if removeHash??>
                <@b.unfollow initiativeId removeHash type/>
            </#if>
        </@b.contentBlock>

        <@u.spacer "15" />


        <#-- Swedish part -->

        <#global switchLocale = altLocale />

        <@b.mainContentBlock title>
            <@b.municipalityDecision type />
        </@b.mainContentBlock>

        <@u.spacer "15" />

        <@b.contentBlock type>
            <#if removeHash??>
                <@b.unfollow initiativeId removeHash type/>
            </#if>
        </@b.contentBlock>

        <@u.spacer "15" />

        <#-- Switch to default locale -->
        <#global switchLocale = locale />

    </@l.emailHtml>

</#escape>