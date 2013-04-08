<#import "/spring.ftl" as spring />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/forms.ftl" as f />
<#import "components/elements.ftl" as e />

<#escape x as x?html> 

<#assign managementURL = urls.getManagement(initiative.id) />

<#--
 * Layout parameters for HTML-title and navigation.
 * 
 * page = "page.management"
 * pageTitle = initiative.name if exists, otherwise empty string
-->

<@l.main page="page.management" pageTitle=initiative.name!"">
    
    <div class="msg-block">
        <h2><@u.message "management.warning.title" /></h2>
        <p><@u.messageHTML "management.warning.description" /></p>
        <p><a class="small-button gray" href="${urls.edit(initiative.id)}"><span class="small-icon edit"><@u.messageHTML 'action.editInitiative' /></span></a>
        <a class="small-button gray push" href="${urls.view(initiative.id)}" target="_blank"><span class="small-icon document">Esikatsele aloitteen julkista näkymää</span></a></p>
    </div>

    <@e.stateInfo initiative />

    <h1 class="name">${initiative.name!""}</h1>
    
    <div class="municipality">${initiative.municipality.name!""}</div>

    <div class="view-block public first">
        <div class="initiative-content-row">
            <@e.initiativeView initiative />
        </div>

        <div class="initiative-content-row last">
            <@e.initiativeContactInfo author.contactInfo />
        </div>
    </div>

    <#if initiative.state == InitiativeState.DRAFT>
        <div class="view-block public">
            <h2><@u.message "management.sendToReview.title" /></h2>
        
            <#if !(RequestParameters['send-to-review']?? && (RequestParameters['send-to-review'] == "confirm" || RequestParameters['send-to-review'] == "confirm-collect"))>
                <@u.systemMessage path="management.sendToReview.description" type="info" showClose=false />
        
                <br/>
                <div class="column col-1of2">
                    <h3><@u.message "management.sendToReview.doNotCollect.title" /></h3>
                    <p><@u.message "management.sendToReview.doNotCollect" /></p>
                </div>
                <div class="column col-1of2 last">
                    <h3><@u.message "management.sendToReview.collect.title" /></h3>
                    <p><@u.message "management.sendToReview.collect" /></p>
                </div>
                <br class="clear" />
                <div class="column col-1of2">
                    <a href="${managementURL}&send-to-review=confirm#send-to-review" id="js-send-to-review" class="large-button js-send-to-review"><span class="large-icon mail"><@u.messageHTML "action.sendToReview.doNotCollect" /></span></a>
                </div>
                <div class="column col-1of2 last">
                    <a href="${managementURL}&send-to-review=confirm-collect#send-to-review" id="js-send-to-review-collect" class="large-button js-send-to-review-collect"><span class="large-icon save-and-send"><@u.messageHTML "action.sendToReview.collect" /></span></a>
                </div>
                <br class="clear" />
            </#if>
            
            <#assign sendToReviewDoNotCollect>
                <@compress single_line=true>
                
                    <p><@u.message "sendToReview.doNotCollect.confirm.description" /></p>
                    
                    <form action="${springMacroRequestContext.requestUri}" method="POST" >
                        <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>
                        <input type="hidden" name="${UrlConstants.PARAM_MANAGEMENT_CODE}" value="${initiative.managementHash.value}"/>
                        <button type="submit" name="${UrlConstants.ACTION_SEND_TO_REVIEW}" id="modal-${UrlConstants.ACTION_SEND_TO_REVIEW}" value="${UrlConstants.ACTION_SEND_TO_REVIEW}" class="large-button"><span class="large-icon mail"><@u.messageHTML "action.sendToReview.doNotCollect" /></button>
                        <a href="${managementURL}" class="push close"><@u.message "action.cancel" /></a>
                    </form>
                </@compress>
            </#assign>
        
            <#-- Confirm send to REVIEW for NOSCRIPT-users -->
            <#if RequestParameters['send-to-review']?? && RequestParameters['send-to-review'] == "confirm">
            <noscript>
                <div id="send-to-review" class="system-msg msg-info">
                    <#noescape>
                        <h4><@u.message "sendToReview.doNotCollect.confirm.title" /></h4>
                        ${sendToReviewDoNotCollect}
                    </#noescape>
                </div>
            </noscript>
            </#if>
            
            <#assign sendToReviewCollect>
                <@compress single_line=true>
                
                    <p><@u.message "sendToReview.collect.confirm.description" /></p>
                    
                    <form action="${springMacroRequestContext.requestUri}" method="POST" >
                        <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>
                        <input type="hidden" name="${UrlConstants.PARAM_MANAGEMENT_CODE}" value="${initiative.managementHash.value}"/>
                        <button type="submit" name="${UrlConstants.ACTION_SEND_TO_REVIEW_COLLECT}" id="modal-${UrlConstants.ACTION_SEND_TO_REVIEW_COLLECT}" value="${UrlConstants.ACTION_SEND_TO_REVIEW_COLLECT}" class="large-button"><span class="large-icon save-and-send"><@u.messageHTML "action.sendToReview.collect" /></button>
                        <a href="${managementURL}" class="push close"><@u.message "action.cancel" /></a>
                    </form>
                </@compress>
            </#assign>
        
            <#-- Confirm send to REVIEW for NOSCRIPT-users -->
            <#if RequestParameters['send-to-review']?? && RequestParameters['send-to-review'] == "confirm-collect">
            <noscript>
                <div id="send-to-review" class="system-msg msg-info">
                    <#noescape>
                        <h4><@u.message "sendToReview.collect.confirm.title" /></h4>
                        ${sendToReviewCollect}
                    </#noescape>
                </div>
            </noscript>
            </#if>
        </div>
    </#if>


    <#--
     * Management VIEW modals
     * 
     * Uses jsRender for templating.
     * Same content is generated for NOSCRIPT and for modals.
     *
     * Modals:
     *  Request message (defined in macro u.requestMessage)
     *  Confirm send for publish and to municiaplity
     *  Confirm send for publish
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
        
        <#-- Modal: Confirm send for publish and to municiaplity. -->
        <#if sendToReviewDoNotCollect??>    
            modalData.sendToReviewDoNotCollect = function() {
                return [{
                    title:      '<@u.message "sendToReview.doNotCollect.confirm.title" />',
                    content:    '<#noescape>${sendToReviewDoNotCollect?replace("'","&#39;")}</#noescape>'
                }]
            };
        </#if>
        
        <#-- Modal: Confirm send for publish. -->
        <#if sendToReviewCollect??>    
            modalData.sendToReviewCollect = function() {
                return [{
                    title:      '<@u.message "sendToReview.collect.confirm.title" />',
                    content:    '<#noescape>${sendToReviewCollect?replace("'","&#39;")}</#noescape>'
                }]
            };
        </#if>
    
        var messageData = {};

        <#-- jsMessage: Warning if cookies are not enabled -->
        messageData.warningCookiesDisabled = function() {
            return [{
                type:      'warning',
                content:    '<h3><@u.message "warning.cookieError.title" /></h3><div><@u.messageHTML key="warning.cookieError.description" args=[managementURL] /></div>'
            }]
        };
    </script>

</@l.main>

</#escape> 