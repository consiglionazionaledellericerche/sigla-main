<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.bilaterali00.bulk.Blt_visiteBulk,
		it.cnr.contab.bilaterali00.bp.CRUDBltVisiteBP"%>
<%
  CRUDBltVisiteBP bp = (CRUDBltVisiteBP)BusinessProcess.getBusinessProcess(request);
  Blt_visiteBulk  model = (Blt_visiteBulk)bp.getModel();
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
			<table class="Panel" width="100%" align="left" cellspacing="4" cellpadding="4">
			  	<tr>
					<td width="12%"><% bp.getController().writeFormLabel(out,"annoVisita");%></td>
			    	<td width="14&"><% bp.getController().writeFormInput(out,"annoVisita");%></td>
			    	<td width="74%"><% bp.getController().writeFormInput(out,null,"statoText",true,"GroupLabel","style=\"background: #F5F5DC;background-color:transparent;border-style:none;cursor:default;font-size:16px;font-style:italic;width:100%;\"");%></td>
				</tr>  
				<tr>
					<td><% bp.getController().writeFormLabel(out,"findAccordo");%></td>
					<td colspan=2><% bp.getController().writeFormInput(out,"findAccordo");%>
					</td>
				</tr>
				<tr>
					<td><% bp.getController().writeFormLabel(out,"findProgetto");%></td>
					<td colspan=2><% bp.getController().writeFormInput(out,"findProgetto");%>
					</td>
				</tr>
				<tr>
					<td><% bp.getController().writeFormLabel(out,"findTerzo");%></td>
					<td colspan=2><% bp.getController().writeFormInput(out,"findTerzo");%>
					</td>
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
