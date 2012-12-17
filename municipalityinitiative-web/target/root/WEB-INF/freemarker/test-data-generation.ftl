<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />

<#escape x as x?html> 

<@l.main "Testidatan luominen">

    <#-- 
        Styles are here so that we would not mixup test-page and site's general styles.
        This page is removed afterall.
        
        TODO: Remove this page BEFORE going live.     
     -->
    <style type="text/css">
        td, th { text-align:left; vertical-align:top; padding:0.3em; }
    
        .title { font-weight:bold; }
    
        .user-details, .initiative-details { margin:0.5em 0; }
        .user-details span { display:inline-block; }
        .user-name { width:130px; }
        .user-ssn { width:130px; }
        .user-role { width:200px; }

        /*.initiative { margin:0.2em 0; }
        .initiative-title { width:200px; margin-left:5px; }*/
        .initiative-title { display:block; margin-left:26px; }
        .initiative-state { width:100px; }
        .initiative-info { width:580px; }
    </style>

    <h1>Testidatan luominen</h1>

    <div class="system-msg msg-summary">Valitse haluamasi testialoitteet luotavaksi. Tarvittavat testikäyttäjät luodaan aina tarvittaessa.<br/>
    Muista syöttää toimivat sähköpostiosoitteet aloitteiden vastuuhenkilöille (voi olla sama molemmille).<br/>
    Jos yhtään aloitetta ei valita, luodaan vain virkamieskäyttäjät. (Virkamieskäyttäjät luodaan vain, jos niitä ei ole ennestään.)<br/>
    Aloitteen tilan tarkemman kuvauksen saa näkyviin viemällä hiiren tilakoodin päälle.<br/>
    <b>HUOM: Tämä sivu on käytössä vain testauksen aikana eikä tule olemaan osa lopullista sovellusta!</b><br/>
    </div>
    
    <div class="content-block-header view">
        <h2>Luo valmiiksi määritellyt aloitteet automaattisesti</h2>
    </div>
    
    <div class="view-block">        
        <div class="initiative-content-row">
            <h3>Virkamieskäyttäjät</h3>
            <div class="user-details">
                <#if testUsers??>
                    <span class="user-name title">Nimi</span><span class="user-ssn title">Hetu</span><span class="user-role title">Rooli</span>

                    <#list testUsers as testUser>   
                        <br class="clear" />
                        <#assign role>
                            <#if testUser.om>OM</#if>
                            <#if testUser.vrk>VRK</#if>
                        </#assign>
                        <span class="user-name">${testUser.firstNames!""} ${testUser.lastName!""}</span><span class="user-ssn">${testUser.ssn!""}</span><span class="user-role" >${role}</span>
                    </#list>
                <#else>
                    Ei käyttäjiä
                </#if>
            </div>
        </div>

        <form method="POST" action="${springMacroRequestContext.requestUri}">
            <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>
        <div class="initiative-content-row">
            <h3>Vastuuhenkilöt</h3>
            <div class="user-details">
                    <span class="user-name title">Nimi</span><span class="user-ssn title">Hetu</span><span class="user-role title">Rooli</span><span class="author-email title">Sähköposti</span>

                <#if currentUser??>
                        <br class="clear" />
                        <span class="user-name">${currentUser.firstNames!""} ${currentUser.lastName!""}</span><span class="user-ssn">${currentUser.ssn!""}</span><span class="user-role" >Vireillepanija, Edustaja</span><span><input name="emails[0]" type="text"></span><span style="margin-left:1em;">(sisäänkirjautuneena)</span>
                </#if>
                <#if testReserveAuthorUser??>
                        <br class="clear" />
                        <span class="user-name">${testReserveAuthorUser.firstNames!""} ${testReserveAuthorUser.lastName!""}</span><span class="user-ssn">${testReserveAuthorUser.ssn!""}</span><span class="user-role" >Varaedustaja</span><span><input name="emails[1]" type="text"></span>
                </#if>
            </div>
        </div>
        
        <div class="initiative-content-row last">
            <h3>Aloitteet</h3>
            
            <div class="initiative-details">
                <#if testInitiatives??>
                    <table>
                    <tr>
                        <th>
                            <label><input type="checkbox" id="select-all" title="Valitse kaikki / Poista valinnat" class="trigger-tooltip" onClick="selectAll();" /></label>
                            <span class="initiative-title title">Nimi</span>
                        </th>
                        <th>
                            <span class="title">Tila</span>
                        </th>
                        <#--
                        <th> 
                            <span class="title">Tiedot</span>
                        </th>
                        -->
                    </tr>
                    
                    <#list testInitiatives as testInitiative>
                        <tr>
                            <td>
                            <label class="initiative">
                                <input type="checkbox" id="selections[${testInitiative_index}]" name="selections[${testInitiative_index}]" class="select" />  
                                <span class="initiative-title">${testInitiative.name.fi!""}</span>  
                            </label>
                            </td>
                            <td>
                                <span class="trigger-tooltip" title="<@u.enumDescription testInitiative.state />" >${testInitiative.state!""}</span>
                            </td>
                        <#--
                            <td>
                                <span class="">Jotain muuta infoa tästä testitapauksesta...</span>
                            </td>
                        </tr>
                        -->
                    </#list>
                    </table>
                <#else>
                    Ei luotavia aloitteita
                </#if>
            </div>
        
        </div> 
        <br />

            <#--<input type="submit" value="Luo testidata"/>-->
            <button class="small-button green" value="true" type="submit"><span class="small-icon save-and-send">Luo käyttäjät ja aloitteet</span></button>
        </form>        
    </div>

    <#if resultInfo??>
        <div id="result" class="content-block-header">
            <h2>Luodut aloitteet</h2>
        </div>
        
        <div class="view-block">        
            <div class="initiative-content-row last">
                ${resultInfo}
            </div>
        </div>
    </#if>

    <#-- TODO: The JavaScript below could be polished if appropriate. -->
    <script type="text/javascript">
        var $select = null;
        
        var selectAll = function() {
            $select = $("input.select");

            var selectAll = $("#select-all");
            
            if (selectAll.is(':checked')) {

                $select.each( function(){
                    var thisSelect = $(this);
                    
                    thisSelect
                    .data('checked',thisSelect.attr('checked') ? 'checked' : '')
                    .attr('checked','checked');
                });
                
            } else {
                $select.each( function(){
                    var thisSelect = $(this);

                    if ( thisSelect.data('checked') === 'checked'){
                      thisSelect.attr('checked',thisSelect.data('checked'));
                    } else {
                        thisSelect.removeAttr('checked');
                    }
                    
                });
            }   
       };
       
       if ($select != null){
           $select.change(function(){
                var thisSelect = $(this);
                if ( !$("#select-all").is(':checked') && !thisSelect.is(':checked') ) {
                    thisSelect.removeAttr('checked');
                    thisSelect.removeAttr('data-checked');
                }
           });
       }
    </script>

</@l.main>

</#escape> 