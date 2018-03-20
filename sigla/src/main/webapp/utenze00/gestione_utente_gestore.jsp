<!-- 
 ?ResourceName "gestione_conti.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Inserimento Associazione Utente - Gestore</title>
</head>
<body class="Form">

<% CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>

<table class="Panel">
  	<tr>
		<td><% bp.getController().writeFormLabel( out, "cd_utente"); %></td>
		<td>
	    	<% bp.getController().writeFormInput( out, "cd_utente"); %>
      		<% bp.getController().writeFormInput( out, "find_utente"); %>	
		</td>				 
	</tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_gestore"); %></td>
	<td><% bp.getController().writeFormInput( out, "cd_gestore"); %>
		<% bp.getController().writeFormInput( out, "cd_utente_uid"); %>
      	<% bp.getController().writeFormInput( out, "find_gestore"); %>	
		</td>	
  </tr>
</table>  
<% bp.closeFormWindow(pageContext); %>
</body>
</html>