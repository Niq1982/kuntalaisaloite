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

    <#if decisionInfo.isPresent() && !showDecisionForm && !editAttachments>
        <div class="msg-block cf">
            <h2>Kunnan vastaus on julkaistu <@u.localDate decisionInfo.getValue().getDate() />
                <#if decisionInfo.getValue().getModifiedDate().isPresent()>
                    (muokattu <@u.localDate decisionInfo.getValue().getModifiedDate().value />)
                </#if>
            </h2>
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

                    <#if !decisionInfo.isPresent()>
                        <div class="input-block-content">
                            <@f.uploadField path="decision.files[0].file" cssClass="multi" name="files[0].file" multiple=false/>
                        </div>
                    </#if>

                    <#if decisionInfo.isPresent()>
                        <div class="input-block-content">
                            <button type="submit" class="small-button"><span class="small-icon save-and-send"><@u.message "decision.edit.submit" /></span></button>
                            <a class="small-button" href="${urls.getMunicipalityDecisionView(initiative.id)}"><@u.message "decision.edit.cancel" /></a>
                        </div>
                    <#else>
                        <div class="input-block-content">
                            <button type="submit" class="small-button"><span class="small-icon save-and-send"><@u.message "decision.submit" /></span></button>
                        </div>
                    </#if>

                    <br/><br/>
                </form>
            </div>
        </div>

    </#if>

    <#if editAttachments>
        <div class="msg-block cf">
            <h2><@u.message "decision.edit.attachments" /></h2>
            <@e.municipalityAttachmentsView attachments=decisionInfo.getValue().attachments manage=true/>
            <form action="${urls.openDecisionAttachmentsForEdit(initiative.id)}" method="POST" id="form-accept" class="sodirty" enctype="multipart/form-data">
                <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>
                <@spring.bind "decision" />
                <@u.errorsSummary path="decision.*" prefix="decision."/>
                <div class="input-block-content">
                    <@f.uploadField path="decision.files[0].file" cssClass="multi" name="files[0].file" multiple=false/>
                    <br>
                    <button type="submit" class="small-button"><span class="small-icon save-and-send"><@u.message "decision.upload.attachments"/></span></button>
                    <a class="small-button" href="${urls.getMunicipalityDecisionView(initiative.id)}"><@u.message "decision.edit.cancel" /></a>
                </div>

            </form>


        </div>
    </#if>

    <#if decisionInfo.isPresent() && !showDecisionForm && !editAttachments>
        <@e.decisionBlock decisionInfo=decisionInfo.getValue() manage=true/>
    </#if>

    <@e.initiativeTitle initiative />

    <@prog.progress initiative=initiative public=false omOrMunicipality=true />

    <br class="cf"/>

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