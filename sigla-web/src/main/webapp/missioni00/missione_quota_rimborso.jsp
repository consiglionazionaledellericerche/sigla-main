<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.missioni00.bp.*"				
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Quota Rimborso</title>
</head>
<body class="Form">

<% 	CRUDMissioneQuotaRimborsoBP bp = (CRUDMissioneQuotaRimborsoBP)BusinessProcess.getBusinessProcess(request);
 	bp.openFormWindow(pageContext); %>

<table class="Panel">

  <tr>
	<td><% bp.getController().writeFormLabel( out,"area_estera"); %></td>
	<td colspan="3"><% bp.getController().writeFormInput(out,"area_estera"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel( out,"gruppo_inquadramento"); %></td>
	<td colspan="3"><% bp.getController().writeFormInput(out,"gruppo_inquadramento"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"im_rimborso"); %></td>
	<td colspan="3"><% bp.getController().writeFormInput(out,"im_rimborso");%></td>
  </tr>	
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dt_inizio_validita"); %></td>
	<td><% bp.getController().writeFormInput(out,"dt_inizio_validita"); %></td>
	<td align="right"><% bp.getController().writeFormLabel(out,"dtFineValidita"); %></td>
	<td><% bp.getController().writeFormInput(out,"dtFineValidita"); %></td>
  </tr>
		
</table>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>