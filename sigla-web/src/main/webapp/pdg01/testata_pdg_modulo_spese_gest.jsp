<head>
<title>Pdg Gestionale - Spese</title>

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
	CRUDPdgModuloSpeseGestBP bp = (CRUDPdgModuloSpeseGestBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
%>
<%! static String[][] tabs = null;%>
<%
        tabs = new String[][] {
	       { "tabTotaliGest","<SPAN style='font : bold 13px;'>Totali</SPAN>","/pdg01/tab_pdg_modulo_spese_gest_totali.jsp" },	                     
	       { "tabSpeseGest","<SPAN style='font : bold 13px;'>Ripartizione Previsione</SPAN>","/pdg01/tab_pdg_modulo_spese_gest.jsp" },	                  
               };   
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
		<td><% bp.getController().writeFormLabel(out,"desctool_classificazione"); %></td>
		<td><% bp.getController().writeFormInput(out,"desctool_classificazione"); %></td>
		<td><% bp.getController().writeFormLabel(out,"desctool_area"); %></td>
		<td><% bp.getController().writeFormInput(out,"desctool_area"); %></td>
	  	<td><% bp.getController().writeFormLabel(out,"desctool_cofog");%></td>
		<td><% bp.getController().writeFormInput(out,"desctool_cofog");%></td>
	</tr>
<!--
	<tr>
		<td><% bp.getController().writeFormLabel(out,"previsione_anno_corrente"); %></td>
		<td><% bp.getController().writeFormInput(out,"previsione_anno_corrente"); %></td>
		<td><% bp.getController().writeFormLabel(out,"dettagli_gestionali_tot"); %></td>
		<td><% bp.getController().writeFormInput(out,"dettagli_gestionali_tot"); %></td>
		<td><% bp.getController().writeFormLabel(out,"dettagli_gestionali_res"); %></td>
		<td><% bp.getController().writeFormInput(out,"dettagli_gestionali_res"); %></td>
	</tr>
-->	
</table>
   <% JSPUtils.tabbed(
                   pageContext,
                   "tab",
                   tabs,
                   bp.getTab("tab"),
                   "center",
                   "100%",
                   "100%" ); %>

<%	bp.closeFormWindow(pageContext); %>
</body>