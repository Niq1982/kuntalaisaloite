<#import "/spring.ftl" as spring />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/forms.ftl" as f />
<#import "components/elements.ftl" as e />

<#escape x as x?html> 

<#assign moderationURL = urls.moderation(initiative.id) />

<#--
 * Layout parameters for HTML-title and navigation.
 * 
 * page = "page.moderation"
 * pageTitle = initiative.name if exists, otherwise empty string
-->

<@l.main page="page.moderation" pageTitle=initiative.name!"">

    <#--
     * Show moderation block
    -->
    <#if managementSettings.allowOmAccept>
        <div class="msg-block">
            <h2><@u.message "moderation.title" /></h2>
            <#assign sendToReviewDate><@u.localDate initiative.stateTime/></#assign>
            <p><@u.message key="moderation.description" args=[sendToReviewDate] /></p>
            
            <div class="js-open-block hidden">
                <a class="small-button gray js-btn-open-block" data-open-block="js-block-container" href="#"><span class="small-icon save-and-send"><@u.message "action.accept" /></span></a>
                <a class="small-button gray push js-btn-open-block" data-open-block="js-block-container-alt" href="#"><span class="small-icon cancel"><@u.message "action.reject" /></span></a>
            </div>
    
            <div class="cf js-block-container js-hide">
                <noscript>
                    <@f.cookieWarning moderationURL />
                </noscript>
    
                <form action="${springMacroRequestContext.requestUri}" method="POST" id="form-accept" class="sodirty">
                    <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>
                    
                    <div class="input-block-content no-top-margin">
                        <textarea name="${UrlConstants.PARAM_SENT_COMMENT}" id="commentAccept" class="collapse" maxlength="${InitiativeConstants.INITIATIVE_MODERATOR_COMMENT_MAX}"></textarea>
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
    
                <form action="${springMacroRequestContext.requestUri}" method="POST" id="form-reject" class="sodirty">
                    <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>
                    
                    <div class="input-block-content no-top-margin">
                        <textarea name="${UrlConstants.PARAM_SENT_COMMENT}" id="commentReject" class="collapse" maxlength="${InitiativeConstants.INITIATIVE_MODERATOR_COMMENT_MAX}"></textarea>
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
    
    <#--
     * Show send back for fixing block
    -->
    <#if managementSettings.allowOmSendBackForFixing>
        <div class="msg-block">
            <h2><@u.message "sendBackForFixing.title" /></h2>
            <p><@u.message "sendBackForFixing.description" /></p>
            
            <div class="js-open-block hidden">
                <a class="small-button gray js-btn-open-block" data-open-block="js-block-container" href="#"><span class="small-icon cancel"><@u.message "action.reject" /></span></a>
            </div>
            
            <div class="cf js-block-container js-hide">
                <noscript>
                    <@f.cookieWarning moderationURL />
                </noscript>
    
                <form action="${springMacroRequestContext.requestUri}" method="POST" id="form-reject" class="sodirty">
                    <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>
                    
                    <div class="input-block-content no-top-margin">
                        <textarea name="${UrlConstants.PARAM_SENT_COMMENT}" id="commentReject" class="collapse" maxlength="${InitiativeConstants.INITIATIVE_MODERATOR_COMMENT_MAX}" ></textarea>
                    </div>
                    
                    <div class="input-block-content">
                        <button type="submit" name="${UrlConstants.ACTION_SEND_TO_FIX}" class="small-button"><span class="small-icon cancel"><@u.message "action.reject" /></span></button>
                        <a href="${springMacroRequestContext.requestUri}#participants" class="push js-btn-close-block hidden"><@u.message "action.cancel" /></a>
                    </div>
                    <br/><br/>
                </form>
            </div>
        </div>
    </#if>
    
    <#--
     * Renew author management hash
    -->
    <#assign renewManagementHash>
    <@compress single_line=true>
        <form action="${springMacroRequestContext.requestUri}" method="POST">
            <@f.securityFilters/>
            <input type="hidden" name="authorId" id="authorId" value="" />
            
            <h3><@u.message "moderator.renewManagementHash.confirm.author" /></h3>
        
            <div id="selected-author" class="details"></div>
            
            <div class="input-block-content">
                <button type="submit"value="<@u.message "action.renewManagementHash" />" class="small-button"><span class="small-icon save-and-send"><@u.message "action.renewManagementHash" /></button>
                <a href="${springMacroRequestContext.requestUri}" class="push close"><@u.message "action.cancel" /></a>
            </div>
        </form>
    </@compress>
    </#assign>

        
    <h1 class="name">${initiative.name!""}</h1>
    
    <div class="municipality">${initiative.municipality.getName(locale)}</div>
    
    <@e.stateInfo initiative />

    <div class="view-block first">
        <@e.initiativeView initiative />        
    </div>
    
    <div class="view-block">
        <div class="initiative-content-row last">
            <h2><@u.message key="initiative.people.title" args=[authors?size] /></h2>
            
            <@e.initiativeContactInfo authorList=authors showRenewManagementHash=!initiative.verifiable/>
        </div>
    </div>
    
    <#--
     * Moderation VIEW modals
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
        
        <#-- Modal: Form modified notification. Uses dirtyforms jQuery-plugin. -->
        modalData.renewManagementHash = function() {
            return [{
                title:      '<@u.message "moderator.renewManagementHash.confirm.title" />',
                content:    '<#noescape>${renewManagementHash?replace("'","&#39;")}</#noescape>'
            }]
        };
        
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