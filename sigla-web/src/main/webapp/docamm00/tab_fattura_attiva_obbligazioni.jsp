<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.docamm00.tabrif.bulk.*,
		it.cnr.contab.docamm00.docs.bulk.*,
		it.cnr.contab.doccont00.core.bulk.*,
		it.cnr.contab.docamm00.bp.*,
		it.cnr.contab.anagraf00.tabrif.bulk.*"
%>

<%	CRUDNotaDiCreditoAttivaBP bp = (CRUDNotaDiCreditoAttivaBP)BusinessProcess.getBusinessProcess(request);
	Nota_di_credito_attivaBulk notaDiCredito = (Nota_di_credito_attivaBulk)bp.getModel();
	it.cnr.contab.anagraf00.core.bulk.TerzoBulk cliente = notaDiCredito.getCliente(); %>
	<div class="Group" style="width:100%">
		<table width="100%">
			<tr>
			  	<td colspan="2">
				  	<span class="FormLabel" style="color:black">Impegni</span>
			  	</td>
			</tr>
			<tr>
			  	<td colspan="2">
					<%	bp.getObbligazioniController().writeHTMLTable(pageContext,"default",false,false,true,"100%","100px"); %>
			  	</td>
			</tr>
			<tr>
			  	<td>
		   			<% bp.writeFormLabel(out,"importoTotalePerObbligazioni"); %>
			  	</td>
			  	<td>
				  	<% bp.writeFormInput(out,null,"importoTotalePerObbligazioni",false,null,"style=\"color:black\"");%>
			  	</td>
	   		</tr>
		</table>
	</div>
	<div class="Group" style="width:100%">
   		<table width="100%">
			<tr>
			  	<td>
		   			<%	Obbligazione_scadenzarioBulk obbligazione_scadenzario = (Obbligazione_scadenzarioBulk)bp.getObbligazioniController().getModel();
		   				if (obbligazione_scadenzario == null) { %>
						  	<span class="FormLabel" style="color:black">Dettagli interessati dalla scadenza obbligazione</span>
		   			<%	} else { %>
						  	<span class="FormLabel" style="color:black">Dettagli interessati dalla scadenza obbligazione "<%=obbligazione_scadenzario.getDs_scadenza()%>"</span>
		   			<%	} %>
			  	</td>
			</tr>
			<tr>
			  	<td>
					<% bp.getDettaglioObbligazioneController().writeHTMLTable(pageContext,"righeNdCSet",false,false,true,"100%","150px"); %>
			  	</td>
			</tr>
   		</table>
	</div>

   <div class="Group card">   
	<table>	
	  <tr>
     	<% bp.getController().writeFormField(out,"termini_pagamento");%>
      </tr>
      <tr>
     	<td>
 	     	<% bp.getController().writeFormLabel(out,"modalita_pagamento");%>
      	</td>      	
     	<td>
	      	<% bp.getController().writeFormInput(out,null,"modalita_pagamento",false,null,"onChange=\"submitForm('doOnModalitaPagamentoChange')\"");%>	
      	</td>   
		<td>
			<% 	if (notaDiCredito.getBanca() != null) {
					bp.getController().writeFormInput(out, null, "listabanche", false, null, "");
				} %>
   		</td>
      </tr>
		<tr>
		  	<td colspan="2">
			<%	if (notaDiCredito.getBanca() != null) {
					if (Rif_modalita_pagamentoBulk.BANCARIO.equalsIgnoreCase(notaDiCredito.getBanca().getTi_pagamento())) {
				 	     	bp.getController().writeFormInput(out,"contoB");
					} else if (Rif_modalita_pagamentoBulk.POSTALE.equalsIgnoreCase(notaDiCredito.getBanca().getTi_pagamento())) {
				 	     	bp.getController().writeFormInput(out,"contoP");
					} else if (Rif_modalita_pagamentoBulk.QUIETANZA.equalsIgnoreCase(notaDiCredito.getBanca().getTi_pagamento())) {
				 	     	bp.getController().writeFormInput(out,"contoQ");
					} else if (Rif_modalita_pagamentoBulk.ALTRO.equalsIgnoreCase(notaDiCredito.getBanca().getTi_pagamento())) { 
				 	     	bp.getController().writeFormInput(out,"contoA");
					} else if (Rif_modalita_pagamentoBulk.IBAN.equalsIgnoreCase(notaDiCredito.getBanca().getTi_pagamento())) { 
			 	     	bp.getController().writeFormInput(out,"contoN");
					} else if (Rif_modalita_pagamentoBulk.BANCA_ITALIA.equalsIgnoreCase(notaDiCredito.getBanca().getTi_pagamento()) && notaDiCredito.getBanca().isTABB()) {
                        bp.getController().writeFormInput(out,"contoB");
                    }
				} else if (notaDiCredito.getModalita_pagamento() != null && (cliente != null && cliente.getCrudStatus() != cliente.UNDEFINED)) { %>
					<span class="FormLabel" style="color:red">
						Nessun riferimento trovato per la modalità di pagamento selezionata!
					</span>
			<%	} %>
			<td>
		</tr>

    </table>
   </div>