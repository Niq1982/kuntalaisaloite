<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#if (initiative.comment)?has_content>
    <@b.comment "text" initiative.comment />
</#if>

<@u.message "email.initiative" /> - ${initiative.municipality.name!""}

TODO: Vahvistusviesti

----

<@u.message "email.municipality.sendFrom" />:
${initiative.url}


<@u.message "email.footer" />