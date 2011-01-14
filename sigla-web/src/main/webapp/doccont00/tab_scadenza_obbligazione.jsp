<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page 
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*,it.cnr.contab.doccont00.bp.*"
%>


<%  
		CRUDObbligazioneBP bp = (CRUDObbligazioneBP)BusinessProcess.getBusinessProcess(request);
		it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = (it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk)bp.getModel();
		it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk scadenza = (it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk)bp.getScadenzario().getModel();
%>

	<table border="0" cellspacing="0" cellpadding="2">
	<% if ( bp.isEditingScadenza() && scadenza != null && scadenza.getScadenza_iniziale() != null ) {%>
	<tr>			
	<td><% bp.getScadenzario().writeFormLabel( out, "dt_scadenza"); %></td>
	<td>
    	<% bp.getScadenzario().writeFormInput( out,"default","dt_scadenza",!bp.isEditingScadenza(),"FormInput",null); %>
		<% bp.getScadenzario().writeFormLabel( out, "fl_aggiorna_scad_successiva"); %>
    	<% bp.getScadenzario().writeFormInput( out,"default","fl_aggiorna_scad_successiva",!bp.isEditingScadenza(),"FormInput",null); %>		
	</td>
	</tr>
	<%} 
	
	 else {%> 
	<tr>			
	<td><% bp.getScadenzario().writeFormLabel( out, "dt_scadenza"); %></td>
	<td>
    	<% bp.getScadenzario().writeFormInput( out,"default","dt_scadenza",!bp.isEditingScadenza(),"FormInput",null); %>
    </td>	
	</tr>
	<%}%>
	
	<tr>
	<td><% bp.getScadenzario().writeFormLabel( out, "im_scadenza"); %></td>	
	<td><% bp.getScadenzario().writeFormInput( out,"default","im_scadenza",!bp.isEditingScadenza(),"FormInput",null); %></td>
	</tr>
	<tr>			
	<td><% bp.getScadenzario().writeFormLabel( out, "ds_scadenza"); %></td>
	<td>
    	<% bp.getScadenzario().writeFormInput( out,"default","ds_scadenza",!bp.isEditingScadenza(),"FormInput",null); %>
    </td>	

	</tr>
	<tr>
	<td colspan=2 ALIGN="CENTER">
			<% JSPUtils.button(out,bp.encodePath("img/edit24.gif"),bp.encodePath("img/edit24.gif"), "Modifica","javascript:submitForm('doEditaScadenza')", bp.isEditScadenzaButtonEnabled()); %>
			<% JSPUtils.button(out,bp.encodePath("img/save24.gif"),bp.encodePath("img/save24.gif"), "Conferma","javascript:submitForm('doConfermaScadenza')", bp.isConfermaScadenzaButtonEnabled()); %>
			<% JSPUtils.button(out,bp.encodePath("img/undo24.gif"),bp.encodePath("img/undo24.gif"), "Annulla","javascript:submitForm('doUndoScadenza')", bp.isUndoScadenzaButtonEnabled()); %>				
	</td>
	</tr>
  </table>