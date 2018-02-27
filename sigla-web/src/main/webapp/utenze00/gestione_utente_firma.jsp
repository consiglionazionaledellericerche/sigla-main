<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.utenze00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<script language="JavaScript" src="scripts/util.js"></script>
<% JSPUtils.printBaseUrl(pageContext); %>
</head>
<script language="javascript" src="scripts/css.js"></script>
<title>Gestione utenza per firma</title>
<body class="Form">

<% 
	CRUDUtenteFirmaBP bp = (CRUDUtenteFirmaBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
%>
	<table class="Form">
		<tr><%bp.writeFormField( out, "utente"); %></tr>
		<tr><%bp.writeFormField( out, "unitaOrganizzativa"); %></tr>
		<tr><%bp.writeFormField( out, "titoloFirma"); %></tr>
		<tr><%bp.writeFormField( out, "daData"); %></tr>
		<tr><%bp.writeFormField( out, "aData"); %></tr>
		<tr><%bp.writeFormField( out, "funzioniAbilitate"); %></tr>
		<tr><%bp.writeFormField( out, "flDelegato"); %></tr>
		<tr><%bp.writeFormField( out, "delegato"); %></tr>		
	</table>	
<% bp.closeFormWindow(pageContext); %>
</body>
</html>