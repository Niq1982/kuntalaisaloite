<#import "../components/utils.ftl" as u />

<#escape x as x?html>

<#--<#if locale == "fi">-->

    <#-- Väliaikainen sivu Kuntalaisaloitteen Leijukkeen demoamiselle -->
    
    <p>Leijukkeen avulla voidaan näyttää Kuntalaisaloite.fi-palvelun sisältöä toisen palvelun sivustolla. Leijuke näyttää uusimmat aloitteet Kuntalaisaloite.fi-palvelussa sekä toiminnot "Selaa kuntalaisaloitteista" sekä "Tee kuntalaisaloite". Toiminnot siirtyvät automaattisesti leijukkeessa määritetyn kunnan vastaaviin toimintoihin Kuntalaisaloite.fi-palveluun.</p>
    <p>Leijukkeen kaikki linkit aukeavat selaimessa uuteen välilehteen ja käyttäjä ohjataan Kuntalaisaloite.fi-palvelun puolelle.</p>
    <p>Leijukkeen sisältö päivittyy Kuntalaisaloite.fi-palvelun sisällön mukaan automaattisesti muutaman minuutin viiveellä.</p>

    <h3>Leijukkeen määrittäminen</h3>
    
    <p>Leijukketta voidaan säätää haluamaksi parametrien avulla. Parametreilla voi säätää</p>
    <ul>
        <li>minkä kunnan aloitteita leijukkeessa listataan</li>
        <li>kuinka monta uusinta aloitetta listassa näytetään</li>
        <li>leijukkeen leveyttä ja korkeutta</li>
    </ul>

    <h2>Esimerkkejä leijukkeista</h2>

    <div class="view-block">
    
        <h2>Pieni leijuke - 250x400 px</h2> 
        <@u.initiativeIframe id="kuntalaisaloite-leijuke" embed=true width="250" height="400" municipality="2" limit="3" />
        <br/><br/>
        <p>Alla on esimerkki-leijukkeen upotus-koodi. Kopioi alla oleva koodi kokonaisuudessaan leikepöydällesi ja muuta parametreja tarpeesi mukaan. Liitä sen jälkeen koodi sivustollesi.</p>
        
        <@u.initiativeIframe id="kuntalaisaloite-leijuke" embed=false width="250" height="400" municipality="2" limit="3" />
    </div>
    
    <div class="view-block">
        <h2>Iso leijuke - 600x600px</h2>
    
        <@u.initiativeIframe id="kuntalaisaloite-leijuke-2" embed=true width="600" height="460" municipality="2" limit="10" />
        <br/><br/>
        <p>Alla on esimerkki-leijukkeen upotus-koodi. Kopioi alla oleva koodi kokonaisuudessaan leikepöydällesi ja muuta parametreja tarpeesi mukaan. Liitä sen jälkeen koodi sivustollesi.</p>
        
        <@u.initiativeIframe id="kuntalaisaloite-leijuke-2" embed=false width="600" height="460" municipality="2" limit="10" />
    </div>

<#--
<#else>

    <p>Medborgarna kan från och med den 1 mars 2012 använda sig av ett nytt påverkningsmedel på statlig nivå, medborgarinitiativ. Medborgarinitiativet ger medborgarna möjlighet att få upp ärenden till behandling i riksdagen. Målet med det nya systemet är att främja fri medborgarverksamhet och därigenom stärka medborgarsamhället, där olika befolkningsgrupper aktivt deltar i och påverkar utvecklandet av samhället.</p>    
    <p>Enligt en ny bestämmelse i grundlagen, som trädde i kraft vid ingången av mars, ska minst 50 000 röstberättigade finska medborgare ha rätt att lägga fram initiativ för riksdagen om att en lag ska stiftas.</p>
    
    <h3>Ett lagförslag eller ett förslag om att lagberedning ska inledas</h3>
    
    <p>I lagen om medborgarinitiativ föreskrivs om förfarandet för att lägga fram ett medborgarinitiativ.</p>
    <p>Länk till lagtexten: <@u.link href="http://www.finlex.fi/sv/laki/alkup/2012/20120012" label="http://www.finlex.fi/sv/laki/alkup/2012/20120012" rel="external" /></p>
    <p>Ett medborgarinitiativ får väckas av en eller flera röstberättigade finska medborgare. Den som väcker ett initiativ ska utse minst en företrädare och en ersättare som ska sköta de praktiska åtgärder som hänför sig till initiativförfarandet.</p>    
    <p>Ett medborgarinitiativ kan innehålla antingen ett lagförslag eller ett förslag om att lagberedning ska inledas. Ett initiativ kan också gälla ändring eller upphävande av en gällande lag. Om initiativet utformas som ett lagförslag, ska det innehålla själva lagtexten. Ett initiativ ska gälla endast en ärendehelhet och det ska alltid innehålla motiveringar till förslaget.</p>
    
    <h3>Stödförklaringar ska samlas in inom sex månader</h3>
    
    <p>Underskrifter som stöder ett medborgarinitiativ, dvs. stödförklaringar, ska samlas in inom sex månader. De ska samlas in på papper eller på elektronisk väg i ett datanät. För insamlingen av stödförklaringar i pappersform ska den av justitieministeriet förordning (86/2012) fastställda blanketten användas.</p>    
    <p>Vid insamling på nätet är det möjligt att utnyttja antingen ett datasystem som tillhandahålls av den som väckt initiativet eller en avgiftsfri nättjänst som upprätthålls av justitieministeriet och som tas i bruk i slutet av år 2012. När man använder ett datasystem som tillhandahålls av den som väckt initiativet, ska systemet godkännas av Kommunikationsverket. Väckande av ett initiativ och insamling av stödförklaringar på nätet förutsätter alltid s.k. stark autentisering, till exempel användning av bankkoder eller teleoperatörernas system för mobil identifiering.</p>
    
    <p><span class="file-icon pdf"></span><a href="${urls.baseUrl}/files/Stodforklaring_sv.pdf?version=${resourcesVersion}" target="_blank" title="">Stödförklaring</a> (PDF)</p>
    <br/>
    
    <h3>Behandling av initiativet efter att insamlingen har slutförts</h3>
    
    <p>Den som väckt initiativet ger in stödförklaringarna till Befolkningsregistercentralen, som kontrollerar deras riktighet och fastställer antalet godkända stödförklaringar. Om antalet godkända stödförklaringar uppgår till minst 50 000, kan den som väckt initiativet lämna in initiativet till riksdagen för behandling. Om initiativet inte har överlämnats till riksdagen senast inom sex månader, förfaller det.</p>
    <p>Riksdagen är skyldig att ta upp ett medborgarinitiativ till behandling, men bedömer sedan om initiativet ska godkännas eller om eventuella ändringar ska göras. Om riksdagen förkastar initiativet, kan ett nytt medborgarinitiativ om samma ärende läggas fram.</p>
    
    <h3>Offentliggörande av ekonomiskt stöd och skydd för personuppgifter</h3>
    
    <p>Bidragsbelopp som fåtts för framläggande av ett initiativ och uppgift om givarens namn ska offentliggöras, om värdet av det bidrag som en och samma givare har gett är minst 1 500 euro.</p>    
    <p>Då stödförklaringar samlas in och det inte ännu är säkert om ärendet ska tas upp till behandling i riksdagen, får undertecknarens namn och övriga personuppgifter inte finnas tillgängliga för allmänheten eller andra undertecknare. Även när stödförklaringar samlas in via den av justitieministeriet upprätthållna nättjänsten är stödförklaringarna sekretessbelagda. Undertecknarnas namn offentliggörs först efter att Befolkningsregistercentralen har kontrollerat att antalet stödförklaringar uppgår till det krävda minimiantalet, 50 000.</p>
    
    <p>Ytterligare upplysningar:<br/>per e-post: <@u.scrambleEmail "kansalaisaloite.om@om.fi" /></p>
    
    <div>Dessutom vid behov</div>
    <ul>
        <li>projektchef Teemu Ropponen, tfn 050 520 9340, <@u.scrambleEmail "förnamn.efternamn@om.fi" /></li>
        <li>konsultativ tjänsteman Kaisa Tiusanen, tfn 02951 50454, <@u.scrambleEmail "förnamn.efternamn@om.fi" />.</li>
    </ul>
    
</#if>
-->

</#escape>