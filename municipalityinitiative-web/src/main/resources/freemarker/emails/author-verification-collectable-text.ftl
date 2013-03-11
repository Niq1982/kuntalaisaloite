<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#if (emailInfo.comment)?has_content>
    <@b.comment "text" emailInfo.comment />
</#if>

<@u.message "email.initiative" /> - ${emailInfo.municipality.name!""}

TODO: Vahvistusviesti

----

<@u.message "email.municipality.sendFrom" />:
${emailInfo.url}


<@u.message "email.footer" />