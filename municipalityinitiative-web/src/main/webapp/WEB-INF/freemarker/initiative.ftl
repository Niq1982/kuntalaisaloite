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

    <#-- TOP INFOS -->
    <#if topInfo??><#noescape>${topInfo}</#noescape></#if>

    <div class="municipality">${initiative.municipality.name!""}</div>

    <h1 class="name">${initiative.name!""}</h1>
    
    <@e.stateDates initiative />

    <#-- TOP CONTRIBUTION -->
    <#if topContribution??><#noescape>${topContribution}</#noescape></#if>

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

    
    <#-- BOTTOM CONTRIBUTION -->
    <#if bottomContribution??><#noescape>${bottomContribution}</#noescape></#if>
    
    
    
    <#--
     * Public VIEW modals
     * 
     * Uses jsRender for templating.
     * Same content is generated for NOSCRIPT and for modals.
     *
     * Modals:
     *  Request message (defined in macro u.requestMessage)
     *
     * jsMessage:
     *  Warning if cookies are disabled
    -->
    <@u.modalTemplate />
    <@u.jsMessageTemplate />
    
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
    
        <#-- Modal: Form modified notification. Uses dirtyforms jQuery-plugin. -->
        modalData.formModifiedNotification = function() {
            return [{
                title:      '<@u.message "form.modified.notification.title" />',
                content:    '<@u.messageHTML "form.modified.notification" />'
            }]
        };
    
        <#-- MODAL DATA (from collect-view.ftl) -->
        <#if modalData??><#noescape>${modalData}</#noescape></#if>
    </script>

</@l.main>
</#escape> 

