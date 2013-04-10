<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

 <@u.message "email.author.sentToMunicipality.title" /> ${initiative.municipality.getLocalizedName(locale)!""}

<@b.initiativeDetails "text" />

----

<@b.contactInfo "text" />

----

<@b.emailFooter "text" />