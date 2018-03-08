<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.anagraf00.bp.*"
%>

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Classificazione per Aree dei paesi esteri</title>
</head>
<body class="Form">

<%	CRUDRifAreePaesiEsteriBP bp = (CRUDRifAreePaesiEsteriBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext); %>

<table class="Panel">
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_area_estera"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_area_estera"); %></td>
  </tr>
    <tr>
	<td><% bp.getController().writeFormLabel(out,"ds_area_estera"); %></td>
	<td><% bp.getController().writeFormInput(out,"ds_area_estera"); %></td>
  </tr>
      <tr>
	<td><% bp.getController().writeFormLabel(out,"ti_italia_estero"); %></td>
	<td colspan="3"><% bp.getController().writeFormInput(out,"ti_italia_estero"); %></td>
  </tr>
</table>

<%	bp.closeFormWindow(pageContext); %>

</body>
</html>