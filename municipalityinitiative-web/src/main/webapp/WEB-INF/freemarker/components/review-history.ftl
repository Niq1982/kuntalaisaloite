<#import "utils.ftl" as u />
<#--<#import "forms.ftl" as f />-->

<#escape x as x?html>

<#--
 * Show review history list
-->
    <#macro reviewHistories histories>

    <style type="text/css">
        li.diff-prefix {
        }
        li.diff-delete {
            background-color: rgb(250, 200, 200);
        }
        li.diff-insert {
            background-color: rgb(200, 250, 200);
        }

    </style>

        <div class="msg-block">
            <h2>Tarkastushistoria</h2>

            <div style="display:inline-block">
                <form action="${springMacroRequestContext.requestUri}" method="POST" id="form-accept" class="sodirty">
                    <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>

                    <div class="input-block-content no-top-margin">
                        <textarea name="${UrlConstants.ACTION_MODERATOR_ADD_COMMENT}" maxlength="${InitiativeConstants.INITIATIVE_COMMENT_MAX}"></textarea>
                    </div>

                    <div class="input-block-content">
                        <button type="submit"  class="small-button"><span class="small-icon save-and-send"><@u.message "review.history.add.comment" /></span></button>
                    </div>
                </form>
            </div>

            <br/><br/>


            <div>
                <#list histories as row>
                    <div class="review-history-row" style="margin-bottom:10px">
                        <span class=review-history-time" style="padding-right:5px"><@u.dateTime row.created/></span>
                        <span class="review-history-details">
                            <@u.message key="review.history.type."+row.type/><br/>
                            <#if row.message.present>
                                    ${row.message.value}
                            </#if>
                            <#if row.type = "REVIEW_SENT">
                                <br><a href="${urls.moderation(initiative.id, row.id)}"><@u.message key="review.history.show.diff"/></a>
                            </#if>
                        </span>
                    </div>
                </#list>
            </div>

            <#if reviewHistoryDiff.present>
            <h2>Vertailu edelliseen</h2>
            <span style="display:inline-block">
                <span style="float:left">
                    <div>
                    <ul>
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
                </span>

                <span style="float:right">
                    <div>
                        <#if reviewHistoryDiff.value.oldText.present>
                            <ul>
                            <#list reviewHistoryDiff.value.oldText.value as oldTextLine>
                                <li>${oldTextLine}</li>
                            </#list>
                            </ul>
                        </#if>
                    </div>
                </span>
            </span>
            </#if>
        </div>

    </#macro>

</#escape>