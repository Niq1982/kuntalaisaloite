<#import "/spring.ftl" as spring />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/forms.ftl" as f />
<#import "components/elements.ftl" as e />

<#escape x as x?html> 

<#assign managementURL = urls.management(initiative.id, initiative.managementHash.value) />

<#--
 * Layout parameters for HTML-title and navigation.
 * 
 * page = "page.initiative.public" or "page.initiative.unnamed"
 * pageTitle = initiative.name if exists, otherwise empty string
-->

<@l.main page="page.initiative.management" pageTitle=initiative.name!"">

    <#assign managementWarningMessageHTML>
        <h2><@u.message "management.warning.title" /></h2>
        <p><@u.messageHTML "management.warning.description" /></p>
        <p><a class="small-button gray" href="${urls.edit(initiative.id, initiative.managementHash.value)}"><span class="small-icon edit"><@u.messageHTML 'action.editInitiative' /></span></a>
        <a class="small-button gray push" href="${urls.view(initiative.id)}"><span class="small-icon document">Esikatsele aloitteen julkista näkymää</span></a></p>
        <#--<a href="${urls.view(initiative.id)}"><@u.message "management.warning.link" /></a>-->
    </#assign>
    
    <@u.systemMessageHTML html=managementWarningMessageHTML type="summary" />

    <span class="extra-info">
    <#if initiative.createTime??>
        <#assign createTime><@u.localDate initiative.createTime /></#assign>
        <@u.message key="initiative.date.create" args=[createTime] />
        <br />
        Aloite odottaa julkaisua
    </#if>
    </span>

    <h1 class="name">${initiative.name!""}</h1>
    
    <div class="municipality">${initiative.municipality.name!""}</div>

    <div class="view-block public first">
        <@e.initiativeView initiative />

        <div class="initiative-content-row last">
            <h2><@u.message "initiative.contactinfo.title" /></h2>
            <p>${author.contactInfo.name!""}<br />
            ${author.contactInfo.email!""}<br />
            ${author.contactInfo.address!""}<br />
            ${author.contactInfo.phone!""}<br />
        </div>
    </div>

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
                <a href="${managementURL}&send-to-review=confirm#send-to-review" class="small-button js-send-to-review"><span class="small-icon mail"><@u.message "action.sendToReview.doNotCollect" /></span></a>
            </div>
            <div class="column col-1of2 last">
                <a href="${managementURL}&send-to-review=confirm-collect#send-to-review" class="small-button js-send-to-review-collect"><span class="small-icon save-and-send"><@u.message "action.sendToReview.collect" /></span></a>
            </div>
            <br class="clear" />
        </#if>
        
        <#assign sendToReviewDoNotCollect>
            <@compress single_line=true>
            
                <p><@u.message "sendToReview.doNotCollect.confirm.description" /></p>
                
                <form action="${springMacroRequestContext.requestUri}" method="POST" >
                    <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>
                    <input type="hidden" name="${UrlConstants.PARAM_MANAGEMENT_CODE}" value="${initiative.managementHash.value}"/>
                    <button type="submit" name="${UrlConstants.ACTION_SEND_TO_REVIEW}" id="modal-${UrlConstants.ACTION_SEND_TO_REVIEW}" value="<@u.message "action.sendToReview.doNotCollect" />" class="small-button green"><span class="small-icon mail"><@u.messageHTML "action.sendToReview.doNotCollect" /></button>
                    <a href="${managementURL}" class="push close"><@u.message "action.cancel" /></a>
                </form>
            </@compress>
        </#assign>
    
        <#-- Confirm send to VRK for NOSCRIPT-users -->
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
                    <button type="submit" name="${UrlConstants.ACTION_SEND_TO_REVIEW_COLLECT}" id="modal-${UrlConstants.ACTION_SEND_TO_REVIEW_COLLECT}" value="<@u.message "action.sendToReview.cllect" />" class="small-button green"><span class="small-icon save-and-send"><@u.messageHTML "action.sendToReview.collect" /></button>
                    <a href="${managementURL}" class="push close"><@u.message "action.cancel" /></a>
                </form>
            </@compress>
        </#assign>
    
        <#-- Confirm send to VRK for NOSCRIPT-users -->
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


    <#--
     * Public VIEW modals
     * 
     * Uses jsRender for templating.
     * Same content is generated for NOSCRIPT and for modals.
     *
     * Modals:
     *  Request message (defined in macro u.requestMessage)
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
        
        <#-- Modal: Confirm send for publish and to municiaplity. Check for components/utils.ftl -->
        <#if sendToReviewDoNotCollect??>    
            modalData.sendToReviewDoNotCollect = function() {
                return [{
                    title:      '<@u.message "sendToReview.doNotCollect.confirm.title" />',
                    content:    '<#noescape>${sendToReviewDoNotCollect?replace("'","&#39;")}</#noescape>'
                }]
            };
        </#if>
        
        <#-- Modal: Confirm send for publish. Check for components/utils.ftl -->
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