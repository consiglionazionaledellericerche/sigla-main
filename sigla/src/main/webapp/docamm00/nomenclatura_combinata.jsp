<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Nomenclatura Combinata</title>
</head>
<body class="Form">

<%	CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext); %>
	<table> 
	<tr>
		<td colspan="3">
		<% bp.getController().writeFormField(out,"id_nomenclatura_combinata"); %>
		</td>
	</tr>
	<tr>	
		<td colspan="3">
		<% bp.getController().writeFormField(out,"esercizio"); %>
		</td>
	</tr>
	<tr>	
		<td colspan="3">
		<% bp.getController().writeFormField(out,"cd_nomenclatura_combinata"); %>
		</td>
	</tr>
	<tr>	
		<td colspan="3">
		<% bp.getController().writeFormField(out,"ds_nomenclatura_combinata"); %>
		</td>
	</tr>
	<tr>	
		<td colspan="3">
		<% bp.getController().writeFormField(out,"livello"); %>
		</td>
	</tr>
	<tr>	
		<td colspan="3">
		<% bp.getController().writeFormField(out,"esercizio_inizio"); %>
		</td>
	</tr>
	<tr>	
		<td colspan="3">
		<% bp.getController().writeFormField(out,"esercizio_fine"); %>
		</td>
	</tr>
	<tr>	
		<td colspan="3">
		<% bp.getController().writeFormField(out,"unita_supplementari"); %>
		</td>
	</tr>
	
	</table>
	
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>