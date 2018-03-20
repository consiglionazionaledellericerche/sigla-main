<!-- 
 ?ResourceName "riepilogativi_iva_centro.jsp"
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
<title>Riepilogativi IVA Ente Provvisori</title>
</head>
<body class="Form">

<% RiepilogativiIvaCentroProvvisorioBP bp = (RiepilogativiIvaCentroProvvisorioBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>

	<div class="Group" style="width:100%">
		<table width="100%">
			<tr>

<%it.cnr.contab.gestiva00.core.bulk.Riepilogativi_iva_centro_provvisorioVBulk riepilogativo = 
(it.cnr.contab.gestiva00.core.bulk.Riepilogativi_iva_centro_provvisorioVBulk) bp.getModel();
 if (riepilogativo.getSezionaliFlag()==null || riepilogativo.getSezionaliFlag().equals(riepilogativo.SEZIONALI_FLAGS_SEZ)){    %>
				<td>
					<% bp.getController().writeFormLabel(out,"tipo_sezionale");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"tipo_sezionale",false,null,"onChange=\"submitForm('doOnTipoSezionaleChange')\"");%>
				</td>
<%}%>
			</tr>
			<br>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"mese");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"mese",false,null,"onChange=\"submitForm('doOnMeseChange')\"");%>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"data_da");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"data_da",false,null,"");%>
				</td>
				<td>
					<% bp.getController().writeFormLabel(out,"data_a");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"data_a",false,null,"");%>
				</td>
			</tr>			
		</table>
	</div>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>