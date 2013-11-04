<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/forms.ftl" as f />

<#escape x as x?html> 
<@l.main "page.frontpage">

<#assign imageNumber="1">

<#function rand min max>
  <#local now = .now?long?c />
  <#local randomNum = _rand +
    ("0." + now?substring(now?length-1) + now?substring(now?length-2))?number />
  <#if (randomNum > 1)>
    <#assign _rand = randomNum % 1 />
  <#else>
    <#assign _rand = randomNum />
  </#if>
  <#return (min + ((max - min) * _rand))?round />
</#function>

<#assign _rand = 0.36 />
<#assign imageNumber = rand(1, 4)?c />

<div class="image-container image-${imageNumber}">
<#if requestMessages?? && (requestMessages?size > 0)>
    <@u.frontpageRequestMessage requestMessages />
</#if>
</div>

<div class="container">
    <div class="big-link-holder noprint">
        <a href="${urls.help(HelpPage.ORGANIZERS.getUri(locale))}" class="big-link"><@u.message "front.bigLink" /> <span class="arrow"></span></a>
    </div>

    <div id="content">
        
        <div class="front-container">
        
            <div class="faux-columns cf">
                <div class="col-1">
    
                    <div class="front-block block-1">
                        <h1><@u.message "front.hero.title" /></h1>
            
                        <p><@u.message "front.hero.description-1" /></p>
                        <p><@u.message "front.hero.description-2" /></p>
                        <p><@u.message "front.hero.description-3" /></p>
                        
                        <#-- Add later if needed
                        <div class="bottom">
                            <a href="${urls.helpIndex()}" class="block-link"><@u.message "front.hero.readmore" /></a>
                        </div>
                        -->
                    </div>
                
                </div>
                <div class="col-2">
                
                    <div class="front-block block-2 noprint">
                        <h2><@u.message "front.browse.title" /></h2>
            
                        <p><@u.message "front.browse.description" /></p>
                        
                        <div class="flat-style">
                            <form action="${urls.search()}" method="GET" id="search-form" class="search-form">
                            <input type="hidden" name="show" value="all"/>
                                <select name="municipality" id="municipality" class="chzn-select municipality-filter" data-initiative-municipality="" data-placeholder="<@u.message "front.browse.choose" />">
                                    <option value=""><@u.message "currentSearch.municipality.all" /></option>
                                    <#list municipalities as municipality>
                                    <#if municipality.active>
                                        <option value="${municipality.id}">${municipality.getName(locale)}</option>
                                    </#if>
                                    </#list>
                                </select>
                                
                                <#-- Submit button for NOSCRIPT users -->
                                <noscript>
                                    <button type="submit" class="small-button"><span class="small-icon search"><@u.message "btn.search" /></span></button>
                                </noscript>
                            </form>
                        </div>
                    </div>
                    
                    <div class="front-block block-3">
                        <h2><@u.message "front.latest.title" /></h2>
                    
                        <#if initiatives?? && (initiatives?size > 0)>
                            <#list initiatives as initiative>
                                <#if initiative_index == 0><ul class="initiative-list no-style"></#if>
                                <li>
                                    <span class="date">${initiative.municipality.getName(locale)!""} <span class="push"><@u.localDate initiative.stateTime!"" /></span></span>
                                    <a href="${urls.view(initiative.id)}" class="name"><@u.limitStringLength initiative.name!"" 150 /></a>
                                </li>
                                <#if !initiative_has_next></ul></#if>
                            </#list>
                            
                        <#-- Search results EMPTY -->
                        <#else>
                            <@u.systemMessage path="front.listEmpty" type="info" />
                        </#if>
                    </div>
                
                </div>
            
            </div>
        
        </div>
    
    </div>
</div>

    
</@l.main>
</#escape> 

