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

<@l.main page="page.manageAttachments">
    
    <div class="msg-block">
        <div class="system-msg msg-info">
            <h2>Hallitse liitteitä</h2>
            <p>Ohjetekstiä...</p>
        </div>
    </div>
    
    <@u.returnPrevious managementURL "link.to.managementView" />

    <#if managementSettings.allowAddAttachments>
        <div class="view-block cf">
        <h2>Lisää liitteitä</h2>
        <form id="form-upload-image" enctype="multipart/form-data" action="${urls.addAttachment(initiative.id)}" method="POST">
            <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>
            <input type="hidden" name="locale" value="{locale.toLanguageTag()}"/>
            <input type="file" name="image">
            <label>Anna liitteelle selkeä ja kuvaava otsiko: <input type="text" name="description" maxlenth="${InitiativeConstants.ATTACHMENT_DESCRIPTION_MAX}"/></label>
            <input type="submit" value="Tallenna tiedosto"/>
        </form>
        </div>
    </#if>
    
    <@u.returnPrevious managementURL "link.to.managementView" />
    

    <#--
     * Management VIEW modals
     * 
     * Uses jsRender for templating.
     * Same content is generated for NOSCRIPT and for modals.
     *
     * Modals:
     *  Confirm delete attachment
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
        
        <#-- Modal: Confirm remove attachment -->
        <#-- TODO:
        <#if startCollecting??>    
            modalData.startCollecting = function() {
                return [{
                    title:      '<@u.message "startCollecting.confirm.title" />',
                    content:    '<#noescape>${startCollecting?replace("'","&#39;")}</#noescape>'
                }]
            };
        </#if>
        -->
            
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