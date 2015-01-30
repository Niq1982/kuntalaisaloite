<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="html" />

<#include "../includes/styles.ftl" />

<#assign keyPrefix="email.report.quarter" />

<#assign publishDateFormatted><@u.localDate initiative.stateTime/></#assign>

<#escape x as x?html>

    <#assign title><@u.message keyPrefix+".title" /></#assign>

    <@l.emailHtml title=title footer=false>

        <@b.mainContentBlock title>
            <@b.initiativeDetails type />

            <p style="${pBothMargins!""}"><@u.message key=keyPrefix+".description" args=[publishDateFormatted, initiative.participantCount]/></p>
            <p style="${pBothMargins!""}"><@u.message keyPrefix+".description.2" /></p>

            <@b.adminViewLink type=type verified=initiative.type.verifiable />
        </@b.mainContentBlock>

        <@u.spacer "15" />

        <@b.emailFooter type />

        <@u.spacer "15" />





        <#global switchLocale = altLocale />


        <#assign titleSv><@u.message keyPrefix+".title" /></#assign>

        <@b.mainContentBlock titleSv>
            <@b.initiativeDetails type />

        <p style="${pBothMargins!""}"><@u.message key=keyPrefix+".description" args=[publishDateFormatted, initiative.participantCount]/></p>
        <p style="${pBothMargins!""}"><@u.message keyPrefix+".description.2" /></p>

            <@b.adminViewLink type=type verified=initiative.type.verifiable />
        </@b.mainContentBlock>

        <@u.spacer "15" />

        <@b.emailFooter type />

        <@u.spacer "15" />

        <#global switchLocale = altLocale />


    </@l.emailHtml>

</#escape>