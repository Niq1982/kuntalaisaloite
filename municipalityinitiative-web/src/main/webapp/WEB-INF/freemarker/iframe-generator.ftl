<#import "/spring.ftl" as spring />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/iframe.ftl" as i />

<#escape x as x?html>

<#--
 * Layout parameters for HTML-title and navigation.
 * 
 * @param page is "page.iframeGenerator"
 * @param pageTitle can be assigned as custom HTML title
-->
<#assign page="page.find" />

<@l.main "page.iframeGenerator" pageTitle!"">

    <h1><@u.message "page.iframeGenerator" /></h1>

    <p>Leijukkeen avulla voidaan näyttää Kuntalaisaloite.fi-palvelun sisältöä toisen palvelun sivustolla. Leijuke näyttää uusimmat aloitteet Kuntalaisaloite.fi-palvelussa sekä toiminnot "Selaa kuntalaisaloitteista" sekä "Tee kuntalaisaloite". Toiminnot siirtyvät automaattisesti leijukkeessa määritetyn kunnan vastaaviin toimintoihin Kuntalaisaloite.fi-palveluun.</p>
    <p>Leijukkeen kaikki linkit aukeavat selaimessa uuteen välilehteen ja käyttäjä ohjataan Kuntalaisaloite.fi-palvelun puolelle.</p>
    <p>Leijukkeen sisältö päivittyy Kuntalaisaloite.fi-palvelun sisällön mukaan automaattisesti muutaman minuutin viiveellä.</p>

    <h3>Leijukkeen määrittäminen</h3>
    
    <p>Leijukketta voidaan säätää haluamaksi parametrien avulla. Parametreilla voi säätää</p>
    <ul>
        <li>minkä kunnan aloitteita leijukkeessa listataan</li>
        <li>kuinka monta uusinta aloitetta listassa näytetään</li>
        <li>leijukkeen leveyttä (220-960 pikseliä)</li>
        <li>leijukkeen korkeutta (300-2000 pikseliä)</li>
    </ul>
    
    <div class="view-block first">
        <@i.initiativeIframeGenerator municipalities />
    </div>

</@l.main>

</#escape>