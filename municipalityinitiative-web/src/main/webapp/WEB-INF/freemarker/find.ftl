
<#import "/spring.ftl" as spring />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/pagination.ftl" as p />
<#import "components/forms.ftl" as f />
<#import "components/elements.ftl" as e />
<#import "components/mobile-components.ftl" as mobile />

<#escape x as x?html>

<#--
 * Layout parameters for HTML-title and navigation.
 * 
 * @param page is "page.find"
 * @param pageTitle can be assigned as custom HTML title
-->
<#assign page="page.find" />

<#assign searchTerm = RequestParameters['search']!"" />

<@l.main "page.find" pageTitle!"">


<div class="search-page-form-wrap">
<div class="view-block search-options cf noprint" id="search-page-form">

    <div>
        <#--
         * Municipality filter
        -->
            <#if currentMunicipalities.present && currentMunicipalities.get()?size == 1>
                <#assign oneCurrentMunicipality = true />
            <#else>
                <#assign oneCurrentMunicipality = false />
            </#if>
        <div class="search-parameters-container cf">
            <form action="${urls.search()}" method="GET" id="search-form" class="search-form">
                <div class="search-form-header <#if oneCurrentMunicipality?c == "false">full-width</#if>">
                    <h1>
                        <@u.message page />
                        <#if user.isVerifiedUser()>
                            <span class="switch-view"><a
                                    href="${urls.ownInitiatives()}"><@u.message "page.ownInitiatives"/></a></span>
                        </#if>
                    </h1>
                    <span class="search-parameters-title filter"><label
                            for="municipality"><@u.message "searchOptions.municipality" /></label></span>
                    <div>
                        <@f.municipalitySelect path="currentSearch.municipalities" options=municipalities required="" cssClass="" showLabel=false defaultOption="currentSearch.municipality.all" allowSingleDeselect=true onlyActive=true multiple=true />
                    </div>
                </div>
                <#if oneCurrentMunicipality?c == "true">
                    <@e.municipalityDescription currentMunicipalities.get()?first />
                </#if>

                <#-- Submit button for NOSCRIPT users -->
                <noscript>
                <div class="column col">
                    <button type="submit" class="small-button"><span class="small-icon search"><@u.message "btn.search" /></span></button>
                </div>
                </noscript>
            </form>
        </div>

        <#--
         * Initiative type
        -->
        <div class="search-parameters-in-row-container">
            <#if enableVerifiedInitiatives>
                <div class="single-search-parameter-container">
                <span class="search-parameters-title filter"><label
                        for="municipality"><@u.message "searchParameters.type" /></label></span>
                    <div class="search-parameters-container cf buttons">
                        <div class="search-parameters four-items">
                            <#switch currentSearch.type>
                                <#case "all">
                                    <#assign typePlaceholder = "All" />
                                    <#break>
                                <#case "normal">
                                    <#assign typePlaceholder = "Normal" />
                                    <#break>
                                <#case "citizen">
                                    <#assign typePlaceholder = "Citizen" />
                                    <#break>
                                <#default>
                                    <#assign typePlaceholder = "All" />
                            </#switch>
                            <select name="initiative-type" class="municipality-filter chzn-select"
                                    data-placeholder="<@u.message "searchParameters.withType${typePlaceholder}"/>" onChange="window.location.href=this.value">
                                <option value=""><@u.message "searchParameters.withTypeAll" /></option>
                                <option value="${queryString.withTypeAll}"><@u.message "searchParameters.withTypeAll" /></option>
                                <option value="${queryString.withTypeNormal}"><@u.message "searchParameters.withTypeNormal" /></option>
                                <option value="${queryString.withTypeCitizen}"><@u.message "searchParameters.withTypeCitizen" /></option>
                            </select>
                        </div>
                    </div>
                </div>
            </#if>
        <#--
         * Search filter and sort states
         * currentSearch.show:      running, sentToMunicipality, closed, all
                                    draft, review, accepted, fix (OM view)
         * currentSearch.orderBy:   id, mostTimeLeft, leastTimeLeft, mostSupports, leastSupports
         * currentSearch.limit:     20, 100, 500
         * currentSearch.offset
        -->

        <#--
         * Search filters for OM and public view
        -->
            <div class="single-search-parameter-container">
                <span class="search-parameters-title filter"><@u.message "searchOptions.filter" /></span>
                <div class="search-parameters-container buttons">

                    <div class="search-parameters three-items">
                        <#switch currentSearch.show>
                            <#case "all">
                                <#assign statePlaceholder = "All" />
                                <#break>
                            <#case "collecting">
                                <#assign statePlaceholder = "Collecting" />
                                <#break>
                            <#case "sent">
                                <#assign statePlaceholder = "Sent" />
                                <#break>
                            <#case "draft">
                                <#assign statePlaceholder = "Draft" />
                                <#break>
                            <#case "review">
                                <#assign statePlaceholder = "Review" />
                                <#break>
                            <#case "fix">
                                <#assign statePlaceholder = "Fix" />
                                <#break>
                            <#case "accepted">
                                <#assign statePlaceholder = "Accepted" />
                                <#break>
                            <#default>
                                <#assign statePlaceholder = "All" />
                        </#switch>
                        <select name="initiative-state" class="municipality-filter chzn-select"
                                data-placeholder="<@u.message "searchParameters.withState${statePlaceholder}"/>" onChange="window.location.href=this.value">
                            <option title="<@u.message "searchParameters.withStateAll" />" value=""><@u.message "searchParameters.withStateAll" /></option>
                            <option title="<@u.message "searchParameters.withStateAll" />" value="${queryString.withStateAll}"><@u.message "searchParameters.withStateAll" /> (${initiativeCounts.all})</option>
                            <#if user.isOmUser()>
                                <option title="<@u.message "searchParameters.withStateDraft" />" value="${queryString.withStateDraft}"><@u.message "searchParameters.withStateDraft" /> (${initiativeCounts.draft})</option>
                                <option title="<@u.message "searchParameters.withStateReview" />" value="${queryString.withStateReview}"><@u.message "searchParameters.withStateReview" /> (${initiativeCounts.review})</option>
                                <option title="<@u.message "searchParameters.withStateAccepted" />" value="${queryString.withStateAccepted}"><@u.message "searchParameters.withStateAccepted" /> (${initiativeCounts.accepted})</option>
                                <option title="<@u.message "searchParameters.withStateFix" />" value="${queryString.withStateFix}"><@u.message "searchParameters.withStateFix" /> (${initiativeCounts.fix})</option>
                            </#if>

                            <option title="<@u.message "searchParameters.withStateAll" />" value="${queryString.withStateCollecting}"><@u.message "searchParameters.withStateCollecting" /> (${initiativeCounts.collecting})</option>
                            <option title="<@u.message "searchParameters.withStateAll" />" value="${queryString.withStateSent}"><@u.message "searchParameters.withStateSent" /> (${initiativeCounts.sent})</option>
                        </select>
                    </div>
                    <br class="clear"/>
                </div>
            </div>

        <#--
         * Search sort
         *
         * Sort only if more than 1 to sort
        -->
            <div class="single-search-parameter-container">
            <#--<#if (initiativeCounts[currentSearch.show] > 1)>-->
                <span class="search-parameters-title filter"><@u.message "searchOptions.sort" /></span>
                <div class="search-parameters-container buttons">
                    <div class="search-parameters three-items">
                        <#switch currentSearch.orderBy>
                            <#case "latest">
                                <#assign orderPlaceholder = "Latest" />
                                <#break>
                            <#case "oldest">
                                <#assign orderPlaceholder = "Oldest" />
                                <#break>
                            <#case "mostParticipants">
                                <#assign orderPlaceholder = "MostParticipants" />
                                <#break>
                            <#case "leastParticipants">
                                <#assign orderPlaceholder = "LeastParticipants" />
                                <#break>
                            <#default>
                                <#assign orderPlaceholder = "Latest" />
                        </#switch>
                        <select name="initiative-order" class="municipality-filter chzn-select"
                                data-placeholder="<@u.message "searchParameters.withOrderBy${orderPlaceholder}" />" onChange="window.location.href=this.value">
                            <option value=""><@u.message "searchParameters.withOrderByLatest" /></option>
                            <option value="${queryString.withOrderByLatest}"><@u.message "searchParameters.withOrderByLatest" /></option>
                            <option value="${queryString.withOrderByOldest}"><@u.message "searchParameters.withOrderByOldest" /></option>
                            <option value="${queryString.withOrderByMostParticipants}"><@u.message "searchParameters.withOrderByMostParticipants" /></option>
                            <option value="${queryString.withOrderByLeastParticipants}"><@u.message "searchParameters.withOrderByLeastParticipants" /></option>
                        </select>
                    </div>

                <br class="clear"/>
                </div>

            <#--</#if>-->
            </div>
        </div>
    </div>
</div>
</div>
<div class="search-page-results-wrap">
<div class="search-page-results" id="search-page-results">
<@mobile.mobileSearch />

<div class="search-terms">
    <#if currentMunicipalities.present>
            <h2>
                <#if currentMunicipalities.get()?size = 1>
                    <@u.message "searchResults.initiativesInMunicipality" />:
                <#elseif currentMunicipalities.get()?size gt 1>
                    <@u.message "searchResults.initiativesInMunicipalities" />:
                </#if>
                <@u.printMunicipalities currentMunicipalities.get() />
            </h2>
    </#if>
</div>

<#assign paginationParams = {
    "total":      initiatives.count,
    "limit":      currentSearch.limit!500,
    "offset":     currentSearch.offset!0,
    "queryString": queryString,
    "enableLimits": true
} />

<@p.pagination paginationParams "top" />

<div class="search-results">
<#if initiatives?? && (initiatives.list?size > 0)>
    <#list initiatives.list as initiative>
        <#assign initiativeUrl><#if (user.isOmUser()) >${urls.moderation(initiative.id)}<#else>${urls.view(initiative.id)}</#if></#assign>
        <#if initiative_index == 0><ul></#if>
        <li <#if initiative_index == 0>class="first"</#if>>
            <a href="${initiativeUrl}" class="search-result">
                <span class="participants">
                    <span class="participants-container">
                        <#if !initiative.public>
                            <span class="no-participants"><@u.message "searchResults.notPublic" /></span>
                        <#elseif !initiative.collaborative>
                            <span class="no-participants"><@u.message "searchResults.notCollaborative" /></span>
                        <#else>
                            <span class="participant-count trigger-tooltip" title="<@u.message "searchResults.sumOfParticipants" />">${initiative.participantCount!""}</span>
                        </#if>
                    </span>
                </span>

                <span class="date trigger-tooltip" title="<@u.message "searchResults.initiative.date."+initiative.state/>" ><@u.localDate initiative.stateTime!"" /></span>
                <span class="title"><span class="name"><@u.limitStringLength initiative.name!"" 150 /></span></span>
                <span class="info"><span class="municipality-search-result">${initiative.municipality.getName(locale)!""}</span> <span class="bull">&bull;</span>

                    <#if !initiative.public>
                        <span class="state"><@u.message "searchResults.notPublic" /></span>
                    <#elseif !initiative.sentTime.present>
                        <span class="state"><@u.message "initiative.state.collecting" /></span>
                    <#else>
                        <#assign sentTime><@u.localDate initiative.sentTime.get()!"" /></#assign>
                        <span class="state"><@u.message key="initiative.date.sent" args=[sentTime] /></span>
                    </#if>
                    <span class="bull">&bull;</span>
                    <span class="initiative-type">
                        <@u.message "initiative.initiativeType."+initiative.type />
                    </span>
                </span>
            </a>
         <@mobile.mobileSearchResult initiative/>
        </li>
        <#if !initiative_has_next></ul></#if>
    </#list>

<#-- Search results EMPTY -->
<#else>
    <div class="msg-block">
        <@u.systemMessage path="searchResults.noResults" type="info" cssClass="first" />
    </div>
</#if>

</div>

<@p.pagination paginationParams "bottom" />

</div>
</div>
</@l.main>

</#escape>