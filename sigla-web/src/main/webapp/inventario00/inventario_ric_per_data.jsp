<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	it.cnr.jada.action.*,
	java.util.*,
	it.cnr.contab.inventario00.consultazioni.bulk.*,
	it.cnr.jada.util.action.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Elenco beni- Ricognizione</title>
</head>
<body class="Form"> 

<%	it.cnr.contab.inventario00.bp.ConsInventarioRicBP bp = (it.cnr.contab.inventario00.bp.ConsInventarioRicBP)BusinessProcess.getBusinessProcess(request);
	VInventarioRicognizioneBulk bulk = (VInventarioRicognizioneBulk)bp.getModel();
    bp.openFormWindow(pageContext); %>

<table>
  
  <tr>
	<td><% bp.getController().writeFormLabel(out,"data"); %></td>
	<td><% bp.getController().writeFormInput(out,"data"); %></td>
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
      	<% bp.getController().writeFormInput(out,"cd_categoria"); %>
      	<% bp.getController().writeFormInput(out,"ds_categoria"); %>
      	<% bp.getController().writeFormInput(out,"find_categoria"); %>
	</td>
  </tr>
</table>

<% bp.closeFormWindow(pageContext); %>

</body>
</html>