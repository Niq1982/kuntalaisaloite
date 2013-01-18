<#import "/spring.ftl" as spring />
<#import "components/utils.ftl" as u />
<#import "components/forms.ftl" as f />

<#escape x as x?html> 

<#assign topContribution>

    <#--
     * Initiative date and state
    -->    
    <span class="extra-info">
        <#if initiative.createTime??>Aloite luotu <@u.localDate initiative.createTime /></#if>
        <br />Aloitetta ei vielä ole lähetetty kunnalle vaan siihen kerätään ensin tekijöitä
    </span>

</#assign>

<#assign bottomContribution>

    <#-- TODO: Extra details when collecting participants. -->
    <#--
     * Show participants
    -->
    <div class="view-block public last">
        <div class="initiative-content-row last">

            <h2>Aloitteen tekijät - TODO</h2>
            <span class="user-count-total">353</span>
            
            <#-- Disable joining when modal request message is showed. -->
            <#-- TODO: Should be disabled when user has just joined to initiative. What should happen with create success-modal? -->
            <#if !requestMessageModalHTML??>
                <div class="join-as-user">
                    <a class="small-button js-participate"><span class="small-icon save-and-send">Osallistu aloitteeseen</span></a>
                    <a class="push" href="#">Mitä tekijäksi liittyminen tarkoittaa?</a>
                </div>
            </#if>
            <br class="clear">

            <div class="top-margin cf">
                <div class="column col-1of2">
                    <p>Äänioikeutettuja jäseniä yhteensä kunnassa Oulu<br />
                    <span class="user-count">217</span><br>
                    <a class="trigger-tooltip show-user-list-1" href="#" title="Näytä nimensä julkistaneiden lista">195 julkista nimeä</a><br>22 ei julkista nimeä</p>
                </div>
                <div class="column col-1of2 last">
                    <p>Ei äänioikeutettuja ja muita kuin Oulun kunnan jäseniä yhteensä<br />
                    <span class="user-count">195</span><br>
                    <a class="trigger-tooltip show-user-list-1" href="#" title="Näytä nimensä julkistaneiden lista">102 julkista nimeä</a><br>34 ei julkista nimeä</p>
                </div>
            </div>

        </div>     
    </div>
    
    <#--
     * Show management block
    -->
    <#-- TODO: This should come from bottomContribution. When different views for one person and multiple person initiatives are ready. -->
    <#if RequestParameters['mgmnt']?? && RequestParameters['mgmnt'] == "true">
        <div class="system-msg msg-summary">
            <div class="system-msg msg-info">
                <p>Voit nyt lähettää aloitteen kuntaan. <a href="#">Mitä kuntaan lähettäminen tarkoittaa?</a></p>
                <button type="submit" name="action-send" class="small-button"><span class="small-icon save-and-send"><@u.message "action.send" /></span></button>
            </div>
        </div>
    </#if>

</#assign>

<#-- TODO: NoSript -->
<#assign participateFormHTML>
<@compress single_line=true>

    <form action="${springMacroRequestContext.requestUri}" method="POST" id="form-participate" class="dirtylisten">
    
     <div class="input-block-content no-top-margin flexible">
        <@f.textField path="participant.participantName" required="required" optional=false cssClass="large" maxLength="512" />
        <@f.formCheckbox path="participant.showName" />
    </div>

    <div class="column col-1of2">
        <div class="input-block-content flexible">
            <#-- TODO: Preselect municipality -->
            <@f.formSingleSelect path="participant.homeMunicipality" options=municipalities required="required" cssClass="municipality-select" />
        </div>
    </div>
    <div class="column col-1of2 last">
        <div id="franchise" class="input-block-content flexible">
            <@f.radiobutton path="participant.franchise" required="required" options={"false":"initiative.franchise.false", "true":"initiative.franchise.true"} attributes="" />
        </div>
    </div>
    
    <div class="input-block-content flexible">
        <button type="submit" name="action-save" class="small-button" ><span class="small-icon save-and-send"><@u.message "action.save" /></span></button>
        <a href="index.html" class="push close"><@u.message "action.cancel" /></a>
    </div>
    
    </form>

</@compress>
</#assign>

<#assign modalData>

    <#-- Modal: Participate initiative -->
    <#if participateFormHTML??>    
        modalData.participateForm = function() {
            return [{
                title:      'Osallistu aloitteeseen',
                content:    '<#noescape>${participateFormHTML?replace("'","&#39;")}</#noescape>'
            }]
        };
    </#if>
    
</#assign>



<#include "public-view.ftl" />

</#escape> 