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

    <@e.initiativeTitle initiative />

    <@e.stateInfo initiative />

    <#-- VIEW BLOCKS -->
    <div class="view-block public first">
        <@e.initiativeView initiative />
    </div>
    
    <div class="view-block public">
        <div class="initiative-content-row last">
            <h2><@u.message key="initiative.people.title" args=[1] /></h2>
            <@e.initiativeAuthor authors />
        </div>
    </div>

    <#--
     * Social media buttons
    -->
    <#if initiative.state == InitiativeState.PUBLISHED>
        <@some.some pageTitle=initiative.name!"" />
    </#if>

</@l.main>

</#escape> 