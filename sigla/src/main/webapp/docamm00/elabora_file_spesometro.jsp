<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.docamm00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Elaborazione File</title>
</head>

<body class="Form">
<% ElaboraFileSpesometroBP bp = (ElaboraFileSpesometroBP)BusinessProcess.getBusinessProcess(request); 
   bp.openFormWindow(pageContext); %>

	<div class="Group" style="width:100%">
	<fieldset> 
		<table width="100%"> 
		<tr>	
		<td>		
					<% bp.getController().writeFormField(out,"nome_file");%>
		</td>
			
		</tr>
		<tr>
			<td>
					<% bp.getController().writeFormField(out,"da_data");%>
			</td>
			<td>		
					<% bp.getController().writeFormField(out,"a_data");%>
			</td>
			<td>
					<% bp.getController().writeFormField(out,"tipo");%>
			</td>
		</tr>
		</table>
	</fieldset>
	</div>
<%bp.closeFormWindow(pageContext); %>
</body>
</html> 