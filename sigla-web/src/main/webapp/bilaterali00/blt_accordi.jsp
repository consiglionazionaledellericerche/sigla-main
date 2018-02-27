<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.bilaterali00.bulk.Blt_accordiBulk,
		it.cnr.contab.bilaterali00.bp.CRUDBltAccordiBP"%>
<%
  CRUDBltAccordiBP bp = (CRUDBltAccordiBP)BusinessProcess.getBusinessProcess(request);
  Blt_accordiBulk  model = (Blt_accordiBulk)bp.getModel();
%>
<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title><%=bp.getBulkInfo().getShortDescription()%></title>
</head>

<body class="Form">
<% bp.openFormWindow(pageContext); %>
<table class="Panel" width="100%" height="100%">
	<tr>
		<td width="100%">
		    <table class="ToolBar" width="100%" cellspacing="0" cellpadding="2"><tr><td>
			<table class="Panel" align="left" cellspacing=4 cellpadding=4>
			  	<tr>
					<td><% bp.getController().writeFormLabel(out,"cd_accordo");%></td>
			    	<td><% bp.getController().writeFormInput(out,"cd_accordo");%></td>
					<td><% bp.getController().writeFormLabel(out,"ds_accordo");%></td>
			    	<td><% bp.getController().writeFormInput(out,"ds_accordo");%></td>
			  </tr>  
		   	</table>
		   	</td></tr>
		   	</table>	
		</td>
	</tr>
	<tr>
		<td height="100%">
		   <% JSPUtils.tabbed(
		                   pageContext,
		                   "tab",
		                   bp.getTabs(),
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
