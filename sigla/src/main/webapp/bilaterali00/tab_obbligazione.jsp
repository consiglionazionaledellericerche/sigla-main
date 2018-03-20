<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.bilaterali00.bulk.Blt_visiteBulk,
		it.cnr.contab.bilaterali00.bp.CRUDBltVisiteBP"%>
<%
  CRUDBltVisiteBP bp = (CRUDBltVisiteBP)BusinessProcess.getBusinessProcess(request);
  Blt_visiteBulk  model = (Blt_visiteBulk)bp.getModel();
%>
	<% if (model.getIncaricoRepertorio()!=null && model.getIncaricoRepertorio().getPg_repertorio()!=null) { %>
	<fieldset class="fieldset">
		<legend class="GroupLabel">Incarico</legend>
		<table>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"incaricoRepertorio");%>
					<% bp.getController().writeFormInput(out,"esercizioRepertorio");%>
					<% bp.getController().writeFormInput(out,"pgRepertorio");%>
				</td>
				<td>
				    <% bp.getController().writeFormInput(out,null,"statoIncarico",true,"GroupLabel","style=\"background: #F5F5DC;background-color:transparent;border-style:none;cursor:default;font-size:16px;font-style:italic;\"");%>
				</td>
			</tr>
		</table>
	</fieldset>
	<% } %>	

	<% if (model.isVisitaDipendente() || 
			(model.isVisitaUniversitario() && model.getFase()>=Blt_visiteBulk.FASE_NONA) || 
			(model.isVisitaStraniero() && model.isAccordoPagataAdEnteStraniero()) ||
			(model.isVisitaStraniero() && model.isVisitaPagataAdEnteStraniero()) ||
			(model.getObbligazioneScadenzario()!=null && model.getObbligazioneScadenzario().getPg_obbligazione()!=null)) { %>
	<fieldset class="fieldset">
		<legend class="GroupLabel">Impegno</legend>
		<table>
			<% if (model!=null && (model.getObbligazioneScadenzario()==null || model.getObbligazioneScadenzario().getPg_obbligazione()==null)) { %>
			<tr>
				<td>
					<% bp.getController().writeFormField(out,"tipo_obbligazione");%>
				</td>
			</tr>
			<%} else {%>
				<tr>
					<td>
						<% bp.getController().writeFormLabel(out,"pgObbligazione");%>
						<% bp.getController().writeFormInput(out,"esercizioOriObblig");%>
						<% bp.getController().writeFormInput(out,"pgObbligazione");%>
					</td>
					<td>
						<% bp.getController().writeFormLabel(out,"esercizioObblig");%>
						<% bp.getController().writeFormInput(out,"esercizioObblig");%>
					</td>									
				</tr>
			<%}%>
			<tr>
			</tr>
		</table>
	</fieldset>
	<% } %>	
	<% if (!bp.isSearching() && model!=null && (model.getFlStampatoProvvImpegno()||model.getNumProtProvvImpegno()!=null||model.getDtProtProvvImpegno()!=null)) {%>
		<fieldset class="fieldset">
			<legend class="GroupLabel">Provvedimento Impegno</legend>
			<table>
				<tr>
					<td>
						<fieldset class="fieldset">
							<legend class="GroupLabel" style="color:red">Protocollo</legend>
							<table>
								<tr>
									<td>
										<% bp.getController().writeFormLabel(out,"numProtProvvImpegno");%>
										<% bp.getController().writeFormInput(out,"numProtProvvImpegno");%>
									</td>				
									<td>
										<% bp.getController().writeFormLabel(out,"dtProtProvvImpegno");%>
										<% bp.getController().writeFormInput(out,"dtProtProvvImpegno");%>
									</td>				
								</tr>
							</table>
						</fieldset>
					</td>				
				</tr>
			</table>
		</fieldset>
	<% } %>