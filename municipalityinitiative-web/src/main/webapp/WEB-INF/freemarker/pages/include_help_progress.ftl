<#import "../components/utils.ftl" as u />

<#if locale == "fi">
    
    <div class="view-block">
        <h3>Sisällysluettelo</h3>
    
        <ol>
            <li><a href="#h1">Tiivistelmä</a></li>
            <li><a href="#h2">Kansalaisaloite</a></li>
            <li><a href="#h3">Aloitteen valmistelu</a></li>
            <li><a href="#h4">Aloitteen vireillepano kansalaisaloite.fi -palvelussa</a></li>
            <li><a href="#h5">Kannatusilmoitusten keräys ja tarkastus</a></li>
            <li><a href="#h6">Kansalaisaloitteen toimittaminen eduskuntaan</a></li>
            <li><a href="#h7">Muuta</a>
                <ul> 
                    <li><a href="#h7-1">Aloitteen kannatusilmoitusten keräyksen päättyminen</a></li>
                    <li><a href="#h7-2">Kannatusilmoitusten hävittäminen</a></li>
                </ul>
            </li>
        </ol>
    </div>

    <h3 id="h1">Tiivistelmä</h3>

    <p>Kansalaisaloite.fi-palvelussa aloite etenee seuraavalla tavalla:</p>

    <div>Vireillepano</div>
    <ul>
        <li>aloitteen valmistelu</li>
        <li>aloitteen vireillepano kansalaisaloite.fi -palvelussa</li>
        <li>oikeusministeriön tarkastus</li>
    </ul>

    <div>Kannatusilmoitusten keräys ja tarkastus</div> 
    <ul>
        <li>kannatusilmoitusten keräys vastuuhenkilöiden järjestämänä korkeintaan 6kk ajan</li>
        <li>Väestörekisterikeskus tarkastaa kannattajailmoitusten oikeellisuuden ja lukumäärän</li>
    </ul>

    <p>Kansalaisaloitteen toimittaminen eduskuntaan</p>

    <p>Kansalaisaloite.fi -palvelussa aloitteen vaihe näkyy selkeästi kunkin aloitteen sivun ylälaidassa, kuvan mukaisella kaaviolla.</p>
    
    <p><@u.image src="flowstate-proposal.png" alt="Vaihepolku" /></p>

    <h3 id="h2">Kansalaisaloite</h3>

    <p>Kansalaisaloite antaa kansalaisille mahdollisuuden saada aloitteensa eduskunnan käsiteltäväksi. Uuden järjestelmän yleisenä tavoitteena on edistää ja tukea vapaata kansalaistoimintaa sekä siten vahvistaa kansalaisyhteiskuntaa, jossa eri väestöryhmät osallistuvat ja vaikuttavat aktiivisesti yhteiskunnan kehittämiseen.</p>
    <p>Joulukuun 1. päivänä 2012 avattu kansalaisaloite.fi -palvelu tarjoaa tavan aloitteiden käynnistämiseen ja hallinnointiin verkossa. Kansalaisaloite.fi-palvelussa aloitemenettely on kolmivaiheinen. Vireillepanon jälkeen aloite etenee kannatusilmoitusten keräämiseen ja jos aloitteella todetaan Väestörekisterikeskuksen tarkastuksessa olevan vähintään 50 000 hyväksyttyä kannatusilmoitusta, se voidaan toimittaa eduskunnan käsittelyyn.</p>
    
    <#assign href>${urls.helpIndex()}/<@u.enumDescription HelpPage.GENERAL /></#assign>
    <p><@u.link href=href label="Lue lisää" /></p>


    <h3 id="h3">Aloitteen valmistelu</h3>

    <p>Ennen kansalaisaloitteen varsinaista vireillepanoa tai käynnistämistä henkilö tai henkilöt voivat valmistella kansalaisaloitetta haluamallaan tavalla. Tämä ennakkovalmistelu tapahtuu Kansalaisaloite.fi –palvelun ulkopuolella.</p>

    <h3 id="h4">Aloitteen vireillepano kansalaisaloite.fi -palvelussa</h3>
    
    <p>Aloitteen vireillepano tarkoittaa, että aloiteteksti ja muut tiedot tallennetaan palveluun, aloitteeseen kutsutaan muita vastuuhenkilöitä ja aloite lähetetään oikeusministeriöön tarkastettavaksi. Tämän jälkeen aloitteen kannatusilmoitusten kerääminen voi alkaa.</p>
    <p>Kansalaisaloitteen vireillepanijana voi olla yksi tai useampi äänioikeutettu Suomen kansalainen.  Vireillepanijan on nimettävä vähintään yksi edustaja ja varaedustaja hoitamaan asiaan liittyviä käytännön toimia.</p>
    <p>Kansalaisaloite voi sisältää joko lakiehdotuksen tai ehdotuksen lainvalmisteluun ryhtymisestä. Aloite voi koskea myös voimassa olevan lain muuttamista tai kumoamista. Jos aloite tehdään lakiehdotuksen muotoon, sen tulee sisältää säädösteksti. Aloitteen tulee kohdistua yhteen asiakokonaisuuteen ja siinä tulee aina olla mukana perustelut.</p>

    <p>Vireillepano etenee seuraavasti:</p>

    <ol>
        <li>Tunnistaudutaan palveluun verkkopankkitunnuksin, mobiilivarmenteella tai sirullisella henkilökortilla (ns. vahva sähköinen tunnistautuminen)</li>
        <li>Aloitteen tiedot syötetään verkkolomakkeella</li>
        <li>Aloitteeseen kutsutaan mukaan vastuuhenkilöitä eri rooleihin (mahdolliset muut vireillepanijat, edustaja(t), varaedustaja(t))</li>
        <li>Muut vastuuhenkilöt tunnistautuvat ja hyväksyvät roolinsa</li>
        <li>Aloite lähetetään oikeusministeriöön kun aloitteella on vähintään yksi vastuuhenkilö kussakin näistä kolmesta roolista (vireillepanija, edustaja ja varaedustaja)</li>
        <li>Oikeusministeriö tarkastaa aloitteen</li>
        <li>Kannatusilmoitusten keräys kansalaisaloite.fi -palvelussa alkaa, kun tarkastus on tehty.</li>
    </ol> 

    <p>Oikeusministeriön tarkastuksessa käydään läpi, että aloitteen tarvittavat tiedot on täytetty ja ettei aloite sisällä verkossa julkaistavaksi sopimatonta materiaalia. Oikeusministeriön tarkastus ei muutoin ota kantaa aloitteen sisältöön. Oikeusministeriön tarkastuksen jälkeen aloitteen pysyviä tietoja ei voi enää muuttaa.</p>

    <#assign href>${urls.helpIndex()}/<@u.enumDescription HelpPage.INITIATIVE /></#assign>
    <p>Tarkemmat tiedot <@u.link href=href label="Käyttöohjeissa" rel="external" /><br/>
    <#assign href>${urls.helpIndex()}/<@u.enumDescription HelpPage.INITIATIVE />#h5</#assign>
    Tutustu myös <@u.link href=href label="vastuuhenkilöiden velvollisuuksiin" /></p>


    <h3 id="h5">Kannatusilmoitusten keräys ja tarkastus</h3>
    
    <p><@u.image src="flowstate-accepted.png" alt="Kannatusilmoitusten keräys ja tarkastus" /></p>

    <h4>Kannatusilmoitusten keräys</h4>

    <#assign href>${urls.helpIndex()}/<@u.enumDescription HelpPage.FAQ />#h5</#assign>
    <#assign label><@u.message "page.help.faq.title" /></#assign>
    <p>Kun oikeusministeriö on tarkastanut aloitteen julkaisukelpoisuuden, kannatusilmoituksia voidaan kerätä kansalaisaloite.fi -palvelussa. Kun aloitteella on vähintään 50 kannatusilmoitusta, se tulee näkyviin kansalaisaloite.fi -palvelun aloitteiden selaussivulla. Tätä ennen aloitteeseen pääsee suoraan linkin kautta. Ennen kuin aloite on esillä haussa, vireillepanijat voivat jakaa aloitteen linkkiä haluamallaan tavalla (<@u.link href=href label=label />).</p>
    <p>Aloitteen vastuuhenkilönä toimiminen ei automaattisesti tarkoita aloitteen kannattamista. Vastuuhenkilöiden tulee siis myös täyttää aloitteen kannatusilmoitus.</p> 
    <p>Kannatusilmoitusten keräyksen aikana vastuuhenkilöt (vireillepanija / edustaja / varaedustaja) voivat halutessaan kansalaisaloite.fi:ssä esimerkiksi:</p>
    
    <ul>
        <li>päivittää tietoja kannatusilmoitusten keräystavoista</li>
        <li>päivittää tietoja muilla keräystavoilla kerättyjen kannatusilmoitusten määrästä</li>
        <li>päivittää tietoja taloudellisen tuen saamisesta</li>
        <li>lisätä aloitteeseen liittyviä linkkejä</li>
    </ul>

    <#assign href>${urls.helpIndex()}/<@u.enumDescription HelpPage.INITIATIVE />#h5</#assign>
    <#assign label><@u.message "page.help.initiative.title" /></#assign>
    <p><@u.link href=href label=label /></p>

    <p>Kansalaisaloitetta tukevat allekirjoitukset eli kannatusilmoitukset on kerättävä kuuden kuukauden kuluessa. Paperilla kerättävissä ilmoituksissa on käytettävä oikeusministeriön asetuksella (86/2012) vahvistamaa lomaketta. Kannatusilmoitusten keräämisessä voidaan käyttää useampaa menetelmää. Kannatusilmoitusten keräys verkossa edellyttää aina niin sanottua vahvaa sähköistä tunnistamista esimerkiksi pankkitunnusten tai teleoperaattoreiden mobiilivarmenteen käyttöä.</p>

    <h4>Kannatusilmoitusten tarkastus</h4>
    
    <p>Kun kannatusilmoituksia on yli 50 000, kansalaisaloitteen edustaja voi toimittaa kannatusilmoitukset Väestörekisterikeskukselle, joka tarkastaa niiden oikeellisuuden ja vahvistaa hyväksyttyjen kannatusilmoitusten määrän. Kannatusilmoitusten keräämistä voi jatkaa myös sen jälkeen, kun aloitteella on vaaditut 50 000 kannatusilmoitusta. Näin voidaan varautua muun muassa siihen, että osa ilmoituksista saatetaan hylätä tarkastuksessa. Kannatusilmoitukset tulee toimittaa Väestörekisterikeskukselle kuuden kuukauden sisällä keräyksen päättymisestä.</p>
    <p>Katso lisää <@u.link href="http://vrk.fi/default.aspx?docid=5886&site=3&id=698" label="Väestörekisterikeskuksen" rel="external" /> sivuilta.</p>
    <p>Jos kannatusilmoitusten keräämisessä on käytetty kansalaisaloite.fi -palvelua, Väestörekisterikeskus voi ladata nämä kannatusilmoitukset suoraan palvelusta käsittelyynsä. Jos kannatusilmoituksia on kerätty myös paperimuotoisella lomakkeella tai muilla verkkokeräysjärjestelmillä, Väestörekisterikeskus ilmoittaa kaikkien keräystapojen kokonaismäärän kansalaisaloite.fi-palvelussa.</p> 
    
    <h3 id="h6">Kansalaisaloitteen toimittaminen eduskuntaan</h3>
    
    <p><@u.image src="flowstate-send-to-parliament.png" alt="Kansalaisaloitteen toimittaminen eduskuntaa" /></p>

    <p>Kun Väestörekisterikeskus on tarkistanut kannatusilmoitukset ja hyväksyttyjä kannatusilmoituksia on vähintään 50 000, aloitteen edustaja voi toimittaa aloitteen eduskunnan käsiteltäväksi.</p>
    <p>Eduskunnalla on velvoite ottaa kansalaisaloite käsiteltäväksi, mutta aloitteen hyväksyminen samoin kuin mahdolliset muutokset aloitteeseen jäävät eduskunnan harkittaviksi. Jos aloite tulee eduskunnassa hylätyksi, samasta asiasta voidaan tehdä uusi aloite.</p> 
    <p>Toistaiseksi kansalaisaloitteen käsittelyä koskevat tiedot eivät välity takaisin kansalaisaloite.fi -palveluun. Kansalaisaloite.fi -palvelussa voi kuitenkin selata myös päättyneitä aloitteita.</p>
    <p>Jos aloitetta ei ole toimitettu eduskunnalle kuuden kuukauden kuluessa, se raukeaa. Jos aloite ei saa 50 000 kannatusilmoitusta, kannatusilmoitukset tulee hävittää viimeistään kuuden kuukauden kuluttua keräyksen päättymisestä.</p>
    
    <h3 id="h7">Muuta</h3>
    
    <h4 id="h7-1">Aloitteen kannatusilmoitusten keräämisen päättyminen</h4>
    
    <p>Aloitteen kannatusilmoitusten kerääminen päättyy kun tulee kuluneeksi kuusi kuukautta aloitteen päiväyksestä. Tämän jälkeen aloitteen kannattaminen palvelussa ei ole enää mahdollista.</p> 
    
    <h4 id="h7-2">Kannatusilmoitusten hävittäminen</h4>

    <p>Kannatusilmoitukset tulee hävittää seuraavasti:</p>
    <ul>
        <li>jos kannatusilmoituksia on yhteensä alle 50 000, ne tulee hävittää kuuden kuukauden kuluttua siitä kun keräys on päättynyt</li>
        <li>jos kannatusilmoituksia on vähintään 50 000, mutta niitä ei toimiteta Väestörekisterikeskukselle, ne tulee hävittää kuuden kuukauden kuluttua siitä kun keräys on päättynyt</li>
        <li>jos kannatusilmoitukset on toimitettu Väestörekisterikeskukselle, se hävittää kannatusilmoitukset</li>
    </ul>

    <p>Aloitteen vastuuhenkilö voi hävittää kansalaisaloite.fi -palveluun tallennetut kannatusilmoitukset.</p>
    
    <#assign label><@u.message "page.search" /></#assign>
    <p>Aloite jää palveluun ja sen löytää esimerkiksi “<@u.link href=urls.search() label=label />” -sivulta.</p>
    
    
        
<#else>

    <div class="view-block">
        <h3>Innehåll</h3>
    
        <ol>
            <li><a href="#h1">Sammanfattning</a></li>
            <li><a href="#h2">Medborgarinitiativet</a></li>
            <li><a href="#h3">Initiativets beredning</a></li>
            <li><a href="#h4">Anhängiggörande av ett initiativ i tjänsten medborgarinitiativ.fi</a></li>
            <li><a href="#h5">Insamling och kontroll av stödförklaringar </a></li>
            <li><a href="#h6">Överlämnande av medborgarinitiativ till riksdagen</a></li>
            <li><a href="#h7">Övrigt </a>
                <ul> 
                    <li><a href="#h7-1">Avslutande av insamlingen av stödförklaringar för initiativet</a></li>
                    <li><a href="#h7-2">Förstöring av stödförklaringar</a></li>
                </ul>
            </li>
        </ol>
    </div>

    <h3 id="h1">Sammanfattning</h3>

    <p>Medborgarinitiativet framskrider på följande sätt i tjänsten medborgarinitiativ.fi:</p>

    <div>Anhängiggörande</div>
    <ul>
        <li>beredning av initiativet</li>
        <li>anhängiggörande av initiativet i tjänsten medborgarinitiativ.fi</li>
        <li>justitieministeriets granskning</li>
    </ul>

    <div>Insamling och kontroll av stödförklaringar</div> 
    <ul>
        <li>insamling av stödförklaringar som ordnas av ansvarspersonerna under högst 6 månader</li>
        <li>Befolkningsregistercentralen kontrollerar stödförklaringarnas riktighet och antal</li>
    </ul>

    <p>Överlämnande av medborgarinitiativ till riksdagen</p>

    <p>I tjänsten medborgarinitiativ.fi syns initiativets aktuella skede tydligt vid övre kanten av respektive initiativs sida enligt följande schema.</p>
    
    <p><@u.image src="flowstate-proposal-sv.png" alt="Schema" /></p>

    <h3 id="h2">Medborgarinitiativet</h3>

    <p>Medborgarinitiativet ger medborgarna en möjlighet att få sitt initiativ behandlat av riksdagen. Det nya systemets allmänna mål är att främja och stöda fri medborgarverksamhet och på så sätt stärka medborgarsamhället, där olika befolkningsgrupper aktivt deltar i och påverkar samhällets utveckling.</p>
    <p>Tjänsten medborgarinitiativ.fi, som öppnades den 1 december 2012, erbjuder en möjlighet att skapa och administrera initiativ på webben. Initiativförfarandet i tjänsten medborgarinitiativ.fi har tre faser. Efter att initiativet väckts börjar man samla in stödförklaringar och om initiativet vid Befolkningsregistercentralens kontroll konstateras ha minst 50 000 godkända stödförklaringar kan det lämnas in för behandling av riksdagen.</p>
    
    <#assign href>${urls.helpIndex()}/<@u.enumDescription HelpPage.GENERAL /></#assign>
    <p><@u.link href=href label="Läs mer" /></p>


    <h3 id="h3">Initiativets beredning</h3>

    <p>Innan personen eller personerna väcker eller skapar ett medborgarinitiativ kan de bereda medborgarinitiativet på önskat sätt. Denna förhandsberedning sker utanför tjänsten medborgarinitiativ.fi.</p>

    <h3 id="h4">Anhängiggörande av ett initiativ i tjänsten medborgarinitiativ.fi</h3>
    
    <p>Anhängiggörande av ett initiativ innebär att initiativformuleringen och övriga uppgifter sparas i tjänsten, att övriga ansvarspersoner bjuds in och att initiativet skickas till justitieministeriet för granskning. Därefter kan insamlingen av stödförklaringar för medborgarinitiativet inledas.</p>
    <p>Ett medborgarinitiativ kan väckas av en eller flera röstberättigade finska medborgare. Den som väcker ett initiativ ska utse minst en företrädare och ersättare för att sköta praktiska ärenden i anslutning till initiativet.</p>
    <p>Ett medborgarinitiativ kan innehålla antingen ett lagförslag eller ett förslag om lagberedning. Initiativet kan även gälla ändring eller upphävande av gällande lag. Om initiativet utformas som ett lagförslag, ska det innehålla en författningstext. Initiativet ska riktas mot en helhet och ska alltid innehålla en motivering.</p>

    <p>Anhängiggörandet framskrider enligt följande:</p>

    <ol>
        <li>Identifiering i tjänsten med nätbankskoder, mobilcertifikat eller chipsförsett identitetskort (s.k. stark autentisering)</li>
        <li>Initiativets uppgifter matas in med ett webbformulär</li>
        <li>Ansvarspersoner i olika roller bjuds in (eventuella övriga initiativtagare, företrädare, ersättare)</li>
        <li>De övriga ansvarspersonerna identifierar sig och godkänner sina roller</li>
        <li>Initiativet skickas till justitieministeriet då det finns minst en ansvarsperson i varje roll (initiativtagare, företrädare och ersättare)</li>
        <li>Justitieministeriet granskar initiativet</li>
        <li>Insamlingen av stödförklaringar i tjänsten medborgarinitiativ.fi inleds då granskningen är gjord.</li>
    </ol> 

    <p>Vid justitieministeriets granskning kontrolleras att de nödvändiga uppgifterna är ifyllda och att initiativet inte innehåller sådant material som inte lämpar sig för publicering på webben. Justitieministeriets granskning tar inte i övrigt ställning till initiativets innehåll. Efter justitieministeriets granskning är det inte längre möjligt att ändra initiativets permanenta uppgifter.</p>

    <#assign href>${urls.helpIndex()}/<@u.enumDescription HelpPage.INITIATIVE /></#assign>
    <p>Närmare uppgifter i <@u.link href=href label="Bruksanvisningarna" rel="external" /><br/>
    <#assign href>${urls.helpIndex()}/<@u.enumDescription HelpPage.INITIATIVE />#h5</#assign>
    Bekanta dig också med <@u.link href=href label="ansvarspersonernas skyldigheter" /></p>


    <h3 id="h5">Insamling och kontroll av stödförklaringar</h3>
    
    <p><@u.image src="flowstate-accepted-sv.png" alt="Insamling och kontroll av stödförklaringar" /></p>

    <h4>Insamling av stödförklaringar</h4>

    <#assign href>${urls.helpIndex()}/<@u.enumDescription HelpPage.FAQ />#h5</#assign>
    <#assign label><@u.message "page.help.faq.title" /></#assign>
    <p>Då justitieministeriet har konstaterat att initiativet är publicerbart kan stödförklaringar samlas in via tjänsten medborgarinitiativ.fi. Då ett initiativ fått minst 50 stödförklaringar syns det på den sida där man kan bläddra bland initiativen på tjänsten medborgarinitiativ.fi. Före denna tidpunkt kommer man åt initiativet via länken. Före initiativet syns i sökningen kan initiativtagarna sprida initiativets länk på önskat sätt (<@u.link href=href label=label />).</p>
    <p>Att fungera som ansvarsperson för ett initiativ innebär inte automatiskt att man understöder initiativet. Således bör även ansvarspersonerna fylla i en stödförklaring för initiativet.</p> 
    <p>I tjänsten medborgarinitiativ.fi har ansvarspersonerna (initiativtagare/företrädare/ersättare) under insamlingen av stödförklaringar möjlighet att exempelvis:</p>
    
    <ul>
        <li>uppdatera uppgifterna om stödförklaringarnas insamlingskanaler</li>
        <li>uppdatera uppgifterna om antalet stödförklaringar som samlats in via andra kanaler</li>
        <li>uppdatera uppgifterna om mottagande av ekonomiskt bidrag</li>
        <li>lägga till länkar som anknyter till initiativet</li>
    </ul>

    <#assign href>${urls.helpIndex()}/<@u.enumDescription HelpPage.INITIATIVE />#h5</#assign>
    <p><@u.link href=href label="Bruksanvisningarna" /></p>

    <p>Underteckningarna som stöder medborgarinitiativet, dvs. stödförklaringarna, måste samlas in inom sex månader. För förklaringar som samlas in i pappersform ska en blankett (länk?) enligt justitieministeriets förordning (86/2012) användas. Vid insamlingen av stödförklaringar kan flera metoder användas. Insamling av stödförklaringar på webben kräver alltid en så kallad stark autentisering exempelvis med nätbankskoder eller teleoperatörernas mobilcertifikat.</p>

    <h4>Kontroll av stödförklaringar</h4>
    
    <p>Då antalet stödförklaringar överskrider 50 000 kan företrädaren för medborgarinitiativet lämna in stödförklaringarna till Befolkningsregistercentralen, som kontrollerar deras riktighet och bekräftar antalet godkända stödförklaringar. Insamlingen av stödförklaringar kan fortsätta även efter att initiativet har uppnått de 50 000 stödförklaringar som krävs. På så sätt är det möjligt att bereda sig på bland annat att en del av förklaringarna kan förkastas i granskningen. Stödförklaringarna ska lämnas till Befolkningsregistercentralen inom sex månader efter insamlingens utgång.</p>
    <p>Ytterligare information på Befolkningsregistercentralens <@u.link href="http://vrk.fi/default.aspx?docid=5886&site=3&id=698" label="Befolkningsregistercentralens" rel="external" /> webbplats.</p>
    <p>Om tjänsten medborgarinitiativ.fi använts vid insamlingen av stödförklaringar kan Befolkningsregistercentralen ladda ner dessa stödförklaringar direkt från tjänsten. Om stödförklaringar samlats in även via blanketten i pappersform eller övriga webbinsamlingssystem meddelar Befolkningsregistercentralen det totala antalet stödförklaringar för alla insamlingssätt i tjänsten medborgarinitiativ.fi.</p> 
    
    <h3 id="h6">Överlämnande till riksdagen</h3>
    
    <p><@u.image src="flowstate-send-to-parliament-sv.png" alt="Överlämnande till riksdagen" /></p>

    <p>Då Befolkningsregistercentralen har kontrollerat stödförklaringarna och antalet godkända stödförklaringar är minst 50 000 kan initiativets företrädare lämna in initiativet för behandling av riksdagen.</p>
    <p>Riksdagen har en skyldighet att behandla medborgarinitiativ, men initiativets godkännande liksom eventuella ändringar övervägs av riksdagen. Om initiativet förkastas av riksdagen kan ett nytt initiativ göras om samma ärende.</p> 
    <p>Tills vidare överförs inte uppgifterna som berör behandlingen av ett initiativ till tjänsten medborgarinitiativ.fi. I tjänsten medborgarinitiativ.fi kan du dock bläddra även bland initiativ vars behandling slutförts.</p>
    <p>Initiativet förfaller om det inte lämnats in till riksdagen inom sex månader. Om initiativet inte får 50 000 stödförklaringar måste stödförklaringarna förstöras senast inom sex månader efter avslutad insamling.</p>
    
    <h3 id="h7">Övrigt</h3>
    
    <h4 id="h7-1">Avslutande av insamlingen av stödförklaringar för initiativet</h4>
    
    <p>Insamlingen av stödförklaringar för initiativet avslutas efter sex månader från initiativets datum. Därefter är det inte längre möjligt att understöda initiativet i tjänsten.</p> 
    
    <h4 id="h7-2">Förstöring av stödförklaringar</h4>

    <p>Stödförklaringarna ska förstöras enligt följande:</p>
    <ul>
        <li>om det totala antalet stödförklaringar är under 50 000 ska de förstöras inom sex månader från det att insamlingen har avslutats</li>
        <li>om det totala antalet stödförklaringar är minst 50 000, men inte lämnas till Befolkningsregistercentralen, ska de förstöras inom sex månader från det att insamlingen har avslutats</li>
        <li>Befolkningsregistercentralen förstör de stödförklaringar som lämnats in</li>
    </ul>

    <p>Initiativets ansvarsperson kan förstöra stödförklaringar som sparats i tjänsten medborgarinitiativ.fi.</p>
    
    <#assign label><@u.message "page.search" /></#assign>
    <p>Initiativet sparas i tjänsten och går att hitta exempelvis på sidan ”<@u.link href=urls.search() label="Bläddra bland medborgarinitiativen" />”.</p>

</#if>

