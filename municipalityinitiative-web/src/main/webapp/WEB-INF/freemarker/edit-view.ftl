<#import "/spring.ftl" as spring />
<#import "components/forms.ftl" as f />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/edit-blocks.ftl" as edit />
<#import "components/elements.ftl" as e />

<#escape x as x?html>

<#--
 * Layout parameters for HTML-title and navigation.
 *
 * @param page is "page.createNew"
 * @param pageTitle can be assigned as custom HTML title
-->
<#assign page="page.edit" />

<#assign locationSelected = updateData.locationLat?? && updateData.locationLng??>

<@l.main page pageTitle!"">

    <h1><@u.message page /></h1>

    <#-- Create form errors summary -->
    <@u.errorsSummary path="updateData.*" prefix="updateData."/>

    <noscript>
        <@f.cookieWarning springMacroRequestContext.requestUri />
    </noscript>
    
    <div class="msg-block">
        <div class="system-msg msg-info">
            <#assign municipality><#if updateData.municipality??>${initiative.municipality.getName(locale)!""}</#if></#assign>
            <h3><@u.message "initiative.initiativeType."+initiative.type /> <@u.message key="initiative.info.municipality" args=[municipality] /></h3>
            <@f.fieldRequiredInfo />
        </div>
    </div>

    <#-- FORM. Use class 'sodirty' to enable dirtylisten. -->
    <form action="${springMacroRequestContext.requestUri}" method="POST" id="form-initiative" class="sodirty dirtylisten <#if hasErrors>has-errors</#if>" novalidate>
        <@f.securityFilters />

        <div class="form-block-container">
            <@edit.blockHeader key="initiative.initiative.title" step=1 />
            <@edit.initiativeBlock "updateData" locationSelected/>
        </div>

        <div class="form-block-container">
            <@edit.blockHeader key="initiative.currentAuthor.title" step=2 />
            <#if initiative.isVerifiable()>
                <@edit.currentVerifiedAuthorBlock "updateData" />
            <#else>
                <@edit.currentAuthorBlock "updateData" />
            </#if>

        </div>
        
        <div class="" id="form-action-panel">
            <div class="msg-block">
                <@u.systemMessage path="initiative.saveDraft.description" type="info" />
            </div>

            <button class="large-button" value="true" name="${UrlConstants.ACTION_SAVE}" type="submit"><span class="large-icon save-and-send"><@u.messageHTML "action.saveDraft" /></span></button>
            <#if hasNeverBeenSaved>
                <a href="${urls.frontpage()}" class="large-button"><span class="large-icon cancel"><@u.messageHTML "action.cancelEditDraft" /></span></a>
            <#else>
                <a href="${previousPageURI}" class="large-button"><span class="large-icon cancel"><@u.messageHTML "action.cancelUpdateInitiative" /></span></a>
            </#if>
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
<@u.jsGoogleMapsLib />
<script type="text/javascript">
    var modalData = {};

    <#-- Modal: Form modified notification. Uses dirtyforms jQuery-plugin. -->
    modalData.formModifiedNotification = function() {
        return [{
            title:      '<@u.message "form.modified.notification.title" />',
            content:    '<@u.messageHTML "form.modified.notification" />'
        }]
    };

    modalData.mapContainer = function() {
        return [{
            title:      '<@u.message "map.mapModalTitle" />',
            content:    '<#noescape>${edit.mapContainer?replace("'","&#39;")}</#noescape>'
        }]
    };

    modalData.initialLocation = '${initiative.municipality.getName(locale)}';
    <#if locationSelected>
        modalData.selectedLocation = {"lat" : "${updateData.locationLat?c}", "lng" : "${updateData.locationLng?c}"};
    </#if>

    var messageData = {};

    <#-- jsMessage: Warning if cookies are not enabled -->
    messageData.warningCookiesDisabled = function() {
        return [{
            type:      'warning',
            content:    '<h3><@u.message "warning.cookieError.title" /></h3><div><@u.messageHTML key="warning.cookieError.description" args=[springMacroRequestContext.requestUri] /></div>'
        }]
    };


</script>

<@edit.sessionExpired />


</@l.main>
</#escape>
