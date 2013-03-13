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
        .user-col { width:140px; }

        input[type="text"] { margin-bottom:0.2em; }
        input[type="text"].small { width:50px; }
        input[type="text"].medium { width:100px; }

        input.datepicker { width:100px; background-position:88px -57px; }

        /*.initiative { margin:0.2em 0; }
        .initiative-title { width:200px; margin-left:5px; }*/
        .initiative-title { display:block; margin-left:26px; }
        .initiative-state { width:100px; }
        .initiative-info { width:580px; }
    </style>

    <h1>Testidatan luominen</h1>

    <div class="system-msg msg-summary">
        <p>Valitse haluamasi testialoitteet luotavaksi.</p>
        <p><strong>HUOM: Tämä sivu on käytössä vain testauksen aikana eikä tule olemaan osa lopullista sovellusta!</strong></p>
    </div>

<#-- TODO: Test data generation -->
    <div class="view-block">  
        <h2>Luo valmiiksi määritellyt aloitteet automaattisesti</h2>
        <#--      
        <div class="initiative-content-row">
            <h3>Virkamieskäyttäjät</h3>
            <div class="user-details">
                <#if testUsers??>
                    <span class="user-col title">Nimi</span><span class="user-ssn title">Hetu</span><span class="user-role title">Rooli</span>

                    <#list testUsers as testUser>   
                        <br class="clear" />
                        <#assign role>
                            <#if testUser.om>OM</#if>
                            <#if testUser.vrk>VRK</#if>
                        </#assign>
                        <span class="user-col">${testUser.firstNames!""} ${testUser.lastName!""}</span><span class="user-ssn">${testUser.ssn!""}</span><span class="user-role" >${role}</span>
                        <span class="user-col">${testUser.firstNames!""} ${testUser.lastName!""}</span><span class="user-ssn">${testUser.ssn!""}</span><span class="user-role" >${role}</span>
                    </#list>
                <#else>
                    Ei käyttäjiä
                </#if>
            </div>
        </div>
        -->

    <form method="POST" action="${springMacroRequestContext.requestUri}">
        <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>
            <div class="initiative-content-row">
                <h3>Aloitteen luoja</h3>
                <div class="user-details cf">
                    <span class="user-col title">Aloitteen luoja</span><span class="user-col title">Kunta</span>
                    <span class="user-col title">Nimi saa näkyä</span>
                    
                    <br class="clear" />
                    
                    <span class="user-col">Teppo Testaaja</span><span class="user-col" >Tampere</span>
                    <span class="user-col" >kyllä</span>
                    
                </div>
                
                <br class="clear" />
                
                <h3>Valitse osallistujien lukumäärä</h3>
                <div class="user-details">
                    <span class="user-col title">Osallistujat</span><span class="user-col title">Kunta</span>
                    <span class="user-col title">Tyyppi</span><span class="user-col title">Nimi saa näkyä</span>
                    <span class="user-col title">Lukumäärä</span>
                    
                    <br class="clear" />
                    
                    <#if testParticipants??>
                        <#list testParticipants as participant>
                            <span class="user-col">${participant.participantName}</span><span class="user-col" >${participant.homeMunicipality!""}</span>
                            <span class="user-col">${(participant.franchise)?string("äänioikeutettu","jäsen")}</span><span class="user-col">${(participant.showName)?string("kyllä","ei")}</span>
                            <span class="user-col"><input type="text" id="participantAmount[${participant_index}]" name="participantAmount[${participant_index}]" class="small" value="2" /></span>
                            <br class="clear" />
                        </#list>
                    </#if>
                    
                    <#--
                    <span class="user-col">Ossi Osallistuja</span><span class="user-col" >Tampere</span>
                    <span class="user-col">äänioikeutettu</span><span class="user-col">kyllä</span>
                    <span class="user-col"><input type="text" id="participantAmount[0]" name="participantAmount[0]" class="small" value="2" /></span>
                
                    <br class="clear" />
                
                    
                    <span class="user-col">Ossi Osallistuja</span><span class="user-col" >Tampere</span>
                    <span class="user-col">äänioikeutettu</span><span class="user-col">ei</span>
                    <span class="user-col"><input type="text" id="participantAmount[0]" name="participantAmount[0]" class="small" value="2" /></span>
                    
                    <br class="clear" />
                    
                    
                    <span class="user-col">Ossi Osallistuja</span><span class="user-col" >Helsinki</span>
                    <span class="user-col">jäsen</span><span class="user-col">kyllä</span>
                    <span class="user-col"><input type="text" id="participantAmount[0]" name="participantAmount[0]" class="small" value="2" /></span>
                    
                    <br class="clear" />
                    
                                        
                    <span class="user-col">Ossi Osallistuja</span><span class="user-col" >Helsinki</span>
                    <span class="user-col">jäsen</span><span class="user-col">ei</span>
                    <span class="user-col"><input type="text" id="participantAmount[0]" name="participantAmount[0]" class="small" value="2" /></span>
                    -->
                </div>
            </div>
        
            <div class="initiative-content-row last">
                <h3>Valitse aloite, aloitteen päivämäärä sekä lukumäärä</h3>
                
                <div class="initiative-details">
                    <#if testInitiatives??>
                        <table style="width:70%;">
                        <tr>
                            <th>
                                <span class="initiative-title title">Nimi</span>
                            </th>
                            <th>
                                <span class="title">Kerätään osallistujia</span>
                            </th>
    
                        </tr>
                        
                        <#list testInitiatives as testInitiative>
                            <tr>
                                <td>
                                <label class="initiative">
                                    <#--<input type="checkbox" id="selections[${testInitiative_index}]" name="selections[${testInitiative_index}]" class="select" />-->
                                    <input type="radio" id="selections[${testInitiative_index}]" name="initiative" value="${testInitiative_index}" class="select" />  
                                    <span class="initiative-title">${testInitiative.name!""}</span>  
                                </label>
                                </td>
                                <td>
                                    ${(testInitiative.collectable)?string("kyllä","ei")}
                                </td>
    
                        </#list>
                        </table>
                    <#else>
                        Ei luotavia aloitteita
                    </#if>
                </div>
            </div> 

            <div class="user-details">
                <span class="user-col title">Päivämäärä</span><span class="user-col title">Lukumäärä</span>
                
                <br />
                
                <span class="user-col "><input type="text" class="medium datepicker" value="13.3.2013" id="date" name="date"></span>
                <span class="user-col "><input type="text" class="small" value="1" id="amount" name="amount"></span>
            </div>
            <br />
            
            <button class="small-button green" value="true" type="submit"><span class="small-icon save-and-send">Luo käyttäjät ja aloitteet</span></button>
        </form>        
    </div>


    <#-- TODO: The JavaScript below could be polished if appropriate. -->
    <#--
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
    -->

</@l.main>

</#escape> 