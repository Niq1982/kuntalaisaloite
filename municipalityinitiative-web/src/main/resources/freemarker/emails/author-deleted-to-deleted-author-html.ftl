<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign title><@u.message "email.author.deleted.to.deleted.author.title" /></#assign>

<#escape x as x?html>

<@l.emailHtml template="HAUSKA_TIETO" title=title footer=false>

<p>${initiative.name}</p>
<p>${initiative.municipality.getName(locale)}</p>

<p>Aloite luotu Kuntalaisaloite.fi-palveluun ${initiative.createTime}</p>

Yksi aloitteen vastuuhenkilöistä on poistanut sinulta aloitteen ylläpito-oikeudet. Et ole enää aloitteen vastuuhenkilö etkä tekijä. Nimesi ja yhteystietosi on poistettu aloitteen tiedoista. Ota tarvittaessa yhteyttä aloitteen vastuuhenkilöihin.
<br/><br/>
Et voi enää kirjautua aloitteen ylläpitosivulle aiemmin saamallasi linkillä.

</@l.emailHtml>

</#escape>