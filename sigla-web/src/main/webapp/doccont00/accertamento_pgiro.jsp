<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@page import="it.cnr.contab.doccont00.bp.CRUDAccertamentoPGiroBP"%>
<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,it.cnr.contab.doccont00.core.bulk.AccertamentoPGiroBulk"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="JavaScript" src="scripts/disableRightClick.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<% CRUDAccertamentoPGiroBP bp = (CRUDAccertamentoPGiroBP)BusinessProcess.getBusinessProcess(request);
	 AccertamentoPGiroBulk acc = (AccertamentoPGiroBulk) bp.getModel();%>
<% if ( !acc.isResiduo()) { %>		
	<title>Gestione Annotazione d'Entrata su Partita di Giro</title>
<% } else { %>
	<title>Gestione Annotazione Residua d'Entrata su Partita di Giro</title>
<%	} %>	 	   		
<body class="Form">
<%  bp.openFormWindow(pageContext);%>

<div class="Group card p-2">
	<table class="Panel">

	<tr>
	<td>	<% bp.getController().writeFormLabel( out, "esercizio"); %></td>
	<td>	<% bp.getController().writeFormInput( out, "esercizio"); %>
	  		<% if ( !acc.isResiduo()) { %>			
			<% bp.getController().writeFormLabel( out, "dt_cancellazione"); 
	 		   bp.getController().writeFormInput( out, "dt_cancellazione");	%></td>
   	<% } else {} %>	 	   	 		   
	</tr>
	
	<tr>
	<td><% bp.getController().writeFormLabel( out, "unita_organizzativa"); %></td>
	<td><% bp.getController().writeFormInput( out,"default", "unita_organizzativa", false, null,"onchange=\"submitForm('doCambiaUnitaOrganizzativa')\"" ); %>
	</td>		
	</tr>
	
	<tr>
	<td>	<% bp.getController().writeFormLabel( out, "pg_accertamento"); %></td>	
	<td>	<% bp.getController().writeFormInput( out, "pg_accertamento"); %>
			<% bp.getController().writeFormLabel( out, "dt_registrazione"); %>
			<% bp.getController().writeFormInput( out, "dt_registrazione"); %>
			<% bp.getController().writeFormLabel( out, "pg_accertamento_ori_riporto"); %>
			<% bp.getController().writeFormInput( out, "pg_accertamento_ori_riporto"); %>
	</td>
	</tr>
	
	<tr>
	<td>	<% bp.getController().writeFormLabel( out, "ds_accertamento"); %></td>
	<td>	<% bp.getController().writeFormInput( out, "ds_accertamento"); %></td>
	</tr>
	<tr>
		<td>
			<% bp.getController().writeFormLabel( out, "fl_netto_sospeso"); %>
		</td>
		<td>
			<% bp.getController().writeFormInput( out, "fl_netto_sospeso"); %>
		</td>
	</tr>
	</table>
</div>

<div class="Group card p-2">
	<table>	
	<tr>
	<td>	<% bp.getController().writeFormLabel( out, "find_debitore"); %></td>
	<td>	<% bp.getController().writeFormInput( out, "find_debitore"); %></td>
	</tr>
	<tr>
	<td>	<% bp.getController().writeFormLabel( out, "cd_terzo_precedente"); %></td>
	<td>	<% bp.getController().writeFormInput( out, "cd_terzo_precedente"); %></td>
	</tr>

	<tr>
	<td>	<% bp.getController().writeFormLabel( out, "codice_fiscale"); %></td>
	<td>	<% bp.getController().writeFormInput( out, "codice_fiscale"); %>
	        <% bp.getController().writeFormLabel( out, "partita_iva"); %>
	        <% bp.getController().writeFormInput( out, "partita_iva"); %>	        
	</td>
	</tr>
	</table>	
</div>
	
<div class="Group card p-2">
	<table>
	<tr>
	<td>	<% bp.getController().writeFormLabel( out, "im_accertamento"); %></td>
	<td>	<% bp.getController().writeFormInput( out, "im_accertamento"); %></td>
	</tr>
	
	<tr>
		<td>	<% bp.getController().writeFormLabel( out, "find_capitolo"); %></td>
		<td>    <% bp.getController().writeFormInput(out,"default","find_capitolo",acc.isFl_isTronco()&&bp.isEditing(),null,null); %> </td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "dt_scadenza"); %></td>
	<td colspan=2>
		<% bp.getController().writeFormInput( out, "dt_scadenza"); %></td> 
	</tr>
	<% if (bp.isFlNuovaGestionePg()){ %>
        <tr>
            <td><% bp.getController().writeFormLabel( out, "cd_elemento_voce_contr"); %></td>
            <td><% bp.getController().writeFormInput(out,"default","find_elemento_voce_contr",!bp.isInserting(),null,null); %></td>
        </tr>
    <%}%>

	</table>
</div>
<%	bp.closeFormWindow(pageContext); %>
</body>