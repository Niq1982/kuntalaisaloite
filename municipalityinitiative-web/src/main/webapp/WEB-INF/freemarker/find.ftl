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


<#-- 
 * TODO:
 * - FIX layout for IE7
 * - Input placeholder for IE browser, make a general js plugin
-->
<#--<div class="view-block search-options cf">-->
    <#--<form action="${springMacroRequestContext.requestUri}" method="GET" id="search-form" class="search-form">
    <div class="table full valign-bottom">
        <div class="cell cell-1of3">
            <@f.municipalitySelect path="currentSearch.municipality" options=municipalities required="" cssClass="municipality-filter" />
        </div> 
        <div class="cell cell-2of3">
            <#assign placeholder><@u.message "currentSearch.placeholder" /></#assign>
            <@f.textField path="currentSearch.search" required="" optional=false cssClass="search-field" maxLength="512" attributes="placeholder='${placeholder}'" />
            
            <button type="submit" class="small-button"><span class="small-icon search"><@u.message "btn.search" /></span></button>
        </div>
    
    </div>
    </form>-->
    
    <#--<div class="view-block search-options cf">-->
    <span class="search-parameters-title filter"><label for="municipality">Kunta</label></span>
    <div class="search-parameters-container">
        <form action="${springMacroRequestContext.requestUri}" method="GET" id="search-form" class="search-form">
            <div class="cell cell-1of3">
                <@f.municipalitySelect path="currentSearch.municipality" options=municipalities required="" cssClass="municipality-filter" showLabel=false />
            </div> 
        </form>
    </div>
    

    <br/>
    
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
            <a href="#" class="active trigger-tooltip" title="Kerätään osallistujia">Kerätään osallistujia<span class="count">798</span></a>
            <a href="#" class=" trigger-tooltip" title="Lähetetty kuntaan">Lähetetty kuntaan<span class="count">375</span></a>
            <a href="#" class=" trigger-tooltip" title="Suljetut">Suljetut<span class="count">150</span></a>
            <a href="#" class=" trigger-tooltip" title="Kaikki">Kaikki<span class="count">1500</span></a>
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
            <#--<#if currentSearch.show == "running">
                <span class="small-icon icon-search-sort by-time-left"><@u.message "searchOptions.runningTimeLeft" /></span>
                <div class="search-sort-links">
                    <@u.searchLink parameter="withOrderByMostTimeLeft" cssClass=(currentSearch.orderBy == "mostTimeLeft")?string('active','') tooltip=false />
                    <@u.searchLink parameter="withOrderByLeastTimeLeft" cssClass=(currentSearch.orderBy == "leastTimeLeft")?string('active','') tooltip=false />
                </div>
            <#else>-->
                <span class="small-icon icon-search-sort by-date-accepted">&#160;</span><div class="search-sort-links">
                    <a href="#" class="active" title="">Uusin ensin</a>
                    <a href="#" class="" title="">Vanhin ensin</a>
                    <#--<@u.searchLink parameter="withOrderByCreatedNewest" cssClass=(currentSearch.orderBy == "createdNewest")?string('active','') tooltip=false />
                    <@u.searchLink parameter="withOrderByCreatedOldest" cssClass=(currentSearch.orderBy == "createdOldest")?string('active','') tooltip=false />-->
                </div>
            <#--</#if>-->
        </div>
        <div class="column search-sort">
            <span class="small-icon icon-search-sort by-support-statements"><@u.message "searchOptions.participants" /></span>
            <div class="search-sort-links">
                <a href="#" class="active" title="">eniten</a>
                <a href="#" class="" title="">vähiten</a>
                <#--<@u.searchLink parameter="withOrderByMostSupports" cssClass=(currentSearch.orderBy == "mostSupports")?string('active','') tooltip=false />
                <@u.searchLink parameter="withOrderByLeastSupports" cssClass=(currentSearch.orderBy == "leastSupports")?string('active','') tooltip=false />-->
            </div>
        </div>
        <br class="clear" />
        
        
    <#--</#if>-->

<#--</div>-->

    <#--
     * TODO: Search pagination - also in the bottom of rearch results
    -->
    <#--
    <#if searchMode != "own">
        <@p.pagination currentSearch.limit!500 currentSearch.offset!0 "top" />
    </#if>
    -->


<div class="search-terms">
    <#if searchMunicipality != "">
        <h2>Aloitteet kunnassa: ${currentMunicipality}</h2>
    </#if>
    
    <#--
    <#if searchTerm != "">
        <p>Haun tulokset hakusanalle: "<span class="search-term">${searchTerm}</span>"</p>
    </#if>
    -->
</div>

<div class="search-results">
<#if initiatives?? && (initiatives?size > 0)>
    <#list initiatives as initiative>
        <#if initiative_index == 0><ul></#if>
        <li <#if initiative_index == 0>class="first"</#if>>
            
            <span class="participants">
                <span class="participants-container">
                    <#-- TODO: counts / isCollectable. For dummy testing every fourth initiative is not collectable here. -->
                    <#if (initiative.id%4 == 0)>
                        <span class="no-participants">Ei kerännyt osallistujia</span>
                    <#else>
                        <#if (initiative.id%2 == 0)>
                            <span class="participant-count one">1</span>
                        <#else>
                            <span class="participant-count">${3*initiative_index}</span>
                        </#if>
                    </#if>
                </span>
            </span>
            
            <span class="date trigger-tooltip" title="<@u.message "searchResults.initiative.date" />" ><@u.localDate initiative.createTime /></span>
            <span class="title"><a href="${urls.view(initiative.id)}" class="name">${initiative.name}</a></span>
            <#-- TODO: Initiative state -->
            <span class="info">${initiative.municipalityName}<span class="state">Aloite lähetetty kuntaan 08.02.2013</span></span>
            
        </li>
        <#if !initiative_has_next></ul></#if>
    </#list>
    
<#-- Search results EMPTY -->
<#else>
    <#assign emptySearchResultsHTML>
        Valitsemallasi rajauksella ei löytynyt yhtään aloitetta.
    </#assign>

    <div class="system-msg msg-summary">
        <@u.systemMessageHTML html=emptySearchResultsHTML type="info" />
    </div>
    
</#if>

</div>


</@l.main>

</#escape>