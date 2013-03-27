<#import "/spring.ftl" as spring />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/forms.ftl" as f />
<#import "components/elements.ftl" as e />

<#escape x as x?html> 

<#assign moderationURL = urls.moderation(initiative.id, initiative.managementHash.value) />

<#--
 * Layout parameters for HTML-title and navigation.
 * 
 * page = "page.initiative.public" or "page.initiative.unnamed"
 * pageTitle = initiative.name if exists, otherwise empty string
-->

<@l.main page="page.initiative.management" pageTitle=initiative.name!"">


    <#--
     * Show moderation block
    -->

    <#if initiative.state == InitiativeState.REVIEW>
        <div class="system-msg msg-summary">
            <h2><@u.message "moderation.title" /></h2>
            <#-- TODO: Real date -->
            <#assign sendToReviewDate>21.3.2013</#assign>
            <p><@u.messageHTML key="moderation.description" args=[sendToReviewDate] /></p>
            
            <div class="js-open-block hidden">
                <a class="small-button gray js-btn-open-block" data-open-block="js-block-container" href="#"><span class="small-icon save-and-send"><@u.message "action.accept" /></span></a>
                <a class="small-button gray push js-btn-open-block" data-open-block="js-block-container-alt" href="#"><span class="small-icon cancel"><@u.message "action.reject" /></span></a>
            </div>
    
            <div class="cf js-block-container js-hide">
                <noscript>
                    <@f.cookieWarning moderationURL />
                </noscript>
    
                <form action="${springMacroRequestContext.requestUri}" method="POST" id="form-send" class="sodirty">
                    <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>
                    <input type="hidden" name="${UrlConstants.PARAM_MANAGEMENT_CODE}" value="${initiative.managementHash.value}"/>
                    
                    <div class="input-block-content no-top-margin">
                        <#-- TODO: path for "acceptInitiative.comment"-->
                        <@f.textarea path="sendToMunicipality.comment" required="" optional=false cssClass="collapse" />
                    </div>
                    
                    <div class="input-block-content">
                        <button type="submit" name="${UrlConstants.ACTION_ACCEPT_INITIATIVE}" class="small-button"><span class="small-icon save-and-send"><@u.message "action.accept" /></span></button>
                        <a href="${springMacroRequestContext.requestUri}#participants" class="push js-btn-close-block hidden"><@u.message "action.cancel" /></a>
                    </div>
                    <br/><br/>
                </form>
            </div>
            
            <div class="cf js-block-container-alt js-hide">
                <noscript>
                    <@f.cookieWarning moderationURL />
                </noscript>
    
                <form action="${springMacroRequestContext.requestUri}" method="POST" id="form-send" class="sodirty">
                    <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>
                    <input type="hidden" name="${UrlConstants.PARAM_MANAGEMENT_CODE}" value="${initiative.managementHash.value}"/>
                    
                    <div class="input-block-content no-top-margin">
                        <#-- TODO: path for "acceptInitiative.comment"-->
                        <@f.textarea path="sendToMunicipality.comment" required="" optional=false cssClass="collapse" />
                    </div>
                    
                    <div class="input-block-content">
                        <button type="submit" name="${UrlConstants.ACTION_REJECT_INITIATIVE}" class="small-button"><span class="small-icon cancel"><@u.message "action.reject" /></span></button>
                        <a href="${springMacroRequestContext.requestUri}#participants" class="push js-btn-close-block hidden"><@u.message "action.cancel" /></a>
                    </div>
                    <br/><br/>
                </form>
            </div>
        </div>
    </#if>

    <@e.stateInfo initiative />

    <h1 class="name">${initiative.name!""}</h1>
    
    <div class="municipality">${initiative.municipality.name!""}</div>

    <div class="view-block public first">
        <@e.initiativeView initiative />

        <div class="initiative-content-row last">
            <h2><@u.message "initiative.contactinfo.title" /></h2>
            <p>${initiative.authorName!""}<br />
        </div>
    </div>

    <#--
     * Moderaion VIEW modals
     * 
     * Uses jsRender for templating.
     * Same content is generated for NOSCRIPT and for modals.
     *
     * Modals:
     *  Request message (defined in macro u.requestMessage)
     *  Form modified notification
     *
     * jsMessage:
     *  Warning if cookies are disabled
    -->
    <@u.modalTemplate />
    <@u.jsMessageTemplate />
    
    <script type="text/javascript">
        var modalData = {};
        
        <#-- Modal: Request messages. Check for components/utils.ftl -->
        <#if requestMessageModalHTML??>    
            modalData.requestMessage = function() {
                return [{
                    title:      '<@u.message requestMessageModalTitle+".title" />',
                    content:    '<#noescape>${requestMessageModalHTML?replace("'","&#39;")}</#noescape>'
                }]
            };
        </#if>
    
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
                content:    '<h3><@u.message "warning.cookieError.title" /></h3><div><@u.messageHTML key="warning.cookieError.description" args=[moderationURL] /></div>'
            }]
        };
    </script>

</@l.main>

</#escape> 