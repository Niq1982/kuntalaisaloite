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

<@l.main page="page.manageVideo">

	<@u.returnPrevious managementURL "link.to.managementView" />
	
	<@u.errorsSummary path="video.*" prefix="video."/>
    
    <div class="msg-block">
        <div class="system-msg msg-info">
            <h2><@u.message "video.videoUrl" /></h2>
			<p><@u.message "video.instruction.1"/></p>
			<p><@u.message "video.instruction.2"/></p>
        </div>
    </div>

    
    <div class="view-block cf">


        <h2><@u.message "video.attachVideoLink"/></h2>

        <#if initiative.videoUrl.isPresent() && initiative.videoName.isPresent()>
            <@e.video manage=true />
        </#if>
        <div class="initiative-content-row cf">

            <form id="form-video-url"  action="${urls.getManageVideoUrl(initiative.id)}" method="POST">
                <@f.securityFilters/>
                <@spring.bind "video" />
                <input type="hidden" name="locale" value="${locale}"/>
                <div class="input-block-content">
                    <@f.textField cssClass="videoUrl large" required="" optional=false path="video.videoUrl" key="video.videoUrl"/>
                </div>
                <div id="videoContainer" class="input-block-content  no-top-margin">

                </div>

                <div class="input-block-content no-top-margin">
                    <@f.textField cssClass="large" required="" optional=false path="video.videoName" key="video.videoName"/>
                </div>

                <div class="input-block-content no-top-margin">
                    <button type="submit" name="${UrlConstants.ACTION_ADD_VIDEO}" class="small-button dbl-click-check-no-msg"><span class="small-icon save-and-send"><@u.message "video.attach"/></span></button>
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
        
        <#-- Modal: Confirm remove Video-->
        modalData.deleteVideoForm = function() {
            return [{
                title:      '<@u.message "deleteVideo.confirm.title" />',
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

        var videoWarning = <@u.message "warning.videoUrl"/>;
    </script>

</@l.main>

<#macro deleteVideoForm >
    <form id="delete-attachment-form" method="POST">
        <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>

        <div class="input-block-content">
            <button type="submit" name="${UrlConstants.ACTION_REMOVE_VIDEO}" class="small-button"><span class="small-icon cancel"><@u.message "deleteVideo.btn" /></button>
            <a href="${springMacroRequestContext.requestUri}" class="push close"><@u.message "action.cancel" /></a>
        </div>
    </form>
</#macro>


</#escape> 