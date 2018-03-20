<!-- 
 ?ResourceName "sezionale.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

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
<title>Sezionale</title>
</head>
<body class="Form">

<% CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>
	<div class="Group">
		<hr>
		<table class="Panel">
			<tr>
				<% bp.getController().writeFormField(out,"esercizio"); %>
			</tr>
		</table>
		<hr>
		<table class="Panel">
			<tr>
				<% bp.getController().writeFormField(out,"cd_tipo_sezionale"); %>			
				<td>
					<% bp.getController().writeFormInput(out,"ds_tipo_sezionale"); %>
				</td>
				<td colspan="3">
					<% bp.getController().writeFormInput(out,"tipo_sezionale"); %>
				</td>
			</tr>
			<tr>
				<td colspan="6">
					<br>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"ti_fattura"); %>
				</td>
				<td colspan="5">
					<% bp.getController().writeFormInput(out,"ti_fattura"); %>
				</td>
			<tr>
		</table>
		<div class="Group">
			<table class="Panel">
				<tr>
					<% bp.getController().writeFormField(out,"primo"); %>
					<% bp.getController().writeFormField(out,"corrente"); %>
					<% bp.getController().writeFormField(out,"ultimo"); %>
				</tr>	  
			</table>
		</div>
	</div>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>