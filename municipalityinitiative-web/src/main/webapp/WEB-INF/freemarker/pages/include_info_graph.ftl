<#import "../components/utils.ftl" as u />

<#escape x as x?html>

<h1><@u.message "infoGraph.title" /></h1>

<div class="info-graph">
 	<div class="block cf">
    <@header "infoGraph.choose.title" 1 />

    <div class="content top-pad cf">
			 <div class="c-group c-group-1 cf">
        <div class="span span-1">
          <h4><@u.message "infoGraph.choose.type.COLLABORATIVE.title" /></h4>
        </div>
        <div class="span span-1">
          <h4><@u.message "infoGraph.choose.type.COLLABORATIVE_COUNCIL.title" /></h4>
        </div>
        <div class="span span-1">
          <h4><@u.message "infoGraph.choose.type.COLLABORATIVE_CITIZEN.title" /></h4>
        </div>

        <div class="span span-1">
          <span class="type">
            <span class="icon icon-type normal"></span>
          </span>
          <p><@u.messageHTML "infoGraph.choose.type.COLLABORATIVE" /></p>
        </div>
        <div class="span span-1">
          <span class="type">
            <span class="icon icon-type two-percent"></span>
          </span>
          <p><@u.messageHTML "infoGraph.choose.type.COLLABORATIVE_COUNCIL" /></p>
        </div>
        <div class="span span-1">
          <span class="type">
            <span class="icon icon-type five-percent"></span>
          </span>
          <p><@u.messageHTML "infoGraph.choose.type.COLLABORATIVE_CITIZEN" /></p>
        </div>
      </div>

    	</div>
	</div>

	<div class="block cf">
		<@header "infoGraph.collect.title" 2 />

    <div class="content top-pad cf">
      <div class="lines line-group-2">
        <div class="line line-30 left green">
          <@arrow "middle" />
        </div>
        <div class="line line-30 middle green">
          <@arrow "middle" />
        </div>
        <div class="line line-30 right green">
          <@arrow "middle" />
        </div>
      </div>

  		<div class="c-group c-group-2 cf">
        <div class="span span-1">
          <span class="icon collect-normal"></span>
          <p><@u.message "infoGraph.collect.COLLABORATIVE" /></p>
        </div>
        <div class="span span-1">
          <span class="icon collect-two-percent"></span>
          <p><@u.message "infoGraph.collect.COLLABORATIVE_COUNCIL" /></p>
        </div>
        <div class="span span-1">
          <span class="icon collect-five-percent"></span>
          <p><@u.message "infoGraph.collect.COLLABORATIVE_CITIZEN" /></p>
        </div>
  		</div>
    </div>
	</div>

	<div class="block cf">
		<@header "infoGraph.send.title" 3 />

    <div class="content top-pad cf">
    	<div class="lines line-group-3">
      		<div class="line line-45 left green">
      			<@arrow "right" />
          </div>

          <div class="line line-10 middle green">
            <@arrow "middle" />
          </div>

          <div class="line line-45 right green">
          		<@arrow "left" />
          </div>
      </div>

    	<div class="c-group c-group-3 cf">
		    <span class="icon municipality"></span>

		    <p><@u.messageHTML "infoGraph.send.description" /></p>
	    </div>
    </div>
	</div>

	<div class="block cf">
    	<@header "infoGraph.decision.title" 4 />

        <div class="content top-pad cf">
        	<div class="lines line-group-4">
            	<div class="line line-100 green">
            		<@arrow "middle" />
            	</div>
      		</div>

        	<div class="c-group c-group-4">
        		<p><@u.messageHTML "infoGraph.decision.description" /></p>
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

