<!-- 
 ?ResourceName "riepilogo_iva_differita.jsp"
 ?ResourceTimestamp "09/08/01 16.54.00"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.gestiva00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Riepilogo Iva ad esigibilità differita definitiva</title>
</head>
<body class="Form">

<% RiepilogoIvaEsigibilitaDifferitaDefinitivaBP bp = (RiepilogoIvaEsigibilitaDifferitaDefinitivaBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>

	<div class="Group" style="width:100%">
		<table width="100%">
			<tr>     	    
				  	 
		     	<td colspan="3">
			   		<% bp.getController().writeFormInput(out, null, "sezionaliFlagsRadioGroup", false, null, "");%>
		      	</td>
		      </tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"mese");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"mese",false,null,"onChange=\"submitForm('doOnMeseChange')\"");%>
				</td>
			</tr>
			
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"data_da");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"data_da",false,null,"");%>
				</td>
				<td>
					<% bp.getController().writeFormLabel(out,"data_a");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"data_a",false,null,"");%>
				</td>
			</tr>				
			<tr>
				<% bp.getController().writeFormField(out,"pageNumber");%>
			</tr>
		</table>
	</div>
	<div class="Group" style="width:100%">
		<div class="GroupLabel">Stampe già eseguite</div>
		<% bp.getRegistri_stampati().writeHTMLTable(pageContext,"default",false,false,false,"100%","200px"); %>
	</div>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>