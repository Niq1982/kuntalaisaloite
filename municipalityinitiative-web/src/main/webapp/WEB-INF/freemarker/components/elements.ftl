<#import "utils.ftl" as u />
<#import "forms.ftl" as f />

<#escape x as x?html> 

<#-- 
 * initiativeView
 * 
 * Generates initiative's public view block
 *
 * @param initiative is initiative
-->
<#macro initiativeView initiative>
    <h2><@u.message "initiative.proposal.title" /></h2>

    <div class="initiative-content-row ${((initiative.extraInfo)?has_content)?string("","last")}">
        <@u.text initiative.proposal!"" />
    </div>

    <#if (initiative.extraInfo)?has_content>
        <h2><@u.message "initiative.extraInfo.title" /></h2>
        <div class="initiative-content-row last">
            <@u.text initiative.extraInfo!"" />
        </div>
    </#if>
</#macro>

<#-- 
 * stateInfo
 * 
 * Generates initiative's state dates
 *
 * @param initiative is initiative
-->
<#macro stateInfo initiative>
    
    <span class="extra-info">
        <#if initiative.sentTime.present>
            <#assign sentTime><@u.localDate initiative.sentTime.value /></#assign>
            <@u.message key="initiative.date.sent" args=[sentTime] />
        <#else>
            <#assign createTime><@u.localDate initiative.createTime /></#assign>
            <@u.message key="initiative.date.create" args=[createTime] />
            <#assign stateTime><@u.localDate initiative.stateTime/></#assign>
            
            <#if initiative.fixState != FixState.OK>
                <span class="bull">&bull;</span> <@u.message key="initiative.fixStateInfo."+initiative.fixState />
            <#elseif initiative.state??>
                <span class="bull">&bull;</span> <@u.message key="initiative.stateInfo."+initiative.state args=[stateTime]/>
                <#if initiative.state == InitiativeState.PUBLISHED && initiative.collaborative><@u.message key="initiative.stateInfo.collecting" /></#if>
            </#if>
        </#if>
    </span>

</#macro>

<#-- 
 * initiativeAuthor
 * 
 * Generates initiative's public author name
 *
 * @param initiative is initiative
-->
<#macro initiativeAuthor publicAuthors>
    <h3><@u.message key="initiative.authors.title" args=[publicAuthors.publicNameCount+publicAuthors.privateNameCount] /></h3>

    <#if (publicAuthors.publicNameCount > 0)>
        <#list publicAuthors.publicAuthors as publicAuthor>
            <div class="column ${((publicAuthor_index + 1) % 3 == 0)?string("last","")}">
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
    
    <@u.systemMessage path="initiative.authors.contactinfo.notPublic" type="info" showClose=false />
    <br />
    
    <#list authorList as a>
        <div class="column author ${((a_index + 1) % 3 == 0)?string("last","")}">
            <p><strong>${a.contactInfo.name!""}</strong>, <@u.solveMunicipality a.municipality/>
            <#if showRenewManagementHash>
                <a  href="#" class="js-renew-management-hash trigger-tooltip" title="<@u.message "moderator.renewManagementHash.tooltip" />"
                    data-id="${a.id}"
                    data-name="${a.contactInfo.name!""}"
                    data-municipality="<@u.solveMunicipality a.municipality/>"
                    data-address="${a.contactInfo.address!""}"
                    data-email="${a.contactInfo.email!""}"
                    data-phone="${a.contactInfo.phone!""}"><span class="icon-small icon-16 resend"></span></a>
            </#if>
            <br />
            <@u.scrambleEmail a.contactInfo.email!"" />
            <br />
            <#if a.contactInfo.address?? && a.contactInfo.address != ""><#noescape>${a.contactInfo.address?replace('\n','<br/>')!""}</#noescape><br /></#if>
            ${a.contactInfo.phone!""}</p>
        </div>
        <#if ((a_index + 1) % 3 == 0) || !a_has_next><br class="clear" /></#if>
    </#list>
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
<#macro participants formHTML="" showForm=true admin=false>
    <h3><@u.message key="initiative.participants.title" args=[participantCount.total+initiative.externalParticipantCount] />
    <#if admin && !initiative.isVerifiable()><span class="switch-view"><a href="${urls.participantListManage(initiative.id)}" class="trigger-tooltip" title="<@u.message "manageParticipants.tooltip" />"><@u.message "manageParticipants.title" /></a></span></#if></h3>
    
    <#if !user.hasRightToInitiative(initiative.id)>
        <#if initiative.verifiable && user.hasParticipatedToInitiative(initiative.id)>
            <@u.systemMessage path="warning.already.participated" type="warning" showClose=false />
        <#elseif initiative.verifiable && user.isVerifiedUser() && user.homeMunicipality.present && user.homeMunicipality.value.id != initiative.municipality.id>
            <@u.systemMessage path="warning.participant.notMember" type="warning" showClose=false />
        <#elseif initiative.verifiable && ((user.isVerifiedUser() && !user.homeMunicipality.present) || !user.isVerifiedUser()) >
            <@u.systemMessage path="participate.verifiable.info" type="info" showClose=false />
        </#if>
    </#if>
    
    <br/>
    <div class="participants-block">
        <span class="user-count-total">${participantCount.total+initiative.externalParticipantCount}</span>
    </div>
    <div class="participants-block separate">
        <span class="user-count-sub-total">
            <#if (participantCount.publicNames > 0)><span class="public-names"><a class="trigger-tooltip" href="${urls.participantList(initiative.id)}" title="<@u.message key="participantCount.publicNames.show"/>"><@u.message key="participantCount.publicNames" args=[participantCount.publicNames] /></a></span><br/></#if>
            <#if (participantCount.privateNames > 0)><span class="private-names"><@u.message key="participantCount.privateNames" args=[participantCount.privateNames] /></span><br/></#if>
            <#if (initiative.externalParticipantCount > 0)><span class="private-names"><@u.message key="participantCount.externalNames" args=[initiative.externalParticipantCount]/></span></p></#if>
        </span>
    </div>
    
    <#assign participateSuccess=false />
    <#list requestMessages as requestMessage>
        <#if requestMessage == RequestMessage.PARTICIPATE>
            <#assign participateSuccess=true />
        </#if>
    </#list>
    
    <#if !admin && !initiative.sentTime.present && !participateSuccess>
        <div class="participants-block ${showForm?string("hidden","")}">
        <#if initiative.verifiable && !user.isVerifiedUser()>
                <a class="small-button" href="${vetumaLoginToCurrentPage}?participate"><span class="small-icon save-and-send"><@u.message "action.authenticate" /></span></a>
        <#else>
            <#if !user.hasParticipatedToInitiative(initiative.id) && (!initiative.verifiable || user.homeMunicipality.notPresent || (user.homeMunicipality.value.id == initiative.municipality.id))>
                <a class="small-button js-participate" href="?participateForm=true#participate-form"><span class="small-icon save-and-send"><@u.message "action.participate" /></span></a>
            </#if>
        </#if>
        </div>
        <div class="participants-block last ${showForm?string("hidden","")}">
            <a title="<@u.messageHTML "action.participate.infoLink.title" />" href="${urls.help(HelpPage.PARTICIPANTS.getUri(locale))}"><@u.messageHTML "action.participate.infoLink" /></a>
        </div>
    </#if>
    <#if !admin && initiative.sentTime.present>
        <div class="participants-block last">
            <div class="participate not-allowed">
                <@u.systemMessage path="participate.sentToMunicipality" type="info" showClose=false />
            </div>
        </div>
    </#if>
    <br class="clear" />

    <#-- NOSCRIPT participate -->
    <#if showForm>
        <#noescape><noscript>
            <div id="participate-form" class="form-container cf top-margin">
                <h3><@u.message "participate.title" /></h3>
                ${formHTML!""}
            </div>
        </noscript></#noescape>
    </#if>
</#macro>


</#escape> 