<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.pdg00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Elaborazione Stralcio Mensile</title>
</head>

<body class="Form">
<% ElaboraStralcioMensileStipendiBP bp = (ElaboraStralcioMensileStipendiBP)BusinessProcess.getBusinessProcess(request); 
   bp.openFormWindow(pageContext); %>

	<div class="Group" style="width:100%">

	<fieldset>	
		<table width="100%">
			<tr>
			  	<td colspan="4">
					<% bp.getV_cnr_estrazione_cori().writeHTMLTable(
						pageContext,
						"distinct_estrazione",
						false,
						false,
						false,
						"100%",
						"180px"); %>
				</td>
			</tr>
		</table>
	</fieldset>
	<fieldset>
		<table width="100%">
			<tr>
				<td>
   	    		  <span style="font-weight:bold; font-family:sans-serif; font-size:16px; color:blue">Elaborazione Dati</span>
 				</td> 
 			</tr>
 			<tr>
				<td align="center">
					<% bp.getController().writeFormLabel(out,"mese");%>
					<% bp.getController().writeFormInput(out,null,"mese",false,null,"onChange=\"submitForm('doOnMeseChange')\"");%>
				</td>
 				<td>
				  <% JSPUtils.button(out,"img/refresh24.gif", "<u>E</u>labora<br>Dati","javascript:submitForm('doElaboraStralcioMensile')", bp.getParentRoot().isBootstrap()); %>
				</td>
				<!--  
				<td>	
				  <% JSPUtils.button(out,"img/delete24.gif", "<u>A</u>nnulla<br>Elaborazione","javascript:submitForm('doReset')", bp.getParentRoot().isBootstrap()); %>
				</td>
				-->
			</tr>
		</table>			
	</fieldset>
	</div>
	
<%bp.closeFormWindow(pageContext); %>
</body>
</html> 