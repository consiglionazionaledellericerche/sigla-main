<%@ page 
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
	bp.getDettaglio().writeHTMLTable(pageContext,"righiSet",true,false,true,"100%","100px"); %>
  
<div class="Group">
  <table>
		<tr>
      	<td>
	  		<% bp.getDettaglio().writeFormLabel(out,"im_riga_divisa");%>
	  	</td>
	  	<td>
	  		<% bp.getDettaglio().writeFormInput(out,null,"im_riga_divisa",false,null,"");%>
	  	</td>
      	<td>
	  		<% bp.getDettaglio().writeFormLabel(out,"im_riga");%>
	  	</td>
	  	<td colspan=3>
	  		<% bp.getDettaglio().writeFormInput(out,null,"im_riga",false,null,"");%>
	  	</td>
      </tr>
      <tr>
      	<td>
	  		<% bp.getDettaglio().writeFormLabel(out,"ds_riga");%>
	  	</td>
	  	<td>
	  		<% bp.getDettaglio().writeFormInput(out,null,"ds_riga",false,null,"");%>
	  	</td>
      </tr>
		<tr>
      		<% bp.getDettaglio().writeFormField(out,"dt_da_competenza_coge");%>
      		<% bp.getDettaglio().writeFormField(out,"dt_a_competenza_coge");%>
      </tr>      
  </table>
</div>
<div class="Group">
  <table class="Panel">
  <tr>
  	<td>
	    <% bp.getDettaglio().writeFormLabel(out,"cd_terzo");%>
    </td>
      <td>
	    <% bp.getDettaglio().writeFormInput(out,"cd_terzo");%>
 	   	<% bp.getDettaglio().writeFormInput(out,"denominazione_sede");%>
  	  	<% bp.getDettaglio().writeFormInput(out,"terzo");%>
  	  	<% bp.getDettaglio().writeFormInput(out, "crea_terzo");%>
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

   <div class="Group">   
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
					bp.getDettaglio().writeFormInput(out,"listabanche");
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