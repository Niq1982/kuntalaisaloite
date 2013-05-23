<#import "/spring.ftl" as spring />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/elements.ftl" as e />
<#import "components/forms.ftl" as f />
<#import "components/edit-blocks.ftl" as edit />
<#import "components/some.ftl" as some />

<#escape x as x?html> 

<#--
 * Layout parameters for HTML-title and navigation.
 * 
 * page = "page.initiative.public" or "page.initiative.unnamed"
 * pageTitle = initiative.name if exists, otherwise empty string
-->
<@l.main "page.initiative.public" initiative.name!"">

    <h1 class="name">${initiative.name!""}</h1>
    
    <div class="municipality">${initiative.municipality.getName(locale)}</div>
    
    <#-- TODO: should not be visible if initiative is sent to municipality -->
    <#if hasManagementRightForInitiative>
        <a style="float:right;" href="${urls.management(initiative.id)}">Siirry hallintasivulle</a>
    </#if>
    
    <@e.stateInfo initiative />

    <#-- VIEW BLOCKS -->
    <div class="view-block public first">
        <div class="initiative-content-row last">
            <@e.initiativeView initiative />
        </div>
    </div>
    
    <#assign participateFormHTML>
    <@compress single_line=true>
    
        <#-- Participate form errors summary -->    
        <@u.errorsSummary path="participant.*" prefix="participant."/>
    
        <#-- Do not use NOSCRIPT here as it will be descendant of another NOSCRIPT. -->
        <div class="js-hide">
            <@f.cookieWarning springMacroRequestContext.requestUri />
        </div>
        
        <form action="${springMacroRequestContext.requestUri}" method="POST" id="form-participate" class="sodirty <#if hasErrors>has-errors</#if>">
            <@f.securityFilters/>
            <@f.notTooFastField participant/>
    
            <input type="hidden" name="municipality" value="${initiative.municipality.id!""}"/>

            <div class="input-block-content no-top-margin">
                <@u.systemMessage path="participate.contactInfo.description" type="info" showClose=false />  
            </div>
            
             <div class="input-block-content">
                <@f.textField path="participant.participantName" required="required" optional=false cssClass="large" maxLength="512" />
                
            </div>
            
            <div class="input-block-content">
                <@f.municipalitySelect path="participant.homeMunicipality" options=municipalities required="required" cssClass="municipality-select" preSelected=initiative.municipality.id />
            </div>
            
            <div id="municipalMembership" class="js-hide">
                <div class="input-block-content hidden">
                    <#assign href="#" />
                    <@u.systemMessage path="initiative.municipality.notEqual" type="info" showClose=false args=[href] />
                </div>
                <div class="input-block-content">
                    <@f.radiobutton path="participant.municipalMembership" required="required" options={
                        "community":"initiative.municipalMembership.community",
                        "company":"initiative.municipalMembership.company",
                        "property":"initiative.municipalMembership.property",
                        "none":"initiative.municipalMembership.none"
                    } attributes="" />
                </div>
                
                <div class="input-block-content is-not-member no-top-margin js-hide hidden">
                    <@u.systemMessage path="warning.initiative.notMember" type="warning" showClose=false />
                </div>
            </div>
            
            <div class="input-block-content">
                <@f.formCheckbox path="participant.showName" checked=true />
            </div>
            
            <div class="input-block-content">
                <@f.textField path="participant.participantEmail" required="required" optional=true cssClass="large" maxLength=InitiativeConstants.CONTACT_EMAIL_MAX />
            </div>

            <div class="input-block-content">
                <button id="participate" type="submit" name="save" value="true" class="small-button"><span class="small-icon save-and-send"><@u.message "action.save" /></span></button>
                <a href="${springMacroRequestContext.requestUri}" class="push close"><@u.message "action.cancel" /></a>
            </div>
        
        </form>
    
    </@compress>
    </#assign>
    
    <div id="participants" class="view-block public last">
        <h2><@u.message key="initiative.people.title" args=[authors.publicNames+authors.privateNames+participantCount.total] /></h2>
    
        <div class="initiative-content-row">
            <@e.initiativeAuthor authors />
        </div>
        
        <#--
         * Do NOT show participate button:
         *  - when modal request message is showed
         *  - when participate form is showed (RequestParameter for NOSCRIPT)
         *  - when the form has validation errors
         *  - when sent to municipality (initiative.sentTime.present)
        -->
        <#assign showParticipateForm = (hasErrors?? && hasErrors) || (RequestParameters['participateForm']?? && RequestParameters['participateForm'] == "true") />
        
        <#--
         * Show participant counts and participate form
        -->
        <div class="initiative-content-row last">
            <@e.participants formHTML=participateFormHTML showForm=showParticipateForm />
        </div>
        
    </div>
    
    <#--
        <div class="initiative-content-row last">

            <h3><@u.message key="initiative.participants.title" args=[participantCount.total] /></h3>
            
            <div class="participants-block">
                <span class="user-count-total">${participantCount.total!""}</span>
            </div>
            <div class="participants-block separate">
                <span class="user-count-sub-total">
                    <#if (participantCount.publicNames > 0)><span class="public-names"><a class="trigger-tooltip" href="${urls.participantList(initiative.id)}" title="<@u.message key="participantCount.publicNames.show"/>"><@u.message key="participantCount.publicNames" args=[participantCount.publicNames] /></a></span><br/></#if>
                    <#if (participantCount.privateNames > 0)><span class="private-names"><@u.message key="participantCount.privateNames" args=[participantCount.privateNames] /></span></p></#if>
                </span>
            </div>
            
            <#if !initiative.sentTime.present && requestMessages?? && !(requestMessages?size > 0) && !showParticipateForm>
                <div class="participants-block">
                    <a class="small-button js-participate" href="?participateForm=true#participate-form"><span class="small-icon save-and-send"><@u.message "action.participate" /></span></a>
                </div>
                <div class="participants-block last">
                    <a title="<@u.messageHTML "action.participate.infoLink.title" />" href="#"><@u.messageHTML "action.participate.infoLink" /></a>
                </div>
            </#if>
            <#if initiative.sentTime.present>
                <div class="participants-block last">
                    <div class="participate not-allowed">
                        <@u.systemMessage path="participate.sentToMunicipality" type="info" showClose=false />
                    </div>
                </div>
            </#if>
            <br class="clear" />

            <#if showParticipateForm>
                <#noescape><noscript>
                    <div id="participate-form" class="participate-form cf top-margin">
                        <h3><@u.message "participate.title" /></h3>
                        ${participateFormHTML!""}
                    </div>
                </noscript></#noescape>
            </#if>
        </div>     
    </div>
    -->

    <#--
     * Social media buttons
    -->
    <#if initiative.state == InitiativeState.PUBLISHED>
        <@some.some pageTitle=initiative.name!"" />
    </#if>

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
    <#-- TODO: Check that what is needed here as there is nomore management -->
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
    
        <#-- Modal: Participate initiative -->
        <#if participateFormHTML??>    
            modalData.participateForm = function() {
                return [{
                    title:      '<@u.message "participate.title" />',
                    content:    '<#noescape>${participateFormHTML?replace("'","&#39;")}</#noescape>'
                }]
            };
            
            <#-- Autoload modal if it has errors -->
            <#if hasErrors>
            modalData.participateFormInvalid = function() {
                return [{
                    title:      '<@u.message "participate.title" />',
                    content:    '<#noescape>${participateFormHTML?replace("'","&#39;")}</#noescape>'
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
        </#if>
    </script>

</@l.main>

</#escape> 