<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.action.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.preventvar00.consultazioni.bp.*" %>


<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Assestato Competenza alla Data</title>
</head>
<body class="Form">

<% 
	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	ConsAssCompPerDataBP bp1 = (ConsAssCompPerDataBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
	boolean isFieldReadOnly = true;
%>

	<div class="Group" style="width:100%">
		<table width="100%">
		
			  <tr>
				<td> <% bp.getController().writeFormLabel(out,"esercizio"); %></td>
    			<td> <% bp.getController().writeFormInput(out,null,"esercizio",false,null,"");%></td>
    		 </tr>
    		
			  <tr>
				<td> <% bp.getController().writeFormLabel(out,"data_approvazione_var");%></td>
				<td> <% bp.getController().writeFormInput(out,null,"data_approvazione_var",false,null,"");%></td>
			  </tr>
			  
			  <tr>
				<td><% bp.getController().writeFormLabel(out,"ti_gestione"); %></td>
    			<td><% bp.getController().writeFormInput(out,null,"ti_gestione",false,null,"onclick=\"submitForm('doCambiaGestione')\""); %></td>
 			</tr>   
 			 
	</table>
		
	</div>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html> 
