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
<title>Ubicazione Bene</title>
</head>
<body class="Form"> 

<% CRUDUbicazioneBP bp = (CRUDUbicazioneBP)BusinessProcess.getBusinessProcess(request);
  bp.openFormWindow(pageContext); %>

  <table class="Group" style="width:100%">
	<tr>
	  <td>
		<% bp.getController().writeFormLabel(out,"cd_ubicazione_propria");%>
	  </td>
	  <td>
		<% bp.getController().writeFormInput(out,"cd_ubicazione_propria");%>
	  </td>
	</tr>
		<tr>
	  <td>
		<% bp.getController().writeFormLabel(out,"ds_ubicazione_bene");%>
	  </td>
	  <td>
		<% bp.getController().writeFormInput(out,"ds_ubicazione_bene");%>
	  </td>
	</tr>
	<tr>
	  <td>
		<% bp.getController().writeFormLabel(out,"find_nodo_padre");%>
	  </td>
	  <td>
		<% bp.getController().writeFormInput(out,null,"find_nodo_padre",bp.isEditing(),null,null);%>
	  </td>
	</tr>
	<tr>
	  <td>
		<% bp.getController().writeFormLabel(out,"fl_ubicazione_default");%>
	  </td>
	  <td>
		<% bp.getController().writeFormInput(out,"fl_ubicazione_default");%>
	  </td>
	</tr>
  </table>
	
<% bp.closeFormWindow(pageContext); %>
</body>
</html>