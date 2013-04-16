<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="html" />

<#include "../includes/styles.ftl" />

<#escape x as x?html>

<#assign title><@u.message "email.initiative" /> - ${initiative.municipality.getLocalizedName(locale)!""}</#assign>

<@l.emailHtml "municipality-collectable" title>

    <@b.mainContentBlock title>
        <p style="${pBothMargins!""}">Tämä on vahvistusviesti osallistumisestasi kuntalaisaloitteeseen.</p>

        <p>Aloite:</p><p>${initiative.name}</p>

        <p style="${pBothMargins!""}">Sinun täytyy vielä vahvistaa osallistumisesi klikkaamalla alla olevaa linkkiä.</p>
        <p style="${smallFont!""}"><@u.link urls.confirmParticipant(initiative.id, confirmationCode) urls.confirmParticipant(initiative.id, confirmationCode) /></p>
    </@b.mainContentBlock>

    <@u.spacer "15" />

</@l.emailHtml>

</#escape>