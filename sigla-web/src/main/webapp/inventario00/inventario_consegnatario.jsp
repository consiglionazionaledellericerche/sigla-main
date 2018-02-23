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
<title>Consegnatario Inventario</title>
</head>
<body class="Form"> 

<% CRUDInventarioConsegnatarioBP bp = (CRUDInventarioConsegnatarioBP)BusinessProcess.getBusinessProcess(request);
  bp.openFormWindow(pageContext); %>

  <table class="Group" style="width:100%">
	<tr>
	  <td><% bp.getController().writeFormLabel(out,"pg_inventario");%></td>
	  <td><% bp.getController().writeFormInput(out,"pg_inventario");%></td>
	  <td colspan=2><% bp.getController().writeFormInput(out,"ds_inventario");%></td>
	</tr>
	<tr>
	  <td><% bp.getController().writeFormLabel(out,"cd_consegnatario");%></td>
	  <td><% bp.getController().writeFormInput(out,"cd_consegnatario");%></td>
	  <td colspan=2><% bp.getController().writeFormInput(out,"ds_consegnatario");%></td>
	  <td><% bp.getController().writeFormInput(out,null,"find_consegnatario",bp.isEditing(),null,null);%></td>
	</tr>
	<tr>
	  <td><% bp.getController().writeFormLabel(out,"cd_delegato");%></td>
	  <td><% bp.getController().writeFormInput(out,"cd_delegato");%></td>
	  <td colspan=2><% bp.getController().writeFormInput(out,"ds_delegato");%></td>
 	  <td><% bp.getController().writeFormInput(out,null,"find_delegato",bp.isEditing(),null,null);%></td>
	</tr>
	
	<tr>
	  <td><% bp.getController().writeFormLabel(out,"dt_inizio_validita");%></td>
	  <td><% bp.getController().writeFormInput(out,"dt_inizio_validita");%></td>
	  <td align = "right"><% bp.getController().writeFormLabel(out,"dataFineValidita");%></td>
	  <td><% bp.getController().writeFormInput(out,"dataFineValidita");%></td>
	</tr>

  </table>
	
<% bp.closeFormWindow(pageContext); %>
</body>
</html>