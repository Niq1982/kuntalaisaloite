<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />

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

<#-- TODO: Filtering and search -->
<div class="view-block search-options cf">
    <div class="column col-1of3"
        <label class="input-header" for="municipality">
            Rajaa aloitteet kunnan mukaan
        </label>
    
        <select data-placeholder="Valitse kunta" id="municipality" name="municipality" tabindex="2" class="chzn-select">
            <option value=""></option>
            <#--<noscript><option value="">Valitse</option></noscript>-->
            <#list municipalities as municipality>
                <option value="${municipality.id}">${municipality.name}</option>
            </#list>
        </select>
    </div>
    <div class="column col-1of2 last cf">
        <label for="name" class="input-header">
            Etsi vapaasanahaulla
        </label>
        <input type="text" maxlength="512" class="large" value="" name="query" id="query" placeholder="Hakusana.." />
    </div>
</div>

<div class="municipalities">

    <div class="search-results">
    <#if initiatives?? && (initiatives?size > 0)>
        <#list initiatives as initiative>
            <#if initiative_index == 0><ul></#if>
            <li <#if initiative_index == 0>class="first"</#if>>
                
                
                <#--<span class="support-votes-details">
                    <span class="support-votes-container">
                        <span class="support-votes">TODO: count</span>
                    </span>
                </span>-->
                
                <span class="date trigger-tooltip" title="<@u.message "searchResults.initiative.date" />" ><@u.localDate initiative.createTime /></span>
                <a href="${urls.view(initiative.id)}" class="title"><span class="name">${initiative.name}</span></a>
                <span class="info">${initiative.municipalityName}</span>
                <#-- ${initiative.proposal} -->
                
            </li>
            <#if !initiative_has_next></ul></#if>
        </#list>
        
    <#-- Search results EMPTY -->
    <#else>
        <#assign emptySearchResultsHTML>
            ei tuloksia
        </#assign>
    
        <div class="system-msg msg-summary">
            <@u.systemMessageHTML html=emptySearchResultsHTML type="info" />
        </div>
        
    </#if>
    
    </div>
</div>


</@l.main>


</#escape>