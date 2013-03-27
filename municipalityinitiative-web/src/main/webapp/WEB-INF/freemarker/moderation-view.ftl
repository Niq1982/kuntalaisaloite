<#import "/spring.ftl" as spring />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/forms.ftl" as f />
<#import "components/elements.ftl" as e />

<#escape x as x?html> 

<#assign managementURL = urls.management(initiative.id, initiative.managementHash.value) />

<#--
 * Layout parameters for HTML-title and navigation.
 * 
 * page = "page.initiative.public" or "page.initiative.unnamed"
 * pageTitle = initiative.name if exists, otherwise empty string
-->

<@l.main page="page.initiative.management" pageTitle=initiative.name!"">


    <#--
     * Show management block
    -->
    <div class="system-msg msg-summary">
        <h2>Julkaise aloite tai palauta aloite korjattavaksi</h2>
        <p>Aloite on lähetetty julkaistavaksi 21.3.2013<br />Aloite lähtee kuntaan julkaisun yhteydessä</p>

        <#-- Check is form has errors -->
        <@spring.bind "sendToMunicipality.*" />
        <#assign hasErrors = false />
        <#if spring.status.error>
            <#assign hasErrors = true />
        </#if>
        
        <#-- Participate form errors summary -->
        <#if hasErrors>
            <div class="input-block-content no-top-margin">    
                <@u.errorsSummary path="sendToMunicipality.*" prefix="sendToMunicipality."/>
            </div>
        <#else>
            <div class="js-open-block hidden">
                <#--<#assign href="#" />
                <p><@u.messageHTML key="sendToMunicipality.description" args=[href] /></p>-->
                
                <a class="small-button gray js-btn-open-block" data-open-block="js-block-container" href="#"><span class="small-icon save-and-send">Julkaise</span></a>
                <a class="small-button gray push js-btn-open-block" data-open-block="js-block-container-alt" href="#"><span class="small-icon cancel">Palauta korjattavaksi</span></a>
            </div>
        </#if>

        <div class="cf js-block-container <#if !hasErrors>js-hide</#if>">

            <noscript>
                <@f.cookieWarning managementURL />
            </noscript>

            <form action="${springMacroRequestContext.requestUri}" method="POST" id="form-send" class="sodirty">
                <@f.securityFilters />
                <input type="hidden" name="managementHash" value="${sendToMunicipality.managementHash}"/>
                
                <div class="input-block-content <#if !hasErrors>no-top-margin</#if>">
                    <@f.textarea path="sendToMunicipality.comment" required="" optional=false cssClass="collapse" />
                </div>
                
                <div class="input-block-content">
                    <#--<button type="submit" name="action-send" class="small-button"><span class="small-icon mail"><@u.message "action.send" /></span></button>-->
                    <a class="small-button gray" href="${urls.edit(initiative.id, initiative.managementHash.value)}"><span class="small-icon save-and-send">Julkaise</span></a>
                    <#if !hasErrors>
                        <a href="${springMacroRequestContext.requestUri}#participants" class="push js-btn-close-block hidden"><@u.message "action.cancel" /></a>
                    <#else>
                        <#-- In case of errors cancel-link clears data by refreshing management page. -->
                        <a href="${managementURL}" class="push hidden"><@u.message "action.cancel" /></a>
                    </#if>
                </div>
                <br/><br/>
            </form>
        </div>
        
        <div class="cf js-block-container-alt <#if !hasErrors>js-hide</#if>">

            <noscript>
                <@f.cookieWarning managementURL />
            </noscript>

            <form action="${springMacroRequestContext.requestUri}" method="POST" id="form-send" class="sodirty">
                <@f.securityFilters />
                <input type="hidden" name="managementHash" value="${sendToMunicipality.managementHash}"/>
                
                <div class="input-block-content <#if !hasErrors>no-top-margin</#if>">
                    <@f.textarea path="sendToMunicipality.comment" required="" optional=false cssClass="collapse" />
                </div>
                
                <div class="input-block-content">
                    <#--<button type="submit" name="action-send" class="small-button"><span class="small-icon mail"><@u.message "action.send" /></span></button>-->
                    <a class="small-button gray" href="${urls.view(initiative.id)}"><span class="small-icon cancel">Palauta korjattavaksi</span></a>
                    <#if !hasErrors>
                        <a href="${springMacroRequestContext.requestUri}#participants" class="push js-btn-close-block hidden"><@u.message "action.cancel" /></a>
                    <#else>
                        <#-- In case of errors cancel-link clears data by refreshing management page. -->
                        <a href="${managementURL}" class="push hidden"><@u.message "action.cancel" /></a>
                    </#if>
                </div>
                <br/><br/>
            </form>
        </div>
    </div>

    <span class="extra-info">
    <#if initiative.createTime??>
        <#assign createTime><@u.localDate initiative.createTime /></#assign>
        <@u.message key="initiative.date.create" args=[createTime] />
        <br />
        Aloite odottaa julkaisua
    </#if>
    </span>

    <h1 class="name">${initiative.name!""}</h1>
    
    <div class="municipality">${initiative.municipality.name!""}</div>

    <div class="view-block public first">
        <@e.initiativeView initiative />

        <div class="initiative-content-row last">
            <h2><@u.message "initiative.contactinfo.title" /></h2>
            <p>${initiative.authorName!""}<br />
        </div>
    </div>

    <#--
     * Public VIEW modals
     * 
     * Uses jsRender for templating.
     * Same content is generated for NOSCRIPT and for modals.
     *
     * Modals:
     *  Request message (defined in macro u.requestMessage)
     *
     * jsMessage:
     *  Warning if cookies are disabled
    -->
    <@u.modalTemplate />
    <@u.jsMessageTemplate />
    
    <script type="text/javascript">
        var modalData = {};
        
        <#-- Modal: Request messages. Check for components/utils.ftl -->
        <#if requestMessageModalHTML??>    
            modalData.requestMessage = function() {
                return [{
                    title:      '<@u.message requestMessageModalTitle+".title" />',
                    content:    '<#noescape>${requestMessageModalHTML?replace("'","&#39;")}</#noescape>'
                }]
            };
        </#if>
    
        <#-- Modal: Form modified notification. Uses dirtyforms jQuery-plugin. -->
        modalData.formModifiedNotification = function() {
            return [{
                title:      '<@u.message "form.modified.notification.title" />',
                content:    '<@u.messageHTML "form.modified.notification" />'
            }]
        };
    
        var messageData = {};

        <#-- jsMessage: Warning if cookies are not enabled -->
        messageData.warningCookiesDisabled = function() {
            return [{
                type:      'warning',
                content:    '<h3><@u.message "warning.cookieError.title" /></h3><div><@u.messageHTML key="warning.cookieError.description" args=[managementURL] /></div>'
            }]
        };
    </script>

</@l.main>

</#escape> 