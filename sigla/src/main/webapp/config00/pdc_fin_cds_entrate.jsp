<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
		import="it.cnr.jada.util.jsp.*,
	        it.cnr.jada.action.*,
	        java.util.*, 
	        it.cnr.jada.util.action.*,
	        it.cnr.contab.config00.pdcfin.bulk.*,
	        it.cnr.jada.UserContext"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<title>Piano dei conti CDS - Gestione entrate</title>
<body class="Form">

<% CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); 
	 UserContext uc = HttpActionContext.getUserContext(session);
	 Elemento_voceBulk voce = (Elemento_voceBulk)bp.getModel();
%>

	<table class="Panel">
<tr>
	<td colspan=2><CENTER><h3>Gestione Capitolo</h3></CENTER></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "esercizio"); %></td>
	<td><% bp.getController().writeFormInputByStatus( out, "esercizio"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "cd_proprio_elemento"); %></td>	
	<td><% bp.getController().writeFormInputByStatus( out, "cd_proprio_elemento"); %></td>
	</tr>
	<tr>
	<td><span class="FormLabel">Titolo</span></td>	
	<td>		
			<% bp.getController().writeFormInputByStatus( out, "cd_elemento_padre"); %>
			<% bp.getController().writeFormInput( out, "ds_elemento_padre"); %>
			<% bp.getController().writeFormInputByStatus( out, "find_elemento_padre"); %>				
	</td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "cd_elemento_voce"); %></td>	
	<td><% bp.getController().writeFormInput( out, "cd_elemento_voce"); %>
		<% bp.getController().writeFormLabel( out, "fl_partita_giro"); %>
		<% bp.getController().writeFormInput( out, "fl_partita_giro"); %></td>
	</tr>	
	<tr>
	<td><% bp.getController().writeFormLabel( out, "ds_elemento_voce"); %></td>	
	<td><% bp.getController().writeFormInput( out, "ds_elemento_voce"); %></td>
	</tr>
	<tr>
	  <td colspan=2><% bp.getController().writeFormLabel( out, "fl_check_terzo_siope"); %>
	  				<% bp.getController().writeFormInput( out,null,"fl_check_terzo_siope",(voce!=null?!voce.isGestoreOk(uc):false),null,null); %> 
	  </td>
	</tr>	
	</table>

<%	bp.closeFormWindow(pageContext); %>
</body>