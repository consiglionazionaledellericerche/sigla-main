<!-- 
 ?ResourceName "liquid_iva_interf.jsp"
 ?ResourceTimestamp "09/06/05 16.54.00"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.gestiva00.bp.*,
		it.cnr.contab.gestiva00.core.bulk.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Liquidazione Iva Interfaccia</title>
</head>
<body class="Form">

<% SimpleCRUDBP bp = (SimpleCRUDBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext);
	 Liquid_iva_interfBulk liquid_iva = (Liquid_iva_interfBulk)bp.getModel(); %>

	<div class="Group" style="width:100%">
		<table width="100%">
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
					<% bp.getController().writeFormLabel(out,"dt_inizio");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"dt_inizio",false,null,"");%>
				</td>
				<td>
					<% bp.getController().writeFormLabel(out,"dt_fine");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"dt_fine",false,null,"");%>
				</td>
			</tr>			
		</table>
	</div>
	<% if (liquid_iva.isVisualizzaDati()) { %>
	<div class="Group" style="width:100%">
		<table width="100%">
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"cd_unita_organizzativa");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"cd_unita_organizzativa",false,null,"");%>
				</td>
				<td>
					<% bp.getController().writeFormLabel(out,"ti_liquidazione");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"ti_liquidazione",false,null,"");%>
				</td>
			</tr>				
			<tr>				
				<td>
					<% bp.getController().writeFormLabel(out,"iva_debito");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"iva_debito",false,null,"");%>
				</td>
				<td>
					<% bp.getController().writeFormLabel(out,"iva_credito");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"iva_credito",false,null,"");%>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"fl_gia_eleborata");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"fl_gia_eleborata",false,null,"");%>
				</td>
			</tr>	
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"note");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"note",false,null,"");%>
				</td>
			</tr>						
		</table>

	</div>
	<% } %>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html> 
