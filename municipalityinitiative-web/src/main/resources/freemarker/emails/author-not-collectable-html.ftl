<#import "../components/email-layout-html.ftl" as el />
<#import "../components/email-utils.ftl" as eu />
<#import "../components/email-blocks.ftl" as eb />

<#escape x as x?html> 

<#assign titleFi="Aloite toimitettu kuntaan "+emailInfo.municipalityName!"" />
<#assign titleSv="Initiativet har skicats till "+emailInfo.municipalityName!"" />

<@el.emailHtml "sent-to-municipality" titleFi>

    <#-- FINNISH -->
    <@eb.emailTemplate "fi" titleFi>
        <@eb.initiativeDetails "fi" "html" />
        
        <@eb.contactInfo "fi" "html" />

        <p>Siirry käsittelemään aloitetta:<br/><@eu.link emailInfo.url /></p>
    </@eb.emailTemplate>
    
    <#-- SWEDISH -->      
    <@eb.emailTemplate "sv" titleSv>
        <@eb.initiativeDetails "sv" "html" />
        
        <@eb.contactInfo "sv" "html" />

        <p>Övergå till att behandla initiativet:<br/><@eu.link emailInfo.url /></p>
    </@eb.emailTemplate>

</@el.emailHtml>

</#escape>
