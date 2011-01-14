<%@ page 
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.pdg00.cdip.bulk.*,
		it.cnr.contab.pdg00.bp.*"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<%	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Stampa Variazione allo Stanziamento Residuo</title>
</head>
<body class="Form">

<%	bp.openFormWindow(pageContext); %>

<table width=100%>
 <tr>
  <td>
	<div class="Group">
	<table>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"esercizio"); %></td>
		<td><% bp.getController().writeFormInput(out,"esercizio"); %></td>
	  </tr>	
	  <tr>	
		<td><% bp.getController().writeFormLabel(out,"findvariazioneForPrint"); %></td>
		<td>
			<% bp.getController().writeFormInput(out,"pg_variazione"); %>
			<% bp.getController().writeFormInput(out,"ds_variazione"); %>
			<% bp.getController().writeFormInput(out,"findvariazioneForPrint"); %></td>
	  </tr>  
	</table>
	</div>
   </td>
  </tr>
</table>


<% bp.closeFormWindow(pageContext); %>

</body>
</html>