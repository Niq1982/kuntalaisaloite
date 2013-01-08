<#import "utils.ftl" as u />
<#import "forms.ftl" as f />

<#--
 *
 * TODO:
 * - Use real fields when MunicipalityInitiativeUICreateDto.java has defined them.
 *
-->

<#escape x as x?html> 

<#--
 * blockHeader
 *
 * Block header for management-view.
 * 
 * @param key is for example "initiative.basicDetails.title"
 * @param step is the number of current block
 -->
<#macro blockHeader key step=0>
    <div id="step-header-${step}" class="content-block-header edit ${(step == 1)?string('open','')}">
        <h2>${step}. <@u.message key!"" /></h2><span class="arrow hidden"> </span>
    </div>
</#macro>

<#--
 * buttons
 *
 * Save and cancel buttons used in block-edit-mode.
 * 
 * @param type is the type of the button: next, save-and-send, save
 * @param nextStep is the number of the following block
 -->
<#-- TODO: remove data-attributes if not needed -->
<#macro buttons type="" nextStep="0">
    <#if type == "next">
        <a href="#step-header-${nextStep}" id="button-next-${nextStep}" class="small-button disable-dbl-click-check ignoredirty" onClick="proceedTo(${nextStep}); return false;"><span class="small-icon next"><@u.message "action.continue" /></span></a>
        <a href="index.html" class="push"><@u.message "action.cancel" /></a>
    <#elseif type == "save-and-send">
        <button type="submit" name="save" class="small-button" ><span class="small-icon mail" data-textsend="Tallenna ja lähetä" data-textsave="Tallenna ja lähetä"><@u.message "action.saveAndSend" /></span></button>
        <#--<br/><br/>
        <a href="index.html" class=""><@u.message "action.cancel" /></a>-->
    <#elseif type == "save">
        <button type="submit" name="save" class="small-button" ><span class="small-icon save-and-send" data-textsend="Tallenna ja aloita kerääminen" data-textsave="Tallenna ja lähetä"><@u.message "action.save" /></span></button>
    </#if>
</#macro>

<#--
 * municipalityBlock
 *
 * Choose municipality
 * Prints help-texts and validation errors in this block
 *
 * @param step is the number of current block
 -->
<#macro municipalityBlock step>      

    <div id="step-${step}" class="input-block cf">
        <div class="input-block-extra">
            <div class="input-block-extra-content">
                <@f.helpText "help.municipality" />
            </div>
        </div>

        <div class="input-block-content">
            <#--<@u.systemMessage path="initiative.municipality.description" type="info" showClose=false />-->
            <div class="system-msg msg-info ">
                <#--Valitse kunta, jolle haluat tehdä aloitteen<br/>-->
                <a href="#" rel="external" class="external">Mille kunnalle minulla on oikeus tehdä aloite?</a>  
            </div>
        </div>
        
        <div class="input-block-content">       
                    
            <label class="input-header" for="municipality">
                Valitse kunta, jolle teet aloitteen <span class="icon-small required trigger-tooltip" title="Pakollinen kentt&auml;"></span>
            </label>

            <select data-placeholder="Valitse kunta" id="municipality" name="municipality" tabindex="1" class="chzn-select">
                <option value=""></option>
                <#--<noscript><option value="">Valitse</option></noscript>-->
                <#list municipalities as municipality>
                    <option value="${municipality.id}">${municipality.name}</option>
                </#list>
            </select>
        </div>
        <div class="input-block-content">
            
            <label class="input-header" for="homeMunicipality">
                    Valitse oma kotikuntasi <span class="icon-small required trigger-tooltip" title="Pakollinen kentt&auml;"></span>
                </label>

                <select data-placeholder="Valitse kunta" id="homeMunicipality" name="homeMunicipality" tabindex="2" class="chzn-select">
                    <option value=""></option>
                    <#--<noscript><option value="">Valitse</option></noscript>-->
                    <#list municipalities as municipality>
                        <option value="${municipality.id}">${municipality.name}</option>
                    </#list>
                </select>

        </div>
        <br class="clear" />
        

        
        <#-- TODO: NOJS vs. JS -->
        <div class="different-municipality js-hide hidden">
            <div class="input-block-content">
                <div class="system-msg msg-info ">
                    Kotikuntasi ei ole kunta, jota aloite koskee. Voit silti liittyä aloitteen tekijäksi, jos olet aloitteen kunnan jäsen. <a href="#" rel="external" class="external">Mitä tämä tarkoittaa?</a>    
                </div>
            </div>
            <div class="input-block-content">
                
                <#--<label>
                    <input type="checkbox" name="municipalMembership" id="municipalMembership" /><span class="label">Vakuutan, että olen vähintään sen kunnan jäsen jota aloite koskee ja ymmärrän <a href="#" rel="external" class="external">ehdot</a>.</span>
                </label>-->
                
                <label>
                    <input type="radio" name="municipalMembership" value="true" class="" /><span class="label">Olen sen kunnan jäsen, jota aloite koskee</span>
                </label>
                <label>
                    <input type="radio" name="municipalMembership" value="false" class="" /><span class="label">En ole aloitteen kunnan jäsen</span>
                </label>
            </div>
        </div>

        <noscript>
        <div class="input-block-content">
            <label>
                <input type="checkbox" name="municipalMembership" id="municipalMembership" checked="checked" disabled="disabled" /><span class="label">Jos aloitteen kunta on eri kuin kotikuntani, vakuutan olevani aloitteen kunnan jäsen. <a href="#" rel="external" class="external">Mitä tämä tarkoittaa?</a></span>
                
            </label>
        </div>
        </noscript>

        <div class="input-block-content hidden">
            <@buttons type="next" nextStep=step+1 />
        </div>
    </div>
</#macro>

<#--
 * initiativeBlock
 *
 * Add initiative title and content
 * Prints help-texts and validation errors in this block
 *
 * @param step is the number of current block
 -->
<#macro initiativeBlock step>      
    <div id="step-${step}" class="input-block cf js-hide">
        <div class="input-block-extra">
            <div class="input-block-extra-content">
                <@f.helpText "help.title" />
                <@f.helpText "help.proposal" />
            </div>
        </div>

        <div class="input-block-content">
            <#--<@u.systemMessage path="initiative.municipality.description" type="info" showClose=false />-->
            <div class="system-msg msg-info ">
                <#--Täytä aloitteen sisältö<br/>-->
                <a href="#" rel="external" class="external">Vinkkejä hyvän aloitteen kirjoittamiseen</a>  
            </div>
        </div>
        
        <div class="input-block-content">      
            <div class="input-header hidden">
                Kunta
            </div>
            <p id="selected-municipality" class="hidden"><i>Ei valittu</i></p>
            
            <@f.textField path="initiative.name" required="required" optional=true cssClass="large" maxLength=InitiativeConstants.INITIATIVE_NAME_MAX?string("#") />
        </div>

        <div class="input-block-content no-top-margin">
            <@f.textarea path="initiative.proposal" required="" optional=false cssClass="textarea-tall" />
        </div>
        
        <div class="input-block-content hidden">
            <@buttons type="next" nextStep=step+1 />
        </div>
    </div>
</#macro>


<#--
 * currentAuthorBlock
 *
 * Add author details
 *  - Name, Home municipality, suffrage
 *  - Email address, phone, street address
 *
 * Prints help-texts and validation errors in this block
 *
 * @param step is the number of current block
 -->
<#macro currentAuthorBlock step>
    <div id="step-${step}" class="input-block cf js-hide">
        <div class="input-block-extra">
            <div class="input-block-extra-content">
                <@f.helpText "help.currentAuthor.name" />
                <@f.helpText "help.currentAuthor.homeMunicipality" />
                <@f.helpText "help.currentAuthor.suffrage" />
                <@f.helpText "help.currentAuthor.contactDetails" />
            </div>
        </div>

         <div class="input-block-content">
            <div class="system-msg msg-info ">
                Anna omat tietosi. Vaikka voit piilottaa nimesi Kuntalaisaloite.fi-palvelussa, voi kunta silti julkistaa sinun ja muiden tekijöiden nimet aloitetta käsitellessään.  
            </div>
        </div>
        
        <div class="input-block-content">
            <label for="author.name" class="input-header">
                Nimi <span title="Pakollinen kenttä" class="icon-small required trigger-tooltip"></span>
            </label>
            <input type="text" maxlength="512" class="large" value="" name="author.name" id="author.name">
            
            <#--<@f.textField path="author.name" required="required" cssClass="large" maxLength="512" />-->

            <label>
                <input type="checkbox" /> Nimeni saa näkyä tämän aloitteen tekijänä Kuntalaisaloite.fi-palvelussa.
            </label>
        </div>

        <div class="input-block-content">
            <div class="system-msg msg-info ">
                Anna vähintään yksi yhteystieto. Nämä eivät tule näkyviin Kuntalaisaloite.fi-palvelussa, mutta ne näkyvät valitsemallasi kunnalle. Kunta voi ottaa sinuun yhteyttä näiden yhteystietojen kautta. Tekemäsi aloitteen linkki lähetetään antamaasi sähköpostiosoitteeseen.
            </div>
        </div>

        <div class="input-block-content">
            <div class="input-header">
                Omat yhteystiedot <span class="icon-small required trigger-tooltip"></span>
            </div>
    
            <div class="initiative-own-details-area">
                <div class="column col-1of2">
                    <label>
                        Sähköposti    <input type="text" maxlength="256" class="medium" value="" name="currentAuthor.contactInfo.email" id="currentAuthor.contactInfo.email">
                    </label>
                    
                    <label>
                        Puhelin    <input type="text" maxlength="128" class="medium" value="" name="currentAuthor.contactInfo.phone" id="currentAuthor.contactInfo.phone">
                    </label>
                </div>
                
                <div class="column col-1of2 last">
                    <label>
                        Osoite    <textarea maxlength="1000" class="address-field noresize" name="currentAuthor.contactInfo.address" id="currentAuthor.contactInfo.address"></textarea>
                    </label>
                </div>
            </div>
        </div>
        
        
        <#--<@f.currentAuthor path="initiative.currentAuthor" realPath=initiative.currentAuthor mode="full" />-->
        
        <div class="input-block-content hidden">
            <@buttons type="next" nextStep=step+1 />
        </div>
    </div>
</#macro>
      
<#--
 * saveBlock
 *
 * Initiative type and save or save and send
 *
 * Prints help-texts and validation errors in this block
 *
 * @param step is the number of current block
 -->
<#macro saveBlock step>      
    <div id="step-${step}" class="input-block cf js-hide">
        <div class="input-block-extra">
            <div class="input-block-extra-content">
                <@f.helpText "help.title" />
                <@f.helpText "help.proposal" />
            </div>
        </div>

        <div class="input-block-content">
            <#--<@u.systemMessage path="initiative.municipality.description" type="info" showClose=false />-->
                <div class="system-msg msg-info ">
                   Valitse, haluatko lähettää aloitteen keti kuntaan vai kerätä siihen ensin lisää tekijöitä Kuntalaisaloite.fi:ssä. Tekijäksi liittyminen on mahdollista heti kun aloite tallennetaan. Kun kunta aikanaan saa aloitteen, se voi halutessaan julkaista keikkien tekijöiden nimet.
                </div>
        </div>
        
        <div class="input-block-content">
        
            <div class="column-separator cf">
                <div class="column col-1of2">
                    <div class="test-box">
                        <div class="title">Lähetä suoraan kuntaan</div>
                    </div>
                    
                    <p>En halua kerätä lisää tekijöitä vaan haluan lähettää aloitteen suoraan kuntaan.</p>
                </div>
                    
                <div class="column col-1of2 last cf">
                    <div class="test-box">
                        <div class="title">Kerää tekijöitä</div>
                    </div>
                
                    <p>Haluan kerätä aloitteelle lisää tekijöitä Kuntalaisaloite.fi:ssä.</p>
    
                    <div id="franchise" class="">
                        <@f.radiobutton path="initiative.franchise" required="required" options={"false":"initiative.franchise.false", "true":"initiative.franchise.true"} attributes="" />
                    </div>
    
                    <br/>
                </div>
               
                <div class="column col-1of2">
                    <@buttons type="save-and-send" />
                </div>
                    
                <div class="column col-1of2 last cf">
                    <@buttons type="save" />
                </div>
            </div>

            <#--
            <br class="clear" /><br />
            <div class="column col-1of2">
                <div class="test-box">
                    <div class="title">Lähetä suoraan kuntaan</div>
                    
                    <div class="content">
                        <p>En halua kerätä lisää tekijöitä vaan haluan lähettää aloitteen suoraan kuntaan.</p>
                        
                        <@buttons type="save-and-send" />
                    </div>
                </div>
            </div>
                
            <div class="column col-1of2 last cf">
                <div class="test-box">
                    <div class="title">Kerää tekijöitä</div>
                    
                    <div class="content">
                        <p>Haluan kerätä aloitteelle lisää tekijöitä Kuntalaisaloite.fi:ssä.</p>
        
                        <div id="franchise" class="">
                            <@f.radiobutton path="initiative.franchise" required="required" options={"false":"initiative.franchise.false", "true":"initiative.franchise.true"} attributes="" />
                        </div>
        
                        <br/>
                        <@buttons type="save" />
                    </div>
                </div>
            </div>
            -->

             <#--            
            <div class="column col-1of2">
                <@buttons type="save-and-send" />
            </div>
                
            <div class="column col-1of2 last cf">
                <@buttons type="save" />
            </div>-->
            
            
        </div>
        
        <#--
        <div class="input-block-content" id="gather-people-container">      
            <div class="input-block-extra helpName js-hide hidden">
                <div class="input-block-extra-content">
                    <h4>Valitse kunta</h4>
                    <p>Kirjoita kansalaisaloitteen otsikko. Otsikon tulee olla selkeä ja varsinaista sisältöä kuvaava. Jos keräät kannatusilmoituksia myös paperilla tai muissa verkkopalveluissa, huomaathan, että kaikissa kannatusilmoituksissa on oltava sama otsikko.</p>
                </div>
            </div>

            <div class="input-header">
                Tekijöiden keräys aloitteelle <span class="icon-small required trigger-tooltip" title="Pakollinen kentt&auml;"></span>
            </div>

            <label>
                <input type="radio" name="gatherPeople" id="gatherPeople.false" value="FALSE" class="binder" /><span class="label">En halua kerätä aloitteelle lisää tekijöitä vaan haluan lähettää aloitteen suoraan kuntaan</span>
            </label>
            <label>
                <input type="radio" name="gatherPeople" id="gatherPeople.true" value="TRUE" class="binder" /><span class="label">Haluan kerätä aloitteelle lisää tekijöitä Kuntalaisaloite.fi:ssä</span>
            </label>
            
            <div class="same-municipality">
                <div class="input-header">
                    Äänioikeus <span class="icon-small required trigger-tooltip" title="Pakollinen kentt&auml;"></span>
                </div>

                <label>
                    <input type="radio" name="municipalCitizen" /><span class="label">Olen äänioikeutettu kunnan asukas</span>
                </label>
                <label>
                    <input type="radio" name="municipalCitizen" /><span class="label">En ole äänioikeutettu kunnan asukas</span>
                </label>
            </div>
            <div class="different-municipality js-hide hidden">
                <div class="input-header">
                    Kotikuntasi ei ole kunta, jota aloite koskee. Voit silti liittyä aloitteen tekijäksi, jos olet aloitteen kunnan jäsen <span class="icon-small required trigger-tooltip" title="Pakollinen kentt&auml;"></span>
                </div>

                <label>
                    <input type="radio" name="municipalMember" /><span class="label">Olen sen kunnan jäsen, jota aloite koskee</span>
                </label>
                <label>
                    <input type="radio" name="municipalMember" /><span class="label">En ole aloitteen kunnan jäsen</span>
                </label>
            </div>

            <div class="gather-people-details js-hide">
                <br/>
                <div class="system-msg msg-info ">
                   Sinun tulee antaa tunnistautumistiedot, jotta voit myöhemmin palata lähettämään tämän aloitteen kuntaan. Tämän aloitteen www-osoite ja tunnistautumistiedot lähetetään antamaasi sähköpostiosoitteeseen. Tunnistautumistietoja ei näytetä julkisesti Kuntalaisaloite.fi-palvelussa.
                </div>
                <br/>

                <p>Keksi aloitteelle tunnus, joka tulee olemaan käytössä vain tälle aloitteelle.</p>

                <div class="column col-1of2">

                    <label for="email" class="input-header">
                        Sähköpostiosoite
                    </label>

                    <input type="text" maxlength="128" class="medium" name="email" id="email" />
                </div>
                <div class="column col-1of2 last">
                    <label for="password" class="input-header">
                        Aloitetunnus
                    </label>

                    <input type="text" maxlength="128" class="medium" name="password" id="password" />
                </div>
            </div>

        </div>
        -->
        
        
    </div>
</#macro>

</#escape> 

