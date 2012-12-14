
<#if locale == "fi">

    <div class="view-block">
        <h3>Sisällysluettelo</h3>
        <ol>
            <li><a href="#h1">Aloitteen vireillepano</a>
                <ol class="alpha">
                    <li><a href="#h1-1">Ennen kuin aloitat</a></li>
                    <li><a href="#h1-2">Aloitelomakkeen tietojen syöttäminen</a>
                        <ol class="roman">
                            <li><a href="#h1-2-1">Aloitteen pysyvät tiedot</a></li>
                            <li><a href="#h1-2-2">Aloitteen muut tiedot</a></li>
                            <li><a href="#h1-2-3">Omat tiedot</a></li>
                            <li><a href="#h1-2-4">Vastuuhenkilöt</a></li>
                        </ol>    
                    </li>
                    <li><a href="#h1-3">Muiden vastuuhenkilöiden roolien hyväksyntä</a></li>
                    <li><a href="#h1-4">Aloitteen lähettäminen oikeusministeriölle</a></li>
                </ol>
            </li>
            <li><a href="#h2">Aloitteen kannatusilmoitusten keräys ja tarkastus</a></li>
            <li><a href="#h3">Toimittaminen eduskuntaan</a></li>
            <li><a href="#h4">Aloitteiden etsiminen, selaaminen ja seuraaminen</a>
                <ol class="alpha">
                    <li><a href="#h4-1">Selaa aloitteita</a></li>
                    <li><a href="#h4-2">Omat kansalaisaloitteeni</a></li>    
                </ol>
            </li>
            <li><a href="#h5">Aloitteen ylläpito ja vastuuhenkilöiden vastuut</a></li>
        </ol>
    </div>

    
    <h3 id="h1">1. Aloitteen vireillepano</h3>

    
    <h3 id="h1-1">a. Ennen kuin aloitat</h3>

    <#assign href>${urls.helpIndex()}/<@u.enumDescription HelpPage.GENERAL /></#assign>
    <p>Jos kansalaisaloitemenettely ei ole sinulle tuttu, <@u.link href=href label="lue kansalaisaloitteesta yleensä" />.</p>
    <#assign href>${urls.helpIndex()}/<@u.enumDescription HelpPage.PROGRESS /></#assign>
    <p>Yleiskuvan aloitteen etenemisestä kansalaisaloite.fi -palvelussa saat esimerkiksi “<@u.link href=href label="Näin aloite etenee kansalaisaloite.fi-palvelussa" />” -sivulta.</p>
    <p>Tarvittaessa voit kysyä palvelusta oikeusministeriöltä sähköpostitse osoitteessa <@u.scrambleEmail "kansalaisaloite.om@om.fi" /> tai <@u.link href="https://www.facebook.com/aloite" label="Facebook" rel="external" />-ryhmässä.</p> 

    <h3 id="h1-2">b. Aloitelomakkeen tietojen syöttäminen</h3>

    <p>Aloittaaksesi uuden kansalaisaloitteen tekemisen, valitse etusivulta “Tee kansalaisaloite”.</p> 
    <p>Huomaa, että tehdäksesi aloitteen sinun täytyy tunnistautua pankkitunnuksilla, mobiilivarmenteella tai sirullisella henkilökortilla Istunto katkeaa tietoturvasyistä 30 minuutin jälkeen. Jos selaimessasi on javascript sallittuna, istuntoa pidennetään enintään 120 minuuttiin asti, kun olet luomassa tai muokkaamassa aloitetta.  Näin ollen suositeltavaa, että kirjoitat mahdolliset pidemmät tekstit ensin esimerkiksi tekstinkäsittelyohjelmalla ja siirrät ne palvelun lomakkeelle erikseen.</p>


    <h4 id="h1-2-1">i. Aloitteen pysyvät tiedot</h4>

    <p><@u.image src="help-basic-details-fi.png" alt="Aloitteen pysyvät tiedot" /></p>

    <p>Aloite laaditaan suomen tai ruotsin kielellä taikka molemmilla kielillä.  “Lisää ruotsinkielinen versio” -linkki avaa tai piilottaa ruotsinkielisen aloitteen otsikon, sisällön ja perusteluiden kentät.</p> 

    <h4 id="name">Kansalaisaloitteen otsikko</h4>
 
    <p>Kirjoita otsikkokenttään kansalaisaloitteen nimi. Otsikon tulee olla selkeä ja kuvata aloitteen varsinaista sisältöä. Jos keräät kannatusilmoituksia aloitteelle myös paperilomaketta käyttäen tai muissa verkkopalveluissa, otsikon tulee olla kaikkialla sanasta sanaan sama, jotta näiden eri kannatusilmoitusten katsottaisiin koskevan samaa aloitetta.</p>
 
    <h4 id="date">Aloitteen päiväys</h4>
 
    <p>Aloitteen päiväys on se päivämäärä, jolloin kannatusilmoitusten keräys on tarkoitus aloittaa tai jolloin se on alkanut muuta keräystapaa käyttäen.</p>
    <p>Valitse päivämäärä, jolloin kannatusilmoitusten keräys on tarkoitus aloittaa. Huomaa, että jos käytät useampaa kannatusilmoitusten keräystapaa (kansalaisaloite.fi, paperilomake, muu verkkopalvelu), aloitteen päiväyksen tulee olla kaikissa kannatusilmoituksissa sama.</p>
    <p>Kun oikeusministeriö on tarkastanut aloitteen ja olet saanut siitä kuittauksen sähköpostiisi, kannatusten keräys voi alkaa. Aloitteita käsitellään oikeusministeriössä virka-aikana ja tarkastus kestää arviolta muutamia päiviä.</p>
 
    <h4 id="proposal-type">Aloitteen muoto</h4>
 
    <p>Kansalaisaloite sisältää joko lakiehdotuksen tai ehdotuksen lainvalmisteluun ryhtymisestä. Aloite voi koskea myös voimassa olevan lain muuttamista tai kumoamista.</p>
    <p>Lakiehdotus tarkoittaa sitä, että aloite on valmisteltu lakitekstin muotoon. Jos siis olet valmistellut jo valmiin lakitekstin, valitse "lakiehdotus". Ehdotus lainvalmisteluun ryhtymisestä puolestaan tarkoittaa aloitetta, joka ei sisällä lakiehdotusta, vaan ehdotuksen siitä, että jostakin asiasta tulisi valtioneuvostossa käynnistää lainvalmistelu. Jos aloite tehdään ehdotuksena lainvalmisteluun ryhtymisestä, valitse siis "ehdotus lainvalmisteluun ryhtymisestä".</p>

    <blockquote>
        <p>Kansalaisaloitteella on kaksi eri muotoa.  Aloitemuodon valinta vaikuttaa aloitteen käsittelyjärjestykseen eduskunnassa. Aloitteen vireillepanija tekee itse arvion siitä kumpi aloitemuoto on tarkoituksenmukaisempi hänen aloitteelleen.</p>
        <p>1) Lakiehdotus</p>
        <p>Lakiehdotuksen tulee sisältää säädösteksti ja ehdotuksen perustelut. Lakiehdotus käsitellään eduskunnassa normaalin lakiehdotuksen käsittelystä säädetyssä järjestyksessä. Jos eduskunta hyväksyy lakiehdotuksen, se tulee voimaan normaalin lain tavoin.</p>   
        <p>2) Ehdotus lainvalmisteluun ryhtymisestä</p>
        <p>Ehdotuksen lainvalmisteluun ryhtymisestä ei tarvitse sisältää valmista säädöstekstiä. Tästä syystä tämä aloitemuoto sopii esimerkiksi laajoihin ja monimutkaisiin lainsäädäntöasioihin. Ehdotuksen tulee sisältää kuitenkin perustelut. Ehdotus käsitellään eduskunnassa yhden käsittelyn asiana. Jos ehdotus hyväksytään eduskunnan päätöksellä, tämä päätös toimitetaan eduskunnan kirjelmällä valtioneuvostolle toimenpiteisiin ryhtymistä varten.</p>
    </blockquote>
    
    <h4 id="proposal">Aloitteen sisältö</h4>

    <p>Kirjoita tähän tekstiä, josta aloitteesi keskeinen sisältö ilmenee. Jos aloite tehdään lakiehdotuksen muotoon, kirjoita tähän ehdottamasi säädösteksti.</p> 
    <p>Aloitteen tulee kohdistua yhteen asiakokonaisuuteen.</p>
 
    <h4 id="rationale">Perustelut</h4>

    <p>Kirjoita tähän aloitteesi perustelut. Ne ovat pakollinen osa aloitetta. Perustele aloitettasi niillä näkökohdilla, jotka tuovat mielestäsi parhaiten esille aloitteen yhteiskunnallisen merkityksen ja ehdottamasi lainsäädännön tarpeellisuuden. Kansalaisaloitteen perustelut ovat vapaamuotoiset, mutta niiden tulisi olla mahdollisimman lyhyet ja selkeät.</p>


    <h4 id="h1-2-2">ii. Aloitteen muut tiedot</h4>

    <p><@u.image src="help-other-details-fi.png" alt="Aloitteen muut tiedot" /></p>

    <h4 id="financial-support">Aloitteen taloudellinen tuki</h4>
 
    <p>Kansalaisaloitteen vireillepanijan ja edustajan on huolehdittava siitä, että aloitteen tekemistä varten saatu taloudellinen tuki ja sen antaja ilmoitetaan, jos yksittäisen tuen tai useista samalta tukijalta saaduista suorituksista koostuvan tuen arvo on vähintään 1 500 euroa. Taloudellisesta tuesta on kerrottava verkossa niin, että palvelun käyttäjät näkevät ilmoituksen viimeistään tuen vastaanottamista seuraavan kalenterikuukauden päättyessä, kuitenkin aikaisintaan kannatusilmoitusten keräyksen alkaessa.</p>
    <p>Kansalaisaloite.fi-palvelussa linkin taloudellisen tuen ilmoitukseen voi julkaista myös keräyksen aikana.</p>

    <h4 id="support-notifications">Kannatusilmoitusten keräystavat</h4>
 
    <p>Merkitse mitä eri keräystapoja (kansalaisaloite.fi, muut verkkokeräysjärjestelmät, keräys paperista kannatusilmoituslomaketta käyttäen) käytät kannatusilmoitusten keräämisessä. Voit myös ilmoittaa ja myöhemmin päivittää tietoja siitä, kuinka paljon kannatusilmoituksia on kerätty muita tapoja käyttäen. Kannatusilmoitusten kokonaismäärä näytetään aloitteen sivulla.</p>

    <h4 id="links">Linkit muihin verkkosivuihin</h4>

    <p>Voit lisätä tähän aloitteeseen liittyvien verkkosivujen osoitteita. Näitä voivat olla esimerkiksi aloitteen mahdollinen kotisivu tai aloitteeseen liittyvä lainsäädäntö (Finlex). Voit lisätä tähän useita verkkosivuja.</p>

    <h4 id="h1-2-3">iii. Omat tiedot</h4>

    <p><@u.image src="help-own-details-fi.png" alt="Omat tiedot" /></p>

    <h4 id="author-roles">Omat roolit (“Olen” -kenttä)</h4>
 
    <p>Oletuksena on, että aloitelomakkeen täyttäjä on vireillepanija ja aloitteen edustaja. Voit valita haluamasi rooli(t), mutta et voi olla samaan aikaan sekä edustaja että varaedustaja.</p> 

    <h4 id="author-details">Omat yhteystiedot</h4>
 
    <p>Täytä vähintään yksi yhteystietokenttä. Suositeltavaa on, että syötät ainakin sähköpostiosoitteesi, jolloin siihen lähetetään aloitteen käsittelyyn ja tilaan liittyviä viestejä kuten oikeusministeriön tarkastuksen hyväksyntä.</p>
    <p>Kansalaisaloitelain mukaan aloitteen edustajan yhteystiedot on oltava allekirjoittajien nähtävillä. Yhteystietoja käytetään asioinnissa eri viranomaisten kanssa. Jos olet edustaja tai varaedustaja, muut kansalaisaloite.fi-palvelun kävijät siis näkevät antamasi yhteystiedot. Huomaa, että vastuuhenkilöt näkevät kaikkien vastuuhenkilöiden koko nimen, kotikunnan, yhteystiedot sekä syntymäajan.</p>

    <h4 id="h1-2-4">iv.  Vastuuhenkilöt</h4>

    <p><@u.image src="help-organizer-details-fi.png" alt="Vastuuhenkilöt" /></p>

    <p>Kutsu henkilöitä eri vastuuhenkilöiden rooleihin kirjoittamalla heidän sähköpostiosoitteensa kenttiin. Aloitteen vireillepanoon tarvitaan vähintään kaksi vastuuhenkilöä - ts. sama henkilö voi olla vireillepanija ja edustaja tai vireillepanija ja varaedustaja, mutta ei edustaja ja varaedustaja.</p>
    <p>Tarvittaessa voit lähettää useita kutsuja samaan sähköpostiosoitteeseen, mutta eri henkilöille – esimerkiksi jos useampi henkilö käyttää samaa sähköpostiosoitetta.</p>

    <h4 id="save-and-send">Tallenna ja lähetä kutsut vastuuhenkilöille</h4>

    <p>Komennolla aloite tallennetaan järjestelmään ja nimetyille vastuuhenkilöille lähetetään kutsuviesti heidän sähköpostiinsa. Kutsu näyttää esimerkiksi seuraavalta:</p>

    <blockquote>
        <p>Matti Mikael Meikäläinen on kutsunut sinut varaedustajaksi kansalaisaloitteeseen. Alla näet tiivistelmän kansalaisaloitteen sisällöstä.</p>
        <p><strong>Kansalaisaloite kansalaisaloitepäivästä ja sen viettämisestä yleisenä juhla- ja vapaapäivänä</strong><br/>
        15.11.2012 Lakiehdotus, ei taloudellista tukea<p>
        
        <p>Tutustu varaedustajan velvollisuuksiin ja oikeuksiin Kansalaisaloite.fi:n ohjeissa.</p>
        
        <p>Jos haluat hyväksyä kutsun varaedustajaksi, siirry aloitteen sivulle alla olevalla linkillä ja valitse "Hyväksy". Tämän jälkeen sinut ohjataan tunnistautumaan. Tunnistautumiseen tarvitaan verkkopankkitunnuksia, mobiilivarmenne tai HST-kortti.</p>
        <p>Jos haluat hylätä kutsun, se tapahtuu myös aloitteen sivulla valitsemalla "Hylkää". Hylkääminen ei vaadi tunnistautumista.</p>
        
        <p><a class="small-button">Siirry hyväksymään kutsu</a></p> 
        
        <p>Tämä kutsu on voimassa 7 vuorokautta, jonka jälkeen et voi enää vastata kutsuun.</p>
         
        <h4>Tiivistelmä kansalaisaloitteesta</h4>
        <p>Ehdotustekstiä...</p>
        
        <p>Näytä aloitteen koko sisältö &rarr;</p>
    </blockquote>

    <p>Lähetetyt vastuuhenkilöiden kutsut ovat tietoturvallisuussyistä voimassa seitsemän päivää. Jos kutsu ehtii umpeutua, se voidaan lähettää uudelleen, mikäli aloitetta ei ole vielä tarkastettu.</p>
    <p>Kansalaisaloite on myös muiden käyttäjien löydettävissä aloitteen osoitteesta välittömästi sen jälkeen kun aloitteeseen on kutsuttu vastuuhenkilöitä.</p>
 
    <p><@u.link href="#h1-3" label="Ohjeet vastuuhenkilön roolin hyväksymiseen" /></p>

    <h4 id="save-as-draft">Tallenna ja jatka muokkaamista</h4>
 
    <p>Komento tallentaa aloitteen, jos haluat jatkaa aloitteen työstöä myöhemmin.</p>

    <h4 id="cancel">Peruuta ja palaa tallentamatta etusivulle</h4>

    <p>Komento tyhjentää lomakkeen. Mitään lomakkeen tietoja ei tallenneta.</p>
    
    
    <h3 id="h1-3">c. Muiden vastuuhenkilöiden roolien hyväksyntä</h3>

    <p>Vastuuhenkilö saa kutsun toiselta vastuuhenkilöltä sähköpostitse. Kutsu näyttää esimerkiksi tältä:</p>

    <blockquote>
        <p>Matti Mikael Meikäläinen on kutsunut sinut varaedustajaksi kansalaisaloitteeseen. Alla näet tiivistelmän kansalaisaloitteen sisällöstä.</p>
        <p><strong>Kansalaisaloite kansalaisaloitepäivästä ja sen viettämisestä yleisenä juhla- ja vapaapäivänä</strong><br/>
        15.11.2012 Lakiehdotus, ei taloudellista tukea<p>
        
        <p>Tutustu varaedustajan velvollisuuksiin ja oikeuksiin Kansalaisaloite.fi:n ohjeissa.</p>
        
        <p>Jos haluat hyväksyä kutsun varaedustajaksi, siirry aloitteen sivulle alla olevalla linkillä ja valitse "Hyväksy". Tämän jälkeen sinut ohjataan tunnistautumaan. Tunnistautumiseen tarvitaan verkkopankkitunnuksia, mobiilivarmenne tai HST-kortti.</p>
        <p>Jos haluat hylätä kutsun, se tapahtuu myös aloitteen sivulla valitsemalla "Hylkää". Hylkääminen ei vaadi tunnistautumista.</p>
        
        <p><a class="small-button">Siirry hyväksymään kutsu</a></p> 
        
        <p>Tämä kutsu on voimassa 7 vuorokautta, jonka jälkeen et voi enää vastata kutsuun.</p>
         
        <h4>Tiivistelmä kansalaisaloitteesta</h4>
        <p>Ehdotustekstiä...</p>
        
        <p>Näytä aloitteen koko sisältö &rarr;</p>
    </blockquote>

    <p><@u.image src="help-accept-invitation-fi.png" alt="Hyväksy kutsu" /></p>
    
    <p>Kutsun hyväksyminen vaatii vahvan tunnistautumisen kansalaisaloite.fi-palveluun. Lähetetyt vastuuhenkilöiden kutsut ovat tietoturvallisuussyistä voimassa seitsemän päivää.</p> 
    <p>Kutsuja voidaan lähettää uudelleen ja eri ihmisille myöhemmin mikäli aloitetta ei vielä ole tarkastettu. Jos aloitteen pysyviä tietoja muokataan vireillepanovaiheen aikana, kunkin vastuuhenkilön täytyy hyväksyä roolinsa uudelleen.</p>
    <p>Huomaa, että vastuuhenkilöt eivät erikseen saa tietoa kunkin muun vastuuhenkilön hyväksymästä tai hylkäämästä roolista. Vastuuhenkilö voi kirjautua palveluun ja seurata vireillepanovaiheessa olevan aloitteensa tilaa esimerkiksi Selaa aloitteita => Omat aloitteet  -linkistä tai sähköpostiviestissä lähetetyn aloitteen linkin kautta.</p>

    <h3 id="h1-4">d. Aloitteen lähettäminen oikeusministeriölle tarkastettavaksi</h3>

    <p>Kun aloitteella on tarvittava määrä vastuuhenkilöitä eli vähintään yksi kussakin roolissa, aloitteen vastuuhenkilö voi lähettää sen oikeusministeriöön tarkastettavaksi. Aloitteen vireillepanoon tarvitaan vähintään kaksi vastuuhenkilöä - sama henkilö voi olla vireillepanija ja edustaja tai vireillepanija ja varaedustaja.<p>

    <p>Vastuuhenkilö voi kirjautua palveluun ja seurata vireillepanovaiheessa olevan aloitteensa tilaa “Selaa kansalaisaloitteita => Omat kansalaisaloitteeni” -linkistä tai sähköpostiviestissä lähetetyn aloitteen linkin kautta. Kun aloitteella on riittävästi vastuuhenkilöitä, aloitteen sivu näyttää kirjautuneelle vastuuhenkilölle esimerkiksi tältä:</p>

    <p><@u.image src="help-send-to-om-fi.png" alt="Aloitteen lähettäminen oikeusministeriölle tarkastettavaksi" /></p>

    <p>Oikeusministeriön tarkastuksessa käydään läpi, että aloitteen tarvittavat tiedot on täytetty asianmukaisesti ja ettei aloite sisällä verkossa julkaistavaksi sopimatonta materiaalia. Oikeusministeriön tarkastus ei muutoin ota kantaa aloitteen sisältöön. Oikeusministeriön tarkastuksen jälkeen aloitteen pysyviä tietoja ei voi enää muuttaa.</p>

    <p>Kun oikeusministeriö on tarkastanut aloitteen, vastuuhenkilöille lähetetään sähköposti:</p>

    <blockquote>
        <h4>Oikeusministeriö on tarkastanut aloitteen</h4>
        <p>Oikeusministeriö on tarkastanut kansalaisaloitteen ja hyväksynyt sen julkaistavaksi kansalaisaloite.fi -palveluun. Aloitteen kannatusilmoitusten kerääminen alkaa yllä mainitusta päivämäärästä alkaen.</p>
        <p>Oikeusministeriön saate:</p>
    </blockquote>

    <p>Vastuuhenkilö voi kirjautua palveluun ja löytää vireillepanovaiheessa olevan aloitteensa “omat aloitteet” -linkin, sähköpostissa olevan linkin tai aloitteen linkin kautta.</p>
    
    
    <h3 id="h2">2. Aloitteen kannatusilmoitusten keräys ja tarkastus</h3>
    
    <p><@u.image src="flowstate-accepted.png" alt="Kannatusilmoitusten keräys ja tarkastus" /></p>
    
    <h4>Kannatusilmoitusten keräys</h4>

    <p>Kun oikeusministeriö on tarkastanut aloitteen sisällön ja muut tiedot, kannatusilmoituksia voidaan kerätä kansalaisaloite.fi -palvelussa.  Aloite on tällöin vaiheessa “Kannatusilmoitusten keräys ja tarkastus” ja “Kannata aloitetta” -nappi näkyy sivulla.  Aloitteen sivu näyttää esimerkiksi tältä</p> 

    <p><@u.image src="help-send-support-statement-1-fi.png" alt="Kannatusilmoitusten keräys" /></p>

    <p>Alussa aloitteen sivulle pääsee aloitteen linkin kautta. Kun aloitteella on vähintään 50 kannatusilmoitusta, kansalaisaloite tulee näkyviin kansalaisaloite.fi -palvelun aloitteiden selaussivulla. Ennen kuin aloite on esillä aloitteiden selaussivulla, vastuuhenkilöt ja muut käyttäjät voivat jakaa aloitteen linkkiä haluamallaan tavalla.</p>
    <p>Aloitetta voivat kannattaa äänioikeutetut Suomen kansalaiset. Kansalaisaloite.fi -palvelu tarkistaa kannattajien kansalaisuuden. Palvelu myös tunnistaa, jos henkilö yrittää kannattaa samaa aloitetta useampaan kertaan kansalaisaloite.fi -palvelussa.</p>
    
    <p><@u.image src="help-send-support-statement-2-fi.png" alt="Tallenna kansalaisaloitteen kannatusilmoitus" /></p>

    <h4>Kannatusilmoitusten lähettäminen Väestörekisterikeskuksen tarkistettavaksi</h4>

    <p>Kun aloitteella on vähintään 50 000 kannatusilmoitusta, ne voi lähettää Väestörekisterikeskukseen tarkastettavaksi. Pyynnön tarkastukseen lähettämisestä voi tehdä aloitteen edustaja tai varaedustaja. Edustajan tai varaedustajan tulee tässä tapauksessa kirjautua palveluun jolloin “Lähetä tarkastettavaksi” -nappi on käytettävissä.</p>

    <p><@u.image src="help-send-to-vrk-fi.png" alt="Kannatusilmoitusten lähettäminen Väestörekisterikeskuksen tarkistettavaksi" /></p>

    <p>Väestörekisterikeskus lataa aloitteen kannatusilmoitukset palvelusta omaan tietojärjestelmäänsä. Tämän jälkeen Väestörekisterikeskus tarkastaa kansalaisaloitteiden kannatusilmoitusten lukumäärän. Väestörekisterikeskus syöttää kansalaisaloitteen kannatusilmoitusten lukumäärän kansalaisaloite.fi –järjestelmään, jonka jälkeen vastuuhenkilöt saavat tiedon Väestörekisterikeskuksen päätöksestä sähköpostilla ja postitse.</p> 

    <p><@u.image src="help-vrk-resolution-fi.png" alt="Väestörekisterikeskuksen päätös" /></p>

    <p>Kannatusilmoitusten keräämistä voi jatkaa myös sen jälkeen, kun aloitteella on jo 50 000 kannatusilmoitusta. Näin voidaan varautua muun muassa siihen, että osa ilmoituksista saatetaan hylätä tarkastuksessa.</p>
    
      
    <h3 id="h3">3. Toimittaminen eduskuntaan</h3>

    <p>Jos Väestörekisterikeskuksen tarkastuksen mukaan aloite on saanut vähintään 50 000 hyväksyttyä kannatusilmoitusta, aloitteen edustaja voi toimittaa kansalaisaloitteen ja Väestörekisterikeskuksen päätöksen eduskuntaan.</p> 
    <#assign href>${urls.helpIndex()}/<@u.enumDescription HelpPage.GENERAL /></#assign>
    <p>Tarkemmat ohjeet kansalaisaloitteen toimittamisesta eduskuntaan löytyvät <@u.link href=href label="yleisistä ohjeista" />.</p>


    <h3 id="h4">4. Aloitteiden etsiminen, selaaminen ja seuraaminen</h3>

    <h3 id="h4-1">b. Selaa kansalaisaloitteita</h3>

    <p>“Selaa kansalaisaloitteita” -sivulla näytetään ne oikeusministeriön tarkastamat aloitteet, jotka ovat saaneet vähintään 50 kannatusilmoitusta. Aloitetta voi kannattaa ja linkkiä aloitteeseen voi vapaasti jakaa oikeusministeriön tarkastuksen jälkeen.</p>

    <p><@u.image src="help-browse-initiatives-fi.png" alt="Selaa kansalaisaloitteita" /></p>


    <h3 id="h4-2">c. Omien aloitteiden ylläpito</h3>

    <p>“Omat kansalaisaloitteeni” -sivulla näet ne aloitteet, joissa olet vastuuhenkilönä, riippumatta siitä missä vaiheessa kyseiset aloitteet ovat.</p> 
     
    <p>Aloitteen ylläpitoon pääsee seuraavasti:</p>
    <ul>
        <li>kirjaudu oikeasta ylälaidasta pankkitunnuksilla, mobiilivarmenteella tai sirullisella henkilökortilla</li>
        <li>mene sivulle “Selaa kansalaisaloitteita”</li>
        <li>sivulta löydät linkin “Omat kansalaisaloitteeni”</li>
    </ul>
 
    <p><@u.image src="help-my-initiatives-fi.png" alt="Omat kansalaisaloitteeni" /></p>
 

    <h3 id="h5">5. Aloitteen tietojen päivittäminen ja vastuuhenkilöiden vastuut</h3>

    <h4>Vastuuhenkilöiden vastuut</h4>
    
    <p>Tehdessään aloitteen tai hyväksyessään vastuuhenkilön roolin vastuuhenkilö vakuuttaa että</p>
    
    <ul>
        <li>hänen antamansa tiedot ovat oikeellisia</li>
        <li>muualla kuin kansalaisaloite.fi –verkkopalvelussa kerätyksi ilmoitetut  kannatusilmoitukset on kerätty kansalaisaloitelain edellyttämällä  tavalla</li>
        <li>hän ymmärtää, että vireillepanijana tai vireillepanijan edustajana hän on henkilörekisterinpitäjä ja ymmärtää velvoitteensa henkilötietojen suojaamiseen ja hävittämiseen kansalaisaloitelain (12/2012) ja henkilötietolain (523/1999) nojalla</li>
        <li>hän sallii, että vireillepanijana tai vireillepanijan edustajana hänen henkilötunnustaan käsitellään ja siitä laskettu tarkistesumma tallennetaan palveluun</li>
        <li>hän sallii oikeusministeriön toimia kansalaisaloitteen kannatusilmoitusten henkilötietojen keräyksessä toimeksisaajana ja yhtenä henkilörekisterin yhteyshenkilöistä</li>
    </ul>
    
    <h4>Aloitteen tietojen päivittäminen</h4>
    
    <p>Aloitteen vireillepanovaiheessa aloitteen vastuuhenkilöt voivat esimerkiksi:</p>
    
    <ul>
        <li>luoda kansalaisaloitteen ja muokata sitä</li>
        <li>kutsua muita henkilöitä vastuuhenkilöiksi</li>
        <li>lähettää aloitteen oikeusministeriöön tarkastettavaksi</li>
    </ul>
    
    <p>Kannatusilmoitusten keräyksen aikana aloitteen vastuuhenkilöt voivat esimerkiksi:</p>
    
    <ul>
        <li>päivittää tietoja kannatusilmoitusten keräystavoista</li>
        <li>päivittää tietoja muilla keräystavoilla kerättyjen kannatusilmoitusten määrästä</li> 
        <li>päivittää tietoja taloudellisen tuen saamisesta</li>
        <li>lisätä aloitteeseen liittyviä linkkejä </li>
        <li>seurata aloitteen kannatusilmoitusten määrää</li>
        <li>jakaa linkkiä aloitteeseen</li>
    </ul>
    
    <p>Kannatusilmoitusten keräyksen jälkeen aloitteen vastuuhenkilöt voivat esimerkiksi:</p>
    
    <ul>
        <li>lähettää kannatusilmoitukset Väestörekisterikeskuksen tarkastettavaksi</li>
        <li>hävittää kannatusilmoitukset</li>
    </ul>
     
    <p>Aloitteen ylläpitoon pääsee seuraavasti:</p>
    
    <ul>
        <li>kirjaudu oikeasta ylälaidasta tunnistautumalla pankkitunnuksilla, mobiilivarmenteella tai sirullisella henkilökortilla</li>
        <li>mene sivulle “Selaa kansalaisaloitteita”</li>
        <li>sivulta löydät linkin “Omat kansalaisaloitteeni”</li>
    </ul>
    
    <p>Linkki näkyy vain kirjautuneille käyttäjille.</p>

<#else>

    <div class="view-block">
        <h3>Innehåll</h3>
        <ol>
            <li><a href="#h1">Anhängiggörande av ett initiativ</a>
                <ol class="alpha">
                    <li><a href="#h1-1">Innan du börjar</a></li>
                    <li><a href="#h1-2">Inmatning av initiativblankettens uppgifter</a>
                        <ol class="roman">
                            <li><a href="#h1-2-1">Bestående information om initiativet</a></li>
                            <li><a href="#h1-2-2">Övrig information om initiativet</a></li>
                            <li><a href="#h1-2-3">Egna uppgifter</a></li>
                            <li><a href="#h1-2-4">Ansvarspersoner</a></li>
                        </ol>    
                    </li>
                    <li><a href="#h1-3">Godkännande av de övriga ansvarspersonernas roller</a></li>
                    <li><a href="#h1-4">Initiativets skickande till justitieministeriet</a></li>
                </ol>
            </li>
            <li><a href="#h2">Insamling och kontroll av initiativets stödförklaringar</a></li>
            <li><a href="#h3">Överlämnande till riksdagen</a></li>
            <li><a href="#h4">Att söka, bläddra bland och följa initiativ</a>
                <ol class="alpha">
                    <li><a href="#h4-1">Bläddra bland initiativen</a></li>
                    <li><a href="#h4-2">Mina medborgarinitiativ</a></li>    
                </ol>
            </li>
            <li><a href="#h5">Initiativets administrering och ansvarspersonernas ansvar</a></li>
        </ol>
    </div>

    
    <h3 id="h1">1. Anhängiggörande av ett initiativ</h3>

    
    <h3 id="h1-1">a. Innan du börjar</h3>

    <#assign href>${urls.helpIndex()}/<@u.enumDescription HelpPage.GENERAL /></#assign>
    <p>Om medborgarinitiativförfarandet inte är bekant för dig, läs <@u.link href=href label="den allmänna informationen om medborgarinitiativet" />.</p>
    <#assign href>${urls.helpIndex()}/<@u.enumDescription HelpPage.PROGRESS /></#assign>
    <p>Du får en helhetsbild av initiativets framskridande i tjänsten medborgarinitiativ.fi till exempel på sidan “<@u.link href=href label="Så här framskrider initiativet i tjänsten medborgarinitiativ.fi" />”.</p>
    <p>Vid behov kan du ställa frågor om tjänsten till justitieministeriet per e-post på adressen <@u.scrambleEmail "kansalaisaloite.om@om.fi" /> eller i <@u.link href="https://www.facebook.com/aloite" label="Facebook" rel="external" />-gruppen.</p> 

    <h3 id="h1-2">b. Inmatning av initiativblankettens uppgifter</h3>

    <p>Välj ”Skapa ett medborgarinitiativ” på ingångssidan för att skapa ett nytt medborgarinitiativ.</p> 
    <p>Observera att du måste identifiera dig med dina nätbankskoder, mobilcertifikat eller elektroniskt ID-kort om du vill skapa ett medborgarinitiativ. Av dataskyddsskäl avbryts sessionen efter 30 minuter. Om din webbläsare godkänner javascript förlängs sessionen till högst 120 minuter medan du skapar eller redigerar initiativet. Således rekommenderas det att du skriver eventuella längre texter på förhand med exempelvis ett textbehandlingsprogram och sedan överför dem i tjänstens blankett.</p>


    <h4 id="h1-2-1">i. Bestående information om initiativet</h4>

    <p><@u.image src="help-basic-details-sv.png" alt="Bestående information om initiativet" /></p>

    <p>Ett initiativ får skapas på finska eller svenska eller på bägge språken. Länken ”Lägg till finskspråkig version” lägger till eller döljer de finskspråkiga fälten för initiativets titel, innehåll och motiveringar.</p> 

    <h4 id="name">Medborgarinitiativets titel</h4>
 
    <p>Skriv medborgarinitiativets namn i titelfältet. Titeln ska vara tydlig och beskriva initiativets egentliga innehåll. Du kan även samla in stödförklaringar genom att använda pappersblanketten eller i övriga webbtjänster, men titeln måste vara exakt densamma överallt för att de olika stödförklaringstyperna ska anses beröra samma initiativ.</p>
 
    <h4 id="date">Initiativets datum</h4>
 
    <p>Initiativets datum är det datum då insamlingen av stödförklaringar är avsedd att inledas eller då insamlingen inletts med något annat insamlingssätt.</p> 
    <p>Välj det datum då insamlingen av stödförklaringar är avsedd att inledas. Observera att om du använder flera insamlingssätt för stödförklaringar (medborgarinitiativ.fi, pappersblankett, annan webbtjänst) måste alla stödförklaringar ha samma datum.</p>
    <p>Insamlingen av stöd kan börja då justitieministeriet granskat initiativet och du fått en bekräftelse i din e-post. Justitieministeriet behandlar initiativen under tjänstetid och granskningen beräknas ta några dagar.</p>
 
    <h4 id="proposal-type">Initiativets form</h4>
 
    <p>Ett medborgarinitiativ innehåller antingen ett lagförslag eller ett förslag om lagberedning. Initiativet kan även gälla ändring eller upphävande av gällande lag.</p>
    <p>Alternativet lagförslag innebär att initiativet utformats som en lagtext. Om du alltså redan har berett en färdig lagtext, välj "lagförslag". Förslag om lagberedning avser å sin sida ett initiativ som inte innefattar ett lagförslag, utan ett förslag om att statsrådet bör inleda en lagberedning om något ärende. Välj alltså ”förslag om att lagberedning ska inledas” om initiativet utformas som ett förslag till lagberedning.</p>

    <blockquote>
        <p>Det finns två olika former av medborgarinitiativ. Valet av initiativform inverkar på behandlingsordningen av initiativet i riksdagen. Den som väcker initiativet avgör själv vilken initiativform som är mer ändamålsenlig för hans eller hennes initiativ.</p>
        <p>1) Lagförslag</p>
        <p>Ett lagförslag ska innehålla en författningstext och motiveringar till förslaget. Lagförslaget behandlas i riksdagen i den normala ordning som föreskrivs för lagförslag. Om riksdagen godkänner lagförslaget träder det i kraft som en vanlig lag.</p>   
        <p>2) Förslag om att lagberedning ska inledas</p>
        <p>Ett förslag om att lagberedning ska inledas behöver inte innehålla en färdig författningstext. Därför lämpar sig denna initiativform exempelvis för omfattande och komplicerade lagstiftningsärenden. Förslaget ska dock innehålla motiveringar. Förslaget behandlas i riksdagen i en behandling. Om förslaget godkänns enligt riksdagens beslut skickas beslutet genom riksdagens skrivelse till statsrådet som vidtar åtgärder.</p>
    </blockquote>
    
    <h4 id="proposal">Initiativets innehåll</h4>

    <p>Här ska du skriva det centrala innehållet i ditt initiativ. Om initiativet utformas som ett lagförslag, skriv ditt förslag till författningstext här.</p> 
    <p>Initiativet ska beröra endast en helhet.</p>
 
    <h4 id="rationale">Motiveringar</h4>

    <p>Här ska du skriva motiveringar till initiativet. De är en obligatorisk del av initiativet. Motivera initiativet med sådana synpunkter som enligt dig på bästa sätt framhäver initiativets samhälleliga betydelse och nödvändigheten av den lagstiftning som du föreslår. Medborgarinitiativets motiveringar får vara fritt formulerade, men de ska vara så kortfattade och tydliga som möjligt.</p>


    <h4 id="h1-2-2">ii. Övrig information om initiativet</h4>

    <p><@u.image src="help-other-details-sv.png" alt="Övrig information om initiativet" /></p>

    <h4 id="financial-support">Ekonomiskt bidrag för initiativet</h4>
 
    <p>Den som väcker ett medborgarinitiativ och företrädaren ska se till att ekonomiska bidrag för framläggande av initiativet och givaren uppges, om värdet på ett enskilt bidrag eller värdet av flera bidrag från en och samma givare är minst 1 500 euro. Ekonomiska bidrag ska redovisas i nätet så att tjänstens användare kan se meddelandet senast vid utgången av den kalendermånad som följer efter det att bidraget togs emot, dock tidigast när insamlingen av stödförklaringar inleds.</p>
    <p>I tjänsten medborgarinitiativ.fi kan länken till meddelandet om ekonomiskt bidrag publiceras även under insamlingen.</p>

    <h4 id="support-notifications">Insamlingskanaler för stödförklaringar</h4>
 
    <p>Ange vilka insamlingskanaler (medborgarinitiativ.fi, andra webbinsamlingssystem, insamling genom att använda stödförklaringsblanketten i pappersform) du använder vid insamling av stödförklaringar. Du kan även meddela och senare uppdatera uppgifterna om antalet stödförklaringar som samlats in genom övriga kanaler. Det totala antalet stödförklaringar visas på initiativets sida.</p>

    <h4 id="links">Länkar till andra webbplatser</h4>

    <p>Här kan du ange adresser till webbplatser som anknyter till initiativet. Det kan exempelvis vara fråga om en webbplats för initiativet eller lagstiftning som anknyter till initiativet (Finlex). Du kan ange flera webbplatser.</p>

    <h4 id="h1-2-3">iii. Egna uppgifter</h4>

    <p><@u.image src="help-own-details-sv.png" alt="Egna uppgifter" /></p>

    <h4 id="author-roles">Egna roller (fältet ”Jag”)</h4>
 
    <p>Utgångspunkten är att den som fyller i initiativblanketten är initiativtagare och initiativets företrädare. Du kan själv välja din roll eller dina roller, men du kan inte samtidigt fungera som både företrädare och ersättare.</p> 

    <h4 id="author-details">Egna kontaktuppgifter</h4>
 
    <p>Fyll i minst ett kontaktuppgiftsfält. Det rekommenderas att du anger åtminstone din e postadress, dit du kan motta meddelanden om behandlingen av initiativet och om hur initiativet framskrider, exempelvis godkännandet av justitieministeriets granskning.</p>
    <p>Enligt lagen om medborgarinitiativ ska företrädarens kontakuppgifter finnas tillgängliga för undertecknarna. Kontaktuppgifterna används i kontakten med olika myndigheter. Om du är företrädare eller ersättare kan alltså alla besökare i tjänsten medborgarinitiativ.fi se dina kontaktuppgifter. Observera att ansvarspersonerna ser alla ansvarspersoners fullständiga namn, hemkommun, kontaktuppgifter och födelsedatum.</p>

    <h4 id="h1-2-4">iv. Ansvarspersoner</h4>

    <p><@u.image src="help-organizer-details-sv.png" alt="Ansvarspersoner" /></p> 

    <p>Bjud in ansvarspersoner genom att skriva in deras e-postadresser i fälten. Det behövs minst två ansvarspersoner för att väcka ett initiativ – med andra ord kan samma person vara initiativtagare och företrädare eller initiativtagare och ersättare, men inte företrädare och ersättare.</p>
    <p>Vid behov kan du skicka flera inbjudningar till olika personer på samma e-postadress, exempelvis om en e-postadress används av flera personer.</p>

    <h4 id="save-and-send">Spara och skicka inbjudningar till ansvarspersonerna</h4>

    <p>Med kommandot sparas initiativet i systemet och ett meddelande om inbjudan skickas till de angivna ansvarspersonernas e-postadresser. Inbjudan kan se ut till exempel på följande sätt:</p>

    <blockquote>
        <p>Matti Mikael Meikäläinen har bjudit in dig som ersättare för ett medborgarinitiativ. Nedan finner du en sammanfattning av medborgarinitiativets innehåll.</p>
        <p><strong>Medborgarinitiativ om en dag för medborgarinitiativ och om att dagen ska bli allmän högtidsdag och ledig dag</strong><br/>
        15.11.2012 Lagförslag, inget ekonomiskt stöd<p>
        
        <p>Bekanta dig med ersättarens skyldigheter och rättigheter i anvisningarna på medborgarinitiativ.fi.</p>
        
        <p>Om du vill godkänna inbjudan till ersättare, gå till initiativets sida via länken nedan och välj "Godkänn". Därefter ges anvisningar om identifiering. Du behöver nätbankskoder, mobilcertifikat eller HST-kort för att identifiera dig.</p>
        <p>Om du vill avböja inbjudan sker det också på initiativets sida genom att välja "Avböj". Det krävs ingen identifikation för att avböja inbjudan.</p>
        
        <p><a class="small-button">Gå till Godkänn inbjudan</a></p> 
        
        <p>Inbjudan är i kraft i 7 dygn, varefter du inte längre kan svara på inbjudan.</p>
         
        <h4>Sammanfattning av medborgarinitiativet</h4>
        <p>Förslagstext...</p>
        
        <p>Visa initiativets hela innehåll &rarr;</p>
    </blockquote>

    <p>Av dataskyddsskäl är inbjudningar som skickats till ansvarspersonerna i kraft i sju dagar. Om en inbjudan hinner förfalla kan den skickas på nytt om initiativet inte ännu har granskats.</p>
    <p>Medborgarinitiativet är också tillgängligt för övriga användare på initiativets adress omedelbart efter att ansvarspersoner för initiativet bjudits in.</p>
 
    <p><@u.link href="#h1-3" label="Anvisningar för godkännande av rollen som ansvarsperson" /></p>

    <h4 id="save-as-draft">Spara och fortsätt redigera</h4>
 
    <p>Kommandot sparar initiativet om du vill fortsätta redigera initiativet vid en senare tidpunkt.</p>

    <h4 id="cancel">Avbryt utan att spara och gå tillbaka till ingångssidan</h4>

    <p>Kommandot tömmer blanketten. Alla uppgifter i blanketten raderas.</p>
    
    
    <h3 id="h1-3">c. Godkännande av de övriga ansvarspersonernas roller</h3>

    <p>En ansvarsperson får inbjudan av en annan ansvarsperson per e-post. Inbjudan kan se ut till exempel på följande sätt:</p>

    <blockquote>
        <p>Matti Mikael Meikäläinen har bjudit in dig som ersättare för ett medborgarinitiativ. Nedan finner du en sammanfattning av medborgarinitiativets innehåll.</p>
        <p><strong>Medborgarinitiativ om en dag för medborgarinitiativ och om att dagen ska bli allmän högtidsdag och ledig dag</strong><br/>
        15.11.2012 Lagförslag, inget ekonomiskt stöd<p>
        
        <p>Bekanta dig med ersättarens skyldigheter och rättigheter i anvisningarna på medborgarinitiativ.fi.</p>
        
        <p>Om du vill godkänna inbjudan till ersättare, gå till initiativets sida via länken nedan och välj "Godkänn". Därefter ges anvisningar om identifiering. Du behöver nätbankskoder, mobilcertifikat eller HST-kort för att identifiera dig.</p>
        <p>Om du vill avböja inbjudan sker det också på initiativets sida genom att välja "Avböj". Det krävs ingen identifikation för att avböja inbjudan.</p>
        
        <p><a class="small-button">Gå till Godkänn inbjudan</a></p> 
        
        <p>Inbjudan är i kraft i 7 dygn, varefter du inte längre kan svara på inbjudan.</p>
         
        <h4>Sammanfattning av medborgarinitiativet</h4>
        <p>Förslagstext...</p>
        
        <p>Visa initiativets hela innehåll &rarr;</p>
    </blockquote>

    <p><@u.image src="help-accept-invitation-sv.png" alt="Inbjudning" /></p>
    
    <p>Godkännande av inbjudan förutsätter en stark autentisering i tjänsten medborgarinitiativ.fi.</p> 
    <p>Av dataskyddsskäl är inbjudningar som skickats till ansvarspersonerna i kraft i sju dagar. Inbjudningar kan skickas på nytt och till olika personer vid en senare tidpunkt om initiativet inte ännu har granskats. Om initiativets bestående uppgifter redigeras under anhängiggörandefasen måste alla ansvarspersoner godkänna sina roller på nytt.</p>
    <p>Observera att ansvarspersonerna inte får separat information om vilka roller de övriga ansvarspersonerna har godkänt eller avböjt. En ansvarsperson kan logga in i tjänsten och följa med sitt anhängiggjorda initiativs status till exempel via länken Bläddra bland initiativen => Mina initiativ eller via länken till initiativet som skickats i ett e-postmeddelande.</p>

    <h3 id="h1-4">d. Initiativets skickande till justitieministeriet för granskning</h3>

    <p>Då ett initiativ har en tillräcklig mängd ansvarspersoner, dvs. minst en person i varje roll, kan initiativets ansvarsperson skicka det till justitieministeriet för granskning. Det behövs minst två ansvarspersoner för att väcka ett initiativ – samma person kan vara initiativtagare och företrädare eller initiativtagare och ersättare.<p>

    <p>En ansvarsperson kan logga in i tjänsten och följa med sitt initiativs status i anhängiggörandefasen till exempel via länken ”Bläddra bland medborgarinitiativen => Mina medborgarinitiativ” eller via länken till initiativet som skickats i ett e-postmeddelande. Då ett initiativ har ett tillräckligt antal ansvarspersoner kan initiativets sida för en inloggad ansvarsperson se ut exempelvis på följande sätt:</p>

    <p><@u.image src="help-send-to-om-sv.png" alt="Initiativets skickande till justitieministeriet för granskning" /></p>

    <p>Vid justitieministeriets granskning kontrolleras att de nödvändiga uppgifterna är korrekt ifyllda och att initiativet inte innehåller sådant material som inte lämpar sig för publicering på webben. Justitieministeriets granskning tar inte i övrigt ställning till initiativets innehåll. Efter justitieministeriets granskning är det inte längre möjligt att ändra initiativets bestående uppgifter.</p>

    <p>Då justitieministeriet granskat initiativet får ansvarspersonen följande e postmeddelande:</p>

    <blockquote>
        <h4>Justitieministeriet har granskat initiativet</h4>
        <p>Justitieministeriet har granskat medborgarinitiativet och gett sitt godkännande för publicering i tjänsten medborgarinitiativ.fi. Insamlingen av stödförklaringar för initiativet inleds från och med ovan nämnda datum.</p>
        <p>Justitieministeriets följebrev:</p>
    </blockquote>

    <p>Ansvarspersonen kan logga in i tjänsten och hittar sitt anhängiggjorda initiativ via länken ”Mina initiativ”, via länken som skickats i ett e-postmeddelande eller via initiativets länk.</p>
    
    
    <h3 id="h2">2. Insamling och kontroll av initiativets stödförklaringar</h3>
    
    <p><@u.image src="flowstate-accepted-sv.png" alt="Insamling och kontroll av stödförklaringar" /></p>
    
    <h4>Insamling av stödförklaringar</h4>

    <p>Stödförklaringar samlas in via tjänsten medborgarinitiativ.fi efter att justitieministeriet har granskat initiativets innehåll och övriga uppgifter. Då är initiativet i fasen ”Insamling och kontroll av stödförklaringar” och knappen ”Understöd initiativet” syns på initiativets sida. Initiativets sida kan se ut exempelvis på följande sätt:</p> 

    <p><@u.image src="help-send-support-statement-1-sv.png" alt="Insamling av stödförklaringar" /></p>

    <p>Till en början kommer man åt initiativets sida via länken till initiativet. Då ett initiativ fått minst 50 stödförklaringar syns medborgarinitiativet på sidan Bläddra bland initiativen i tjänsten medborgarinitiativ.fi. Före initiativet blir synligt på sidan Bläddra bland initiativen kan ansvarspersonerna och övriga användare sprida initiativets länk på önskat sätt.</p>
    <p>Röstberättigade finska medborgare kan understöda initiativet. Tjänsten medborgarinitiativ.fi kontrollerar medborgarskapet för de personer som understöder initiativet. Tjänsten kan också identifiera personer som försöker understöda samma initiativ flera gånger i tjänsten medborgarinitiativ.fi.</p>
    
    <p><@u.image src="help-send-support-statement-2-sv.png" alt="Spara stödförklaringen för medborgarinitiativet" /></p>

    <h4>Skickande av stödförklaringarna till Befolkningsregistercentralen för kontroll</h4>

    <p>Då ett initiativ har fått minst 50 000 stödförklaringar kan de skickas till Befolkningsregistercentralen för kontroll. Initiativets företrädare eller ersättare kan begära att stödförklaringarna skickas för kontroll. I detta fall bör företrädaren eller ersättaren logga in i tjänsten eftersom knappen ”Skicka till granskning” då är aktiv.</p>

    <p><@u.image src="help-send-to-vrk-sv.png" alt="Skickande av stödförklaringarna till Befolkningsregistercentralen för kontroll" /></p>

    <p>Befolkningsregistercentralen laddar ner initiativets stödförklaringar från tjänsten till sitt eget datasystem. Därefter kontrollerar Befolkningsregistercentralen antalet stödförklaringar för medborgarinitiativet. Befolkningsregistercentralen matar in antalet stödförklaringar för medborgarinitiativet i systemet medborgarinitiativ.fi, varefter ansvarspersonerna informeras om Befolkningsregistercentralens beslut per e-post och per post.</p> 

    <p><@u.image src="help-vrk-resolution-sv.png" alt="Befolkningsregistercentralens beslut" /></p> 

    <p>Insamlingen av stödförklaringar kan fortsätta även efter att initiativet uppnått 50 000 stödförklaringar. På så sätt är det möjligt att vara förberedd på bland annat att en del av förklaringarna kan förkastas i granskningen.</p>
    
      
    <h3 id="h3">3. Överlämnande till riksdagen</h3>

    <p>Om ett initiativ enligt Befolkningsregistercentralens granskning har fått minst 50 000 godkända stödförklaringar kan initiativets företrädare överlämna medborgarinitiativet och Befolkningsregistercentralens beslut till riksdagen.</p> 
    <#assign href>${urls.helpIndex()}/<@u.enumDescription HelpPage.GENERAL /></#assign>
    <p>Mer specifika anvisningar om överlämnande av ett medborgarinitiativ till riksdagen finns i <@u.link href=href label="de allmänna anvisningarna" />.</p>


    <h3 id="h4">4. Att söka, bläddra bland och följa initiativ</h3>

    <h3 id="h4-1">b. Bläddra bland medborgarinitiativen</h3>

    <p>På sidan ”Bläddra bland medborgarinitiativen” visas de initiativ som justitieministeriet granskat och som fått minst 50 stödförklaringar. Efter justitieministeriets granskning kan initiativet understödas och länken till initiativet kan spridas fritt.</p>

    <p><@u.image src="help-browse-initiatives-sv.png" alt="Bläddra bland medborgarinitiativen" /></p>


    <h3 id="h4-2">c. Administrering av de egna initiativen</h3>

    <p>På sidan ”Mina medborgarinitiativ” ser du de initiativ för vilka du är ansvarsperson, oberoende av i vilken fas initiativen är.</p> 
     
    <p>Du kan administrera ett initiativ på följande sätt:</p>
    <ul>
        <li>logga in vid högra övre kanten med dina nätbankskoder, med mobilcertifikat eller med elektroniskt ID-kort</li>
        <li>gå till sidan ”Bläddra bland medborgarinitiativen”</li>
        <li>på sidan hittar du länken ”Mina medborgarinitiativ”</li>
    </ul>
 
    <p><@u.image src="help-my-initiatives-sv.png" alt="Mina medborgarinitiativ" /></p>
 

    <h3 id="h5">5. Uppdatering av initiativets uppgifter och ansvarspersonernas ansvar</h3>

    <h4>Ansvarspersonernas ansvar</h4>
    
    <p>Då ansvarspersonen väcker ett initiativ eller godkänner rollen som ansvarsperson försäkrar personen att</p>
    
    <ul>
        <li>de uppgifter han eller hon gett är riktiga</li>
        <li>de stödförklaringar som uppgetts som insamlade via andra kanaler än webbtjänsten medborgarinitiativ.fi är insamlade enligt vad som föreskrivs i lagen om medborgarinitiativ</li>
        <li>han eller hon förstår att han eller hon i egenskap av den som väcker initiativ eller dennes företrädare är personregisteransvarig och är skyldig att skydda och förstöra personuppgifterna enligt lagen om medborgarinitiativ (12/2012) och personuppgiftslagen (523/1999)</li>
        <li>han eller hon tillåter i egenskap av den som väcker initiativ eller dennes företrädare att hans eller hennes personbeteckning behandlas och att den uträknade kontrollsumman utgående från personbeteckningen lagras i tjänsten</li>
        <li>han eller hon tillåter justitieministeriet agera som uppdragshavare vid insamling av personuppgifterna i stödförklaringarna för medborgarinitiativet och som en av personregistrets kontaktpersoner</li>
    </ul>
    
    <h4>Uppdatering av initiativets uppgifter</h4>
    
    <p>I anhängiggörandefasen kan initiativets ansvarspersoner exempelvis:</p>
    
    <ul>
        <li>skapa ett initiativ och redigera det</li>
        <li>bjuda in övriga personer som ansvarspersoner</li>
        <li>skicka initiativet till justitieministeriet för granskning</li>
    </ul>
    
    <p>Under insamlingen av stödförklaringar kan initiativets ansvarspersoner exempelvis:</p>
    
    <ul>
        <li>uppdatera uppgifterna om stödförklaringarnas insamlingskanaler</li>
        <li>uppdatera uppgifterna om antalet stödförklaringar som samlats in via andra kanaler</li>
        <li>uppdatera uppgifterna om mottagande av ekonomiskt bidrag</li>
        <li>lägga till länkar som anknyter till initiativet</li>
        <li>följa med antalet stödförklaringar för initiativet</li>
        <li>sprida initiativets länk</li>
    </ul>
    
    <p>Efter insamlingen av stödförklaringar kan initiativets ansvarspersoner exempelvis:</p>
    
    <ul>
        <li>skicka stödförklaringarna till Befolkningsregistercentralen för kontroll</li>
        <li>förstöra stödförklaringarna</li>
    </ul>
     
    <p>Du kan administrera ett initiativ på följande sätt:</p>
    
    <ul>
        <li>logga in vid högra övre kanten genom att identifiera dig med dina nätbankskoder, med mobilcertifikat eller med elektroniskt ID-kort</li>
        <li>gå till sidan ”Bläddra bland medborgarinitiativen”</li>
        <li>på sidan hittar du länken ”Mina medborgarinitiativ”</li>
    </ul>
    
    <p>Länken är synlig endast för inloggade användare.</p>
    
    <#-- TODO: SCREENSHOT -->

</#if>