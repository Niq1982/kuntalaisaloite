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

    <div class="municipality">${initiative.municipalityName!""}</div>

    <h1 class="name">${initiative.name!""}</h1>
    
    <#-- TOP CONTRIBUTION -->
    <#if topContribution??><#noescape>${topContribution}</#noescape></#if>

    <#-- VIEW BLOCKS -->
    <div class="view-block public">
    
        <#if initiative.proposal??>
            <div class="initiative-content-row <#if !initiative.showName>last</#if>">
                <h2>Aloitteen sisältö</h2>
                
                <@u.text initiative.proposal!"" />
            </div>
        </#if>
        
         <#if initiative.showName>
             <div class="initiative-content-row last">
                <h2>Aloitteen tekijä</h2>
                <p>${initiative.authorName!""}</p>
            </div>
        </#if>
    </div>
    
    +
        <@u.errorsSummary path="participant.*" prefix="participant."/>
    <@spring.bind "participant.*" />
    <#if spring.status.error>
        virhe
    </#if>
    <#list spring.status.errors.allErrors as error>
        * ${springMacroRequestContext.getMessage(error)}
    </#list>
    +

    <form action="${springMacroRequestContext.requestUri}" method="POST" id="form-participate" class="sodirty">
    
     <div class="input-block-content no-top-margin flexible">
        <@f.textField path="participant.participantName" required="required" optional=false cssClass="large" maxLength="512" />
        <@f.formCheckbox path="participant.showName" />
    </div>

    <div class="column col-1of2">
        <div class="input-block-content flexible">
            <#-- TODO: Preselect municipality -->
            <@f.formSingleSelect path="participant.homeMunicipality" options=municipalities required="required" cssClass="municipality-select" />
        </div>
    </div>
    <div class="column col-1of2 last">
        <div id="franchise" class="input-block-content flexible">
            <@f.radiobutton path="participant.franchise" required="required" options={"false":"initiative.franchise.false", "true":"initiative.franchise.true"} attributes="" />
        </div>
    </div>
    
    <div class="input-block-content flexible">
        <button type="submit" name="action-save" class="small-button" ><span class="small-icon save-and-send"><@u.message "action.save" /></span></button>
        <a href="${springMacroRequestContext.requestUri}#participants" class="push close"><@u.message "action.cancel" /></a>
    </div>
    
    </form>
    <br class="clear" />
    
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

