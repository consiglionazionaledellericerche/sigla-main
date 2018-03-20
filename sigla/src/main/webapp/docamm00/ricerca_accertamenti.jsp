<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.docamm00.tabrif.bulk.*,
		it.cnr.contab.docamm00.docs.bulk.*,
		it.cnr.contab.docamm00.bp.*"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title>Ricerca accertamenti per contabilizzazione</title>

</head>
<body class="Form">
<%	RicercaAccertamentiBP bp = (RicercaAccertamentiBP)BusinessProcess.getBusinessProcess(request);
	Filtro_ricerca_accertamentiVBulk filtro = (Filtro_ricerca_accertamentiVBulk)bp.getModel();
	it.cnr.contab.anagraf00.core.bulk.TerzoBulk cliente = filtro.getCliente();

	bp.openFormWindow(pageContext); %>
<%if (cliente!=null){%>
	<div class="Group Panel card border-primary p-3 mb-2">
		<table>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"fl_cliente");%>
				</td>      	
				<td>
					<% bp.getController().writeFormInput(out,null,"fl_cliente",false,null,"onClick=\"submitForm('doOnFlClienteChange')\"");%>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"cd_cliente");%>
				</td>
				<td colspan="3">
					<% bp.getController().writeFormInput(out,"cd_cliente");%>
					
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"cd_precedente");%>
				</td>
				<td colspan="3">
					<% bp.getController().writeFormInput(out, "cd_precedente");%>
				</td>
			</tr>

			<%	if (cliente != null && cliente.getAnagrafico() != null) {
					if ((cliente.getAnagrafico().isStrutturaCNR() ||
						cliente.getAnagrafico().isPersonaGiuridica() ||
						cliente.getAnagrafico().isDittaIndividuale()) &&
						cliente.getAnagrafico().getRagione_sociale() != null &&
						cliente.getAnagrafico().getRagione_sociale().length() > 0) { %>
						<tr>
							<%	if (cliente.getAnagrafico().isStrutturaCNR()) { %>
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
					if (cliente.getAnagrafico().isPersonaFisica()) { %>
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
				<%	if (!cliente.getAnagrafico().isStrutturaCNR()) { %>
						<tr>
							<% 	if (cliente.getAnagrafico().isPersonaGiuridica() || 
									cliente.getAnagrafico().isDittaIndividuale()) { %>
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
<%}%>
	<div class="Group Panel card border-primary p-3 mb-2">
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
	<div class="Group Panel card border-primary p-3 mb-2">
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
	<div class="Group Panel card border-primary p-3 mb-2">
		<table>	
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"fl_nr_accertamento");%>
				</td>      	
				<td>
					<% bp.getController().writeFormInput(out,null,"fl_nr_accertamento",false,null,"onClick=\"submitForm('doOnFlNrAccertamentoChange')\"");%>
				</td>
			</tr>
			<% 	bp.getController().writeFormField(out,"nr_accertamento"); %>
		</table>
	</div>

	<% bp.closeFormWindow(pageContext); %>
</body>