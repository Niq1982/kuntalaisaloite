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
	
	<@u.errorsSummary path="video.*" prefix="video."/>
    
    <div class="msg-block">
        <div class="system-msg msg-info">
            <h2><@u.message "attachmentManage.title" /></h2>
			<p><@u.message "attachmentManage.description.1" /></p>
			<p><@u.message "attachmentManage.description.3" /><br/><a href="#"><@u.message "attachmentManage.readMore" /></a></p>
        </div>
    </div>

    
    <div class="view-block cf">


        <h2><@u.message "attachment.add.title" /></h2>

        <#assign videoPresent = video.videoUrl?? && video.videoName?? />
        <#if videoPresent>
            <div>
                <iframe src="${video.videoUrl}"></iframe>
                <a href="?deleteAttachment" class="js-delete-attachment delete-attachment trigger-tooltip"
                   title="<@u.message "deleteAttachment.btn" />"><span class="icon-small icon-16 cancel"></span></a>
                </a>
                <p>${video.videoName}</p>
            </div>
        <#else>
            <div class="initiative-content-row cf">

                <form id="form-video-url"  action="${urls.getManageVideoUrl(initiative.id)}" method="POST">
                    <@f.securityFilters/>
                    <@spring.bind "video" />
                    <input type="hidden" name="locale" value="${locale}"/>
                    <div class="input-block-content no-top-margin">
                        <@f.textField cssClass="videoUrl large" required="" optional=false path="video.videoUrl" key="video.videoUrl"/>
                    </div>
                    <div id="videoContainer">

                    </div>

                    <div class="input-block-content">
                        <@f.textField cssClass="large" required="" optional=false path="video.videoName" key="video.videoName"/>
                    </div>

                    <div class="input-block-content no-top-margin">
                        <button type="submit" name="${UrlConstants.ACTION_ADD_VIDEO}" class="small-button dbl-click-check-no-msg"><span class="small-icon save-and-send"><@u.message "attachment.save" /></span></button>
                    </div>

                </form>
            </div>
        </#if>


        <div class="initiative-content-row cf last">
            <a href="${managementURL}" class="small-button" ><@u.message "attachment.ready" /></a>

        </div>

    </div>
    

    
    <@u.returnPrevious managementURL "link.to.managementView" />
    
    <#-- HTML for confirm delete Modal -->
    <#assign deleteAattachment>
        <@compress single_line=true>
            <@e.deleteAattachmentForm />
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
<#macro attachmentDetailsById id municipality=false>
    <#list attachments.images as attachment>
        <#if attachment.attachmentId?string == id>
            <h4 class="header">${attachment.description}</h4>
            <img src="${urls.getAttachmentThumbnail(attachment.attachmentId, municipality)}" alt="${attachment.description}" />
        </#if>
    </#list>
    
    <#list attachments.pdfs as attachment>
        <#if attachment.attachmentId?string == id>
            <p class="pdf-attachment"><@u.fileIcon type="pdf" /> <span class="pdf-label">${attachment.description}</span></p>            
        </#if>
    </#list>
</#macro>


</#escape> 