<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/view-blocks.ftl" as view />
<#import "components/some.ftl" as some />

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

    <h1 class="name">${initiative.name}</h1>
    <#if initiative.createTime??>
        <span class="extra-info"><@u.localDate initiative.createTime /></span>
    </#if>
    
    <#-- TOP CONTRIBUTION -->
    <#if topContribution??><#noescape>${topContribution}</#noescape></#if>

    <#-- VIEW BLOCKS -->
    <div class="view-block public">
        <#--<@view.basicDetails />    
        <@view.extraDetails />-->
    </div>

    
    <#-- BOTTOM CONTRIBUTION -->
    <#if bottomContribution??><#noescape>${bottomContribution}</#noescape></#if>
    
    <@some.some pageTitle=currentPage />
    
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
    
    <#-- Modal: Request messages. Also in management-view. Check for components/utils.ftl -->
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

