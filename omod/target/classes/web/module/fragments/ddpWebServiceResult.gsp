<div class="info-section">
    <div class="info-header">
        <h3>Web Service DDP </h3>
    </div>
    
    <div class="info-body">
    	<% if (items != null) { %>
    		
			    	<% items.each { %>
			    	
			    			Site Code : ${ui.format(it.getSiteCode())}<br/>
	    					Patient ID : ${ui.format(it.getPatientId())}<br/>
	    					Date visite : ${ui.format(it.getVisitDate())}<br/>
	    					Date de creation : ${ui.format(it.getCreatedDate())}<br/>
	    					Drug ID : ${ui.format(it.getDrugId())}<br/>
	    					Nom drug : ${ui.format(it.getDrugName())}<br/>
	    					Date dispense : ${ui.format(it.getDispenseDate())}
	    					<br/>
	    					<br/>
				    	
			    	<% } %>
			   
    	<% } %>
    </div>
</div>