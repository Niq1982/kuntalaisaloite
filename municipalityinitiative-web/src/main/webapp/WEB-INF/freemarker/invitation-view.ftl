<#import "/spring.ftl" as spring />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/elements.ftl" as e />
<#import "components/forms.ftl" as f />
<#import "components/some.ftl" as some />

<#escape x as x?html> 

<#--
 * Layout parameters for HTML-title and navigation.
 * 
 * page = "page.initiative.public" or "page.initiative.unnamed"
 * pageTitle = initiative.name if exists, otherwise empty string
-->
<@l.main "page.initiative.public" initiative.name!"">
        
    <#-- Bind form for detecting validation errors -->
    <@spring.bind "authorInvitation.*" />
    <#assign validationError = spring.status.error />

    <#--
     * invitationAcceptHtml
     * 
     * Accept or reject invitation.
     * Launches a modal-window for confirmation.
     *
     * NOSCRIPT-users gets confirmation form by request parameter 'invitation-reject'.
    -->
    <#if !RequestParameters['invitation-reject']?? && !RequestParameters['invitation-accept']??>
        <div class="msg-block ${validationError?string("hidden","")}">
            <h2><@u.message "invitation.view.title" /></h2>
            
            <p><@u.message "invitation.view.description" /></p>
            <p><@u.message "invitation.view.instruction" /></p>
            
            <a href="?invitation=${authorInvitation.confirmCode!""}&invitation-accept=confirm" class="small-button green green save-and-send js-accept-invitation"><span class="small-icon save-and-send"><@u.message "invitation.accept" /></span></a>
            <a href="?invitation=${authorInvitation.confirmCode!""}&invitation-reject=confirm" title="<@u.message "invitation.reject" />" class="small-button gray push js-reject-invitation"><span class="small-icon cancel"><@u.message "invitation.reject" /></span></a>
        </div>
    </#if>
    
    
    <#--
     * invitationRejectConfirmHtml
     * 
     * Modal: Confirm reject invitation.
     *
     * NOSCRIPT-users gets confirmation form by request parameter 'invitation-reject=confirm'.
    -->
    <#assign invitationRejectConfirmHtml>
        <@compress single_line=true>
        
            <p><@u.message "invitation.reject.confirm.description" /></p>

            <form action="${springMacroRequestContext.requestUri}" method="POST">
                <input type="hidden" name="CSRFToken" value="${CSRFToken!}"/>
                <input type="hidden" name="${UrlConstants.PARAM_INVITATION_CODE}" value="${authorInvitation.confirmCode!""}"/>
                <button type="submit" name="${UrlConstants.ACTION_REJECT_INVITATION}" id="modal-${UrlConstants.ACTION_REJECT_INVITATION}"  value="<@u.message "invitation.reject" />" class="small-button gray cancel"><span class="small-icon cancel"><@u.message "invitation.reject.confirm" /></span></button>
                <a href="?invitation=${authorInvitation.confirmCode!""}" class="push close"><@u.message "action.cancel" /></a>
            </form>
        
        </@compress>
    </#assign>
    
    <#-- Confirm reject invitation for NOSCRIPT-users -->
    <#if RequestParameters['invitation-reject']?? && RequestParameters['invitation-reject'] == "confirm">
        <noscript>
            <div class="msg-block">
                <h2><@u.message "invitation.reject.confirm.title" /></h2>
               <#noescape>${invitationRejectConfirmHtml}</#noescape>
            </div>
        </noscript>
    </#if> 
        
    
    
    <#--
     * invitationAcceptHtml
     * 
     * Modal for accepting invitation.
     *
     * Includes NOSCRIPT
    -->
    <#assign invitationAcceptHtml>
        <@compress single_line=true>        
        
            <@u.systemMessage path="invitation.accept.confirm.description" type="info" showClose=false />
        
            <@u.errorsSummary path="authorInvitation.*" prefix="initiative."/>

            <form action="${springMacroRequestContext.requestUri}" method="POST" class="js-validate" novalidate>
                <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>
                <input type="hidden" name="confirmCode" value="${authorInvitation.confirmCode!""}"/>
                <@f.notTooFastField authorInvitation/>
            
                    <div class="column col-1of2">
                        <@f.textField path="authorInvitation.contactInfo.name" required="required" optional=false cssClass="medium" maxLength=InitiativeConstants.CONTACT_NAME_MAX key="contactInfo.name" />
                    </div>
                    
                    <div class="column col-1of2 last">
                        <@f.municipalitySelect path="authorInvitation.homeMunicipality" options=municipalities required="required" cssClass="municipality-select" preSelected=initiative.municipality.id key="initiative.homeMunicipality" />
                    </div>
                    <br class="clear" />
                    
                    <div id="municipalMembership" class="js-hide">
                        <div class="input-block-content hidden">
                            <#assign href="#" />
                            <@u.systemMessage path="initiative.municipality.notEqual" type="info" showClose=false args=[href] />
                        </div>
                        <div class="input-block-content">
                            <@f.radiobutton path="authorInvitation.municipalMembership" required="required" options={
                                "community":"initiative.municipalMembership.community",
                                "company":"initiative.municipalMembership.company",
                                "property":"initiative.municipalMembership.property",
                                "none":"initiative.municipalMembership.none"
                            } attributes="" key="initiative.municipalMembership" />
                        </div>
                        
                        <div class="input-block-content is-not-member no-top-margin js-hide hidden">
                            <@u.systemMessage path="warning.initiative.notMember" type="warning" showClose=false />
                        </div>
                    </div>
                    
                    <div class="input-block-content">
                        <@f.formCheckbox path="authorInvitation.contactInfo.showName" checked=true key="contactInfo.showName" />
                    </div>
                    
                    <div class="input-block-content">
                        <div class="input-header">
                            <@u.message "contactInfo.title" />
                        </div>
                        
                        <@f.contactInfo path="authorInvitation.contactInfo" disableEmail=false mode="full" />
                    </div>
                
                    <div class="input-block-content">
                        <button type="submit" name="${UrlConstants.ACTION_ACCEPT_INVITATION}" id="modal-${UrlConstants.ACTION_ACCEPT_INVITATION}" value="<@u.message "invitation.accept.confirm" />" class="small-button green save-and-send"><span class="small-icon save-and-send"><@u.message "invitation.accept.confirm" /></span></button>
                        <a href="?invitation=${authorInvitation.confirmCode!""}" class="push close"><@u.message "action.cancel" /></a>
                    </div>
            </form>                    
        </@compress>
    </#assign>

    <#if validationError || RequestParameters['invitation-accept']?? && RequestParameters['invitation-accept'] == "confirm">
        <noscript>
            <div class="msg-block">
                <h2><@u.message "invitation.accept.confirm.title" /></h2>
                <#noescape>${invitationAcceptHtml}</#noescape>
                <br class="clear" />
            </div>
        </noscript>
    </#if>


    <h1 class="name">${initiative.name!""}</h1>
    
    <div class="municipality">${initiative.municipality.getName(locale)}</div>
    
    <@e.stateInfo initiative />

    <#-- VIEW BLOCKS -->
    <div class="view-block public first">
        <div class="initiative-content-row">
            <@e.initiativeView initiative />
        </div>
        <#-- TODO: Maybe show contact info here ?-->
        <div class="initiative-content-row last">
            <@e.initiativeAuthor publicAuthors />
        </div>
    </div>


    <#--
     * Public VIEW modals
     * 
     * Uses jsRender for templating.
     * Same content is generated for NOSCRIPT and for modals.
     *
     * Modals:
     *  Accept invitation
     *  Confirm reject invitation
     *
     * jsMessage:
     *  Warning if cookies are disabled
    -->
    <#-- TODO: Check that what is needed here as there is nomore management -->
    <@u.modalTemplate />
    <@u.jsMessageTemplate />
    
    <script type="text/javascript">
        var modalData = {};

        <#-- Modal: Accept invitation -->
        <#if invitationAcceptHtml??>    
            modalData.acceptInvitation = function() {
                return [{
                    title:      '<@u.message "invitation.accept.confirm.title" />',
                    content:    '<#noescape>${invitationAcceptHtml?replace("'","&#39;")}</#noescape>'
                }]
            };
            
            <#-- Autoload modal if it has errors -->
            <#if hasErrors?? && hasErrors>
            modalData.acceptInvitationInvalid = function() {
                return [{
                    title:      '<@u.message "invitation.accept.confirm.title" />',
                    content:    '<#noescape>${invitationAcceptHtml?replace("'","&#39;")}</#noescape>'
                }]
            };
            </#if>
        </#if>
        
        <#-- Modal: Accept invitation -->
        <#if invitationRejectConfirmHtml??>    
            modalData.confirmRejectInvitation = function() {
                return [{
                    title:      '<@u.message "invitation.reject.confirm.title" />',
                    content:    '<#noescape>${invitationRejectConfirmHtml?replace("'","&#39;")}</#noescape>'
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