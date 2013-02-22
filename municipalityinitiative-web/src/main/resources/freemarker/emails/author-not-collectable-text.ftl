<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

 <@u.message "email.author.sentToMunicipality.title" /> ${emailInfo.municipality.name!""}

<@b.initiativeDetails "text" />

----

<@b.contactInfo "text" />

<@u.message "email.author.linkToManagement" />
${emailInfo.url}