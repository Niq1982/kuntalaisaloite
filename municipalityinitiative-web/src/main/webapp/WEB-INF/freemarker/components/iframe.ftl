<#import "/spring.ftl" as spring />
<#import "utils.ftl" as u />
<#import "forms.ftl" as f />

<#escape x as x?html> 

<#--
 * initiativeIframe (Leijuke)
 *
 * Generates iframe source code for listing initiatives in defined municipality.
 *
 * @param embed desides whether HTML is escaped for embed code or not espaced for pre-code.
 * @param id unique id for iframe
 * @param width sets iframe width in pixels. Use plain number.
 * @param height sets iframe height in pixels. Use plain number.
 * @param municipality sets municipality for the iframe
 * @param limit sets the limit for showed initiatives
-->
<#macro initiativeIframe id="kuntalaisaloite-leijuke" embed=true width="250" height="400" municipality="" limit="3"><@compress single_line=true>
    <#assign iframeHTML><iframe id="${id}"
            frameborder="0"
            scrolling="no"
            src="${urls.baseUrl}/${locale}/iframe?municipality=${municipality}&amp;limit=${limit}&amp;orderBy=latest&amp;width=${width}&amp;height=${height}"
            width="${width}"
            height="${height}">
    </iframe></#assign>
        
    <#if embed>
        <#noescape>${iframeHTML}</#noescape>
    <#else>
        <pre>${iframeHTML}</pre>
    </#if>
</@compress></#macro>


<#--
 * initiativeIframeGenerator (Leijuke-generaattori)
 *
 * Interactive functionality for generating iFrames on the fly.
 *
 * @param options list of municipalities
-->
<#macro initiativeIframeGenerator options>
    <div class="hidden">
        <div class="input-block-content no-top-margin">       
            
            <label for="municipality" class="input-header">
                Kunta
            </label>
            <select name="municipality" id="municipality" class="chzn-select" data-placeholder="<@u.message "initiative.chooseMunicipality" />" data-allow-single-deselect="allow">
                <option value=""><@u.message "initiative.chooseMunicipality" /></option>
                <#list options as option>
                    <#if option.active><option value="${option.id}">${option.getName(locale)}</option></#if>
                </#list>
            </select>
        </div>
        
        <#assign digit = "\\d+" />
        
        <div class="input-block-content">
            <div class="column col-1of3">
                <label for="municipality" class="input-header">
                    Aloitteiden lukumäärä
                </label>
                <input type="text" maxlength="2" class="small" value="3" name="limit" id="limit" pattern="${digit}" />
            </div>
            <div class="column col-1of3">
                <label for="municipality" class="input-header">
                    Leijukkeen leveys
                </label>
                <input type="text" maxlength="4" class="small" value="250" name="width" id="width" pattern="${digit}"  />
            </div>
            <div class="column col-1of3 last">
                <label for="municipality" class="input-header">
                    Leijukkeen korkeus
                </label>
                <input type="text" maxlength="4" class="small" value="400" name="height" id="height" pattern="${digit}"  />
            </div>
        </div>
        <br class="clear" />
        
        <#--<a href="#" class="small-button js-update-iframe">Päivitä</a> <a href="#" class="small-button js-reset-iframe push">Käytä oletusta</a>-->
        <p><a href="#" class="js-reset-iframe">Palauta oletusarvot</a></p>
    
        <div id="iframe-container"></div>
        
        <script type="text/javascript">
            window.hasIFrame = true;
            window.defaultData = {
                municipality:   "",
                limit:          "3",
                width:          "250",
                height:         "400"
            };
            
            function iFrameLoaded(id, loaderId){
                document.getElementById(id).style.display="block";
                document.getElementById(loaderId).style.display="none";
            }
        </script>
    
        <script id="iframe-template" type="text/x-jsrender">
            <h3>Leijuke</h3>
            
            <div id="iframe-placeholder" style="width:{{:width}}px; height:{{:height}}px;"><span class="loader" /></div>
            <iframe id="kuntalaisaloite-leijuke"
                    frameborder="0"
                    scrolling="no"
                    src="${urls.baseUrl}/${locale}/iframe?municipality={{:municipality}}&amp;limit={{:limit}}&amp;orderBy=latest&amp;width={{:width}}&amp;height={{:height}}"
                    width="{{:width}}"
                    height="{{:height}}" onload="iFrameLoaded('kuntalaisaloite-leijuke', 'iframe-placeholder')">
            </iframe>
            
            <#assign iFrameSrc>
            <@compress single_line=true>
                <iframe id="kuntalaisaloite-leijuke"
                    frameborder="0"
                    scrolling="no"
                    src="${urls.baseUrl}/${locale}/iframe?municipality={{:municipality}}&amp;limit={{:limit}}&amp;orderBy=latest&amp;width={{:width}}&amp;height={{:height}}"
                    width="{{:width}}"
                    height="{{:height}}">
                </iframe>
            </@compress>
            </#assign>
            
            <h3>Leijukkeen lähdekoodi</h3>
            
            <pre id="iframe-source">${iFrameSrc}</pre>
        </script>
    </div>
    
    <noscript>
        <h3>Leijukegeneraattori vaatii JavaScriptin sallimista selaimessa</h3>
        
        <p>Voit kuitenkin muokata alla olevaa esimerkki-leijuketta tarpeisiisi sopivaksi. Kopioi alla oleva upotuskoodi ja muuta siitä tarvitsemasi parametrit.</p>
        
        <@i.initiativeIframe id="kuntalaisaloite-leijuke" embed=true width="250" height="400" municipality="1" limit="3" />
        
        <br/><br/>
        <p>Alla on esimerkki-leijukkeen upotuskoodi. Kopioi alla oleva koodi kokonaisuudessaan leikepöydällesi ja muuta parametreja tarpeesi mukaan. Liitä sen jälkeen koodi sivustollesi.</p>
        
        <@i.initiativeIframe id="kuntalaisaloite-leijuke" embed=false width="250" height="400" municipality="1" limit="3" />
    </noscript>
</#macro>


</#escape>