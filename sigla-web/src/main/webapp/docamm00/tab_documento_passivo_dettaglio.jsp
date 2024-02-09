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

<% 	CRUDDocumentoGenericoPassivoBP bp = (CRUDDocumentoGenericoPassivoBP)BusinessProcess.getBusinessProcess(request);	
	Documento_generico_rigaBulk riga = (Documento_generico_rigaBulk)bp.getDettaglio().getModel();
	bp.getDettaglio().writeHTMLTable(pageContext,"righiSet",true,false,true,"100%","auto;max-height:30vh"); %>
  
<div class="Group card">
  <table style="width: 75%;">
		<tr>
      	<td style="width: 15%;">
	  		<% bp.getDettaglio().writeFormLabel(out,"im_riga_divisa");%>
	  	</td>
	  	<td>
	  		<% bp.getDettaglio().writeFormInput(out,null,"im_riga_divisa",false,null,"");%>
	  	</td>
      	<td>
	  		<% bp.getDettaglio().writeFormLabel(out,"im_riga");%>
	  		<% bp.getDettaglio().writeFormInput(out,null,"im_riga",false,null,"");%>
	  	</td>
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
  		<% bp.getDettaglio().writeFormField(out,"termini_pagamento");%>
      </tr>
      <tr>
     	<td>
 	     	<% bp.getDettaglio().writeFormLabel(out,"modalita_pagamento");%>
      	</td>      	
     	<td>
	      	<% bp.getDettaglio().writeFormInput(out,null,"modalita_pagamento",false,null,"onChange=\"submitForm('doOnModalitaPagamentoChange')\"");%>	
      	</td>   
		<td>
			<% 	if (riga != null && riga.getBanca() != null) {
					bp.getDettaglio().writeFormInput(out,null,"listabanche",false,null,"");
				} %>
   		</td>
      </tr>
      </table>
      <table>
		<tr>
		  	<td colspan="2">
	<%	if (riga != null && riga.getTerzo()!=null){
			
			if(riga.getBanca() != null) {
				if (Rif_modalita_pagamentoBulk.BANCARIO.equalsIgnoreCase(riga.getBanca().getTi_pagamento())) {
			 	     	bp.getDettaglio().writeFormInput(out,"contoB");
				} else if (Rif_modalita_pagamentoBulk.POSTALE.equalsIgnoreCase(riga.getBanca().getTi_pagamento())) {
			 	     	bp.getDettaglio().writeFormInput(out,"contoP");
				} else if (Rif_modalita_pagamentoBulk.QUIETANZA.equalsIgnoreCase(riga.getBanca().getTi_pagamento())) {
			 	     	bp.getDettaglio().writeFormInput(out,"contoQ");
				} else if (Rif_modalita_pagamentoBulk.ALTRO.equalsIgnoreCase(riga.getBanca().getTi_pagamento())) { 
			 	     	bp.getDettaglio().writeFormInput(out,"contoA");
				} else if (Rif_modalita_pagamentoBulk.IBAN.equalsIgnoreCase(riga.getBanca().getTi_pagamento())) { 
		 	     	bp.getDettaglio().writeFormInput(out,"contoN");
				} else if (Rif_modalita_pagamentoBulk.BANCA_ITALIA.equalsIgnoreCase(riga.getBanca().getTi_pagamento()) && riga.getBanca().isTABB()) {
                       bp.getDettaglio().writeFormInput(out,"contoB");
                }
			}
			else{
			if ((riga.getModalita()==null || riga.getModalita().isEmpty()) && riga.getTerzo().getAnagrafico()!=null && !bp.isSearching()) { %>
				<span class="FormLabel" style="color:red"> Nessuna modalità di pagamento trovata</span>
			<%} else if (riga.getModalita_pagamento() != null && riga.getTerzo().getCd_terzo()!=null && !bp.isSearching()) {%>
				<span class="FormLabel" style="color:red">Nessun riferimento trovato per la modalità di pagamento selezionata!</span>
		<%	}}
		%>
		</tr>
		<% if (riga.getBanca()!=null && riga.getCessionario() != null) { %>
				<tr>
					<td>
						<% bp.getDettaglio().writeFormLabel(out,"cd_cessionario");%>
					</td>   
					<td>
						<% bp.getDettaglio().writeFormInput(out,null,"cd_cessionario",false,null,"");%>						
						<% bp.getDettaglio().writeFormInput(out,null,"denom_sede_cessionario",false,null,"");%>	
					</td>   
				</tr>
		<% }
	} %>

    </table>
   </div>