<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="html" />

<#include "../includes/styles.ftl" />

<#escape x as x?html>

<#assign title><@u.message "email.initiative" /> - ${initiative.municipality.getLocalizedName(locale)!""}</#assign>

<@l.emailHtml "municipality-collectable" title>

    <@b.mainContentBlock title>
        <p style="${pBothMargins!""}">Tämä on vahvistusviesti kuntalaisaloitteelle, jonka tekemisen aloitit Kuntalaisaloite.fi-palvelussa.</p>
        
        <p style="${pBothMargins!""}">Sinun täytyy vielä vahvistaa aloitteen luominen palvelussa klikkaamalla alla olevaa linkkiä ja syöttämällä aloitteen tiedot.</p>
        <p style="${smallFont!""}"><@u.link urls.loginAuthor(initiative.id, initiative.managementHash.value) urls.loginAuthor(initiative.id, initiative.managementHash.value) /></p>

        <p style="${pBothMargins!""}">Ylläpito-linkki on henkilökohtainen ja sen avulla pääset myöhemmässä vaiheessa ylläpitämään aloitetta. Säilytä linkki äläkä jaa sitä muille.</p>
        
        <h4 style="${h4}">Ylläpito-sivulla voit</h4>
        <ol style="margin-top:0.5em;">
            <li style="margin-top:0.5em; margin-bottom:0.5em;">Voit muokata aloitteen tietoja</li>
            <li style="margin-bottom:0.5em;">Voit lähettää aloitteen oikeusministeriön tarkastettavaksi</li>
            <li style="margin-bottom:0.5em;">Voit lähettää aloitteen kuntaan tai liittää siihen vastuuhenkilöitä sekä kerätä osallistujia</li>
        </ol>
    </@b.mainContentBlock>
    
    <@u.spacer "15" />

</@l.emailHtml>

</#escape>