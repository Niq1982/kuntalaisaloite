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
                    <button class="small-button"><span class="small-icon save-and-send">Liity tekijäksi</span></button>
                    <a class="push" href="#">Mitä tekijäksi liittyminen tarkoittaa?</a>
                </div>
            </#if>
            <br class="clear">

            <div class="top-margin cf">
                <div class="column col-1of2">
                    <p>Äänioikeutettuja Oulun kunnan jäseniä yhteensä<br>
                    <span class="user-count">217</span><br>
                    <a class="trigger-tooltip show-user-list-1" href="#" title="Näytä nimensä julkistaneiden lista">195 julkista nimeä</a><br>22 ei julkista nimeä</p>
                </div>
                <div class="column col-1of2 last">
                    <p>Ei äänioikeutettuja ja muita kuin Oulun kunnan jäseniä yhteensä<br>
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

<#include "public-view.ftl" />

</#escape> 