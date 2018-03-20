<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	it.cnr.jada.action.*,
	java.util.*,
	it.cnr.jada.util.action.*,
	it.cnr.contab.doccont00.bp.*,
	it.cnr.contab.inventario00.docs.bulk.Stampa_registro_inventarioVBulk"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Stampa Registro Inventario</title>
</head>
<body class="Form"> 

<%	it.cnr.contab.inventario00.bp.StampaRegistroInventarioBP bp = (it.cnr.contab.inventario00.bp.StampaRegistroInventarioBP)BusinessProcess.getBusinessProcess(request);
	Stampa_registro_inventarioVBulk bulk = (Stampa_registro_inventarioVBulk)bp.getModel();
	bp.openFormWindow(pageContext); %>

<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_cds"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_cds"); %></td>
	<td></td>
	<td></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findUoForPrint"); %></td>
	<td colspan=5>
		<% bp.getController().writeFormInput(out,null,"cdUoForPrint",(bulk!=null?!bulk.isUOForPrintEnabled():false),null,null); %>
		<% bp.getController().writeFormInput(out,"dsUoForPrint"); %>
		<% bp.getController().writeFormInput(out,null,"findUoForPrint",(bulk!=null?!bulk.isUOForPrintEnabled():false),null,null); %>
	</td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"find_categoria"); %></td>
	<td colspan=5>
      	<% bp.getController().writeFormInput(out,null,"cd_categoria",(bulk ==null) ||bulk.getFl_ufficiale().booleanValue(),null,null); %>
      	<% bp.getController().writeFormInput(out,null,"ds_categoria",(bulk ==null) ||bulk.getFl_ufficiale().booleanValue(),null,null); %>
      	<% bp.getController().writeFormInput(out,null,"find_categoria",(bulk ==null) ||bulk.getFl_ufficiale().booleanValue(),null,null); %>
	</td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"find_gruppo"); %></td>
	<td colspan=5>
		<% bp.getController().writeFormInput(out,null,"cd_gruppo",(bulk ==null) ||bulk.getFl_ufficiale().booleanValue(),null,null); %>
		<% bp.getController().writeFormInput(out,null,"ds_gruppo",(bulk ==null) ||bulk.getFl_ufficiale().booleanValue(),null,null); %>
		<% bp.getController().writeFormInput(out,null,"find_gruppo",(bulk ==null) ||bulk.getFl_ufficiale().booleanValue(),null,null); %>
	</td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dataInizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"dataInizio"); %></td>
	<td></td>
	<td><% bp.getController().writeFormLabel(out,"dataFine"); %></td>
	<td><% bp.getController().writeFormInput(out,"dataFine"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"nrInventarioFrom"); %></td>
	<td><% bp.getController().writeFormInput(out,null,"nrInventarioFrom",(bulk ==null) ||bulk.getFl_ufficiale().booleanValue(),null,null); %></td>
	<td></td>
	<td><% bp.getController().writeFormLabel(out,"nrInventarioTo"); %></td>
	<td><% bp.getController().writeFormInput(out,null,"nrInventarioTo",(bulk ==null) ||bulk.getFl_ufficiale().booleanValue(),null,null); %></td>
  </tr>
  <tr>
  	<td><% bp.getController().writeFormLabel(out,"tipo_movimento"); %></td>
	<td colspan=5>
		<% bp.getController().writeFormInput(out,null,"tipo_movimento",(bulk ==null) ||bulk.getFl_ufficiale().booleanValue(),null,null); %>
	</td>
  </tr>
  <tr>
  	<td><% bp.getController().writeFormLabel(out,"fl_solo_totali"); %></td>
	<td colspan=5>
		<% bp.getController().writeFormInput(out,null,"fl_solo_totali",(bulk ==null) ||bulk.getFl_ufficiale().booleanValue(),null,null); %>
	</td>
  
  </tr>
  <tr>
  	<td><% bp.getController().writeFormLabel(out,"ti_commerciale_istituzionale"); %></td>
	<td colspan=5>
		<% bp.getController().writeFormInput(out,"ti_commerciale_istituzionale"); %>
	</td>
  
  </tr>
   <tr>
  	<td><% bp.getController().writeFormLabel(out,"fl_ufficiale"); %></td>
	<td colspan=5>
		<% bp.getController().writeFormInput(out,null,"fl_ufficiale",!bp.isUfficiale(),null,"onClick=\"submitForm('doClickFlagUfficiale')\""); %>
	</td>
  
  </tr>
</table>

<% bp.closeFormWindow(pageContext); %>

</body>
</html>