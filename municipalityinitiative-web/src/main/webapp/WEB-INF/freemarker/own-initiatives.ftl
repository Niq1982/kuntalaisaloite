
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
        <div class="system-msg msg-info">Tällä sivulla listan niistä luomistasi aloitteista, jotka tähtäävät 2 tai 5 prosenttiin kunnan äänioikeutetuista asukkaista.</div>
    </div>
    

<div class="search-results">
<#if initiatives?? && (initiatives?size > 0)>
    <#list initiatives as initiative>
        <#if initiative_index == 0><ul></#if>
        <li <#if initiative_index == 0>class="first"</#if>>

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

            <span class="date trigger-tooltip" title="<@u.message "searchResults.initiative.date" />" ><@u.localDate initiative.createTime!"" /></span>
            <span class="title">
                <a href="${urls.management(initiative.id)}" class="name">
                    <#if initiative.name??>
                        <@u.limitStringLength initiative.name!"" 150 />
                    <#else>
                        Luonnos
                    </#if>
                </a>
            </span>
            <#if !initiative.public>
                <span class="info">${initiative.municipality.getName(locale)!""}<span class="bull">&bull;</span><span class="state"><@u.message "searchResults.notPublic" /></span></span>
            <#elseif !initiative.sentTime.present>
                <span class="info">${initiative.municipality.getName(locale)!""}<span class="bull">&bull;</span><span class="state"><@u.message "initiative.state.collecting" /></span></span>
            <#else>
                <#assign sentTime><@u.localDate initiative.sentTime.value!"" /></#assign>
                <span class="info">${initiative.municipality.getName(locale)!""}<span class="bull">&bull;</span><span class="state"><@u.message key="initiative.date.sent" args=[sentTime] /></span></span>
            </#if>

        </li>
        <#if !initiative_has_next></ul></#if>
    </#list>

<#-- Search results EMPTY -->
<#else>
    <div class="msg-block">
        <@u.systemMessage path="searchResults.noResults" type="info" cssClass="first" showClose=false />
    </div>
</#if>

</div>


</@l.main>

</#escape>