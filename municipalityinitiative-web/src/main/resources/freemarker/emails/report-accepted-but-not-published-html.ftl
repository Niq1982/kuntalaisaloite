<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="html" />

<#include "../includes/styles.ftl" />

<#assign keyPrefix="email.report.accepted.not.published" />

<#escape x as x?html>

    <#assign title><@u.message keyPrefix+".title" /></#assign>

    <@l.emailHtml title>

        <@b.mainContentBlock title>
            <@b.initiativeDetails type />

            <p style="${pBothMargins!""}"><@u.message keyPrefix+".description"/> <@u.localDate initiative.stateTime/></p>
            <p style="${pBothMargins!""}"><@u.message keyPrefix+".description.2" /></p>

            <@b.adminViewLink type=type verified=initiative.type.verifiable />
        </@b.mainContentBlock>

        <@u.spacer "15" />

        <@b.emailFooter type />

        <@u.spacer "15" />





        <#global switchLocale = altLocale />







        <@b.mainContentBlock title>
            <@b.initiativeDetails type />

        <p style="${pBothMargins!""}"><@u.message keyPrefix+".description"/> <@u.localDate initiative.stateTime/></p>
        <p style="${pBothMargins!""}"><@u.message keyPrefix+".description.2" /></p>

            <@b.adminViewLink type=type verified=initiative.type.verifiable />
        </@b.mainContentBlock>

        <@u.spacer "15" />

        <@b.emailFooter type />

        <@u.spacer "15" />




        <#global switchLocale = altLocale />


    </@l.emailHtml>

</#escape>