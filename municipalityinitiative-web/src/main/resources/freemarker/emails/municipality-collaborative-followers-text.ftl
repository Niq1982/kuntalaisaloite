<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="text" />

<@u.message "email.follow.sent.to.municipality" />

"${initiative.name!""}"
${initiative.municipality.getLocalizedName(switchLocale!locale)!""}
<@u.message "email.sentToMunicipality" />

<@b.separator />

<@b.attachments type/>

<@b.separator />

<@u.message key="email.participantCount.total" /> ${initiative.participantCount!"0"}

<@b.separator />

<@b.contentBlock type>
    ${urls.unsubscribe(initiativeId, removeHash)}
</@b.contentBlock>


<@b.separator />

<#-- Swedish part -->

<#global switchLocale = altLocale />
<@u.message "email.follow.sent.to.municipality" />

"${initiative.name!""}"
${initiative.municipality.getLocalizedName(switchLocale!locale)!""}
<@u.message "email.sentToMunicipality" />

<@b.separator />

<@b.attachments type/>

<@b.separator />

<@u.message key="email.participantCount.total" /> ${initiative.participantCount!"0"}

<@b.separator />

<@b.contentBlock type>
${urls.unsubscribe(initiativeId, removeHash)}
</@b.contentBlock>


<@b.separator />

<#-- Switch to default locale -->
<#global switchLocale = locale />

<@b.emailFooter type ".sentToMunicipality" />
