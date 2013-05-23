<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/elements.ftl" as e />

<#escape x as x?html> 

<#--
 * Layout parameters for HTML-title and navigation.
 * 
 * page = "page.initiative.public" or "page.initiative.unnamed"
 * pageTitle = initiative.name if exists, otherwise empty string
-->
<@l.main "page.initiative.participants" initiative.name!"">

    <h1 class="name">${initiative.name!""}</h1>
    
    <div class="municipality">${initiative.municipality.getName(locale)}</div>
    
    <@e.stateInfo initiative />
    
    <@returnPrevious />

    <#if !RequestParameters['deleteParticipant']??>
        <div class="view-block single public cf">
            <h2><@u.message "manageParticipants.title" />
                <span class="switch-view"><a href="${urls.participantList(initiative.id)}"><@u.message key="participantList.title" /> &bull; ${participantCount.publicNames!""}<@u.message key="participantList.title.count" /></a></span>
            </h2>

            <@participantListManage participants />
        </div>
    <#else>
        <#-- Confirm Delete form for NOSCRIPT users -->
        <div class="msg-block cf">
            <h2><@u.message "deleteParticipant.confirm.title" /></h2>
        
            <@deleteParticipantForm modal=false />
        </div>
    </#if>

    <@returnPrevious />

    <#-- HTML for confirm Delete Modal -->
    <#assign deleteParticipant>
        <@compress single_line=true>
            <@deleteParticipantForm />
        </@compress>
    </#assign>
    
    <#--
     * Manage participant modals
     * 
     * Uses jsRender for templating.
     * Same content is generated for NOSCRIPT and for modals.
     *
     * Modals:
     *  Delete participant
     *
     * jsMessage:
     *  Warning if cookies are disabled
    -->
    <@u.modalTemplate />
    <@u.jsMessageTemplate />
    
    <script type="text/javascript">
        var modalData = {};
        
        <#-- Modal: Request messages. Check for components/utils.ftl -->
        <#if deleteParticipant??>    
            modalData.deleteParticipant = function() {
                return [{
                    title:      '<@u.message "deleteParticipant.confirm.title" />',
                    content:    '<#noescape>${deleteParticipant?replace("'","&#39;")}</#noescape>'
                }]
            };
        </#if>

        var messageData = {};

        <#-- jsMessage: Warning if cookies are not enabled -->
        messageData.warningCookiesDisabled = function() {
            return [{
                type:      'warning',
                content:    '<h3><@u.message "warning.cookieError.title" /></h3><div><@u.messageHTML key="warning.cookieError.description" args=[urls.participantList(initiative.id)] /></div>'
            }]
        };
    </script>
</@l.main>

<#-- 
 * participantListManage
 *
 * Prints public participant name list with municipality and participate date.
 *
 * @param participants is participants object list
-->
<#macro participantListManage participants>
    <#list participants as participant>

        <#if participant_index == 0><ul class="participant-list no-style"></#if>
            <li><span class="date"><@u.localDate participant.participateDate!"" /></span> <span class="name-container"><span class="name">${participant.name!""}</span> <span class="home-municipality"><span class="bull">&bull;</span> ${participant.homeMunicipality.getName(locale)!""}</span>
            <#if !participant.isAuthor()>
                <span class="bull">&bull;</span>
                <a  href="?deleteParticipant=${participant.id!""}" class="js-delete-participant"
                    data-id="${participant.id!""}"
                    data-date="<@u.localDate participant.participateDate!"" />"
                    data-name="${participant.name!""}"
                    data-municipality="${participant.homeMunicipality.getName(locale)!""}"><@u.message "deleteParticipant.delete" /></a></span></li>

            <#else>
                <span class="bull">&bull;</span> <@u.message "deleteParticipant.authorCannotBeDeleted" />
            </#if>
            
        <#if !participant_has_next></ul></#if>
    </#list>
</#macro>

<#-- 
 * participantDetailsById
 *
 * Prints participant's details by id
 *
 * @param participants is participants object list
 * @param id is participant's id
-->
<#macro participantDetailsById participants id>
    <#list participants as participant>
        <#if participant.id?string == id>
            <ul class="participant-list no-style">
                <li><span class="date"><@u.localDate participant.participateDate!"" /></span> <span class="name-container"><span class="name">${participant.name!""}</span> <span class="home-municipality"><span class="bull">&bull;</span> ${participant.homeMunicipality.getName(locale)!""}</span></li>
            </ul>
        </#if>
    </#list>
</#macro>

<#-- 
 * deleteParticipantForm
 *
 * Generates a form for deleting participant
 *
 * @param modal is a boolean for selecting either JS- or NOSCRIPT-version
-->
<#macro deleteParticipantForm modal=true>
    <form action="${springMacroRequestContext.requestUri}" method="POST" id="delete-participant-form">
        <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>
        
        <input type="hidden" name="${UrlConstants.PARAM_PARTICIPANT_ID}" id="${UrlConstants.PARAM_PARTICIPANT_ID}" value="<#if !modal>${RequestParameters['deleteParticipant']}</#if>"/>
        
        <h3><@u.message "deleteParticipant.confirm.description" /></h3>
        
        <#if modal>
            <ul id="selected-participant" class="participant-list no-style"></ul>
            <br/>
        <#else>
            <@participantDetailsById participants RequestParameters['deleteParticipant'] />
        </#if>
        
        <@u.systemMessage path="deleteParticipant.confirm.warning" type="warning" showClose=false />
        
        <div class="input-block-content">
            <button type="submit" name="${UrlConstants.ACTION_DELETE_PARTICIPANT}" id="modal-${UrlConstants.ACTION_DELETE_PARTICIPANT}" value="${UrlConstants.ACTION_DELETE_PARTICIPANT}" class="small-button"><span class="small-icon cancel"><@u.message "action.deleteParticipant.confirm" /></button>
            <a href="${springMacroRequestContext.requestUri}" class="push close"><@u.message "action.cancel" /></a>
        </div>
    </form>
</#macro>

<#-- 
 * returnPrevious
 *
-->
<#macro returnPrevious url label>
    <p><a href="${urls.getManagement(initiative.id)}">&laquo; <@u.message "link.to.managementView" /></a></p>
</#macro>

</#escape> 

