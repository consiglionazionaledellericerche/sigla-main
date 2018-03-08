<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.config00.contratto.bulk.*,
		it.cnr.contab.config00.bp.*"
%>

<%
	SimpleCRUDBP bp = (SimpleCRUDBP)BusinessProcess.getBusinessProcess(request);
%>
<%!     static String[][] tabs = null;
%>
<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="JavaScript" src="scripts/fckeditor.js"></script>
<title><%=bp.getBulkInfo().getShortDescription()%></title>
</head>

<body class="Form">
<% bp.openFormWindow(pageContext);
   tabs = new String[][] {
	               { "tabTestata","Testata","/config00/tabTestataParametriCNR.jsp" },
	               { "tabEmailConguagli","Configurazione EMail Terzi da Conguagliare","/config00/tabEMailConguaParametriCNR.jsp" }
	               };   
%>
<table class="Panel" width="100%" height="100%">
	<tr>
		<td height="100%">
		   <% JSPUtils.tabbed(
		                   pageContext,
		                   "tab",
		                   tabs,
		                   bp.getTab("tab"),
		                   "center",
		                   "100%",
		                   "100%" ); %>
		</td>
	</tr>
</table>
<% bp.closeFormWindow(pageContext); %>
</body>
</html>