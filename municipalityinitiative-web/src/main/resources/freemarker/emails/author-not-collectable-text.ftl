

Aloite toimitettu kuntaan.

Otsikko:
${emailInfo.name}

Ehdotus:
${emailInfo.proposal}

Muuta shaibaa
${emailInfo.url}
${emailInfo.municipalityName}

${localizations.getMessage("initiative.currentAuthor.contactInfo")}
${emailInfo.contactInfo.name}
${emailInfo.contactInfo.address!""}
${emailInfo.contactInfo.phone!""}
${emailInfo.contactInfo.email}

<#-- TODO: Txt version

<#import "../components/email-layout-html.ftl" as el />
<#import "../components/email-utils.ftl" as eu />
<#import "../components/email-blocks.ftl" as eb />

<#escape x as x?html>

<#assign titleFi="Aloite toimitettu kuntaan" />
<#assign titleSv="SV Aloite toimitettu kuntaan" />

<@el.emailHtml "sent-to-municipality" titleFi>

    <@eb.emailTemplate titleFi>
        <@eb.initiativeDetails "html" />
        
        Otsikko:
        ${emailInfo.name}
        
        Ehdotus:
        ${emailInfo.proposal}
        
        Muuta shaibaa
        ${emailInfo.url}
        ${emailInfo.municipalityName}
        
        Yhteystietoi:
        ${emailInfo.contactInfo.name}
        ${emailInfo.contactInfo.address!""}
        ${emailInfo.contactInfo.phone!""}
        ${emailInfo.contactInfo.email}
        
    </@eb.emailTemplate>


</@el.emailHtml>

</#escape> 
-->
