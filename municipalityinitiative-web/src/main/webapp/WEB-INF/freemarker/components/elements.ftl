<#import "utils.ftl" as u />
<#import "forms.ftl" as f />

<#escape x as x?html>

<#--
 * participantGraph
 *
 * Displays participation counts Raphael.js graph
 *
-->
<#macro participantGraph initiative data totalParticipantCount>
    <#if initiative.isCollaborative()>
        <div class="participant-graph">

            <h3 class="inline-style">
                <@u.message key="graph.title" />
                <span id="internal-support-count-${initiative.id?c}" >
                    <span >${totalParticipantCount!0}</span>
                </span>
            </h3>
            <div id="participantGraph">
                <noscript><@u.message key="graph.javaScriptSupport" /></noscript>
            </div>
            <div class="update-info">
                <@u.message key="graph.updateInfo" /><br/>
                <a href="${urls.widget(initiative.id)}"><@u.message key="graph.widgetLink" /></a>
            </div>
        </div>

         <script type="text/javascript">
            (function(window) {
                window.participantGraph = {
                    votes : <#noescape>${data}</#noescape>,
                    startDate : <#if initiative.stateTime??>'${initiative.stateTime}'<#else>null</#if>,
                    endDate : <#if initiative.sentTime.present>'${initiative.sentTime.value}'<#else>null</#if>,
                    lang : {
                        btnCumul: '<@u.message key="graph.btnCumul" />',
                        btnDaily : '<@u.message key="graph.btnDaily" />',
                        btnZoomIn : '<@u.message key="graph.btnZoomIn" />',
                        btnZoomOut : '<@u.message key="graph.btnZoomOut" />'
                    }
                };
            }(window));
         </script>
    <#else>
        <h3><@u.message "initiative.notcollaborative" /></h3>
    </#if>
</#macro>

<#-- 
 * initiativeView
 * 
 * Generates initiative's public view block
 *
 * @param initiative is initiative
-->
<#macro initiativeView initiative>
    <#assign pageIsConfirmParticipation = currentRequestUri?ends_with("show-participate")/>
    <#assign showMap = locations?? && locations?size gt 0 && !pageIsConfirmParticipation && googleMapsEnabled />
    <#assign showVideo = initiative.videoUrl.isPresent() && videoEnabled/>

    <h2><@u.message "initiative.proposal.title" /></h2>

    <div class="initiative-content-row ${((initiative.extraInfo)?has_content)?string("","last")}">
        <@u.text initiative.proposal!"" />
    </div>

    <#if attachments??>
    	<@attachmentsView attachments />
    </#if>



    <#if (initiative.extraInfo)?has_content || showMap ||showVideo>
        <h2><@u.message "initiative.extraInfo.title" /></h2>
    </#if>

    <#if (initiative.extraInfo)?has_content>
        <div class="initiative-content-row last replace-links">
            <@u.text initiative.extraInfo!"" />
        </div>
    </#if>

    <#if showMap>
        <@map locations />
    </#if>

    <#if showVideo>
        <br/>
        <@video />
    </#if>

    <#if (initiative.youthInitiativeId.present)>
        <h2><@u.message "initiative.youthInitiative.title" /></h2>
        <div class="initiative-content-row last">
            <@u.message "initiative.youthInitiative.link" /> <a href="${urls.youthInitiativeWebUrl(initiative.youthInitiativeId.value)}">${urls.youthInitiativeWebUrl(initiative.youthInitiativeId.value)}</a>
        </div>
    </#if>

</#macro>

<#macro map locations>

        <@u.jsGoogleMapsLib />

        <div class="map-container initiative-content-row last">
            <div id="map-canvas-view">
                <noscript><@u.message key="map.javaScriptSupport" /></noscript>
            </div>
        </div>
        <script type="text/javascript">
            var initiative = {locations: []};
            <#list locations as location>
                initiative.locations.push({lat: ${locations[location_index].lat?c}, lng: ${locations[location_index].lng?c}});
            </#list>
        </script>

</#macro>

<#-- 
 * initiativeViewManage
 * 
 * Generates initiative's public view block
 *
 * @param initiative is initiative
-->
<#macro initiativeViewManage initiative>
    <#assign showMap = locations?? && locations?size gt 0 && googleMapsEnabled />
    <#assign showVideo = initiative.videoUrl.isPresent() && videoEnabled/>

    <h2><@u.message "initiative.proposal.title" /></h2>

    <div class="initiative-content-row ${((initiative.extraInfo)?has_content)?string("","last")}">
        <@u.text initiative.proposal!"" />
    </div>

	<@e.attachmentsView attachments />

    <#if managementSettings.allowAddAttachments>
    	<div class="initiative-content-row">
    		<a href="${urls.manageAttachments(initiative.id)}" class="small-button"><span class="small-icon add"><@u.message "attachment.add.btn" /></span></a>
		</div>
    </#if>



    <#if (initiative.extraInfo)?has_content || showMap || showVideo>
        <h2><@u.message "initiative.extraInfo.title" /></h2>
    </#if>

    <#if (initiative.extraInfo)?has_content>
        <div class="initiative-content-row last replace-links">
            <@u.text initiative.extraInfo!"" />
        </div>
    </#if>


    <#if showMap>
        <@map locations />
    </#if>

    <#if showVideo>
        <br/>
        <@video />
    </#if>


    <#if (initiative.youthInitiativeId.present)>
        <h2><@u.message "initiative.youthInitiative.title" /></h2>
            <div class="initiative-content-row last">
            <@u.message "initiative.youthInitiative.link" /> <a href="${urls.youthInitiativeWebUrl(initiative.youthInitiativeId.value)}">${urls.youthInitiativeWebUrl(initiative.youthInitiativeId.value)}</a>
            </div>
    </#if>

</#macro>

<#-- 
 * attachmentsView
 * 
 * Generates attachment PDF and thumbnail list with manage option
 *
 * @param attachments is all attachments
 * @param manage boolean for showing delete-button. Default is false
-->
<#macro attachmentsView attachments manage=false>
	<#if (attachments.images?size + attachments.pdfs?size) gt 0>
		<div class="initiative-content-row thumbnail-list cf">

        <#assign userCanRemoveAttachments = manage && user.hasRightToInitiative(initiative.id)/>
    	<h3><@u.message "attachments.title" /></h3>

		    <#list attachments.images as attachment>

		    	<div class="column col-1of4 ${((attachment_index + 1) % 4 == 0)?string("last","")}">
		    		<span class="thumbnail">
				        <a href="${urls.attachment(attachment.attachmentId, attachment.fileName, false)}" target="_blank">
				            <img src="${urls.getAttachmentThumbnail(attachment.attachmentId,  false)}" alt="<@u.stripHtmlTags attachment.description />" />
			            </a>
		            </span>
		            <span class="img-label"><@u.stripHtmlTags attachment.description />
			            <#if userCanRemoveAttachments >
		                    <a  href="?deleteAttachment=${attachment.attachmentId}" class="js-delete-attachment delete-attachment trigger-tooltip"
		                        data-id="${attachment.attachmentId}"
		                        data-name="<@u.stripHtmlTags attachment.description />"
		                        data-type="image"
		                        data-src="${urls.getAttachmentThumbnail(attachment.attachmentId, false)}" title="<@u.message "deleteAttachment.btn" />"><span class="icon-small icon-16 cancel"></span></a></span>
						    </a>
				        </#if>
			        </span>
		        </div>
	            <#if ((attachment_index + 1) % 4 == 0) || !attachment_has_next><br class="clear" /></#if>

		    </#list>
	    </div>

	    <div class="initiative-content-row">
		    <#list attachments.pdfs as attachment>
	    		<#if attachment_index == 0><ul class="no-style"></#if>

			        <li class="pdf-attachment">
			        	<a href="${urls.attachment(attachment.attachmentId, attachment.fileName, false)}" target="_blank">
				            <@u.fileIcon type="pdf" />
				            <span class="pdf-label"><@u.stripHtmlTags attachment.description /></span>
			            </a>

			            <#if userCanRemoveAttachments >
		                    <a  href="?deleteAttachment=${attachment.attachmentId}" class="js-delete-attachment trigger-tooltip"
		                        data-id="${attachment.attachmentId}"
		                        data-name="<@u.stripHtmlTags attachment.description />"
		                        data-type="pdf" title="<@u.message "deleteAttachment.btn" />"><span class="icon-small icon-16 cancel"></span></a></span>
						    </a>
				        </#if>
			        </li>

		        <#if !attachment_has_next></ul></#if>
		    </#list>
	    </div>
    </#if>
</#macro>


<#--
 *
 * Municipality attachmentsview
 *
 *
 -->
<#macro municipalityAttachmentsView attachments manage=false>
    <#if (attachments.images?size + attachments.pdfs?size) gt 0>
    <div class="initiative-content-row thumbnail-list cf">

        <#assign userCanRemoveAttachments = manage />
        <#if !manage>
            <h3><@u.message "attachments.title" /></h3>
        </#if>
        <#list attachments.images as attachment>

            <div class="column col-1of4 ${((attachment_index + 1) % 4 == 0)?string("last","")}">
                <span class="thumbnail">
                    <a href="${urls.attachment(attachment.attachmentId, attachment.fileName, true)}" target="_blank">
                        <img src="${urls.getAttachmentThumbnail(attachment.attachmentId,  true)}" alt="<@u.stripHtmlTags attachment.description />" />
                    </a>
                </span>
            <span class="img-label"><@u.stripHtmlTags attachment.description />
                <#if userCanRemoveAttachments >
                    <a  href="?deleteAttachment=${attachment.attachmentId}" class="js-delete-attachment delete-attachment trigger-tooltip"
                        data-id="${attachment.attachmentId}"
                        data-name="<@u.stripHtmlTags attachment.description />"
                        data-type="image"
                        data-src="${urls.getAttachmentThumbnail(attachment.attachmentId, true)}" title="<@u.message "deleteAttachment.btn" />"><span class="icon-small icon-16 cancel"></span></a></span>
                    </a>
                </#if>
                </span>
            </div>
            <#if ((attachment_index + 1) % 4 == 0) || !attachment_has_next><br class="clear" /></#if>

        </#list>
    </div>

    <div class="initiative-content-row">
        <#list attachments.pdfs as attachment>
            <#if attachment_index == 0><ul class="no-style"></#if>

            <li class="pdf-attachment">
                <a href="${urls.attachment(attachment.attachmentId, attachment.fileName, true)}" target="_blank">
                    <@u.fileIcon type="pdf" />
                    <span class="pdf-label"><@u.stripHtmlTags attachment.description /></span>
                </a>

                <#if userCanRemoveAttachments >
                    <a  href="?deleteAttachment=${attachment.attachmentId}" class="js-delete-attachment trigger-tooltip"
                        data-id="${attachment.attachmentId}"
                        data-name="<@u.stripHtmlTags attachment.description />"
                        data-type="pdf" title="<@u.message "deleteAttachment.btn" />"><span class="icon-small icon-16 cancel"></span></a></span>
                    </a>
                </#if>
            </li>

            <#if !attachment_has_next></ul></#if>
        </#list>
    </div>
    </#if>
</#macro>

<#-- 
 * initiativeTitle
 * 
 * Generates initiative's title, municipality and initiative type
 *
 * @param initiative is initiative
-->
<#macro initiativeTitle initiative>
    <h1 class="name">${initiative.name!""}</h1>

    <div class="municipality">${initiative.municipality.getName(locale)} <span class="bull">&bull;</span> <@u.message "initiative.initiativeType."+initiative.type /></div>
</#macro>


<#-- 
 * initiativeAuthor
 * 
 * Generates initiative's public author name
 *
 * @param initiative is initiative
-->
<#macro initiativeAuthor publicAuthors>

    <#if (publicAuthors.publicNameCount > 0)>
        <#list publicAuthors.publicAuthors as publicAuthor>
            <div class="column author-list ${((publicAuthor_index + 1) % 3 == 0)?string("last","")}">
                <h4 class="header">${publicAuthor.name}</h4>
                <p><@u.solveMunicipality municipality=publicAuthor.municipality/></p>
            </div>
            <#if ((publicAuthor_index + 1) % 3 == 0) || !publicAuthor_has_next><br class="clear" /></#if>
        </#list>

    </#if>
    <#if (publicAuthors.publicNameCount == 0) && (publicAuthors.privateNameCount == 1)>
        <p><@u.message key="authors.onlyOnePrivate" /></p>
    <#elseif (publicAuthors.privateNameCount > 0)>
        <p><@u.messageHTML key="authors.privateAuthors" args=[publicAuthors.publicNameCount, publicAuthors.privateNameCount] /></p>
    </#if>
</#macro>

<#-- 
 * initiativeAuthor
 * 
 * Generates initiative's contact info for private views
 *
 * @param contactInfo is author.contactInfo
-->
<#macro initiativeContactInfo authorList showTitle=true showRenewManagementHash=false>
    <#if showTitle><h3><@u.message key="initiative.authors.title" args=[authorList?size] /></h3></#if>

    <@u.systemMessage path="initiative.authors.contactinfo.notPublic" type="info" />
    <br />

    <#list authorList as a>
        <div class="column author author-list ${((a_index + 1) % 3 == 0)?string("last","")}">
            <p><strong>${a.contactInfo.name!""}</strong>, <@u.solveMunicipality a.municipality/>
            <#if showRenewManagementHash>
                <a  href="#" class="js-renew-management-hash trigger-tooltip" title="<@u.message "moderator.renewManagementHash.tooltip" />"
                    data-id="${a.id}"
                    data-name="<@u.stripHtmlTags a.contactInfo.name!"" />"
                    data-municipality="<@u.solveMunicipality a.municipality/>"
                    data-address="${a.contactInfo.address!""}"
                    data-email="${a.contactInfo.email!""}"
                    data-phone="${a.contactInfo.phone!""}"><span class="icon-small icon-16 resend"></span></a>
            </#if>
            <br />
            <@u.scrambleEmail a.contactInfo.email!"" />
            <br />
            <#if a.contactInfo.address?? && a.contactInfo.address != "">
            	<#assign safeAddress><@u.stripHtmlTags a.contactInfo.address!"" /></#assign>
            	<#noescape>${safeAddress?replace('\n','<br/>')!""}</#noescape>
            	<br />
        	</#if>
            ${a.contactInfo.phone!""}</p>
        </div>
        <#if ((a_index + 1) % 3 == 0) || !a_has_next><br class="clear" /></#if>
    </#list>
</#macro>

<#macro participantInfo admin=false>
    <#assign showParticipangGraph = supportCountData?? && supportCountData!="[]" && participantCount.total gt 0 />
    <div class="participant-info view-block last <#if !showParticipangGraph> hide-in-mobile</#if>">
        <h2><@u.message key="initiative.participants.title" args=[participantCount.total] /></h2>
        <#if admin><h3><span><a href="${urls.participantListManage(initiative.id)}" class="trigger-tooltip" title="<@u.message "manageParticipants.tooltip" />"><@u.message "manageParticipants.title" /></a></span></h3></#if>
        <@e.participantInformation/>
        <#if showParticipangGraph>
            <@participantGraph initiative supportCountData!"{}" participantCount.total/>
        </#if>
    </div>
</#macro>

<#-- 
 * participants
 * 
 * Generates participants block with optional participate button and form
 *
 * @param formHTML is the markup for the form for NOSCRIPT-users
 * @param showForm is boolean for toggling form visibility 
 * @param admin is boolean for toggling participate button and participant manage -link 
-->
<#macro participants formHTML="" showForm=true>
    <#assign participateSuccess=false />
    <#list requestMessages as requestMessage>
        <#if requestMessage == RequestMessage.PARTICIPATE>
            <#assign participateSuccess=true />
        </#if>
    </#list>

    <@participateButton participateSuccess showForm/>


    <#-- NOSCRIPT participate -->
    <#if showForm>
        <#noescape><noscript>
            <div id="participate-form" class="form-container cf top-margin">
                <h3><@u.message "participate.title" /></h3>
            ${formHTML!""}
            </div>
        </noscript></#noescape>
    </#if>

    <#if initiative.sentTime.present>
        <div class="participants-block last noprint">
            <div class="participate not-allowed">
                <@u.systemMessage path="participate.sentToMunicipality" type="info" />
            </div>
        </div>
    </#if>

    <br class="clear" />

    <#if  !initiative.sentTime.present && !user.hasRightToInitiative(initiative.id)>
        <#if user.hasParticipatedToInitiative(initiative.id)>
            <@u.systemMessage path="warning.already.participated" type="warning" />
             <br class="clear" />
        <#elseif initiative.verifiable && user.isVerifiedUser() && !user.allowVerifiedParticipation(initiative.id, initiative.municipality)>
            <br class="clear" />
            <@u.systemMessage path="warning.participant.notMember" type="warning" />
        <#elseif initiative.verifiable && ((user.isVerifiedUser() && !user.homeMunicipality.present) || !user.isVerifiedUser()) >
            <@u.systemMessage path="participate.verifiable.info"+user.isVerifiedUser()?string(".verifiedUser","") type="info" />
            <br class="clear" />
        </#if>
    </#if>



</#macro>


<#macro participateButton participateSuccess showForm>
    <#if !initiative.sentTime.present && !participateSuccess>
        <div class="participants-block ${showForm?string("hidden","")} noprint">
            <#if initiative.verifiable>
                <#if user.isVerifiedUser()>
                    <#if !user.hasParticipatedToInitiative(initiative.id) && (user.homeMunicipality.notPresent || (user.homeMunicipality.value.id == initiative.municipality.id))>
                        <a class="small-button js-participate" href="?participateForm=true#participate-form"><span class="small-icon save-and-send"><@u.message "action.participate" /></span></a>
                    </#if>
                <#else>
                    <a class="small-button" href="${urls.vetumaLogin(currentRequestUri+"?show-participate")}"><span class="small-icon save-and-send"><@u.message "action.authenticate" /></span></a>
                </#if>
            <#else>
                <#if !user.hasParticipatedToInitiative(initiative.id)>
                    <a class="small-button js-participate" href="?participateForm=true#participate-form"><span class="small-icon save-and-send"><@u.message "action.participate" /></span></a>
                </#if>
            </#if>
        </div>
        <#if !user.hasParticipatedToInitiative(initiative.id)>
        <div class="participants-block last ${showForm?string("hidden","")} noprint">
            <a title="<@u.messageHTML "action.participate.infoLink.title" />" href="${urls.help(HelpPage.PARTICIPANTS.getUri(locale))}"><@u.messageHTML "action.participate.infoLink" /></a>
        </div>
        </#if>
    </#if>
</#macro>


<#macro follow >
    <#if initiative.isSent()>
        <#assign tooltipTitle>followInitiativeSent.tooltip</#assign>
    <#else>
        <#assign tooltipTitle>followInitiative.tooltip</#assign>
    </#if>
    <p class="noprint">
        <a class="js-follow trigger-tooltip" title="<@u.message tooltipTitle />">
            <span class="icon-small icon-16 envelope margin-right"></span>
            <@u.message "action.follow" />
        </a>

    </p>
</#macro>

<#macro participantInformation>
    <div class="participants-block">
        <span class="user-count-total test-user-count-total">${participantCount.total+initiative.externalParticipantCount}</span>
    </div>
    <div class="participants-block separate">
            <span class="user-count-sub-total">
                <#if (participantCount.publicNames > 0)><span class="public-names"><a class="trigger-tooltip" href="${urls.participantList(initiative.id)}" title="<@u.message key="participantCount.publicNames.show"/>"><@u.message key="participantCount.publicNames" args=[participantCount.publicNames] /></a></span></#if><br/>
                <#if (participantCount.privateNames > 0)><span class="private-names"><@u.message key="participantCount.privateNames" args=[participantCount.privateNames] /></span><br/></#if>
                <#if (initiative.externalParticipantCount > 0)><span class="private-names"><@u.message key="participantCount.externalNames" args=[initiative.externalParticipantCount]/></span></p></#if>
            </span>
    </div>
    <br class="clear" />
</#macro>


<#macro decisionBlock decisionInfo manage=false>
    <div class="view-block first cf">
        <div class="initiative-content-row last">
            <h2><@u.message "municipality.decision" /></h2>
            <#if decisionInfo.getDecisionText().isPresent()>
                <@u.text decisionInfo.getDecisionText().value />
            </#if>
            <#if manage>
                <a class="small-button edit-decision" href="${urls.openDecisionForEdit(initiative.id)}"><span class="small-icon edit"><@u.message "municipality.decision.editDecision" /> </span></a>
            </#if>
            <@municipalityAttachmentsView attachments=decisionInfo.attachments />
            <#if manage>
            <a class="small-button " href="${urls.openDecisionAttachmentsForEdit(initiative.id)}"><span class="small-icon edit"><@u.message "decision.edit.attachments" /></span></a>
            </#if>
        </div>
    </div>
</#macro>

<#macro video manage=false>
    <div>
        <#if initiative.videoUrl.isPresent() && initiative.videoUrl.getValue()?? && initiative.videoUrl.getValue() != "">
            <iframe src="${initiative.videoUrl.value}" width="90%" height="400px"></iframe>
        </#if>
    </div>
</#macro>

</#escape> 