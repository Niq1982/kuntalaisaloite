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
 * TODO: emailMessageType AND EmailMessageType.class
-->

<#--<#if emailMessageType == EmailMessageType.ACCEPTED_BY_OM_AND_SENT>-->
<#if true>
    <#-- TEXT -->
    <#assign statusTitleFi>Kuntalaisaloite on julkaistu Kuntalaisaloite.fi-palvelussa ja lähetetty kuntaan</#assign>
    <#assign statusTitleSv>SV Kuntalaisaloite on julkaistu Kuntalaisaloite.fi-palvelussa ja lähetetty kuntaan</#assign>
    <#assign statusInfoFi>
        Oikeusministeriö on tarkastanut kuntalaisaloitteesi ja hyväksynyt sekä samalla julkaissut sen Kuntalaisaloite.fi-palvelussa. Julkaisupyynnön yhteydessä valitsit, että aloite lähetetään samalla kuntaan. Aloite on nyt lähetetty kuntaan [KUNTA].
        
        <@b.statusInfoComment "text" />
        
        Alla on linkki aloitteesi julkiselle sivulle Kuntalaisaloite.fi-palvelussa.
        [LINKKI TÄHÄN]
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

        <@b.statusInfoComment "html" />

        <p style="${pBothMargins!""}">
            Alla on linkki aloitteesi julkiselle sivulle Kuntalaisaloite.fi-palvelussa.<br />
            [LINKKI TÄHÄN]
        </p>
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
        
        Oikeusministeriön saate:
        ${stateComment!"Ei saatetta"}
        
        Siirry kuntalaisaloitteen ylläpito-sivulle alla olevalla linkillä
        [LAITETAANKO YLLÄPITO-LINKKI TÄHÄN?]
    </#assign>
    <#assign statusInfoSv>
        SV TODO
    </#assign>
    
    <#-- HTML -->
    <#assign statusTitleHTMLFi>${statusTitleFi}</#assign>
    <#assign statusTitleHTMLSv>${statusTitleSv}</#assign>
    <#assign statusInfoHTMLFi>
        Oikeusministeriö on tarkastanut kuntalaisaloitteesi ja hyväksynyt sen Kuntalaisaloite.fi-palvelussa. Julkaisupyynnön yhteydessä valitsit, että haluat liittää aloitteeseen vastuuhenkilöitä ja kerätä osallistujia. Aloite ei ole julkinen palvelussa ennen kuin julkaiset sen. Voit nyt liittää aloitteeseen vastuuhenkilöitä. Aloitteeseen ei voi kerätä osallistujia ennen kuin se on julkaistu.
        <br /><br />
        Oikeusministeriön saate:<br />
        ${stateComment!"Ei saatetta"}
        <br /><br />
        Siirry kuntalaisaloitteen ylläpito-sivulle alla olevalla linkillä<br />
        [LAITETAANKO YLLÄPITO-LINKKI TÄHÄN?]
        <br /><br />
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
        
        Oikeusministeriön saate:
        ${stateComment!"Ei saatetta"}

        Siirry kuntalaisaloitteen ylläpito-sivulle alla olevalla linkillä
        [LAITETAANKO YLLÄPITO-LINKKI TÄHÄN?]
        
    </#assign>
    <#assign statusInfoSv>
        SV TODO
    </#assign>
    
    <#-- HTML -->
    <#assign statusTitleHTMLFi>${statusTitleFi}</#assign>
    <#assign statusTitleHTMLSv>${statusTitleSv}</#assign>
    <#assign statusInfoHTMLFi>
        Oikeusministeriö on tarkastanut kuntalaisaloitteesi ja palauttanut sen korjattavaksi. Voit jälleen muokata aloitteen otsikkoa sekä sisältöä. Korjauksen jälkeen voit lähettää aloitteen uudelleen oikeusministeriön tarkastettavaksi.
        <br /><br />
        Oikeusministeriön saate:<br />
        ${stateComment!"Ei saatetta"}
        <br /><br />
        Siirry kuntalaisaloitteen ylläpito-sivulle alla olevalla linkillä<br />
        [LAITETAANKO YLLÄPITO-LINKKI TÄHÄN?]
        <br />
    </#assign>
    <#assign statusInfoHTMLSv>
        SV TODO
    </#assign>
    
<#elseif emailMessageType == EmailMessageType.INVITATION_ACCEPTED>
    <#-- TEXT -->
    <#assign statusTitleFi>Kutsu vastuuhenkilöksi on hyväksytty</#assign>
    <#assign statusTitleSv>Den tilltänkta ansvarspersonen har godkänt inbjudan</#assign>
    <#assign statusInfoFi>
        Aloitteen vastuuhenkilöksi kutsuttu "${currentAuthor.firstNames} ${currentAuthor.lastName}" on hyväksynyt kutsun.
        <@b.initiativeStatus "fi" "text" />
    </#assign>
    <#assign statusInfoSv>
        "${currentAuthor.firstNames} ${currentAuthor.lastName}" har godkänt inbjudan att bli ansvarsperson för initiativet.
        <@b.initiativeStatus "sv" "text" />
    </#assign>
    
    <#-- HTML -->
    <#assign statusTitleHTMLFi>${statusTitleFi}</#assign>
    <#assign statusTitleHTMLSv>${statusTitleSv}</#assign>
    <#assign statusInfoHTMLFi>
        <p style="margin-top:0.5em;">Aloitteen vastuuhenkilöksi kutsuttu <strong>${currentAuthor.firstNames} ${currentAuthor.lastName}</strong> on hyväksynyt kutsun.</p>
        
        <@b.initiativeStatus "fi" "html" />
    </#assign>
    <#assign statusInfoHTMLSv>
        <p style="margin-top:0.5em;"><strong>${currentAuthor.firstNames} ${currentAuthor.lastName}</strong> har godkänt inbjudan att bli ansvarsperson för initiativet.</p>
        
        <@b.initiativeStatus "sv" "html" />
    </#assign>

<#elseif emailMessageType == EmailMessageType.INVITATION_REJECTED>
    <#-- TEXT -->
    <#assign statusTitleFi>Kutsu vastuuhenkilöksi on hylätty</#assign>
    <#assign statusTitleSv>Den tilltänkta ansvarspersonen har avböjt inbjudan</#assign>
    <#assign statusInfoFi>
        Aloitteen vastuuhenkilöksi kutsuttu "${rejectedEmail!""}" on hylännyt kutsun.
        <@b.initiativeStatus "fi" "text" />
    </#assign>
    <#assign statusInfoSv>
        "${rejectedEmail!""}" har avböjt inbjudan att bli ansvarsperson för initiativet.
        <@b.initiativeStatus "sv" "text" />
    </#assign>
    
    <#-- HTML -->
    <#assign statusTitleHTMLFi>${statusTitleFi}</#assign>
    <#assign statusTitleHTMLSv>${statusTitleSv}</#assign>
    <#assign statusInfoHTMLFi>
        <p style="margin-top:0.5em;">Aloitteen vastuuhenkilöksi kutsuttu <@eu.link "mailto:"+rejectedEmail rejectedEmail /> on hylännyt kutsun.</p>
        
        <@b.initiativeStatus "fi" "html" />
    </#assign>
    <#assign statusInfoHTMLSv>
        <p style="margin-top:0.5em;"><@eu.link "mailto:"+rejectedEmail rejectedEmail /> har avböjt inbjudan att bli ansvarsperson för initiativet.</p>
        
        <@b.initiativeStatus "sv" "html" />
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
    <#assign statusTitleFi>Kansalaisloite on lähetetty tarkastettavaksi oikeusministeriöön</#assign>
    <#assign statusTitleSv>Medborgarinitiativet har skickats till justitieministeriet för granskning</#assign>
    <#assign statusInfoFi>
        Kansalaisloite on lähetetty oikeusministeriölle tarkastettavaksi.
        
        Oikeusministeriö tarkastaa, että kansalaisaloite.fi-palveluun tehdyissä aloitteissa on tarvittavat tiedot ja ettei aloite sisällä verkossa julkaistavaksi sopimatonta materiaalia. Tämän jälkeen aloitteelle voi kerätä kannatusilmoituksia. Aloitteita käsitellään oikeusministeriössä arkisin virka-aikana ja tarkastus kestää muutamia päiviä. Saat tietoa tarkastuksen etenemisestä sähköpostitse.
    </#assign>
    <#assign statusInfoSv>
        Medborgarinitiativet har skickats till justitieministeriet för granskning.
    
        Justitieministeriet granskar att de initiativ som gjorts på webbtjänsten medborgarinitiativ.fi har den information som krävs och att initiativet inte innehåller material som är olämpligt att publicera på internet. Därefter kan insamlingen av stödförklaringar för initiativet påbörjas. Justitieministeriet behandlar initiativ på vardagar under tjänstetid och granskningen tar några dagar. Du får information om hur granskningen framskrider per e-post.
    </#assign>
    
    <#-- HTML -->
    <#assign statusTitleHTMLFi>${statusTitleFi}</#assign>
    <#assign statusTitleHTMLSv>${statusTitleSv}</#assign>
    <#assign statusInfoHTMLFi>
        <p>Kansalaisloite on lähetetty oikeusministeriölle tarkastettavaksi.</p>
        <p>Oikeusministeriö tarkastaa, että kansalaisaloite.fi-palveluun tehdyissä aloitteissa on tarvittavat tiedot ja ettei aloite sisällä verkossa julkaistavaksi sopimatonta materiaalia. Tämän jälkeen aloitteelle voi kerätä kannatusilmoituksia. Aloitteita käsitellään oikeusministeriössä arkisin virka-aikana ja tarkastus kestää muutamia päiviä. Saat tietoa tarkastuksen etenemisestä sähköpostitse.</p>    
    </#assign>
    <#assign statusInfoHTMLSv>
        <p>Medborgarinitiativet har skickats till justitieministeriet för granskning.</p>
        <p>Justitieministeriet granskar att de initiativ som gjorts på webbtjänsten medborgarinitiativ.fi har den information som krävs och att initiativet inte innehåller material som är olämpligt att publicera på internet. Därefter kan insamlingen av stödförklaringar för initiativet påbörjas. Justitieministeriet behandlar initiativ på vardagar under tjänstetid och granskningen tar några dagar. Du får information om hur granskningen framskrider per e-post.</p>
    </#assign>
    
</#if>

