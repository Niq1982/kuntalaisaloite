<#import "../components/utils.ftl" as u />

<#if locale == "fi">

    <h3 id="h1">Yleistä</h3>
    
    <p>Aloitteen vireillepanija ja edustaja ovat henkilötietolain tarkoittamia rekisterinpitäjiä kannatusilmoitusten keräysvaiheessa. Näin on myös silloin, kun kannatusilmoituksia kerätään kansalaisaloite.fi -verkkopalvelussa, sillä tällöinkin kannatusilmoituksia kerätään vireillepanijan ja edustajan lukuun ja heidän käyttöään varten.</p>
    <p>Kansalaisaloitteisiin liittyvien henkilötietojen keräämisestä säädetään kansalaisaloitelaissa (12/2012) ja erityisesti sen 6 § ja 12 §:ssä. Lisäksi henkilötietojen käsittelyyn sovelletaan muilta osin henkilötietolakia (523/1999).</p>  
    <p>Rekisterinpitäjän eli vireillepanijan tai tämän edustajan on laadittava henkilörekisteristä rekisteriseloste. Kansalaisaloite.fi –palvelussa yksittäistä aloitetta koskevan rekisteriselosteen katsotaan muodostuvan aloitteen tiedoista yhdistettynä alla olevaan henkilörekisteripohjaan.  Näin ollen kaikkiin kansalaisaloite.fi -palvelussa tallennettuihin kansalaisaloitteisiin sovelletaan alla olevaa henkilörekisteripohjaa. Kansalaisaloite.fi -verkkopalvelussa on siis useita henkilörekistereitä ja jokaiselle aloitteelle muodostuu oma henkilörekisteri aloitteen ja henkilörekisteripohjan tiedoista.</p>
    <p>Väestörekisterikeskus on puolestaan lain tarkoittama rekisterinpitäjä kannatusilmoitusten tarkastusvaiheessa. (Vrt. 46/2011, <@u.link href="http://www.finlex.fi/fi/esitykset/he/2011/20110046" label="http://www.finlex.fi/fi/esitykset/he/2011/20110046" rel="external" />)</p>
    <#assign href>${urls.infoIndex()}/<@u.enumDescription InfoPage.FEEDBACK /></#assign>
    <p>Kysyttävää? <@u.link href=href label="Ota yhteyttä" />.</p>
    
    <h3 id="h2">Kansalaisaloitteen kannatusilmoitusten henkilörekisteriselosteet</h3>
    
    <p>Henkilötietolaki (523/99) 10 §<br/>Päivitetty viimeksi 23.11.2012</p>
    
    <h4 id="h2-1">1. Rekisterinpitäjän nimi</h4> 
        
    <p>Kansalaisaloitteen rekisterinpitäjänä toimivat kunkin kansalaisaloitteen vastuuhenkilöt eli vireillepanija(t), edustaja(t) ja varaedustaja(t).</p>
    
    <p>Rekisterinpitäjät näkyvät kunkin aloitteen omalla sivulla.</p>
    
    <h4 id="h2-2">2. Yhteyshenkilöt</h4>
    
    <p>Kunkin kansalaisaloitteen osalta yhteyshenkilöinä ovat:<br/>
    Kansalaisaloitteen edustaja(t) ja varaedustaja(t)</p>
    
    <#assign href>${urls.helpIndex()}/<@u.enumDescription HelpPage.INITIATIVE />#h1-2-4</#assign>
    <p>Edustajan ja varaedustajan yhteystiedot ovat aloitekohtaisia ja näkyvät kunkin aloitteen omalla sivulla. <@u.link href=href label="Lue lisää ohjeista" /></p>
        
    <p>Lisäksi edellä mainittujen lisäksi kunkin henkilörekisterin yhteyshenkilönä toimii:</p>
    
    <p><strong>Teemu Ropponen</strong><br/>
    Oikeusministeriö, Demokratia-, kieli- ja perusoikeusasioiden yksikkö<br/>
    PL 25, 00023 VALTIONEUVOSTO<br/>
    <@u.scrambleEmail "etunimi.sukunumi@om.fi" />  tai <@u.scrambleEmail "kansalaisaloite.om@om.fi" /></p>
    
    
    <h4 id="h2-3">3. Rekisterin nimi</h4>
    
    <p>Henkilörekisteri on aloitekohtainen. Kukin henkilörekisteri on nimeltään:</p>
    <p>“Rekisteri kansalaisaloitteen [ALOITTEEN NIMI JA PÄIVÄYS] sähköisistä kannatusilmoituksista Kansalaisaloite.fi -palvelussa”</p>
    
    <h4 id="h2-4">4. Henkilötietojen käsittelyn tarkoitus (rekisterin käyttötarkoitus)</h4>
    
    <p>Henkilötietojen keräyksen peruste on kansalaisaloitelaki (12/2012). Rekisteriin kerätään kansalaisaloitteen kannatusilmoitukset ja niihin liittyvät henkilötiedot kansalaisaloitelain edellyttämällä tavalla.</p> 
    <p>Kansalaisaloitelain (12/2012) 12 § mukaan:</p>
    
    <blockquote>
        <p>Kannatusilmoituksissa kerättyjä henkilötietoja voidaan käyttää vain tässä laissa säädetyllä tavalla. Vireillepanija ja tämän edustaja voivat luovuttaa tietoja vain Väestörekisterikeskukselle. Viranomaisten hallussa olevat tiedot ovat julkisia, kun perustuslaissa säädetty kannatusilmoitusten vähimmäismäärä on täyttynyt.</p>
        <p>Aloitteen vireillepanijan ja edustajan on huolehdittava hallussaan olevien kannatusilmoitusten hävittämisestä viimeistään kuuden kuukauden kuluttua 9 §:n nojalla annetusta Väestörekisterikeskuksen päätöksestä. Jos kannatusilmoituksia ei toimiteta Väestörekisterikeskuksen tarkastettaviksi, ne on hävitettävä viimeistään kuuden kuukauden kuluttua siitä, kun kannatusilmoitusten keräys on lopetettu.</p>
        <p>Väestörekisterikeskuksen on hävitettävä kannatusilmoitukset aloitteen eduskuntakäsittelyn päätyttyä. Jos aloitetta ei toimiteta eduskuntaan, Väestörekisterikeskus säilyttää kannatusilmoituksia yhdeksän kuukautta 9 §:n nojalla antamastaan päätöksestä, minkä jälkeen ne on hävitettävä.</p>
        <p>Kannatusilmoituksissa kerättyjen henkilötietojen käsittelyyn sovelletaan muutoin henkilötietolakia (523/1999).<p>
    </blockquote>
    
    <p>Hallituksen esityksessä (HE 46/2011, <@u.link href="http://www.finlex.fi/fi/esitykset/he/2011/20110046" label="http://www.finlex.fi/fi/esitykset/he/2011/20110046" rel="external" />) on tarkennettu, että “Aloitteen vireillepanija ja edustaja olisivat henkilötietolain tarkoittamia rekisterinpitäjiä kannatusilmoitusten keräysvaiheessa. Näin olisi myös silloin, kun kannatusilmoituksia kerätään oikeusministeriön verkkopalvelussa, sillä tällöinkin kannatusilmoituksia kerätään vireillepanijan ja edustajan lukuun ja heidän käyttöään varten. Väestörekisterikeskus olisi lain tarkoittama rekisterinpitäjä kannatusilmoitusten tarkastusvaiheessa.”</p>
    
    <p>Oikeusministeriö on ns. toimeksisaaja vireillepanijoiden ja edustajien toimesta, ja toimii yhtenä yhteystahona henkilörekisteriä koskevissa asioissa.</p>
    
    
    <h4 id="h2-5">5. Rekisterin tietosisältö</h4>
    
    <p>Kansalaisaloitelain (12/2012) 6 §:n mukaan kannatusilmoituksista tallennetaan seuraavat tiedot:</p>
    
    <ol>
        <li>kansalaisaloitteen otsikko ja päiväys;</li>
        <li>aloitteen allekirjoittajan täydellinen nimi, syntymäaika ja kotikunta;</li>
        <li>aloitteen allekirjoittajan vakuutus siitä, että hän on äänioikeutettu Suomen kansalainen ja että hän on tehnyt kannatusilmoituksen samaan aloitteeseen vain kerran;</li>
        <li>kannatusilmoituksen päiväys.</li>
    </ol>
    
    <p>Lisäksi kannatusilmoituksille lasketaan henkilötunnusta hyödyksi käyttäen aloitekohtainen yksisuuntainen tiiviste, jolla turvallisesti estetään saman aloitteen kannattaminen moneen kertaan. Tämän tiedon perusteella ei voida selvittää, mitä eri aloitteita yksittäinen henkilö on kannattanut.</p>
    
    <h4 id="h2-6">6. Säännönmukaiset tietolähteet</h4>
    
    <p>Kansalaisaloite.fi -palvelun säännönmukaiset tietolähteet ovat</p>
    
    <ul>
        <li>VETUMA (Kansalaisen tunnistus- ja maksamispalvelu)</li>
        <li>Väestörekisterikeskuksen ylläpitämä väestötietojärjestelmä</li>
        <li>“Hyväksy kannatusilmoitus” -verkkosivu</li>
    </ul>
    
    <h4 id="h2-7">7. Tietojen säännönmukaiset luovutukset</h4>
    
    <p>Kannatusilmoituksissa kerättyjä henkilötietoja voidaan käyttää vain tässä laissa säädetyllä tavalla. Vireillepanija ja tämän edustaja voivat luovuttaa tietoja vain Väestörekisterikeskukselle.</p> 
    
    <h4 id="h2-8">8. Tietojen siirto EU:n tai ETA:n ulkopuolelle</h4>
    
    <p>Rekisterin sisältämiä henkilötietoja ei luovuteta Suomen ulkopuolelle.</p>
    
    <h4 id="h2-9">9. Rekisterin suojauksen periaatteet</h4>
    
    <p>Tiedot on tallennettu oikeusministeriön tuottamaan ja käyttämään, Viestintäviraston tarkastamaan tietojärjestelmään. Viestintävirasto tarkastaa, että järjestelmä toimii kansalaisaloitelain (12/2012) 7§ edellyttämällä tavalla.</p>
    
    <p>Tietoliikenteen suojaus</p>
    
    <ul>
        <li>Yhteys käyttäjien koneelta internetin yli palveluun tapahtuu käyttäen suojattua (SSL-) yhteyttä.</li>
        <li>Viestintävirasto on tarkastanut yhteydet eri järjestelmien välillä sekä fyysisen tietoturvan. Palvelu täyttää kansallisen turvallisuuskriteeristön (KATAKRI) perustason vaatimukset soveltuvin osin.</li>
    </ul>
    
    <p>Tietojärjestelmien suojaus</p>
    
    <ul>
        <li>Kannatusilmoitusten tiedot tallennetaan salattuna</li>
        <li>Pääsy tietojärjestelmiin ja tietokantoihin on teknisesti rajoitettu</li>
    </ul>
    
    <p>Ylläpitäjät</p>
    
    <ul>
        <li>Palvelun ylläpito-oikeudet on vain rajatulla joukolla erikseen sovittuja henkilöitä</li>
        <li>Ylläpitäjien tietoliikenneyhteydet ovat suojatut</li>
        <li>Ylläpitäjien toiminta tallennetaan lokiin</li>
    </ul>
    
    <h4 id="h2-10">10. Rekisteröidyn tarkastusoikeus</h4>
    
    <p>Rekisterissä olevalla on oikeus tarkastaa rekisteriin tallennetut itseään koskevat tiedot ja saada niistä kopiot. Tarkastuspyyntö tulee tehdä kirjallisesti ja osoittaa rekisteriasioista vastaavalle henkilölle omakätisesti allekirjoitetussa tai sitä vastaavalla tavalla varmennetussa asiakirjassa tai henkilökohtaisesti rekisterinpitäjän luona.</p> 
    <p>Yhteystiedot löytyvät <a href="#h2-2">kohdasta 2</a>.</p>
    
    <h4 id="h2-11">11. Tiedon korjaaminen</h4>
    
    <p>Rekisterinpitäjä oikaisee, poistaa tai täydentää rekisterissä olevan, käsittelyn tarkoituksen kannalta virheellisen, tarpeettoman, puutteellisen tai vanhentuneen tiedon oma-aloitteisesti tai rekisterissä olevan vaatimuksesta.</p>
    
    
    <h3 id="h3">Rekisteriseloste Kansalaisaloite.fi -palvelun kansalaisaloitteiden vastuuhenkilöistä</h3> 
    
    <h4 id="h3-1">1. Rekisterinpitäjä</h4>
    
    <p>Oikeusministeriö</p>
    
    <h4 id="h3-2">2. Rekisteriasioista vastaava henkilö ja/tai yhteyshenkilö</h4>
    
    <p><strong>Teemu Ropponen</strong><br/>
    Oikeusministeriö, Demokratia-, kieli- ja perusoikeusasioiden yksikkö<br/>
    PL 25, 00023 VALTIONEUVOSTO<br/>
    <@u.scrambleEmail "etunimi.sukunumi@om.fi" />  tai <@u.scrambleEmail "kansalaisaloite.om@om.fi" /></p>
    
    <h4 id="h3-3">3. Rekisterin nimi</h4>
    
    <p>Rekisteri Kansalaisaloite.fi -palvelun kansalaiasaloitteiden vastuuhenkilöistä<p> 
    
    <h4 id="h3-4">4. Henkilötietojen käsittelyn tarkoitus (rekisterin käyttötarkoitus)</h4>
    
    <p>Henkilötietojen keräyksen peruste on kansalaisaloitelaki (12/2012).</p> 
    
    <p>Rekisterin tarkoituksena on varmistaa, että</p>
     
    <ul>
        <li>kansalaisaloitteiden vastuuhenkilöt tiedetään kansalaisaloitelain edellyttämällä tavalla viranomaisasiointia varten sekä kansalaisaloitteiden kannatusilmoitusten keräämistä varten</li>
        <li>vastuuhenkilöiden toiminta tallennetaan ja poikkeustilanteissa voidaan jäljittää</li>
        <li>vastuuhenkilöt pääsevät itse hallinnoimaan kansalaisaloitetta, jossa ovat vastuuhenkilönä</li>
    </ul>
    
    <h4 id="h3-5">5. Rekisterin tietosisältö</h4>
    
    <p>Aloitteen vastuuhenkilöstä tallennetaan seuraavat tiedot (suluissa lähde)</p>
    
    <ul>
        <li>Sukunimi (Vetuma)</li>
        <li>Etunimet (Vetuma)</li>
        <li>Syntymäaika (Vetuma)</li>
        <li>Henkilötunnuksesta (Vetuma) laskettu yksisuuntainen tiiviste, jolla vastuuhenkilö voidaan yksiselitteisesti yksilöidä järjestelmässä</li>
        <li>Edellisen kirjautumisen ajankohta</li>
    </ul>
    
    <p>Kustakin aloitteesta jossa vastuuhenkilö on osallisena, tallennetaan seuraavat vastuuhenkilöä koskevat tiedot (suluissa lähde)</p>
    
    <ul>
        <li>Sukunimi (Vetuma)</li>
        <li>Etunimet (Vetuma)</li>
        <li>Kotikunta (Väestötietojärjestelmä)</li>
        <li>Puhelinnumero* (käyttäjän antama vapaaehtoinen tieto)</li>
        <li>Sähköpostiosoite* (käyttäjän antama vapaaehtoinen tieto)</li>
        <li>Postiosoite* (käyttäjän antama vapaaehtoinen tieto)</li>
        <li>Tieto vastuuhenkilön rooleista aloitteessa [vireillepanija, edustaja, varaedustaja] (käyttäjän antama tieto)</li>
        <li>Vastuuhenkilön roolien vahvistamisen ajankohta (järjestelmän tuottama tieto)</li>
        <li>Listaus aloitteelle tehdyistä toimenpiteistä (esimerkiksi aloitteen luonti, muokkaus) ja näiden ajankohdista (järjestelmän tuottama tieto)</li>
    </ul>
    
    <p>*Edustajan sekä varaedustajan on annettava vähintään yksi valinnaisista kentistä (puhelinnumero, sähköpostiosoite, postiosoite).</p>
    
    <h4 id="h3-6">6. Säännönmukaiset tietolähteet</h4>
    
    <ul>
        <li>VETUMA (Kansalaisen tunnistus- ja maksamispalvelu)</li>
        <li>Väestörekisterikeskuksen ylläpitämä väestötietojärjestelmä</li>
        <li>“Tee uusi aloite” -verkkosivu (vastuuhenkilön syöttämät tiedot)</li>
        <li>“Hyväksy rooli” -verkkosivu (vastuuhenkilön syöttämät tai hyväksymät tiedot)</li>
    </ul>
    
    <h4 id="h3-7">7. Tietojen säännönmukaiset luovutukset</h4>
    
    <p>Rekisterin tietoja ei luovuteta säännönmukaisesti ulkopuolisille tahoille.</p>
    
    <h4 id="h3-8">8. Tietojen siirto EU:n tai ETA:n ulkopuolelle</h4>
    
    <p>Rekisterin sisältämiä henkilötietoja ei luovuteta Suomen ulkopuolelle.</p>
    
    <h4 id="h3-9">9. Rekisterin suojauksen periaatteet</h4>
    
    <p>Tiedot on tallennettu oikeusministeriön tuottamaan ja käyttämään, Viestintäviraston tarkastamaan tietojärjestelmään. Viestintävirasto on tarkastanut, että järjestelmä toimii kansalaisaloitelain (12/2012) 7 §:n edellyttämällä tavalla.</p>
    <p>Tietoliikenteen suojaus</p>
    
    <ul>
        <li>Yhteys käyttäjien koneelta internetin yli palveluun tapahtuu käyttäen suojattua (SSL-) yhteyttä.</li>
        <li>Viestintävirasto on tarkastanut yhteydet eri järjestelmien välillä sekä fyysisen tietoturvan. Palvelu täyttää kansallisen turvallisuuskriteeristön (KATAKRI) perustason vaatimukset soveltuvin osin.</li>
    </ul>
    
    <p>Tietojärjestelmien suojaus</p>
    
    <ul>
        <li>Kannatusilmoitusten tiedot tallennetaan salattuna</li>
        <li>Pääsy tietojärjestelmiin ja tietokantoihin on teknisesti rajoitettu</li>
    </ul>
    
    <p>Ylläpitäjät</p>
    
    <ul>
        <li>Palvelun ylläpito-oikeudet ovat vain rajatulla joukolla erikseen sovittuja henkilöitä</li>
        <li>Ylläpitäjien tietoliikenneyhteydet ovat suojatut</li>
        <li>Ylläpitäjien toiminta tallennetaan lokiin</li>
    </ul>

<#else>

    <h3 id="h1">Allmänt</h3>
    
    <p>I det skede då stödförklaringarna samlas in är den som väcker initiativet och företrädaren sådana registeransvariga som avses i personuppgiftslagen. Så är det också när stödförklaringar samlas in via webbtjänsten medborgarinitiativ.fi, eftersom stödförklaringarna då samlas in för dess räkning som väcker ett initiativ och för företrädarens räkning och för deras användning.</p>
    <p>Bestämmelser om insamling av personuppgifter i anslutning till medborgarinitiativ finns i lagen om medborgarinitiativ (12/2012), särskilt i 6 § och 12 §. På behandlingen av personuppgifter tillämpas i övrigt personuppgiftslagen (523/1999).</p>  
    <p>Den registeransvarige, dvs. den som väcker initiativet eller hans eller hennes företrädare, ska göra upp en registerbeskrivning över personregistret. I tjänsten medborgarinitiativ.fi anses en registerbeskrivning som berör ett enskilt initiativ bestå av uppgifterna om initiativet i kombination med personregisterformuläret nedan. Således tillämpas personregisterformuläret nedan på alla medborgarinitiativ som sparats i tjänsten medborgarinitiativ.fi. Webbtjänsten medborgarinitiativ.fi innehåller alltså ett flertal personregister och för varje initiativ skapas ett eget personregister på basis av uppgifterna i initiativet och i personregisterformuläret.</p>
    <p>Befolkningsregistercentralen är å sin sida en sådan registeransvarig som avses i lagen i det skede när stödförklaringarna kontrolleras.<br/>(Jfr 46/2011, <@u.link href="http://www.finlex.fi/sv/esitykset/he/2011/20110046.pdf" label="http://www.finlex.fi/sv/esitykset/he/2011/20110046.pdf" rel="external" />)</p>
    <#assign href>${urls.infoIndex()}/<@u.enumDescription InfoPage.FEEDBACK /></#assign>
    <p>Frågor? <@u.link href=href label="Ta kontakt" />.</p>
    
    <h3 id="h2">Registerbeskrivningar över medborgarinitiativets stödförklaringar</h3>
    
    <p>Personuppgiftslagen (523/99): 10 §<br/>Senast uppdaterad 23.11.2012</p>
    
    <h4 id="h2-1">1. Den registeransvariges namn </h4> 
        
    <p>De registeransvariga för ett medborgarinitiativ utgörs av ansvarspersonerna för respektive medborgarinitiativ, dvs. den eller de som väcker initiativet, företrädaren eller företrädarna samt ersättaren eller ersättarna.</p>
    
    <p>De registeransvariga syns på den egna sidan för respektive initiativ.</p>
    
    <h4 id="h2-2">2. Kontaktpersoner</h4>
    
    <p>Kontaktpersonerna för respektive medborgarinitiativ är:<br/>
    Medborgarinitiativets företrädare och ersättare</p>
    
    <#assign href>${urls.helpIndex()}/<@u.enumDescription HelpPage.INITIATIVE />#h1-2-4</#assign>
    <p>Företrädarens och ersättarens kontaktuppgifter är initiativspecifika och syns på den egna sidan för respektive initiativ. <@u.link href=href label="Läs mer i anvisningarna" /></p>
        
    <p>Utöver de ovan nämnda finns en gemensam kontaktperson för personregistren:</p>
    
    <p><strong>Teemu Ropponen</strong><br/>
    Justitieministeriet, Enheten för demokrati, språk och grundläggande rättigheter<br/>
    PB 25, 00023 STATSRÅDET<br/>
    <@u.scrambleEmail "förnamn.efternamn@om.fi" />  tai <@u.scrambleEmail "kansalaisaloite.om@om.fi" /></p>
    
    
    <h4 id="h2-3">3. Registrets namn</h4>
    
    <p>Personregistret är initiativspecifikt. Namnet för respektive register är:</p>
    <p>”Register över elektroniska stödförklaringar för medborgarinitiativet [INITIATIVETS NAMN OCH DATUM] i tjänsten Medborgarinitiativ.fi”</p>
    
    <h4 id="h2-4">4. Syftet med behandlingen av personuppgifter (registrets ändamål)</h4>
    
    <p>Grunden för insamling av personuppgifter är lagen om medborgarinitiativ (12/2012). I registret samlas medborgarinitiativets stödförklaringar och personuppgifter i anslutning till dem på det sätt som avses i lagen om medborgarinitiativ.</p> 
    <p>12 § i lagen om medborgarinitiativ:</p>
    
    <blockquote>
        <p>Personuppgifterna i de stödförklaringar som har samlats in får användas endast på det sätt som föreskrivs i denna lag. Den som väcker initiativ och hans eller hennes företrädare får överlåta uppgifterna enbart till Befolkningsregistercentralen. De uppgifter som myndigheterna innehar är offentliga när kravet på minsta antal stödförklaringar enligt grundlagen har uppfyllts.</p>
        <p>Den som väcker initiativ och företrädaren ska se till att de stödförklaringar som de har i sin besittning förstörs inom sex månader från det att Befolkningsregistercentralen fattade sitt beslut enligt 9 §. Om stödförklaringarna inte lämnas till Befolkningsregistercentralen för kontroll, ska de förstöras inom sex månader från det att insamlingen av stödförklaringar har avslutats.</p>
        <p>Befolkningsregistercentralen ska förstöra stödförklaringarna när riksdagsbehandlingen av initiativet har avslutats. Om initiativet inte överlämnas till riksdagen, förvarar Befolkningsregistercentralen stödförklaringarna i nio månader räknat från dess beslut enligt 9 §. Efter det ska stödförklaringarna förstöras.</p>
        <p>På behandlingen av personuppgifterna i de stödförklaringar som samlats in för initiativet tillämpas i övrigt personuppgiftslagen (523/1999).<p>
    </blockquote>
    
    <p>I regeringens proposition (RP 46/2011, <@u.link href="http://www.finlex.fi/sv/esitykset/he/2011/20110046.pdf" label="http://www.finlex.fi/sv/esitykset/he/2011/20110046.pdf" rel="external" />) preciseras att ”I det skede då stödförklaringarna samlas in är den som väcker initiativet och företrädaren sådana registeransvariga som avses i personuppgiftslagen. Så är det också när stödförklaringar samlas in via justitieministeriets nättjänst, eftersom stödförklaringarna då samlas in för dess räkning som väcker ett initiativ och för företrädarens räkning och för deras användning. Befolkningsregistercentralen är en sådan registeransvarig som avses i lagen i det skede när stödförklaringarna kontrolleras.”</p>
    
    <p>Justitieministeriet är den s.k. uppdragstagaren genom initiativtagarnas och företrädarnas försorg, och fungerar som en av kontaktinstanserna i ärenden som berör personregistret.</p>
    
    
    <h4 id="h2-5">5. Registrets datainnehåll</h4>
    
    <p>Enligt 6 § i lagen om medborgarinitiativ (12/2012) sparas följande uppgifter i stödförklaringarna:</p>
    
    <ol>
        <li>medborgarinitiativets titel och datum,</li>
        <li>initiativundertecknarens fullständiga namn, födelsetid och hemkommun,</li>
        <li>en försäkran från initiativundertecknaren om att han eller hon är röstberättigad finsk medborgare och att han eller hon har gett endast en stödförklaring för samma initiativ,</li>
        <li>stödförklaringens datum.</li>
    </ol>
    
    <p>Dessutom räknar man utgående från personbeteckningen ut ett initiativspecifikt envägshashvärde för stödförklaringarna. På så sätt säkerställer man att en person kan understöda samma initiativ endast en gång. På basis av denna information är det inte möjligt att ta reda på vilka initiativ en enskild person har understött.</p>
    
    <h4 id="h2-6">6. Regelmässiga informationskällor</h4>
    
    <p>De regelmässiga informationskällorna för tjänsten Medborgarinitiativ.fi är</p>
    
    <ul>
        <li>VETUMA (Identifierings- och betalningstjänst för medborgarna)</li>
        <li>Befolkningsdatasystemet som upprätthålls av Befolkningsregistercentralen</li>
        <li>Webbplatsen ”Godkänn stödförklaringen”</li>
    </ul>
    
    <h4 id="h2-7">7. Regelmässig överlåtelse av uppgifter</h4>
    
    <p>Personuppgifterna i de stödförklaringar som har samlats in får användas endast på det sätt som föreskrivs i denna lag. Den som väcker initiativ och hans eller hennes företrädare får överlåta uppgifterna enbart till Befolkningsregistercentralen.</p> 
    
    <h4 id="h2-8">8. Överföring av uppgifter till länder utanför EU eller EES</h4>
    
    <p>Personuppgifter som finns i registret överlåts inte utomlands.</p>
    
    <h4 id="h2-9">9. Principer för skydd av registret</h4>
    
    <p>Uppgifterna är sparade i ett informationssystem som produceras och används av justitieministeriet och som granskas av Kommunikationsverket. Kommunikationsverket kontrollerar att systemet fungerar enligt vad som föreskrivs i 7 § i lagen om medborgarinitiativ (12/2012).</p>
    
    <p>Skydd av datakommunikationen</p>
    
    <ul>
        <li>Kontakten från användarens dator till tjänsten över internet sker genom en skyddad (SSL ) förbindelse.</li>
        <li>Kommunikationsverket har granskat förbindelserna mellan olika system samt den fysiska datasäkerheten. Tjänsten uppfyller i tillämpliga delar kraven för grundnivån i den nationella kriteriesamlingen för säkerhetsauditering (KATAKRI).</li>
    </ul>
    
    <p>Skydd av informationssystem</p>
    
    <ul>
        <li>Uppgifterna i stödförklaringarna sparas i krypterad form</li>
        <li>Tillgången till informationssystem och till databaser är tekniskt begränsad</li>
    </ul>
    
    <p>Administratörer</p>
    
    <ul>
        <li>Administrationsrättigheter för tjänsten innehas av endast ett begränsat antal särskilt utnämnda personer</li>
        <li>Administratörernas datatrafikförbindelser är skyddade</li>
        <li>Administratörernas verksamhet sparas i en logg</li>
    </ul>
    
    <h4 id="h2-10">10. Den registrerades rätt till insyn</h4>
    
    <p>Den som finns i registret har rätt att kontrollera de uppgifter i registret som berör honom eller henne själv samt att få kopior av uppgifterna. Begäran om insyn ska göras skriftligen och riktas till den person som ansvarar för registerärenden med ett dokument som undertecknats egenhändigt eller som certifierats på annat sätt eller personligen hos den registeransvarige.</p> 
    <p>Kontaktuppgifter finns i <a href="#h2-2">punkt 2</a>.</p>
    
    <h4 id="h2-11">11. Rättelse av uppgifter</h4>
    
    <p>Den registeransvarige rättar till, tar bort eller kompletterar sådan information i registret som är felaktig, onödig, bristfällig eller föråldrad för behandlingens syfte på eget initiativ eller på fordran av den som finns i registret.</p>
    
    
    <h3 id="h3">Registerbeskrivning över ansvarspersonerna för medborgarinitiativen i tjänsten Medborgarinitiativ.fi</h3> 
    
    <h4 id="h3-1">1. Registeransvarig</h4>
    
    <p>Justitieministeriet</p>
    
    <h4 id="h3-2">2. Person som ansvarar för registerärenden och/eller kontaktperson</h4>
    
    <p><strong>Teemu Ropponen</strong><br/>
    Justitieministeriet, Enheten för demokrati, språk och grundläggande rättigheter<br/>
    PB 25, 00023 STATSRÅDET<br/>
    <@u.scrambleEmail "förnamn.efternamn@om.fi" />  tai <@u.scrambleEmail "kansalaisaloite.om@om.fi" /></p>
    
    <h4 id="h3-3">3. Registrets namn</h4>
    
    <p>Register över ansvarspersonerna för medborgarinitiativen i tjänsten Medborgarinitiativ.fi<p> 
    
    <h4 id="h3-4">4. Syftet med behandlingen av personuppgifter (registrets ändamål)</h4>
    
    <p>Grunden för insamling av personuppgifter är lagen om medborgarinitiativ (12/2012).</p> 
    
    <p>Syftet med registret är att säkerställa att</p>
     
    <ul>
        <li>man känner till medborgarinitiativens ansvarspersoner enligt vad som föreskrivs i lagen om medborgarinitiativ för uträttande och behandling av ärenden hos myndigheter samt för insamling av stödförklaringar för medborgarinitiativ</li>
        <li>ansvarspersonernas verksamhet lagras och kan i undantagsfall spåras</li>
        <li>ansvarspersonerna själva kan administrera de medborgarinitiativ som de ansvarar för</li>
    </ul>
    
    <h4 id="h3-5">5. Registrets datainnehåll</h4>
    
    <p>Om initiativets ansvarsperson lagras följande uppgifter (källan inom parentes)</p>
    
    <ul>
        <li>Efternamn (Vetuma)</li>
        <li>Förnamn (Vetuma)</li>
        <li>Födelsedatum (Vetuma)</li>
        <li>Envägshashvärdet som räknats ut utgående från personbeteckningen (Vetuma) och som kan användas för att entydigt identifiera ansvarspersonen i systemet</li>
        <li>Tidpunkten för föregående inloggning</li>
    </ul>
    
    <p>För respektive initiativ där ansvarspersonen är delaktig lagras följande uppgifter som berör ansvarspersonen (källan inom parentes)</p>
    
    <ul>
        <li>Efternamn (Vetuma)</li>
        <li>Förnamn (Vetuma)</li>
        <li>Hemkommun (Befolkningsdatasystemet)</li>
        <li>Telefonnummer* (frivillig uppgift som anges av användaren)</li>
        <li>E-postadress* (frivillig uppgift som anges av användaren)</li>
        <li>Postadress* (frivillig uppgift som anges av användaren)</li>
        <li>Uppgift om ansvarspersonens roller i anslutning till initiativet [initiativtagare, företrädare, ersättare] (uppgift som anges av användaren)</li>
        <li>Tidpunkten för fastställande av ansvarspersonens roller (uppgift ur systemet)</li>
        <li>Lista över åtgärder som gjorts i anslutning till initiativet (exempelvis skapande av initiativ, redigering) och deras tidpunkter (uppgift ur systemet)</li>
    </ul>
    
    <p>*Företrädaren och ersättaren måste ange minst en av de valbara uppgifterna (telefonnummer, e-postadress, postadress).</p>
    
    <h4 id="h3-6">6. Regelmässiga informationskällor</h4>
    
    <ul>
        <li>VETUMA (Identifierings- och betalningstjänst för medborgarna)</li>
        <li>Befolkningsdatasystemet som upprätthålls av Befolkningsregistercentralen</li>
        <li>Webbplatsen ”Skapa ett nytt initiativ” (uppgifter som matas in av ansvarspersonen)</li>
        <li>Webbplatsen ”Godkänn rollen” (uppgifter som matas in eller godkänns av ansvarspersonen)</li>
    </ul>
    
    <h4 id="h3-7">7. Regelmässig överlåtelse av uppgifter</h4>
    
    <p>I regel överlåts uppgifterna i registret inte till utomstående aktörer.</p>
    
    <h4 id="h3-8">8. Överföring av uppgifter till länder utanför EU eller EES</h4>
    
    <p>Personuppgifter som finns i registret överlåts inte utomlands.</p>
    
    <h4 id="h3-9">9. Principer för skydd av registret</h4>
    
    <p>Uppgifterna är sparade i ett informationssystem som produceras och används av justitieministeriet och som granskas av Kommunikationsverket. Kommunikationsverket har kontrollerat att systemet fungerar enligt vad som föreskrivs i 7 § i lagen om medborgarinitiativ (12/2012).</p>
    <p>Skydd av datakommunikationen</p>
    
    <ul>
        <li>Kontakten från användarens dator till tjänsten över internet sker genom en skyddad (SSL ) förbindelse.</li>
        <li>Kommunikationsverket har granskat förbindelserna mellan olika system samt den fysiska datasäkerheten. Tjänsten uppfyller i tillämpliga delar kraven för grundnivån i den nationella kriteriesamlingen för säkerhet (KATAKRI).</li>
    </ul>
    
    <p>Skydd av informationssystem</p>
    
    <ul>
        <li>Uppgifterna i stödförklaringarna sparas i krypterad form</li>
        <li>Tillgången till informationssystem och till databaser är tekniskt begränsad</li>
    </ul>
    
    <p>Administratörer</p>
    
    <ul>
        <li>Administrationsrättigheter för tjänsten innehas av endast ett begränsat antal särskilt utnämnda personer</li>
        <li>Administratörernas datatrafikförbindelser är skyddade</li> 
        <li>Administratörernas verksamhet sparas i en logg</li>
    </ul>

</#if>