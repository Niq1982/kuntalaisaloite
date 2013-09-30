<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/forms.ftl" as f />

<#escape x as x?html> 

<#--
 * municipalitySelect
 *
 * This is simplified version of the select macro that forms.ftl offers
 *
 * Show a selectbox (dropdown) input element allowing a single value to be chosen
 * from a list of options.
 * 
 * @param preSelected the predefined value for the select
-->
<#macro municipalitySelect path="" preSelected="">
<select name="${path}" id="${path}" class="chzn-select" data-placeholder="<@u.message "initiative.chooseMunicipality" />">
    <option value=""><@u.message "initiative.chooseMunicipality" /></option>
    <#list municipalities as municipality>
        <option value="${municipality.id}"<@f.checkSelected municipality.id preSelected />>${municipality.nameFi}</option>
    </#list>
</select>
</#macro>

<@l.main "Testidatan luominen">

    <style type="text/css">
        td, th { text-align:left; vertical-align:top; padding:0.3em; }
    
        .title { font-weight:bold; }
    
        .user-details, .initiative-details { margin:0.5em 0; }
        .user-details span { display:inline-block; }
        .user-col { width:140px; }
        .user-col-municipality { width:280px; }

        input[type="text"] { margin-bottom:0.2em; }
        input[type="text"].small { width:50px; }
        input[type="text"].medium { width:100px; }

        input.datepicker { width:100px; background-position:88px -57px; }

        .initiative-title { display:block; margin-left:26px; }
        .initiative-info { width:580px; }
    </style>

    <#if requestAttribute??>
        <div class="system-msg msg-success">
            <#--<#if type == 'info'><span class="icon-small arrow-right-3 floated"></span></#if>-->
            Aloitteet luotu. Linkki viimeisimmän hallintasivulle: <a href="${requestAttribute}">${requestAttribute}</a>
        </div>
    </#if>

    <h1>Testidatan luominen</h1>

    <div class="msg-block">
        <p>Valitse haluamasi testialoitteet luotavaksi.</p>
        <p>Aloitteen luoja ja kaksi ensimmäistä osallistujaa liitetään aloitteen kuntaan.<br/>Kaksi seuraavaa osallistujaa liitetään toiseen valittuun kuntaan.</p>
        <p><strong>HUOM: Tämä sivu on käytössä vain testauksen aikana eikä tule olemaan osa lopullista sovellusta!</strong></p>
    </div>

    <div class="view-block">  
        <h2>Luo valmiiksi määritellyt aloitteet automaattisesti</h2>

        <form method="POST" action="${springMacroRequestContext.requestUri}">
            <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>
            
            <div class="initiative-content-row">
            
                <h3>Valitse kunnat</h3>
                <div class="user-details cf">
                    <span class="user-col-municipality title">Aloitteen kunta</span>
                    <span class="user-col-municipality title">Osallistujan kunta</span>
                    <br />
                    <span class="user-col-municipality"><@municipalitySelect "municipalityId" "20" /></span> <#-- Municipality id 20: Akaa -->
                    <span class="user-col-municipality" ><@municipalitySelect "homeMunicipalityId" "20" /></span>
                </div>
            
                <br class="clear" />
            
                <h3>Aloitteen luoja</h3>
                <div class="user-details cf">
                    <span class="user-col title">Aloitteen luoja</span><span class="user-col title">Kunta</span>
                    <span class="user-col title">Nimi saa näkyä</span>
                    <span class="user-col title">Sähköposti</span>
                    
                    <br class="clear" />
                    
                    <#if testInitiatives??>
                        <span class="user-col">${testInitiatives[0].author.contactInfo.name!""}</span><span class="user-col" >Aloitteen kunta</span>
                        <span class="user-col" >${(true)?string("kyllä","ei")}</span>
                        <span class="user-col"><input type="email" required="required" name="authorEmail"/></span>
                    </#if>
                    
                    
                    
                </div>

                <br class="clear" />
                
                <h3>Valitse osallistujien lukumäärä</h3>
                <div class="user-details">
                    <span class="user-col title">Osallistujat</span>
                    <span class="user-col title">Nimi saa näkyä</span>
                    <span class="user-col title">Lukumäärä</span>
                    
                    <br class="clear" />
                   
                    <#if testParticipants??>
                        <#list testParticipants as participant>
                            <span class="user-col">${participant.participantName!""}</span>
                            <span class="user-col">
                                <#if participant.showName>
                                    Kyllä
                                <#else>
                                    Ei
                                </#if>
                            </span>
                            <span class="user-col"><input type="text" id="participantAmount[${participant_index}]" name="participantAmount[${participant_index}]" class="small" value="2" /></span>
                            <br class="clear" />
                        </#list>
                    </#if>
                    
                    
                </div>
            </div>
        
            <div class="initiative-content-row">
                <h3>Valitse aloite sekä luotavien aloitteiden lukumäärä</h3>
                
                <div class="initiative-details">
                    <#if testInitiatives??>
                        <table style="width:70%;">
                        <tr>
                            <th>
                                <span class="title">Nimi</span>
                            </th>
                            <th>
                                <span class="title">Kerätään osallistujia</span>
                            </th>
    
                        </tr>
                        
                        <#list testInitiatives as testInitiative>
                            <tr>
                                <td>
                                <label class="initiative">
                                    <input type="radio" ${(testInitiative.initiative.type.verifiable && !user.isVerifiedUser())?string("disabled","")} id="selections[${testInitiative_index}]" name="initiative" value="${testInitiative_index}" <#if testInitiative_index == 1>checked="checked"</#if> class="select" />
                                    <span class="initiative-title">
                                        ${testInitiative.initiative.name!""}
                                        <#if testInitiative.initiative.type.verifiable && !user.isVerifiedUser()>
                                            <br/><a href="/fi/tunnistaudu?target=%2Ffi%2Ftestdata">Kirjaudu sisään luodaksesi vetuma-aloitteen</a>
                                        </#if>
                                    </span>
                                </label>
                                </td>
                                <td>
                                    ${testInitiative.initiative.type}
                                </td>
    
                        </#list>
                        </table>
                    <#else>
                        Ei luotavia aloitteita
                    </#if>
                </div>
                
                 <div class="user-details">
                    <span class="user-col title">Lukumäärä</span>
                    <span class="user-col title">Tila</span>
                    <br />
                    <span class="user-col "><input type="text" class="small" value="1" id="amount" name="amount"></span>
                    <span class="user-col "><select class="small" name="state">

                    <option value="DRAFT">DRAFT</option>
                    <option value="REVIEW">REVIEW</option>
                    <option value="ACCEPTED">ACCEPTED</option>
                    <option selected="selected" value="PUBLISHED">PUBLISHED</option>

                    </select>
                    </span>

                </div>
            </div> 

            <div class="initiative-content-row last">                
                <button class="small-button green disable-dbl-click-check" value="true" type="submit"><span class="small-icon save-and-send">Luo käyttäjät ja aloitteet</span></button>
            </div>
        </form>        
    </div>
</@l.main>

</#escape> 