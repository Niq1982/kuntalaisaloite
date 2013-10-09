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
            <h2>Ylläpidä liitteitä</h2>
			<p>Voit liitää aloitteeseen jpg- ja png-kuvia sekä pdf-tiedostoja. Liitteitä ei voi enää lisätä tai poistaa aloitteen tarkastuksen ja julkaisun jälkeen. Liitteet näkyvät lisäysjärjestyksessä ja pdf-tiedostot ovat omassa listassaan kuvien alla.</p>
			<p>Liitettävien tiedostojen maksimikoko on 2 MB. Jpg- ja png-kuvien maksimimitta on 1000 x 1000 pikseliä, ja tätä suuremmat kuvat pienennetään automaattisesti.</p>
			<p>Ota huomioon, että sinulla tai jollain aloitteen vastuuhenkilöistä tulee olla käyttöoikeus liitettäviin kuviin ja tiedostoihin.<br/><a href="#">Lue lisää</a></p>
        </div>
    </div>
    
    <div class="view-block cf">
	    <#if managementSettings.allowAddAttachments>
	        
	        <h2>Liitä tiedostoja</h2>
			<div class="initiative-content-row cf">
		        <form id="form-upload-image" enctype="multipart/form-data" action="${urls.addAttachment(initiative.id)}" method="POST">
		            <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>
		            <input type="hidden" name="locale" value="${locale}"/>
		            
		            <div class="input-block-content no-top-margin">
		            	<input type="file" name="image" chars="40" />
		            </div>
		            
		            <div class="input-block-content"> 
					    <label for="name" class="input-header">
				        	Anna liitteelle selkeä ja kuvaava otsikko
				        	<span class="instruction-text">Liitteen otsikko tulee näkyviin aloitteen sivulle</span>
					    </label>
	
				    	<input type="text" name="description" class="large" maxlength="${InitiativeConstants.ATTACHMENT_DESCRIPTION_MAX}"/>
				    </div>
		            
		            <div class="input-block-content no-top-margin">
		            	<button type="submit" class="small-button" ><span class="small-icon save-and-send">Tallenna tiedosto</span></button>
	            	</div>
		        </form>
	        </div>
	    </#if>
    
    	<@e.attachmentsView attachments=attachments manage=true />
    	
    	<div class="initiative-content-row cf last">
        	<a href="${managementURL}" class="small-button" >Valmis</a>
        </div>
    
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