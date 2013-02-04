<#import "/spring.ftl" as spring />
<#import "components/utils.ftl" as u />
<#import "components/forms.ftl" as f />

<#escape x as x?html> 

<#assign topContribution>

    <#-- Sent to municipality -->
    <#if initiative.createTime??>
        <span class="extra-info">
            <#assign createTime><@u.localDate initiative.createTime /></#assign>
            <@u.message key="initiative.date.sent" args=[createTime] />
        </span>
    </#if>

</#assign>

<#include "initiative.ftl" />

</#escape> 