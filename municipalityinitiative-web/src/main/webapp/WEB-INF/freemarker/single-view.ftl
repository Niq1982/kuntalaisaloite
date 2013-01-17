<#import "/spring.ftl" as spring />
<#import "components/utils.ftl" as u />
<#import "components/forms.ftl" as f />

<#escape x as x?html> 

<#assign topContribution>

    <#-- Sent to municipality -->
    <#if initiative.createTime??>
        <span class="extra-info">Aloite lähetetty kuntaan <@u.localDate initiative.createTime /></span>
    </#if>

</#assign>

<#include "public-view.ftl" />

</#escape> 