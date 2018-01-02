<%@page import="it.cnr.contab.prevent01.bulk.Pdg_modulo_speseBulk"%>
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
	Pdg_modulo_speseBulk moduloSpese = (Pdg_modulo_speseBulk)bp.getModel();
	bp.openFormWindow(pageContext);
%>
<%! static String[][] tabs = null;%>
<%
        tabs = new String[][] {
	       { "tabTotaliGest","<SPAN style='font : bold 13px;'>Totali</SPAN>","/pdg01/tab_pdg_modulo_spese_gest_totali.jsp" },	                     
	       { "tabSpeseGest","<SPAN style='font : bold 13px;'>Ripartizione Previsione</SPAN>","/pdg01/tab_pdg_modulo_spese_gest.jsp" },	                  
               };   
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
			<% bp.getController().writeFormField(out,"modulo"); %>
			<% bp.getController().writeFormField(out,"commessa"); %>
			<% bp.getController().writeFormField(out,"progetto"); %>
			<% bp.getController().writeFormField(out,"desctool_area"); %>
		<% } else { %>
			<% bp.getController().writeFormField(out,"cd_progetto_liv2"); %>
			<% bp.getController().writeFormField(out,"cd_progetto_liv1"); %>
			<% bp.getController().writeFormField(out,"programma_liv2"); %>
			<% bp.getController().writeFormField(out,"desctool_missione"); %>
		<% } %>		
	</tr>
	<tr>
		<% if (!bp.isFlNuovoPdg()) { %>
			<% bp.getController().writeFormField(out,"dipartimento"); %>
		<% } %>		
		<% bp.getController().writeFormField(out,"desctool_classificazione"); %>
	  	<% bp.getController().writeFormField(out,"desctool_cofog");%>
		<% if (moduloSpese.getCd_voce_piano()!=null) {%>
		  	<% bp.getController().writeFormField(out,"desctool_voce_piano_economico_prg");%>
		<% } %>		
	</tr>
</table>
</div>

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