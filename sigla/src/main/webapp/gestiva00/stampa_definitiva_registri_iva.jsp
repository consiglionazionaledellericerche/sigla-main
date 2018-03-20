<!-- 
 ?ResourceName "stampa_provvisoria_registri_iva.jsp"
 ?ResourceTimestamp "09/08/01 16.54.00"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.gestiva00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Stampa definitiva registri IVA</title>
</head>
<body class="Form">

<% StampaDefinitivaRegistriIvaBP bp = (StampaDefinitivaRegistriIvaBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>

	<div class="Group card" style="width:100%">
		<table width="100%">
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"tipo_sezionale");%>
				</td>
				<td colspan="4">
					<% bp.getController().writeFormInput(out,null,"tipo_sezionale",false,null,"onChange=\"submitForm('doOnTipoSezionaleChange')\"");%>
				</td>
			</tr>
			<tr >
				<td>
					<% bp.getController().writeFormLabel(out,"mese");%>
				</td>
				<td colspan="4">
					<% bp.getController().writeFormInput(out,null,"mese",false,null,"onChange=\"submitForm('doOnMeseChange')\"");%>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"data_da");%>
				</td>
				<td colspan="2">
					<% bp.getController().writeFormInput(out,null,"data_da",false,null,"");%>
				</td>
				<td>
					<% bp.getController().writeFormLabel(out,"data_a");%>&nbsp;
					<% bp.getController().writeFormInput(out,null,"data_a",false,null,"");%>
				</td>
			</tr>			
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"pageNumber");%>
				</td>
				<td colspan="4">
					<% bp.getController().writeFormInput(out,null,"pageNumber",false,null,"");%>
				</td>
			</tr>
		</table>
	</div>
	<div class="GroupLabel text-primary h3">Stampe già eseguite</div>
	<div class="Group card" style="width:100%">
		<% bp.getRegistri_stampati().writeHTMLTable(pageContext,"default",false,false,false,"100%","200px"); %>
	</div>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>