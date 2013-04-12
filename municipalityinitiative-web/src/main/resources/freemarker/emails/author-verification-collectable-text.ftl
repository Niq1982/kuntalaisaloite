<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="text" />

<#if (initiative.comment)?has_content>
    <@b.comment type initiative.comment />
</#if>

<@u.message "email.initiative" /> - ${initiative.municipality.getLocalizedName(locale)!""}

TODO: Vahvistusviesti

----

<@b.emailFooter type />