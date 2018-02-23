<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.utenze00.bp.*"
%>


<%  
		CRUDUtenzaBP bp = (CRUDUtenzaBP)BusinessProcess.getBusinessProcess(request);
%>

<table cellspacing="0" cellpadding="2">
	<tr>
        <td><% bp.getController().writeFormLabel( out, "cd_utente"); %></td>
        <td><% bp.getController().writeFormInputByStatus( out, "cd_utente"); %></td>
	</tr>
	<tr>
        <td><% bp.getController().writeFormLabel( out, "ds_utente"); %></td>
        <td><% bp.getController().writeFormInput( out, "ds_utente"); %></td>
	</tr>
	<tr>
        <td><% bp.getController().writeFormLabel( out, "dt_inizio_validita"); %></td>
        <td><% bp.getController().writeFormInput( out, "dt_inizio_validita"); %></td>
	</tr>
	<tr>
        <td><% bp.getController().writeFormLabel( out, "dt_fine_validita"); %></td>
        <td><% bp.getController().writeFormInput( out, "dt_fine_validita"); %></td>
	</tr>
</table>