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
            <h2>Aloitteen videolinkki</h2>
			<p>Voit liittää aloitteeseen videon lisäämällä linkin videoon joka sijaitsee Youtube-verkkopalvelussa.</p>
			<p>Lataa haluamasi video Youtube-verkkopalveluun ja kopio videon linkkio osoiteriviltä Videolinkki -kenttään.</p>
        </div>
    </div>

    
    <div class="view-block cf">


        <h2>Liitä videolinkki</h2>

        <#if !managementSettings.isAddVideo()>
            <div class="delete-video-url">
                <iframe src="${video.videoUrl}"></iframe>
                <a href="?deleteVideoForm" class="js-delete-video delete-video trigger-tooltip"
                   title="<@u.message "deleteAttachment.btn" />"><span class="icon-small icon-16 cancel"></span></a>
                <p>${video.videoName}</p>
            </div>
        </#if>
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


        <div class="initiative-content-row cf last">
            <a href="${managementURL}" class="small-button" ><@u.message "attachment.ready" /></a>

        </div>

    </div>
    

    
    <@u.returnPrevious managementURL "link.to.managementView" />
    
    <#-- HTML for confirm delete Modal -->
    <#assign deleteVideo>
        <@compress single_line=true>
            <@deleteVideoForm />
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
        modalData.deleteVideoForm = function() {
            return [{
                title:      '<@u.message "deleteAttachment.confirm.title" />',
                content:    '<#noescape>${deleteVideo?replace("'","&#39;")}</#noescape>'
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

<#macro deleteVideoForm modal=true >
    <#if !modal><#assign attachmentId = RequestParameters['deleteVideoForm']?number /></#if>

    <form id="delete-attachment-form" action="<#if !modal>${urls.getManageAttachments(initiative.id)}</#if>" method="POST">
        <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>

        <#if modal>
            <div id="selected-attachment" class="details"></div>
            <br/>
        <#else>
            <@attachmentDetailsById RequestParameters['deleteVideoForm'] />
        </#if>

        <div class="input-block-content">
            <button type="submit" name="${UrlConstants.ACTION_REMOVE_VIDEO}" class="small-button"><span class="small-icon cancel"><@u.message "deleteAttachment.btn" /></button>
            <a href="${springMacroRequestContext.requestUri}" class="push close"><@u.message "action.cancel" /></a>
        </div>
    </form>
</#macro>


</#escape> 