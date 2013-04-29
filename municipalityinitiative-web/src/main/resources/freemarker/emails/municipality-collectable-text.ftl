<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="text" />

<#if (initiative.extraInfo)?has_content>
    <@b.comment type initiative.extraInfo "email.commentToMunicipality" />
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