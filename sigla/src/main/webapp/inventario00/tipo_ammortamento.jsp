<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.inventario00.tabrif.bulk.*,
		it.cnr.contab.inventario00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Tipo Ammortamento</title>
</head>
<body class="Form">

<% CRUDTipoAmmortamentoBP bp = (CRUDTipoAmmortamentoBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); 
	 Tipo_ammortamentoBulk tipo_ammortamento = (Tipo_ammortamentoBulk)bp.getModel(); %>

	<table class="Group" style="width:100%">
		<tr>
			<td>
				<% bp.getController().writeFormLabel(out,"cd_tipo_ammortamento");%>
			</td>
			<td>
				<% bp.getController().writeFormInput(out,null,"cd_tipo_ammortamento",bp.isEditing(),null,null);%>
			</td>
		</tr>
		<tr>
			<td>
				<% bp.getController().writeFormLabel(out,"ds_tipo_ammortamento");%>
			</td>
			<td>
				<% bp.getController().writeFormInput(out,"ds_tipo_ammortamento");%>
			</td>
		</tr>		
	</table>
	<table class="Group" style="width:100%">
		<tr>
			<td colspan="2">
				<% bp.getController().writeFormLabel(out,"fl_ordinario");%>			
				<% bp.getController().writeFormInput(out,null,"fl_ordinario",bp.isEditing(),null,"onClick=\"submitForm('doChangeTipoAmmortamento(" + Tipo_ammortamentoBulk.TIPO_ORDINARIO + ")')\"");%>
			</td>
		</tr>
		<tr>
			<td>
				<% bp.getController().writeFormLabel(out,"perc_primo_anno_ord");%>
			</td>
			<td>
				<% bp.getController().writeFormInput(out,"perc_primo_anno_ord");%>
			</td>
		</tr>
		<tr>
			<td>
				<% bp.getController().writeFormLabel(out,"perc_successivi_ord");%>
			</td>
			<td>
				<% bp.getController().writeFormInput(out,"perc_successivi_ord");%>
			</td>
		</tr>
	</table>
	<table class="Group" style="width:100%">
		<tr>			
			<td colspan="2">
				<% bp.getController().writeFormLabel(out,"fl_anticipato");%>
				<% bp.getController().writeFormInput(out,null,"fl_anticipato",bp.isEditing(),null,"onClick=\"submitForm('doChangeTipoAmmortamento(" + Tipo_ammortamentoBulk.TIPO_ACCELERATO + ")')\"");%>

			</td>
		</tr>
		<tr>
			<td>
				<% bp.getController().writeFormLabel(out,"perc_primo_anno_ant");%>
			</td>
			<td>
				<% bp.getController().writeFormInput(out,"perc_primo_anno_ant");%>
			</td>
		</tr>
		<tr>
			<td>
				<% bp.getController().writeFormLabel(out,"perc_successivi_ant");%>
			</td>
			<td>
				<% bp.getController().writeFormInput(out,"perc_successivi_ant");%>
			</td>
		</tr>
	</table>
	<table class="Group" style="width:100%">
		<tr>			
			<td colspan="2">
				<% bp.getController().writeFormLabel(out,"fl_altro");%>
				<% bp.getController().writeFormInput(out,null,"fl_altro",bp.isEditing(),null,"onClick=\"submitForm('doChangeTipoAmmortamento(" + Tipo_ammortamentoBulk.TIPO_ALTRO + ")')\"");%>
			</td>	
		</tr>		
		<tr>
			<td>
				<% bp.getController().writeFormLabel(out,"perc_primo_anno_altro");%>
			</td>
			<td>
				<% bp.getController().writeFormInput(out,"perc_primo_anno_altro");%>
			</td>
		</tr>
		<tr>
			<td>
				<% bp.getController().writeFormLabel(out,"perc_successivi_altro");%>
			</td>
			<td>
				<% bp.getController().writeFormInput(out,"perc_successivi_altro");%>
			</td>
		</tr>
	</table>	
	<table>
	  <tr>
		<td colspan="4">
		  <span class="FormLabel" style="color:black">Categorie Gruppo Associate:</span>
		</td>		
	  <tr>
	  <tr>
	  	<td colspan = "4">
		  <% bp.getGruppiController().writeHTMLTable(
				pageContext,
				"default",
				!bp.isSearching(),
				false,
				!bp.isSearching(),
				null,
				"100px",
				true); %>
		</td>
	  </tr>
	</table>	
	<% if (tipo_ammortamento.getAmmortamento_associato() != null) { %>
	<table class="Group" style="width:100%">
		<tr>
			<td>
				<% bp.getController().writeFormLabel(out,"cd_tipo_ammortamento_associato");%>			
				<% bp.getController().writeFormInput(out,null,"cd_tipo_ammortamento_associato",true,null,null);%>
				<% bp.getController().writeFormInput(out,null,"ds_tipo_ammortamento_associato",true,null,null);%>
			</td>
		</tr>
	</table>
	<% } %>
<% bp.closeFormWindow(pageContext); %>
</body>
</html>