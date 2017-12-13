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
    <legend class="GroupLabel ml-2">
    	<% bp.getController().writeFormInput(out,null,"statoText",true,"GroupLabel text-primary h3 inputFieldReadOnly",
    			bp.getParentRoot().isBootstrap()?null:"style=\"background: #F5F5DC;background-color:transparent;border-style : none; cursor:default;font-size : 16px;\"");%>
    </legend>    
    <table class="Panel" align="left" cellspacing=4 cellpadding=4>
	    <tr>
           <% bp.getController().writeFormField(out,"esercizio");%>
           <% bp.getController().writeFormField(out,"pg_richiesta");%>
        </tr>  
    </table>
<% } %>    
<table class="Panel w-100">
<% if (!bp.getParentRoot().isBootstrap()) { %>
	  <tr><td>&nbsp;</td></tr>
<% } %>
      <tr>
      	<td>
	      <div class="GroupLabel h3 text-primary ml-2">Estremi del richiedente</div>         
	      <div class="Group card p-2 mb-2">
	      	<table class="w-100">
			  <tr>
		         <td><% bp.getController().writeFormLabel(out,"cds");%></td>
				 <td><% bp.getController().writeFormInput(out,"default","cds",!bp.isUoEnte(),null,null); %></td>
		      </tr>  	      
			  <tr>
		         <td><% bp.getController().writeFormLabel(out,"unita_organizzativa");%></td>
				 <td><% bp.getController().writeFormInput(out,"default","unita_organizzativa",!(bp.isUoEnte()||bp.isUtenteAbilitatoFunzioniIncarichi()),null,null); %></td>
		      </tr>  	      
			  <tr>
		         <td><% bp.getController().writeFormLabel(out,"email_risposte");%></td>
		         <td><% bp.getController().writeFormInput(out,"email_risposte");%></td>
		      </tr>  	
	      	</table>
	     </div>
       </td>
      </tr>
      
	  <%if (bp.isSearching()||
			model.getData_pubblicazione()!=null || model.getData_fine_pubblicazione()!=null ||
			model.getData_scadenza()!=null) {%>
	<% if (!bp.getParentRoot().isBootstrap()) { %>
	  <tr><td>&nbsp;</td></tr>
	<% } %>
	  <tr>
	  	<td>
	      <div class="GroupLabel h3 text-primary ml-2">Date di validit�</div>
	      <div class="Group card p-2 mb-2">
	      <table class="w-100">         
			  <tr>
			    <td><% bp.getController().writeFormField(out,"data_pubblicazione");%></td>
			    <td>&nbsp;&nbsp;</td>
		        <td><% bp.getController().writeFormField(out,"data_fine_pubblicazione");%></td>
		        <td>&nbsp;&nbsp;</td>
		        <td><% bp.getController().writeFormField(out,"data_scadenza");%></td>
		      </tr>      
	      </table>
	      </div>
	    </td>
	  </tr>
	  <%}%>

      <% if (!model.isRichiestaProvvisoria() && !model.isPubblicazioneInCorso()) {%>
<% if (!bp.getParentRoot().isBootstrap()) { %>
	  <tr><td>&nbsp;</td></tr>
<% } %>
      <tr>
        <td>
      	  <div class="GroupLabel h3 text-primary ml-2">Esito ricerca</div>
	      <div class="Group card p-2 mb-2">
	      <table width="100%" class="w-100">
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
		  	 	 	 <td><% Button.write(out,bp.encodePath("img/book_opened.gif"),bp.encodePath("img/book_closed.gif"),"Avvia Procedura<BR>Conferimento<BR>Incarico","javascript:submitForm('doRichiediContratto')", null, "Chiude la ricerca e procede ad attivare una richiesta di contratto ", ((CRUDIncarichiRichiestaBP)bp).isButtonRichiediContrattoEnabled(), bp.getParentRoot().isBootstrap()); %></td>
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
		 	 	 	<td rowspan="3" align="center" valign="middle"><% Button.write(out,bp.encodePath("img/book_opened.gif"),bp.encodePath("img/book_closed.gif"),"Avvia Procedura<BR>Conferimento<BR>Incarico","javascript:submitForm('doRichiediContratto')", null, "Chiude la ricerca e procede ad attivare una richiesta di contratto ", ((CRUDIncarichiRichiestaBP)bp).isButtonRichiediContrattoEnabled(), bp.getParentRoot().isBootstrap()); %></td>
 	         	<% } %>
			</tr>
			<tr>
				<% if (!model.isRichiestaChiusa()) {%>
		      		<% bp.getController().writeFormField(out,"nr_risorse_trovate_na");%>
				<% } else { %>
		      		<% bp.getController().writeFormField(out,"nr_risorse_trovate_na_ro");%>
 	         	<% } %>
	 	 	</tr>
	 	 	<tr>
				<% if (!model.isRichiestaChiusa()) {%>
		      		<% bp.getController().writeFormField(out,"nr_risorse_trovate_no");%>
				<% } else { %>
		      		<% bp.getController().writeFormField(out,"nr_risorse_trovate_no_ro");%>
 	         	<% } %>
		    </tr>  	      
	        <% } %>
	      </table>
       	  </div>
	    </td>
	  </tr>
	  <% } else {%>
	<% if (!bp.getParentRoot().isBootstrap()) { %>
	  <tr><td>&nbsp;</td></tr>
	<% } %>

	  <tr>
	     <td>
	 	 	<div class="GroupLabel h3 text-primary ml-2">Ricerca</div>
		    <div class="Group card p-2 mb-2">
		    <table class="w-25">
		      <tr>
			    <% bp.getController().writeFormField(out,"nr_risorse_da_trovare");%>
			  </tr>      
     		</table>
       	    </div>
	     </td>
	  </tr>
	  <% } %>
 
<% if (!bp.getParentRoot().isBootstrap()) { %>
	  <tr><td>&nbsp;</td></tr>
<% } %>

      <tr>
      	<td>
	      <div class="GroupLabel h3 text-primary ml-2">Informazioni sull'attivit&aacute;</div>
	      <div class="Group card p-2 mb-2">
	      <table class="w-100">
			  <tr>         
		         <% bp.getController().writeFormField(out,"attivita");%>
		      </tr>                     	
			  <tr>         
		         <% bp.getController().writeFormField(out,"durata");%>
		      </tr>                     	
			  <tr>         
		         <% bp.getController().writeFormField(out,"sede_lavoro");%>
		      </tr> 
      	  </table>
      	  </div>
        </td>
      </tr>

<% if (!bp.getParentRoot().isBootstrap()) { %>
	  <tr><td>&nbsp;</td></tr>
<% } %>

      <tr>
      	<td>
	      <div class="GroupLabel h3 text-primary ml-2">Informazioni aggiuntive</div>
	      <div class="Group card p-2 mb-2">
	      <table class="w-100">
		 	  <tr>         
		         <% bp.getController().writeFormField(out,"competenze");%>
		      </tr> 
			  <tr>         
		         <% bp.getController().writeFormField(out,"note");%>
		      </tr> 
	      </table>
	      </div>
        </td>
      </tr>
	</table>   
<% if (model.getNrContrattiAttivati().compareTo(new Integer(0))==0) {%>
<%="</fieldset>"%> 	
<% } %>
