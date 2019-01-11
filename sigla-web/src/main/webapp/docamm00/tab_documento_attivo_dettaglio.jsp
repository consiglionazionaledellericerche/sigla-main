<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.docamm00.tabrif.bulk.*,
		it.cnr.contab.docamm00.docs.bulk.*,
		it.cnr.contab.docamm00.bp.*,
		it.cnr.contab.anagraf00.tabrif.bulk.*"
%>

<% CRUDDocumentoGenericoAttivoBP bp = (CRUDDocumentoGenericoAttivoBP)BusinessProcess.getBusinessProcess(request);%>
	<%Documento_generico_rigaBulk riga = (Documento_generico_rigaBulk)bp.getDettaglio().getModel();%>
	<%bp.getDettaglio().writeHTMLTable(pageContext,"righiSet",true,false,true,"100%","100px"); %>
<div class="Group card">
  <table style="width: 75%;">
		<tr>
	      	<td style="width: 15%;">
		  		<% bp.getDettaglio().writeFormLabel(out,"im_riga_divisa_euro");%>
		  	</td>
		  	<td>
		  		<% bp.getDettaglio().writeFormInput(out,null,"im_riga_divisa_euro",false,null,"");%>
		  	</td>
		<% if (bp.isDetailDoubling()) { %>
		   	<td>
		  		<% bp.getDettaglio().writeFormLabel(out,"im_riga_sdoppia");%>
		  	</td>
		  	<td>
		  		<% bp.getDettaglio().writeFormInput(out,null,"im_riga_sdoppia",false,null,"");%>
		  	</td>
		 <%}%>
      </tr>
      <tr>
      	<td>
	  		<% bp.getDettaglio().writeFormLabel(out,"ds_riga");%>
	  	</td>
	  	<td colspan="3" class="w-100">
	  		<% bp.getDettaglio().writeFormInput(out,null,"ds_riga",false,null,"");%>
	  	</td>
      </tr>
		<tr>
      		<% bp.getDettaglio().writeFormField(out,"dt_da_competenza_coge");%>
      		<td>
      			<% bp.getDettaglio().writeFormLabel(out,"dt_a_competenza_coge");%>
      			<% bp.getDettaglio().writeFormInput(out,"dt_a_competenza_coge");%>
      		</td>
      </tr>      
  </table>
</div>

<div class="Group card">
  <table class="Panel" style="width: 75%;">
  <tr>
  	<td>
	    <% bp.getDettaglio().writeFormLabel(out,"terzo");%>
    </td>
    <td colspan="3">
  	  	<% bp.getDettaglio().writeFormInput(out,"terzo");%>
      </td>
  	</tr>	
		<tr>
			<% bp.getDettaglio().writeFormField(out, "cd_precedente");%>
		</tr>
      <tr>
		<% bp.getDettaglio().writeFormField(out,"nome");%>
		<% bp.getDettaglio().writeFormField(out,"cognome");%>
      </tr>
      
      <tr>
		<td>
			<% bp.getDettaglio().writeFormLabel(out,"ragione_sociale");%>
		</td>
		<td colspan="3">
	 		<% bp.getDettaglio().writeFormInput(out,"ragione_sociale");%>
		</td>
      </tr>

	  <tr>
		<td>
			<% bp.getDettaglio().writeFormLabel(out,"denominazione_sede"); %>
		</td>
		<td colspan="3">
			<% bp.getDettaglio().writeFormInput(out,"denominazione_sede"); %>
		</td>
	  </tr>
	        
      <tr>
		<% bp.getDettaglio().writeFormField(out,"codice_fiscale");%>
		<% bp.getDettaglio().writeFormField(out,"partita_iva");%>
      </tr>

     <tr>
		<% bp.getDettaglio().writeFormField(out,"via_fiscale");%>
		<% bp.getDettaglio().writeFormField(out,"num_civico");%>
      </tr>
      <tr>
		<% bp.getDettaglio().writeFormField(out,"ds_comune");%>
		<% bp.getDettaglio().writeFormField(out,"ds_provincia");%>
      </tr> 
    </table>
   </div>

   <div class="Group card">   
	<table>	
	  <tr>
  		<% bp.getDettaglio().writeFormField(out,"termini_pagamento_uo_cds");%>
      </tr>
      <tr>
     	<td>
 	     	<% bp.getDettaglio().writeFormLabel(out,"modalita_pagamento_uo_cds");%>
      	</td>      	
     	<td>
	      	<% bp.getDettaglio().getBulkInfo().writeFormInput(out,bp.getDettaglio().getModel(),null, "modalita_pagamento_uo_cds",bp.isROBank_ModPag(riga),null,"onChange=\"submitForm('doOnModalitaPagamentoUOCDSChange')\"",bp.getDettaglio().getInputPrefix(), bp.getDettaglio().getStatus(), bp.getDettaglio().getFieldValidationMap(), bp.getParentRoot().isBootstrap());%>	
	      </td>   
		<td>
			<% 	if (riga != null && riga.getBanca_uo_cds() != null) {
				bp.getDettaglio().getBulkInfo().writeFormInput(out,bp.getDettaglio().getModel(),null, "listabanche_uo_cds",bp.isROBank(riga),null,"onChange=\"submitForm('doOnModalitaPagamentoUOCDSChange')\"",bp.getDettaglio().getInputPrefix(), bp.getDettaglio().getStatus(), bp.getDettaglio().getFieldValidationMap(), bp.getParentRoot().isBootstrap());
				} %>
   		</td>
      </tr>
      </table>
      <table>
		<tr>
		  	<td colspan="2">
		<%	if (riga != null){
		 	if(riga.getBanca_uo_cds() != null) {
				if (Rif_modalita_pagamentoBulk.BANCARIO.equalsIgnoreCase(riga.getBanca_uo_cds().getTi_pagamento())) {
			 	     	bp.getDettaglio().writeFormInput(out,"contoBUO");
				} else if (Rif_modalita_pagamentoBulk.POSTALE.equalsIgnoreCase(riga.getBanca_uo_cds().getTi_pagamento())) {
			 	     	bp.getDettaglio().writeFormInput(out,"contoPUO");
				} else if (Rif_modalita_pagamentoBulk.QUIETANZA.equalsIgnoreCase(riga.getBanca_uo_cds().getTi_pagamento())) {
			 	     	bp.getDettaglio().writeFormInput(out,"contoQUO");
				} else if (Rif_modalita_pagamentoBulk.ALTRO.equalsIgnoreCase(riga.getBanca_uo_cds().getTi_pagamento())) { 
			 	     	bp.getDettaglio().writeFormInput(out,"contoAUO");
				} else if (Rif_modalita_pagamentoBulk.BANCA_ITALIA.equalsIgnoreCase(riga.getBanca_uo_cds().getTi_pagamento())) {
				        if (riga.getBanca_uo_cds().isTABB()) {
				            bp.getDettaglio().writeFormInput(out,"contoBUO");
				        } else {
			 	     	    bp.getDettaglio().writeFormInput(out,"contoIUO");
			 	     	}
				} else if (Rif_modalita_pagamentoBulk.IBAN.equalsIgnoreCase(riga.getBanca_uo_cds().getTi_pagamento())) { 
		 	     	bp.getDettaglio().writeFormInput(out,"contoNUO");
				}
			} else {
				if ((riga.getModalita()==null || riga.getModalita().isEmpty()) && !bp.isSearching()) { %>
					<span class="FormLabel" style="color:red">
						Nessuna modalità di pagamento trovata per la UO
					</span>
				<%} else if (riga.getModalita_pagamento_uo_cds() != null && !bp.isSearching()) {%>
				<span class="FormLabel" style="color:red">
					Nessun riferimento trovato per la modalità di pagamento selezionata!
				</span>
								
		<%	}}} %>
			  	</td>
			</tr>
		
    </table>
   </div>
   <% if (bp.isDetailDoubling()) { %>
   	<script>setFocusOnInput('main.Dettaglio.im_riga_sdoppia')</script>
   <% bp.setDetailDoubling(false);} %>
