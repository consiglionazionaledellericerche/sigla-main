<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*"
%>

<%
	SimpleCRUDBP bp = (SimpleCRUDBP)BusinessProcess.getBusinessProcess(request);
%>

<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title>Firme</title>
</head>

<body class="Form">
<%
	bp.openFormWindow(pageContext);
%>

	<table class="Panel">
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"esercizio");%>
			</TD><TD>
			<% bp.getController().writeFormInput(out,"esercizio");%>
		</TD></TR>
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"tipo");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"tipo");%>
		</TD></TR>
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"firma1");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"firma1");%>
		</TD></TR>
				<TR><TD>
			<% bp.getController().writeFormLabel(out,"firma2");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"firma2");%>
		</TD></TR>
				<TR><TD>
			<% bp.getController().writeFormLabel(out,"firma3");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"firma3");%>
		</TD></TR>
				<TR><TD>
			<% bp.getController().writeFormLabel(out,"firma4");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"firma4");%>
		</TD></TR>
		
	</table>

<%	bp.closeFormWindow(pageContext); %>
</body>