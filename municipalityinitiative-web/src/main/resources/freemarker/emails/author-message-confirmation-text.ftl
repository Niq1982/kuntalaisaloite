<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="text" />
<#assign keyPrefix="email.author.message.confirmation" />



<@u.message keyPrefix+".title" />


<@b.initiativeDetails type />

<@u.message keyPrefix+".description" />

<@u.message keyPrefix+".description.2" />


<@u.message keyPrefix+".sender" />

${authorMessage.contactName}
${authorMessage.contactEmail}

<@u.message keyPrefix+".message" />

${authorMessage.message}

${urls.confirmAuthorMessage(authorMessage.confirmationCode)}

<@b.separator />

<@b.emailFooter type />

<#-- Switch language -->
<#global switchLocale = altLocale />

<@b.separator />

<@u.message keyPrefix+".title" />


<@b.initiativeDetails type />

<@u.message keyPrefix+".description" />

<@u.message keyPrefix+".description.2" />


<@u.message keyPrefix+".sender" />

${authorMessage.contactName}
${authorMessage.contactEmail}

<@u.message keyPrefix+".message" />

${authorMessage.message}

${urls.confirmAuthorMessage(authorMessage.confirmationCode)}

<@b.separator />

<@b.emailFooter type />

<#global switchLocale = locale />
