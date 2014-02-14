<#import "../components/utils.ftl" as u />

<#escape x as x?html>

<h1><@u.message "infoGraph.title" /></h1>
                
<div class="info-graph">
 	<div class="block cf">
        <@header "infoGraph.choose.title" 1 />
        
        <div class="content top-pad cf">
			<p><@u.message "infoGraph.choose.description" /></p>
              
			<div class="c-group c-group-1 cf">
	        	<div class="span span-1">
					<span class="icon member"></span>
	            	<h4><@u.message "infoGraph.choose.member" /></h4>
		            <p class="small"><a href="#"><@u.message "infoGraph.choose.member.linkLabel" /></a></p>
				</div>
			      
				<div class="span span-2">
	          		<span class="icon right-to-vote"></span>
			            
		            <h4><@u.message "infoGraph.choose.rightToVote" /></h4>
		            <p class="small"><a href="#"><@u.message "infoGraph.choose.rightToVote.linkLabel" /></a></p>
				</div>
			</div>
              
			<div class="lines line-group-1">
              	<div class="line line-20 left green">
              		<@arrow "right" />
              	</div>
              	
              	<div class="line line-70 right black">
              		<@arrow "left" />
              		<@arrow "arrow-1-2-2" />
              		<@arrow "arrow-1-2-3" />
              	</div>
			</div>
              
			<div class="c-group c-group-2 cf">
				<div class="span span-1">
					<span class="type">
						<span class="icon icon-type normal"></span>
					</span>
                        
					<h4><@u.message "infoGraph.choose.type.COLLABORATIVE.title" /></h4>
					<p><@u.message "infoGraph.choose.type.COLLABORATIVE" /></p>
				</div>
                <div class="span span-1">
            		<span class="type">
                    	<span class="icon icon-type two-percent"></span>
                    </span>
                    
                    <h4><@u.message "infoGraph.choose.type.COLLABORATIVE_COUNCIL.title" /></h4>
					<p><@u.message "infoGraph.choose.type.COLLABORATIVE_COUNCIL" /></p>
              	</div>
              	<div class="span span-1">
                	<span class="type">
                		<span class="icon icon-type five-percent"></span>
                    </span>
                        
                    <h4><@u.message "infoGraph.choose.type.COLLABORATIVE_CITIZEN.title" /></h4>
					<p><@u.message "infoGraph.choose.type.COLLABORATIVE_CITIZEN" /></p>
              	</div>
            
              	<p class="small"><a href="#"><@u.message "infoGraph.choose.type.linkLabel" /></a></p>
			</div>
              
    	</div>
	</div>
  	
	<div class="block cf">
		<@header "infoGraph.collect.title" 2 />
		
        <div class="content top-pad cf">
        	<div class="lines line-group-2">
              	<div class="line line-60 left green">
              		<@arrow "arrow-2-1-1" />
              		<@arrow "right" />
              	</div>
              	
              	<div class="line line-55 right pullup black">
              		<@arrow "middle" />
              	</div>
      		</div>
        
    		<div class="c-group c-group-3 cf">
              	<div class="span span-1">
                    <span class="icon no-collect"></span>
                    <p><@u.message "infoGraph.noCollect.description" /></p>
              	</div>
              	<div class="span span-2">
                    <span class="icon collect"></span>
                    <p><@u.message "infoGraph.collect.description" /></p>
                    <p class="small"><a href="#"><@u.message "infoGraph.collect.linkLabel" /></a></p>
          		</div>
    		</div>
        </div>
	</div>
  
	<div class="block cf">
		<@header "infoGraph.send.title" 3 />
		
        <div class="content top-pad cf">
        	<div class="lines line-group-3">
          		<div class="line line-50 left green">
          			<@arrow "right" />
              	</div>
              	
              	<div class="line line-50 right black">
              		<@arrow "left" />
              	</div>
          	</div>
          	
        	<div class="c-group c-group-4 cf">
				<span class="icon municipality"></span>
                  
				<p><@u.message "infoGraph.send.description" /></p>
				<p class="small"><a href="#"><@u.message "infoGraph.send.linkLabel" /></a></p>
			</div>
    	</div>
	</div>
  
	<div class="block cf">
    	<@header "infoGraph.decision.title" 4 />
    	
        <div class="content top-pad cf">
        	<div class="lines line-group-4">
              	<div class="line line-50 left green">
              		<@arrow "right" />
              	</div>
              	
              	<div class="line line-50 right black">
              		<@arrow "left" />
              	</div>
      		</div>
        
        	<div class="c-group c-group-5">
        		<p><@u.message "infoGraph.decision.description" /></p>
            </div>
        </div>
	</div>

</div>

<#-- 
 * header
 * 
 * Header block for the infograph
 *
 * @param titleKey for localization
 * @param index for the order number
-->
<#macro header titleKey index>
	<div class="header top-pad">
		<span class="order">${index}</span>
    	<h3><@u.message titleKey /></h3>
    </div>
</#macro>

<#-- 
 * arrow
 * 
 * HTML markup for the arrow and css triangle
 *
 * @param cssClass for styling

-->
<#macro arrow cssClass>
	<div class="arrow ${cssClass}">
		<div class="arrow-down"></div>
	</div>
</#macro>


</#escape>

