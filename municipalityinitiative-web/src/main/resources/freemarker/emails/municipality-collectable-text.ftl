<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="text" />

<#if (initiative.comment)?has_content>
    <@b.comment type initiative.comment "email.commentToMunicipality" />
</#if>

<@u.message "email.initiative" />


<@b.initiativeDetails type />

----

<@b.contactInfo type />

----

<@b.participants type />

----

<@b.emailFooter type ".sentToMunicipality" />
