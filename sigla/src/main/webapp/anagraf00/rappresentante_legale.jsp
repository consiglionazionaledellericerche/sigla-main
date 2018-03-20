<!-- 
 ?ResourceName "gestione_conti.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.jsp.*,
		java.util.*,
		it.cnr.jada.util.action.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Rappresentante Legale</title>
</head>
<body class="Form">

<% CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext); %>

	<table class="Panel">
		<tr><% bp.getController().writeFormField(out,"pg_rapp_legale");%></tr>
		<tr><% bp.getController().writeFormField(out,"nome");%></tr>
		<tr><% bp.getController().writeFormField(out,"cognome");%></tr>
		<tr><% bp.getController().writeFormField(out,"codice_fiscale");%></tr>
		<tr><% bp.getController().writeFormField(out,"titolo");%></tr>
		<tr>
			<td colspan="3">
				<div class="GroupLabel">Dati anagrafici</div><div class="Group">
					<table class="Panel">
						<tr>
							<% bp.getController().writeFormField(out,"ds_comune_nascita");%>
							<td><% bp.getController().writeFormInput(out,"find_comune_nascita");%></td>
						<tr>
							<% bp.getController().writeFormField(out,"dt_nascita");%>
						</tr>
						<tr>
							<% bp.getController().writeFormField(out,"ds_provincia_nascita");%>
						</tr>
						<tr>
							<% bp.getController().writeFormField(out,"ds_nazione_nascita");%>
						</tr>
					</table>
				</div>
			</td>
		</tr>
	</table>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>