<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page pageEncoding="UTF-8"  import = "it.cnr.jada.util.jsp.*, it.cnr.jada.action.*, it.cnr.jada.bulk.*, it.cnr.jada.util.action.*, it.cnr.contab.missioni00.bp.*, it.cnr.contab.missioni00.docs.bulk.*" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<script language="JavaScript" src="scripts/util.js"></script>
<% JSPUtils.printBaseUrl(pageContext); %>
</head>
<script language="javascript" src="scripts/css.js"></script>
<title>Anticipo</title>
<body class="Form">

<%  
	CRUDAnticipoBP bp = (CRUDAnticipoBP)BusinessProcess.getBusinessProcess(request);
	AnticipoBulk anticipo = (AnticipoBulk)bp.getModel();

	bp.openFormWindow(pageContext); 
%>

<table width="100%">
	<tr></tr>

	<tr>
	<td><% bp.getController().writeFormLabel( out, "esercizio"); %></td>
	<td><% bp.getController().writeFormInput( out, "esercizio"); %></td>
	<td><% bp.getController().writeFormLabel( out, "pg_anticipo"); %></td>
	<td><% bp.getController().writeFormInput( out, "pg_anticipo"); %></td>
	</tr>

	<tr>	
	<td><% bp.getController().writeFormLabel( out, "cd_cds"); %></td>
	<td><% bp.getController().writeFormInput( out, "cd_cds"); %></td>
	<td><% bp.getController().writeFormLabel( out, "cd_unita_organizzativa"); %></td>
	<td><% bp.getController().writeFormInput( out, "cd_unita_organizzativa"); %></td>
	</tr>
	
	<% if (anticipo.isRiportataInScrivania() && !bp.isSearching()) { %>
			<tr>
			<td><span class="FormLabel" style="color:red">
					Documento Riportato
				</span>
			</td>
			<td colspan="3"></td>
	      </tr>
  	<% } %>

	<tr></tr>
</table>

<table class="Panel" width="100%">
	<tr><td>
		<%	JSPUtils.tabbed(	pageContext, "tab",
								bp.getTabs(),
								bp.getTab("tab"),
								"center", "100%", null, 
								true);
		%>
	</td></tr>	
</table>

<%	bp.closeFormWindow(pageContext); %>

</body>
</html>