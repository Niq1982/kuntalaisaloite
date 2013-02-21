<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/elements.ftl" as e />

<#escape x as x?html> 

<#--
 * Layout parameters for HTML-title and navigation.
 * 
 * page = "page.initiative.public" or "page.initiative.unnamed"
 * pageTitle = initiative.name if exists, otherwise empty string
-->
<#if initiative??>
    <#if initiative.name??>
        <#assign page="page.initiative.public" />
        <#assign pageTitle>${initiative.name}</#assign>
    <#else>
        <#assign page="page.initiative.unnamed" />
        <#assign pageTitle="" />
    </#if>
</#if>

<@l.main page pageTitle>

    <#-- TOP INFOS
    <#if topInfo??><#noescape>${topInfo}</#noescape></#if>
    -->

    <div class="municipality">${initiative.municipalityName!""}</div>

    <h1 class="name">${initiative.name!""}</h1>
    
    <@e.stateDates initiative />
    
    <@returnPrevious />

    <#-- VIEW BLOCKS -->
    <div class="view-block single public">
    
        <#-- TODO: Using request parameters. Finalize when we are sure that we want 2 different views-->
        <#if RequestParameters['show']?? && RequestParameters['show'] == "others">
            <h2><@u.message "participantList.noFranchise.title" /></h2>
            <@participantList participants.noFranchise />
        <#else>
            <h2><@u.message "participantList.franchise.title" /></h2>
            <@participantList participants.franchise />
        </#if>

    </div>
    
    <@returnPrevious />

    
    <#-- BOTTOM CONTRIBUTION
    <#if bottomContribution??><#noescape>${bottomContribution}</#noescape></#if>
     -->
    
    <#--
     * Public VIEW modals
     * 
     * Uses jsRender for templating.
     * Same content is generated for NOSCRIPT and for modals.
     *
     * Modals:
     *  Request message (defined in macro u.requestMessage)
    -->
    <@u.modalTemplate />
    
    <script type="text/javascript">
        var modalData = {};
        
        <#-- Modal: Request messages. Check for components/utils.ftl -->
        <#if requestMessageModalHTML??>    
            modalData.requestMessage = function() {
                return [{
                    title:      '<@u.message requestMessageModalTitle+".title" />',
                    content:    '<#noescape>${requestMessageModalHTML?replace("'","&#39;")}</#noescape>'
                }]
            };
        </#if>
    
    </script>

    
</@l.main>

<#macro participantList participants>

    <#assign participantCount = participants?size />
    <#assign cue = (participantCount/2)?ceiling />
        
    <#assign columns=1 />    
    <#-- We could use multiple columns -->
    <#--<#if ( participantCount > 19)>
        <#assign columns=2 />
    </#if>-->

    <div class="column col-1of2">

        <#list participants as participant>
            <#if participant_index == 0><ul class="participant-list no-style"></#if>
                <li><span class="date"><@u.localDate participant.participateDate!"" /></span> <span class="name-container"><span class="name">${participant.name!""}</span> <span class="home-municipality">- ${participant.homeMunicipality!""}</span></span></li>
                
                <#if columns == 2 && participant_index == cue>
                    </ul></div>
                    <div class="column col-1of2 last"><ul class="participant-list no-style">
                </#if>
                
            <#if !participant_has_next></ul></#if>
        </#list>
            
    </div>
    <br class="clear" />

</#macro>

<#-- 
 * returnPrevious
 *
 *  If request header referer equals management
 *      previousPageURI is the management URI
 *  Otherwise
 *      previousPageURI is the public view URI
-->
<#macro returnPrevious>
    <#if previousPageURI == urls.management(initiative.id, initiative.managementHash.value)>
        <p><a href="${previousPageURI}">&laquo; <@u.message "participantList.return.management" /></a></p>
    <#else>
        <p><a href="${previousPageURI}">&laquo; <@u.message "participantList.return.view" /></a></p>
    </#if>
</#macro>


</#escape> 

