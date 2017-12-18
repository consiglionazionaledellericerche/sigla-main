<!-- 
 ?ResourceName "riepilogativi_provvisori_iva.jsp"
 ?ResourceTimestamp "09/08/01 16.54.00"
 ?ResourceEdition "1.0"
-->

<%@ page 
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
<title>Riepilogativi provvisori IVA</title>
</head>
<body class="Form">

<% RiepilogativiProvvisoriIvaBP bp = (RiepilogativiProvvisoriIvaBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>

	<div class="Group card" style="width:100%">
		<table width="100%">
			<tr>
<%/*%>
				<td rowspan = "2">
		 		 	<% bp.getTipi_sezionali().writeHTMLTable(
					pageContext,
					"Tipo sezionale",
					false,
					false,
					false,
					"300px",
					"200px",
					true); %>
				</td>
<%*/%>
			<tr>     	
				<td>
					<% bp.getController().writeFormLabel(out,"tipoSezionaleFlag");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"tipoSezionaleFlag",false,null,"onChange=\"submitForm('doOnTipoChange')\"");%>
				</td>
			</tr>     	
			<tr>     	
		     	<td>
			   		<% bp.getController().writeFormLabel(out, "sezionaliFlagsRadioGroup");%>
		      	</td>      	
		     	<td colspan="3">
			   		<% bp.getController().writeFormInput(out, null, "sezionaliFlagsRadioGroup", false, null, "onClick=\"submitForm('doOnSezionaliFlagsChange')\"");%>
		      	</td>
		     	<td width="100">&nbsp;</td>
		      	<td>
		      		<table>
					      <tr>     	
					   	 </tr>
					      <tr>     	
					   	 </tr>
		      		</table>
		      	</td>
	      </tr>
		  <tr>
<%it.cnr.contab.gestiva00.core.bulk.Riepilogativi_provvisori_ivaVBulk riepilogativo = 
(it.cnr.contab.gestiva00.core.bulk.Riepilogativi_provvisori_ivaVBulk) bp.getModel();
 if (riepilogativo.getSezionaliFlag()==null || riepilogativo.getSezionaliFlag().equals(riepilogativo.SEZIONALI_FLAGS_SEZ)){    %>
				<td>
					<% bp.getController().writeFormLabel(out,"tipo_sezionale");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"tipo_sezionale",false,null,"");%>
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
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>