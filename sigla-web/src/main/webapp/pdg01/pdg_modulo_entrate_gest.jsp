<%@page import="it.cnr.contab.prevent01.bulk.Pdg_Modulo_EntrateBulk"%>
<head>
<title>Pdg Gestionale - Entrate</title>

<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<%@ page pageEncoding="UTF-8"
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
	Pdg_Modulo_EntrateBulk moduloEntrate = (Pdg_Modulo_EntrateBulk)bp.getModel();
	bp.openFormWindow(pageContext);
	boolean isDettaglioGestionaleEnable = !bp.isDettaglioGestionaleEnable((Pdg_modulo_entrate_gestBulk)bp.getCrudDettagliGestionali().getModel());
%>
<div class="Group card">
<table class="w-100">
	<tr>
		<td><% bp.getController().writeFormLabel(out,"cd_centro_responsabilita"); %></td>
	 	<td colspan=7>
	        <div class="input-group input-group-searchtool w-100">
		 		<% bp.getController().writeFormInput(out,"cd_centro_responsabilita"); %>
				<% bp.getController().writeFormInput(out,"ds_centro_responsabilita"); %>
			</div>
		</td>	
	</tr>
	<tr>
		<% if (!bp.isFlNuovoPdg()) { %>
			<td><% bp.getController().writeFormLabel(out,"modulo"); %></td>
			<td><% bp.getController().writeFormInput(out,"modulo"); %></td>
			<td><% bp.getController().writeFormLabel(out,"commessa"); %></td>
			<td><% bp.getController().writeFormInput(out,"commessa"); %></td>
			<td><% bp.getController().writeFormLabel(out,"progetto"); %></td>
			<td><% bp.getController().writeFormInput(out,"progetto"); %></td>
		<% } else { %>
			<td><% bp.getController().writeFormLabel(out,"cd_progetto_liv2"); %></td>
			<td><% bp.getController().writeFormInput(out,"cd_progetto_liv2"); %></td>
			<td><% bp.getController().writeFormLabel(out,"cd_progetto_liv1"); %></td>
			<td><% bp.getController().writeFormInput(out,"cd_progetto_liv1"); %></td>
			<td><% bp.getController().writeFormLabel(out,"dipartimento_liv2"); %></td>
			<td><% bp.getController().writeFormInput(out,"dipartimento_liv2"); %></td>
		<% } %>		
	</tr>
	<tr>
		<% if (!bp.isFlNuovoPdg()) { %>
			<td><% bp.getController().writeFormLabel(out,"dipartimento"); %></td>
			<td><% bp.getController().writeFormInput(out,"dipartimento"); %></td>
		<% } %>		
		<td><% bp.getController().writeFormLabel(out,"classificazione"); %></td>
		<td><% bp.getController().writeFormInput(out,"classificazione"); %></td>
		<% if (!bp.isFlNuovoPdg()) { %>
			<td><% bp.getController().writeFormLabel(out,"desctool_area"); %></td>
			<td><% bp.getController().writeFormInput(out,"desctool_area"); %></td>
		<% } %>
		<% if (moduloEntrate.getCd_voce_piano()!=null) {%>
		  	<td><% bp.getController().writeFormLabel(out,"desctool_voce_piano_economico_prg");%></td>
			<td><% bp.getController().writeFormInput(out,"desctool_voce_piano_economico_prg");%></td>
		<% } %>		
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
</div>
<% bp.getCrudDettagliGestionali().writeHTMLTable(
				pageContext,
				"insertGestionale",	
				true,
				false,
				!isDettaglioGestionaleEnable,
				"100%",
				"150px",
				true); %>
<div class="Group card">
<table class="w-100">
	<tr>
		<td><% bp.getCrudDettagliGestionali().writeFormLabel(out,"find_cdr_assegnatario");%></td>
		<td colspan=3><% bp.getCrudDettagliGestionali().writeFormInput(out,null,"find_cdr_assegnatario",isDettaglioGestionaleEnable,null,null);%></td>
	</tr>
	<tr>
		<td><% bp.getCrudDettagliGestionali().writeFormLabel(out,"find_linea_attivita");%></td>
		<td colspan=3>
			<% bp.getCrudDettagliGestionali().writeFormInput( out,"default","find_linea_attivita",isDettaglioGestionaleEnable,null,null);%>
		</td>
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