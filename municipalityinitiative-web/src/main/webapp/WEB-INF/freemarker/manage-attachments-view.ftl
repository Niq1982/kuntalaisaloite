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
    
    <div class="msg-block">
        <div class="system-msg msg-info">
            <h2>Hallitse liitteitä</h2>
            <p>Voit lisätä aloitteelle jpg-, png- ja pdf-muotoisia liitetiedostoja aloitteelle. Liitetiedostoja eoi voi enää lisätä tai poistaa aloitteen julkaisun jälkeen.</p>
            <p>Jpg- ja png-kuvien maksimikoko on 1000x1000 pikseliä, ja pdf-tiedoston maksimikoko on X.0 MB</p>
        </div>
    </div>
    
    <div class="view-block cf">
	    <#if managementSettings.allowAddAttachments>
	        
	        <h2>Lisää liitteitä</h2>
			<div class="initiative-content-row cf">
			    <@u.errorsSummary path="attachment.*" prefix="attachment."/>
		        <form id="form-upload-image" enctype="multipart/form-data" action="${urls.addAttachment(initiative.id)}" method="POST">
		            <@f.securityFilters/>
                    <@spring.bind "attachment" />
		            <input type="hidden" name="locale" value="${locale}"/>
		            
		            <div class="input-block-content">
				    	<@f.textField path="attachment.description" required="" optional=false maxLength="${InitiativeConstants.ATTACHMENT_DESCRIPTION_MAX}" />
				    </div>

                    <div class="input-block-content no-top-margin">
                        <@f.uploadField path="attachment.image" />
                    </div>
		            
		            <div class="input-block-content no-top-margin">
		            	<#--<input type="submit" value="Tallenna tiedosto"/>-->
		            	
		            	<button type="submit" class="small-button" ><span class="small-icon save-and-send">Tallenna tiedosto</span></button>
	            	</div>
		        </form>
	        </div>
	        
	    </#if>
    
    	<@e.attachmentsView attachments=attachments manage=true />
    
    </div>
    
    <@u.returnPrevious managementURL "link.to.managementView" />
    

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
        <#-- TODO:
        <#if startCollecting??>    
            modalData.startCollecting = function() {
                return [{
                    title:      '<@u.message "startCollecting.confirm.title" />',
                    content:    '<#noescape>${startCollecting?replace("'","&#39;")}</#noescape>'
                }]
            };
        </#if>
        -->
            
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

</#escape> 