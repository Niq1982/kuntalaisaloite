<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="text" />

<@u.message "email.initiative" /> - ${initiative.municipality.getLocalizedName(locale)!""}

<#if (initiative.sentComment)?has_content>
    <@b.comment type initiative.sentComment "email.sentComment" />
    ----
    
</#if>

<@b.initiativeDetails type />

----

<@b.contactInfo type />

----

<@b.publicViewLink type />

----

<@b.emailFooter type />
