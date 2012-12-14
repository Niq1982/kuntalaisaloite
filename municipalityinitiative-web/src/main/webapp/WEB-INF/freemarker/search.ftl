<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/flow-state.ftl" as flow />

<#escape x as x?html> 

<#if searchUnremovedVotes??>
    <#assign searchMode>unremovedVotes</#assign>

<#elseif currentSearch.includePublic && currentSearch.includeOwn>
    <#assign searchMode>other</#assign>
    
<#elseif currentSearch.includePublic>
    <#assign searchMode>public</#assign>
    
<#elseif currentSearch.includeOwn>
    <#assign searchMode>own</#assign>
    
<#else> 
    <#-- blocked in server side, should never be here --> 
    <#assign searchMode>none</#assign>
</#if>

<#assign pageTitle><@u.messageHTML "initiative.search.${searchMode}.title" /></#assign>

<@l.main "page.search" pageTitle!"">

    <h1>${pageTitle}
    <#if searchUnremovedVotes??> <#-- currently no alternative search option in this mode -->
	<#elseif currentSearch.includeOwn><span class="switch-view"><a href="${urls.search()}"><@u.message "initiative.search.public.link"/></a></span>
	<#elseif currentUser.registered><span class="switch-view"><a href="${urls.searchOwnOnly()}"><@u.message "user.myInitiatives"/></a></span>
    </#if>
    </h1>

    <p><@u.message key="initiative.search.${searchMode}.description" args=[minSupportCountForSearch!""] /></p>
    
    

    <div class="search-results">
    <#if initiatives?? && (initiatives?size > 0)>
        <#list initiatives as initiative>
            <#if initiative_index == 0><ul></#if>
            <li <#if initiative_index == 0>class="first"</#if>>
                <#if initiative.state == InitiativeState.ACCEPTED>
                <span class="support-votes-details">
                    <span class="support-votes-container">
                        <span class="support-votes">${initiative.totalSupportCount}</span>
                        <span class="bar" style="width:${(100*initiative.totalSupportCount/requiredVoteCount)?string("#")}%;"></span>
                    </span>
                    <#if (initiative.supportCount > 0)>
                        <#assign args><span class="internal-count">${initiative.supportCount}</span></#assign>
                        <span class="internal-support-votes"><@u.messageHTML key="initiative.internalSupportCount" args=[args] /></span>
                    </#if>
                </span>
                </#if>
                
                <#if searchMode == "unremovedVotes" || searchMode == "public"><#assign showTitle="show"></#if>
                <span class="date trigger-tooltip" title="<@u.message "searchResults.initiative."+searchMode+".startDate" />" ><@u.localDate initiative.startDate /></span>
                <a href="${urls.view(initiative.id)}" class="title"><span class="name"><@u.text initiative.name /></span></a>
                <span class="info"><@flow.flowStateDescription initiative /></span>
                <#-- ${initiative.id} state: ${initiative.state}/${flowStateAnalyzer.getFlowState(initiative)} -->
                
            </li>
            <#if !initiative_has_next></ul></#if>
        </#list>
        
    <#-- Search results EMPTY -->
    <#else>
        <#assign emptySearchResultsHTML>
            <#assign href>${urls.helpIndex()}/<@u.enumDescription HelpPage.PROGRESS /></#assign>
            <@u.messageHTML key="initiative.search.${searchMode}.description.2" args=[minSupportCountForSearch!"",href] />
        </#assign>
    
        <div class="system-msg msg-summary">
            <@u.systemMessage path="searchResults.${searchMode}.empty" type="info" showClose=false />
            <@u.systemMessageHTML html=emptySearchResultsHTML type="info" />
        </div>
        
    </#if>
    
    </div>

</@l.main>

</#escape>