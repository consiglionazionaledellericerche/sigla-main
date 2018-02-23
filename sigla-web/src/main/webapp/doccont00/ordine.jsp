<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	it.cnr.jada.action.*,
	java.util.*,
	it.cnr.jada.util.action.*,
	it.cnr.contab.doccont00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Ordine</title>
</head>
<body class="Form"> 

<% CRUDOrdineBP bp = (CRUDOrdineBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>

<table>
  <tr>
	<td><% bp.getController().writeFormLabel( out, "pg_ordine"); %></td>
	<td><% bp.getController().writeFormInput( out, "pg_ordine"); %></td>
	<td><% bp.getController().writeFormLabel( out, "esercizio_ori_obbligazione"); %></td>
	<td><% bp.getController().writeFormInput( out, "esercizio_ori_obbligazione"); %></td>
	<td><% bp.getController().writeFormLabel( out, "pg_obbligazione"); %></td>
	<td><% bp.getController().writeFormInput( out, "pg_obbligazione"); %></td>
  </tr>
</table>

<table class="Panel">
  <tr>
	<td><% JSPUtils.tabbed(
				pageContext,
				"tab",
				new String[][] {
					{ "tabOrdineTestata","Testata","/doccont00/tab_ordine_testata.jsp" },
					{ "tabOrdineFornitore","Fornitore","/doccont00/tab_ordine_fornitore.jsp" },
					{ "tabOrdineDettaglio","Dettaglio","/doccont00/tab_ordine_dettaglio.jsp" },
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