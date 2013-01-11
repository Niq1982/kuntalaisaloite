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
            <#assign href="#" />
            <@u.systemMessage path="initiative.municipality.description" type="info" showClose=false args=[href] />
        </div>
        
        <div class="input-block-content">       
            <@f.formSingleSelect path="initiative.municipality" options=municipalities required="required" cssClass="municipality-select" />
        </div>
        <div class="input-block-content">
            <@f.formSingleSelect path="initiative.homeMunicipality" options=municipalities required="required" cssClass="municipality-select" />
        </div>
        <br class="clear" />
        
        <div class="municipalitys-differs js-hide hidden">
            <div class="input-block-content">
                <#assign href="#" />
                <@u.systemMessage path="initiative.municipality.municipalitysDiffers" type="info" showClose=false args=[href] />
            </div>
            <div class="input-block-content">
                <@f.radiobutton path="initiative.municipalMembership" required="" options={"true":"initiative.municipalMembership.true", "false":"initiative.municipalMembership.false"} attributes="" header=false />
            </div>
        </div>

        <#-- Different treat for NOSCRIPT-users. Dummy checkbox for better UX. -->
        <noscript>
        <div class="input-block-content">
            <label>
                <#assign href="#" />
                <input type="checkbox" name="municipalMembership" id="municipalMembership" checked="checked" disabled="disabled" /><span class="label"><@u.messageHTML key="initiative.checkMembership" args=[href] /></span>
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
            <#assign href="#" />
            <@u.systemMessage path="initiative.proposal.description" type="info" showClose=false args=[href] />  
        </div>
        
        <div class="input-block-content">      
            <div class="input-header hidden">
                <@u.message "selectedMunicipality.title" />
            </div>
            <p id="selected-municipality" class="hidden"><i><@u.message "selectedMunicipality.notSelected" /></i></p>
            
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
            <@u.systemMessage path="initiative.ownDetails.description" type="info" showClose=false />  
        </div>
        
        <div class="input-block-content">
            <#--<label for="author.name" class="input-header">
                Nimi <span title="Pakollinen kenttä" class="icon-small required trigger-tooltip"></span>
            </label>-->
            <@f.textField path="initiative.contactName" required="required" optional=true cssClass="large" maxLength="512" />
            
            <@f.formCheckbox path="initiative.showName" />

            <label>
                <input type="checkbox" id="initiative.showName" name="initiative.showName"><span class="label"><@u.message "initiative.allowPublicName" /></a></span>
            </label>
            
        </div>

        <div class="input-block-content">
            <@u.systemMessage path="initiative.contactInfo.description" type="info" showClose=false />
        </div>

        <#--
        <div class="input-block-content">
            <div class="input-header">
                Omat yhteystiedot <span class="icon-small required trigger-tooltip"></span>
            </div>
    
            <div class="initiative-own-details-area">
                <div class="column col-1of2">
                    <label>
                        Sähköposti    <input type="text" maxlength="256" class="medium" value="" name="initiative.contactEmail" id="currentAuthor.contactEmail" />
                    </label>
                    
                    <label>
                        Puhelin    <input type="text" maxlength="128" class="medium" value="" name="initiative.contactPhone" id="initiative.contactPhone" />
                    </label>
                </div>
                
                <div class="column col-1of2 last">
                    <label>
                        Osoite    <textarea maxlength="1000" class="address-field noresize" name="initiative.contactAddress" id="initiative.contactAddress"></textarea>
                    </label>
                </div>
            </div>
        </div>
        -->
        
        <#-- TODO: Add proper path and bindings -->
        <@f.contactInfo path="initiative.contactInfo" realPath=initiative.contactInfo mode="full" />
        
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
            <@u.systemMessage path="initiative.save.description" type="info" showClose=false />
        </div>
        
        <#-- TODO: Finalize the test layout.-->
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

