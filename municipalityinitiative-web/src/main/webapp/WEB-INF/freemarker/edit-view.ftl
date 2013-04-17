<#import "/spring.ftl" as spring />
<#import "components/forms.ftl" as f />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/edit-blocks.ftl" as edit />

<#escape x as x?html>

<#--
 * Layout parameters for HTML-title and navigation.
 *
 * @param page is "page.createNew"
 * @param pageTitle can be assigned as custom HTML title
-->
<#assign page="page.edit" />

<@l.main page pageTitle!"">

    <h1><@u.message page /></h1>

    <#-- Create form errors summary -->
    <@u.errorsSummary path="initiative.*" prefix="initiative."/>

    <noscript>
        <@f.cookieWarning springMacroRequestContext.requestUri />
    </noscript>
    
    <div class="msg-block">
        <div class="system-msg msg-info">
            <#assign municipality><#if initiative.municipality??>${initiative.municipality.getName(locale)!""}</#if></#assign>
            <h3><@u.message key="initiative.info.title" args=[municipality] /></h3>
            <@u.message "initiative.required" /> <span class="icon-small required"></span>
        </div>
    </div>

    <#-- FORM. Use class 'sodirty' to enable dirtylisten. -->
    <form action="${springMacroRequestContext.requestUri}" method="POST" id="form-initiative" class="sodirty dirtylisten <#if hasErrors>has-errors</#if>">
        <@f.securityFilters />

        <div class="form-block-container">
            <@edit.blockHeader key="initiative.initiative.title" step=1 />
            <@edit.initiativeBlock />
        </div>

        <div class="form-block-container">
            <@edit.blockHeader key="initiative.currentAuthor.title" step=2 />
            <@edit.currentAuthorBlock "initiative" />
        </div>
        
        <div class="" id="form-action-panel">
            <div class="msg-block">
                <@u.systemMessage path="initiative.saveDraft.description" type="info" showClose=false />
            </div>

            <button class="large-button" value="true" name="${UrlConstants.ACTION_SAVE}" type="submit"><span class="large-icon save-and-send"><@u.messageHTML "action.saveDraft" /></span></button>
            <a href="${previousPageURI}" class="large-button"><span class="large-icon cancel"><@u.messageHTML "action.cancelEditDraft" /></span></a>
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
