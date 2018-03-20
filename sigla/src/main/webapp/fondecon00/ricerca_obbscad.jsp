<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.fondecon00.core.bulk.*,
		it.cnr.contab.fondecon00.bp.*"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title>Ricerca impegni per contabilizzazione</title>

</head>
<body class="Form">
<%	RicercaObbScadBP bp = (RicercaObbScadBP)BusinessProcess.getBusinessProcess(request);
	Filtro_ricerca_obbligazioniVBulk filtro = (Filtro_ricerca_obbligazioniVBulk)bp.getModel();
	it.cnr.contab.anagraf00.core.bulk.TerzoBulk fornitore = filtro.getFornitore();

	bp.openFormWindow(pageContext); %>

	<div class="Group">
		<table>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"fl_fornitore");%>
				</td>      	
				<td>
					<% bp.getController().writeFormInput(out,null,"fl_fornitore",false,null,"onClick=\"submitForm('doOnFlFornitoreChange')\"");%>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"cd_fornitore");%>
				</td>
				<td colspan="3">
					<% bp.getController().writeFormInput(out,"fornitore");%>
				</td>
			</tr>

			<%	if (fornitore != null && fornitore.getAnagrafico() != null) {
					if ((fornitore.getAnagrafico().isStrutturaCNR() ||
						fornitore.getAnagrafico().isPersonaGiuridica() ||
						fornitore.getAnagrafico().isDittaIndividuale()) &&
						fornitore.getAnagrafico().getRagione_sociale() != null &&
						fornitore.getAnagrafico().getRagione_sociale().length() > 0) { %>
						<tr>
							<%	if (fornitore.getAnagrafico().isStrutturaCNR()) { %>
									<td>
										<b>Nome</b>
									</td>
							<%	} else { %>
									<td>
										<%bp.getController().writeFormLabel(out,"ragione_sociale");%>
									</td>
							<% } %>
							<td  colspan="3">
								<%bp.getController().writeFormInput(out,"ragione_sociale");%>
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
											<% bp.getController().writeFormLabel(out,"partita_iva"); %>
										</td>
										<td>
											<% bp.getController().writeFormInput(out,"partita_iva"); %>
										</td>
							<%	} %>
							<% bp.getController().writeFormField(out,"codice_fiscale"); %>
						</tr>
			<%		} 
				} else { %>
					<tr>
						<td>
							<% bp.getController().writeFormLabel(out,"ragione_sociale");%>
						</td>
						<td colspan="3">
							<% bp.getController().writeFormInput(out,"ragione_sociale");%>
						</td>
					</tr>
					<tr>
						<% bp.getController().writeFormField(out,"nome");%>
						<% bp.getController().writeFormField(out,"cognome");%>
					</tr>
					<tr>
						<% bp.getController().writeFormField(out,"codice_fiscale");%>
						<% bp.getController().writeFormField(out,"partita_iva");%>
					</tr>
			<%	} %>
		</table>	
	</div>
	<div class="Group">
		<table>	
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"fl_data_scadenziario");%>
				</td>      	
				<td>
					<% bp.getController().writeFormInput(out,null,"fl_data_scadenziario",false,null,"onClick=\"submitForm('doOnFlDataScadenziarioChange')\"");%>
				</td>
			</tr>
			<% 	bp.getController().writeFormField(out,"data_scadenziario"); %>
		</table>
	</div>
	<div class="Group">
		<table>	
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"fl_importo");%>
				</td>      	
				<td>
					<% bp.getController().writeFormInput(out,null,"fl_importo",false,null,"onClick=\"submitForm('doOnFlImportoChange')\"");%>
				</td>
			</tr>
			<% 	bp.getController().writeFormField(out,"im_importo"); %>
		</table>
	</div>
	<div class="Group">
		<table>	
			<tr>
				<% bp.getController().writeFormField(out,"fl_associate"); %>
			</tr>
		</table>
	</div>
	<div class="Group">
		<table>	
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"fl_nr_obbligazione");%>
				</td>      	
				<td>
					<% bp.getController().writeFormInput(out,null,"fl_nr_obbligazione",false,null,"onClick=\"submitForm('doOnFlNrObbligazioneChange')\"");%>
				</td>
			</tr>
			<% 	bp.getController().writeFormField(out,"tipo_obbligazione"); %>
			<% 	bp.getController().writeFormField(out,"nr_obbligazione"); %>
			<% 	bp.getController().writeFormField(out,"nr_scadenza"); %>
		</table>
	</div>

	<% bp.closeFormWindow(pageContext); %>
</body>