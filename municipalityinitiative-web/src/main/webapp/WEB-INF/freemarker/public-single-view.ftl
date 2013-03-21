<#import "/spring.ftl" as spring />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/elements.ftl" as e />
<#import "components/forms.ftl" as f />
<#import "components/some.ftl" as some />

<#escape x as x?html> 

<#--
 * Layout parameters for HTML-title and navigation.
 * 
 * page = "page.initiative.public" or "page.initiative.unnamed"
 * pageTitle = initiative.name if exists, otherwise empty string
-->
<@l.main "page.initiative.public" initiative.name!"">

    <h1 class="name">${initiative.name!""}</h1>
    
    <div class="municipality">${initiative.municipality.name!""}</div>
    
    <@e.stateDates initiative />

    <#-- VIEW BLOCKS -->
    <div class="view-block public">
        
        <#if initiative.proposal??>
            <div class="initiative-content-row <#if !initiative.showName>last</#if>">
                <h2><@u.message "initiative.content.title" /></h2>
                
                <@u.text initiative.proposal!"" />
            </div>
        </#if>
        
         <#if initiative.showName>
             <div class="initiative-content-row last">
                <h2><@u.message "initiative.author.title" /></h2>
                <p>${initiative.authorName!""}</p>
            </div>
        </#if>
    </div>

    <#--
     * Social media buttons
    -->
    <@some.some pageTitle=initiative.name!"" />

</@l.main>

</#escape> 