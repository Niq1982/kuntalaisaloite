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
 * canonical = optional URL otherwise currentUri is used
-->

<@l.main page="page.initiative.participants" pageTitle=initiative.name!"" canonical=urls.view(initiative.id)>

    <#if hasManagementRightForInitiative>
        <@u.returnPrevious urls.management(initiative.id) "link.to.managementView" />
    <#else>
        <@u.returnPrevious urls.view(initiative.id) "link.to.publicView" />
    </#if>

    <@e.initiativeTitle initiative />
    
    <@prog.progress initiative=initiative />
    
    <div class="view-block first single public cf">
        <h2><@u.message key="participantList.title" /><span class="bull">&bull;</span>${participantCount.publicNames!""} <@u.message key="participantList.title.count" />
            <#if hasManagementRightForInitiative><span class="switch-view"><a href="${urls.participantListManage(initiative.id)}" class="trigger-tooltip" title="<@u.message "manageParticipants.tooltip" />"><@u.message "manageParticipants.title" /></a></span></#if>
        </h2>
        
        <#if initiative.verifiable && hasManagementRightForInitiative>
            <#assign secureCount = 0/>
            <#list participants as participant>
                <#if !participant.participant.municipalityVerified>
                    <#assign secureCount = secureCount+1/>
                </#if>
            </#list>

            <#if (secureCount > 0)><p><@u.message key="participantList.secureCount" args=[secureCount] /></p></#if>
        </#if>
        
        <#assign paginationParams = {
            "total":      participantCount.publicNames,
            "limit":      50,
            "offset":     offset,
            "enableLimits": false
        } />
        
        <@p.pagination paginationParams "participants top" />
        
        <@participantList participants />

        <@p.pagination paginationParams "participants bottom" />
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

