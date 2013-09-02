
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
<#assign page="page.ownInitiatives" />

<#assign searchTerm = RequestParameters['search']!"" />

<@l.main "page.ownInitiatives" pageTitle!"">

    <h1>
        <@u.message page />
        <span class="switch-view"><a href="${urls.search()}"><@u.message "page.find"/></a></span>
    </h1>

    <div class="msg-block">
        <@u.systemMessage path="ownInitiatives.description" type="info" cssClass="first" showClose=false />
        
        <#-- Initiative list EMPTY -->
        <#if !initiatives?? || initiatives?size == 0>
            <@u.systemMessage path="ownInitiatives.empty" type="info" showClose=false />
        </#if>    
    </div>
    

<#if initiatives?? && (initiatives?size > 0)>
<div class="search-results">
    <#list initiatives as initiative>
        <#if initiative_index == 0><ul></#if>
        <li <#if initiative_index == 0>class="first"</#if>>
            <a href="${urls.management(initiative.id)}">
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
    
                <span class="date trigger-tooltip" title="<@u.message "searchResults.initiative.date."+initiative.state />" ><@u.localDate initiative.stateTime /></span>
                <span class="title">
                    <span class="name">
                        <#if initiative.name??>
                            <@u.limitStringLength initiative.name!"" 150 />
                        <#else>
                            <@u.message "initiative.draft" />
                        </#if>
                    </span>
                </span>
                <span class="info">
                    <#if !initiative.public>
                        ${initiative.municipality.getName(locale)!""}<span class="bull">&bull;</span><span class="state"><@u.message "searchResults.notPublic" /></span>
                    <#elseif !initiative.sentTime.present>
                        ${initiative.municipality.getName(locale)!""}<span class="bull">&bull;</span><span class="state"><@u.message "initiative.state.collecting" /></span>
                    <#else>
                        <#assign sentTime><@u.localDate initiative.sentTime.value!"" /></#assign>
                        ${initiative.municipality.getName(locale)!""}<span class="bull">&bull;</span><span class="state"><@u.message key="initiative.date.sent" args=[sentTime] /></span>
                    </#if>
                    <span class="bull">&bull;</span>
                    <@u.message "initiative.initiativeType."+initiative.type />
                </span>
            </a>
        </li>
        <#if !initiative_has_next></ul></#if>
    </#list>
</div>
</#if>



</@l.main>

</#escape>