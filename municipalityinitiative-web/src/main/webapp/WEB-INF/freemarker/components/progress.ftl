<#import "utils.ftl" as u />

<#escape x as x?html>

<#-- 
 * progressBar
 * 
 * Generates progress bar between each progress step
 * Add fallbacks for pseudo CSS selectors for IE8
 *
 * @param steps is a hashMap for initiative's progress steps
-->
<#macro progressBar steps fixState>
	<@compress single_line=true>
	<#list steps as step>
		<#if step_index lt steps?size - 1>
		    <div class="initiative-progress-bar bar-items-${barItems} nth-child-${step_index + 1}">
		    	<#list 1..barItems as i>
		    		<#assign disableClass="" />
		    		<#if (fixState != FixState.OK && (step.icon != "draft" && step.icon != "mgmnt" || (step.icon == "mgmnt" && i != 1)))><#assign disableClass="disabled" /></#if>
		    		<#if fixState == FixState.FIX && step.icon == "draft" && i != 1 || (fixState == FixState.FIX && step.icon == "mgmnt")><#assign disableClass="disabled" /></#if>
		    		<span><span class="${(steps[step_index+1].done || step.done && i_index == 0)?string("done", "")} ${disableClass}"></span></span>
		    	</#list>
		    </div>
	    </#if>
	</#list>
	</@compress>
</#macro>

<#-- 
 * progressSteps
 * 
 * Generates initiative's progress steps
 * Add fallbacks for pseudo CSS selectors for IE8
 *
 * @param steps is a hashMap for initiative's progress steps
-->
<#macro progressSteps steps fixState>

	<div>

		<#assign activeLabel>
			<#noescape><@u.message "progress.management.draft" /></#noescape>
		</#assign>


		<#list steps as step>
			<#assign disableClass="" />
    		<#if (fixState != FixState.OK && (step.icon != "draft" && step.icon != "mgmnt"))><#assign disableClass="disabled" /></#if>
    		<#if fixState == FixState.FIX && step.icon != "draft"><#assign disableClass="disabled" /></#if>

			<#if step.done && (!step_has_next || !steps[step_index+1].done)><#assign active=true><#else><#assign active=false></#if>
			<#if active>
				<#assign activeLabel>
					<#noescape>${step.label}</#noescape>
				</#assign>
			</#if>

		    <div class="step nth-child-${step_index + 1} ${step_has_next?string("","last-child")} ${step.done?string("done", "")} ${disableClass} <#if active>active</#if>">
		    	<div class="step-icon-holder"><i class="icon-progress icon-${step.icon} " rel="tooltip" title="<#noescape>${step.label}</#noescape>"></i></div>
		    	<span class="label"><#noescape>${step.label}</#noescape></span>
				<#if active>
                    <span class="arrow-up "></span>
				</#if>

		    </div>

	    </#list>

	</div>

	</div>
	<div>
	<span class="mobile-label"><#noescape>${activeLabel}</#noescape></span>

</#macro>

<#-- 
 * progress
 * 
 * Generates initiative's progress graph
 *
 * @param initiative is initiative
 * @param public is boolean for public or management/moderation view
-->
<#macro progress initiative public=true omOrMunicipality=false>
	<#assign isDraftDone = true />
	<#assign isSentDone = initiative.sentTime.present />
	<#assign isCollectDone = !isSentDone && initiative.state == InitiativeState.PUBLISHED || isSentDone />
	<#assign isPublishDone = isCollectDone />
	<#assign isManagementDone = !isPublishDone && (initiative.state == InitiativeState.REVIEW || initiative.state == InitiativeState.ACCEPTED) || isPublishDone />
	<#assign isMunicipalityDecision = decisionInfo?? && decisionInfo.isPresent()/>

	<#assign createTime><@u.localDate initiative.createTime /></#assign>
    <#assign initiativePublished><@u.message "progress.public.published" /><br/>${createTime}</#assign>
    <#assign sendToMunicipality><@u.message "progress.public.sent" /><#if initiative.sentTime.present><br/><@u.localDate initiative.sentTime.value /></#if></#assign>
	<#assign municipalityDecision><@u.message "progress.public.decision"/><br/><#if initiative.getDecisionDate().isPresent()><@u.localDate initiative.getDecisionDate().value /></#if>
		<#if initiative.getDecisionModifiedDate().isPresent()><br/><@u.message "progress.public.decision.modified"/><br/><@u.localDate initiative.getDecisionModifiedDate().value /></#if></#assign>

	<#-- PUBLIC VIEW -->
	<#if public>
		<#-- SINGLE -->
		<#assign steps = [
			{
				"label": initiativePublished,
				"done": isPublishDone,
				"icon": "publish"
			},
			{
				"label": sendToMunicipality,
				"done": isSentDone,
				"icon": "sent"
			}

		]>
		<#if isMunicipalityDecision>
			<#assign steps = steps + [{
			"label": municipalityDecision,
			"done": isMunicipalityDecision,
			"icon": "decision"
			}]/>
		</#if>
	
		<#-- COLLABORATIVE -->
		<#if initiative.collaborative>
		    <#assign initiativeCollecting><@u.message "progress.public.collecting" /></#assign>
				
			<#assign steps = [
				{
					"label": initiativePublished,
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
			<#if isMunicipalityDecision>
				<#assign steps = steps + [{
				"label": municipalityDecision,
				"done": isMunicipalityDecision,
				"icon": "decision"
				}]/>
			</#if>

		</#if>
  
  	<#-- MANAGEMENT/MODERATION VIEW -->
  	<#else>
		<#assign initiativeDraftCreated><@u.message "progress.management.draft" /></#assign>
		<#assign initiativeReview><@u.message "progress.management.review" /></#assign>
		<#assign initiativeCollecting><@u.message "progress.management.collecting" /></#assign>
		
	    <#if isCollectDone>
	    	<#assign initiativePublish><@u.message "progress.management.published" /></#assign>
	    	<#assign initiativePublishSingle>${initiativePublish}</#assign>
	    <#else>
	    	<#assign initiativePublish><@u.message "progress.management.publish" /></#assign>
	    	<#assign initiativePublishSingle><@u.message "progress.management.publish.single" /></#assign>
	    </#if>
	    
		<#if initiative.sentTime.present>
	        <#assign sentTime><@u.localDate initiative.sentTime.value /></#assign>
	        <#assign sendToMunicipality><@u.message "progress.management.sent" /><br/>${sentTime}</#assign>
	        <#assign sendToMunicipalitySingle><@u.message "progress.management.sent" /><br/>${sentTime}</#assign>
	    <#else>
	    	<#assign sendToMunicipality><@u.message "progress.management.send" /></#assign>
	    	<#assign sendToMunicipalitySingle><@u.message "progress.management.send.single" /></#assign>
	   	</#if>
		    
		<#-- SINGLE -->
    	<#assign steps = [
    		{
				"label": initiativeDraftCreated,
				"done": isDraftDone,
				"icon": "draft"
			},
			{
				"label": initiativeReview,
				"done": isManagementDone,
				"icon": "mgmnt"
			},
			{
				"label": initiativePublishSingle,
				"done": isPublishDone,
				"icon": "mgmnt-publish"
			},
			{
				"label": sendToMunicipalitySingle,
				"done": isSentDone,
				"icon": "sent"
			}
		]>

		<#if omOrMunicipality>
			<#assign steps = steps + [{
			"label": municipalityDecision,
			"done": isMunicipalityDecision,
			"icon": "decision"
			}]/>
		</#if>


	<#-- COLLABORATIVE or UNDEFINED -->
		<#if initiative.collaborative || (!initiative.collaborative && !initiative.single)>				
	    <#assign steps = [
				{
					"label": initiativeDraftCreated,
					"done": isDraftDone,
					"icon": "draft"
				},
				{
					"label": initiativeReview,
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
			<#if omOrMunicipality>
				<#assign steps = steps + [{
				"label": municipalityDecision,
				"done": isMunicipalityDecision,
				"icon": "decision"
				}]/>
			</#if>

		</#if>
   	</#if>
  
  	<#assign barItems = 4 />
	<#if steps?size == 4>
        <#assign barItems = 3 />
    <#elseif steps?size == 5>
        <#assign barItems = 2 />
	<#elseif steps?size == 6>
		<#assign  barItems = 2/>
	</#if>

	<div class="initiative-progress-container">
	    <div class="initiative-progress steps-${steps?size}">
	    	<@progressBar steps initiative.fixState />
	    	<@progressSteps steps initiative.fixState />
		</div>
	</div>
</#macro>

</#escape> 