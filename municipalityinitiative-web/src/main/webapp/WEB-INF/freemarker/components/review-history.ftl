<#import "utils.ftl" as u />
<#--<#import "forms.ftl" as f />-->

<#escape x as x?html>

<#--
 * Show review history list
-->
    <#macro reviewHistories histories>

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
        </div>

    </#macro>

</#escape>