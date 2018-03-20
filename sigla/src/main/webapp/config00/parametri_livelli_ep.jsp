<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
  	    it.cnr.jada.UserContext,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.config00.bp.*,
		it.cnr.contab.config00.bulk.*,
		it.cnr.contab.config00.pdcep.cla.bulk.*"
%>

<%
	CRUDConfigParametriLivelliEPBP bp = (CRUDConfigParametriLivelliEPBP)BusinessProcess.getBusinessProcess(request);
    Parametri_livelli_epBulk bulk = ((Parametri_livelli_epBulk)bp.getModel());
%>

<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title>Parametri Livelli</title>
</head>
<%
	bp.openFormWindow(pageContext);
%>

<body class="Form">
	<table class="Panel">
		<TR>
			<TD><% bp.getController().writeFormLabel(out,"esercizio");%></TD>
			<TD><% bp.getController().writeFormInput(out,"esercizio");%></TD>
		</TR>
		<TR>
<%	((CRUDConfigParametriLivelliEPBP)bp).writeTable(out, ((Parametri_livelli_epBulk) bp.getModel()));%>
		</TR>
	</table>

<%	bp.closeFormWindow(pageContext); %>
</body>