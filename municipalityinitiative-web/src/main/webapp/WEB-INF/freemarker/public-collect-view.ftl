<#import "/spring.ftl" as spring />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/elements.ftl" as e />
<#import "components/progress.ftl" as prog />
<#import "components/forms.ftl" as f />
<#import "components/edit-blocks.ftl" as edit />
<#import "components/some.ftl" as some />
<#import "components/mobile-components.ftl" as mobile />

<#escape x as x?html>

<#-- For verifiable initiatives when user returns from VETUMA -->
<#assign showNotAllowedToParticipate = user.isVerifiedUser() && !user.allowVerifiedParticipation(initiative.id, initiative.municipality) &&
     initiative.verifiable && RequestParameters['show-participate']?? />

<#assign notAllowedToParticipateHTML>
    <@compress single_line=true>
    	<#if user.hasParticipatedToInitiative(initiative.id)>
        	<@u.systemMessage path="warning.already.participated" type="warning" />
       	<#else>
       		<@u.systemMessage path="warning.participant.notMember" type="warning" />
        </#if>

        <div class="input-block-content">
            <a href="${urls.logout()}" class="small-button"><span class="small-icon logout"><@u.message "common.logout" /></span></a><a href="${springMacroRequestContext.requestUri}" class="small-button push"><@u.message "modal.continueBrowsing" /></a>
        </div>
    </@compress>
</#assign>


<#--
 * Layout parameters for HTML-title and navigation.
 * 
 * page = "page.initiative.public" or "page.initiative.unnamed"
 * pageTitle = initiative.name if exists, otherwise empty string
-->
<@l.main "page.initiative.public" initiative.name!"">

    <#if showNotAllowedToParticipate>
        <noscript>
            <div class="msg-block cf">
                <#noescape>${notAllowedToParticipateHTML!""}</#noescape>
            </div>
        </noscript>
    </#if>

    <#if user.hasRightToInitiative(initiative.id) && !initiative.sent>
        <@u.returnPrevious urls.management(initiative.id) "link.to.managementView" />
    <#else>
        <@u.returnPrevious url=urls.search() labelKey="link.to.searchView" useJsBack=true />
    </#if>

    <@e.initiativeTitle initiative />

    <@prog.progress initiative />

    <#if decisionInfo.isPresent() >
        <@e.decisionBlock decisionInfo.getValue() />
    </#if>

    <#-- VIEW BLOCKS -->
    <div class="view-block public first">

        <@e.initiativeView initiative />
    </div>

    <#assign contactAuthorForm>
    <@compress single_line=true>

        <@u.errorsSummary path="authorMessage.*" prefix="authorMessage."/>

        <#-- Do not use NOSCRIPT here as it will be descendant of another NOSCRIPT. -->
        <div class="js-hide">
            <@f.cookieWarning springMacroRequestContext.requestUri />
        </div>

        <form action="${springMacroRequestContext.requestUri}?formError=contactAuthor" method="POST" id="form-contact-author" class="sodirty dirtylisten js-validate <#if hasErrors>has-errors</#if>" novalidate>
            <@f.securityFilters/>

            <div class="input-block-content no-top-margin">
                <@u.systemMessage path="contactAuthor.description" args=[authors.publicNameCount+authors.privateNameCount] type="info" />
            </div>

            <div class="input-block-content">
                <p><@f.fieldRequiredInfo /></p>
                <@f.textarea path="authorMessage.message" required="required" optional=false cssClass="large" maxLength=InitiativeConstants.AUTHOR_MESSAGE_MAX?string("#") />
            </div>

             <div class="input-block-content">
                <@f.textField path="authorMessage.contactName" required="required" optional=false cssClass="large"  maxLength=InitiativeConstants.CONTACT_NAME_MAX />
            </div>

            <div class="input-block-content">
                <@f.textField path="authorMessage.contactEmail" required="required" optional=true cssClass="large" attributes='data-type="email"' maxLength=InitiativeConstants.CONTACT_EMAIL_MAX />
            </div>

            <div class="input-block-content">
                <button id="contact-author" type="submit" name="${UrlConstants.ACTION_CONTACT_AUTHOR}" value="true" class="small-button"><span class="small-icon mail"><@u.message "action.sendMessage" /></span></button>
                <a href="${springMacroRequestContext.requestUri}" class="push close"><@u.message "action.cancel" /></a>
            </div>

        </form>

    </@compress>
    </#assign>

    <#assign followInitiativeFormHTML>
        <@compress single_line=true>
            <#if initiative.isSent()>
                <@u.message "followSent.text" />
            <#else>
                <@u.message "follow.text" />
            </#if>
            <form action="${springMacroRequestContext.requestUri}?formError=follow" id="follow-form" method="POST">
                <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>

                <div class="input-block-content">
                    <@f.textField path="followInitiative.participantEmail" required="" optional=true cssClass="large" maxLength=InitiativeConstants.CONTACT_NAME_MAX />
                </div>
                <div class="input-block-content">
                    <button id="participate" type="submit"  value="true" name="action-follow" class="small-button"><span class="small-icon save-and-send"><@u.message "action.save" /></span></button>
                    <a href="${springMacroRequestContext.requestUri}" class="push close"><@u.message "action.cancel" /></a>
                </div>
            </form>
        </@compress>
    </#assign>

    <#assign participateFormHTML>
    <@compress single_line=true>


    <#if !user.isVerifiedUser()>
        <div class="input-block-content no-top-margin">
            <@u.systemMessage path="authentication.selection.description" type="info" />
        </div>
    </#if>

    <div class="authentication-selection">

        <#assign formSelectionVisible=user.isVerifiedUser() || RequestParameters['formError']??/>

        <#if !user.isVerifiedUser()>
            <label id="vetuma-authentication-button" class="authentication verified <#if !formSelectionVisible>selected</#if>"><@u.message "authentication.selection.verified" /></label>
            <label id="email-authentication-button" class="authentication email <#if formSelectionVisible>selected</#if>"><@u.message "authentication.selection.email"/></label>
        </#if>

        <div class="participation-vetuma-login-container" <#if formSelectionVisible>style="display:none"</#if>

            <form class="sodirty dirtylisten js-validate">
                <div class="input-block-content no-top-margin">
                    <@u.systemMessage path="authentication.selection.verified.description" type="info" />
                </div>
            </form>

            <a class="small-button" href="${urls.login(currentRequestUri+"?show-participate")}"><span class="small-icon save-and-send"><@u.message "action.authenticate" /></span></a>
        </div>

        <div class="participation-authentication-container" <#if !formSelectionVisible>style="display:none"</#if>>

        <#-- Participate form errors summary -->
        <@u.errorsSummary path="participant.*" prefix="participant."/>

        <#-- Do not use NOSCRIPT here as it will be descendant of another NOSCRIPT. -->
        <div class="js-hide">
            <@f.cookieWarning springMacroRequestContext.requestUri />
        </div>

        <form action="${springMacroRequestContext.requestUri}?formError=participate" method="POST" id="form-participate" class="sodirty dirtylisten js-validate <#if hasErrors>has-errors</#if>" novalidate>
            <@f.securityFilters/>
            <@f.notTooFastField participant/>

            <#if user.isVerifiedUser() && user.homeMunicipality.present>
                <#assign infoKeyPostfix = ".verified" />
            <#elseif initiative.verifiable && user.isVerifiedUser() && !user.homeMunicipality.present>
                <#assign infoKeyPostfix = ".noMunicipality" />
            <#elseif user.isVerifiedUser() && !user.homeMunicipality.present>
                <#assign infoKeyPostfix = ".noMunicipality.normalInitiative" />
            <#else>
                <#assign infoKeyPostfix = "" />
            </#if>

            <div class="input-block-content no-top-margin">
                <@u.systemMessage path="participate.contactInfo.description"+infoKeyPostfix type="info" />
            </div>

             <div class="input-block-content">
                <p><@f.fieldRequiredInfo /></p>
                <div class="column col-1of2">
                    <#if user.isVerifiedUser()>
                         <div class="input-header"><@u.message "contactInfo.verified.name" /></div>
                         <div class="input-placeholder">${user.contactInfo.name}</div>
                     <#else>
                        <@f.textField path="participant.participantName" required="required" optional=false cssClass="large" maxLength=InitiativeConstants.CONTACT_NAME_MAX />
                     </#if>
                </div>


                 <#assign selectedHomeMunicipalitySameAsInitiatives = !participant.homeMunicipality?? || initiative.municipality.id == participant.homeMunicipality/>
                 <#assign hasVerifiedMunicipality = (user.isVerifiedUser() && user.homeMunicipality.present)/>

                 <#if !hasVerifiedMunicipality>
                     <div class="column col-1of2" id="participation-criterion">
                         <label>
                             <input type="radio" name="participation-criterion" value="same-municipality" <#if selectedHomeMunicipalitySameAsInitiatives>checked</#if>/>
                             <@u.message "initiative.sameMunicipality" />
                         </label>
                         <label>
                             <input type="radio" name="participation-criterion" value="other-municipality" <#if !selectedHomeMunicipalitySameAsInitiatives>checked</#if>/>
                             <@u.message "initiative.otherMunicipality" />
                         </label>
                     </div>

                     <div class="column col-1of2 <#if selectedHomeMunicipalitySameAsInitiatives>hide<#else>show</#if>" id="home-municipality-select">
                         <@f.municipalitySelect path="participant.homeMunicipality" options=municipalities required="required" cssClass="municipality-select" preSelected=initiative.municipality.id multiple=false id="homeMunicipality"/>
                     </div>
                 <#else>
                     <div class="column col-1of2 last">
                         <div class="input-header"><@u.message "contactInfo.homeMunicipality" /></div>
                         <div class="input-placeholder"><@u.solveMunicipality user.homeMunicipality/></div>
                     </div>
                 </#if>

            </div>

            <div class="input-block-content cf">
                <div id="municipalMembership" class="municipality-not-equal js-hide">
                    <#if !initiative.verifiable>
                        <div class="input-block-content hidden">
                            <#assign href=urls.help(HelpPage.PARTICIPANTS.getUri(locale)) />
                        <@u.systemMessage path="initiative.municipality.notEqual.participation" type="info" args=[href] />
                        </div>
                        <div class="input-block-content">
                            <@f.radiobutton path="participant.municipalMembership" required="required" options={
                            "community":"initiative.municipalMembership.community",
                            "company":"initiative.municipalMembership.company",
                            "property":"initiative.municipalMembership.property",
                            "none":"initiative.municipalMembership.none"
                            } attributes="" />
                        </div>
                    </#if>

                    <div class="input-block-content <#if !initiative.verifiable>is-not-member no-top-margin js-hide </#if> hidden">
                        <@u.systemMessage path="warning.participant.notMember" type="warning" />
                    </div>

                </div>
            </div>


            <div class="input-block-content">
                <@f.formCheckbox path="participant.showName" checked=true />
            </div>

            <#if user.isVerifiedUser()>

            <#else>
                <div class="input-block-content">
                    <@f.textField path="participant.participantEmail" required="required" optional=true cssClass="large" attributes='data-type="email"' maxLength=InitiativeConstants.CONTACT_EMAIL_MAX />
                </div>
            </#if>

            <div class="input-block-content">
                <#if user.isVerifiedUser()>
                    <button id="participate" type="submit" name="save" value="true" class="small-button"><span class="small-icon save-and-send"><@u.message "action.save" /></span></button>
                <#else>
                    <@u.systemMessage type="warning" path="participate.confirmation.notification"/>
                    <br/>
                    <button id="participate" type="submit" name="save" value="true" class="small-button"><span class="small-icon save-and-send"><@u.message "action.send.confirmation" /></span></button>
                </#if>

                <a href="${springMacroRequestContext.requestUri}" class="push close"><@u.message "action.cancel" /></a>
            </div>

        </form>

        </div>

    </div>

    </@compress>
    </#assign>

    <div id = "authors" class="view-block public">
        <h2><@u.message key="initiative.authors.title" args=[authors.getPublicNameCount() + authors.getPrivateNameCount()] /></h2>
        <div class="initiative-content-row last">
            <@e.initiativeAuthor authors />

            <#if initiative.state == InitiativeState.PUBLISHED && !initiative.sentTime.present>
                <p class="noprint"><a href="?contactAuthorForm=true#form-contact-author" class="js-contact-author"><span class="icon-small icon-16 envelope margin-right"></span> <@u.message key="contactAuthor.link" args=[authors.publicNameCount+authors.privateNameCount] /></a></p>

                <#if (RequestParameters['formError']?? && RequestParameters['formError'] == "contactAuthor")
                || (RequestParameters['contactAuthorForm']?? && RequestParameters['contactAuthorForm'] == "true")>
                    <noscript>
                        <div id="form-contact-author" class="form-container cf top-margin">
                            <h3><@u.message key="contactAuthor.title" args=[authors.publicNameCount+authors.privateNameCount] /></h3>
                            <#noescape>${contactAuthorForm}</#noescape>
                        </div>
                    </noscript>
                </#if>
            </#if>
        </div>

    </div>
    <#assign canFollow = ( initiative.state == InitiativeState.PUBLISHED && !initiative.decisionDate.present && followEnabled) />
    <#assign showFollowForm = canFollow && ((RequestParameters['formError']?? && RequestParameters['formError'] == "follow") || (RequestParameters['follow']?? && RequestParameters['follow'] == "true"))/>

    <#if initiative.state == InitiativeState.PUBLISHED>
        <div id="participants" class="view-block public participants">
            <h2><@u.message key="initiative.participation.title"/></h2>

        <#--
         * Do NOT show participate button:
         *  - when modal request message is showed
         *  - when participate form is showed (RequestParameter for NOSCRIPT)
         *  - when the form has validation errors
         *  - when sent to municipality (initiative.sentTime.present)
        -->
            <#assign showParticipateForm = (RequestParameters['formError']?? && RequestParameters['formError'] == "participate")
            || (RequestParameters['participateForm']?? && RequestParameters['participateForm'] == "true") || RequestParameters['show-participate']?? />

        <#--
         * Show participant counts and participate form
         *
         * - Hide when not published. OM sees this view in REVIEW state.
        -->



        <div class="initiative-content-row no-bottom-margin">
            <@e.participants formHTML=participateFormHTML showForm=showParticipateForm />
        </div>

        <#if canFollow>
            <div class="initiative-content-row last">
                <@e.follow />
            </div>
            <#noescape><noscript>
                <#if showFollowForm>
                    <div id="follow-form" class="form-container cf top-margin">
                        ${followInitiativeFormHTML!""}
                    </div>
                </#if>
            </noscript></#noescape>
        </#if>

        </div>
    </#if>




    <@mobile.participantsBlock participantCount/>


    <@e.participantInfo />

    <#if user.hasRightToInitiative(initiative.id) && !initiative.sent>
        <@u.returnPrevious urls.management(initiative.id) "link.to.managementView" />
    <#else>
        <@u.returnPrevious url=urls.search() labelKey="link.to.searchView" useJsBack=true />
    </#if>

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


            var userMunicipalityVerifiedByVetuma = false;
            var userMunicipalityMatchesInitiativeMunicipality = false;

            <#if user.isVerifiedUser() && user.homeMunicipality?? && user.homeMunicipality.isPresent()>
                userMunicipalityVerifiedByVetuma = true;
                <#if user.homeMunicipality.value.id == initiative.municipality.id>
                    userMunicipalityMatchesInitiativeMunicipality = true;
                </#if>
            </#if>

            <#-- Autoload modal if it has errors or returned from VETUMA and user is allowed to participate -->
            <#if RequestParameters['show-participate']?? ||
                 RequestParameters['formError']?? && RequestParameters['formError'] == "participate">
            modalData.participateFormAutoLoad = function() {
                return [{
                    title:      '<@u.message "participate.title" />',
                    content:    '<#noescape>${participateFormHTML?replace("'","&#39;")}</#noescape>'
                }]
            };
            </#if>
            <#-- Autoload modal with warning if user is returned from VETUMA and user is NOT allowed to participate -->
            <#if showNotAllowedToParticipate>
            modalData.participateFormAutoLoad = function() {
                return [{
                    title:      '<@u.message "participate.title" />',
                    content:    '<#noescape>${notAllowedToParticipateHTML?replace("'","&#39;")}</#noescape>'
                }]
            };
            </#if>

            var messageData = {};
        </#if>

        <#-- Modal: Participate initiative -->
        modalData.contactAuthor = function() {
            return [{
                title:      '<@u.message key="contactAuthor.title" args=[authors.publicNameCount+authors.privateNameCount] />',
                content:    '<#noescape>${contactAuthorForm?replace("'","&#39;")}</#noescape>'
            }]
        };

        <#-- Autoload modal if it has errors -->
        <#if RequestParameters['formError']?? && RequestParameters['formError'] == "contactAuthor">
        modalData.contactAuthorFormAutoLoad = function() {
            return [{
                title:      '<@u.message key="contactAuthor.title" args=[authors.publicNameCount+authors.privateNameCount] />',
                content:    '<#noescape>${contactAuthorForm?replace("'","&#39;")}</#noescape>'
            }]
        };
        </#if>

        <#-- jsMessage: Warning if cookies are not enabled -->
        messageData.warningCookiesDisabled = function() {
            return [{
                type:      'warning',
                content:    '<h3><@u.message "warning.cookieError.title" /></h3><div><@u.messageHTML key="warning.cookieError.description" args=[springMacroRequestContext.requestUri] /></div>'
            }]
        };

        modalData.followInitiative = function(){
            return [{
                title: '<@u.message "followInitiative.title"/>',
                content: '<#noescape>${followInitiativeFormHTML?replace("'","&#39;")}</#noescape>'
            }]
        };

        <#if showFollowForm>
            modalData.followFormAutoLoad = function() {
                return [{
                    title:     '<@u.message "followInitiative.title"/>',
                    content:   '<#noescape>${followInitiativeFormHTML?replace("'","&#39;")}</#noescape>'
                }]
            };
        </#if>

    </script>

</@l.main>

</#escape> 