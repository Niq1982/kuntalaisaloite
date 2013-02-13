<#import "../components/layout.ftl" as l />
<#import "../components/utils.ftl" as u />

<#escape x as x?html> 
<@l.main "page.news">
 
    <h1><@u.message "news.title" /></h1>
 
 <#if locale == "fi">
 
    <div id="wysihtml5-editor-toolbar">
      <header>
        <ul class="commands">
          <li data-wysihtml5-command="bold" title="Make text bold (CTRL + B)" class="command"></li>
          <li data-wysihtml5-command="italic" title="Make text italic (CTRL + I)" class="command"></li>
          <li data-wysihtml5-command="insertUnorderedList" title="Insert an unordered list" class="command"></li>
          <li data-wysihtml5-command="insertOrderedList" title="Insert an ordered list" class="command"></li>
          <li data-wysihtml5-command="createLink" title="Insert a link" class="command"></li>
          <li data-wysihtml5-command="insertImage" title="Insert an image" class="command"></li>
          <li data-wysihtml5-command="formatBlock" data-wysihtml5-command-value="h1" title="Insert headline 1" class="command"></li>
          <li data-wysihtml5-command="formatBlock" data-wysihtml5-command-value="h2" title="Insert headline 2" class="command"></li>
          <#--<li data-wysihtml5-command-group="foreColor" class="fore-color" title="Color the selected text" class="command">
            <ul>
              <li data-wysihtml5-command="foreColor" data-wysihtml5-command-value="silver"></li>
              <li data-wysihtml5-command="foreColor" data-wysihtml5-command-value="gray"></li>
              <li data-wysihtml5-command="foreColor" data-wysihtml5-command-value="maroon"></li>
              <li data-wysihtml5-command="foreColor" data-wysihtml5-command-value="red"></li>
              <li data-wysihtml5-command="foreColor" data-wysihtml5-command-value="purple"></li>
              <li data-wysihtml5-command="foreColor" data-wysihtml5-command-value="green"></li>
              <li data-wysihtml5-command="foreColor" data-wysihtml5-command-value="olive"></li>
              <li data-wysihtml5-command="foreColor" data-wysihtml5-command-value="navy"></li>
              <li data-wysihtml5-command="foreColor" data-wysihtml5-command-value="blue"></li>
            </ul>
          </li>
          <li data-wysihtml5-command="insertSpeech" title="Insert speech" class="command"></li>-->
          <li data-wysihtml5-action="change_view" title="Show HTML" class="action"></li>
        </ul>
      </header>
      <div data-wysihtml5-dialog="createLink" style="display: none;">
        <label>
          Link:
          <input data-wysihtml5-dialog-field="href" value="http://">
        </label>
        <a data-wysihtml5-dialog-action="save">OK</a>&nbsp;<a data-wysihtml5-dialog-action="cancel">Cancel</a>
      </div>

      <div data-wysihtml5-dialog="insertImage" style="display: none;">
        <label>
          Image:
          <input data-wysihtml5-dialog-field="src" value="http://">
        </label>
        <a data-wysihtml5-dialog-action="save">OK</a>&nbsp;<a data-wysihtml5-dialog-action="cancel">Cancel</a>
      </div>
    </div>
 
 
    <form><textarea id="wysihtml5-textarea"  spellcheck="false" wrap="off" autofocus style="height:100%;" placeholder="Enter your text ..." autofocus>

    <p>Ensimmäiset kansalaisaloitteet on nyt tarkastettu oikeusministeriössä. Ajantasaisia uutisia saat parhaiten seuraamalla "Kansalaisaloite - Medborgarinitativ" -ryhmää Facebookissa tai Otakantaa-blogia.</p> 


    <p>Danske Bank on on saanut tunnistautumista koskevan ongelman korjattua.<br/>
    
    <p>Valitettavasti Danske Bank –tunnistautumisessa on havaittu 1.12.2012 klo 11.30 ongelmia. Ongelma vaikuttaisi olevan kansalaisaloite.fi-palvelun ulkopuolella ja tilannetta seurataan.</p>

    <p>Seuraa myös "Kansalaisaloite - Medborgarinitativ" -ryhmää Facebookissa tai Otakantaa-blogia.</p>

    <p>Palvelu julkaistiin klo 11:40.</p>
    
    <h3>OIKEUSMINISTERIÖ TIEDOTTAA</h3>
    
    <p>Kansalaisaloite-verkkopalvelu avataan 1. joulukuuta</p>
    
    <p>Oikeusministeriön verkkopalvelu kansalaisaloitteiden tekemiseksi ja aloitteiden kannatusilmoitusten keräämiseksi avataan käyttöön lauantaina 1. joulukuuta (iltapäivällä). Palvelu toimii osoitteessa <@u.link href="https://www.kansalaisaloite.fi" label="www.kansalaisaloite.fi" />. Se tarjoaa kansalaisille mahdollisuuden saada aloitteensa eduskunnan käsiteltäväksi.</p> 
    
    <p>Uusi verkkopalvelu on maksuton, esteetön ja turvallinen käyttää. Palvelu toimii sekä suomeksi että ruotsiksi. Kannatusilmoituksia voi kerätä myös sellaisiin aloitteisiin, joiden keräys on aloitettu jo muissa verkkopalveluissa tai paperilomakkeella. Jatkossa palveluun tulee mahdollisuus myös kuntalaisaloitteen tekemiselle.</p>
    
    <p>Oikeusministeriö tarkastaa, että kansalaisten jättämissä aloitteissa on tarvittavat tiedot ja ettei aloite sisällä verkossa julkaistavaksi sopimatonta materiaalia. Tämän jälkeen aloitteelle voi kerätä kannatusilmoituksia. Aloitteita tarkastetaan oikeusministeriössä arkisin virka-aikana ensi maanantaista alkaen. Vielä avausviikonloppuna aloitteita ei siis voi kannattaa verkkopalvelussa. Kansalaisaloitteiden tekemistä koskeviin kysymyksiin vastataan sähköpostitse ja sosiaalisessa mediassa (Facebook).</p> 
    
    <p>Kansalaisaloitepalvelun avaaminen ajoittuu joulukuun alkuun, koska oikeusministeriön kansalaisaloite-verkkopalvelua koskevat säännökset tulevat silloin voimaan. Palvelua on kehitetty avoimesti ja hyvässä yhteistyössä järjestöjen kanssa. Järjestelmän toimittaja on it-palveluyritys Solita.</p>
    
    <p>Aloitepalvelu toimii myös osana otakantaa.fi -osallistumisympäristöä, joka toteuttaa ja kokoaa yhteen useita kansalaisvaikuttamiseen liittyviä verkkopalveluita. Osallistumisympäristöhanke kuuluu valtiovarainministeriön koordinoimaan Sähköisen asioinnin ja demokratian vauhdittamisohjelmaan (SADe-ohjelma).</p>
    
    <h3>Lakialoite tai ehdotus lainvalmisteluun ryhtymisestä</h3>
    
    <p>Vähintään 50 000 äänioikeutetulla Suomen kansalaisella on oikeus tehdä eduskunnalle aloite lain säätämiseksi. Kansalaisaloite voi sisältää joko lakiehdotuksen tai ehdotuksen lainvalmisteluun ryhtymisestä. Aloite voi koskea myös voimassa olevan lain muuttamista tai kumoamista.</p> 
    
    <p>Kansalaisaloitetta tukevat allekirjoitukset eli kannatusilmoitukset on kerättävä kuuden kuukauden kuluessa. Ne kerätään sähköisesti tietoverkossa tai paperilla. Paperilla kerättävissä ilmoituksissa on käytettävä oikeusministeriön vahvistamaa lomakepohjaa.</p>
    
    <p>Verkossa tehtävä aloite ja sen kannatusilmoitusten keräys edellyttävät aina niin sanottua vahvaa sähköistä tunnistamista, esimerkiksi pankkitunnusten tai teleoperaattoreiden mobiilivarmenteen käyttöä.</p>
    
    <p>Lisätietoja:</p>
    <ul> 
        <li>projektipäällikkö Teemu Ropponen, puh. 050 520 9340, </li>
        <li>Viestintävirasto: apulaispäällikkö Aki Tauriainen, puh. 09 696 6624 tai 040 521 1542, </li>  
        <li>Väestörekisterikeskus: tietopalvelupäällikkö Timo Salovaara,  puh. 09 2291 6551 tai 050 344 3111, </li>
    </ul> 

    </textarea></form>
    
<#else>

    <span class="news-date">3.12.2012</span> 

    <p>De första medborgarinitiativen har nu granskats på justitieministeriet. Den mest aktuella informationen hittar du i vår Facebook-grupp och i vår utvecklarblogg.</p> 
    
    <p><@u.link href="https://www.facebook.com/aloite" label="https://www.facebook.com/aloite" rel="external" /></p>

    <p><@u.link href="http://blogi.otakantaa.fi/sv" label="http://blogi.otakantaa.fi/sv" rel="external" /></p>
    
    <span class="news-date">3.12.2012</span> 

    <p>Danske Bank har åtgärdat problemet i sin nätbank - identifieringen fungerar igen.<br/>
    <@u.link href="http://yle.fi/uutiset/danske_bankin_verkkomaksupalveluvika_korjattu/6399294" label="Lue lisää" rel="external" /></p>  

    <span class="news-date">1.12.2012</span>

    <p>Beklagligen har ett fel i Danske Banks identifieringstjänst upptäckts den 1.12.2012 klockan 11.30. Felet verkar inte vara orsakad av kansalaisaloite.fi-tjänsten, men vi följer upp situationen.</p>

    <p>Följ också:</p>
    
    <p><@u.link href="https://www.facebook.com/aloite" label="https://www.facebook.com/aloite" rel="external" /></p>

    <p><@u.link href="http://blogi.otakantaa.fi" label="http://blogi.otakantaa.fi" rel="external" /></p>

    <span class="news-date">1.12.2012</span>

    <p>Tjänsten publicerades kl. 11:40.</p>
    
    <span class="news-date">30.11.2012</span>

    <h3>JUSTITIEMINISTERIET INFORMERAR</h3>
    
    <p>Nättjänsten för medborgarinitiativ öppnas den 1 december</p>
    
    <p>Justitieministeriets nättjänst för framläggande av medborgarinitiativ och insamling av stödförklaringar öppnas lördagen den 1 december (på eftermiddagen). Tjänsten finns på adressen <@u.link href="https://www.medborgarinitiativ.fi" label="www.medborgarinitiativ.fi" />. Nättjänsten ger medborgarna möjlighet att få upp ärenden till behandling i riksdagen.</p> 
    
    <p>Den nya nättjänsten är avgiftsfri, tillgänglig och trygg, och den kan användas både på finska och på svenska. Stödförklaringar kan samlas in även för sådana initiativ vars insamling redan har börjat i andra webbtjänster eller på pappersblankett. I fortsättningen kan också invånarinitiativ läggas fram via tjänsten.</p>
    
    <p>Justitieministeriet kontrollerar att medborgarinitiativen innehåller alla uppgifter som krävs och att materialet är sådant som kan publiceras på internet. Därefter kan insamlingen av stödförklaringar börja för initiativet. Initiativ kontrolleras vid justitieministeriet vardagar under tjänstetid från och med nästa måndag. Därför är det inte möjligt att stödja initiativ i nättjänsten under öppningsveckoslutet. Frågor som gäller framläggande av medborgarinitiativ besvaras per e-post och i sociala medier (på Facebook).</p> 
    
    <p>Nättjänsten för medborgarinitiativ öppnas i början av december då bestämmelserna om justitieministeriets nättjänst för medborgarinitiativ träder i kraft. Tjänsten har utvecklats på ett öppet sätt och i ett gott samarbete med organisationer. Systemleverantören är it-serviceföretaget Solita.</p>
    
    <p>Initiativtjänsten fungerar även som en del av dinasikt.fi-plattformen som producerar och samlar ihop flera webbtjänster som hänför sig till medborgarinflytandet. Projektet Plattform för delaktighet hör till finansministeriets program för påskyndande av elektronisk kommunikation och ärendehantering (SADe-programmet).</p>
    
    <h3>Lagförslag eller förslag om att lagberedning ska inledas</h3>
    
    <p>Minst femtiotusen röstberättigade finska medborgare har rätt att lägga fram initiativ för riksdagen om att en lag ska stiftas. Ett medborgarinitiativ kan innehålla antingen ett lagförslag eller ett förslag om att lagberedning ska inledas. Ett initiativ kan också gälla en ändring eller upphävande av en gällande lag.</p> 
    
    <p>Underskrifter som stöder ett medborgarinitiativ, dvs. stödförklaringar, ska samlas in inom sex månader. De samlas antingen elektroniskt i ett datanät eller på papper. För insamlingen av stödförklaringar i pappersform ska den av justitieministeriet fastställda blanketten användas.</p>
    
    <p>För framläggande av initiativ och insamling av stödförklaringar via nätet förutsätts alltid en s.k. stark autentisering, till exempel med nätbankskoder eller teleoperatörernas mobilcertifikat.</p> 
    
    <p>Ytterligare upplysningar:</p> 
    
    <ul>
        <li>projektchef Teemu Ropponen, tfn 050 520 9340, <@u.scrambleEmail "förnamn.efternamn@om.fi" /></li>
        <li>Kommunikationsverket: biträdande chef Aki Tauriainen, tfn 09 696 6624 eller 040 521 1542, <@u.scrambleEmail "förnamn.efternamn@ficora.fi" /></li>
        <li>Befolkningsregistercentralen: chef för informationstjänster Timo Salovaara, tfn 09 2291 6551 eller 050 344 3111, <@u.scrambleEmail "förnamn.efternamn@vrk.fi" /></li>
    </ul>

    <p><@u.link href="https://www.medborgarinitiativ.fi" label="https://www.medborgarinitiativ.fi" /></p>    
    
    <p><@u.link href="https://www.facebook.com/aloite" label="https://www.facebook.com/aloite" rel="external" /></p> 

</#if>
    
</@l.main>
</#escape> 

