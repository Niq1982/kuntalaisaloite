<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="html" />

<#include "../includes/styles.ftl" />

<#escape x as x?html>

    <#assign title><@u.message "email.followConfirm.title" /></#assign>

    <@l.emailHtml title=title footer=true>

        <@b.mainContentBlock title>
            <h4 style="${h4!""}">${initiative.name!""}</h4>
            <p style="${pBottomMargin!""}">${initiative.municipality.getLocalizedName(switchLocale!locale)!""}</p>

        </@b.mainContentBlock>

        <@u.spacer "15" />


        <@b.contentBlock type>
            <h4 style="${h4!""}"><@u.message "email.followConfirm" /></h4>
            <p style="${pBottomMargin!""}"><@u.messageHTML key="email.followConfirm.text" /></p>
            <@b.publicViewLink type/>
        </@b.contentBlock>

        <@u.spacer "15" />

        <@b.contentBlock type>
            <#if removeHash??>
                <@b.unfollow initiativeId removeHash type/>
            </#if>
        </@b.contentBlock>

        <@u.spacer "15" />

    </@l.emailHtml>

</#escape>