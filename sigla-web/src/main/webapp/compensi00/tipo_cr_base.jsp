<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.contab.compensi00.bp.*,
		it.cnr.jada.util.action.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Ass. Gruppo CORI - CORI</title>
</head>
<body class="Form">

<% 	SimpleCRUDBP bp = (SimpleCRUDBP)BusinessProcess.getBusinessProcess(request);
 	bp.openFormWindow(pageContext);
%>

<table class="Panel">

  
  <tr>
	<td><% bp.getController().writeFormLabel(out,"find_contributo_ritenuta"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_contributo_ritenuta"); %></td>
	<td><% bp.getController().writeFormInput(out,"ds_contributo_ritenuta"); %></td>
	<td><% bp.getController().writeFormInput(out,"find_contributo_ritenuta"); %></td>				 
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"gruppo"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_gruppo_cr"); %></td>
	<td><% bp.getController().writeFormInput(out,"ds_gruppo_cr"); %></td>
	<td><% bp.getController().writeFormInput(out,"gruppo"); %></td>				 
  </tr>
</table>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>