<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	it.cnr.jada.action.*,
	java.util.*,
	it.cnr.jada.util.action.*,
	it.cnr.contab.compensi00.bp.*,
	it.cnr.contab.compensi00.docs.bulk.*"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title> Elaborazione 770 </title>
</head>
<body class="Form"> 

<%	Estrazione770BP bp = (Estrazione770BP)BusinessProcess.getBusinessProcess(request);
    Estrazione770Bulk estrazione = (Estrazione770Bulk)bp.getModel();
	bp.openFormWindow(pageContext); 
	%>

<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"esercizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"esercizio"); %></td>
  </tr>
</table>
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findQuadri"); %></td>
	<td>
		<% bp.getController().writeFormInput(out,"cd_quadro"); %>
		<% bp.getController().writeFormInput(out,"ds_quadro"); %>
		<% bp.getController().writeFormInput(out,"findQuadri"); %>
	</td>
  </tr>
</table>
<table>  
  <tr>
	<td colspan=2>
		<% if (estrazione != null && estrazione.isFileOrdinario()) { %>
			<span class="FormLabel" style="color:red">Modello Ordinario</span>
		<% } %>
	</td>
  </tr>
  <tr>
	<td colspan=2>
		<% if (estrazione != null && estrazione.isFileSemplificato()) { %>
			<span class="FormLabel" style="color:red">Modello Semplificato</span>
		<% } %>
	</td>
  </tr>  
</table>

<% bp.closeFormWindow(pageContext); %>

</body>
</html>