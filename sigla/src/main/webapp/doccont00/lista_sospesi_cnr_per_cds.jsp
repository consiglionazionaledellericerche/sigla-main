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

<% ListaSospesiCNRPerCdsBP bp = (ListaSospesiCNRPerCdsBP)BusinessProcess.getBusinessProcess(request); %>

<title>Ricerca Sospesi CNR</title>

<body class="Form">

<% bp.openFormWindow(pageContext); %>
	<table align="center" class="Panel">
		<tr>
		<td>
		<fieldset>
			<legend class="GroupLabel">Entrata/Spesa</legend>
			<table align="center" class="Panel">
				<tr>
				   <td><% bp.getController().writeFormInput( out, "ti_entrata_spesa"); %></td>		
				</tr>
			</table>
		</fieldset>
		</td>

		<td>
		<fieldset>
			<legend class="GroupLabel">Stato Sospesi</legend>
			<table align="center" class="Panel">
				<tr>
				   <td><% bp.getController().writeFormInput( out, "ricercaSospesiAssegnati"); %></td>		
				   <td><% bp.getController().writeFormLabel( out, "ricercaSospesiAssegnati"); %></td>
				</tr>
				<tr>
				   <td><% bp.getController().writeFormInput( out, "ricercaSospesiInSospesoSelezionati"); %></td>		
				   <td><% bp.getController().writeFormLabel( out, "ricercaSospesiInSospesoSelezionati"); %></td>
				</tr>
				<tr>
				   <td><% bp.getController().writeFormInput( out, "ricercaSospesiInSospeso"); %></td>		
				   <td><% bp.getController().writeFormLabel( out, "ricercaSospesiInSospeso"); %></td>
				</tr>
			</table>
		</fieldset>
		</td>
		</tr>
	</table>
		
	<table align="center" class="Panel">	
		<tr>
			<td align="center">
				<% JSPUtils.button(out,bp.encodePath("img/find24.gif"),bp.encodePath("Ricerca"), 
						"javascript:submitForm('doCercaSospesiCNR')",null, bp.getParentRoot().isBootstrap()); %>
			</td>		
		</tr>
	</table>


<%bp.closeFormWindow(pageContext); %>
</body>
</html>