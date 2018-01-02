<%@page import="it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk"%>
<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.incarichi00.bp.CRUDIncarichiProceduraBP"%>
<%
	CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)BusinessProcess.getBusinessProcess(request);
    Incarichi_proceduraBulk model = (Incarichi_proceduraBulk)bp.getModel(); 
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
		         <td><% bp.getController().writeFormField(out,"esercizio");%></td>
		         <td><% bp.getController().writeFormField(out,"pg_procedura");%></td>
				 <td><% bp.getController().writeFormInput(out,null,"statoText",true,"GroupLabel text-primary h3 inputFieldReadOnly font-italic",
						 	bp.getParentRoot().isBootstrap()?null:"style=\"background: #F5F5DC;background-color:transparent;border-style:none;cursor:default;font-size:16px;font-style:italic;\"");%></td>
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
