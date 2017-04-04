<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="text" />

<@b.contentBlock type>
    <@b.initiativeDetails type />

Tästä linkistä pääset lisäämään vastauksen. Linkki on voimassa yhden tunnin.
${urls.municipalityLogin(municipalityDecisionHash, municipalityDecisionLoginHash)}

Jos linkki on vanhentunut, voit luoda uuden linkin tästä: ${urls.get(locale).loginMunicipality(municipalityDecisionHash)}

</@b.contentBlock>


<#-- Swedish part -->

<#global switchLocale = altLocale />

<@b.contentBlock type>
    <@b.initiativeDetails type />
</@b.contentBlock>

<#-- Switch to default locale -->
<#global switchLocale = locale />
