<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.doccont00.ordine.bulk.*,
		it.cnr.contab.doccont00.bp.*,
		it.cnr.contab.anagraf00.tabrif.bulk.*"
%>
<% CRUDOrdineBP bp = (CRUDOrdineBP)BusinessProcess.getBusinessProcess(request); 
	OrdineBulk ordine = (OrdineBulk)bp.getModel(); %>
	
<div class="Group">	
<table>			

  <tr>
	<td><% bp.getController().writeFormLabel(out,"cognome"); %></td>
	<td><% bp.getController().writeFormInput(out,"cognome"); %></td>
	<td><% bp.getController().writeFormLabel(out,"nome"); %></td>
	<td><% bp.getController().writeFormInput(out,"nome"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ragione_sociale"); %></td>
	<td colspan=3><% bp.getController().writeFormInput(out,"ragione_sociale"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"via_sede"); %></td>
	<td colspan=4><% bp.getController().writeFormInput(out,"via_sede"); %>
		<% bp.getController().writeFormInput(out,"numero_civico_sede"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"codice_fiscale"); %></td>
	<td><% bp.getController().writeFormInput(out,"codice_fiscale"); %></td>
	<td><% bp.getController().writeFormLabel(out,"partita_iva"); %></td>
	<td><% bp.getController().writeFormInput(out,"partita_iva"); %></td>
  </tr>
</table>
</div>

<div class="Group">
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"termini_pagamento");%></td>
	<td colspan="2"><% bp.getController().writeFormInput(out,"termini_pagamento");%></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"modalita_pagamento");%></td>
	<td><% bp.getController().writeFormInput(out,null,"modalita_pagamento",false,null,"onChange=\"submitForm('doOnModalitaPagamentoChange')\"");%></td>
	<td>
	  <% if (ordine.getBanca() != null) {
			bp.getController().writeFormInput(out, null, "listabanche", false, null, "");
		 } %>
	</td>
  </tr>

  <tr>
 	<td colspan="3">
	<%	if (ordine.getBanca() != null) {
			if (Rif_modalita_pagamentoBulk.BANCARIO.equalsIgnoreCase(ordine.getBanca().getTi_pagamento())) {
	 	     	bp.getController().writeFormInput(out,"contoB");
			} else if (Rif_modalita_pagamentoBulk.POSTALE.equalsIgnoreCase(ordine.getBanca().getTi_pagamento())) {
			   	bp.getController().writeFormInput(out,"contoP");
			} else if (Rif_modalita_pagamentoBulk.QUIETANZA.equalsIgnoreCase(ordine.getBanca().getTi_pagamento())) {
			   	bp.getController().writeFormInput(out,"contoQ");
			} else if (Rif_modalita_pagamentoBulk.ALTRO.equalsIgnoreCase(ordine.getBanca().getTi_pagamento())) { 
			   	bp.getController().writeFormInput(out,"contoA");
			} else if (Rif_modalita_pagamentoBulk.IBAN.equalsIgnoreCase(ordine.getBanca().getTi_pagamento())) { 
			   	bp.getController().writeFormInput(out,"contoN");
			} else if (Rif_modalita_pagamentoBulk.BANCA_ITALIA.equalsIgnoreCase(ordine.getBanca().getTi_pagamento()) && ordine.getBanca().isTABB()) {
                bp.getController().writeFormInput(out,"contoB");
            }
  		} else if (ordine.getModalitaPagamento() != null && (ordine.getTerzo() != null && ordine.getTerzo().getCrudStatus() != ordine.getTerzo().UNDEFINED)) { %>
			<span class="FormLabel" style="color:red">
				Nessun riferimento trovato per la modalità di pagamento selezionata!
			</span>
	<%	} %>
  	</td>
  </tr>

</table>
</div>