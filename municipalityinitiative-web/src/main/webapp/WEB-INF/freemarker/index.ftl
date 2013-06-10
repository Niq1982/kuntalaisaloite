<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />

<#escape x as x?html> 
<@l.main "page.frontpage">
 
<div class="image-container-new">
</div>

<#--
<div class="image-container">
    <div class="inner">
        <div class="image image-1"></div>
        <div class="image image-2"></div>
        <div class="image image-3"></div>
        <div class="image image-4"></div>
        <div class="image image-5"></div>
        <div class="image image-6"></div>
    </div>
</div>
-->
 
<div class="container">
    <div class="big-link-holder">
        <a class="big-link">Kuntalaisena voit tehdä kunnalle aloitteen <span class="arrow"></span></a>
    </div>

    <div id="content">
    
        <#-- FIXME: requestMessagesON    
        <#if requestMessagesON && requestMessages?? >
            <@u.requestMessage requestMessages />
        </#if>
        -->
    
        <div class="front-container">
        
            <div class="faux-columns cf">
                <div class="col-1">
    
                    <div class="front-block block-1">
                        <h1>Kuntalaisaloite.fi -verkkopalvelussa voit tehdä kuntalaisaloitteita sekä kannattaa ja seurata muiden tekemiä aloitteita</h1>
            
                        <p>Kuntalaki takaa kunnan asukkaille ja jäsenille oikeuden tehdä kunnalle aloite sen toimintaa koskevissa asioissa. Aloitteen tekijälle ilmoitetaan aloitteen johdosta suoritetut toimenpiteet.</p>
                        <p>Kaksi prosenttia kunnan äänioikeutetuista asukkaista voi tehdä kunnanvaltuuston toimivaltaan kuuluvassa asiassa aloitteen, jonka valtuusto käsittelee. Lisäksi viisi prosenttia kunnan äänioikeutetuista asukkaista voi tehdä kunnalle aloitteen kunnallisen kansanäänestyksen järjestämisestä.</p>
                        
                        <div class="bottom">
                            <a href="#" class="block-link">Lue lisää miten voit vaikuttaa</a>
                        </div>
                    </div>
                
                </div>
                <div class="col-2">
                
                    <div class="front-block block-2">
                        <h2>Selaa kuntalaisaloitteita</h2>
            
                        <p>Selaa kuntalaisaloitteita omalta paikkakunnaltasi</p>
                        
                        <#-- TODO: Chosen -->
                        <a href="#" class="block-link" style="margin-top:2em;">Helsinki</a>
                    </div>
                    
                    <div class="front-block block-3">
                        <h2>Viimeisimmät aloitteet</h2>
                        
                        <ul class="initiative-list no-style">
                            <li>
                                <span class="date">Helsinki <span class="push">06.06.2013</span></span>
                                <a class="name" href="https://localhost:8443/fi/aloite/3">Siljonkatu ja Vanhatullinkatun vaaralliset risteykset</a>
                            </li>
                            <li>
                                <span class="date">Helsinki <span class="push">06.06.2013</span></span>
                                <a class="name" href="https://localhost:8443/fi/aloite/3">Siljonkatu ja Vanhatullinkatun vaaralliset risteykset</a>
                            </li>
                            <li>
                                <span class="date">Helsinki <span class="push">06.06.2013</span></span>
                                <a class="name" href="https://localhost:8443/fi/aloite/3">Siljonkatu ja Vanhatullinkatun vaaralliset risteykset</a>
                            </li>
                        </ul>
                    </div>
                
                </div>
            
            </div>
        
        </div>
    
    </div>
</div>

    
</@l.main>
</#escape> 

