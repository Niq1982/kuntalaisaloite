<#import "/spring.ftl" as spring />
<#import "components/forms.ftl" as f />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/edit-blocks.ftl" as edit />
<#import "components/some.ftl" as some />

<#escape x as x?html>

<#--
 * Layout parameters for HTML-title and navigation.
 *
 * @param page is "page.pending.confirmation"
 * @param pageTitle can be assigned as custom HTML title
-->
<#assign pageTitle><@u.message "create.pendingConfirmation.title" /></#assign>

<@l.main page="page.pending.confirmation" pageTitle=pageTitle!"" requestMessagesON=false>

<h1><#noescape>${pageTitle!""}</#noescape></h1>
    
<#if requestMessages?? && (requestMessages?size > 0)>
    <@u.requestMessage requestMessages />
</#if>

<p>Linkki kuntalaisaloitteen tekemiseen on lähetetty antamaasi sähköpostiosoitteeseen <strong>${requestAttribute!""}</strong>.</p>

<p>Siirry lukemaan saamasi sähköposti ja klikkaa siellä olevaa linkkiä. Tämän jälkeen pääset täyttämään aloitteen sisällön.</p>

<h3>Mitä tehdä, jos sähköpostia ei ole tullut</h3>
<ul>
    <li>Tarkista yltä, että annoit oikean sähköpostiosoitteen. Jos osoitteessasi on virhe, siirry takaisin <a href="${urls.prepare()}">Tee kuntalaisaloite</a>-sivulle ja aloita kuntalaisaloitteen tekeminen uudelleen.</li>
    <li>Tarkista sähköpostisi roskapostilaatikko, lähetetty posti on saattanut mennä sinne.</li>
    <li>Tarkista, ettei sähköpostilaatikkosi ole täynnä.</li>
</ul>
    
<h2>Ylläpitolinkki testausta varten - poistuu kun sähköpostit lähtevät</h2>
<a href="${urls.loginAuthor(initiative.id, initiative.managementHash.value)}">${urls.loginAuthor(initiative.id, initiative.managementHash.value)}</a>


</@l.main>
</#escape>
