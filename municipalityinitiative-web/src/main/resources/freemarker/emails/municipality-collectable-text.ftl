<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="text" />

<#if (initiative.sentComment)?has_content>
    <@b.comment type initiative.sentComment "email.sentComment" />
</#if>
<#--
<@u.message "email.initiative" /> - ${initiative.municipality.getLocalizedName(locale)!""}

<@b.initiativeDetails type />

----

<@b.contactInfo type />

----

<@b.participants type />

----

<@b.emailFooter type />
-->