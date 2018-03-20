<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
  	    it.cnr.jada.UserContext,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.config00.bp.*,
		it.cnr.contab.config00.bulk.*"
%>

<%
	CRUDConfigParametriUoBP bp = (CRUDConfigParametriUoBP)BusinessProcess.getBusinessProcess(request);
	UserContext uc = HttpActionContext.getUserContext(session);
	boolean isFlGestioneModuliEnabled = !bp.isFlGestioneModuliEnabled(uc, (Parametri_uoBulk)bp.getModel());
%>

<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title>Parametri Unità Organizzativa</title>
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
			<% bp.getController().writeFormLabel(out,"unita_organizzativa");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"unita_organizzativa");%>
		</TD></TR>
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"fl_gestione_moduli");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"fl_gestione_moduli");%>
		</TD></TR>
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"modulo_default");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"modulo_default");%>
		</TD></TR>
	</table>

<%	bp.closeFormWindow(pageContext); %>
</body>