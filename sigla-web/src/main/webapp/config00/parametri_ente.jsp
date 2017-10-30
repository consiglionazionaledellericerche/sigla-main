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

	<table class="Panel">
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"id");%>
			</TD><TD>
			<% bp.getController().writeFormInput(out,"id");%>
		</TD></TR>	
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"descrizione");%>
			</TD><TD>
			<% bp.getController().writeFormInput(out,"descrizione");%>
		</TD></TR>
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
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"cancella_stampe");%>
			</TD><TD>
			<% bp.getController().writeFormInput(out,"cancella_stampe");%>
		</TD></TR>
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"fl_autenticazione_ldap");%>
			</TD><TD>
			<% bp.getController().writeFormInput(out,"fl_autenticazione_ldap");%>
		</TD></TR>
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"ldap_user");%>
			</TD><TD>
			<% bp.getController().writeFormInput(out,"ldap_user");%>
		</TD></TR>
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"ldap_password");%>
			</TD><TD>
			<% bp.getController().writeFormInput(out,"ldap_password");%>
		</TD></TR>
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"ldap_base_dn");%>
			</TD><TD>
			<% bp.getController().writeFormInput(out,"ldap_base_dn");%>
		</TD></TR>
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"ldap_app_name");%>
			</TD><TD>
			<% bp.getController().writeFormInput(out,"ldap_app_name");%>
		</TD></TR>
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"dt_ldap_migrazione");%>
			</TD><TD>
			<% bp.getController().writeFormInput(out,"dt_ldap_migrazione");%>
		</TD></TR>
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"ldap_link_cambio_password");%>
			</TD><TD>
			<% bp.getController().writeFormInput(out,"ldap_link_cambio_password");%>
		</TD></TR>
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"fl_informix");%>
			</TD><TD>
			<% bp.getController().writeFormInput(out,"fl_informix");%>
		</TD></TR>
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"fl_gae_es");%>
			</TD><TD>
			<% bp.getController().writeFormInput(out,"fl_gae_es");%>
		</TD></TR>
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"fl_prg_pianoeco");%>
			</TD><TD>
			<% bp.getController().writeFormInput(out,"fl_prg_pianoeco");%>
		</TD></TR>
		<TABLE width="400">
		<TR>		
		<TD align="center" width="50%"><% bp.getController().writeFormLabel(out,"box_scadenze");%></TD>
		<TD align="center" width="50%"><% bp.getController().writeFormLabel(out,"box_comunicazioni");%></TD>		
		</TR>
		<TR>		
		<TD align="center" width="50%"><% bp.getController().writeFormInput(out,"box_scadenze");%></TD>
		<TD align="center" width="50%"><% bp.getController().writeFormInput(out,"box_comunicazioni");%></TD>		
		</TR>		
		</TABLE>
	</table>

<%	bp.closeFormWindow(pageContext); %>
</body>