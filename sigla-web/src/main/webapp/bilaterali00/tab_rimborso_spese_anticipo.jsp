<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
	        it.cnr.jada.util.jsp.*,
	        it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk,
	        it.cnr.contab.bilaterali00.bulk.Blt_visiteBulk,
	        it.cnr.contab.bilaterali00.bp.CRUDBltVisiteBP"%>
<%
	CRUDBltVisiteBP bp = (CRUDBltVisiteBP) BusinessProcess
			.getBusinessProcess(request);
	Blt_visiteBulk model = (Blt_visiteBulk) bp.getModel();
%>
<fieldset class="fieldset">
	<legend class="GroupLabel">Anticipo di Pagamento</legend>
	<table>
			<tr>
			<td>
			<%
				bp.getController().writeFormLabel(out, "dtPagamAnt");
			 	bp.getController().writeFormInput(out, "dtPagamAnt");
			 %>
			</td>
		
		<tr>
			<td>
			<%
				bp.getController().writeFormLabel(out, "imRimbSpeseAntRO");
			 	bp.getController().writeFormInput(out, "imRimbSpeseAntRO");
			 %>
			</td>
		</tr>
		<tr>
			<td>
			<table>
				<tr>
					<td>
						<table>
							<tr>
						     	<td>
						 	     	<% bp.getController().writeFormLabel(out,"modalitaPagamentoAnticipo");%>
						      	</td>      	
						     	<td>
							      	<% bp.getController().writeFormInput(out,null,"modalitaPagamentoAnticipo",false,null,"onChange=\"submitForm('doOnModalitaPagamentoAnticipoChange')\"");%>	
						      	</td>   
								<td>
									<% 	if (model != null && model.getBancaAnticipo() != null) {
											bp.getController().writeFormInput(out,"listabanche");
										} %>
						   		</td>
							</tr>
						</table>
					</td>
					<td>
						<table>
							<tr>
							  	<td colspan="2">
									<%	if (model != null && model.getTerzoPagamento()!=null){
											if(model.getBancaAnticipo() != null) {
												if (Rif_modalita_pagamentoBulk.BANCARIO.equalsIgnoreCase(model.getBancaAnticipo().getTi_pagamento())) {
										 	     	bp.getController().writeFormInput(out,"contoBAnt");
												} else if (Rif_modalita_pagamentoBulk.POSTALE.equalsIgnoreCase(model.getBancaAnticipo().getTi_pagamento())) {
										 	     	bp.getController().writeFormInput(out,"contoPAnt");
												} else if (Rif_modalita_pagamentoBulk.QUIETANZA.equalsIgnoreCase(model.getBancaAnticipo().getTi_pagamento())) {
										 	     	bp.getController().writeFormInput(out,"contoQAnt");
												} else if (Rif_modalita_pagamentoBulk.ALTRO.equalsIgnoreCase(model.getBancaAnticipo().getTi_pagamento())) { 
										 	     	bp.getController().writeFormInput(out,"contoAAnt");
												} else if (Rif_modalita_pagamentoBulk.IBAN.equalsIgnoreCase(model.getBancaAnticipo().getTi_pagamento())) { 
										 	     	bp.getController().writeFormInput(out,"contoNAnt");
												} else if (Rif_modalita_pagamentoBulk.BANCA_ITALIA.equalsIgnoreCase(model.getBancaAnticipo().getTi_pagamento()) && model.getBancaAnticipo().isTABB()) {
                                                    bp.getController().writeFormInput(out,"contoBAnt");
                                                }
											} else {
												if ((model.getModalitaPagamentoAnticipoOptions()==null || model.getModalitaPagamentoAnticipoOptions().isEmpty()) && model.getTerzoPagamento().getAnagrafico()!=null && !bp.isSearching()) { %>
													<span class="FormLabel" style="color:red"> Nessuna modalità di pagamento trovata</span>
								<%			}
										} %>
							</tr>
							<% if (model.getBancaAnticipo()!=null && model.getCessionarioAnticipo() != null) { %>
							<tr>
								<td>
									<% bp.getController().writeFormLabel(out,"cd_cessionario_ant");%>
								</td>   
								<td>
									<% bp.getController().writeFormInput(out,null,"cd_cessionario_ant",false,null,"");%>						
									<% bp.getController().writeFormInput(out,null,"denom_sede_cessionario_ant",false,null,"");%>	
								</td>   
							</tr>
						<% }
						} %>
						</table>
					</td>
				</tr>
			</table>
			</td>
		</tr>
		<% if (model.getFase()>=Blt_visiteBulk.FASE_TREDICESIMA) { %>
		<tr>
			<td>
				<fieldset class="fieldset">
				<legend class="GroupLabel" style="color:red">Protocollo Anticipo Pagamento</legend>
					<table>
						<tr>
							<td>
								<% bp.getController().writeFormLabel(out,"numProtProvvPagamAnt");%>
								<% bp.getController().writeFormInput(out,"numProtProvvPagamAnt");%>
							</td>				
							<td>
								<% bp.getController().writeFormLabel(out,"dtProtProvvPagamAnt");%>
								<% bp.getController().writeFormInput(out,"dtProtProvvPagamAnt");%>
							</td>				
						</tr>
					</table>
				</fieldset>
			</td>				
		</tr>
		<% } %>		
	</table>
</fieldset>