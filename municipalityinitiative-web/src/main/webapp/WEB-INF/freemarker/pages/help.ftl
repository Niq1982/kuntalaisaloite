<#import "../components/layout.ftl" as l />
<#import "../components/utils.ftl" as u />

<#if omUser>
    <#import "../components/wysiwyg-editor.ftl" as editor />
    <#global editorStyles><@editor.styles /></#global>
</#if>

<#escape x as x?html>

<#if content??>
    <#assign pageTitle>${content.subject}</#assign>
<#else>
    <#assign pageTitle>jee</#assign>
</#if>

<#--
 * Navigation for subpages (public view)
 *
 * @param map is the hashMap for navigation items
 * @param titleKey is for navigation block title
 * @param cssClass is used for box-style links
-->
<#macro navigation map titleKey="" cssClass="">
    <#if cssClass == "box">
        <#list map as link>
            <a href="${urls.help(link.uri)}" class="${(link.uri == helpPage)?string("active","")} ${cssClass} ${link_has_next?string("","last")}">
                <#-- NOTE: we could also use urls (fi/sv) to determine the class -->
                <#if link_index == 0>
                    <#assign iconClass="author" />
                <#elseif link_index == 1>
                    <#assign iconClass="participants" />
                <#else>
                    <#assign iconClass="info" />
                </#if>
            
                <span class="help-nav-icon icon-${iconClass}">${link.subject}</span>
            </a>
        </#list>
    <#else>
        <#if titleKey?has_content><h3 class="navi-title"><@u.message titleKey /></h3></#if>
        <ul class="navi block-style">
            <#list map as link>
                <li><a href="${urls.help(link.uri)}" <#if link.uri == helpPage>class="active"</#if>>${link.subject}</a></li>
            </#list>
        </ul>
    </#if>
</#macro>

<#--
 * Layout parameters for HTML-title
 *
 * @param page is for example "page.help.general.title"
 * @param pageTitle used in HTML title.
-->
<@l.main "page.help" pageTitle!"">

    <div class="columns cf">

        <div class="column col-1of4 extra-margin navigation">
            <@navigation categoryLinksMap['MAIN'] "" "box" />
            <@navigation categoryLinksMap['KUNTALAISALOITE_FI'] "help.service.title" />
            <@navigation categoryLinksMap['KUNTALAISALOITE'] "help.general.title" />
        </div>

        <#if omUser>
            <div class="editor-buttons bootstrap-icons hidden">
                <a href="${urls.helpEdit(helpPage)}" class="btn" href="#"><@u.message "editor.switchToEdit" />&nbsp;&nbsp;<i class="icon-chevron-right"></i></a>
            </div>
        </#if>

        <div class="column col-3of4 last">
            <#if content??>
                <h1>${content.subject!""}</h1>
                <#noescape>${content.content!""}</#noescape>
            <#else>
                <h1>Kuntalaisaloite lyhyesti</h1>
                
                <div class="info-graph">
                  <div class="block cf">
                        <div class="header top-pad">
                        
                              <span class="order">1</span>
                              <h3>Valitaan<br/>aloitetyyppi</h3>
                        
                        </div>
                        <div class="content top-pad cf">
                        
                              <p>Aloitteen tekeminen alkaa valitsemalla yksi kolmesta aloitetyypistä. Riippuen aloitteen tekijästä, on mahdollista valita tavallinen aloite tai kannatukseen tähtäävä aloite.</p>
                              
                              <div class="span span-1">
                                    <span class="icon member"></span>
                                    
                                    <h4>Kunnan jäsen</h4>
                                    <p class="small"><a href="#">Kuka luetaan kunnan jäseneksi?</a></p>
                              </div>
                              
                              <div class="span span-2">
                                    <span class="icon right-to-vote"></span>
                                    
                                    <h4>Äänioikeutettu kuntalainen</h4>
                                    <p class="small"><a href="#">Olenko äänioikeutettu kuntalainen?</a></p>
                              </div>
                              
                              <br class="clear" />
                              <div class="">nuolipalkki</div>
                              
                              <div class="span span-1">
                                    <span class="type">
                                    	<span class="icon normal"></span>
                                    </span>
                                    
                                    <h4>Tavallinen</h4>
                                    <p>kuntalaisaloite</p>
                              </div>
                              <div class="span span-1">
                                    <span class="type">
                                    	<span class="icon two-percent"></span>
                                    </span>
                                    
                                    <h4>2% kannatus</h4>
                                    <p>valtuustokäsittelyyn tähtäävä aloite</p>
                              </div>
                              <div class="span span-1">
                                    <span class="type">
                                    	<span class="icon five-percent"></span>
                                    </span>
                                    
                                    <h4>5% kannatus</h4>
                                    <p>aloite kunnallisesta kansanäänestyksestä</p>
                              </div>
                        
                              <p class="small"><a href="#">Mistä tiedän mikä aloitemuoto pitäisi valita ja mikä on niiden ero?</a></p>
                              
                              <br class="clear" />
                              <div class="">nuolipalkki</div>
                        </div>
                  </div>
                  
                  <div class="block cf">
                        <div class="header top-pad">
                        
                              <span class="order">2</span>
                              <h3>Aloitteeseen<br/>kerätään<br/>osallistujia</h3>
                        
                        </div>
                        <div class="content top-pad cf">
                        
                              <div class="span span-1">
                                    <span class="icon no-collect"></span>
                                    
                                    <p>Tavallisen aloitteen voi lähettää suoraan kuntaan, jos ei halua kerätä siihen muita osallistujia.</p>
                              </div>
                              <div class="span span-2">
                                    <span class="icon collect"></span>
                                    
                                    <p>Aloitteeseen kerätään allekirjoituksia, eli osallistujia. Kun aloitteen tekijöiden mielestä osallistujia on tarpeeksi, he lähettävät aloitteen kuntaan.</p>
                                    <p class="small"><a href="#">Mitä aloitteen osallistujien kerääminen on käytännössä?</a></p>
                              </div>
                        
                              <br class="clear" />
                              <div class="">nuolipalkki</div>
                        
                        </div>
                  </div>
                  
                  <div class="block cf">
                        <div class="header top-pad">
                        
                              <span class="order">3</span>
                              <h3>Aloite<br/>lähetetään<br/>kuntaan</h3>
                              
                        </div>
                        <div class="content top-pad cf">
                        
                              <span class="icon municipality"></span>
                              
                              <p>Kunta käsittelee saapuneet aloitteet omien käytäntöjensä mukaan.</p>
                              <p class="small"><a href="#">Lue miten kotikuntasi käsittelee aloitteita.</a></p>
                        
                        </div>
                  </div>
                  
                  <div class="block cf">
                        <div class="header top-pad">
                        
                              <span class="order">4</span>
                              <h3>Aloitteesta<br/>tehdään<br/>päätös</h3>
                        
                        </div>
                        <div class="content top-pad cf">
                        
                              <p>Kunta tekee aloitteesta päätöksen, joka lähetetään tiedoksi aloitteen vastuuhenkilöille.</p>
                        
                        </div>
                  </div>
            
            </div>
                
            </#if>
        </div>
    </div>

</@l.main>
</#escape>

