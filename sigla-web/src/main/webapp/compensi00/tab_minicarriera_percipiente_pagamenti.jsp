<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.compensi00.bp.*,
		it.cnr.contab.compensi00.docs.bulk.*,
		it.cnr.contab.anagraf00.tabrif.bulk.*"
%>

<%	CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)BusinessProcess.getBusinessProcess(request);
	MinicarrieraBulk carriera = (MinicarrieraBulk)bp.getModel();
	it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk percipiente = carriera.getPercipiente();
%>

<div class="Group card">   
	<table>	
	  <tr>
     	<td>
 	     	<% bp.getController().writeFormLabel(out,"termini_pagamento");%>
      	</td>      	
     	<td>
	      	<% bp.getController().writeFormInput(out,null,"termini_pagamento",false,null,"");%>	
      	</td>   
      </tr>
      <tr>
     	<td>
 	     	<% bp.getController().writeFormLabel(out,"modalita_pagamento");%>
      	</td>      	
     	<td>
	      	<% bp.getController().writeFormInput(out,null,"modalita_pagamento",false,null,"onChange=\"submitForm('doOnModalitaPagamentoChange')\"");%>
      	</td>   
		<td>
			<% 	if (carriera.getBanca() != null) {
					bp.getController().writeFormInput(out, null, "listaBanche", false, null, "");
				} %>
   		</td>
      </tr>
		<tr>
		  	<td colspan="2">
		<%	if (carriera.getBanca() != null) {
				if (Rif_modalita_pagamentoBulk.BANCARIO.equalsIgnoreCase(carriera.getBanca().getTi_pagamento())) {
			 	     	bp.getController().writeFormInput(out,"contoB");
				} else if (Rif_modalita_pagamentoBulk.POSTALE.equalsIgnoreCase(carriera.getBanca().getTi_pagamento())) {
			 	     	bp.getController().writeFormInput(out,"contoP");
				} else if (Rif_modalita_pagamentoBulk.QUIETANZA.equalsIgnoreCase(carriera.getBanca().getTi_pagamento())) {
			 	     	bp.getController().writeFormInput(out,"contoQ");
				} else if (Rif_modalita_pagamentoBulk.ALTRO.equalsIgnoreCase(carriera.getBanca().getTi_pagamento())) {
			 	     	bp.getController().writeFormInput(out,"contoA");
				} else if (Rif_modalita_pagamentoBulk.IBAN.equalsIgnoreCase(carriera.getBanca().getTi_pagamento())) {
		 	     	bp.getController().writeFormInput(out,"contoN");
				} else if (Rif_modalita_pagamentoBulk.BANCA_ITALIA.equalsIgnoreCase(carriera.getBanca().getTi_pagamento()) && carriera.getBanca().isTABB()) {
                    bp.getController().writeFormInput(out,"contoB");
                }
			} else if (carriera.getModalita_pagamento() != null && (percipiente != null && percipiente.getCrudStatus() != percipiente.UNDEFINED)) { %>
				<span class="FormLabel" style="color:red">
					Nessun riferimento trovato per la modalità di pagamento selezionata!
				</span>
		<%	} %>
			<td>
		<tr>
    </table>
</div>