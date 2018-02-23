<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.config00.bulk.*"
%>

<%
	SimpleCRUDBP bp = (SimpleCRUDBP)BusinessProcess.getBusinessProcess(request);
	Parametri_enteBulk bulk = (Parametri_enteBulk)bp.getModel();
%>

<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title>Parametri Ente</title>
</head>

<body class="Form">
<%
	bp.openFormWindow(pageContext);
%>
	<table class="Panel card p-2">
		<TR><% bp.getController().writeFormField(out,"id");%></TR>
		<TR><% bp.getController().writeFormField(out,"descrizione");%></TR>
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"tipo_db");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"tipo_db");%>
		</TD></TR>
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"attivo");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"attivo");%>
		</TD></TR>
		<TR><% bp.getController().writeFormField(out,"cancella_stampe");%></TR>
		<TR><% bp.getController().writeFormField(out,"fl_autenticazione_ldap");%></TR>
		<TR><% bp.getController().writeFormField(out,"ldap_user");%></TR>
		<TR><% bp.getController().writeFormField(out,"ldap_password");%></TR>
		<TR><% bp.getController().writeFormField(out,"ldap_base_dn");%></TR>
		<TR><% bp.getController().writeFormField(out,"ldap_app_name");%></TR>
		<TR><% bp.getController().writeFormField(out,"dt_ldap_migrazione");%></TR>
		<TR><% bp.getController().writeFormField(out,"ldap_link_cambio_password");%></TR>
		<TR><% bp.getController().writeFormField(out,"fl_informix");%></TR>
		<TR><% bp.getController().writeFormField(out,"fl_gae_es");%></TR>
		<TR><% bp.getController().writeFormField(out,"fl_prg_pianoeco");%></TR>

		<TR><% bp.getController().writeFormField(out,"abil_progetto_strorg");%></TR>
		<TR><% bp.getController().writeFormField(out,"fl_variazioni_trasferimento");%></TR>
	</table>

<%	bp.closeFormWindow(pageContext); %>
</body>