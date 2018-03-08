<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.docamm00.consultazioni.bulk.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Liquidazioni CORI non effettuate dagli Istituti</title>
</head>
<body class="Form">

<% 
	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
%>

	<div class="Group" style="width:100%">
		<table width="100%">
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"dt_da");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"dt_da",false,null,"");%>
				</td>
				<td>
					<% bp.getController().writeFormLabel(out,"dt_a");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"dt_a",false,null,"");%>
				</td>
			</tr>
		</table>
	</div>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html> 
