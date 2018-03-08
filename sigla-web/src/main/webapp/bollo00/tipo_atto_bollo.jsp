<%@page import="it.cnr.contab.doccont00.tabrif.bulk.Tipo_bolloBulk"%>
<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.bollo00.tabrif.bulk.*"
%>

<%
	SimpleCRUDBP bp = (SimpleCRUDBP)BusinessProcess.getBusinessProcess(request);
	Tipo_atto_bolloBulk model = (Tipo_atto_bolloBulk)bp.getModel(); 
%>

<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title>Tipi Documento per Bollo Virtuale</title>
</head>
<body class="Form">
<%
	bp.openFormWindow(pageContext);
%>
<div class="card p-2">
	<table class="Panel w-100">
		<TR><% bp.getController().writeFormField(out,"codice");%></TR>
		<TR><% bp.getController().writeFormField(out,"descrizione");%></TR>
		<TR><% bp.getController().writeFormField(out,"descNorma");%></TR>
		<tr><% bp.getController().writeFormField(out,"imBollo"); %></tr>
		<tr><% bp.getController().writeFormField(out,"tipoCalcolo"); %></tr>
		<% if (model!=null && model.getTipoCalcolo()!=null) { %>  
			<tr>
				<% 	if (model.isCalcoloPerImporto())
						bp.getController().writeFormField(out,"impLimiteCalcolo");
				 	else if (model.isCalcoloPerFoglio())
						bp.getController().writeFormField(out,"numFogliCalcolo");
					else
						bp.getController().writeFormField(out,"numEsemplariCalcolo");
				%>
			</tr>
			<% if (model.isCalcoloPerFoglio()) { %>
				<tr><% bp.getController().writeFormField(out,"righeFoglio"); %></tr>
			<% } %>
		<% } %>		
		<tr><% bp.getController().writeFormField(out,"dtIniValidita"); %></tr>
		<tr><% bp.getController().writeFormField(out,"dtFinValidita"); %></tr>
	</table>
</div>
<%	bp.closeFormWindow(pageContext); %>
</body>