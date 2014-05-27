<#import "utils.ftl" as u />
<#--<#import "forms.ftl" as f />-->

<#escape x as x?html>

<#--
 * Show review history list
-->
    <#macro reviewHistories histories>
        <div class="msg-block">
            <h2><@u.message key="review.history.title"/></h2>

			<div class="toggle-container">
				<div class="js-open-block hidden">
	                <a class="small-button gray js-btn-open-block" data-open-block="js-block-container" href="#"><span class="small-icon save-and-send"><@u.message "review.history.add.comment" /></span></a>
	            </div>
				
				<div class="cf js-block-container js-hide">
		            <form action="${springMacroRequestContext.requestUri}" method="POST" id="form-accept" class="sodirty cf">
		                <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>
		
		                <div class="input-block-content no-top-margin">
		                    <textarea class="collapse" name="${UrlConstants.ACTION_MODERATOR_ADD_COMMENT}" maxlength="${InitiativeConstants.INITIATIVE_COMMENT_MAX}"></textarea>
		                </div>
		
		                <div class="input-block-content">
		                    <button type="submit"  class="small-button"><span class="small-icon save-and-send"><@u.message "review.history.add.comment" /></span></button>
		                    <a href="#" class="push js-btn-close-block hidden"><@u.message "action.cancel" /></a>
		                </div>
		            </form>
	            </div>
            </div>

            <ul class="review-history">
            <#list histories as row>                
                <li>
                	<span class="date"><@u.dateTime row.created/></span>
                	<span class="title"><@u.message key="review.history.type."+row.type/></span>
                    <div class="info">
                        <#if row.message.present>
                        ${row.message.value}
                        </#if>
                        <#if row.type = "REVIEW_SENT">
                            <a href="${urls.moderation(initiative.id, row.id)}#diff"><@u.message key="review.history.show.diff"/></a>
                        </#if>
                    </div>
                </li>
            </#list>
            </ul>
        </div>
        
		<#if reviewHistoryDiff.present>
			<div class="diff-block cf">
	            <h2 id="diff"><@u.message key="review.history.show.diff"/></h2>
                <div class="diff-col left">
                    <ul class="diff-list">
                        <#list reviewHistoryDiff.value.diff as difRow>
                            <#if difRow.modificationType.present && difRow.modificationType.value== "INSERT">
                                <li class="diff-prefix diff-insert">
                            <#elseif difRow.modificationType.present && difRow.modificationType.value == "DELETE">
                                <li class="diff-prefix diff-delete">
                            <#else>
                                <li class="diff-prefix">
                            </#if>
                            ${difRow.line}
                        </li>
                        </#list>
                    </ul>
                </div>

                <div class="diff-col right">
                    <#if reviewHistoryDiff.value.oldText.present>
                        <ul class="diff-list">
                        <#list reviewHistoryDiff.value.oldText.value as oldTextLine>
                            <li>${oldTextLine}</li>
                        </#list>
                        </ul>
                    </#if>
                </div>
			</div>
		</#if>

    </#macro>

</#escape>