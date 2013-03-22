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
 * @param page is "page.createNew"
 * @param pageTitle can be assigned as custom HTML title
-->
<#assign page="page.createNew" />

<#--
 * Get current municipality as request parameter.
 * - Request parameter is for iFrame create-link
 * - We could want to try to guess user's municipality later on with different methods like GEO location, user's history etc..
-->
<#assign currentMunicipality = RequestParameters['municipality']!"" />

<#assign confirmationSent = false />
<#if RequestParameters['success']?? && RequestParameters['success'] == "true" >
    <#assign confirmationSent = true />
</#if>

<#if RequestParameters['id']??>
    <#assign initiativeId = RequestParameters['id'] />
</#if>

<@l.main page pageTitle!"">

    <#if confirmationSent == false>

        <h1><@u.message page /></h1>
    
        <#-- TOP CONTRIBUTION -->
        <#noescape>${topContribution!""}</#noescape>
    
        <#-- Create form errors summary -->
        <@u.errorsSummary path="initiative.*" prefix="initiative."/>
    
        <noscript>
            <@f.cookieWarning springMacroRequestContext.requestUri />
        </noscript>
    
        <#-- FORM. Use class 'sodirty' to enable dirtylisten. -->
        <form action="${springMacroRequestContext.requestUri}" method="POST" id="form-preparation" class="sodirty dirtylisten <#if hasErrors>has-errors</#if>">
    
            <@f.securityFilters />
            <@f.notTooFastField initiative />
    
            <div class="form-block-container">
                <div id="" class="input-block cf">
                    <@edit.municipalityBlock municipality=currentMunicipality />
                </div>
            </div>
            
            <div class="form-block-container toggle-disable">
                <div id="" class="input-block cf">
                    <@edit.chooseInitiativeType />
                </div>
            </div>
    
            <div class="form-block-container toggle-disable">
                <div id="" class="input-block cf">
                
                    <@edit.authorEmailBlock />
                    
                    <div class="input-block-content no-top-margin">
                        <@edit.buttons type="save" /> <span class="fill-in-all push hidden"><@u.message "initiative.fillInAllFields" /></span>
                    </div>
                    
                </div>
            </div>
        </form>
    
        <#-- BOTTOM CONTRIBUTION -->
        <#noescape>${bottomContribution!""}</#noescape>
    
    <#else>
    
        <h1>Linkki aloitteen tekemiseen on lähetetty sähköpostiisi</h1>
        
        
        <#assign confirmInfo>
            <p>Linkki kuntalaisaloitteen tekemiseen on lähetetty antamaasi sähköpostiosoitteeseen<br/>
            ritva.matikainen@yahoo.com</p>
            
            <p>Siirry lukemaan saamasi sähköposti ja klikkaa siellä olevaa linkkiä. Tämän jälkeen pääset täyttämään aloitteen sisällön.</p>

            <h3>Mitä tehdä, jos sähköpostia ei ole tullut</h3>
            <ul>
                <li>Tarkista yltä, että annoit oikean sähköpostiosoitteen. Jos osoitteessasi on virhe, siirry takaisin <a href="${urls.prepare()}">Tee kuntalaisaloite</a>-sivulle ja aloitea kuntalaisaloitteen tekeminen uudelleen</li>
                <li>Tarkista sähköpostisi roskapostilaatikko, lähetetty posti on saattanut mennä sinne</li>
                <li>Tarkista, ettei sähköpostilaatikkosi ole täynnä</li>
            </ul>
        </#assign>
    
        <@u.systemMessageHTML confirmInfo "summary" />
    
    </#if>


<#--
 * Create page modals and jsMessage
 *
 * Uses jsRender for templating.
 * Same content is generated for NOSCRIPT and for modals.
 *
 * Modals:
 *  Form modified notification (dirtyform)
 *
 * jsMessage:
 *  Warning if cookies are disabled
-->
<@u.modalTemplate />
<@u.jsMessageTemplate />

<script type="text/javascript">
    var modalData = {};

    <#-- Modal: Form modified notification. Uses dirtyforms jQuery-plugin. -->
    modalData.formModifiedNotification = function() {
        return [{
            title:      '<@u.message "form.modified.notification.title" />',
            content:    '<@u.messageHTML "form.modified.notification" />'
        }]
    };

    var messageData = {};

    <#-- jsMessage: Warning if cookies are not enabled -->
    messageData.warningCookiesDisabled = function() {
        return [{
            type:      'warning',
            content:    '<h3><@u.message "warning.cookieError.title" /></h3><div><@u.messageHTML key="warning.cookieError.description" args=[springMacroRequestContext.requestUri] /></div>'
        }]
    };
</script>


</@l.main>
</#escape>