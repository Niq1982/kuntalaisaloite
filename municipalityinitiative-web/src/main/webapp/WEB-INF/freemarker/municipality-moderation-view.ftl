<#import "/spring.ftl" as spring />
<#import "components/forms.ftl" as f />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/edit-blocks.ftl" as edit />

<#escape x as x?html>

<#--
 * Layout parameters for HTML-title and navigation.
-->
<@l.main "page.municipality.moderation" >

<h1><@u.message "municipalities.edit.title" /></h1>

    <#-- Create form errors summary -->
    <#--<@u.errorsSummary path="updateData.*" prefix="updateData."/>-->

<div class="msg-block">
    <p><@u.message "municipalities.edit.description" /></p>
    <p><@u.message "municipalities.edit.instruction" /></p>
</div>

<table class="data municipalities">
    <thead>
        <tr>
            <th><a href="#" class="js-toggle-active trigger-tooltip" title="<@u.message "municipalities.edit.toggle" />" ><span class="icon-small more less"></span></a></th>
            <th><@u.message "municipality.nameFi" /></th>
            <th><@u.message "municipality.nameSv" /></th>
            <th><@u.message "municipality.email" /></th>
        </tr>
    </thead>
    <tbody>
        <#list municipalities as municipality>
            <tr class="${municipality.active?string("active","not-active")}">
                <td><#if municipality.active><span class="icon-small icon-16 confirmed trigger-tooltip" title="<@u.message "municipalities.edit.active" />"></span></#if></td>
                <td><a  href="#"
                        class="js-edit-municipality"
                        data-id="${municipality.id}"
                        data-email="${municipality.email!""}"
                        data-active="${municipality.active?string}"
                    >${municipality.nameFi}</a></td>
                <td>${municipality.nameSv}</td>
                <td>${municipality.email!""}</td>
            </tr>
        </#list>
    </tbody>
</table>


<#assign editMunicipalityDetailsHTML>
<@compress single_line=true>
    <#-- Create form errors summary -->
    <@u.errorsSummary path="updateData.*" prefix="updateData."/>

    <form action="${springMacroRequestContext.requestUri}" id="municipality-form" method="POST">
        <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>
        
        <@spring.formHiddenInput "updateData.id" />
        
        <h3 id="selected-municipality" data-empty="<i><@u.message "municipality.edit.noneSelected" /></i>">
            <#if spring.status.value??>${municipalities[spring.status.value?number-1].getName(locale)!""}</#if>
        </h3>
        
        <div class="input-block-content">
            <@f.radiobutton path="updateData.active" required="" header=false options={
                    "true":"municipality.edit.active",
                    "false":"municipality.edit.notActive"
                } attributes="" />
        </div>
    
        <div class="input-block-content">
            <@f.textField path="updateData.municipalityEmail" required="required" optional=false cssClass="large" maxLength=InitiativeConstants.CONTACT_EMAIL_MAX />
        </div>
        
        <div class="input-block-content">
            <button type="submit" name="${UrlConstants.ACTION_ACCEPT_INITIATIVE}" class="small-button"><span class="small-icon save-and-send"><@u.message "action.save" /></span></button>
            <a href="${springMacroRequestContext.requestUri}" class="push close"><@u.message "action.cancel" /></a>
        </div>
    </form>
</@compress>
</#assign>

<#--
     * Public VIEW modals
     * 
     * Uses jsRender for templating.
     * Same content is generated for NOSCRIPT and for modals.
     *
     * Modals:
     *  Edit municipality details
     *
     * jsMessage:
     *  Warning if cookies are disabled
    -->
    <#-- TODO: Check that what is needed here as there is nomore management -->
    <@u.modalTemplate />
    <@u.jsMessageTemplate />
    
    <script type="text/javascript">
        var modalData = {};
    
        <#-- Modal: Edit municipality details -->
        modalData.editMunicipalityDetails = function() {
            return [{
                title:      '<@u.message "municipality.edit.title" />',
                content:    '<#noescape>${editMunicipalityDetailsHTML!""}</#noescape>'
            }]
        };
        
        <#-- Modal: Edit municipality details invalid form data -->
        <#if hasErrors?? && hasErrors>
        modalData.editMunicipalityDetailsInvalid = function() {
            return [{
                title:      '<@u.message "municipality.edit.title" />',
                content:    '<#noescape>${editMunicipalityDetailsHTML!""}</#noescape>'
            }]
        };
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

</@l.main>

</#escape>