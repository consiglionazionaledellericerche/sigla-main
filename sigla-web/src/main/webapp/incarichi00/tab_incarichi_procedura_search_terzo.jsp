<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.contab.incarichi00.bp.*"
%>

<%
CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)BusinessProcess.getBusinessProcess(request);
%>
   <fieldset class="fieldset">
    <legend class="GroupLabel"><% bp.getController().writeFormInput(out,null,"statoText",true,"GroupLabel","style=\"background: #F5F5DC;background-color:transparent;border-style : none; cursor:default;font-size : 16px;\"");%></legend>    
	<table class="Panel">
      <tr><td colspan=4>
      <div class="Group"><table>      
	  <tr>         
		<td><% bp.getController().writeFormLabel(out,"terzoSearch");%></td>
		<td><% bp.getController().writeFormInput(out,"default", "cd_terzoSearch", false,"FormInput",null); %>
		    <% bp.getController().writeFormInput(out,"default", "terzoSearch", false,"FormInput",null); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"nome");%></td>
		<td><% bp.getController().writeFormInput(out,"nome");%>
		    <% bp.getController().writeFormLabel(out,"cognome");%>
		    <% bp.getController().writeFormInput(out,"cognome");%></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragione_sociale"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragione_sociale"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"indirizzoTerzo");%></td>
		<td><% bp.getController().writeFormInput(out,"indirizzoTerzo");%></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ds_comune");%></td>
		<td><% bp.getController().writeFormInput(out,"ds_comune");%>
		    <% bp.getController().writeFormLabel(out,"ds_provincia");%>
		    <% bp.getController().writeFormInput(out,"ds_provincia");%></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"codice_fiscale"); %></td>
		<td><% bp.getController().writeFormInput(out,"codice_fiscale"); %>
		    <% bp.getController().writeFormLabel(out,"partita_iva"); %>
		    <% bp.getController().writeFormInput(out,"partita_iva"); %></td>	
	  </tr>
      </table></div>
      </td></tr>
	</table>
   </fieldset>
  