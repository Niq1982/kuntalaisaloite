<#import "/spring.ftl" as spring />
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



<#macro initiativeIframeGenerator>
    <#--<div class="input-block-content">       
        <@f.municipalitySelect path="initiative.municipality" options=municipalities required="required" cssClass="municipality-select" preSelected=municipality onlyActive=true/>
    </div>-->
    
    <div class="input-block-content no-top-margin">       
        <label for="municipality" class="input-header">
            Valitse kunta, jolle teet aloitteen <span class="icon-small required trigger-tooltip"></span>
        </label>

        <select name="municipality" id="municipality"  class="chzn-select municipality-select" data-initiative-municipality="" data-placeholder="Valitse kunta">
            <option value=""></option>
                <option value="1" selected="selected">Akaa</option>
                <option value="5">Asikkala</option>
                <option value="35">Helsinki</option>
                <option value="45">Hämeenlinna</option>
                <option value="105">Kirkkonummi</option>
                <option value="289">Tampere</option>
                <option value="298">Turku</option>
                <option value="300">Tuusula</option>
                <option value="314">Vantaa</option>
        </select>
    </div>
    
    <div class="input-block-content">
        <div class="column col-1of3">
            <label for="municipality" class="input-header">
                Aloitteiden lukumäärä
            </label>
            <input type="text" maxlength="2" class="small" value="3" name="limit" id="limit" />
        </div>
        <div class="column col-1of3">
            <label for="municipality" class="input-header">
                Leijukkeen leveys
            </label>
            <input type="text" maxlength="4" class="small" value="250" name="width" id="width" />
        </div>
        <div class="column col-1of3 last">
            <label for="municipality" class="input-header">
                Leijukkeen korkeus
            </label>
            <input type="text" maxlength="4" class="small" value="400" name="height" id="height" />
        </div>
    </div>
    
    <#--
    <div class="input-block-content">
        <label for="municipality" class="input-header">
            Aloitteiden lukumäärä
        </label>
        <input type="text" maxlength="2" class="medium" value="3" name="limit" id="limit" />
    </div>
    
    <label for="municipality" class="input-header">
        Leijukkeen leveys
    </label>
    <input type="text" maxlength="4" class="medium" value="250" name="width" id="width" />
    
    <label for="municipality" class="input-header">
        Leijukkeen korkeus
    </label>
    <input type="text" maxlength="4" class="medium" value="400" name="height" id="height" />
    -->
    
    <#--<a href="#" class="small-button js-update-iframe">Päivitä</a> <a href="#" class="small-button js-reset-iframe push">Käytä oletusta</a>-->
    <a href="#" class="js-reset-iframe">Palauta oletusarvot</a>

    <br />
    <br />

    <div id="iframe-container" style="position:relative;"></div>
    
    <br />
    
    <pre id="iframe-source"></pre>
    
    <script type="text/javascript">
        window.defaultData = {
            municipality:   "2",
            limit:          "3",
            width:          "250",
            height:         "400"
        };
    </script>

    <script id="iframe-template" type="text/x-jsrender">
        <iframe id="kuntalaisaloite-leijuke"
                frameborder="0"
                scrolling="no"
                src="${urls.baseUrl}/${locale}/iframe?municipality={{:municipality}}&amp;limit={{:limit}}&amp;orderBy=latest&amp;width={{:width}}&amp;height={{:height}}"
                width="{{:width}}"
                height="{{:height}}">
        </iframe>
    </script>
</#macro>


</#escape>