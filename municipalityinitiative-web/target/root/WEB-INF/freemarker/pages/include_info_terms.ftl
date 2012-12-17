<#import "../components/utils.ftl" as u />

<#if locale == "fi">
    
    <h3 id="h1">1. Kansalaisaloite.fi -palvelun käyttötarkoitus</h3>
    
    <p>1.3.2012 alkaen Suomessa on käytössä uusi valtiollisen tason vaikuttamiskeino, kansalaisaloite. Se antaa kansalaisille mahdollisuuden saada aloitteensa eduskunnan käsiteltäväksi. Eduskunta käsittelee aloitteen, joka saa taakseen vähintään 50 000 äänioikeutettua kannattajaa kuudessa kuukaudessa.</p>
    <p>Kansalaisaloite.fi -verkkopalvelussa voit tehdä, kannattaa ja seurata muiden tekemiä kansalaisaloitteita. Palvelun käyttö on turvallista ja maksutonta. Voit käyttää kansalaisaloite.fi -palvelua myös ruotsiksi.</p>
    <p>Velvoite palvelun tuottamiseen perustuu kansalaisaloitelakiin. Palvelua ylläpitää oikeusministeriö (Demokratia-, kieli- ja perusoikeusasioiden yksikkö).</p> 
    
    <h3 id="h2">2. Käyttäjän tunnistautuminen</h3>
    
    <p>Kansalaisaloitteita voi selata tunnistautumatta tai rekisteröitymättä palveluun.</p>
    <p>Kannatusilmoitusten verkkokeräyksessä käytetään vahvasta sähköisestä tunnistamisesta ja sähköisistä allekirjoituksista annetussa laissa (617/2009) tarkoitettua vahvaa sähköistä tunnistamista.</p>
    <p>Vahva tunnistaminen tarkoittaa käyttäjän henkilöllisyyden varmentamista verkkopankkitunnuksilla, mobiili-varmenteella tai sirullisella henkilökortilla (HST-kortilla).</p>
    
    <p>Katso myös</p>
    <ul>
        <#assign href>${urls.infoIndex()}/<@u.enumDescription InfoPage.PERSON_REGISTER /></#assign>
        <li><@u.link href=href label="Henkilörekisteriselosteeet" /></li>
        <#assign href>${urls.infoIndex()}/<@u.enumDescription InfoPage.PRIVACY /></#assign>
        <li><@u.link href=href label="Henkilötietojen suoja ja tietoturva" /></li>
    </ul>
    
    <h3 id="h3">3. Kansalaisaloitteen kielet</h3>
    
    <p>Kansalaisaloite.fi -palvelu on kaksikielinen.</p> 
    <p>Kansalaisaloite tehdään suomen tai ruotsin kielellä taikka molemmilla kielillä.</p>
    
    <h3 id="h4">4. Kansalaisaloitteiden tarkastaminen ja julkaisu</h3>
    
    <p>Oikeusministeriön tarkastuksessa käydään läpi, että aloitteen tarvittavat tiedot on täytetty ja ettei aloite sisällä verkossa julkaistavaksi sopimatonta materiaalia. Oikeusministeriön tarkastus ei muutoin ota kantaa aloitteen sisältöön. Oikeusministeriön tarkastuksen jälkeen aloitteen pysyviä tietoja ei voi enää muuttaa.</p>
    
    <p>Aloitteita käsitellään oikeusministeriössä virka-aikana ja tarkastus kestää arviolta muutamia päiviä.</p>
    <p>Kansalaisaloite voidaan jättää julkaisematta tai voidaan poistaa, jos aloite:</p>
     
    <ol class="alpha">
        <li>on lainvastainen, esimerkiksi
        
            <ul>
                <li>loukkaa toisen kunniaa</li>
                <li>levittää toisen yksityisyyttä koskevia tietoja</li>
                <li>levittää salassa pidettäviä tietoja</li>
                <li>kiihottaa kansanryhmää vastaan</li>
                <li>yllyttää rikokseen.</li>
            </ul>
            
            <p>Kaikki lainvastaiset teot on määritelty Suomen lainsäädännössä.</p>
        </li>
        <li>ei täytä kansalaisaloitelaissa aloitteelle säädettyjä edellytyksiä</li>
        <li>on muusta syystä julkaistavaksi kelpaamaton</li>
    </ol>
     
    <p>Palvelussa ei myöskään julkaista aloitteita jotka sisältävät</p>
    
    <ul>
        <li>kaupallisia viestejä</li>
        <li>toisen yksityishenkilön henkilö- tai yhteystietoja sisältäviä viestejä.</li>
    </ul>
    
    <p>Kansalaisaloitteiden sisältöä ei muokata kansalaisaloite.fi-ylläpidossa, vaan aloite julkaistaan tai pyydetään korjaamaan. Ylläpito voi poistaa jo julkaistun aloitteen palvelusta, jos siihen ilmenee myöhemmin tarvetta.</p>
    
    <h3 id="h5">5. Vastuuhenkilön vastuu</h3>
    
    <p>Palveluun lähetettyjen kansalaisaloitteiden viestien sisällöstä vastaavat niiden vastuuhenkilöt. Vastuuhenkilöt vastaavat siitä, että annetut tiedot ovat oikeita. Toisen henkilön tietojen luvaton käyttö on kiellettyä. Tällaisia tietoja ovat esimerkiksi yksityishenkilöiden nimet, sähköpostiosoitteet ja puhelinnumerot. Käyttäjä vastaa itse niistä mahdollisista seurauksista, joita aiheutuu toisen henkilön henkilö- tai muiden tietojen väärinkäyttämisestä.</p>
    <p>Vastuuhenkilöiden tulee myös olla aloitteellisia kansalaisaloitteen kannatusilmoitusten keräämisessä eli harkintansa mukaan laittaa aloite vireille palveluun, toimittaa aloite oikeusministeriön tarkastettavaksi ja aikanaan toimittaa kannatusilmoitukset Väestörekisterikeskukseen. Vastuuhenkilöiden tulee hävittää kannatusilmoitukset asianmukaisesti.</p>
    
    <#assign href>${urls.helpIndex()}/<@u.enumDescription HelpPage.INITIATIVE />#h5</#assign>
    <p><@u.link href=href label="Lue lisää vastuuhenkilöiden vastuista" /></p>
    
    <h3 id="h6">6. Muuta</h3>
    
    <p>Palvelun ylläpito voi tarvittaessa väliaikaisesti sulkea palvelun tai palvelun osia esimerkiksi teknisistä syistä.</p>

<#else>

    <h3 id="h1">1. Ändamålet med tjänsten Medborgarinitiativ.fi</h3>
    
    <p>Sedan 1.3.2012 finns det ett nytt sätt att påverka på statlig nivå, medborgarinitiativet. Det ger medborgarna en möjlighet att få olika politiska ärenden till riksdagsbehandling. Riksdagen behandlar alla initiativ som inom loppet av sex månader får stöd av minst 50 000 röstberättigade medborgare.</p>
    <p>I webbtjänsten Medborgarinitiativ.fi kan du utforma egna initiativ samt stödja och följa med initiativ som andra medborgare har startat. Det är avgiftsfritt och säkert att använda tjänsten, som fungerar även på finska.</p>
    <p>Skyldigheten att producera tjänsten baserar sig på lagen om medborgarinitiativ. Tjänsten administreras av justitieministeriet (Enheten för demokrati, språk och grundläggande rättigheter).</p> 
    
    <h3 id="h2">2. Identifiering av användaren</h3>

    <p>Man kan bläddra bland medborgarinitiativen utan att identifiera sig eller registrera sig i tjänsten.</p>
    <p>Vid webbinsamling av stödförklaringar ska en sådan stark autentisering som avses i lagen om stark autentisering och elektroniska signaturer (617/2009) användas.</p>
    <p>Stark autentisering innebär bestyrkande av användarens identitet med nätbankskoder, mobilcertifikat eller ett chipförsett personkort (HST-kort).</p>
    
    <p>Se även</p>
    <ul>
        <#assign href>${urls.infoIndex()}/<@u.enumDescription InfoPage.PERSON_REGISTER /></#assign>
        <li><@u.link href=href label="Personregistret" /></li>
        <#assign href>${urls.infoIndex()}/<@u.enumDescription InfoPage.PRIVACY /></#assign>
        <li><@u.link href=href label="Integritetsinformation" /></li>
    </ul>
    
    <h3 id="h3">3. Medborgarinitiativets språk</h3>
    
    <p>Webbtjänsten Medborgarinitiativ.fi är tvåspråkig.</p> 
    <p>Medborgarinitiativ görs på finska, svenska eller på båda språken.</p>
    
    <h3 id="h4">4. Granskning och publicering av medborgarinitiativ</h3>
    
    <p>Vid justitieministeriets granskning kontrollerar man att initiativen har den information som krävs och att initiativet inte innehåller material som är olämpligt att publicera på internet. Justitieministeriets granskning tar i övrigt inte ställning till initiativets innehåll. Efter justitieministeriets granskning kan initiativets bestående delar inte längre ändras.</p>
    
    <p>Justitieministeriet behandlar initiativ under tjänstetid och granskningen tar uppskattningsvis några dagar.</p>
    <p>Ett meddelande kan lämnas opublicerat eller avlägsnas, om det:</p>
     
    <ol class="alpha">
        <li>strider mot lag, exempelvis
        
            <ul>
                <li>kränker en annan persons ära</li>
                <li>sprider information som gäller en annan persons integritet</li>
                <li>sprider sekretessbelagda uppgifter</li>
                <li>hetsar mot folkgrupp</li>
                <li>uppmanar till brott.</li>
            </ul>
            
            <p>Alla lagstridiga handlingar definieras i Finlands lagstiftning.</p>
        </li>
        <li>inte uppfyller de förutsättningar för initiativ som föreskrivs i lagen om medborgarinitiativ</li>
        <li>av andra orsaker är olämpligt att publicera</li>
    </ol>
     
    <p>På tjänsten publiceras inte heller initiativ som innehåller</p>
    
    <ul>
        <li>kommersiella meddelanden</li>
        <li>meddelanden som innehåller en annan privatpersons person- eller kontaktuppgifter.</li>
    </ul>
    
    
    
    <p>Innehållet i medborgarinitiativen redigeras inte av administratörerna för medborgarinitiativ.fi, utan initiativet publiceras eller skickas tillbaka för korrigering. Redaktionen kan avlägsna ett redan publicerat initiativ från tjänsten om det senare visar sig vara nödvändigt.</p>
    
    
    <h3 id="h5">5. Ansvarspersonens ansvar</h3>
    
    <p>Ansvarspersonerna svarar för innehållet i de meddelanden som skickas till tjänsten angående deras medborgarinitiativ. Ansvarspersonerna svarar för att de uppgifter som lämnas är riktiga. Det är förbjudet att utan tillstånd använda en annan persons uppgifter. Sådana uppgifter är exempelvis privatpersoners namn, e-postadresser och telefonnummer. Användaren svarar själv för de följder som eventuellt följer på missbruk av en annan persons personuppgifter eller andra uppgifter.</p>
    <p>Ansvarspersonerna ska även delta i insamlingen av stödförklaringar till medborgarinitiativet, det vill säga enligt egen prövning göra initiativet anhängigt på tjänsten, skicka initiativet till justitieministeriet för granskning och i sinom tid skicka stödförklaringarna till Befolkningsregistercentralen. Ansvarspersonerna ska vederbörligen radera stödförklaringarna.</p>
    
    <#assign href>${urls.helpIndex()}/<@u.enumDescription HelpPage.INITIATIVE />#h5</#assign>
    <p><@u.link href=href label="Läs mer om ansvarspersonens ansvar" /></p>
    
    <h3 id="h6">6. Övrigt</h3>
    
    <p>Administratörerna för tjänsten kan vid behov tillfälligt stänga tjänsten eller delar av den till exempel av tekniska orsaker.</p>

</#if>