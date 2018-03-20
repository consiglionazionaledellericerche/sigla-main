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
		it.cnr.contab.coepcoan00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="JavaScript" src="scripts/disableRightClick.js"></script>
<% JSPUtils.printBaseUrl(pageContext);%>
</head>
<script language="javascript" src="scripts/css.js"></script>
<title>Ricerca movimenti contabili</title>
<body class="Form">

<%  
		RicercaMovContCoanBP bp = (RicercaMovContCoanBP)BusinessProcess.getBusinessProcess(request);
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
	<td><% bp.getController().writeFormLabel(out,"pg_scrittura"); %></td>
	<td><% bp.getController().writeFormInput(out,"pg_scrittura"); %></td>
</tr>

<tr>
	<td><% bp.getController().writeFormLabel(out,"attiva"); %></td>
	<td>
		<% bp.getController().writeFormInput(out,"attiva"); %>
		<% bp.getController().writeFormLabel(out,"pg_scrittura_annullata"); %>
		<% bp.getController().writeFormInput(out,"pg_scrittura_annullata"); %>			
	</td>
</tr>

<tr>
	<td><% bp.getController().writeFormLabel(out,"sezione"); %></td>
	<td><% bp.getController().writeFormInput(out,"sezione"); %></td>
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
    	<% bp.getController().writeFormInput(out,"ds_voce_ep"); %>
    	<% bp.getController().writeFormInput(out,"find_voce_ep"); %>    	
	</td>
</tr>
<tr>
	<td><% bp.getController().writeFormLabel(out,"cd_linea_attivita"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_centro_responsabilita");%>
		<% bp.getController().writeFormInput(out,"cd_linea_attivita"); %>
    	<% bp.getController().writeFormInput(out,"ds_linea_attivita"); %>
    	<% bp.getController().writeFormInput(out,"find_linea_attivita"); %>    	
	</td>
</tr>
<tr>
	<td><% bp.getController().writeFormLabel(out,"im_movimento"); %></td>
	<td><% bp.getController().writeFormInput(out,"im_movimento"); %></td>
</tr>
</table>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>