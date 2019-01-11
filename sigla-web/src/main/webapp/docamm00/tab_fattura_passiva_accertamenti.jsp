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

<%	CRUDNotaDiCreditoBP bp = (CRUDNotaDiCreditoBP)BusinessProcess.getBusinessProcess(request);
	Nota_di_creditoBulk notaDiCredito = (Nota_di_creditoBulk)bp.getModel();
	it.cnr.contab.anagraf00.core.bulk.TerzoBulk ente = notaDiCredito.getEnte(); %>
	<div class="Group card" style="width:100%">
		<table width="100%">
			<tr>
			  	<td colspan="3">
				  	<span class="FormLabel" style="color:black">Accertamenti</span>
			  	</td>
			</tr>
			<tr>
			  	<td colspan="3">
					<%	bp.getAccertamentiController().writeHTMLTable(pageContext,"default",false,false,true,"100%","100px"); %>
			  	</td>
			</tr>
			<tr>
			  	<td>
		   			<% bp.writeFormLabel(out,"importoTotalePerAccertamenti"); %>
			  	</td>
			  	<td>
				  	<% bp.writeFormInput(out,null,"importoTotalePerAccertamenti",false,null,"style=\"color:black\"");%>
			  	</td>
			  	<td>
					<% if (notaDiCredito.quadraturaInDeroga()) { %>
						<span class="FormLabel" style="color:red">
							Quadratura in deroga
						</span>
					<%	} %>
			  	</td>
	   		</tr>
		</table>
	</div>
	<div class="Group card" style="width:100%">
   		<table width="100%">
			<tr>
			  	<td>
		   			<%	Accertamento_scadenzarioBulk accertamento_scadenzario = (Accertamento_scadenzarioBulk)bp.getAccertamentiController().getModel();
		   				if (accertamento_scadenzario == null) { %>
						  	<span class="FormLabel" style="color:black">Dettagli interessati dalla scadenza accertamento</span>
		   			<%	} else { %>
						  	<span class="FormLabel" style="color:black">Dettagli interessati dalla scadenza accertamento "<%=accertamento_scadenzario.getDs_scadenza()%>"</span>
		   			<%	} %>
			  	</td>
			</tr>
			<tr>
			  	<td>
					<% bp.getDettaglioAccertamentoController().writeHTMLTable(pageContext,"righeNdCSet",false,false,true,"100%","150px"); %>
			  	</td>
			</tr>
   		</table>
	</div>

   <div class="Group card">   
	<table>	
	  <tr>
     	<% bp.getController().writeFormField(out,"termini_pagamento_uo");%>
      </tr>
      <tr>
     	<td>
 	     	<% bp.getController().writeFormLabel(out,"modalita_pagamento_uo");%>
      	</td>      	
     	<td>
	      	<% bp.getController().writeFormInput(out,null,"modalita_pagamento_uo",false,null,"onChange=\"submitForm('doOnModalitaPagamentoUOChange')\"");%>	
      	</td>   
		<td>
			<% 	if (notaDiCredito.getBanca_uo() != null) {
					bp.getController().writeFormInput(out, null, "listabancheuo", bp.isROBank(), null, "");
				} %>
   		</td>
      </tr>
		<tr>
		  	<td colspan="2">
			<%	if (notaDiCredito.getBanca_uo() != null) {
					if (Rif_modalita_pagamentoBulk.BANCARIO.equalsIgnoreCase(notaDiCredito.getBanca_uo().getTi_pagamento())) {
				 	     	bp.getController().writeFormInput(out,"contoBUO");
					} else if (Rif_modalita_pagamentoBulk.POSTALE.equalsIgnoreCase(notaDiCredito.getBanca_uo().getTi_pagamento())) {
				 	     	bp.getController().writeFormInput(out,"contoPUO");
					} else if (Rif_modalita_pagamentoBulk.QUIETANZA.equalsIgnoreCase(notaDiCredito.getBanca_uo().getTi_pagamento())) {
				 	     	bp.getController().writeFormInput(out,"contoQUO");
					} else if (Rif_modalita_pagamentoBulk.ALTRO.equalsIgnoreCase(notaDiCredito.getBanca_uo().getTi_pagamento())) { 
				 	     	bp.getController().writeFormInput(out,"contoAUO");
					} else if (Rif_modalita_pagamentoBulk.BANCA_ITALIA.equalsIgnoreCase(notaDiCredito.getBanca_uo().getTi_pagamento())) {
					        if (notaDiCredito.getBanca_uo().isTABB()) {
					            bp.getController().writeFormInput(out,"contoBUO");
					        } else {
				 	     	    bp.getController().writeFormInput(out,"contoIUO");
				 	     	}
					} else if (Rif_modalita_pagamentoBulk.IBAN.equalsIgnoreCase(notaDiCredito.getBanca_uo().getTi_pagamento())) { 
			 	     	bp.getController().writeFormInput(out,"contoNUO");
					}
				} else if (notaDiCredito.getModalita_pagamento_uo() != null && (ente != null && ente.getCrudStatus() != ente.UNDEFINED)) { %>
					<span class="FormLabel" style="color:red">
						Nessun riferimento trovato per la modalità di pagamento selezionata!
					</span>
			<%	} %>
			<td>
		</tr>
    </table>
   </div>