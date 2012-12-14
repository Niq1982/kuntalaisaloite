<#import "../components/utils.ftl" as u />

<#if locale == "fi">
    
    <p>Kansalaisaloite.fi on uusi palvelu ja kansalaisaloite uusi valtiollisen tason vaikuttamiskeino.</p> 
    
    <p>Toivomme, että verkkopalvelu on helppokäyttöinen ja innostava. Kehitämme palvelua edelleen ja siksi palautteesi on arvokasta! Voit lähettää kysymyksiä. kritiikkiä, kiitoksia tai parannusehdotuksia mistä tahansa.</p> 
    
    <p>Lähetä palautetta osoitteeseen <@u.scrambleEmail "kansalaisaloite.om@om.fi" />.</p>
    
    <p>Seuraa kansalaisaloitetta <@u.link href="https://www.facebook.com/aloite" label="Facebookissa" rel="external" />.</p>
    
    <h3>Kansalaisaloite.fi-palvelun yhteyshenkilöt</h3>
    
    <h4>Kehitys ja ylläpito</h4>
    <p>projektipäällikkö <strong>Teemu Ropponen</strong><br/>
    <@u.scrambleEmail "etunimi.sukunumi@om.fi" />  tai <@u.scrambleEmail "kansalaisaloite.om@om.fi" /></p> 
    
    <h3>Kansalaisaloite.fi-palvelun kehittäminen</h3>
    
    <p>Haluatko osallistua kansalaisaloite.fi -palvelun tai muiden Osallistumisympäristö-hankkeen palveluiden kehittämiseen?</p>
    
    <p>Osallistumisympäristö tarjoaa sähköisiä palveluita kansalaisyhteiskunnan, julkisen hallinnon ja poliittisten päättäjien väliseen vuorovaikutukseen. Palvelut helpottavat kansalaisvaikuttamista ja tiedonsaantia sekä lisäävät päätösten valmistelun ja päätöksenteon läpinäkyvyyttä ja parantavat niiden laatua. Osallistumisympäristö-hanke kuuluu valtiovarainministeriön koordinoimaan Sähköisen asioinnin ja demokratian vauhdittamisohjelmaan (SADe-ohjelma).</p>
    
    <p>Seuraa hankeblogiamme <@u.link href="http://blogi.otakantaa.fi" label="http://blogi.otakantaa.fi" rel="external" /></p>    
    
    <p>Liity <@u.link href="https://www.facebook.com/#!/groups/osallistumisymparisto/" label="kehittäjäyhteisöömme" rel="external" /></p> 
    
    <#assign href>${urls.infoIndex()}/<@u.enumDescription InfoPage.DEVELOPERS /></#assign>
    <p><@u.link href=href label="Lue lisää avoimen datan rajapinnasta" /></p>
    
    
    

<#else>

    <p>Medborgarinitiativ.fi är en ny tjänst och medborgarinitiativet är ett nytt sätt att påverka på statlig nivå.</p> 

    <p>Vi hoppas att webbtjänsten är lätt att använda och inspirerande. Vi utvecklar fortsättningsvis tjänsten och därför är din respons värdefull! Du kan skicka frågor, kritik, tack eller förbättringsförslag om vad som helst.</p> 
    
    <p>Skicka din respons till adressen <@u.scrambleEmail "kansalaisaloite.om@om.fi" />.</p>
    
    <p>Följ medborgarinitiativet på <@u.link href="https://www.facebook.com/aloite" label="Facebook" rel="external" />.</p>
    
    <h3>Kontaktpersoner för tjänsten medborgarinitiativ.fi</h3>
    
    <h4>Utveckling och administrering</h4>
    <p>projektchef <strong>Teemu Ropponen</strong><br/>
    <@u.scrambleEmail "förnamn.efternamn@om.fi" />  eller <@u.scrambleEmail "kansalaisaloite.om@om.fi" /></p> 
    
    <h3>Utveckling av tjänsten medborgarinitiativ.fi</h3>
    
    <p>Vill du delta i utvecklingen av tjänsten medborgarinitiativ.fi eller övriga tjänster inom projektet Plattform för delaktighet?</p>
    
    <p>Plattform för delaktighet erbjuder nättjänster för att stöda interaktionen mellan medborgarsamhället, den offentliga förvaltningen och de politiska beslutsfattarna. Tjänsterna underlättar medborgarnas möjligheter att påverka och få information, samt ökar transparensen vid beredningen och beslutsfattandet av ärenden och förbättrar deras kvalitet. Projektet Plattform för delaktighet ingår i Programmet för att påskynda elektronisk ärendehantering och demokrati (SADe), som koordineras av finansministeriet.</p>
    
    <p>Följ vår projektblogg <@u.link href="http://blogi.otakantaa.fi" label="http://blogi.otakantaa.fi" rel="external" /></p>    
    
    <p>Kom med <@u.link href="https://www.facebook.com/#!/groups/osallistumisymparisto/" label="i vår utvecklargrupp" rel="external" /></p> 
    
    <#assign href>${urls.infoIndex()}/<@u.enumDescription InfoPage.DEVELOPERS /></#assign>
    <p><@u.link href=href label="Läs med om gränssnitt för öppen data" /></p>

</#if>