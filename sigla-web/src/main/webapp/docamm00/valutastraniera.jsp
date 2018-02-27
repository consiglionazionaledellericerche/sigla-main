<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.docamm00.tabrif.bulk.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Valuta Straniera</title>
</head>
<body class="Form">

<% CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>

	<div class="Group">
		<table class="Panel" border="0" cellspacing="0" cellpadding="2">

		   <tr>
				<td>
					<% bp.getController().writeFormField(out,"cd_divisa");%>							
				</td>
		   </tr>
		   <tr>
				<td>
					<% bp.getController().writeFormField(out,"ds_divisa");%>							
				</td>
		   </tr>
		   <tr>
				<td>
					<% bp.getController().writeFormField(out,"precisione");%>							
				</td>
		   </tr>	
		   <tr>
				<td>
				    <% bp.getController().writeFormField(out,"fl_calcola_con_diviso");%>
				</td>
		   </tr>	
		</table>
	</div>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>