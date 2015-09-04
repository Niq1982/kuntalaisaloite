<#import "utils.ftl" as u />

<#escape x as x?html> 

<#--
 * pages
 *
 * Print link to pages
 * Current page is displayed as a span
 *
 * @param params is an object containing:
 *  - total is the total amount of filtered initiatives
 *  - limit is the current limit
 *  - offset is the current offset
-->
<#macro pages params>
    <#assign totalPages = (params.total / params.limit)?ceiling />
    <#assign currOffset= (params.offset / params.limit)?floor + 1 />
    
    <span class="page-numbers">
        <@u.message "pagination.page" /> <span>${currOffset} / ${totalPages}</span>
    </span>
    
</#macro>

<#--
 * numbers
 *
 * Not in use. If enabled it will need update before usage (add params object).
 *
 * Print link to pages
 * Current page is displayed as a span
 *
 * @param totalItems is the total amount of filtered initiatives
 * @param limit is the current limit
 * @param offset is the current offset
-->
<#macro numbers totalItems limit offset>
    <#assign totalPages = (totalItems / limit)?ceiling />
    
    <#list 1..totalPages as page>
        <#if page_index == 0><span class="pagination-numbers"></#if>
        
        <#assign currOffset=(page-1)*limit />
        
        <#if currOffset == offset>
            <span>${page}</span>
        <#else>
            <a href="${springMacroRequestContext.requestUri}${queryString.withOffset(currOffset)}">${page}</a>
        </#if>
        
        <#if !page_has_next></span></#if>
    </#list>  
</#macro>

<#--
 * previousPage
 *
 * Print previous page link
 * Do not display link if the first page is active
 *
 * @param params is an object containing:
 *  - limit is the current limit
 *  - offset is the current offset
-->
<#macro previousPage params>
    <#assign prev = params.offset - params.limit />
    <#if (prev >= 0)>
        <#if queryString??>
            <#assign urlParam = queryString.withOffset(prev) />
        <#else>
            <#assign urlParam = "?offset=" + prev  />
        </#if>
        
        <a href="${springMacroRequestContext.requestUri}${urlParam}" class="prev"><span class="icon-small arrow-left"></span> <@u.message "pagination.prev" /></a>
        
    <#else>
        <span class="prev"><span class="icon-small arrow-left"></span> <@u.message "pagination.prev" /></span>
    </#if>
</#macro>

<#--
 * nextPage
 *
 * Print next page link
 * Do not display link if there is no more pages
 *
 * @param params is an object containing:
 *  - total is the total amount of filtered initiatives
 *  - limit is the current limit
 *  - offset is the current offset
-->
<#macro nextPage params>
    <#assign totalPages = (params.total / params.limit)?ceiling />
    <#assign next = params.offset + params.limit />
    
    <#if (next < totalPages * params.limit)>
        <#if queryString??>
            <#assign urlParam = queryString.withOffset(params.offset + params.limit) />
        <#else>
            <#assign urlParam = "?offset=" + next  />
        </#if>
    
        <a href="${springMacroRequestContext.requestUri}${urlParam}" class="next"><@u.message "pagination.next" /> <span class="icon-small arrow-right"></span></a>
    <#else>
        <span class="next"><@u.message "pagination.next" /> <span class="icon-small arrow-right"></span></span>
    </#if>
</#macro>

<#--
 * limiters
 *
 * Print limiter links
 * Compress to remove whitespaces
 *
 * @param limits the set of all limits
 * @param limit is the current limit
 * @param is size of the maximum limit
-->
<#macro limiters limits limit maxLimit>
<@compress single_line=true>
    <span class="pagination-limiter">
        <@u.message "pagination.limiter" />
        <#list limits as l>
            <#if l != limit>
                <a href="${springMacroRequestContext.requestUri}${queryString.withLimit(l)}">${l}</a>
            <#else>
                <span class="active">${l}</span>
            </#if>
        </#list>
    </span>
</@compress>
</#macro>

<#--
 * pagination
 *
 * Print pagination if more than 1 page
 * Show pagination when more than minimum pagination count
 *
 * @param params is an object
 * @param cssClass for custom class
-->
<#macro pagination params cssClass>
    <#assign limits = [20, 100, 500]>
    <#assign totalPages = (params.total / params.limit)?ceiling />
    <#assign showPagination = (totalPages > 1) />
    <#assign showLimits = (params.total > limits[0]) />

    <#if showPagination || showLimits>
        <div class="pagination cf ${cssClass}">
            
                <#if showPagination>
                    <div class="pagination-links">
                        <@previousPage params />
            
                        <@pages params />
                        
                        <@nextPage params />
                    </div>
                </#if>
            
                <#if params.enableLimits && showLimits>
                    <@limiters limits params.limit 500 />
                </#if>
            
         </div>
     </#if>
 </#macro>

<#--
 * showMore
 * Pagination for mobile view.
-->
<#macro showMore params>
    <#if params.total gt params.limit>
        <a class="show-more" href="${springMacroRequestContext.requestUri}${queryString.withLimit(params.limit + 100)}">Näytä lisää</a>
    </#if>
</#macro>
 
</#escape>