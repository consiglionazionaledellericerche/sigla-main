<%@ page 
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.ordmag.anag00.*,
		it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk,
		it.cnr.contab.ordmag.ordini.bp.CRUDOrdineAcqBP,
		it.cnr.contab.ordmag.ordini.*,
		it.cnr.contab.anagraf00.tabrif.bulk.*"
%>

<% CRUDBP bp = (CRUDOrdineAcqBP)BusinessProcess.getBusinessProcess(request);
	OrdineAcqBulk ordine = (OrdineAcqBulk)bp.getModel();
	it.cnr.contab.anagraf00.core.bulk.TerzoBulk fornitore = ordine.getFornitore();
	boolean roOnAutoGen = false;
%>

	<div class="Group">
		<table>
			<tr>
		<%
			bp.getController().writeFormField(out, "findFornitore");
		%>

			</tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"cd_precedente");%>
				</td>
				<td colspan="3">
					<% bp.getController().writeFormInput(out, null, "cd_precedente", roOnAutoGen, null, "");%>
				</td>
			</tr>
			<%	if (fornitore != null && fornitore.getAnagrafico() != null) {
					if ((fornitore.getAnagrafico().isStrutturaCNR() ||
						fornitore.getAnagrafico().isPersonaGiuridica() ||
						fornitore.getAnagrafico().isDittaIndividuale()) &&
						ordine.getRagioneSociale() != null &&
						ordine.getRagioneSociale().length() > 0) { %>
						<tr>
							<%	if (fornitore.getAnagrafico().isStrutturaCNR()) { %>
									<td>
										<b>Nome</b>
									</td>
							<%	} else { %>
									<td>
										<%bp.getController().writeFormLabel(out,"ragioneSociale");%>
									</td>
							<% } %>
							<td  colspan="3">
								<%bp.getController().writeFormInput(out,"ragioneSociale");%>
							</td>
						</tr>
				<%	}
					if (fornitore.getAnagrafico().isPersonaFisica()) { %>
						<tr>
							<td>
								<% bp.getController().writeFormLabel(out,"cognome");%>
							</td>
							<td>
								<%bp.getController().writeFormInput(out,"cognome");%>
							</td>
							<td>
								<% bp.getController().writeFormLabel(out,"nome");%>
							</td>
							<td>
								<%bp.getController().writeFormInput(out,"nome");%>
							</td>
						</tr>
				<%	} %>
					<tr>
						<td>
							<% bp.getController().writeFormLabel(out,"denominazione_sede"); %>
						</td>
						<td colspan="3">
							<% bp.getController().writeFormInput(out,"denominazione_sede"); %>
						</td>
					</tr>
				<%	if (!fornitore.getAnagrafico().isStrutturaCNR()) { %>
						<tr>
							<% 	if (fornitore.getAnagrafico().isPersonaGiuridica() || 
									fornitore.getAnagrafico().isDittaIndividuale()) { %>
										<td>
											<% bp.getController().writeFormLabel(out,"partitaIva"); %>
										</td>
										<td>
											<% bp.getController().writeFormInput(out,"partitaIva"); %>
										</td>
							<%	} %>
							<% bp.getController().writeFormField(out,"codiceFiscale"); %>
							<% if (bp.isSearching() || fornitore.getCrudStatus() != it.cnr.jada.bulk.OggettoBulk.NORMAL)
								bp.getController().writeFormField(out,"partitaIva");%>
						</tr>
			<%		} 
				} else { %>
					<tr>
						<td>
							<% bp.getController().writeFormLabel(out,"ragioneSociale");%>
						</td>
						<td colspan="3">
							<% bp.getController().writeFormInput(out,"ragioneSociale");%>
						</td>
					</tr>
					<tr>
						<% bp.getController().writeFormField(out,"nome");%>
						<% bp.getController().writeFormField(out,"cognome");%>
					</tr>
					<tr>
						<% bp.getController().writeFormField(out,"codiceFiscale");%>
						<% bp.getController().writeFormField(out,"partitaIva");%>
					</tr>
			<%	} %>
	      <tr>
			<% bp.getController().writeFormField(out,"via_fiscale");%>
			<% bp.getController().writeFormField(out,"num_civico");%>
	      </tr>
	      <tr>
			<% bp.getController().writeFormField(out,"ds_comune");%>
			<% bp.getController().writeFormField(out,"ds_provincia");%>
	      </tr> 
		</table>
	</div>

   <div class="Group card">   
	<table>	
	  <tr>
     	<td>
 	     	<% bp.getController().writeFormLabel(out,"termini_pagamento");%>
      	</td>      	
     	<td>
	      	<% bp.getController().writeFormInput(out,null,"termini_pagamento",roOnAutoGen,null,"");%>	
      	</td>   
      </tr>
      <tr>
     	<td>
 	     	<% bp.getController().writeFormLabel(out,"modalita_pagamento");%>
      	</td>      	
     	<td>
	      	<% bp.getController().writeFormInput(out,null,"modalita_pagamento",roOnAutoGen,null,"onChange=\"submitForm('doOnModalitaPagamentoChange')\"");%>	
      	</td>   
		<td>
			<% 	if (ordine.getBanca() != null) {
					bp.getController().writeFormInput(out, null, "listabanche", roOnAutoGen, null, "");
				} %>
   		</td>
      </tr>
		<tr>
		  	<td colspan="2">
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
				}
			} else if (ordine.getModalitaPagamento() != null && (fornitore != null && fornitore.getCrudStatus() != fornitore.UNDEFINED)) { %>
				<span class="FormLabel" style="color:red">
					Nessun riferimento trovato per la modalità di pagamento selezionata!
				</span>
		<%	} else if(ordine.getModalitaPagamento() == null && ordine.getModalita()!= null && ordine.getModalita().size() == 0 && (fornitore != null && fornitore.getCrudStatus() != fornitore.UNDEFINED)){ %>
				<span class="FormLabel" style="color:red">
				Attenzione! Nessuna modalità di pagamento trovata!
			</span>
		 <% } %>
			<td>
		<tr>
    </table>
   </div>
