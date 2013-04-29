<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="text" />

<@u.message "email.initiative" /> - ${initiative.municipality.getLocalizedName(locale)!""}

<@u.message "email.created.description" />

----

<@b.initiativeDetails type />

----
<@b.contactInfo type />
----

<@b.adminViewLink type />

----

<@b.emailFooter type />