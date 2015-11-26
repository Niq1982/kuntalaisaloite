<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="html" />

<#include "../includes/styles.ftl" />

<#escape x as x?html>

    <#assign title><@u.message "email.initiative" /></#assign>

    <@l.emailHtml title=title footer=false>

        <#if (initiative.sentComment)?has_content>
            <@b.comment type initiative.sentComment "email.sentComment" />
            <@u.spacer "15" />
        </#if>

        <@b.mainContentBlock title>
            <@b.initiativeDetails type=type showProposal=true showDate=true showExtraInfo=true />
        </@b.mainContentBlock>

        <@u.spacer "15" />

        <#if (attachmentCount > 0) || hasLocationAttached>
            <@b.contentBlock type>
                <@b.attachments type/>
            </@b.contentBlock>

            <@u.spacer "15" />
        </#if>

        <@b.contentBlock type>
            <#if removeHash??>
                Etkö halua saada sähköpostitiedotteita tästä aloitteesta? Lopeta aloitteen seuraaminen:
                <@u.button "Lopeta tilaus" urls.unsubscribe(initiativeId, removeHash) "green" />
            </#if>
        </@b.contentBlock>

        <@u.spacer "15" />


        <@b.emailFooter type ".sentToMunicipality" />

    </@l.emailHtml>

</#escape>