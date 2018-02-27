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
<title>Tariffario </title>
</head>
<body class="Form">

<% CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
	TariffarioBulk tariff = (TariffarioBulk)bp.getModel(); %>

	<div class="Group">
		<table class="Panel">
			<tr>
				<td><% bp.getController().writeFormLabel(out,"cd_tariffario");%></td>
				<td><% bp.getController().writeFormInput(out,"cd_tariffario");%></td>
			</tr>
			<tr>
				<td><% bp.getController().writeFormLabel(out,"ds_tariffario");%></td>
				<td><% bp.getController().writeFormInput(out,"ds_tariffario");%></td>
			</tr>
			<tr>
				<td><% bp.getController().writeFormLabel(out,"dt_ini_validita");%></td>
				<td>
					<% bp.getController().writeFormInput(out,"dt_ini_validita");%>
					<% if (tariff!=null && tariff.getDataFineValidita()!=null) { %>
						<%	bp.getController().writeFormLabel(out,"dt_fine_validita");%>
						<%	bp.getController().writeFormInput(out,"dt_fine_validita");%>
					<% } %>
				</td>
			  </tr>

			<tr></tr>
			<tr></tr>
			<tr></tr>
			<tr></tr>
			<tr></tr> 
  	    
			<tr>
				<td><% bp.getController().writeFormLabel(out,"unita_misura");%></td>
				<td><% bp.getController().writeFormInput(out,"unita_misura");%></td>
			</tr>
			<tr>
				<td><% bp.getController().writeFormLabel(out,"cd_voce_iva");%></td>
				<td>
					<% bp.getController().writeFormInput(out,"cd_voce_iva");%>
					<% bp.getController().writeFormInput(out,"ds_voce_iva");%>
					<% bp.getController().writeFormInput(out,"voce_iva");%>
				</td>
			</tr>
			<tr>
				<td><% bp.getController().writeFormLabel(out,"im_tariffario");%></td>
				<td><% bp.getController().writeFormInput(out,"im_tariffario");%></td>
			</tr>
		</table>
	</div>

<% bp.closeFormWindow(pageContext); %>
</body>
</html>