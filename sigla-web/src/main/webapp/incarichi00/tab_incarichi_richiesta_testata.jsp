<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.jsp.*,
	    it.cnr.contab.incarichi00.bulk.*,
	    it.cnr.contab.incarichi00.bp.CRUDIncarichiRichiestaBP"
%>

<%
	CRUDIncarichiRichiestaBP bp = (CRUDIncarichiRichiestaBP)BusinessProcess.getBusinessProcess(request);
 	Incarichi_richiestaBulk  model = (Incarichi_richiestaBulk)bp.getModel();
%>
<% if (model.getNrContrattiAttivati().compareTo(new Integer(0))==0) {%>
<fieldset class="fieldset">
    <legend class="GroupLabel"><% bp.getController().writeFormInput(out,null,"statoText",true,"GroupLabel","style=\"background: #F5F5DC;background-color:transparent;border-style : none; cursor:default;font-size : 16px;\"");%></legend>    
	<table class="Panel">
  	  <tr>
		  <td width="100%">
		     <table class="ToolBar" width="100%" cellspacing="0" cellpadding="2">
		     <tr><td>
		  	   <table class="Panel" align="left" cellspacing=4 cellpadding=4>
			    <tr>
		           <td><% bp.getController().writeFormField(out,"esercizio");%></td>
		           <td><% bp.getController().writeFormField(out,"pg_richiesta");%></td>
		        </tr>  
		   	  </table>
		   	</td></tr>
		    </table>	
		  </td>
 	  </tr>
<% } else { %>    
	<table class="Panel">
<% } %>    

 	  <tr><td>&nbsp;</td></tr>
      <tr><td>
      <div class="GroupLabel">Estremi del richiedente</div>         
      <div class="Group"><table>
	  <tr>
         <td><% bp.getController().writeFormLabel(out,"cds");%></td>
		 <td><% bp.getController().writeFormInput(out,"default","cds",!bp.isUoEnte(),"FormInput",null); %></td>
      </tr>  	      
	  <tr>
         <td><% bp.getController().writeFormLabel(out,"unita_organizzativa");%></td>
		 <td><% bp.getController().writeFormInput(out,"default","unita_organizzativa",!(bp.isUoEnte()||bp.isUtenteAbilitatoFunzioniIncarichi()),"FormInput",null); %></td>
      </tr>  	      
	  <tr>
         <td><% bp.getController().writeFormLabel(out,"email_risposte");%></td>
         <td colspan=3><% bp.getController().writeFormInput(out,"email_risposte");%></td>
      </tr>  	
      </table></div>
      </td></tr>
      
	  <%if (bp.isSearching()||
			model.getData_pubblicazione()!=null || model.getData_fine_pubblicazione()!=null ||
			model.getData_scadenza()!=null) {%>
	  	  <tr><td>&nbsp;</td></tr>
	      <tr><td>
	      <div class="GroupLabel">Date di validità</div>
	      <div class="Group"><table>            
		  <tr>
		    <td><% bp.getController().writeFormField(out,"data_pubblicazione");%></td>
		    <td>&nbsp;&nbsp;</td>
	        <td><% bp.getController().writeFormField(out,"data_fine_pubblicazione");%></td>
	        <td>&nbsp;&nbsp;</td>
	        <td><% bp.getController().writeFormField(out,"data_scadenza");%></td>
	      </tr>      
	      </table></div>
	      </td></tr>
	  <%}%>

      <% if (!model.isRichiestaProvvisoria() && !model.isPubblicazioneInCorso()) {%>
 		  <tr><td>&nbsp;</td></tr>
	      <tr><td>
      	  <div class="GroupLabel">Esito ricerca</div>
	      <div class="Group">
	      <table width=100%>
			<% if (!bp.isSearching() && model.getNr_risorse_da_trovare().compareTo(new Integer(1))==0) {%>
			<tr>
	     		<td><% bp.getController().writeFormLabel(out,"nr_risorse_da_trovare");%></td>
		        <td><% bp.getController().writeFormInput(out,"nr_risorse_da_trovare");%></td>
			  	<td><% bp.getController().writeFormLabel(out,"personale_interno");%></td>
		        <td>
				   <% if (!model.isRichiestaChiusa())
		      			bp.getController().writeFormInput(out,"personale_interno");
			    	  else
  	   	 	 	        bp.getController().writeFormInput(out,"personale_interno_ro");
					%>
				</td>
			 	<% if (model.getNrContrattiAttivati().compareTo(model.getNr_risorse_da_trovare())==-1) {%>
					 <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
		  	 	 	 <td><% Button.write(out,bp.encodePath("img/book_opened.gif"),bp.encodePath("img/book_closed.gif"),"Avvia Procedura<BR>Conferimento<BR>Incarico","javascript:submitForm('doRichiediContratto')", null, "Chiude la ricerca e procede ad attivare una richiesta di contratto ", ((CRUDIncarichiRichiestaBP)bp).isButtonRichiediContrattoEnabled() ); %></td>
		  	 	<% } %>
		  	</tr>
			<% } else { %>
			<tr>
	     		<td rowspan="3" align="center" valign="middle"><% bp.getController().writeFormLabel(out,"nr_risorse_da_trovare");%></td>
		        <td rowspan="3" align="center" valign="middle"><% bp.getController().writeFormInput(out,"nr_risorse_da_trovare");%></td>
	   	  	 	<td rowspan="3" align="center" valign="middle"><SPAN class="FormLabel">di cui:</SPAN></td>
				<% if (!model.isRichiestaChiusa()) {%>
		      		<td><% bp.getController().writeFormLabel(out,"nr_risorse_trovate_si");%></td>
	 	         	<td><% bp.getController().writeFormInput(out,"nr_risorse_trovate_si");%></td>
				<% } else { %>
		      		<td><% bp.getController().writeFormLabel(out,"nr_risorse_trovate_si_ro");%></td>
	 	         	<td><% bp.getController().writeFormInput(out,"nr_risorse_trovate_si_ro");%></td>
 	         	<% } %>
		 	    <% if (!bp.isSearching() && model.getNrContrattiAttivati().compareTo(model.getNr_risorse_da_trovare())==-1) {%>				 
		 	 	 	<td rowspan="3" align="center" valign="middle"><% Button.write(out,bp.encodePath("img/book_opened.gif"),bp.encodePath("img/book_closed.gif"),"Avvia Procedura<BR>Conferimento<BR>Incarico","javascript:submitForm('doRichiediContratto')", null, "Chiude la ricerca e procede ad attivare una richiesta di contratto ", ((CRUDIncarichiRichiestaBP)bp).isButtonRichiediContrattoEnabled() ); %></td>
 	         	<% } %>
			</tr>
			<tr>
				<% if (!model.isRichiestaChiusa()) {%>
		      		<td><% bp.getController().writeFormLabel(out,"nr_risorse_trovate_na");%></td>
	 	         	<td><% bp.getController().writeFormInput(out,"nr_risorse_trovate_na");%></td>
				<% } else { %>
		      		<td><% bp.getController().writeFormLabel(out,"nr_risorse_trovate_na_ro");%></td>
	 	         	<td><% bp.getController().writeFormInput(out,"nr_risorse_trovate_na_ro");%></td>
 	         	<% } %>
	 	 	</tr>
	 	 	<tr>
				<% if (!model.isRichiestaChiusa()) {%>
		      		<td><% bp.getController().writeFormLabel(out,"nr_risorse_trovate_no");%></td>
	 	         	<td><% bp.getController().writeFormInput(out,"nr_risorse_trovate_no");%></td>
				<% } else { %>
		      		<td><% bp.getController().writeFormLabel(out,"nr_risorse_trovate_no_ro");%></td>
	 	         	<td><% bp.getController().writeFormInput(out,"nr_risorse_trovate_no_ro");%></td>
 	         	<% } %>
		    </tr>  	      
	        <% } %>
  		  </table>
       	  </div>
	      </td></tr>
	  <% } else {%>
 		  <tr><td>&nbsp;</td></tr>
	      <tr><td>
      	  <div class="GroupLabel">Ricerca</div>
	      <div class="Group">
	      <table>
	      	  <tr>
		         <td><% bp.getController().writeFormLabel(out,"nr_risorse_da_trovare");%></td>
		         <td><% bp.getController().writeFormInput(out,"nr_risorse_da_trovare");%></td>
			  </tr>      
  		  </table>
       	  </div>
	      </td></tr>
	  <% } %>
 
 	  <tr><td>&nbsp;</td></tr>
      <tr><td>
      <div class="GroupLabel">Informazioni sull'attività</div>
      <div class="Group"><table>            
	  <tr>         
         <td><% bp.getController().writeFormLabel(out,"attivita");%></td>
         <td colspan="5"><% bp.getController().writeFormInput(out,"attivita");%></td>
      </tr>                     	
	  <tr>         
         <td><% bp.getController().writeFormLabel(out,"durata");%></td>
         <td colspan="5"><% bp.getController().writeFormInput(out,"durata");%></td>
      </tr>                     	
	  <tr>         
         <td><% bp.getController().writeFormLabel(out,"sede_lavoro");%></td>
         <td colspan="5"><% bp.getController().writeFormInput(out,"sede_lavoro");%></td>
      </tr> 
      </table></div>
      </td></tr>

  	  <tr><td>&nbsp;</td></tr>
      <tr><td>
      <div class="GroupLabel">Informazioni aggiuntive</div>
      <div class="Group"><table>            
 	  <tr>         
         <td><% bp.getController().writeFormLabel(out,"competenze");%></td>
         <td colspan="5"><% bp.getController().writeFormInput(out,"competenze");%></td>
      </tr> 
	  <tr>         
         <td><% bp.getController().writeFormLabel(out,"note");%></td>
         <td colspan="5"><% bp.getController().writeFormInput(out,"note");%></td>
      </tr> 
      </table></div>
      </td></tr>
	</table>   
<% if (model.getNrContrattiAttivati().compareTo(new Integer(0))==0) {%>
<%="</fieldset>"%> 	
<% } %>
