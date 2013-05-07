<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="html" />

<#escape x as x?html>

    <#assign title><@u.message "email.author.invitation.accepted.subject" /></#assign>
    <@l.emailHtml template="author-invitation" title=title footer=false>

  <@u.spacer "15" />

  <@b.contentBlock type>

      <#assign url = urls.loginAuthor(initiative.id, managementHash) />
      <p style="${pBothMargins!""} ${smallFont!""}"><@u.link url url /></p>
  </@b.contentBlock>


</@l.emailHtml>

</#escape>