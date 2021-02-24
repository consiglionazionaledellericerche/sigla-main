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
<title>ID Inventario</title>
</head>
<body class="Form">

<% CRUDIdInventarioBP bp = (CRUDIdInventarioBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>

	<table class="Group" style="width:100%">
		<tr>
			<td><% bp.getController().writeFormLabel(out,"pg_inventario");%></td>
			<td><% bp.getController().writeFormInput(out,"pg_inventario");%></td>
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel(out,"ds_inventario");%></td>
			<td><% bp.getController().writeFormInput(out,"ds_inventario");%></td>
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel(out,"nr_inventario_iniziale");%></td>
			<td><% bp.getController().writeFormInput(out,"nr_inventario_iniziale");%></td>
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel(out,"cd_inventario_origine");%></td>
			<td><% bp.getController().writeFormInput(out,"cd_inventario_origine");%></td>
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel(out,"cd_uo_resp");%></td>
			<td>
				<% bp.getController().writeFormInput(out,"cd_uo_resp");%>
				<% bp.getController().writeFormInput(out,"ds_uo_resp");%>
			</td>
		</tr>
	</table>

	<table class="Group" style="width:100%">
	  <tr>
		<td colspan="2">
		  <span class="FormLabel" style="color:black">U.O. Disponibili:</span>
		</td>
		<td>
		  <span class="FormLabel" style="color:black">U.O. Associate:</span>
		</td>
	  <tr>
	  <tr>
		<td rowspan = "2">
		  <% bp.getUoDisponibili().writeHTMLTable(
				pageContext,
				"scrivania",
				false,
				false,
				false,
				"300px",
				"200px",
				true); %>
		</td>
		<td><% JSPUtils.button(out,"img/doublerightarrow24.gif","img/doublerightarrow24.gif",null,"if (disableDblClick()) submitForm('doAssociaUo')",null,bp.isBottoneAssociaEnabled(), bp.getParentRoot().isBootstrap());%></td>
		<td rowspan = "2">
		  <% bp.getUo().writeHTMLTable(
				pageContext,
				"scrivania",
				false,
				false,
				false,
				"300px",
				"200px",
				true); %>
		</td>
	  </tr>
	  <tr>
		<td><% JSPUtils.button(out,"img/doubleleftarrow24.gif","img/doubleleftarrow24.gif",null,"if (disableDblClick()) submitForm('doRimuoviUo')",null,bp.isBottoneRimuoviEnabled(), bp.getParentRoot().isBootstrap());%></td>
	  </tr>
  	  <tr>
  	    <td colspan="2">&nbsp;</td>
		<td><% JSPUtils.button(out, null, null, "Imposta Uo Responsabile", "if (disableDblClick()) submitForm('doImpostaUoResponsabile')", null, bp.isBottoneImpostaUoEnabled(), bp.getParentRoot().isBootstrap());%></td>
	  </tr>
	</table>


<% bp.closeFormWindow(pageContext); %>
</body>
</html>