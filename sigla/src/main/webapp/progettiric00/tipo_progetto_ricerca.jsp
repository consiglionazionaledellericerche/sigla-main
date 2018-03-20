<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.progettiric00.tabrif.bulk.*"
%>

<%
	SimpleCRUDBP bp = (SimpleCRUDBP)BusinessProcess.getBusinessProcess(request);
	Tipo_progettoBulk bulk = (Tipo_progettoBulk)bp.getModel();
%>

<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title>Definizione Tipi di Progetti di Ricerca</title>
</head>

<body class="Form">
<%
	bp.openFormWindow(pageContext);
%>

	<table class="Panel">
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"cd_tipo_progetto");%>
			</TD><TD>
			<% bp.getController().writeFormInput(out,"cd_tipo_progetto");%>
		</TD></TR>
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"ds_tipo_progetto");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"ds_tipo_progetto");%>
		</TD></TR>
	</table>

<%	bp.closeFormWindow(pageContext); %>
</body>