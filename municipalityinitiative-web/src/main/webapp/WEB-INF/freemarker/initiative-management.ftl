<#import "/spring.ftl" as spring />
<#import "components/forms.ftl" as f />
<#import "components/layout.ftl" as l />
<#import "components/general-messages.ftl" as m />
<#import "components/utils.ftl" as u />
<#import "components/flow-state.ftl" as flow />
<#import "components/view-blocks.ftl" as view />
<#import "components/edit-blocks.ftl" as edit />
<#import "components/some.ftl" as some />

<#escape x as x?html> 

<#if managementSettings.editMode != EditMode.NONE && managementSettings.editMode != EditMode.FULL>
    <#global showExposeMask=true />
</#if>

<#--
 * Layout parameters for HTML-title and navigation.
 * 
 * page = "page.management" or "page.createNew"
 * pageTitle = initiative.name if exists, otherwise empty string
-->
<#if initiative??>
    <#if initiative.name[locale]?? || initiative.name[altLocale]??>
        <#assign page="page.management" />
        <#assign pageTitle><@u.text initiative.name /></#assign>
    <#else>
        <#assign page="page.createNew" />
    </#if>
</#if>

<@l.main page pageTitle!"">

    <h1>
    <#if pageTitle??>
        <#noescape>${pageTitle}</#noescape>
    <#else>
        <@u.message page />
    </#if>
    </h1>
    <#if initiative.startDate??>
    <span class="extra-info"><@u.localDate initiative.startDate /> - <@u.message "page.management" /></span>
    </#if>
    
    <#--
      * Logic for showing info-message wrapper
      * - Known issue: Shows incorrectly in VRK-view in REVIEW-state
    -->
    <#assign showInfoMsg = false />
    <#if    (initiative.currentAuthor?? && managementSettings.editMode != EditMode.FULL) ||
            (initiative.state != InitiativeState.DRAFT && initiative.state != InitiativeState.PROPOSAL)>
            
        <#assign showInfoMsg = true />
    </#if>
    
    <#-- TOP CONTRIBUTION -->
    <#if showInfoMsg>
        <div class="system-msg msg-summary">
            <#noescape>${topContribution!""}</#noescape>
        </div>
    </#if>
        
    <#noescape>${topContributionVRK!""}</#noescape>   
   
    <#-- Full edit form errors summary -->
    <#if managementSettings.editMode == EditMode.FULL>
        <@u.errorsSummary path="initiative.*" prefix="initiative."/>
    </#if> 

    <#-- Keeps the block-mode editing in right place when errors occurs -->
    <#switch managementSettings.editMode>
      <#case EditMode.BASIC>
         <#assign currentBlockHash="#initiative.basicDetails.title" />
         <#break>
      <#case EditMode.EXTRA>
         <#assign currentBlockHash="#initiative.extraDetails.title" />
         <#break>
      <#case EditMode.CURRENT_AUTHOR>
         <#assign currentBlockHash="#initiative.currentAuthorDetails.title" />
         <#break>
     <#case EditMode.ORGANIZERS>
         <#assign currentBlockHash="#initiative.organizerDetails.title" />
         <#break>
      <#default>
         <#assign currentBlockHash="" />
    </#switch>
    
    <#-- FORM. Use class 'sodirty' to enable dirtylisten. -->
    <#if managementSettings.editMode != EditMode.NONE>
        <form action="${springMacroRequestContext.requestUri}${currentBlockHash}" method="POST" id="form-initiative" class="sodirty" >
           <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>
           <input type="hidden" name="edit" value="${managementSettings.editMode}" />
    </#if>

        <#if managementSettings.editMode == EditMode.FULL>
        
            <div class="form-block-container">
                <@edit.blockHeader key="initiative.municipality.title" step=1 />
                <@edit.municipalityBlock step=1 />
            </div>
        
            <div class="form-block-container">
                <@edit.blockHeader key="initiative.initiative.title" step=2 />
                <@edit.initiativeBlock step=2 />
            </div>
        
            <div class="form-block-container">
                <@edit.blockHeader key="initiative.currentAuthor.title" step=3 />
                <@edit.currentAuthorBlock step=3 />
            </div>
        
            <div class="form-block-container">
                <@edit.blockHeader key="initiative.save.title" step=4 />
                <@edit.saveBlock step=4 />
            </div>
        
        </#if>

        
    <#if managementSettings.editMode != EditMode.NONE>
        </form>
    </#if>
    
    <#-- BOTTOM CONTRIBUTION -->
    <#if showInfoMsg>
        <div class="system-msg msg-summary">
            <#noescape>${bottomContribution!""}</#noescape>
        </div>
    </#if>
    
<#--
 * Management VIEW and EDIT modals
 * 
 * Uses jsRender for templating.
 * Same content is generated for NOSCRIPT and for modals.
 *
 * Modals:
 *  Form modified notification (dirtyform)
-->
<@u.modalTemplate />

<script type="text/javascript">
    var modalData = {};

    <#-- Modal: Form modified notification. Uses dirtyforms jQuery-plugin. -->
    modalData.formModifiedNotification = function() {
        return [{
            title:      '<@u.message "form.modified.notification.title" />',
            content:    '<@u.messageHTML "form.modified.notification" />'
        }]
    };
</script>



</@l.main>
</#escape> 
