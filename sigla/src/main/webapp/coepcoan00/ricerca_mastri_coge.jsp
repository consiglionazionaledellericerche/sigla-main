<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,it.cnr.jada.bulk.*,it.cnr.jada.util.action.*,it.cnr.contab.coepcoan00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="JavaScript" src="scripts/disableRightClick.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Visualizzazione mastri</title>
</head>
<body class="Form">

<%  
		RicercaMastriCogeBP bp = (RicercaMastriCogeBP)BusinessProcess.getBusinessProcess(request);
		bp.openFormWindow(pageContext); 
%>

<table class="Panel">
<tr>
	<td><% bp.getController().writeFormLabel(out,"esercizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"esercizio"); %></td>	
</tr>
<tr>
	<td><% bp.getController().writeFormLabel(out,"cd_cds"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_cds"); %>
		<% bp.getController().writeFormInput(out,"ds_cds"); %></td>	
</tr>
<tr>
	<td><% bp.getController().writeFormLabel(out,"cd_unita_organizzativa"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_unita_organizzativa"); %>
		<% bp.getController().writeFormInput(out,"ds_unita_organizzativa"); %></td>	
</tr>
<tr>
	<td><% bp.getController().writeFormLabel(out,"ti_istituz_commerc"); %></td>
	<td><% bp.getController().writeFormInput(out,"ti_istituz_commerc"); %></td>	
</tr>
<tr>
	<td><% bp.getController().writeFormLabel(out,"cd_terzo"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_terzo"); %>
    	<% bp.getController().writeFormInput(out,"ds_terzo"); %>
    	<% bp.getController().writeFormInput(out,"find_terzo"); %></td>
</tr>
<tr>
	<td><% bp.getController().writeFormLabel(out,"cd_voce_ep"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_voce_ep"); %>
    	<% bp.getController().writeFormInput(out,"ds_conto"); %>
    	<% bp.getController().writeFormInput(out,"find_conto"); %>    	
	</td>
<tr>
	<td><% bp.getController().writeFormLabel(out,"tot_avere"); %></td>
	<td><% bp.getController().writeFormInput(out,"tot_avere"); %></td>
</tr>
<tr>
	<td><% bp.getController().writeFormLabel(out,"tot_dare"); %></td>
	<td><% bp.getController().writeFormInput(out,"tot_dare"); %></td>
</tr>
</table>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>