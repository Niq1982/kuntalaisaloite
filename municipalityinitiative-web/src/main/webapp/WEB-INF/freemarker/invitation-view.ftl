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

    <#-- PAHASTI VAIHEESSA -->

    <#--
     * invitationAcceptHtml
     * 
     * Accept or decline invitation.
     * Launches a modal-window for confirmation.
     *
     * NOSCRIPT-users gets confirmation form by request parameter 'invitation-decline'.
    -->
    <#if !RequestParameters['invitation-decline']?? && !RequestParameters['invitation-accept']??>
        <div class="msg-block">
            <h2>Kutsu vastuuhenkilöksi</h2>
            
            <p>Ohjetta vastuuhenkilöistä Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut erat purus, mattis eget tempor vel, sodales et ipsum. Morbi enim orci, lobortis sagittis volutpat at, placerat in nisi.</p>
            
            <form action="${springMacroRequestContext.requestUri}" method="POST" >
                <input type="hidden" name="invitation" value="${invitationCode!""}"/>
        
                <a href="?invitation-accept=confirm" class="small-button green green save-and-send js-accept-invitation"><span class="small-icon save-and-send"><@u.message "invitation.accept" /></span></a>
                <a href="?invitation-decline=confirm" title="<@u.message "invitation.decline" />" class="small-button gray push js-decline-invitation"><span class="small-icon cancel"><@u.message "invitation.decline" /></span></a>
            </form>
        </div>
    </#if>
    
    
    <#--
     * invitationDeclineConfirmHtml
     * 
     * Modal: Confirm decline invitation.
     *
     * NOSCRIPT-users gets confirmation form by request parameter 'invitation-decline=confirm'.
    -->
    <#assign invitationDeclineConfirmHtml>
        <@compress single_line=true>
        
            <p><@u.message "invitation.decline.confirm" /></p>
            
            <form action="${springMacroRequestContext.requestUri}" method="POST" >
                <input type="hidden" name="CSRFToken" value="${CSRFToken!}"/>
                <input type="hidden" name="invitation" value="${invitationCode!""}"/>
                <button type="submit" name="#" value="<@u.message "invitation.decline" />" class="small-button gray cancel"><span class="small-icon cancel"><@u.message "invitation.decline" /></span></button>
                <a href="${springMacroRequestContext.requestUri}" class="push close"><@u.message "action.cancel" /></a>
            </form>
        
        </@compress>
    </#assign>
    
    <#-- Confirm decline invitation for NOSCRIPT-users -->
    <#if RequestParameters['invitation-decline']?? && RequestParameters['invitation-decline'] == "confirm">
        <noscript>
            <div class="msg-block">
                <h2><@u.message "invitation.decline.confirm.title" /></h2>
               <#noescape>${invitationDeclineConfirmHtml}</#noescape>
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
        
            <@u.systemMessage path="invitation.accept.description" type="info" showClose=false />
        
            <#--<@u.errorsSummary path="currentAuthor.*" prefix="initiative.currentAuthor."/>-->

            <form action="${springMacroRequestContext.requestUri}" method="POST" >
                <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>
                <#--<input type="hidden" name="invitation" value="${invitation.invitationCode!""}"/>-->
                <input type="hidden" name="action" value="confirm-accept-invitation"/>
            
                    <div class="column col-1of2">
                        <@f.textField path="authorInvitation.name" required="required" optional=false cssClass="medium" maxLength=InitiativeConstants.CONTACT_NAME_MAX  key="contactInfo.name" />
                    </div>
                    
                    <div class="column col-1of2 last">
                        <#--<@f.municipalitySelect path="authorInvitation.initiativeMunicipality" options=municipalities required="required" cssClass="municipality-select" />-->
                    
                        <label for="municipality" class="input-header">
                            Valitse kotikuntasi <span class="icon-small required trigger-tooltip"></span>
                        </label>
                        <select name="municipality" id="municipality"  class="chzn-select municipality-select" data-initiative-municipality="" data-placeholder="Valitse kunta" >
                            <option value=""></option>
                                <option value="1">Helsinki</option>
                                <option value="4">Hämeenlinna</option>
                                <option value="6">Kirkkonummi</option>
                                <option value="2">Tampere</option>
                                <option value="7">Turku</option>
                                <option value="3">Tuusula</option>
                                <option value="5">Vantaa</option>
                        </select>
                    </div>
                    <br class="clear" />
                
                    <div class="initiative-contact-details">
                        <div class="column col-1of2">
                            <#--<@f.textField path="authorInvitation.email" required="required" optional=false cssClass="medium"  maxLength=InitiativeConstants.CONTACT_EMAIL_MAX key="contactInfo.email" />-->
                            <@f.textField path="authorInvitation.phone" required="" optional=false cssClass="medium"  maxLength=InitiativeConstants.CONTACT_PHONE_MAX key="contactInfo.phone" />
                        </div>
                        
                        <div class="column col-1of2 last">
                            <label>
                                <@u.message "contactInfo.address" />
                                <@spring.formTextarea "authorInvitation.address", 'class="address-field noresize" maxlength="'+InitiativeConstants.CONTACT_ADDRESS_MAX+'"' />
                            </label>
                        </div>
                    
                    </div>
                
                <#--<#assign href>${urls.help(HelpPage.ORGANIZERS.getUri(locale))}</#assign>
                <p><@u.messageHTML key="userConfirmation.invitation" args=[href] /></p>-->
                
                <button type="submit" name="" value="<@u.message "invitation.accept.confirm" />" class="small-button green save-and-send"><span class="small-icon save-and-send"><@u.message "invitation.accept" /></span></button>
                <a href="${springMacroRequestContext.requestUri}" class="push close"><@u.message "action.cancel" /></a>
            </form>                    
        </@compress>
    </#assign>
    
    <#if RequestParameters['invitation-accept']?? && RequestParameters['invitation-accept'] == "confirm">
        <div class="msg-block">
            <h2><@u.message "invitation.accept.confirm.title" /></h2>
            <#noescape>${invitationAcceptHtml}</#noescape>
        </div>
    </#if>


    <h1 class="name">${initiative.name!""}</h1>
    
    <div class="municipality">${initiative.municipality.getName(locale)}</div>
    
    <@e.stateInfo initiative />

    <#-- VIEW BLOCKS -->
    <div class="view-block public first">
        <div class="initiative-content-row">
            <@e.initiativeView initiative />
        </div>
        <#-- TODO: Show contact info here -->
        <div class="initiative-content-row last">
            <@e.initiativeAuthor initiative />
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
     *  Confirm decline invitation
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
        <#if invitationDeclineConfirmHtml??>    
            modalData.confirmDeclineInvitation = function() {
                return [{
                    title:      '<@u.message "invitation.decline.confirm.title" />',
                    content:    '<#noescape>${invitationDeclineConfirmHtml?replace("'","&#39;")}</#noescape>'
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