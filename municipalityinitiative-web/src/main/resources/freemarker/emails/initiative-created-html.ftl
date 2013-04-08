<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#include "../includes/styles.ftl" />

<#escape x as x?html>

<#assign title><@u.message "email.initiative" /> - ${initiative.municipality.name!""}</#assign>

<@l.emailHtml "municipality-collectable" title>

    <@b.contentBlock "html">
        <p style="${pBottomMargin}">Kuntalaisaloite on luotu alla olevilla tiedoilla. Ylläpito-sivulta voit muokata aloitteen tietoja tai lähettää sen oikeusministeriön tarkastettavaksi.</p>
    </@b.contentBlock>

    <@u.spacer "15" />

    <@b.mainContentBlock title>
        <@b.initiativeDetails "html" />
    </@b.mainContentBlock>
    
    <@u.spacer "15" />
    
    <@b.contentBlock "html">
        <@b.contactInfo "html" />
    </@b.contentBlock>
    
    <@u.spacer "15" />
    
    <@b.contentBlock "html">
        <@b.adminViewLink "html" />
    </@b.contentBlock>
    
    <@u.spacer "15" />

</@l.emailHtml>

</#escape>