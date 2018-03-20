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
	<legend class="GroupLabel">Dati Annullamento Visita</legend>
	<table>
		<tr>
			<td>
				<fieldset class="fieldset">
					<legend class="GroupLabel" style="color: red">Protocollo Rinuncia Visita</legend>
					<table>
						<tr>
							<td>
								<%
									bp.getController().writeFormLabel(out, "numProtRinunciaVisita");
								 	bp.getController().writeFormInput(out, "numProtRinunciaVisita");
								 %>
							</td>
							<td>
								<%
									bp.getController().writeFormLabel(out, "dtProtRinunciaVisita");
								 	bp.getController().writeFormInput(out, "dtProtRinunciaVisita");
 								%>
							</td>
						</tr>
					</table>
				</fieldset>
			</td>
		</tr>
		<% if (!bp.isSearching() && model!=null && (model.getFlStampatoAnnProvvImpegno()||model.getNumProtAnnullaProvv()!=null||model.getDtProtAnnullaProvv()!=null)) {%>
		<tr>
			<td>
				<fieldset class="fieldset">
					<legend class="GroupLabel" style="color: red">Protocollo Annullamento Provvedimento Impegno</legend>
					<table>
						<tr>
							<td>
								<%
									bp.getController().writeFormLabel(out, "numProtAnnullaProvv");
								 	bp.getController().writeFormInput(out, "numProtAnnullaProvv");
								 %>
							</td>
							<td>
								<%
									bp.getController().writeFormLabel(out, "dtProtAnnullaProvv");
								 	bp.getController().writeFormInput(out, "dtProtAnnullaProvv");
 								%>
							</td>
						</tr>
					</table>
				</fieldset>
			</td>
		</tr>
		<% } %>
	</table>
</fieldset>
