<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.prevent00.bp.*,
		it.cnr.contab.prevent00.bulk.*"		
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Residui presunti CNR</title>
</head>
<body class="Form">

<%  
	CRUDResiduiPresuntiBP bp = (CRUDResiduiPresuntiBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
	Voce_f_res_presBulk voce = (Voce_f_res_presBulk) bp.getModel();	
%>

	
<table class="Panel">

	<tr>
	<td>	<% bp.getController().writeFormLabel( out, "cd_voce"); %></td>	
	<td>	<% bp.getController().writeFormInput( out, "cd_voce"); %>
			<% bp.getController().writeFormInput( out, "ds_voce"); %>	
			<% bp.getController().writeFormInput( out, "find_voce"); %></td>
	</tr>
	<tr>	
	<td>	<% bp.getController().writeFormLabel( out, "im_residui_presunti"); %></td>
	<td>	<% bp.getController().writeFormInput( out, "im_residui_presunti"); %>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<% bp.getController().writeFormLabel( out, "ti_gestione"); %>	
			<% bp.getController().writeFormInput( out, "ti_gestione"); %></td>	
	</tr>

</table>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>