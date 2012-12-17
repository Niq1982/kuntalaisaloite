<#import "../components/utils.ftl" as u />

<#if locale == "fi">
    
    <p>1.3.2012 alkaen Suomessa on käytössä uusi valtiollisen tason vaikuttamiskeino, kansalaisaloite. Se antaa kansalaisille mahdollisuuden saada aloitteensa eduskunnan käsiteltäväksi. Eduskunta käsittelee aloitteen, joka saa taakseen Vähintään 50 000 äänioikeutettua kannattajaa kuudessa kuukaudessa.</p>
    
    <p>Kansalaisaloite.fi -verkkopalvelussa voit tehdä, kannattaa ja seurata muiden tekemiä kansalaisaloitteita. Palvelun käyttö on turvallista ja maksutonta. Voit käyttää kansalaisaloite.fi -palvelua myös ruotsiksi.</p>
    
    <p>Velvoite palvelun tuottamiseen perustuu kansalaisaloitelakiin. Palvelua ylläpitää oikeusministeriö (Demokratia-, kieli- ja perusoikeusasioiden yksikkö).</p>
    
    <p>Palvelua kehitetään osana Osallistumisympäristö-hanketta. Osallistumisympäristö tarjoaa sähköisiä palveluita kansalaisyhteiskunnan, julkisen hallinnon ja poliittisten päättäjien väliseen vuorovaikutukseen. Palvelut helpottavat kansalaisvaikuttamista ja tiedonsaantia sekä lisäävät päätösten valmistelun ja päätöksenteon läpinäkyvyyttä ja parantavat niiden laatua. Osallistumisympäristö-hanke kuuluu valtiovarainministeriön koordinoimaan Sähköisen asioinnin ja demokratian vauhdittamisohjelmaan (SADe-ohjelma).</p>
    
    <#assign href>${urls.infoIndex()}/<@u.enumDescription InfoPage.FEEDBACK /></#assign>
    <p><@u.link href=href label="Ota yhteyttä" /></p>
    
    <p><@u.link href="http://www.vm.fi/sade" label="SADe" rel="external" />-ohjelma</p>


<#else>

    <p>Den 1 mars 2012 tog Finland i bruk ett nytt sätt att påverka på statlig nivå: medborgarinitiativet. Det ger medborgarna en möjlighet att få sitt initiativ behandlat av riksdagen. Riksdagen behandlar alla initiativ som inom loppet av sex månader får stöd av minst 50 000 röstberättigade medborgare.</p>
    <p>I webbtjänsten Medborgarinitiativ.fi kan du utforma egna initiativ samt stödja och följa med initiativ som andra medborgare har startat. Det är säkert och avgiftsfritt att använda tjänsten, som fungerar även på finska med ingångssidan kansalaisaloite.fi.</p>
    <p>Skyldigheten att erbjuda tjänsten grundar sig på lagen om medborgarinitiativ. Tjänsten upprätthålls av justitieministeriet (Enheten för demokrati, språk och grundläggande rättigheter).</p> 
    <p>Tjänsten utvecklas som en del av projektet Plattform för delaktighet. Plattform för delaktighet erbjuder nättjänster för att stöda interaktionen mellan medborgarsamhället, den offentliga förvaltningen och de politiska beslutsfattarna. Tjänsterna underlättar medborgarnas möjligheter att påverka och få information, samt ökar transparensen i beredningen av och beslut i ärenden och förbättrar deras kvalitet. Projektet Plattform för delaktighet ingår i Programmet för att påskynda elektronisk ärendehantering och demokrati (SADe), som koordineras av finansministeriet.</p> 

    <#assign href>${urls.infoIndex()}/<@u.enumDescription InfoPage.FEEDBACK /></#assign>
    <p><@u.link href=href label="Ta kontakt" /></p>
    
    <p><@u.link href="http://www.vm.fi/vm/sv/05_projekt/04_sade/index.jsp" label="SADe" rel="external" />-programmet</p>

</#if>