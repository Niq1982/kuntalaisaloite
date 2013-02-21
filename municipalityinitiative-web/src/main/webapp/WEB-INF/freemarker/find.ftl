<#import "/spring.ftl" as spring />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/pagination.ftl" as p />
<#import "components/forms.ftl" as f />
<#import "components/elements.ftl" as e />

<#escape x as x?html>

<#--
 * Layout parameters for HTML-title and navigation.
 * 
 * @param page is "page.find"
 * @param pageTitle can be assigned as custom HTML title
-->
<#assign page="page.find" />

<@l.main "page.find" pageTitle!"">

<h1><@u.message page /></h1>

<#assign searchMunicipality = RequestParameters['municipality']!"" />
<#assign searchTerm = RequestParameters['search']!"" />


<div class="view-block search-options cf">

    <#--
     * Municipality filter
    -->
    <span class="search-parameters-title filter"><label for="municipality"><@u.message "searchOptions.municipality" /></label></span>
    <div class="search-parameters-container cf">
        <form action="${springMacroRequestContext.requestUri}" method="GET" id="search-form" class="search-form">
            <div class="column col-1of3">
                <@f.municipalitySelect path="currentSearch.municipality" options=municipalities required="" cssClass="municipality-filter" showLabel=false />
            </div>
            
            <#-- Submit button for NOSCRIPT users -->
            <noscript>
            <div class="column col">
                <button type="submit" class="small-button"><span class="small-icon search"><@u.message "btn.search" /></span></button>
            </div>
            </noscript>
        </form>
    </div>
    
    <#--
     * Search filter and sort states
     * currentSearch.show:      running, sentToMunicipality, closed, all
     * currentSearch.orderBy:   id, mostTimeLeft, leastTimeLeft, mostSupports, leastSupports
     * currentSearch.limit:     20, 100, 500
     * currentSearch.offset
    -->
    
    <#--
     * Search filters
    -->
    <span class="search-parameters-title filter"><@u.message "searchOptions.filter" /></span>
    <div class="search-parameters-container">
        <div class="search-parameters">
            <@u.searchLink parameter="withStateCollecting" cssClass=(currentSearch.show == "collecting")?string('active','') count=initiativeCounts.collecting />
            <@u.searchLink parameter="withStateSent" cssClass=(currentSearch.show == "sent")?string('active','') count=initiativeCounts.sent/>
            <@u.searchLink parameter="withStateAll" cssClass=(currentSearch.show == "all")?string('active','') count=initiativeCounts.all/>
        </div>
        <br class="clear" />
    </div>
    
    <#--
     * Search sort
     *
     * Sort only if more than 1 to sort
    -->
    <#--<#if (initiativeCounts[currentSearch.show] > 1)>-->
        <span class="search-parameters-title sort"><@u.message "searchOptions.sort" /></span>
        <div class="column search-sort">
            <#if currentSearch.show == "sent">
                <span class="small-icon icon-search-sort by-date-accepted">&#160;</span>
                <div class="search-sort-links">
                    <@u.searchLink parameter="withOrderByLatestSent" cssClass=(currentSearch.orderBy == "latestSent")?string('active','') tooltip=false />
                    <@u.searchLink parameter="withOrderByOldestSent" cssClass=(currentSearch.orderBy == "oldestSent")?string('active','') tooltip=false />
                </div>
            <#else>
                <span class="small-icon icon-search-sort by-date-accepted">&#160;</span>
                <div class="search-sort-links">
                    <@u.searchLink parameter="withOrderByLatest" cssClass=(currentSearch.orderBy == "latest")?string('active','') tooltip=false />
                    <@u.searchLink parameter="withOrderByOldest" cssClass=(currentSearch.orderBy == "oldest")?string('active','') tooltip=false />
                </div>
            </#if>
        </div>
        <div class="column search-sort">
            <span class="small-icon icon-search-sort by-support-statements"><@u.message "searchOptions.participants" /></span>
            <div class="search-sort-links">
                <@u.searchLink parameter="withOrderByMostParticipants" cssClass=(currentSearch.orderBy == "mostParticipants")?string('active','') tooltip=false />
                <@u.searchLink parameter="withOrderByLeastParticipants" cssClass=(currentSearch.orderBy == "leastParticipants")?string('active','') tooltip=false />
            </div>
        </div>
        <br class="clear" />
        
        
    <#--</#if>-->

</div>

<div class="search-terms">
    <#if searchMunicipality != "">
        <h2><@u.message "searchResults.initiativesInMunicipality" />: ${currentMunicipality!""}</h2>
    </#if>
    
    <#--
    <#if searchTerm != "">
        <p>Haun tulokset hakusanalle: "<span class="search-term">${searchTerm}</span>"</p>
    </#if>
    -->
</div>

<@p.pagination currentSearch.limit!500 currentSearch.offset!0 "top" />

<div class="search-results">
<#if initiatives?? && (initiatives?size > 0)>
    <#list initiatives as initiative>
        <#if initiative_index == 0><ul></#if>
        <li <#if initiative_index == 0>class="first"</#if>>
            
            <span class="participants">
                <span class="participants-container">
                    <#if !initiative.collectable>
                        <span class="no-participants"><@u.message "searchResults.notCollectable" /></span>
                    <#else>
                        <span class="participant-count trigger-tooltip" title="<@u.message "searchResults.sumOfParticipants" />">${initiative.participantCount!""}</span>
                    </#if>
                </span>
            </span>
            
            <span class="date trigger-tooltip" title="<@u.message "searchResults.initiative.date" />" ><@u.localDate initiative.createTime!"" /></span>
            <span class="title"><a href="${urls.view(initiative.id)}" class="name">${initiative.name!""}</a></span>
            <#if initiative.collectable && !initiative.sentTime.present>
                <span class="info">${initiative.municipality.name!""}<span class="bull">&bull;</span><span class="state"><@u.message "initiative.state.collecting" /></span></span>
            <#else>
                <#assign sentTime><@u.localDate initiative.sentTime.value!"" /></#assign>
                <span class="info">${initiative.municipality.name!""}<span class="bull">&bull;</span><span class="state"><@u.message key="initiative.date.sent" args=[sentTime] /></span></span>
            </#if>
            
        </li>
        <#if !initiative_has_next></ul></#if>
    </#list>
    
<#-- Search results EMPTY -->
<#else>
    <#assign emptySearchResultsHTML>
        <@u.message "searchResults.noResults" />
    </#assign>

    <div class="system-msg msg-summary">
        <@u.systemMessageHTML html=emptySearchResultsHTML type="info" />
    </div>
    
</#if>

</div>

<@p.pagination currentSearch.limit!500 currentSearch.offset!0 "bottom" />


</@l.main>

</#escape>