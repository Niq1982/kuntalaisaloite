<#import "../components/email-layout-html.ftl" as el />
<#import "../components/email-utils.ftl" as eu />
<#import "../components/email-blocks.ftl" as eb />

<#escape x as x?html> 

<#assign titleFi="Kuntalaisaloite - "+emailInfo.municipalityName!"" />
<#assign titleSv="Invånarinitiativ - "+emailInfo.municipalityName!"" />

<@el.emailHtml "municipality-initiative" titleFi>

    <#-- FINNISH -->
    <@eb.emailTemplate "fi" titleFi>
        <@eb.initiativeDetails "fi" "html" />
        
        <@eb.contactInfo "fi" "html" />
        
        <p style="margin:0 0 1em 0;">Tämä aloite lähetettiin Kuntalaisaloite.fi-palvelun kautta. Katso aloite palvelussa<br/><@eu.link emailInfo.url /></p>
        <p style="margin:0 0 1em 0;">Tähän sähköpostiin lähetetty vastaus menee suoraan aloitteen tekijän antamaan sähköpostiosoitteeseen.</p>
    </@eb.emailTemplate>
    
    <#-- SWEDISH -->
    <#-- TODO: Should use localizations      
    <@eb.emailTemplate "sv" titleSv>
        <@eb.initiativeDetails "sv" "html" />
        
        <@eb.contactInfo "sv" "html" />

        <p>Initiativet har skickats från <@eu.link "https://kuntalaisaloite.fi" "Invånarinitiativ.fi" />-webtjänst. Initiativet finns på adressen:<br/><@eu.link emailInfo.url /></p>
    </@eb.emailTemplate>
    -->

</@el.emailHtml>

</#escape>  