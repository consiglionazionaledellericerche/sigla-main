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
<title>Apertura/Chiusura Inventario</title>
</head>
<body class="Form"> 

<% CRUDInventarioApChBP bp = (CRUDInventarioApChBP)BusinessProcess.getBusinessProcess(request);
  bp.openFormWindow(pageContext); %>

  <table class="Group" style="width:100%">
	<tr>
	  <td>
	  	<% bp.getController().writeFormLabel(out,"pg_inventario");%>
	  </td>
	  <td>
	  	<% bp.getController().writeFormInput(out,"pg_inventario");%>
	  </td>
	  <td colspan=2>
	  	<% bp.getController().writeFormInput(out,"ds_inventario");%>
	  </td>
	</tr>
	<tr>
	  <td>
	  	<% bp.getController().writeFormLabel(out,"dt_apertura");%>
	  </td>
	  <td>
	  	<% bp.getController().writeFormInput(out,null,"dt_apertura",bp.isEditing(),null,null);%>
	  </td>
	  <td align="right">
	  	<% bp.getController().writeFormLabel(out,"dataChiusura");%>
	  </td>
	  <td>
	  	<% bp.getController().writeFormInput(out,null,"dataChiusura",bp.isEditing(),null,null);%>
	  </td>
	</tr>
	<tr>
	  <td><% bp.getController().writeFormLabel(out,"nr_bene_iniziale");%></td>
	  <td><% bp.getController().writeFormInput(out,null,"nr_bene_iniziale",(bp.isInventarioRO() || bp.isEditing()),null,null);%></td>
	</tr>
  </table>

  <table class="Group" style="width:100%">

	<tr>
    	<td colspan=4>
		  <span class="FormLabel" style="color:blue">Consegnatario Attuale</span>
		</td>
    </tr>  
	<tr>
	  <td><% bp.getController().writeFormLabel(out,"cd_consegnatario");%></td>
	  <td><% bp.getController().writeFormInput(out,null,"cd_consegnatario",true,null,null);%></td>
	  <td colspan=2><% bp.getController().writeFormInput(out,"ds_consegnatario");%></td>
	</tr>
	<tr>
	  <td><% bp.getController().writeFormLabel(out,"cd_delegato");%></td>
	  <td><% bp.getController().writeFormInput(out,"cd_delegato");%></td>
	  <td colspan=2><% bp.getController().writeFormInput(out,"ds_delegato");%></td>
	</tr>

	<tr>
	  <td><% bp.getController().writeFormLabel(out,"dt_inizio_validita");%></td>
	  <td><% bp.getController().writeFormInput(out,null,"dt_inizio_validita",true,null,null);%></td>
	  <td colspan=2>
	  	<% bp.getController().writeFormLabel(out,"dt_fine_validita");%>
	  	<% bp.getController().writeFormInput(out,"dt_fine_validita");%>
	  </td>	
	</tr>
  </table>
 
<% bp.closeFormWindow(pageContext); %>
</body>
</html>