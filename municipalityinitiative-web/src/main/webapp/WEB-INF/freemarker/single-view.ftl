<#import "/spring.ftl" as spring />
<#import "components/utils.ftl" as u />
<#import "components/forms.ftl" as f />

<#escape x as x?html> 

<#assign topContribution>

    <#-- Sent to municipality -->
    <#if initiative.createTime??>
        <span class="extra-info">
            <#assign createTime><@u.localDate initiative.createTime /></#assign>
            <#if initiative.createTime??><@u.message key="initiative.date.sent" args=[createTime] /></#if>
        </span>
    </#if>

</#assign>

<#include "public-view.ftl" />

</#escape> 