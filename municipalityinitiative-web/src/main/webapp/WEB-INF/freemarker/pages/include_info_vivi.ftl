<#import "../components/utils.ftl" as u />

<#if locale == "fi">
    
    <p>Kansalaisaloite.fi -palvelu on Viestintäviraston hyväksymä. Viestintäviraston hyväksyntä on voimassa 30.11.2013 asti.</p>

    <p>Viestintäviraston tehtävänä on vahvistaa, että verkkokeräyksessä käytettävä tietojärjestelmä täyttää kansalaisaloitelaissa asetetut vaatimukset.</p>
    
    <p>Viestintävirasto tarkastaa että:</p>
    <ol>
        <li>kannatusilmoitusten keräyksessä käytetään vahvasta sähköisestä tunnistamisesta ja sähköisistä allekirjoituksista annetussa laissa (617/2009) tarkoitettua vahvaa sähköistä tunnistamista.</li>
        <li>järjestelmän sisältämien tietojen luvaton muuttaminen ja muu luvaton tai asiaton käsittely estetään. Käytännössä tämä tehdään mm. käyttöoikeushallinnan, käytön valvonnan sekä tietoverkkojen, tietojärjestelmien ja tietopalvelujen asianmukaisilla ja riittävillä turvallisuusjärjestelyillä ja muilla toimenpiteillä.</li>
        <li>järjestelmä noudattaa Väestörekisterikeskuksen määräyksiä tietojärjestelmässä kerättyjen tietojen vakiomuodosta, jossa ne ovat sähköisesti yhdistettävissä väestötietojärjestelmän tietoihin kannatusilmoitusten tarkastusta varten.</li>
    </ol>
    
    <#assign href>${urls.infoIndex()}/<@u.enumDescription InfoPage.FEEDBACK /></#assign>
    <p>Kysyttävää? <@u.link href=href label="Ota yhteyttä" /></p> 
    

<#else>

    <p>Tjänsten medborgarinitiativ.fi är godkänd av Kommunikationsverket. Kommunikationsverkets godkännande är i kraft fram till 30.11.2013.</p>

    <p>Kommunikationsverkets uppgift är att säkerställa att det informationssystem som används vid webbinsamlingen uppfyller de krav som ställs i lagen om medborgarinitiativ.</p>
    
    <p>Kommunikationsverket kontrollerar att:</p>
    <ol>
        <li>insamlingen av stödförklaringar sker genom utnyttjande av sådan stark autentisering som avses i lagen om stark autentisering och elektroniska signaturer (617/2009).</li>
        <li>olovlig ändring av uppgifterna i systemet och annan olovlig eller obefogad hantering förhindras. I praktiken sköts detta bl.a. genom ändamålsenliga och tillräckliga säkerhetsarrangemang som rör förvaltningen av användarrättigheter och övervakningen av användningen samt datanäten, informationssystemen och informationstjänsterna och genom andra åtgärder.</li>
        <li>systemet följer Befolkningsregistercentralens föreskrifter om vilken standardform de uppgifter som samlas in via ett datasystem ska ha för att elektroniskt kunna samköras med uppgifter i befolkningsdatasystemet för kontroll av stödförklaringar.</li>
    </ol>
    
    <#assign href>${urls.infoIndex()}/<@u.enumDescription InfoPage.FEEDBACK /></#assign>
    <p>Frågor? <@u.link href=href label="Ta kontakt" /></p> 

</#if>