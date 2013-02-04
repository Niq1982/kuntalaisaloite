<#import "/spring.ftl" as spring />
<#import "components/utils.ftl" as u />
<#import "components/forms.ftl" as f />

<#escape x as x?html> 

<#assign topContribution>

    <#-- Sent to municipality -->
    <span class="extra-info">
        <#assign createTime><@u.localDate initiative.sentTime.value /></#assign>
        <@u.message key="initiative.date.sent" args=[createTime] />
    </span>

</#assign>

<#include "initiative.ftl" />

</#escape> 