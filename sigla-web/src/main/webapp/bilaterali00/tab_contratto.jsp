<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.jsp.*,
		java.math.BigDecimal,
		it.cnr.contab.bilaterali00.bulk.Blt_visiteBulk,
		it.cnr.contab.bilaterali00.bp.CRUDBltVisiteBP"%>
<%
  CRUDBltVisiteBP bp = (CRUDBltVisiteBP)BusinessProcess.getBusinessProcess(request);
  Blt_visiteBulk  model = (Blt_visiteBulk)bp.getModel();
%>
	<fieldset class="fieldset">
		<legend class="GroupLabel">Contratto</legend>
		<table>
			<tr>
				<td>
					<fieldset class="fieldset">
						<legend class="GroupLabel" style="color:red">Protocollo</legend>
						<table>
							<tr>
								<td>
									<% bp.getController().writeFormLabel(out,"numProtContratto");%>
									<% bp.getController().writeFormInput(out,"numProtContratto");%>
								</td>				
								<td>
									<% bp.getController().writeFormLabel(out,"dataProtContratto");%>
									<% bp.getController().writeFormInput(out,"dataProtContratto");%>
								</td>				
							</tr>
						</table>
					</fieldset>
				</td>				
			</tr>
		</table>
	</fieldset>
<% if (model.isVisitaStraniero() && (model.isNotaAddebitoAnticipoRequired() || model.isNotaAddebitoSaldoConAnticipoRequired())) {%>
	<fieldset class="fieldset">
		<legend class="GroupLabel">Nota Addebito</legend>
		<table>
			<tr>
				<% if (model.isNotaAddebitoAnticipoRequired()) {%>
				<td>
					<fieldset class="fieldset">
						<legend class="GroupLabel" style="color:red">Protocollo Anticipo</legend>
						<table>
							<tr>
								<td>
									<% bp.getController().writeFormLabel(out,"numProtNotaAddebitoAnt");%>
									<% bp.getController().writeFormInput(out,"numProtNotaAddebitoAnt");%>
								</td>				
								<td>
									<% bp.getController().writeFormLabel(out,"dtProtNotaAddebitoAnt");%>
									<% bp.getController().writeFormInput(out,"dtProtNotaAddebitoAnt");%>
								</td>				
							</tr>
						</table>
					</fieldset>
				</td>				
		<% } %>
		<% if (!model.isNotaAddebitoAnticipoRequired()) {%>
				<td>
					<fieldset class="fieldset">
						<legend class="GroupLabel" style="color:red">Protocollo Saldo</legend>
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
		<% } %>
			</tr>
		</table>
	</fieldset>
<% } %>
	<% if (model.getFase()>=Blt_visiteBulk.FASE_SESTA) { %>
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