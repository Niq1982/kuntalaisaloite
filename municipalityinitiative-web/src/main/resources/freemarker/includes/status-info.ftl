<#import "../components/email-blocks.ftl" as b />

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
    <#assign statusTitleFi>Aloite on julkaistu Kuntalaisaloite.fi-palvelussa ja lähetetty kuntaan</#assign>
    <#assign statusTitleSv>Initiativet har granskats</#assign>
    <#assign statusInfoFi>
        Oikeusministeriö on tarkastanut kuntalaisaloitteesi ja hyväksynyt ja julkaissut Kuntalaisaloite.fi -palvelussa. Julkaisupyynnön yhteydessä valitsit, että aloite lähetetään samalla kuntaan. Aloite on nyt lähetetty kuntaan [KUNTA].

        Alla on linkki aloitteesi julkiselle sivulle Kuntalaisaloite.fi-palvelussa.
        [LINKKI TÄHÄN]
    </#assign>
    <#assign statusInfoSv>
        TODO
    </#assign>
    
    <#-- HTML -->
    <#assign statusTitleHTMLFi>${statusTitleFi}</#assign>
    <#assign statusTitleHTMLSv>${statusTitleSv}</#assign>
    <#assign statusInfoHTMLFi>
        <p>Oikeusministeriö on tarkastanut kansalaisaloitteen ja hyväksynyt sen julkaistavaksi kansalaisaloite.fi -palveluun. Aloitteen kannatusilmoitusten kerääminen alkaa yllä mainitusta päivämäärästä alkaen.</p>
    </#assign>
    <#assign statusInfoHTMLSv>
        TODO
    </#assign>
    
<#elseif emailMessageType == EmailMessageType.ACCEPTED_BY_OM>
    <#-- TEXT -->
    <#assign statusTitleFi>Aloite on tarkastettu</#assign>
    <#assign statusTitleSv>Initiativet har granskats</#assign>
    <#assign statusInfoFi>
        Oikeusministeriö on tarkastanut kansalaisaloitteen ja hyväksynyt sen julkaistavaksi kansalaisaloite.fi -palveluun. Aloitteen kannatusilmoitusten kerääminen alkaa yllä mainitusta päivämäärästä alkaen.
        
        Oikeusministeriön saate:
        ${stateComment!"Ei saatetta"}
    </#assign>
    <#assign statusInfoSv>
        Justitieministeriet har granskat medborgarinitiativet och godkänt det för publicering på webbtjänsten medborgarinitiativ.fi. Insamlingen av stödförklaringar för initiativet börjar från och med det datum som nämns ovan.
        
        Justitieministeriets följebrev:
        ${stateComment!"Ingen följebrev"}
    </#assign>
    
    <#-- HTML -->
    <#assign statusTitleHTMLFi>${statusTitleFi}</#assign>
    <#assign statusTitleHTMLSv>${statusTitleSv}</#assign>
    <#assign statusInfoHTMLFi>
        <p>Oikeusministeriö on tarkastanut kansalaisaloitteen ja hyväksynyt sen julkaistavaksi kansalaisaloite.fi -palveluun. Aloitteen kannatusilmoitusten kerääminen alkaa yllä mainitusta päivämäärästä alkaen.</p>
        <p><strong>Oikeusministeriön saate:</strong><br>${stateComment!"Ei saatetta"}</p>
    </#assign>
    <#assign statusInfoHTMLSv>
        <p>Justitieministeriet har granskat medborgarinitiativet och godkänt det för publicering på webbtjänsten medborgarinitiativ.fi. Insamlingen av stödförklaringar för initiativet börjar från och med det datum som nämns ovan.</p>
        <p><strong>Justitieministeriets följebrev:</strong><br>${stateComment!"Ingen följebrev"}</p>
    </#assign>
    
<#elseif emailMessageType == EmailMessageType.REJECTED_BY_OM>
    <#-- TEXT -->
    <#assign statusTitleFi>Aloite on palautettu korjattavaksi</#assign>
    <#assign statusTitleSv>Initiativet har skickats tillbaka för korrigering</#assign>
    <#assign statusInfoFi>
        Oikeusministeriö on tarkastanut kansalaisaloitteen ja palauttanut sen täydennettäväksi.
        
        Oikeusministeriön saate:
        ${stateComment!"Ei saatetta"}
    </#assign>
    <#assign statusInfoSv>
        Justitieministeriet har granskat medborgarinitiativet  och skickat tillbaka det för komplettering.
        
        Justitieministeriets följebrev:
         ${stateComment!"Ingen följebrev"}
    </#assign>
    
    <#-- HTML -->
    <#assign statusTitleHTMLFi>${statusTitleFi}</#assign>
    <#assign statusTitleHTMLSv>${statusTitleSv}</#assign>
    <#assign statusInfoHTMLFi>
        <p>Oikeusministeriö on tarkastanut kansalaisaloitteen ja palauttanut sen täydennettäväksi.</p>
        <p><strong>Oikeusministeriön saate:</strong><br>${stateComment!"Ei saatetta"}</p>
    </#assign>
    <#assign statusInfoHTMLSv>
        <p>Justitieministeriet har granskat medborgarinitiativet  och skickat tillbaka det för komplettering.</p>
        <p><strong>Justitieministeriets följebrev:</strong><br> ${stateComment!"Ingen följebrev"}</p>
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

