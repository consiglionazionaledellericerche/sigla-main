<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
	        it.cnr.jada.util.jsp.*,
	        java.math.BigDecimal,
			it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk,
	        it.cnr.contab.bilaterali00.bulk.Blt_visiteBulk,
	        it.cnr.contab.bilaterali00.bp.CRUDBltVisiteBP"%>
<%
	CRUDBltVisiteBP bp = (CRUDBltVisiteBP) BusinessProcess
			.getBusinessProcess(request);
	Blt_visiteBulk model = (Blt_visiteBulk) bp.getModel();
%>
<% if (!model.isAccordoPagataAdEnteStraniero() && !model.isNotaAddebitoSaldoConAnticipoRequired()){ %>
<fieldset class="fieldset">
	<legend class="GroupLabel">Attestato Soggiorno</legend>
	<table>
		<tr>
			<td>
				<fieldset class="fieldset">
					<legend class="GroupLabel" style="color:red">Protocollo</legend>
					<table>
						<tr>
							<td>
								<% bp.getController().writeFormLabel(out,"numProtAttestatoSogg");%>
								<% bp.getController().writeFormInput(out,"numProtAttestatoSogg");%>
							</td>				
							<td>
								<% bp.getController().writeFormLabel(out,"dtProtAttestatoSogg");%>
								<% bp.getController().writeFormInput(out,"dtProtAttestatoSogg");%>
							</td>				
						</tr>
					</table>
				</fieldset>
			</td>				
		</tr>
		<tr>
			<td>
				<fieldset class="fieldset">
					<legend class="GroupLabel" style="color:red">Dati Viaggio Effettivi</legend>
					<table>
						<tr>
							<td>
								<% bp.getController().writeFormLabel(out,"dtIniVisitaEffettiva");%>
							</td>				
							<td>
								<% bp.getController().writeFormInput(out,"dtIniVisitaEffettiva");%>
							</td>				
							<td>
								<% bp.getController().writeFormLabel(out,"dtFinVisitaEffettiva");%>
								<% bp.getController().writeFormInput(out,"dtFinVisitaEffettiva");%>
							</td>				
						</tr>
					</table>
				</fieldset>
			</td>				
		</tr>
	</table>
</fieldset>
<% } %>

<% if (model.getFlPagamentoFineVisita() && model.getFase()>=Blt_visiteBulk.FASE_SEDICESIMA) { %>
<fieldset class="fieldset">
	<legend class="GroupLabel">Nota di Addebito</legend>
	<table>
		<tr>
			<td>
				<fieldset class="fieldset">
					<legend class="GroupLabel" style="color:red">Protocollo</legend>
					<table>
						<tr>
							<td>
								<% bp.getController().writeFormLabel(out,"numProtNotaAddebito");%>
								<% bp.getController().writeFormInput(out,"numProtNotaAddebito");%>
							</td>				
							<td>
								<% bp.getController().writeFormLabel(out,"dtProtNotaAddebito");%>
								<% bp.getController().writeFormInput(out,"dtProtNotaAddebito");%>
							</td>				
						</tr>
					</table>
				</fieldset>
			</td>				
		</tr>
	</table>
</fieldset>
<% } %>

<% if ((!model.isAccordoPagataAdEnteStraniero() &&  model.isAnticipoPrevisto() &&  model.getFase()>=Blt_visiteBulk.FASE_SEDICESIMA)) { %>
<fieldset class="fieldset">
	<legend class="GroupLabel">Nota di Addebito</legend>
	<table>
		<tr>
			<td>
				<fieldset class="fieldset">
					<legend class="GroupLabel" style="color:red">Protocollo</legend>
					<table>
						<tr>
							<td>
								<% bp.getController().writeFormLabel(out,"numProtNotaAddebito");%>
								<% bp.getController().writeFormInput(out,"numProtNotaAddebito");%>
							</td>				
							<td>
								<% bp.getController().writeFormLabel(out,"dtProtNotaAddebito");%>
								<% bp.getController().writeFormInput(out,"dtProtNotaAddebito");%>
							</td>				
						</tr>
					</table>
				</fieldset>
			</td>				
		</tr>
	</table>
</fieldset>
<% } %>

<% if (!(!model.isAccordoPagataAdEnteStraniero() && !model.isNotaAddebitoSaldoConAnticipoRequired()) ||
		  model.getFase()>=Blt_visiteBulk.FASE_DICIASSETTESIMA || model.isVisitaPagataAdEnteStraniero() && model.getFase()>=Blt_visiteBulk.FASE_NONA) { %>
<fieldset class="fieldset">
	<legend class="GroupLabel">Saldo</legend>
	<table>
			<tr>
			<td colspan=2>
			<%
				bp.getController().writeFormLabel(out, "dtPagamSaldo");
			 	bp.getController().writeFormInput(out, "dtPagamSaldo");
			 %>
			</td>
		</tr>
		<tr>
			<td colspan=2>
			<%
				bp.getController().writeFormLabel(out, "imRimbSpeseRO");
			 	bp.getController().writeFormInput(out, "imRimbSpeseRO");
			 %>
			</td>
		</tr>
		<% if (model.getImRimbSpese().compareTo(BigDecimal.ZERO)==1) { %>
		<tr>
			<td>
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
												} else if (Rif_modalita_pagamentoBulk.BANCA_ITALIA.equalsIgnoreCase(model.getBanca().getTi_pagamento()) && model.getBanca().isTABB()) {
                                                    bp.getController().writeFormInput(out,"contoB");
                                                }
											} else {
												if ((model.getModalitaPagamentoSaldoOptions()==null || model.getModalitaPagamentoSaldoOptions().isEmpty()) && model.getTerzoPagamento().getAnagrafico()!=null && !bp.isSearching()) { %>
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
			</td>
		</tr>
		<% } %>
		
		<% if ((model.getFase()>=Blt_visiteBulk.FASE_DICIOTTESIMA) ||
			   (model.isVisitaPagataAdEnteStraniero() && model.getFase()>=Blt_visiteBulk.FASE_DECIMA) ||
			   (model.isAccordoPagataAdEnteStraniero() && model.getFase()>=Blt_visiteBulk.FASE_SETTIMA)){ %>
		<tr>
			<td>
				<fieldset class="fieldset">
				<% if (model.getImRimbSpese().compareTo(BigDecimal.ZERO)==1) { %>
				<legend class="GroupLabel" style="color:red">Protocollo Saldo Pagamento</legend>
				<% } else { %>
				<legend class="GroupLabel" style="color:red">Protocollo Richiesta Rimborso</legend>
				<% } %>
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
<% } %>
<% if ((!model.isAccordoPagataAdEnteStraniero() && model.isNotaAddebitoSaldoConAnticipoRequired() && model.getFase()>=Blt_visiteBulk.FASE_DICIANNOVESIMA) || 
	    (model.isAccordoPagataAdEnteStraniero() && model.getFase()>=Blt_visiteBulk.FASE_OTTAVA)){ %>
<fieldset class="fieldset">
	<legend class="GroupLabel">Attestato Soggiorno</legend>
	<table>
		<tr>
			<td>
				<fieldset class="fieldset">
					<legend class="GroupLabel" style="color:red">Protocollo</legend>
					<table>
						<tr>
							<td>
								<% bp.getController().writeFormLabel(out,"numProtAttestatoSogg");%>
								<% bp.getController().writeFormInput(out,"numProtAttestatoSogg");%>
							</td>				
							<td>
								<% bp.getController().writeFormLabel(out,"dtProtAttestatoSogg");%>
								<% bp.getController().writeFormInput(out,"dtProtAttestatoSogg");%>
							</td>				
						</tr>
					</table>
				</fieldset>
			</td>				
		</tr>
		<tr>
			<td>
				<fieldset class="fieldset">
					<legend class="GroupLabel" style="color:red">Dati Viaggio Effettivi</legend>
					<table>
						<tr>
							<td>
								<% bp.getController().writeFormLabel(out,"dtIniVisitaEffettiva");%>
							</td>				
							<td>
								<% bp.getController().writeFormInput(out,"dtIniVisitaEffettiva");%>
							</td>				
							<td>
								<% bp.getController().writeFormLabel(out,"dtFinVisitaEffettiva");%>
								<% bp.getController().writeFormInput(out,"dtFinVisitaEffettiva");%>
							</td>				
						</tr>
					</table>
				</fieldset>
			</td>				
		</tr>
	</table>
</fieldset>
<% } %>