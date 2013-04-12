<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="html" />

<#include "../includes/styles.ftl" />

<#escape x as x?html>

<#assign title><@u.message "email.initiative" /> - ${initiative.municipality.getLocalizedName(locale)!""}</#assign>

<@l.emailHtml "municipality-collectable" title>

    <@b.contentBlock type>
        <p style="${pBottomMargin}">Kuntalaisaloite on luotu alla olevilla tiedoilla. Ylläpito-sivulta voit muokata aloitteen tietoja tai lähettää sen oikeusministeriön tarkastettavaksi.</p>
    </@b.contentBlock>

    <@u.spacer "15" />

    <@b.mainContentBlock title>
        <@b.initiativeDetails type />
    </@b.mainContentBlock>
    
    <@u.spacer "15" />
    
    <@b.contentBlock type>
        <@b.contactInfo type />
    </@b.contentBlock>
    
    <@u.spacer "15" />
    
    <@b.contentBlock type>
        <@b.adminViewLink type />
    </@b.contentBlock>
    
    <@u.spacer "15" />

</@l.emailHtml>

</#escape>