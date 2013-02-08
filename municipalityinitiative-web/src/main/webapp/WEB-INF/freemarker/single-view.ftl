<#import "/spring.ftl" as spring />
<#import "components/utils.ftl" as u />
<#import "components/forms.ftl" as f />
<#import "components/some.ftl" as some />

<#escape x as x?html> 

<#--
 * Elements for the top section of the initiative's management-view page
-->
<#assign topContribution>

</#assign>

<#--
 * Elements for the bottom section of the initiative's management-view page
-->
<#assign bottomContribution>
    <#--
     * Social media buttons
    -->
    <@some.some pageTitle=currentPage />
</#assign>

<#include "initiative.ftl" />

</#escape> 