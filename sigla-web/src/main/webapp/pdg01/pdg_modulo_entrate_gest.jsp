<head>
<title>Pdg Gestionale - Entrate</title>

<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<%@ page 
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.pdg01.bp.*,
		it.cnr.contab.pdg01.bulk.*"
%>
<body class="Form">
<%
	CRUDPdgModuloEntrateGestBP bp = (CRUDPdgModuloEntrateGestBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
	boolean isDettaglioGestionaleEnable = !bp.isDettaglioGestionaleEnable((Pdg_modulo_entrate_gestBulk)bp.getCrudDettagliGestionali().getModel());
%>
<table>
	<tr>
		<td><% bp.getController().writeFormLabel(out,"cd_centro_responsabilita"); %></td>
	 	<td colspan=7>
	 		<% bp.getController().writeFormInput(out,"cd_centro_responsabilita"); %>
			<% bp.getController().writeFormInput(out,"ds_centro_responsabilita"); %>
		</td>	
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel(out,"modulo"); %></td>
		<td><% bp.getController().writeFormInput(out,"modulo"); %></td>
		<td><% bp.getController().writeFormLabel(out,"commessa"); %></td>
		<td><% bp.getController().writeFormInput(out,"commessa"); %></td>
		<td><% bp.getController().writeFormLabel(out,"progetto"); %></td>
		<td><% bp.getController().writeFormInput(out,"progetto"); %></td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel(out,"dipartimento"); %></td>
		<td><% bp.getController().writeFormInput(out,"dipartimento"); %></td>
		<td><% bp.getController().writeFormLabel(out,"classificazione"); %></td>
		<td><% bp.getController().writeFormInput(out,"classificazione"); %></td>
		<td><% bp.getController().writeFormLabel(out,"desctool_area"); %></td>
		<td><% bp.getController().writeFormInput(out,"desctool_area"); %></td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel(out,"previsione_anno_corrente"); %></td>
		<td><% bp.getController().writeFormInput(out,"previsione_anno_corrente"); %></td>
		<td><% bp.getController().writeFormLabel(out,"dettagli_gestionali_tot"); %></td>
		<td><% bp.getController().writeFormInput(out,"dettagli_gestionali_tot"); %></td>
		<td><% bp.getController().writeFormLabel(out,"dettagli_gestionali_res"); %></td>
		<td><% bp.getController().writeFormInput(out,"dettagli_gestionali_res"); %></td>
	</tr>
</table>
<table>
	  <tr>
	  	<td colspan = "4">
		  <% bp.getCrudDettagliGestionali().writeHTMLTable(
				pageContext,
				"insertGestionale",	
				true,
				true,
				!isDettaglioGestionaleEnable,
				"900px",
				"150px",
				true); %>
		</td>
	  </tr>
</table>
<table>
	<tr>
		<td><% bp.getCrudDettagliGestionali().writeFormLabel(out,"find_cdr_assegnatario");%></td>
		<td colspan=3><% bp.getCrudDettagliGestionali().writeFormInput(out,null,"find_cdr_assegnatario",isDettaglioGestionaleEnable,null,null);%></td>
	</tr>
	<tr>
		<td><% bp.getCrudDettagliGestionali().writeFormLabel(out,"find_linea_attivita");%></td>
		<td colspan=3><% bp.getCrudDettagliGestionali().writeFormInput(out,null,"find_linea_attivita",isDettaglioGestionaleEnable,null,null);%></td>
	</tr>
	<tr>
		<td><% bp.getCrudDettagliGestionali().writeFormLabel(out,"find_elemento_voce");%></td>
		<td colspan=3><% bp.getCrudDettagliGestionali().writeFormInput(out,null,"find_elemento_voce",isDettaglioGestionaleEnable,null,null);%></td>
	</tr>
	<tr>
		<td><% bp.getCrudDettagliGestionali().writeFormLabel(out,"im_entrata");%></td>
		<td><% bp.getCrudDettagliGestionali().writeFormInput(out,null,"im_entrata",isDettaglioGestionaleEnable,null,null);%></td>
		<td><% bp.getCrudDettagliGestionali().writeFormLabel(out,"im_incassi");%></td>
		<td><% bp.getCrudDettagliGestionali().writeFormInput(out,null,"im_incassi",isDettaglioGestionaleEnable,null,null);%></td>
	</tr>
	<tr>
		<td><% bp.getCrudDettagliGestionali().writeFormLabel(out,"descrizione");%></td>
		<td colspan=3><% bp.getCrudDettagliGestionali().writeFormInput(out,null,"descrizione",isDettaglioGestionaleEnable,null,null);%></td>
	</tr>
</table>
<%	bp.closeFormWindow(pageContext); %>
</body>