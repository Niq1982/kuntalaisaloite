<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="text" />

<@u.message "email.create.title" />


<@u.message "email.create.description" />


${urls.loginAuthor(initiative.managementHash.value)}

----

<@u.message "email.create.description.2" />


<@u.message "email.create.instruction.title" />


- <@u.message "email.create.instruction.bullet-1" />
- <@u.message "email.create.instruction.bullet-2" />
- <@u.message "email.create.instruction.bullet-3" />

----

<@b.emailFooter type />