<#import "/spring.ftl" as spring />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />

<#escape x as x?html>

<#--
 * Layout parameters for HTML-title and navigation.
 *
 * @param page is "page.authenticate"
 * @param pageTitle can be assigned as custom HTML title
-->
<#assign page="page.authenticate" />

<@l.main page>

    <div class="msg-block cf">
        <h1><@u.message "authenticate.title" /></h1>

        <p>Voit tunnistautua ja ylläpitää niitä luomiasia aloitteita, jotka tähtäävät 2 tai 5 prosenttiin kunnan äänioikeutetuista asukkaista.</p>
        <p>Tavallisia Kuntalaisaloitteita voi ylläpitää ainoastaan sähköpostiin lähetetyllä ylläpitolinkillä.</p>
            
        <p><a href="${vetumaLoginToOwnInitiatives}" class="small-button"><span class="small-icon next"><@u.message "common.continueToAuthenticate" /></span></a></p>
    </div>

</@l.main>
</#escape>