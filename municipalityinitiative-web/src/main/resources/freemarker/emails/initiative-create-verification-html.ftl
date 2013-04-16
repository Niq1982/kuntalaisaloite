<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="html" />

<#include "../includes/styles.ftl" />

<#escape x as x?html>

<#assign title><@u.message "email.initiative" /> - ${initiative.municipality.getLocalizedName(locale)!""}</#assign>

<@l.emailHtml "municipality-collectable" title>

    <#-- TODO: localize -->
    <@b.mainContentBlock title>
        <p style="${pBothMargins!""}">Tämä sähköposti sisältää linkin uuden kuntalaisaloitteen luomiseen Kuntalaisaloite.fi-palvelussa. Klikkaa alla olevaa linkkiä ja siirry täyttämään aloitteen sisältö.</p>
        
        <p style="${smallFont!""}"><@u.link urls.loginAuthor(initiative.id, initiative.managementHash.value) urls.loginAuthor(initiative.id, initiative.managementHash.value) /></p>

        <p style="${pBothMargins!""}">Kun olet täyttänyt aloitteen sisällön ja tallentanut sen, pääset jatkossa tämän linkin avulla aloitteen ylläpitosivulle. Ylläpitolinkki on henkilökohtainen. Säilytä linkki äläkä jaa sitä muille.</p>
        
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