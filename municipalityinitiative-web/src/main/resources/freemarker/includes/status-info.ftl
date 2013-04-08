<#import "../components/email-blocks.ftl" as b />

<#include "../includes/styles.ftl" />

<#--
 * Includes common status infos for generic emails.
 *
 * Includes both TEXT and HTML versions. TEXT version is used as a default value.
 *
 * Types:
 *  ACCEPTED_BY_OM_AND_SENT,
 *  ACCEPTED_BY_OM,
 *  REJECTED_BY_OM,
 *  INVITATION_ACCEPTED,
 *  INVITATION_REJECTED,
 *  COLLECTING,
 *  SENT_TO_MUNICIPALITY
 *
 *
 * TODO: Localize
-->

<#if emailMessageType == EmailMessageType.ACCEPTED_BY_OM_AND_SENT>
    <#-- TEXT -->
    <#assign statusTitleFi>Kuntalaisaloite on julkaistu Kuntalaisaloite.fi-palvelussa ja lähetetty kuntaan</#assign>
    <#assign statusTitleSv>SV Kuntalaisaloite on julkaistu Kuntalaisaloite.fi-palvelussa ja lähetetty kuntaan</#assign>
    <#assign statusInfoFi>
        Oikeusministeriö on tarkastanut kuntalaisaloitteesi ja hyväksynyt sekä samalla julkaissut sen Kuntalaisaloite.fi-palvelussa. Julkaisupyynnön yhteydessä valitsit, että aloite lähetetään samalla kuntaan. Aloite on nyt lähetetty kuntaan [KUNTA].
        
        <@b.statusInfoComment "text" initiative.comment!"" />
        
        <@b.publicViewLink "text" />
    </#assign>
    <#assign statusInfoSv>
        SV TODO
    </#assign>
    
    <#-- HTML -->
    <#assign statusTitleHTMLFi>${statusTitleFi}</#assign>
    <#assign statusTitleHTMLSv>${statusTitleSv}</#assign>
    <#assign statusInfoHTMLFi>
        <p style="${pBothMargins!""}">
            Oikeusministeriö on tarkastanut kuntalaisaloitteesi ja hyväksynyt sekä samalla julkaissut sen Kuntalaisaloite.fi-palvelussa.
        </p>
        <p style="${pBothMargins!""}">
            Julkaisupyynnön yhteydessä valitsit, että aloite lähetetään samalla kuntaan. Aloite on nyt lähetetty kuntaan [KUNTA].
        </p>

        <@b.statusInfoComment "html" initiative.comment!"" />
        <@b.publicViewLink "html" />
    </#assign>
    <#assign statusInfoHTMLSv>
        SV TODO
    </#assign>
    
<#elseif emailMessageType == EmailMessageType.ACCEPTED_BY_OM>
    <#-- TEXT -->
    <#assign statusTitleFi>Kuntalaisaloite on hyväksytty</#assign>
    <#assign statusTitleSv>SV Kuntalaisaloite on hyväksytty</#assign>
    <#assign statusInfoFi>
        Oikeusministeriö on tarkastanut kuntalaisaloitteesi ja hyväksynyt sen Kuntalaisaloite.fi-palvelussa. Julkaisupyynnön yhteydessä valitsit, että haluat liittää aloitteeseen vastuuhenkilöitä ja kerätä osallistujia. Aloite ei ole julkinen palvelussa ennen kuin julkaiset sen. Voit nyt liittää aloitteeseen vastuuhenkilöitä. Aloitteeseen ei voi kerätä osallistujia ennen kuin se on julkaistu.
        
        <@b.statusInfoComment "text" initiative.comment!"" />
        
        <@b.adminViewLink "text" />
    </#assign>
    <#assign statusInfoSv>
        SV TODO
    </#assign>
    
    <#-- HTML -->
    <#assign statusTitleHTMLFi>${statusTitleFi}</#assign>
    <#assign statusTitleHTMLSv>${statusTitleSv}</#assign>
    <#assign statusInfoHTMLFi>
        <p style="${pBothMargins!""}">
            Oikeusministeriö on tarkastanut kuntalaisaloitteesi ja hyväksynyt sen Kuntalaisaloite.fi-palvelussa. Julkaisupyynnön yhteydessä valitsit, että haluat liittää aloitteeseen vastuuhenkilöitä ja kerätä osallistujia. Aloite ei ole julkinen palvelussa ennen kuin julkaiset sen. Voit nyt liittää aloitteeseen vastuuhenkilöitä. Aloitteeseen ei voi kerätä osallistujia ennen kuin se on julkaistu.
        </p>
        
        <@b.statusInfoComment "html" initiative.comment!"" />
        <@b.adminViewLink "html" />
    </#assign>
    <#assign statusInfoHTMLSv>
        SV TODO
    </#assign>
    
<#elseif emailMessageType == EmailMessageType.REJECTED_BY_OM>
    <#-- TEXT -->
    <#assign statusTitleFi>Kuntalaisaloite on palautettu korjattavaksi</#assign>
    <#assign statusTitleSv>SV Kuntalaisaloite on palautettu korjattavaksi</#assign>
    <#assign statusInfoFi>
        Oikeusministeriö on tarkastanut kuntalaisaloitteesi ja palauttanut sen korjattavaksi. Voit jälleen muokata aloitteen otsikkoa sekä sisältöä. Korjauksen jälkeen voit lähettää aloitteen uudelleen oikeusministeriön tarkastettavaksi.
        
        <@b.statusInfoComment "text" initiative.comment!"" />

        <@b.adminViewLink "text" />
    </#assign>
    <#assign statusInfoSv>
        SV TODO
    </#assign>
    
    <#-- HTML -->
    <#assign statusTitleHTMLFi>${statusTitleFi}</#assign>
    <#assign statusTitleHTMLSv>${statusTitleSv}</#assign>
    <#assign statusInfoHTMLFi>
        <p style="${pBothMargins!""}">
            Oikeusministeriö on tarkastanut kuntalaisaloitteesi ja palauttanut sen korjattavaksi. Voit jälleen muokata aloitteen otsikkoa sekä sisältöä. Korjauksen jälkeen voit lähettää aloitteen uudelleen oikeusministeriön tarkastettavaksi.
        </p>
        
        <@b.statusInfoComment "html" initiative.comment!"" />
        
        <@b.adminViewLink "html" />
    </#assign>
    <#assign statusInfoHTMLSv>
        SV TODO
    </#assign>
    
<#elseif emailMessageType == EmailMessageType.INVITATION_ACCEPTED>
    <#-- TEXT -->
    <#assign statusTitleFi>Kutsu vastuuhenkilöksi on hyväksytty</#assign>
    <#assign statusTitleSv>Den tilltänkta ansvarspersonen har godkänt inbjudan</#assign>
    <#assign statusInfoFi>
        TODO
    </#assign>
    <#assign statusInfoSv>
        SV TODO
    </#assign>
    
    <#-- HTML -->
    <#assign statusTitleHTMLFi>${statusTitleFi}</#assign>
    <#assign statusTitleHTMLSv>${statusTitleSv}</#assign>
    <#assign statusInfoHTMLFi>
        TODO
    </#assign>
    <#assign statusInfoHTMLSv>
        SV TODO
    </#assign>

<#elseif emailMessageType == EmailMessageType.INVITATION_REJECTED>
    <#-- TEXT -->
    <#assign statusTitleFi>Kutsu vastuuhenkilöksi on hylätty</#assign>
    <#assign statusTitleSv>Den tilltänkta ansvarspersonen har avböjt inbjudan</#assign>
    <#assign statusInfoFi>
        TODO
    </#assign>
    <#assign statusInfoSv>
        SV TODO
    </#assign>
    
    <#-- HTML -->
    <#assign statusTitleHTMLFi>${statusTitleFi}</#assign>
    <#assign statusTitleHTMLSv>${statusTitleSv}</#assign>
    <#assign statusInfoHTMLFi>
        TODO
    </#assign>
    <#assign statusInfoHTMLSv>
        SV TODO
    </#assign>

<#elseif emailMessageType == EmailMessageType.COLLECTING>
    <#-- TEXT -->
    <#assign statusTitleFi>TODO</#assign>
    <#assign statusTitleSv>TODO</#assign>
    <#assign statusInfoFi>TODO</#assign>
    <#assign statusInfoSv>TODO</#assign>
    
    <#-- HTML -->
    <#assign statusTitleHTMLFi>${statusTitleFi}</#assign>
    <#assign statusTitleHTMLSv>${statusTitleSv}</#assign>
    <#assign statusInfoHTMLFi>${statusInfoFi}</#assign>
    <#assign statusInfoHTMLSv>${statusInfoSv}</#assign>
    
<#elseif emailMessageType == EmailMessageType.SENT_TO_MUNICIPALITY>
    <#-- TEXT -->
    <#assign statusTitleFi>Kuntalaisaloite on lähetetty kuntaan [KUNTA]</#assign>
    <#assign statusTitleSv>SV Kuntalaisaloite on lähetetty kuntaan [KUNTA]</#assign>
    <#assign statusInfoFi>
        Vähemmän koivuja Joutsan keskustaan
        Aloite luotu Kuntalaisaloite.fi-palveluun: 04.04.2013 
        Lähetetty kuntaan: 04.04.2013
        
        [SISÄLTÖ]
        
        Yhteystiedot
        [YHTEYSTIEDOT]
    </#assign>
    <#assign statusInfoSv>
        SV TODO
    </#assign>
    
    <#-- HTML -->
    <#assign statusTitleHTMLFi>${statusTitleFi}</#assign>
    <#assign statusTitleHTMLSv>${statusTitleSv}</#assign>
    <#assign statusInfoHTMLFi>
        Vähemmän koivuja Joutsan keskustaan
        Aloite luotu Kuntalaisaloite.fi-palveluun: 04.04.2013 
        Lähetetty kuntaan: 04.04.2013
        
        [SISÄLTÖ]
        
        Yhteystiedot
        [YHTEYSTIEDOT]    
    </#assign>
    <#assign statusInfoHTMLSv>
        SV TODO
    </#assign>
    
</#if>

