<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="text" />

<@u.message "email.managementhash.renewed.title" />


<@b.initiativeDetails type=type showDate=true />

<@u.message "email.managementhash.renewed.description" />


<@b.separator />
<@b.adminViewLink type />

<@b.separator />

<@b.emailFooter type />

<#-- Swedish part -->
<#global switchLocale = altLocale />

<@b.separator />

<@u.message "email.managementhash.renewed.title" />


<@b.initiativeDetails type=type showDate=true />

<@u.message "email.managementhash.renewed.description" />


<@b.separator />
<@b.adminViewLink type />

<@b.separator />

<@b.emailFooter type />

<#global switchLocale = locale />