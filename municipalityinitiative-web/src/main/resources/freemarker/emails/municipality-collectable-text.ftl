<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#if (emailInfo.comment)?has_content>
    <@b.comment "text" emailInfo.comment />
</#if>

<@u.message "email.initiative" /> - ${emailInfo.municipalityName!""}

<@b.initiativeDetails "text" />

----

<@b.contactInfo "text" />

<@u.message "email.municipality.sendFrom" />:
${emailInfo.url}

<#-- TODO: Reply to not yet done
<@u.message "email.municipality.replyTo" />
-->
