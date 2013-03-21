<#import "/spring.ftl" as spring />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/elements.ftl" as e />
<#import "components/forms.ftl" as f />
<#import "components/some.ftl" as some />

<#escape x as x?html> 

<#--
 * Layout parameters for HTML-title and navigation.
 * 
 * page = "page.initiative.public" or "page.initiative.unnamed"
 * pageTitle = initiative.name if exists, otherwise empty string
-->
<@l.main "page.initiative.public" initiative.name!"">

    <h1 class="name">${initiative.name!""}</h1>
    
    <div class="extra-info">${initiative.municipality.name!""}</div>
    
    <@e.stateDates initiative />

    <#-- VIEW BLOCKS -->
    <div class="view-block public">
        
        <#if initiative.proposal??>
            <div class="initiative-content-row <#if !initiative.showName>last</#if>">
                <h2><@u.message "initiative.content.title" /></h2>
                
                <@u.text initiative.proposal!"" />
            </div>
        </#if>
        
         <#if initiative.showName>
             <div class="initiative-content-row last">
                <h2><@u.message "initiative.author.title" /></h2>
                <p>${initiative.authorName!""}</p>
            </div>
        </#if>
    </div>


    <#--
     * Social media buttons
    -->
    <@some.some pageTitle=initiative.name!"" />

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
    <#-- TODO: Check what is needed. 'Cause there is nothing user could do here. -->
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
    </script>

</@l.main>

</#escape> 