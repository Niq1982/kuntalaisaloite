<#import "/spring.ftl" as spring />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/forms.ftl" as f />

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
<#--
<div class="view-block search-options cf">
    <form action="${springMacroRequestContext.requestUri}" method="GET" id="search-form" class="search-form">
    <div class="column col-1of3">
        <label class="input-header" for="municipality">
            Rajaa aloitteet kunnan mukaan
        </label>
    
        <select data-placeholder="Valitse kunta" id="municipality" name="municipality" tabindex="2" class="chzn-select municipality-filter">
            <option value=""></option>
            <#list municipalities as municipality>
                <option value="${municipality.id}">${municipality.name}</option>
            </#list>
        </select>
    </div>
    <div class="column col-1of3">
        <label for="search" class="input-header">
            Etsi vapaasanahaulla
        </label>
        <input type="text" maxlength="512" class="large" value="" name="search" id="search" placeholder="Hakusana.." />
    </div>
    
    <div class="column col-1of3 last cf">
        <button type="submit" class="small-button"><span class="small-icon next">Hae</span></button>
    </div>
        

    </form>
</div>
-->

<#assign searchMunicipality = RequestParameters['municipality']!"" />
<#assign searchTerm = RequestParameters['search']!"" />


<#-- 
 * TODO:
 * - FIX layout for IE7
 * - Input placeholder for IE browser, make a general js plugin
-->
<div class="view-block search-options cf">
    <form action="${springMacroRequestContext.requestUri}" method="GET" id="search-form" class="search-form">
    <div class="table full valign-bottom">
        <div class="cell cell-1of3">
            <@f.municipalitySelect path="currentSearch.municipality" options=municipalities required="" cssClass="municipality-filter" />
        </div>
        <div class="cell cell-2of3">
            <#assign placeholder><@u.message "currentSearch.placeholder" /></#assign>
            <@f.textField path="currentSearch.search" required="" optional=false cssClass="search-field" maxLength="512" attributes="placeholder='${placeholder}'" />
            
            <button type="submit" class="small-button"><span class="small-icon search"><@u.message "btn.search" /></span></button>
        </div>
        <#--
        <div class="cell cell-1of3">
            <button type="submit" class="small-button"><span class="small-icon next">Hae</span></button>
        </div>-->
    </div>
        

    </form>
</div>


<div class="search-terms">
    <#if searchMunicipality != "">
        <h2>Aloitteet kunnassa: ${currentMunicipality}</h2>
    </#if>
    
    <#if searchTerm != "">
        <p>Haun tulokset hakusanalle: "<span class="search-term">${searchTerm}</span>"</p>
    </#if>
</div>

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
            <span class="title"><a href="${urls.view(initiative.id)}" class="name">${initiative.name}</a></span>
            <span class="info">${initiative.municipalityName}</span>
            <#-- ${initiative.proposal} -->
            
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