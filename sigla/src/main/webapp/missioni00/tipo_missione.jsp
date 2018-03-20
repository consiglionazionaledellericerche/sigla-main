<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.missioni00.tabrif.bulk.*,
		it.cnr.contab.missioni00.bp.*"				
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Ambito Missione</title>
</head>
<body class="Form">

<% 	
	CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
 	bp.openFormWindow(pageContext);
%>

<table class="Panel">
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_tipo_missione"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_tipo_missione"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ds_tipo_missione"); %></td>
	<td><% bp.getController().writeFormInput(out,"ds_tipo_missione"); %></td>
  </tr>
   <tr>
	<td><% bp.getController().writeFormLabel(out,"fl_valido"); %></td>
	<td><% bp.getController().writeFormInput(out,"fl_valido"); %></td>
  </tr>
</table>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>