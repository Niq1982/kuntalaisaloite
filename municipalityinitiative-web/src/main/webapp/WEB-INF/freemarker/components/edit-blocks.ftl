<#import "utils.ftl" as u />
<#import "forms.ftl" as f />

<#escape x as x?html> 

<#--
 * Toggle the visibility of alternative language fields.
-->
<#global altLangClass="hide" />
<#if initiative.hasTranslation(altLocale) >
    <#global altLangClass="" />
</#if>


<#--
 * blockHeader
 *
 * Block header for management-view.
 * 
 * @param key is for example "initiative.basicDetails.title"
 -->
<#macro blockHeader key step=0>
    <div id="step-header-${step}" class="content-block-header edit ${(step == 1)?string('open','')}">
        <h2><@u.message key!"" /></h2><span class="arrow hidden"> </span>
    </div>
</#macro>

<#--
 * buttons
 *
 * Save and cancel buttons used in block-edit-mode.
 * 
 * @param validateBlock enables validateForm javascript-function in defined block
 * @param editWarning assigns warning system-message for current block.
 -->
<#macro buttons type="" nextStep="0">
    <div class="input-block-content">
        <#if type == "next">
            <a href="#step-header-${nextStep}" class="small-button disable-dbl-click-check ignoredirty" onClick="proceedTo(${nextStep});"><span class="small-icon next">Jatka</span></a>
        <#elseif type == "save">
            <button type="submit" name="save" class="small-button bind" ><span class="small-icon save-and-send" data-textsend="Tallenna ja lähetä aloite" data-textsave="Tallenna aloite">Tallenna aloite</span></button>
        </#if>
        <a href="index.html" class="push">Peruuta</a>
    </div>
</#macro>

<#--
 * municipalityBlock
 *
 * Choose municipality
 *
 * Prints help-texts and validation errors in this block
 -->
<#macro municipalityBlock step>      

    <div id="step-${step}" class="input-block cf">
        <div class="input-block-extra">
            <div class="input-block-extra-content">
                <@f.helpText "help.municipality" />
            </div>
        </div>

        <div class="input-block-content">
            <@u.systemMessage path="initiative.municipality.description" type="info" showClose=false />
            <#--<div class="system-msg msg-info ">
                Valitse kunta, jolle haluat aloitteen tehdä<br/>
                <a href="#">Mille kunnalle minulla on oikeus tehdä aloite?</a>  
            </div>-->
        </div>
        
        <div class="input-block-content">       
                    
            <label class="input-header" for="municipality.fi">
                Valitse kunta, jolle haluat tehdä aloitteen <span class="icon-small required trigger-tooltip" title="Pakollinen kentt&auml;"></span>
            </label>

            <#-- TODO: Get municipalities from backend -->
            <select data-placeholder="Valitse kunta" id="municipality" name="municipality" tabindex="1" class="chzn-select">
                <option value=""></option> 
                <option value="TODO">TODO</option>
                <option value="TODO">TODO</option>
                <option value="TODO">TODO</option>
                <option value="TODO">TODO</option>
            </select>
            
                <#list municipalities as municipality>
        ${municipality.name}<br/>
    </#list>
        </div>

        <@buttons type="next" nextStep=step+1 />
    </div>
</#macro>

<#--
 * initiativeBlock
 *
 * Add initiative title and content
 *
 * Prints help-texts and validation errors in this block
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
            <@u.systemMessage path="initiative.municipality.description" type="info" showClose=false />
            <#--<div class="system-msg msg-info ">
                Täytä aloitteen sisältö<br/>
                <a href="#">Vinkkejä hyvän aloitteen kirjoittamiseen</a>  
            </div>-->
        </div>
        
        <div class="input-block-content">      
            <div class="input-header hidden">
                Kunta
            </div>
            <p id="selected-municipality" class="hidden">Ei valittu</p>
            
            <@f.textField path="initiative.name" required="required" cssClass="large" maxLength=InitiativeConstants.INITIATIVE_NAME_MAX?string("#") />
        </div>

        <div class="input-block-content no-top-margin">
            <@f.textarea path="initiative.proposal" required="required" optional=true cssClass="textarea-tall" />
        </div>
        
        <@buttons type="next" nextStep=step+1 />
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
    
        <#-- Block-edit errors -->
        <#if managementSettings.editMode != EditMode.FULL>
        <div class="input-block-content">
            <@u.errorsSummary path="initiative.*" prefix="initiative."/>
        </div>
        </#if>
    
         <div class="input-block-content">
            <div class="system-msg msg-info ">
                Anna omat tietosi. Vaikka voit piilottaa nimesi Kuntalaisaloite.fi-palvelussa, voi kunta silti julkistaa sinun ja muiden tekijöiden nimet aloitetta käsitellessään.  
            </div>
        </div>
        
        <div class="input-block-content">
            <p>TODO: nimi</p>
            <#--<@f.textField path="currentAuthor.name" required="required" cssClass="large" maxLength="512" />-->

            <label>
                <input type="checkbox" /> Nimeni saa näkyä tämän aloitteen tekijänä Kuntalaisaloite.fi-palvelussa.
            </label>
        </div>

        <div class="input-block-content">

            <div class="column col-1of2">
                <label class="input-header" for="homeMunicipality.fi">
                    Kotikunta <span class="icon-small required trigger-tooltip" title="Pakollinen kentt&auml;"></span>
                </label>

                <#-- TODO: Get municipalities from backend -->
                <select data-placeholder="Valitse kunta" id="homeMunicipality" name="homeMunicipality" tabindex="2" class="chzn-select">
                    <option value=""></option> 
                    <option value="Akaa">Akaa</option>
                </select>
            </div>
                
            <div class="column col-1of2 last">
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
            </div>
            
        </div>

        <div class="input-block-content">
            <div class="system-msg msg-info ">
                Anna vähintään yksi yhteystieto. Nämä eivät tule näkyviin Kuntalaisaloite.fi-palvelussa, mutta ne näkyvät valitsemallasi kunnalle. Kunta voi ottaa sinuun yhteyttä näiden yhteystietojen kautta. Tekemäsi aloitteen linkki lähetetään antamaasi sähköpostiosoitteeseen.
            </div>
        </div>

        <@f.currentAuthor path="initiative.currentAuthor" realPath=initiative.currentAuthor mode="full" />
        
        <@buttons type="next" nextStep=step+1 />
    </div>
</#macro>
      
<#--
 * saveBlock
 *
 * Initiative type and save or save and send
 *
 * Prints help-texts and validation errors in this block
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
            <@u.systemMessage path="initiative.municipality.description" type="info" showClose=false />
            <#--<div class="input-block-content">
            <div class="system-msg msg-info ">
               Valitse, haluatko lähettää aloitteen keti kuntaan vai kerätä siihen ensin lisää tekijöitä Kuntalaisaloite.fi:ssä. Tekijäksi liittyminen on mahdollista heti kun aloite tallennetaan. Kun kunta aikanaan saa aloitteen, se voi halutessaan julkaista keikkien tekijöiden nimet.
            </div>
        </div>-->
        </div>
        
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
        
        <@buttons type="save" />
    </div>
</#macro>

</#escape> 

