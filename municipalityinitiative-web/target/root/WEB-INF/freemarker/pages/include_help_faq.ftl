<#import "../components/utils.ftl" as u />

<#assign faqItems=32 />
<#list 1..faqItems as faqItem>
    
    <#-- Show topic for question set. Defined in messages.properties -->
    <#assign showTopic=false />
    
    <#if faqItem == 1 || faqItem == 4 || faqItem == 9
      || faqItem == 13 || faqItem == 19 || faqItem == 21
      || faqItem == 23 || faqItem == 27 || faqItem == 31>
      
        <#assign showTopic=true />
        
    </#if>
                    
    <@u.faqItem item=faqItem topic=showTopic?string(faqItem,0) />
    
</#list> 
