<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.compensi00.bp.*,		
		it.cnr.contab.compensi00.tabrif.bulk.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Esenzioni Addizionali Comunali</title>
</head>
<body class="Form">

<% 	SimpleCRUDBP bp = (SimpleCRUDBP)BusinessProcess.getBusinessProcess(request);
 	bp.openFormWindow(pageContext);
 	Esenzioni_addcomBulk esenzioni = (Esenzioni_addcomBulk)bp.getModel(); %>

<table class="Panel">
	<tr>
			<td>
				<% bp.getController().writeFormLabel(out,"find_comune"); %>
			</td>
			<td colspan="3">
				<% bp.getController().writeFormInput(out,"cd_catastale"); %>
				<% bp.getController().writeFormInput(out,"pg_comune"); %>
				<% bp.getController().writeFormInput(out,"ds_comune"); %>
				<% bp.getController().writeFormInput(out,"find_comune"); %>
			</td>
		</tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dt_inizio_validita"); %></td>
	<td><% bp.getController().writeFormInput(out,"dt_inizio_validita"); %></td>
	 </tr>
  <tr>
	
		<td><% bp.getController().writeFormLabel(out,"dataFineValidita"); %></td>
		<td><% bp.getController().writeFormInput(out,"dataFineValidita"); %></td>
	
  </tr>

  <tr>
	<td><% bp.getController().writeFormLabel(out,"importo"); %></td>
	<td><% bp.getController().writeFormInput(out,"importo"); %></td>
  </tr>			
</table>


<% bp.closeFormWindow(pageContext); %>
</body>
</html>