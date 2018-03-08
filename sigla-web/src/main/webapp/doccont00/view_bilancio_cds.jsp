<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.doccont00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
	<head>
		<script language="JavaScript" src="scripts/util.js"></script>
		<% JSPUtils.printBaseUrl(pageContext);%>
	</head>
	<script language="javascript" src="scripts/css.js"></script>

	<% ViewBilancioCdSBP bp = (ViewBilancioCdSBP)BusinessProcess.getBusinessProcess(request); %>
	<%if ( "E".equals( bp.getTipoGestione())) {%>
		<title>Bilancio Entrate CNR</title>
	<%}else {%>
		<title>Bilancio Spese CNR</title>
	<%}%>		
	<body class="Form">
	<% bp.openFormWindow(pageContext); %>	

<div class="Group">
<table>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "cd_cds"); %></td>
		<td><% bp.getController().writeFormInput( out, "cd_cds"); %></td>
		<td>
<!--		<% bp.getController().writeFormInput( out, "ds_cds"); %>
-->
		</td>
	</tr>
</table>
<table>
	<tr>
		<td>
			<% bp.getVociBilancio().setEnabled( false );
			   if ( "E".equals( bp.getTipoGestione()))
				bp.getVociBilancio().writeHTMLTable(pageContext,"bilancioEntrate",false,false,false,"100%","300px");	 
			   else if ( "S".equals( bp.getTipoGestione()))
				bp.getVociBilancio().writeHTMLTable(pageContext,"bilancioSpese",false,false,false,"100%","300px");	 				  
				  %>
		</td>
	</tr>
</table>	
	<%if ( "E".equals( bp.getTipoGestione())) {%>
	<table>		
		<tr>	
			<td><% bp.getController().writeFormLabel( out, "totAccreditato"); %></td>
			<td><% bp.getController().writeFormInput( out, "totAccreditato"); %></td>
		</tr>
		<tr>	
			<td><% bp.getController().writeFormLabel( out, "totIncassatoMenoAccreditato"); %></td>
			<td><% bp.getController().writeFormInput( out, "totIncassatoMenoAccreditato"); %></td>
		</tr>
		<tr>	
			<td><% bp.getController().writeFormLabel( out, "totAccertatoMenoAccreditato"); %></td>
			<td><% bp.getController().writeFormInput( out, "totAccertatoMenoAccreditato"); %></td>
		</tr>
	</table>	
	<%}%>		
	<%	bp.closeFormWindow(pageContext); %>
</body>