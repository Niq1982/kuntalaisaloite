<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="text" />



<@u.message "email.author.message.confirmation.title" />

<@b.initiativeDetails type />

<@u.message "email.author.message.confirmation.description" />
<@u.message "email.author.message.confirmation.description.2" />

<@u.message "email.author.message.to.authors.sender" />
${authorMessage.contactName}
${authorMessage.contactEmail}

<@u.message "email.author.message.to.authors.message" />
${authorMessage.message}

${urls.confirmAuthorMessage(authorMessage.confirmationCode)}

<@b.separator />

<@b.emailFooter type />


<@b.separator />

<@u.message "email.author.message.confirmation.title" />

<@b.initiativeDetails type />

<@u.message "email.author.message.confirmation.description" />
<@u.message "email.author.message.confirmation.description.2" />

<@u.message "email.author.message.to.authors.sender" />
${authorMessage.contactName}
${authorMessage.contactEmail}

<@u.message "email.author.message.to.authors.message" />
${authorMessage.message}

${urls.confirmAuthorMessage(authorMessage.confirmationCode)}

<@b.separator />


<@b.separator />

<@b.emailFooter type />

<#global switchLocale = locale />
