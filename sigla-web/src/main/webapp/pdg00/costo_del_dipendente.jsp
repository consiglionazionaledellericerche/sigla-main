<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.pdg00.cdip.bulk.*,
		it.cnr.contab.pdg00.bp.*"
%>

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<%	CRUDCostoDelDipendenteBP bp = (CRUDCostoDelDipendenteBP)BusinessProcess.getBusinessProcess(request); %>
<%	if (bp.isMensile()) { %>
<title>Costi stipendiali mensili</title>
<%	} else { %>
<title>Costi del personale</title>
<%	} %>
</head>
<body class="Form">

<%	bp.openFormWindow(pageContext); %>

<div class="Group card">
<table class="Panel w-75">
	<tr>
		<td>
			<fieldset>
				<legend class="GroupLabel">Informazioni anagrafiche</legend>
				<table width="100%">
					<% if (bp.isMensile()) { %>
					<tr>
						<% bp.getController().writeFormField(out,"mese");%>
					</tr>
					<% } %>
					<tr>
						<% bp.getController().writeFormField(out,"unita_organizzativa");%>
					</tr>
					<tr>
						<% bp.getController().writeFormField(out,"id_matricola");%>
					</tr>
					<tr>
						<% bp.getController().writeFormField(out,"nominativo");%>
					</tr>
					<tr>
						<% bp.getController().writeFormField(out,"dt_scad_contratto");%>
					</tr>
					<tr>
						<td>
							<span class="FormLabel">Codici livelli</span>
						</td>
						<td>
							<% bp.getController().writeFormInput(out,"cd_livello_1");%>
							<% bp.getController().writeFormInput(out,"cd_livello_2");%>
							<% bp.getController().writeFormInput(out,"cd_livello_3");%>
						</td>
					</tr>
					<tr>
						<td>
							<span class="FormLabel">Profilo</span>
						</td>
						<td>
							<% bp.getController().writeFormInput(out,"cd_profilo");%>
							<% bp.getController().writeFormInput(out,"ds_profilo");%>
						</td>
					</tr>
					<tr>
						<% bp.getController().writeFormField(out,"origine_fonti");%>
					</tr>
				</table>
			</fieldset>
		</td>
	</tr>
	<tr>
		<td>
			<fieldset>
				<legend class="GroupLabel">Importi per capitoli</legend>
				<table width="100%">
					<tr>
						<td colspan="4">
							<% bp.getCosti_per_elemento_voce().writeHTMLTable(
								pageContext,
								null,
								false,
								false,
								false,
								"100%",
								"100px"); %>
						</td>
					</tr>
					<tr>
						<td></td>
						<td><span class="FormLabel">Anno 1</span></td>
						<td><span class="FormLabel">Anno 2</span></td>
						<td><span class="FormLabel">Anno 3</span></td>
					</tr>
					<tr>
						<td><span class="FormLabel">Importo</span></td>
						<td><% bp.getCosti_per_elemento_voce().writeFormInput(out,"im_a1"); %></td>
						<td><% bp.getCosti_per_elemento_voce().writeFormInput(out,"im_a2"); %></td>
						<td><% bp.getCosti_per_elemento_voce().writeFormInput(out,"im_a3"); %></td>
					</tr>
					<tr>
						<td><span class="FormLabel">Importo oneri CNR</span></td>
						<td><% bp.getCosti_per_elemento_voce().writeFormInput(out,"im_oneri_cnr_a1"); %></td>
						<td><% bp.getCosti_per_elemento_voce().writeFormInput(out,"im_oneri_cnr_a2"); %></td>
						<td><% bp.getCosti_per_elemento_voce().writeFormInput(out,"im_oneri_cnr_a3"); %></td>
					</tr>
					<tr>
						<td><span class="FormLabel">Importo TFR</span></td>
						<td><% bp.getCosti_per_elemento_voce().writeFormInput(out,"im_tfr_a1"); %></td>
						<td><% bp.getCosti_per_elemento_voce().writeFormInput(out,"im_tfr_a2"); %></td>
						<td><% bp.getCosti_per_elemento_voce().writeFormInput(out,"im_tfr_a3"); %></td>
					</tr>
				</table>
			</fieldset>
		</td>
	</tr>
</table>
</div>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>