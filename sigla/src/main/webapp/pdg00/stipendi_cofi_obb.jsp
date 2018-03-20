<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.pdg00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Impegni per Stipendi</title>
</head>

<body class="Form">
<% StipendiCofiObbBP bp = (StipendiCofiObbBP)BusinessProcess.getBusinessProcess(request); 
   bp.openFormWindow(pageContext); %>

	<div class="Group" style="width:100%">
	<fieldset>	
		<table width="100%">
			<tr>
			  	<td colspan="4">
					<% bp.getStipendi_obb().writeHTMLTable(
						pageContext,
						"default",
						true,
						true,
						true,
						"100%",
						"300px"); %>
				</td>
			</tr>
		</table>
		<table>
			<tr>
				<td> <% bp.getStipendi_obb().writeFormLabel(out,"find_obbligazioni"); %> </td>
				<td> <% bp.getStipendi_obb().writeFormInput(out,"find_obbligazioni"); %> </td>
			</tr>	
		</table>	
	</fieldset>
	</div>
<%bp.closeFormWindow(pageContext); %>
</body>
</html> 