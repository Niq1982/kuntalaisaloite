<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign title><@u.message "email.author.invitation.title" /></#assign>

<#escape x as x?html>

<@l.emailHtml template="author-invitation" title=title footer=false>

<p>${initiative.name}</p>
<p>${initiative.municipality.getName(locale)}</p>

<p>Aloite luotu kuntalaisaloitepalveluun ${initiative.createTime}</p>

Yksi vastuuhenkilöistä on poistanut seuraavalta henkilöltä aloitteen ylläpito-oikeudet. Henkilö ei ole enää aloitteen vastuuhenkilö eikä tekijä.

<p>Poistettu vastuuhenkilö:</p>
${contactInfo.name} ${contactInfo.phone} ${contactInfo.email} ${contactInfo.address}

</@l.emailHtml>

</#escape>