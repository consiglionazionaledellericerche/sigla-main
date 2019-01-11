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
	<legend class="GroupLabel">Richiesta Rimborso Spese</legend>
	<table>
		<tr>
			<td>
				<fieldset class="fieldset">
					<legend class="GroupLabel" style="color: red">Protocollo</legend>
					<table>
						<tr>
							<td>
								<%
									bp.getController().writeFormLabel(out, "numProtRimbSpese");
								 	bp.getController().writeFormInput(out, "numProtRimbSpese");
								 %>
							</td>
							<td>
								<%
									bp.getController().writeFormLabel(out, "dtProtRimbSpese");
								 	bp.getController().writeFormInput(out, "dtProtRimbSpese");
 								%>
							</td>
						</tr>
					</table>
				</fieldset>
			</td>
		</tr>
		<tr>
			<td>
				<fieldset class="fieldset">
					<legend class="GroupLabel" style="color: red">Dati Rimborso Spesa</legend>
					<table>
						<tr>
						<tr>
							<td><%bp.getController().writeFormLabel(out, "dtPagamSaldo");
			 					bp.getController().writeFormInput(out, "dtPagamSaldo");
			 					%>
							</td>
						<tr>
							<td colspan=2>
								<%
									bp.getController().writeFormLabel(out, "imRimbSpese");
								 	bp.getController().writeFormInput(out, "imRimbSpese");
								 %>
							</td>
						</tr>
						<tr>
							<table>
								<tr>
									<td>
										<table>
											<tr>
										     	<td>
										 	     	<% bp.getController().writeFormLabel(out,"modalitaPagamento");%>
										      	</td>      	
										     	<td>
											      	<% bp.getController().writeFormInput(out,null,"modalitaPagamento",false,null,"onChange=\"submitForm('doOnModalitaPagamentoChange')\"");%>	
										      	</td>   
												<td>
													<% 	if (model != null && model.getBanca() != null) {
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
															if(model.getBanca() != null) {
																if (Rif_modalita_pagamentoBulk.BANCARIO.equalsIgnoreCase(model.getBanca().getTi_pagamento())) {
															 	     	bp.getController().writeFormInput(out,"contoB");
																} else if (Rif_modalita_pagamentoBulk.POSTALE.equalsIgnoreCase(model.getBanca().getTi_pagamento())) {
															 	     	bp.getController().writeFormInput(out,"contoP");
																} else if (Rif_modalita_pagamentoBulk.QUIETANZA.equalsIgnoreCase(model.getBanca().getTi_pagamento())) {
															 	     	bp.getController().writeFormInput(out,"contoQ");
																} else if (Rif_modalita_pagamentoBulk.ALTRO.equalsIgnoreCase(model.getBanca().getTi_pagamento())) { 
															 	     	bp.getController().writeFormInput(out,"contoA");
																} else if (Rif_modalita_pagamentoBulk.IBAN.equalsIgnoreCase(model.getBanca().getTi_pagamento())) { 
														 	     	bp.getController().writeFormInput(out,"contoN");
																}else if (Rif_modalita_pagamentoBulk.BANCA_ITALIA.equalsIgnoreCase(model.getBanca().getTi_pagamento()) && model.getBanca().isTABB()) {
                                                                    bp.getController().writeFormInput(out,"contoB");
                                                                }
															} else {
																if ((model.getModalitaPagamentoSaldoOptions()==null || model.getModalitaPagamentoSaldoOptions().isEmpty()) && 
																	 model.getTerzoPagamento().getAnagrafico()!=null && !bp.isSearching()) { %>
																	<span class="FormLabel" style="color:red"> Nessuna modalità di pagamento trovata</span>
													<%			}
															} %>
											</tr>
											<% if (model.getBanca()!=null && model.getCessionario() != null) { %>
													<tr>
														<td>
															<% bp.getController().writeFormLabel(out,"cd_cessionario");%>
														</td>   
														<td>
															<% bp.getController().writeFormInput(out,null,"cd_cessionario",false,null,"");%>						
															<% bp.getController().writeFormInput(out,null,"denom_sede_cessionario",false,null,"");%>	
														</td>   
													</tr>
											<% }
										} %>
									</table>
								</td>
							</tr>
					    </table>
					</tr>
					<% if ((model.isVisitaDipendente() && model.getFase()>=Blt_visiteBulk.FASE_NONA) ||
						   (model.isVisitaUniversitario() && model.getFase()>=Blt_visiteBulk.FASE_TREDICESIMA))  { %>
					<tr>
						<td>
							<fieldset class="fieldset">
							<legend class="GroupLabel" style="color:red">Protocollo Saldo Pagamento</legend>
								<table>
									<tr>
										<td>
											<% bp.getController().writeFormLabel(out,"numProtProvvPagam");%>
											<% bp.getController().writeFormInput(out,"numProtProvvPagam");%>
										</td>				
										<td>
											<% bp.getController().writeFormLabel(out,"dtProtProvvPagam");%>
											<% bp.getController().writeFormInput(out,"dtProtProvvPagam");%>
										</td>				
									</tr>
								</table>
							</fieldset>
						</td>				
					</tr>
					<% } %>		
				</table>
			</fieldset>
			</td>
		</tr>
	</table>
</fieldset>
