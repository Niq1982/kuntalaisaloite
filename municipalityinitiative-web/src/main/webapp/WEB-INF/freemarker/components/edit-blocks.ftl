<#import "/spring.ftl" as spring />
<#import "utils.ftl" as u />
<#import "forms.ftl" as f />

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
<#macro buttons type="" nextStep="0">
    <#if type == "next">
        <a href="#step-header-${nextStep}" id="button-next-${nextStep}" class="small-button disable-dbl-click-check ignoredirty js-proceed-to-next" data-next-step="${nextStep}"><span class="small-icon next"><@u.message "action.continue" /></span></a>
    <#elseif type == "save-and-send">
        <button type="submit" name="action-save-and-send" class="small-button"><span class="small-icon mail"><@u.message "action.saveAndSend" /></span></button>
    <#elseif type == "save">
        <button type="submit" id="action-send-confirm" name="action-send-confirm" class="small-button" value="true" ><span class="small-icon save-and-send"><@u.message "action.prepare.send" /></span></button>
        <#--<button type="submit" id="action-save" name="action-save" class="small-button" value="true" ><span class="small-icon save-and-send"><@u.message "action.saveAndCollect" /></span></button>-->
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
<#macro municipalityBlock municipality="">      
    
        <div class="input-block-extra">
            <div class="input-block-extra-content">
                <@f.helpText "help.municipality" />
                <@f.helpText "help.homeMunicipality" />
            </div>
        </div>

        <div class="input-block-content">
            <#assign href="#" />
            <@u.systemMessage path="initiative.municipality.description" type="info" showClose=false args=[href] />
        </div>
        
        <div class="input-block-content">       
            <@f.municipalitySelect path="initiative.municipality" options=municipalities required="required" cssClass="municipality-select" preSelected=municipality />
        </div>
        <div class="input-block-content">
            <@f.municipalitySelect path="initiative.homeMunicipality" options=municipalities required="required" cssClass="municipality-select" preSelected=municipality />
        </div>
        <br class="clear" />
        
        <noscript>
            <div class="input-block-content">
                <div class="system-msg msg-info">
                    <#assign href= "#" />
                    <@u.messageHTML key="initiative.municipality.different" args=[href] />
                </div>
            </div>
        </noscript>
        
        <div id="municipalMembership" class="municipality-not-equal js-hide">
            <div class="input-block-content hidden">
                <#assign href="#" />
                <@u.systemMessage path="initiative.municipality.notEqual" type="info" showClose=false args=[href] />
            </div>
            <div class="input-block-content">
                <@f.radiobutton path="initiative.municipalMembership" required="required" options={
                    "community":"initiative.municipalMembership.community",
                    "company":"initiative.municipalMembership.company",
                    "property":"initiative.municipalMembership.property",
                    "none":"initiative.municipalMembership.none"
                } attributes="" />
            </div>
            
            <div class="input-block-content is-not-member no-top-margin js-hide hidden">
                <@u.systemMessage path="warning.initiative.notMember" type="warning" showClose=false />
            </div>
        </div>
</#macro>

<#--
 * chooseInitiativeType
 *
 * Choose the type of the initiative
 * - Normal
 * - 2% and 5% will be enabled later
 *
 * Prints help-texts and validation errors in this block
 -->
<#macro chooseInitiativeType>
    <div class="input-block-extra">
        <div class="input-block-extra-content">
            <@f.helpText "help.initiativeType" />
        </div>
    </div>
        
    <div class="input-block-content">
        <div class="input-header">
            <@u.message "initiative.type" /> <span class="icon-small required trigger-tooltip"></span>
        </div>
        
        <div class="initiative-types cf">
            <#-- TODO: Create a macro when other options are selectable -->
            <@spring.bind "initiative.initiativeType" /> 
            <@f.showError />
            <label class="initiative-type">
                <span class="type">
                    <input type="radio" id="initiativeType" name="${spring.status.expression}" value=""
                        <#if spring.stringStatusValue == "normal">checked="checked"</#if>
                    <@spring.closeTag/> <@u.message "initiative.type.normal" />
                </span>
                <span class="description"><@u.message "initiative.type.normal.description" /></span>
            </label>
            
            <label class="initiative-type disabled trigger-tooltip" title="<@u.message "initiative.type.disabled" />">
                <span class="type"><input type="radio" id="initiativeType[1]" name="initiativeType" value="COLLABORATIVE_COUNCIL" disabled="disabled" /> <@u.message "initiative.type.two-percent" /></span>
                <span class="description"><@u.message "initiative.type.two-percent.description" /><br/><br/></span>
            </label>
            
            <label class="initiative-type disabled trigger-tooltip" title="<@u.message "initiative.type.disabled" />">
                <span class="type"><input type="radio" id="initiativeType[2]" name="initiativeType" value="COLLABORATIVE_CITIZEN" disabled="disabled" /> <@u.message "initiative.type.five-percent" /></span>
                <span class="description"><@u.message "initiative.type.five-percent.description" /><br/><br/></span>
            </label>
        </div>

    </div>

</#macro>


<#--
 * authorEmailBlock
 *
 * Add confirmation email for author
 * Prints help-texts and validation errors in this block
 -->
<#macro authorEmailBlock>
    <div class="input-block-extra">
        <div class="input-block-extra-content">
            <@f.helpText "help.participantEmail" />
        </div>
    </div>
    
    <div class="input-block-content">
        <@u.systemMessage path="initiative.participantEmail.description" type="info" showClose=false />
    </div>

    <div class="input-block-content">
        <@f.textField path="initiative.participantEmail" required="required" optional=false cssClass="large" maxLength=InitiativeConstants.CONTACT_EMAIL_MAX />
    </div>
</#macro>

<#--
 * initiativeBlock
 *
 * Add/edit initiative title, content and extra-info
 * Prints help-texts and validation errors in this block
 *
 * @param locked locks some field from editing
 -->
<#macro initiativeBlock>      
    <div class="input-block cf">
        <div class="input-block-extra">
            <div class="input-block-extra-content">
                <@f.helpText "help.name" />
                <@f.helpText "help.proposal" />
                <@f.helpText "help.extraInfo" />
            </div>
        </div>

        <div class="input-block-content">
            <#assign href="#" />
            <@u.systemMessage path="initiative.proposal.description" type="info" showClose=false args=[href] />
        </div>
        
        <div class="input-block-content">
            <@f.textField path="initiative.name" required="required" optional=true cssClass="large" maxLength=InitiativeConstants.INITIATIVE_NAME_MAX />
        </div>
        
        <div class="input-block-content no-top-margin">
            <@f.textarea path="initiative.proposal" required="required" optional=false cssClass="textarea-tall" />
        </div>
        
        <div class="input-block-content">
            <@f.textarea path="initiative.extraInfo" required="" optional=true cssClass="textarea" />
        </div>
    </div>
</#macro>

<#--
 * updateInitiativeBlock
 *
 * Title and content are NOT editable
 * Update initiative extra-info details
 *
 * Prints help-texts and validation errors in this block
 *
 * @param locked locks some field from editing
 -->
<#macro updateInitiativeBlock path>
    <div class="input-block cf">
        <div class="input-block-extra">
            <div class="input-block-extra-content">
                <@f.helpText "help.extraInfo" />
            </div>
        </div>

        <div class="input-block-content">
            <@f.textarea path=path+".extraInfo" required="" optional=true cssClass="textarea" key="initiative.extraInfo" />
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
<#macro currentAuthorBlock path>
    <div class="input-block cf">
        <div class="input-block-extra">
            <div class="input-block-extra-content">
                <@f.helpText "help.contactInfo.name" />
                <@f.helpText "help.contactInfo.contactDetails" />
            </div>
        </div>

        <div class="input-block-content">
            <@u.systemMessage path="contactInfo.ownDetails.description" type="info" showClose=false />  
        </div>
        
        <div class="input-block-content">
            <div class="column col-2of3">
                <@f.textField path=path+".contactInfo.name" required="required" optional=false cssClass="medium" maxLength=InitiativeConstants.CONTACT_NAME_MAX key="contactInfo.name" />
                
            </div>
            <div class="column col-1of3 last">
                <div class="input-header"><@u.message "contactInfo.homeMunicipality" /></div>
                
                <div class="input-placeholder">${author.municipality.getName(locale)}</div>
            </div>
            <br class="clear" />
            <@f.formCheckbox path=path+".contactInfo.showName" checked=true key="contactInfo.showName" />
        </div>

        <div class="input-block-content">
            <@u.systemMessage path="contactInfo.description" type="info" showClose=false />
        </div>

        <div class="input-block-content">
            <@f.contactInfo path=path+".contactInfo" mode="full" />
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
<#macro saveBlock>      
    <div class="input-block cf">
        <div class="input-block-extra">
            <div class="input-block-extra-content">
                <@f.helpText "help.send" />
                <@f.helpText "help.collect" />
            </div>
        </div>

        <div class="input-block-content">
            <@u.systemMessage path="initiative.save.description" type="info" showClose=false />
        </div>
        
        <#-- TODO: Testing the layout. Finalize the test layout.-->
        <div class="input-block-content">
        
            <div class="column-separator cf">
                <div class="column col-1of2">
                    <div class="highlight-box">
                        <div class="title"><@u.message "initiative.send.title" /></div>
                    </div>
                    
                    <p><@u.message "initiative.send.description" /></p>
                </div>
                    
                <div class="column col-1of2 last cf">
                    <div class="highlight-box">
                        <div class="title"><@u.message "initiative.collect.title" /></div>
                    </div>
                
                    <p><@u.message "initiative.collect.description" /></p>
    
                    <div id="franchise" class="">
                        <@f.radiobutton path="initiative.franchise" required="required" options={"true":"initiative.franchise.true", "false":"initiative.franchise.false"} attributes="" />
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
            
        </div>
        
    </div>
</#macro>

</#escape> 

