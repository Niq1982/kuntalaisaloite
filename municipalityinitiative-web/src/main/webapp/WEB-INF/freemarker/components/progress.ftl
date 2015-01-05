<#import "utils.ftl" as u />

<#escape x as x?html>

<#-- 
 * progress
 * 
 * Generates initiative's progress graph
 *
 * @param initiative is initiative
 * @param public is boolean for public or management/moderation view
-->
<#macro progress initiative public=true>
	<#assign isDraftDone = true />
	<#assign isSentDone = initiative.sentTime.present />
	<#assign isCollectDone = !isSentDone && initiative.state == InitiativeState.PUBLISHED || isSentDone />
	<#assign isPublishDone = isCollectDone />
	<#assign isManagementDone = !isPublishDone && (initiative.state == InitiativeState.REVIEW || initiative.state == InitiativeState.ACCEPTED) || isPublishDone />

	<#assign createTime><@u.localDate initiative.createTime /></#assign>
    <#assign initiativeCreated>Aloite on julkaistu<br/>${createTime}</#assign>

	<#if initiative.sentTime.present>
        <#assign sentTime><@u.localDate initiative.sentTime.value /></#assign>
        <#assign sendToMunicipality>Aloite lähetetty kuntaan<br/>${sentTime}</#assign>
    <#else>
    	<#assign sendToMunicipality>Aloite lähetetään kuntaan</#assign>
   	</#if>

	<#-- PUBLIC VIEW -->
	<#if public>
		<#-- SINGLE -->
		<#assign steps = [
			{
				"label": initiativeCreated,
				"done": isPublishDone,
				"icon": "publish"
			},
			{
				"label": sendToMunicipality,
				"done": isSentDone,
				"icon": "sent"
			}
		]>
	
		<#-- COLLABORATIVE -->
		<#if initiative.collaborative>
			<#if initiative.fixState != FixState.OK>
		        <#assign initiativeCollecting><@u.message key="initiative.fixStateInfo."+initiative.fixState /></#assign>
		    <#elseif initiative.state?? && initiative.state == InitiativeState.PUBLISHED>
		    	<#assign initiativeCollecting>Aloite kerää osallistujia</#assign>
		    </#if>
				
			<#assign steps = [
				{
					"label": initiativeCreated,
					"done": isPublishDone,
					"icon": "publish"
				},
				{
					"label": initiativeCollecting,
					"done": isCollectDone,
					"icon": "collect"
				},
				{
					"label": sendToMunicipality,
					"done": isSentDone,
					"icon": "sent"
				}
			]>
	    </#if>
  
  	<#-- MANAGEMENT/MODERATION VIEW -->
  	<#else>
		<#assign createTime><@u.localDate initiative.createTime /></#assign>
		<#assign initiativeDraftCreated>Aloite on luotu<br/>${createTime}</#assign>
		
	    <#if isCollectDone>
	    	<#assign initiativePublish>Aloite on julkaistu</#assign>
	    	<#assign initiativeCollecting>Aloite kerää osallistujia</#assign>
	    <#else>
	    	<#assign initiativePublish>Aloite julkaistaan</#assign>
	    	<#assign initiativeCollecting>Osallistujien kerääminen</#assign>
	    </#if>
		    
		<#-- SINGLE -->
    	<#assign steps = [
    		{
				"label": initiativeDraftCreated,
				"done": isDraftDone,
				"icon": "draft"
			},
			{
				"label": "Aloite odottaa hyväksyntää",
				"done": isManagementDone,
				"icon": "mgmnt"
			},
			{
				"label": initiativePublish,
				"done": isPublishDone,
				"icon": "mgmnt-publish"
			},
			{
				"label": sendToMunicipality,
				"done": isSentDone,
				"icon": "sent"
			}
		]>
	
		<#-- COLLABORATIVE or UNDEFINED -->
		<#if initiative.collaborative || (!initiative.collaborative && !initiative.single)>				
			<#assign steps = [
				{
					"label": initiativeDraftCreated,
					"done": isDraftDone,
					"icon": "draft"
				},
				{
					"label": "Aloitteen ylläpito",
					"done": isManagementDone,
					"icon": "mgmnt"
				},
				{
					"label": initiativePublish,
					"done": isPublishDone,
					"icon": "mgmnt-publish"
				},
				{
					"label": initiativeCollecting,
					"done": isCollectDone,
					"icon": "collect"
				},
				{
					"label": sendToMunicipality,
					"done": isSentDone,
					"icon": "sent"
				}
			]>
	    </#if>
   	</#if>
  
  	<#assign barItems = 4 />
	<#if steps?size == 4>
        <#assign barItems = 3 />
    <#elseif steps?size == 5>
        <#assign barItems = 2 />
    </#if>

	<div class="initiative-progress-container">
	    <div class="initiative-progress steps-${steps?size}">
	    	<@compress single_line=true>
	    	<#list steps as step>
	    		<#if step_index lt steps?size - 1>
				    <div class="initiative-progress-bar bar-items-${barItems}">
				    	<#list 1..barItems as i>
				    		<span><span class="done"></span></span>
				    	</#list>
				    </div>
			    </#if>
		    </#list>
		    </@compress>
		    
		    <div>
		    	<#list steps as step>
				    <div class="step ${step.done?string("done", "")}">
				    	<div class="step-icon-holder"><i class="icon-progress icon-${step.icon}"></i></div>
				    	<span class="label"><#noescape>${step.label}</#noescape></span>
				    </div>
			    </#list>
		    </div>
		</div>
	</div>
</#macro>

</#escape> 