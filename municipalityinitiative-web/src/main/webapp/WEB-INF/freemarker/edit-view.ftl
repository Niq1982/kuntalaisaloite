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

<@l.main page pageTitle!"">

    <h1><@u.message page /></h1>

    <#-- Create form errors summary -->
    <@u.errorsSummary path="initiative.*" prefix="initiative."/>

    <noscript>
        <@f.cookieWarning springMacroRequestContext.requestUri />
    </noscript>
    
    <div class="system-msg msg-summary">
        <div class="system-msg msg-info">
            <h3>Tavallinen aloite, kunnalle <#if initiative.municipality??>${initiative.municipality.getName(locale)!""}</#if></h3>
            Lomakkeen pakolliset tiedot on merkitty tähdellä <span class="icon-small required"></span>
        </div>
    </div>

    <#-- FORM. Use class 'sodirty' to enable dirtylisten. -->
    <form action="${springMacroRequestContext.requestUri}" method="POST" id="form-initiative" class="sodirty dirtylisten <#if hasErrors>has-errors</#if>">

        <@f.securityFilters />
        <#--<@f.notTooFastField initiative />-->

        <div class="form-block-container">
            <@edit.blockHeader key="initiative.initiative.title" step=1 />
            <#-- use param locked=true to lock editing of the name and the proposal fields -->
            <@edit.initiativeBlock />
        </div>

        <div class="form-block-container">
            <@edit.blockHeader key="initiative.currentAuthor.title" step=2 />
            <@edit.currentAuthorBlock />
        </div>

        <#--
        <div class="form-block-container">
            <@edit.blockHeader key="initiative.save.title" step=3 />
        
            <div class="input-block cf">
                <div class="input-block-content">
                    <div class="system-msg msg-info">
                        Kun tallennat aloitteen, se tallentuu palveluun luonnoksena. Luonnos ei ole vielä julkinen vaan näkyy vain sinulle. Voit palata muokkaamaan aloitetta myöhemmin tai lähettää sen välittömästi julkaistavaksi ja kuntaan. Antamaasi sähköpostiosoitteeseen lähetetään aloitteen ylläpitolinkki.
                    </div>
                </div>
            
                <div class="input-block-content">
                    <input type="hidden" name="managementHash" value="${initiative.managementHash}"/>
                    <button type="submit" name="${UrlConstants.ACTION_SAVE}" value="<@u.messageHTML 'action.saveAsDraft' />" class="small-button green"><span class="small-icon save-and-send"><@u.messageHTML 'action.save' /></span></button>
                    
                    <a class="small-button red" href="${previousPageURI!urls.baseUrl+"/"+locale}"><span class="small-icon cancel"><@u.messageHTML 'action.cancel' /></span></a>
                </div>
            </div>
        </div>
        -->
        
        
        <div class="" id="form-action-panel">
        
            <div class="system-msg msg-summary">
                <div class="system-msg msg-info">
                    Kun tallennat aloitteen, se tallentuu palveluun luonnoksena. Luonnos ei ole vielä julkinen vaan näkyy vain sinulle. Voit palata muokkaamaan aloitetta myöhemmin tai lähettää sen välittömästi julkaistavaksi ja kuntaan. Antamaasi sähköpostiosoitteeseen lähetetään aloitteen ylläpitolinkki.
                </div>
            </div>
        
            <input type="hidden" name="managementHash" value="${initiative.managementHash}"/>
            <button class="large-button" value="true" name="${UrlConstants.ACTION_SAVE}" type="submit"><span class="large-icon save-and-send"><strong>Tallenna luonnos</strong> ja&nbsp;siirry&nbsp;aloitteen&nbsp;ylläpitosivulle</span></button>
            <a href="${previousPageURI!urls.baseUrl+"/"+locale}" class="large-button"><span class="large-icon cancel"><strong>Peruuta</strong> ja&nbsp;palaa&nbsp;tallentamatta&nbsp;etusivulle</span></a>
        </div>
        
        
    </form>

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
