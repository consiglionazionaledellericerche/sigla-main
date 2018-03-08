<%@ page pageEncoding="UTF-8"
		import="it.cnr.jada.util.jsp.*,
	        it.cnr.jada.action.*,
	        java.util.*, 
	        it.cnr.jada.util.action.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Dipartimento</title>
</head>
<body class="Form">

<% CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>
<div class="Group" style="width:100%">
	<table class="Panel">
	<tr>
	<td><% bp.getController().writeFormLabel( out, "cd_dipartimento"); %></td>	
	<td><% bp.getController().writeFormInputByStatus( out, "cd_dipartimento"); %></td>
	</tr>
    <tr>
	<td><% bp.getController().writeFormLabel( out, "ds_dipartimento"); %></td>
	<td><% bp.getController().writeFormInputByStatus( out, "ds_dipartimento"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "dt_istituzione"); %></td>	
	<td><% bp.getController().writeFormInput( out, "dt_istituzione"); %></td>
	</tr>
	<tr>
    <td><% bp.getController().writeFormLabel( out, "ds_del_ist"); %></td>
	<td><% bp.getController().writeFormInput( out, "ds_del_ist"); %></td>
	</tr>	
	<tr>
	<td><% bp.getController().writeFormLabel( out, "cd_terzo"); %></td>	
	<td>
	  <%
	     bp.getController().writeFormInput( out, "cd_terzo"); 
         bp.getController().writeFormInput( out, "ds_direttore"); 
         bp.getController().writeFormInput( out, "find_direttore");
	     bp.getController().writeFormInput( out, "crea_direttore");		 
      %>
	</td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "dt_soppressione"); %></td>	
	<td><% bp.getController().writeFormInput( out, "dt_soppressione"); %></td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "ds_del_soppr"); %></td>	
		<td><% bp.getController().writeFormInput( out, "ds_del_soppr"); %></td>
	</tr>
	</table>
</div>
<%	bp.closeFormWindow(pageContext); %>
</body>