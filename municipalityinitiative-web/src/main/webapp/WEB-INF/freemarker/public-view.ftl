<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
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
        <h2>Aloitteen sisältö</h2>
        
        ${initiative.proposal}
    </div>
    
    <#-- TODO: Extra details -->
    <div class="view-block public last">
        <div class="initiative-content-row last">
            
            
            <#-- One man's initiative -->
            <h2>Aloitteen tekijä</h2>
            <p>${initiative.contactName}</p>

            <#-- Initiative with more authors -->
            <#--
            <h2>Aloitteen tekijät - TODO</h2>
            <span class="user-count-total">353</span>
            <div class="join-as-user">
                <button class="small-button"><span class="small-icon save-and-send">Liity tekijäksi</span></button>
                <a class="push" href="#">Mitä tekijäksi liittyminen tarkoittaa?</a>
            </div>
            <br class="clear">

            <div class="top-margin cf">
                <div class="column col-1of2">
                    <p>Äänioikeutettuja Oulun kunnan jäseniä yhteensä<br>
                    <span class="user-count">217</span><br>
                    <a class="trigger-tooltip show-user-list-1" href="#" title="Näytä nimensä julkistaneiden lista">195 julkista nimeä</a><br>22 ei julkista nimeä</p>
                </div>
                <div class="column col-1of2 last">
                    <p>Ei äänioikeutettuja ja muita kuin Oulun kunnan jäseniä yhteensä<br>
                    <span class="user-count">195</span><br>
                    <a class="trigger-tooltip show-user-list-1" href="#" title="Näytä nimensä julkistaneiden lista">102 julkista nimeä</a><br>34 ei julkista nimeä</p>
                </div>
            </div>
            -->

        </div>     
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

