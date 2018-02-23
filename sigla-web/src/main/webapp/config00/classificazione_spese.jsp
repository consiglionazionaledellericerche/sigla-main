<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.config00.pdcfin.bulk.*"
%>

<%
	SimpleCRUDBP bp = (SimpleCRUDBP)BusinessProcess.getBusinessProcess(request);
%>

<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title>Classificazione Spese</title>
</head>

<body class="Form">
<%
	bp.openFormWindow(pageContext);
%>

	<table class="Panel">
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"esercizio");%>
			</TD><TD>
			<% bp.getController().writeFormInput(out,"esercizio");%>
		</TD></TR>	
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"codice_cla_s");%>
			</TD><TD>
			<% bp.getController().writeFormInput(out,"codice_cla_s");%>
		</TD></TR>
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"descrizione");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"descrizione");%>
		</TD></TR>
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"codice_cla_s_padre");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"esercizio_padre");%>
			<% bp.getController().writeFormInput(out,"codice_cla_s_padre");%>
			<% bp.getController().writeFormInput(out,"find_classificazione_spese");%>
		</TD></TR>	
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"fl_mastrino");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"fl_mastrino");%>
		</TD></TR>
	</table>

<%	bp.closeFormWindow(pageContext); %>
</body>