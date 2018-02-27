<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.config00.blob.bulk.*,
		it.cnr.jada.util.jsp.*"
%>
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<title>File Excel</title>
<body class="Form">

<% 
	SimpleCRUDBP bp = (SimpleCRUDBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
%>
	<table class="Panel">
		<tr>
           <% bp.writeFormField(out,"default","blob"); %>
		<tr>
			<% bp.writeFormField(out,"default","ds_file"); %>
		</tr>
		<tr>
			<% bp.writeFormField(out,"default","nome_file"); %>
		</tr>
		<tr>
			<% bp.writeFormField(out,"default","stato"); %>
		</tr>
		<tr>
			<% bp.writeFormField(out,"default","tipo"); %>
		</tr>
		<tr>
			<% bp.writeFormField(out,"default","dacr"); %>
		</tr>
		<tr>
			<% bp.writeFormField(out,"default","duva"); %>
		</tr>
			
	</table>
<%	bp.closeFormWindow(pageContext); %>
</body>	