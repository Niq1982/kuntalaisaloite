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
 * @param defaults is an array for default values [municipalityId, limit, width, height]
 * @param bounds is an array for min and max values [min limit, max limit, min width, max width, min height, max height]
-->
<#macro initiativeIframeGenerator options defaults bounds>
    <h2><@u.message "iframeGenerator.settings.title" /></h2>
    <div class="hidden">
        <div class="column col-1of3">
            <label for="municipality" class="input-header">
                <@u.message "iframeGenerator.municipality" />
            </label>
            <select name="municipality" id="municipality" class="chzn-select" data-placeholder="<@u.message "initiative.chooseMunicipality" />" data-allow-single-deselect="allow">
                <option value=""><@u.message "initiative.chooseMunicipality" /></option>
                <#list options as option>
                    <#if option.active><option value="${option.id}">${option.getName(locale)}</option></#if>
                </#list>
            </select>
        </div>
        
        <#assign digit = "\\d+" />
        
        <div class="column col-1of10 push-width">
            <label for="municipality" class="input-header">
                <@u.message "iframeGenerator.width" />
            </label>
            <input type="text" maxlength="4" class="small" value="250" name="width" id="width" pattern="${digit}" />
        </div>
        <div class="column col-1of10">
            <label for="municipality" class="input-header">
                <@u.message "iframeGenerator.height" />
            </label>
            <input type="text" maxlength="4" class="small" value="400" name="height" id="height" pattern="${digit}"  />
        </div>
        <div class="column col-1of10 push-amount">
            <label for="municipality" class="input-header">
                <@u.message "iframeGenerator.initiativeCount" />
            </label>
            <input type="text" maxlength="2" class="small" value="3" name="limit" id="limit" pattern="${digit}" />
        </div>
        <div class="column col-1of5">
            <div class="input-header">
                <@u.message "iframeGenerator.lang" />
            </div>
            
            <div class="input-placeholder">
                <label class="inline">
                    <input type="radio" name="language" value="${locale}" checked="checked" class="no-float" /><@u.message "iframeGenerator.lang.locale" />
                </label>
                <label class="inline push">
                    <input type="radio" name="language" value="${altLocale}" class="no-float" /><@u.message "iframeGenerator.lang.altLocale" />
                </label>
            </div>
        </div>
            
        <br class="clear" />
        
        <p><a href="#" class="js-reset-iframe"><@u.message "iframeGenerator.reset" /></a></p>
    
        <div id="iframe-container"></div>
        
        <script type="text/javascript">
            window.hasIFrame = true;
            window.defaultData = {
                municipality:   "${defaults[0]}",
                lang:           "${defaults[1]}",
                limit:          "${defaults[2]}",
                width:          "${defaults[3]}",
                height:         "${defaults[4]}"
            };
            
            window.bounds = { 
                min : {
                    limit:  ${bounds[0]},
                    width:  ${bounds[2]},
                    height: ${bounds[4]}
                },
                max : {
                    limit:  ${bounds[1]},
                    width:  ${bounds[3]},
                    height: ${bounds[5]}
                }
            };
            
            function iFrameLoaded(id, loaderId){
                document.getElementById(id).style.display="block";
                document.getElementById(loaderId).style.display="none";
            }
        </script>
    
        <script id="iframe-template" type="text/x-jsrender">
            <h2><@u.message "iframeGenerator.preview.title" /></h2>
            
            <div id="iframe-placeholder" style="width:{{:width}}px; height:{{:height}}px;"><span class="loader" /></div>
            <iframe id="kuntalaisaloite-leijuke"
                    frameborder="0"
                    scrolling="no"
                    src="${urls.iframe()}?municipality={{:municipality}}&amp;limit={{:limit}}&amp;orderBy=latest&amp;width={{:width}}&amp;height={{:height}}"
                    width="{{:width}}"
                    height="{{:height}}" onload="iFrameLoaded('kuntalaisaloite-leijuke', 'iframe-placeholder')">
            </iframe>
            
            <#assign iFrameSrc>
            <@compress single_line=true>
                <iframe id="kuntalaisaloite-leijuke"
                    frameborder="0"
                    scrolling="no"
                    src="${urls.iframe()}?municipality={{:municipality}}&amp;limit={{:limit}}&amp;orderBy=latest&amp;width={{:width}}&amp;height={{:height}}"
                    width="{{:width}}"
                    height="{{:height}}">
                </iframe>
            </@compress>
            </#assign>
            
            <h2><@u.message "iframeGenerator.source.title" /></h2>
            
            <pre id="iframe-source">${iFrameSrc}</pre>
        </script>
    </div>
    
    <noscript>
        <h3><@u.message "iframeGenerator.nojs.title" /></h3>
        
        <p><@u.message "iframeGenerator.nojs.description" /></p>
        
        <@i.initiativeIframe id="kuntalaisaloite-leijuke" embed=true width="250" height="400" municipality="1" limit="3" />
        
        <br/><br/>
        <h2><@u.message "iframeGenerator.source.title" /></h2>
        <p><@u.message "iframeGenerator.nojs.source" /></p>
        
        <@i.initiativeIframe id="kuntalaisaloite-leijuke" embed=false width="250" height="400" municipality="1" limit="3" />
    </noscript>
</#macro>


</#escape>