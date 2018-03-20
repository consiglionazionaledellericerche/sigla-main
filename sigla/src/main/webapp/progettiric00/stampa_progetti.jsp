<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.progettiric00.core.bulk.*,
		it.cnr.contab.progettiric00.bp.*"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Stampa progetti per Dipartimento/Istituto</title>
</head>
<body class="Form">

<%	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	Stampa_progettiVBulk bulk = (Stampa_progettiVBulk)bp.getModel();
	bp.openFormWindow(pageContext); %>
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"esercizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"esercizio"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_cds"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_cds"); %></td>
  </tr>  
  <tr>
	  <td>
		<% bp.getController().writeFormLabel(out,"cd_progetto");%>
	  </td>
	  <td>
		<% bp.getController().writeFormInput(out,"cd_progetto");%>
		<% bp.getController().writeFormInput(out,"ds_progetto");%>
		<% bp.getController().writeFormInput(out,"find_nodo_padre");%>
	  </td>
   </tr>
  
</table>

<% bp.closeFormWindow(pageContext); %>

</body>
</html>