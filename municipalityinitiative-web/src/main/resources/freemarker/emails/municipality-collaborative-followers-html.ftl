<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="html" />

<#include "../includes/styles.ftl" />

<#escape x as x?html>

    <#assign title><@u.message "email.follow.sent.to.municipality" /></#assign>

    <@l.emailHtml title=title footer=false>

        <@b.mainContentBlock title>
            <h4 style="${h4!""}">${initiative.name!""}</h4>
            <p style="${pBottomMargin!""}">${initiative.municipality.getLocalizedName(switchLocale!locale)!""}</p>

        </@b.mainContentBlock>

        <@u.spacer "15" />

        <#if (attachmentCount > 0) || hasLocationAttached>
            <@b.contentBlock type>
                <@b.attachments type/>
            </@b.contentBlock>

            <@u.spacer "15" />
        </#if>

        <@b.contentBlock type>
            <h4 style="${h4!""}"><@u.message "email.sentToMunicipality" /></h4>
            <p style="${pBottomMargin!""}"><@u.messageHTML key="email.participantCount.total" /> ${initiative.participantCount!"0"}</p>
            <@b.publicViewLink type/>
        </@b.contentBlock>

        <@u.spacer "15" />

        <@b.contentBlock type>
            <#if removeHash??>
                <@b.unfollow initiativeId removeHash type/>
            </#if>
        </@b.contentBlock>

        <@u.spacer "15" />


        <@b.emailFooter type ".sentToMunicipality" />

    </@l.emailHtml>

</#escape>