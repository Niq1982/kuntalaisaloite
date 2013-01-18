<#import "/spring.ftl" as spring />
<#import "components/forms.ftl" as f />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/edit-blocks.ftl" as edit />
<#import "components/some.ftl" as some />

<#escape x as x?html> 

<#--
 * Layout parameters for HTML-title and navigation.
 * 
 * @param page is "page.createNew"
 * @param pageTitle can be assigned as custom HTML title
-->
<#assign page="page.createNew" />

<@l.main page pageTitle!"">

    <h1><@u.message page /></h1>
    
    <#-- TOP CONTRIBUTION -->
    <#noescape>${topContribution!""}</#noescape>
   
    <#-- Full edit form errors summary -->
    <@u.errorsSummary path="initiative.*" prefix="initiative."/>
    
    

    <#-- FORM. Use class 'sodirty' to enable dirtylisten. -->
    <form action="${springMacroRequestContext.requestUri}" method="POST" id="form-initiative" class="sodirty <#if hasErrors>has-errors</#if>">
        <#--<input type="hidden" name="CSRFToken" value="${CSRFToken}"/>-->

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
    </form>
    
    <#-- BOTTOM CONTRIBUTION -->
    <#noescape>${bottomContribution!""}</#noescape>

    
<#--
 * Create page modals
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
