<#import "/spring.ftl" as spring />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/forms.ftl" as f />
<#import "components/elements.ftl" as e />
<#import "components/progress.ftl" as prog />
<#escape x as x?html>

<#assign moderationURL = urls.moderation(initiative.id) />

<#--
 * Layout parameters for HTML-title and navigation.
 *
 * page = "page.moderation"
 * pageTitle = initiative.name if exists, otherwise empty string
-->

<@l.main page="page.moderation" pageTitle=initiative.name!"">

    <#if decisionInfo.isPresent() && !showDecisionForm>
        <div class="msg-block cf">
            <h2>Kunnan vastaus on julkaistu <@u.localDate decisionInfo.getValue().getDate() /></h2>
            <p>Voit muokata kunnan vastausta. Kaikki muokkaukset ovat julkisia.</p>

        </div>
    </#if>

    <#--
      * Municipality decision form
    -->
    <#if showDecisionForm>
        <div class="msg-block cf">
            <#if decisionInfo.isPresent()>
                <h2><@u.message "municipality.decision.editDecision" /></h2>
            <#else>
                <h2><@u.message "municipality.decision.giveDecision" /></h2>
            </#if>
            <p class="full-width"><@u.message key="municipality.decision.description" /></p>
            <div>
                <form action="${urls.getMunicipalityDecisionView(initiative.id)}" method="POST" id="form-accept" class="sodirty" enctype="multipart/form-data">
                    <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>

                    <@spring.bind "decision" />

                    <@u.errorsSummary path="decision.*" prefix="decision."/>

                    <div class="input-block-content no-top-margin full-width">
                        <@f.textarea  required="" optional=false path="decision.description" key="decision.description"/>
                    </div>

                    <input type="hidden" name="locale" value="${locale}"/>

                    <div class="input-block-content">
                        <@f.uploadField path="decision.files[0].file" cssClass="multi" name="files[0].file" multiple=false/>
                    </div>

                    <div class="input-block-content">
                        <button type="submit" class="small-button"><span class="small-icon save-and-send"><@u.message "decision.submit" /></span></button>
                        <a class="small-button" href="${urls.getMunicipalityDecisionView(initiative.id)}">Poistu</a>
                    </div>

                    <br/><br/>
                </form>
            </div>
        </div>

    </#if>

    <#if editAttachments>
        <div class="msg-block cf">
            <h2><@u.message "municipality.decision.removeAttachments" /></h2>
            <p>Voit poistaa liitteitä klikkaamalla raksia</p>
            <@e.attachmentsView attachments=decisionInfo.getValue().attachments manage=true municipality=true/>
        </div>
    </#if>

    <#if decisionInfo.isPresent()>
        <@e.decisionBlock decisionInfo=decisionInfo.getValue() manage=true/>
    </#if>




    <@e.initiativeTitle initiative />

    <@prog.progress initiative=initiative public=false omOrMunicipality=true />


    <div class="view-block first">
        <@e.initiativeView initiative />
    </div>

    <div class="view-block">
        <div class="initiative-content-row last">
            <h2><@u.message key="initiative.people.title" args=[authors?size] /></h2>

            <@e.initiativeContactInfo authorList=authors showRenewManagementHash=false/>
        </div>
    </div>

    <#assign deleteAattachment>
        <@compress single_line=true>
            <@e.deleteAattachmentForm />
        </@compress>
    </#assign>



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

        <#-- Modal: Confirm remove attachment -->
        modalData.deleteAttachment = function() {
            return [{
                title:      '<@u.message "deleteAttachment.confirm.title" />',
                content:    '<#noescape>${deleteAattachment?replace("'","&#39;")}</#noescape>'
            }]
        };


    </script>

</@l.main>

</#escape>