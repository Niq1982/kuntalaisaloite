<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/elements.ftl" as e />
<#import "components/pagination.ftl" as p />
<#import "components/progress.ftl" as prog />

<#escape x as x?html> 

<#--
 * Layout parameters for HTML-title and navigation.
 * 
 * page = "page.initiative.public" or "page.initiative.unnamed"
 * pageTitle = initiative.name if exists, otherwise empty string
-->
<@l.main "page.initiative.participants" initiative.name!"">

    <@u.returnPrevious urls.management(initiative.id) "link.to.managementView" />

    <@e.initiativeTitle initiative />
    
    <@prog.progress initiative=initiative public=false />

    <#if !RequestParameters['deleteParticipant']??>
        <div class="view-block first single public cf">
            <h2><@u.message "manageParticipants.title" />
                <span class="switch-view"><a href="${urls.participantList(initiative.id)}"><@u.message key="participantList.title" /> &bull; ${participantCount.publicNames!""} <@u.message key="participantList.title.count" /></a></span>
            </h2>

            <#if !initiative.isSent()>
                <p><@u.message "participantList.goToUpdate"/><a href="${urls.update(initiative.id)}#form-initiative"><@u.message "participantList.here"/></a></p>
            </#if>

            <#assign paginationParams = {
                "total":      participantCount.total,
                "limit":      50,
                "offset":     offset,
                "enableLimits": false
            } />
            
            <@p.pagination paginationParams "participants top" />
            
            <@participantListManage participants />
    
            <@p.pagination paginationParams "participants bottom" />
        </div>
    <#else>
        <#-- Confirm Delete form for NOSCRIPT users -->
        <div class="msg-block cf">
            <h2><@u.message "deleteParticipant.confirm.title" /></h2>
        
            <@deleteParticipantForm modal=false />
        </div>
    </#if>

    <@u.returnPrevious urls.management(initiative.id) "link.to.managementView" />

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
            <li><span class="date"><@u.localDate participant.participant.participateDate!"" /></span> <span class="name-container"><span class="name">${participant.participant.name!""}</span>
                
            <#-- Show home municipality for non-verified participants  -->
            <#if !initiative.isVerifiable()><span class="home-municipality"><span class="bull">&bull;</span> <@u.solveMunicipality participant.participant.homeMunicipality/></span></#if>

            <#-- Generate links for removing author -->
            <#if participant.isAuthor()>
                <span class="bull">&bull;</span> <@u.message "deleteParticipant.authorCannotBeDeleted" />

            <#else>
                <span class="bull">&bull;</span>
                <a  href="?deleteParticipant=${participant.participant.id!""}&verified=${participant.participant.isVerified()?c}" class="js-delete-participant"
                    data-id="${participant.participant.id!""}"
                    data-verified="${participant.participant.isVerified()?c}"
                    data-date="<@u.localDate participant.participant.participateDate!"" />"
                    data-name="<@u.stripHtmlTags participant.participant.name!"" />"
                    <#if !initiative.isVerifiable()>data-municipality="<@u.solveMunicipality participant.participant.homeMunicipality /></#if>"><@u.message "deleteParticipant.delete" /></a></span></li>

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
        <#if participant.participant.id?string == id>
            <ul class="participant-list no-style">
                <li>
                    <span class="date"><@u.localDate participant.participant.participateDate!"" /></span> <span class="name-container"><span class="name">${participant.participant.name!""}</span>
                    <#if !initiative.isVerifiable()><span class="home-municipality"><span class="bull">&bull;</span> <@u.solveMunicipality participant.participant.homeMunicipality/></span></#if>
                </li>
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
        <input type="hidden" name="verified" id="verified" value="<#if !modal>${RequestParameters['verified']}</#if>"/>
        
        <h3><@u.message "deleteParticipant.confirm.description" /></h3>
        
        <#if modal>
            <ul id="selected-participant" class="participant-list no-style"></ul>
            <br/>
        <#else>
            <@participantDetailsById participants RequestParameters['deleteParticipant'] />
        </#if>
        
        <@u.systemMessage path="deleteParticipant.confirm.warning" type="warning" />
        
        <div class="input-block-content">
            <button type="submit" name="${UrlConstants.ACTION_DELETE_PARTICIPANT}" id="modal-${UrlConstants.ACTION_DELETE_PARTICIPANT}" value="${UrlConstants.ACTION_DELETE_PARTICIPANT}" class="small-button"><span class="small-icon cancel"><@u.message "action.deleteParticipant.confirm" /></button>
            <a href="${springMacroRequestContext.requestUri}" class="push close"><@u.message "action.cancel" /></a>
        </div>
    </form>
</#macro>

</#escape> 

