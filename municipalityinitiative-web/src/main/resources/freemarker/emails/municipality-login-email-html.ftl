<#import "../components/email-layout-html.ftl" as l />
<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="html" />

<#include "../includes/styles.ftl" />

<#escape x as x?html>

    <#assign title><@u.message "email.municipality.login.subject" /></#assign>

    <@l.emailHtml title=title footer=false>
        <@b.mainContentBlock title>
            <@b.initiativeDetails type />

        <p>
            <a href="${urls.municipalityLogin(municipalityDecisionHash, municipalityDecisionLoginHash)}">Tästä linkistä</a>&nbsp;pääset lisäämään vastauksen. Linkki on voimassa yhden tunnin.
        </p>

        <p>
            Jos linkki on vanhentunut, voit luoda uuden linkin&nbsp;<a href="${urls.get(locale).loginMunicipality(municipalityDecisionHash)}">tästä</a>.
        </p>

        </@b.mainContentBlock>

        <@u.spacer "15" />

        <@b.emailFooter type />

        <@u.spacer "15" />

        <#-- Swedish part -->

        <#global switchLocale = altLocale />

        <#assign title><@u.message "email.municipality.login.subject" /></#assign>

        <@b.mainContentBlock title>
            <@b.initiativeDetails type />

        <p>
            Via&nbsp;<a href="${urls.alt().municipalityLogin(municipalityDecisionHash, municipalityDecisionLoginHash)}">denna länk</a>&nbsp;kan du svara på initiativet. Länken är i kraft i en timme.
        </p>

        <p>
            Om länken har gått ut, kan du skapa en ny länk&nbsp;<a href="${urls.alt().get(locale).loginMunicipality(municipalityDecisionHash)}">här</a>.
        </p>

        </@b.mainContentBlock>

        <@u.spacer "15" />

        <@b.emailFooter type />

        <#-- Switch to default locale -->
        <#global switchLocale = locale />

    </@l.emailHtml>

</#escape>