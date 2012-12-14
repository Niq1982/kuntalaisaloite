<#import "../components/utils.ftl" as u />

<#if locale == "fi">
    
    <h3 id="h1">Henkilötietojen suojasta</h3> 
    
    <p>Kun käyttäjä tunnistautuu ja täyttää kannatusilmoituksen sähköisesti, järjestelmä tallentaa allekirjoittajasta seuraavat kansalaisaloitelain vaatimat tiedot:</p>
    
    <ul>
        <li>Koko nimi</li>
        <li>Syntymäaika</li>
        <li>Kotikunta</li>
        <li>Vakuutus siitä, että henkilö on äänioikeutettu Suomen kansalainen ja kannattanut kyseistä aloitetta vain kerran</li>
    </ul>
    
    <p>Järjestelmä ei tallenna kannatusilmoituksen allekirjoittajan henkilötunnusta. Kannatusilmoituksille lasketaan henkilötunnusta hyödyksi käyttäen aloitekohtainen yksisuuntainen tiiviste, jolla turvallisesti estetään saman aloitteen kannattaminen moneen kertaan. Tämän tiedon perusteella ei voida selvittää, mitä eri aloitteita yksittäinen henkilö on kannattanut. Kannatusilmoitusten tiedot säilytetään tietokannassa vahvasti salattuna. Järjestelmän ylläpitäjien toimenpiteet tallennetaan lokiin.</p>
    
    <h3 id="h2">Kannatusilmoitusten julkisuudesta</h3>
    
    <p>Kannatusilmoitusten allekirjoittajien nimet eivät ole julkisia vielä silloin, kun kannatusilmoituksia kerätään.</p> 
    
    <p>Kansalaisaloitteen kannatusilmoitukset ovat julkisia sen jälkeen, kun viranomainen toteaa, että perustuslaissa säädetty kannatusilmoitusten vähimmäismäärä on täyttynyt eli kun aloitteella on vähintään 50 000 äänioikeutettua Suomen kansalaista allekirjoittajina. Allekirjoittajien nimet tulevat siis julkisiksi vasta, kun Väestörekisterikeskus vahvistaa, että aloitteella on eduskuntakäsittelyn edellyttämä määrä kannattajia. Tällöinkään tiedot eivät tule näkyviin kansalaisaloite.fi –palveluun. Vireillepanija tai tämän edustaja saavat lain mukaan luovuttaa kerätyt tiedot vain Väestörekisterikeskukselle.</p>
    
    <#assign href>${urls.infoIndex()}/<@u.enumDescription InfoPage.PERSON_REGISTER /></#assign>
    <p>Lue lisää <@u.link href=href label="henkilörekisteriselosteet" /> -sivulta.</p>
    
    <p>Lue lisää <@u.link href="http://www.finlex.fi/fi/laki/alkup/2012/20120012" label="kansalaisaloitelaista" rel="external" />.</p> 
    
    <#assign href>${urls.infoIndex()}/<@u.enumDescription InfoPage.FEEDBACK /></#assign>
    <p>Jos sinulla on kysyttävää henkilötietojen suojasta palvelussa, ota yhteyttä <@u.scrambleEmail "kansalaisaloite.om@om.fi" /> tai <@u.link href=href label="anna palautetta" />.</p>
    
    <h3 id="h3">Tietoturvasta</h3>
    
    <#assign href>${urls.infoIndex()}/<@u.enumDescription InfoPage.VIVI /></#assign>
    <p>Kansalaisaloite.fi –verkkopalvelu on testattu ja auditoitu huolellisesti ja on siten turvallinen käyttää. Viestintävirasto on auditoinut järjestelmän kansalaisaloitelain edellyttämällä tavalla (<@u.link href=href label="Viestintäviraston hyväksyntä" />).</p>

   
<#else>

    <h3 id="h1">Om skyddet av personuppgifter</h3> 
    
    <p>När användaren identifierar sig och fyller i den elektroniska stödförklaringen sparar datasystemet följande uppgifter om undertecknaren enligt lagen om medborgarinitiativ:</p>
    
    <ul>
        <li>Fullständigt namn</li>
        <li>Födelsedatum</li>
        <li>Hemkommun</li>
        <li>En försäkran om att personen är röstberättigad finsk medborgare och har understött ifrågavarande initiativ endast en gång</li>
    </ul>
    
    <p>Datasystemet sparar inte initiativundertecknarens personbeteckning. Utgående från personbeteckningen uträknas ett initiativspecifikt envägshashvärde för stödförklaringarna. På så sätt säkerställer man att en person kan understöda samma initiativ endast en gång. På basis av denna information är det inte möjligt att ta reda på vilka initiativ en enskild person har understött. Stödförklaringarnas uppgifter sparas i en databas med stark kryptering. Systemadministratörernas verksamhet sparas i en logg.</p>
    
    <h3 id="h2">Om stödförklaringarnas offentlighet</h3>
    
    <p>Initiativundertecknarnas namn är inte offentliga medan stödförklaringarna samlas in.</p> 
    
    <p>Medborgarinitiativets stödförklaringar är offentliga när myndigheterna konstaterar att kravet på minsta antal stödförklaringar enligt grundlagen har uppfyllts, dvs. när minst 50 000 röstberättigade finska medborgare har undertecknat initiativet.  Med andra ord offentliggörs undertecknarnas namn först när Befolkningsregistercentralen bekräftar att initiativet har tillräckligt med underskrifter för behandling i riksdagen. Inte ens då syns uppgifterna i tjänsten medborgarinitiativ.fi. Den som väcker ett initiativ eller dennes företrädare får enligt lag överlåta de insamlade uppgifterna enbart till Befolkningsregistercentralen.</p>
    
    <#assign href>${urls.infoIndex()}/<@u.enumDescription InfoPage.PERSON_REGISTER /></#assign>
    <p>Läs mera på sidan om <@u.link href=href label="registerbeskrivningar" />.</p>
    
    <p>Läs mer om <@u.link href="http://www.finlex.fi/sv/laki/alkup/2012/20120012" label="lagen om medborgarinitiativ" rel="external" />.</p> 
    
    <#assign href>${urls.infoIndex()}/<@u.enumDescription InfoPage.FEEDBACK /></#assign>
    <p>Om du har frågor om tjänstens skydd av personuppgifter, ta kontakt via <@u.scrambleEmail "kansalaisaloite.om@om.fi" /> eller <@u.link href=href label="ge respons" /></p> 
    
    <h3 id="h3">Om dataskyddet</h3>
    
    <#assign href>${urls.infoIndex()}/<@u.enumDescription InfoPage.VIVI /></#assign>
    <p>Webbtjänsten Medborgarinitiativ.fi har testats och kontrollerats noga och är således säker att använda. Kommunikationsverket har kontrollerat datasystemet enligt vad som föreskrivs i lagen om medborgarinitiativ (<@u.link href=href label="Kommunikationsverkets godkännande" />).</p>

</#if>