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
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Documento generico passivo</title>
</head>
<body class="Form">
<% CRUDBP bp = (CRUDDocumentoGenericoPassivoBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>
	<table>
		<%

		JSPUtils.tabbed(
					pageContext,
					"tab",
					new String[][] {
                        { "tabDocumentoPassivo","Documento Generico","/docamm00/tab_documento_passivo.jsp" },
                        { "tabDocumentoPassivoDettaglio","Dettaglio","/docamm00/tab_documento_passivo_dettaglio.jsp" },
                        { "tabDocumentoGenericoObbligazioni","Impegni","/docamm00/tab_documento_generico_obbligazioni.jsp" },
                        { "tabLetteraPagamentoEstero","Documento 1210","/docamm00/tab_generico_lettera_pagam_estero.jsp"},
                        {"tabEconomica", "Economico/Patrimoniale", "/coepcoan00/tab_doc_economica.jsp"}
					},
					bp.getTab("tab"),
					"center",
					"100%",
					null );
		%>
	
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>