<#import "/spring.ftl" as spring />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/forms.ftl" as f />
<#import "components/elements.ftl" as e />

<#escape x as x?html> 

<#assign managementURL = urls.getManagement(initiative.id) />

<#--
 * Layout parameters for HTML-title and navigation.
 * 
 * page = "page.management"
 * pageTitle = initiative.name if exists, otherwise empty string
-->

<@l.main page="page.manageAttachments">

	<@u.returnPrevious managementURL "link.to.managementView" />
	
	<@u.errorsSummary path="attachment.*" prefix="attachment."/>
    
    <div class="msg-block">
        <div class="system-msg msg-info">
            <h2><@u.message "attachmentManage.title" /></h2>
			<p><@u.message "attachmentManage.description.1" /></p>
			<p><@u.message key="attachmentManage.description.2" args=[imageProperties.getMaxFilesizeInKilobytes(), imageProperties.getMaxWidth(), imageProperties.getMaxHeight()] /></p>
			<p><@u.message "attachmentManage.description.3" /><br/><a href="#"><@u.message "attachmentManage.readMore" /></a></p>
        </div>
    </div>
    
    <#if !RequestParameters['deleteAttachment']??>
    
	    <div class="view-block cf">
		    <#if managementSettings.allowAddAttachments>
		    
		    	
		        
		        <h2><@u.message "attachment.add.title" /></h2>
				<div class="initiative-content-row cf">
				    
			        <form id="form-upload-image" enctype="multipart/form-data" action="${urls.addAttachment(initiative.id)}" method="POST">
			            <@f.securityFilters/>
	                    <@spring.bind "attachment" />
			            <input type="hidden" name="locale" value="${locale}"/>
			            
			            <div class="input-block-content no-top-margin">
	                        <@f.uploadField path="attachment.image" />
	                    </div>
					    	
			            <div class="input-block-content">
					    	<@f.textField path="attachment.description" required="" optional=true cssClass="large" maxLength=InitiativeConstants.ATTACHMENT_DESCRIPTION_MAX />
					    </div>
			            
			            <div class="input-block-content no-top-margin">
			            	<button type="submit" class="small-button" ><span class="small-icon save-and-send"><@u.message "attachment.save" /></span></button>
		            	</div>
			        </form>
		        </div>
		    </#if>
	    
	    	<@e.attachmentsView attachments=attachments manage=true />
	    	
	    	<div class="initiative-content-row cf last">
	        	<a href="${managementURL}" class="small-button" ><@u.message "attachment.ready" /></a>
	        </div>
	    
	    </div>
    
    <#else>
        <#-- Confirm Delete form for NOSCRIPT users -->
        <div class="msg-block cf">
        	<div class="system-msg msg-info cf">
            	<h2><@u.message "deleteAttachment.confirm.title" /></h2>
        	
            	<@deleteAattachmentForm modal=false />
            </div>
        </div>
    </#if>
    
    <@u.returnPrevious managementURL "link.to.managementView" />
    
    <#-- HTML for confirm delete Modal -->
    <#assign deleteAattachment>
        <@compress single_line=true>
            <@deleteAattachmentForm />
        </@compress>
    </#assign>
    

    <#--
     * Management VIEW modals
     * 
     * Uses jsRender for templating.
     * Same content is generated for NOSCRIPT and for modals.
     *
     * Modals:
     *  Confirm delete attachment
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
        
        <#-- Modal: Confirm remove attachment -->   
        modalData.deleteAttachment = function() {
            return [{
                title:      '<@u.message "deleteAttachment.confirm.title" />',
                content:    '<#noescape>${deleteAattachment?replace("'","&#39;")}</#noescape>'
            }]
        };

        var messageData = {};

        <#-- jsMessage: Warning if cookies are not enabled -->
        messageData.warningCookiesDisabled = function() {
            return [{
                type:      'warning',
                content:    '<h3><@u.message "warning.cookieError.title" /></h3><div><@u.messageHTML key="warning.cookieError.description" args=[managementURL] /></div>'
            }]
        };
    </script>

</@l.main>

<#-- 
 * attachmentDetailsById
 *
 * Prints attachment's details by id
 *
 * @param list is attachments object list
 * @param id is attachment's id
-->
<#macro attachmentDetailsById id>
    <#list attachments.images as attachment>
        <#if attachment.id?string == id>
            <h4 class="header">${attachment.description}</h4>
            <img src="${urls.getAttachmentThumbnail(attachment.attachmentId)}" alt="${attachment.description}" />
        </#if>
    </#list>
    
    <#list attachments.pdfs as attachment>
        <#if attachment.attachmentId?string == id>
            <p class="pdf-attachment"><@u.fileIcon type="pdf" /> <span class="pdf-label">${attachment.description}</span></p>            
        </#if>
    </#list>
</#macro>

<#-- 
 * deleteAattachmentForm
 *
 * Generates a form for deleting attachment
 *
 * @param modal is a boolean for selecting either JS- or NOSCRIPT-version
-->
<#macro deleteAattachmentForm modal=true>
	<#if !modal><#assign attachmentId = RequestParameters['deleteAttachment']?number /></#if>

    <form id="delete-attachment-form" action="<#if !modal>${urls.deleteAttachment(attachmentId)}</#if>" method="POST">
        <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>
        <input type="hidden" name="locale" value="${locale}"/>
        
        <#if modal>
            <div id="selected-attachment" class="details"></div>
            <br/>
        <#else>
            <@attachmentDetailsById RequestParameters['deleteAttachment'] />
        </#if>
        
        <div class="input-block-content">
            <button type="submit" class="small-button"><span class="small-icon cancel"><@u.message "deleteAttachment.btn" /></button>
            <a href="${springMacroRequestContext.requestUri}" class="push close"><@u.message "action.cancel" /></a>
        </div>
    </form>
</#macro>

</#escape> 