<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="text" />

<#if (initiative.sentComment)?has_content>
    <@b.comment type initiative.sentComment "email.sentComment" />
</#if>

<@u.message "email.initiative" />


<@b.initiativeDetails type />

<#if (initiative.extraInfo)?has_content>
    <@u.message "email.extraInfo" />
    
    ${initiative.extraInfo}
</#if>

----

<@b.contactInfo type />

----

<@b.participants type />

----

<@b.emailFooter type ".sentToMunicipality" />
