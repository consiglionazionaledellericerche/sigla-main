<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.bilaterali00.bulk.*,
		it.cnr.contab.bilaterali00.bp.CRUDBltVisiteBP"%>
<%
  CRUDBltVisiteBP bp = (CRUDBltVisiteBP)BusinessProcess.getBusinessProcess(request);
  Blt_visiteBulk  model = (Blt_visiteBulk)bp.getModel();
  Blt_accordiBulk  accordo = (Blt_accordiBulk)model.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo();
  
  boolean isVisitaDipendente = model.getBltAutorizzatiDett()!=null && model.getBltAutorizzatiDett().getBltAutorizzati()!=null && 
		                       model.isVisitaDipendente() ;
  boolean isVisitaUniversitario = model.getBltAutorizzatiDett()!=null && model.getBltAutorizzatiDett().getBltAutorizzati()!=null && 
          						  model.isVisitaUniversitario();
  boolean isVisitaStraniero = model.getBltAutorizzatiDett()!=null && model.getBltAutorizzatiDett().getBltAutorizzati()!=null && 
          					  model.isVisitaStraniero();
%>
<fieldset class="fieldset">
	<legend class="GroupLabel">Protocollo Candidatura</legend>
		<table>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"numProtCandidatura");%>
					<% bp.getController().writeFormInput(out,"numProtCandidatura");%>
				</td>				
				<td>
					<% bp.getController().writeFormLabel(out,"dtProtCandidatura");%>
					<% bp.getController().writeFormInput(out,"dtProtCandidatura");%>
				</td>				
			</tr>
		</table>
</fieldset>

<fieldset class="fieldset">
	<legend class="GroupLabel">Dati Viaggio</legend>
		<table>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"dtIniVisita");%>
				</td>				
				<td>
					<% bp.getController().writeFormInput(out,"dtIniVisita");%>
				</td>				
				<td>
					<% bp.getController().writeFormLabel(out,"dtFinVisita");%>
					<% bp.getController().writeFormInput(out,"dtFinVisita");%>
				</td>
					<% if (!bp.isSearching() && model!=null && (isVisitaDipendente)) {%>
				<td align="right">
					<% bp.getController().writeFormLabel(out,"flAutorizzDirettore");%>
					<% bp.getController().writeFormInput(out,"flAutorizzDirettore");%>
				</td>
			</tr>						
			
			<% } %>
		
			<% if (model!=null && model.getFlAutorizzDirettore() != null && model.getFlAutorizzDirettore()) {%>
					<tr>
						<td colspan=4>
							<fieldset class="fieldset">
								<legend class="GroupLabel" style="color:red">Protocollo Autorizzazione</legend>
									<table>
										<tr>
											<td>
												<% bp.getController().writeFormLabel(out,"numProtAutorizzDirettore");%>
												<% bp.getController().writeFormInput(out,"numProtAutorizzDirettore");%>
											</td>	
											<td>
												<% bp.getController().writeFormLabel(out,"dtProtAutorizzDirettore");%>
												<% bp.getController().writeFormInput(out,"dtProtAutorizzDirettore");%>
											</td>	
										</tr>
									</table>
							</fieldset>
						</td>
					</tr>
			<% } %>			
			
			
			
			<% if (!bp.isSearching() && model!=null && (isVisitaStraniero && !model.isAccordoPagataAdEnteStraniero())) {%>
					<td align="right">
						<% bp.getController().writeFormLabel(out,"flBrevePeriodo");%>
						<% bp.getController().writeFormInput(out,"flBrevePeriodo");%>
					</td>				
			<% } %>
					
			<% if (!bp.isSearching() && model!=null && (isVisitaStraniero || isVisitaUniversitario)) {%>
				<tr>
					<td>
						<% bp.getController().writeFormLabel(out,"luogoVisita");%>
					</td>				
					<td colspan=2>
						<% bp.getController().writeFormInput(out,"luogoVisita");%>
					</td>
				</tr>	
			
			
			
			
			<% } %>
					
		</tr>						
	</table>
</fieldset>

	<% if (!bp.isSearching() && model!=null && isVisitaStraniero && !model.isAccordoPagataAdEnteStraniero()) {%>
				<fieldset class="fieldset">
					<legend class="GroupLabel">Modalità di pagamento</legend>
						<table>
							<tr>
								<td>
									<% bp.getController().writeFormLabel(out,"flPagamentoFineVisita");%>
									<% bp.getController().writeFormInput(out,"flPagamentoFineVisita");%>
								</td>	
								<td>
									<% bp.getController().writeFormLabel(out,"flPagamentoConBonifico");%>
									<% bp.getController().writeFormInput(out,"flPagamentoConBonifico");%>
								</td>
								<tr>
								<td>
									<% bp.getController().writeFormLabel(out,"flPagamentoEnte");%>
									<% bp.getController().writeFormInput(out,"flPagamentoEnte");%>
								</td>
								</tr>
							</tr>
							<% if (model!=null && model.getFlPagamentoEnte()!= null && model.getFlPagamentoEnte()) {%>
									<tr>
										<td>
											<fieldset class="fieldset">
												<legend class="GroupLabel" style="color:red">Ente / Istituto da pagare</legend>
													<table>
														<tr>
															<td><% bp.getController().writeFormLabel(out,"cdTerzoEnte");%></td>
															<td><% bp.getController().writeFormInput(out,"cdTerzoEnte");%></td>
				    										<td><% bp.getController().writeFormInput(out,"dsTerzoEnte");%></td>
				    										<td><% bp.getController().writeFormInput(out,"findTerzoEnte");%></td>													
														</tr>
													</table>
											</fieldset>
										</td>
									</tr>
								<% } %>
			
		
												
						</table>
				</fieldset>
	<% } %>		
			<% if (!bp.isSearching() && model!=null && isVisitaStraniero && !model.isAccordoPagataAdEnteStraniero()) {%>
				<fieldset class="fieldset">
					<legend class="GroupLabel">Convenzione Fiscale</legend>
						<table>
							<tr>
								<td>
									<% bp.getController().writeFormLabel(out,"flAccettazioneConvenzione");%>
									<% bp.getController().writeFormInput(out,"flAccettazioneConvenzione");%>
								</td>
							</tr>

								<% if (model!=null && model.getFlAccettazioneConvenzione()!= null && model.getFlAccettazioneConvenzione()) {%>
									<tr>
										<td colspan=4>
											<fieldset class="fieldset">
												<legend class="GroupLabel" style="color:red">Protocollo Accettazione Convenzione</legend>
													<table>
														<tr>
															<td>
																<% bp.getController().writeFormLabel(out,"numProtAccettConvenz");%>
																<% bp.getController().writeFormInput(out,"numProtAccettConvenz");%>
															</td>	
															<td>
																<% bp.getController().writeFormLabel(out,"dtProtAccettConvenz");%>
																<% bp.getController().writeFormInput(out,"dtProtAccettConvenz");%>
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
		


			<% if (!bp.isSearching() && model!=null && model.getFlStampatoDocCandidatura()) { %>
				<fieldset class="fieldset">
					<legend class="GroupLabel">Trasmissione Candidatura</legend>
						<table>
							<tr>
								<td>
									<fieldset class="fieldset">
										<legend class="GroupLabel" style="color:red">Protocollo Lettera</legend>
											<table>
												<tr>
													<td>
														<% bp.getController().writeFormLabel(out,"numProtTrasmissCandidatura");%>
														<% bp.getController().writeFormInput(out,"numProtTrasmissCandidatura");%>
													</td>				
													<td>
														<% bp.getController().writeFormLabel(out,"dtProtTrasmissCandidatura");%>
														<% bp.getController().writeFormInput(out,"dtProtTrasmissCandidatura");%>
													</td>				
												</tr>
											</table>
									</fieldset>
								</td>				
								<td>
									<fieldset class="fieldset">
										<legend class="GroupLabel" style="color:red">Protocollo Accettazione</legend>
											<table>
												<tr>
													<td>
														<% bp.getController().writeFormLabel(out,"numProtAccettEnteStr");%>
														<% bp.getController().writeFormInput(out,"numProtAccettEnteStr");%>
													</td>				
													<td>
														<% bp.getController().writeFormLabel(out,"dtProtAccettEnteStr");%>
														<% bp.getController().writeFormInput(out,"dtProtAccettEnteStr");%>
													</td>				
												</tr>
											</table>
									</fieldset>
								</td>				
							</tr>
						</table>
			</fieldset>
			<% if (isVisitaDipendente || isVisitaUniversitario) {%>
				<fieldset class="fieldset">
					<legend class="GroupLabel">Disposizioni Finanziarie</legend>
						<table>
							<tr>
								<td>
									<fieldset class="fieldset">
										<legend class="GroupLabel" style="color:red">Protocollo Lettera</legend>
											<table>
												<tr>
													<td>
														<% bp.getController().writeFormLabel(out,"numProtDispFin");%>
														<% bp.getController().writeFormInput(out,"numProtDispFin");%>
													</td>				
													<td>
														<% bp.getController().writeFormLabel(out,"dtProtDispFin");%>
														<% bp.getController().writeFormInput(out,"dtProtDispFin");%>
													</td>				
												</tr>
											</table>
								</fieldset>
							</td>				
					</tr>
				</table>
			</fieldset>
		<% } %>
		<% if ((isVisitaDipendente && model.getFase()>=Blt_visiteBulk.FASE_QUARTA) ||
			   ((isVisitaStraniero && model.isAccordoPagataAdEnteStraniero()) && model.getFase()>=Blt_visiteBulk.FASE_QUARTA) || 
			   ((isVisitaStraniero && model.isVisitaPagataAdEnteStraniero()) && model.getFase()>=Blt_visiteBulk.FASE_QUARTA)) { %>
			<fieldset class="fieldset">
				<legend class="GroupLabel">Autorizzazione alla Partenza/Arrivo</legend>
				<table>
					<tr>
						<td>
							<fieldset class="fieldset">
								<legend class="GroupLabel" style="color:red">Protocollo</legend>
								<table>
									<tr>
										<td>
											<% bp.getController().writeFormLabel(out,"numProtAutorizPartenza");%>
											<% bp.getController().writeFormInput(out,"numProtAutorizPartenza");%>
										</td>				
										<td>
											<% bp.getController().writeFormLabel(out,"dtProtAutorizPartenza");%>
											<% bp.getController().writeFormInput(out,"dtProtAutorizPartenza");%>
										</td>				
									</tr>
								</table>
							</fieldset>
						</td>				
					</tr>
				</table>
			</fieldset>
		<% } %>	
		<% if ((isVisitaStraniero && !model.isAccordoPagataAdEnteStraniero()) && (isVisitaStraniero && !model.isVisitaPagataAdEnteStraniero()) ||  isVisitaUniversitario) {%>
			<fieldset class="fieldset">
				<legend class="GroupLabel">Attribuzione Incarico</legend>
				<table>
					<tr>
						<td>
							<fieldset class="fieldset">
								<legend class="GroupLabel" style="color:red">Protocollo Provvedimento</legend>
								<table>
									<tr>
										<td>
											<% bp.getController().writeFormLabel(out,"numProtAttribIncarico");%>
											<% bp.getController().writeFormInput(out,"numProtAttribIncarico");%>
										</td>				
										<td>
											<% bp.getController().writeFormLabel(out,"dtProtAttribIncarico");%>
											<% bp.getController().writeFormInput(out,"dtProtAttribIncarico");%>
										</td>				
									</tr>
								</table>
							</fieldset>
						</td>				
					</tr>
				</table>
			</fieldset>
		<% } %>
<% } %>