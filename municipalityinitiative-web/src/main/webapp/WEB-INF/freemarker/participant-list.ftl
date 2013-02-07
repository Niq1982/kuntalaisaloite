<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />

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
    
    <span class="extra-info">
        <#if initiative.createTime??>
            <#assign createTime><@u.localDate initiative.createTime /></#assign>
            <@u.message key="initiative.date.create" args=[createTime] />
        </#if>
        <br />

        <#if initiative.sentTime.present>
            <#assign sentTime><@u.localDate initiative.sentTime.value /></#assign>
            <@u.message key="initiative.date.sent" args=[sentTime] />
        <#else>
            <@u.message "initiative.state.collecting" />
        </#if>
    </span>

    <#-- VIEW BLOCKS -->
    <div class="view-block public">
    
        <h2>Äänioikeutetut osallistujat</h2>
        
        <#if participants??>
            <#list participants.franchise as participant>
                <#if participant_index == 0><ul class="participants no-style"></#if>
                    <li>${participant}</li>
                <#if !participant_has_next></ul></#if>
            </#list>
        </#if>
        
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
    
        <#-- MODAL DATA (from collect-view.ftl) -->
        <#if modalData??><#noescape>${modalData}</#noescape></#if>
    
    </script>

    
</@l.main>
</#escape> 

