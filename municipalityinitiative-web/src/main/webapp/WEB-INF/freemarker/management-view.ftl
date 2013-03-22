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

<@l.main "page.initiative.management" initiative.name!"">

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

    <div class="view-block public">
        <#if initiative.proposal??>
            <div class="initiative-content-row">
                <h2><@u.message "initiative.content.title" /></h2>
                
                <@u.text initiative.proposal!"" />
            </div>
        </#if>

        <div class="initiative-content-row last">
            <h2><@u.message "initiative.contactinfo.title" /></h2>
            <p>${initiative.authorName!""}<br />
        </div>
    </div>

    <div class="view-block public">
        <h2><@u.message "management.sendToReview.title" /></h2>
    
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
            <a class="small-button"><span class="small-icon mail"><@u.message "action.sendToReview.doNotCollect" /></span></a>
        </div>
        <div class="column col-1of2 last">
            <a class="small-button" value="true" ><span class="small-icon save-and-send"><@u.message "action.sendToReview.collect" /></span></a>
        </div>
        <br class="clear" />
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
                content:    '<h3><@u.message "warning.cookieError.title" /></h3><div><@u.messageHTML key="warning.cookieError.description" args=[managementURL] /></div>'
            }]
        };
    </script>

</@l.main>

</#escape> 