<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page 
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.doccont00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<%  
   CRUDObbligazioneBP bp = (CRUDObbligazioneBP)BusinessProcess.getBusinessProcess(request);
%>
<head>
<script language="JavaScript" src="scripts/util.js"></script>
<% JSPUtils.printBaseUrl(pageContext);%>
</head>
<script language="javascript" src="scripts/css.js"></script>
<title><%=bp.getBulkInfo().getShortDescription()%></title>
<body class="Form">

<%  
		bp.openFormWindow(pageContext); 
%>

<table class="Panel">
	<tr><td colspan=2>
	<%	
		JSPUtils.tabbed(
					pageContext,
					"tab",
					bp.getTabs(),
					bp.getTab("tab"),
					"center",
					null, null,
					!bp.isEditingScadenza());
	%>
	</td></tr>	
	</table>
<%	bp.closeFormWindow(pageContext); %>
</body>