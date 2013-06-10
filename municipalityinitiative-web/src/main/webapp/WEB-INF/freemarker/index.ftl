<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />

<#escape x as x?html> 
<@l.main "page.frontpage">
 
         <#if requestMessages??>
            <div class="container"><@u.requestMessage requestMessages /></div>
        </#if>
 
<div class="image-container-new">
</div>

<#--
<div class="image-container">
    <div class="inner">
        <div class="image image-1"></div>
        <div class="image image-2"></div>
        <div class="image image-3"></div>
        <div class="image image-4"></div>
        <div class="image image-5"></div>
        <div class="image image-6"></div>
    </div>
</div>
-->
 
<div class="container">
    <div class="big-link-holder">
        <a class="big-link"><@u.message "front.bigLink" /> <span class="arrow"></span></a>
    </div>

    <div id="content">
        

    
        <div class="front-container">
        
            <div class="faux-columns cf">
                <div class="col-1">
    
                    <div class="front-block block-1">
                        <h1><@u.message "front.hero.title" /></h1>
            
                        <p><@u.message "front.hero.description-1" /></p>
                        <p><@u.message "front.hero.description-2" /></p>
                        
                        <div class="bottom">
                            <a href="#" class="block-link"><@u.message "front.hero.readmore" /></a>
                        </div>
                    </div>
                
                </div>
                <div class="col-2">
                
                    <div class="front-block block-2">
                        <h2><@u.message "front.browse.title" /></h2>
            
                        <p><@u.message "front.browse.description" /></p>
                        
                        <#-- TODO: Chosen -->
                        <a href="#" class="block-link" style="margin-top:2em;">Helsinki</a>
                    </div>
                    
                    <div class="front-block block-3">
                        <h2><@u.message "front.latest.title" /></h2>
                        
                        <div class="search-results">
                            <#if initiatives?? && (initiatives?size > 0)>
                                <#list initiatives as initiative>
                                    <#if initiative_index == 0><ul class="initiative-list no-style"></#if>
                                    <li>
                                        <span class="date">${initiative.municipality.getName(locale)!""} <span class="push"><@u.localDate initiative.createTime!"" /></span></span>
                                        <a href="${urls.view(initiative.id)}" class="name"><@u.limitStringLength initiative.name!"" 150 /></a>
                                    </li>
                                    <#if !initiative_has_next></ul></#if>
                                </#list>
                                
                            <#-- Search results EMPTY -->
                            <#else>
                                <@u.systemMessage path="front.listEmpty" type="info" showClose=false />
                            </#if>
                        </div>
                    </div>
                
                </div>
            
            </div>
        
        </div>
    
    </div>
</div>

    
</@l.main>
</#escape> 

