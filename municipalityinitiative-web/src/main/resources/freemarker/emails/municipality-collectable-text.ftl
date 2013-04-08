<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#if (initiative.comment)?has_content>
    <@b.comment "text" initiative.comment "email.commentToMunicipality" />
</#if>

<@u.message "email.initiative" /> - ${initiative.municipality.name!""}

<@b.initiativeDetails "text" />

----

<@b.contactInfo "text" />

----

<@b.participants "text" />

----

<@u.message "email.municipality.sendFrom" />:
${initiative.url}


<@u.message "email.footer" />