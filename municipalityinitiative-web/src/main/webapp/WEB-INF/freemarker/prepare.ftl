<#import "/spring.ftl" as spring />
<#import "components/forms.ftl" as f />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/edit-blocks.ftl" as edit />

<#escape x as x?html>

<#--
 * Layout parameters for HTML-title and navigation.
 *
 * @param page is "page.prepare"
 * @param pageTitle can be assigned as custom HTML title
-->
<#assign page="page.prepare" />

<#--
 * Get current municipality as request parameter.
 * - Request parameter is for iFrame create-link
 * - We could want to try to guess user's municipality later on with different methods like GEO location, user's history etc..
-->
<#assign currentMunicipality = RequestParameters['municipality']!"" />

<@l.main page pageTitle!"">

    <h1><@u.message page /></h1>

    <#-- Create form errors summary -->
    <@u.errorsSummary path="initiative.*" prefix="initiative."/>

    <noscript>
        <@f.cookieWarning springMacroRequestContext.requestUri />
    </noscript>

    <#-- FORM. Use class 'sodirty' to enable dirtylisten. -->
    <form action="${springMacroRequestContext.requestUri}" method="POST" id="form-preparation" class="js-validate <#if hasErrors>has-errors</#if>" novalidate data-verified="${user.isVerifiedUser()?c}">

        <@f.securityFilters />
        <@f.notTooFastField initiative />

        <div class="form-block-container">
            <#if !user.isVerifiedUser()>
            <div class="input-block cf prepare-auth" >

                <div class="form-introduction">
                    <@u.message "prepare.form.introduction"/>
                </div>

                <div class="input-block-extra">
                    <div class="input-block-extra-content">
                        <@f.helpText "help.authenticationSelection" />
                    </div>

                </div>

                <div class="input-block-content">

                    <div class="authentication-selection">

                        <#assign formSelectionVisible=initiative.participantEmail?? && initiative.participantEmail?length != 0/>

                        <#if !user.isVerifiedUser()>
                            <label id="vetuma-authentication-button" class="authentication verified <#if !formSelectionVisible>selected</#if>"><@u.message "authentication.selection.verified" /></label>
                            <label id="email-authentication-button" class="authentication email <#if formSelectionVisible>selected</#if>"><@u.message "authentication.selection.email"/></label>
                        </#if>

                            <div class="participation-vetuma-login-container" <#if formSelectionVisible>style="display:none"</#if>

                        <form class="sodirty dirtylisten js-validate">
                            <div class="input-block-content">
                                <@u.systemMessage path="authentication.selection.verified.description" type="info" />
                            </div>
                        </form>
                        <div class="input-block-content">
                            <@edit.buttons type="verify" class="small-button"/>
                        </div>

                    </div>


                    <div id="email-auth-container" class="participation-authentication-container" <#if !formSelectionVisible>style="display:none"</#if>>


                        <@edit.authorEmailBlock />

                        <div class="input-block-content no-top-margin">
                            <@edit.buttons type="continue" class="small-button email-auth-btn"/>
                        </div>

                    </div>

                </div>

            </div>
            </#if>

        </div>

        <div class="prepare-content <#if !user.isVerifiedUser()>hide</#if>">
            <div class="form-block-container">
                <div class="input-block cf" id="prepare-municipality-selection">
                    <@edit.municipalityBlock municipality=currentMunicipality/>
                </div>
            </div>

            <div class="form-block-container toggle-disable" id="initiative-type">
                <div class="input-block no-sidebar cf">
                    <@edit.chooseInitiativeType />
                </div>
            </div>
            <div class="form-block-container input-block">
                <div class="input-block-content float-init">
                    <div class="toggle-disable-send mask-div"><@edit.buttons type="send" class="small-button" /></div>
                </div>
            </div>
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
