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
            <#list histories as row>
                <div class="review-history-row" style="margin-bottom:10px">
                    <div class=review-history-time" style="float:left; padding-right:5px"><@u.dateTime row.created/></div>
                    <div class="review-history-details">
                        <@u.message key="review.history.type."+row.type/><br/>
                        <#if row.message.present>
                                ${row.message.value}
                        </#if>
                        <#if row.type = "REVIEW_SENT">
                            <br><a href="${urls.moderation(initiative.id, row.id)}"><@u.message key="review.history.show.diff"/></a>
                        </#if>
                    </div>
                </div>
            </#list>

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