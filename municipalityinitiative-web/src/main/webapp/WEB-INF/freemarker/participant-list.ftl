<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/elements.ftl" as e />
<#import "components/pagination.ftl" as p />

<#escape x as x?html> 

<#--
 * Layout parameters for HTML-title and navigation.
 * 
 * page = "page.initiative.public" or "page.initiative.unnamed"
 * pageTitle = initiative.name if exists, otherwise empty string
-->
<@l.main "page.initiative.participants" initiative.name!"">

    <@e.initiativeTitle initiative />
    
    <@e.stateInfo initiative />
    
    <#if hasManagementRightForInitiative>
        <@u.returnPrevious urls.management(initiative.id) "link.to.managementView" />
    <#else>
        <@u.returnPrevious urls.view(initiative.id) "link.to.publicView" />
    </#if>
    
    <div class="view-block single public cf">
        <h2><@u.message key="participantList.title" /><span class="bull">&bull;</span>${participantCount.publicNames!""} <@u.message key="participantList.title.count" />
            <#if !initiative.isVerifiable() && hasManagementRightForInitiative><span class="switch-view"><a href="${urls.participantListManage(initiative.id)}" class="trigger-tooltip" title="<@u.message "manageParticipants.tooltip" />"><@u.message "manageParticipants.title" /></a></span></#if>
        </h2>
        
        <#assign paginationParams = {
            "total":      participantCount.publicNames,
            "limit":      50,
            "offset":     offset,
            "enableLimits": false
        } />
        
        <@p.pagination paginationParams "top" />
        
        <@participantList participants />

        <@p.pagination paginationParams "bottom" />
    </div>
    
    <#if hasManagementRightForInitiative>
        <@u.returnPrevious urls.management(initiative.id) "link.to.managementView" />
    <#else>
        <@u.returnPrevious urls.view(initiative.id) "link.to.publicView" />
    </#if>
</@l.main>

<#-- 
 * participantList
 *
 * Prints public participant name list with municipality and participate date.
 *
 * @param participants is participants object list
-->


<#macro participantList participants>
    <#list participants as participant>
        <#if participant_index == 0><ul class="participant-list no-style"></#if>
            <li><span class="date"><@u.localDate participant.participant.participateDate!"" /></span>
                <span class="name-container"><span class="name">${participant.participant.name}</span>
                <#if !initiative.isVerifiable()><span class="home-municipality"><span class="bull">&bull;</span> <@u.solveMunicipality participant.participant.homeMunicipality/></span></#if>
                </span>
            </li>
        <#if !participant_has_next></ul></#if>
    </#list>
</#macro>

</#escape> 

