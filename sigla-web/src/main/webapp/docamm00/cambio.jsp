<%@ page 
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
<title>Cambio </title>
</head>
<body class="Form">

<% CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
   CambioBulk cambio = (CambioBulk)bp.getModel();
	 bp.openFormWindow(pageContext);%>
	<div class="Group">
		<table class="Panel">
			<tr>
				<td><% bp.getController().writeFormLabel( out, "cd_divisa"); %></td>
				<td>
					<% bp.getController().writeFormInput(out,"cd_divisa");%>			
					<% bp.getController().writeFormInput(out,"ds_divisa");%>			
					<% bp.getController().writeFormInput(out,"divisa");%>
				</td>
			</tr>
			<tr>		
			    <td><% bp.getController().writeFormLabel( out, "cambio"); %></td>
				<td><% bp.getController().writeFormInput( out, "cambio"); %></td>
			</tr>
			<tr>		
			    <td><% bp.getController().writeFormLabel( out, "dt_inizio_validita"); %></td>
			    <td><% bp.getController().writeFormInput( out, "dt_inizio_validita"); %></td>
			</tr>
			<tr>
				<td><% bp.getController().writeFormLabel( out, "dt_fine_validita"); %></td>
				<td><% bp.getController().writeFormInput( out, "dt_fine_validita"); %></td>
			</tr>
			<%if (cambio.isCambioDefault() && !bp.isSearching()) {%>
			    <span class="FormLabel" style="color:red">
				Non è possibile modificare la valuta di default!
	     		</span>
	     	<%}%>
		</table>
	</div>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>