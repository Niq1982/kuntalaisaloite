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
    
    <@e.stateDates initiative.collectable />

    <#-- VIEW BLOCKS -->
    <div class="view-block public">
    
        <#-- TODO: The spec is unfinished. On hold. -->
    
        <h2>Äänioikeutetut osallistujat</h2>
        
        <#-- Testing modal with large list -->
        <#--<div class="css-cols-3"><ul class="no-style"><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li></ul></div>-->
        
        <#-- TODO: Generate as a component. Make cue points for columns -->
        <ul class="no-style">
            <#assign x = 100 />
            
            <#if ( x > 30)>
                <#assign columns=2 />
            <#elseif (x > 90)>
                <#assign columns=3 />
            <#else>
                <#assign columns=1 />
            </#if>
            
            <#list 1..x as i>
                <li>Matti Meikäläinen</li>
            </#list>
        </ul>

    <#--        
        <#if participants??>
            <#list participants.franchise as participant>
                <#if participant_index == 0><ul class="participants no-style"></#if>
                    <li>${participant}</li>
                <#if !participant_has_next></ul></#if>
            </#list>
        </#if>-->

        <#--
        <h2>Ei-äänioikeutetut osallistujat</h2>
        <#if participants??>
            <#list participants.noFranchise as participant>
                <#if participant_index == 0><ul class="participants no-style"></#if>
                    <li>${participant}</li>
                <#if !participant_has_next></ul></#if>
            </#list>
        </#if>
        -->
        
        <br/>
        <p><a href="${urls.view(initiative.id)}">Palaa aloitteen näkymään</a></p>
        
    </div>

    
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
</#escape> 

