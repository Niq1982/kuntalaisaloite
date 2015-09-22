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

    <#--
     * Show decision
    -->
    <#if decisionInfo.isPresent()>
        <div class="view-block cf">
            <div class="initiative-content-row last">
                <h2><@u.message "municipality.decision" /></h2>
                <p>${decisionInfo.getValue().getDecisionText()}</p>
                <ul>
                <#list decisionInfo.getValue().getAttachments() as attachment>
                       <li>${attachment.fileName}</li>
                </#list>
                </ul>
            </div>
        </div>
    </#if>
    <#--
     * Show Municipality decision form
    -->

    <div class="msg-block cf">
        <h2><@u.message "municipality.decision.giveDecision" /></h2>
        <p><@u.message key="municipality.decision.description" /></p>

        <div>
            <form action="${springMacroRequestContext.requestUri}" method="POST" id="form-accept" class="sodirty" enctype="multipart/form-data">
                <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>

                <@spring.bind "decision" />

                <div class="input-block-content no-top-margin">
                    <textarea path="decision.description" name="description" id="commentAccept" class="collapse" maxlength="${InitiativeConstants.INITIATIVE_COMMENT_MAX}"></textarea>
                </div>

                <input type="hidden" name="locale" value="${locale}"/>

                <div class="input-block-content">
                    <@f.uploadField path="decision.files" name="files" multiple=true/>
                </div>

                <div class="input-block-content">
                    <button type="submit" class="small-button"><span class="small-icon save-and-send"><@u.message "decision.submit" /></span></button>
                </div>
                <br/><br/>
            </form>
        </div>
    </div>

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



    <@e.initiativeTitle initiative />

    <@prog.progress initiative=initiative public=false />


    <div class="view-block first">
        <@e.initiativeView initiative />
    </div>

    <div class="view-block">
        <div class="initiative-content-row last">
            <h2><@u.message key="initiative.people.title" args=[authors?size] /></h2>

            <@e.initiativeContactInfo authorList=authors showRenewManagementHash=!initiative.verifiable && !initiative.sent/>
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