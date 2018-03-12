<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	it.cnr.jada.action.*,
	java.util.*,
	it.cnr.jada.util.action.*,
	it.cnr.contab.compensi00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Minicarriera</title>
</head>
<body class="Form"> 

<% CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>

<table width="100%" class="card p-2">
  <tr>
	<td><% bp.getController().writeFormLabel( out, "esercizio"); %></td>
	<td><% bp.getController().writeFormInput( out, "esercizio"); %></td>
	<td><% bp.getController().writeFormLabel( out, "pgMinicarrieraPos"); %></td>
	<td><% bp.getController().writeFormInput( out, "pgMinicarrieraPos"); %></td>
  </tr>
</table>

<table class="Panel" width="100%">
  <tr>
	<td><% JSPUtils.tabbed(
					pageContext,
					"tab",
					new String[][] {
						{ "tabMinicarriera","Testata","/compensi00/tab_minicarriera.jsp" },
						{ "tabMinicarrieraPercipiente","Terzo","/compensi00/tab_minicarriera_percipiente.jsp" },
						{ "tabMinicarrieraRate", "Rate", "/compensi00/tab_minicarriera_rate.jsp"}
					},
					bp.getTab("tab"),
					"center",
					"100%",
					null ); %>

	</td>
  </tr>
</table>

<% bp.closeFormWindow(pageContext); %>

</body>
</html>