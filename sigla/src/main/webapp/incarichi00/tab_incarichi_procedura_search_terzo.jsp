<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.contab.incarichi00.bp.*"
%>

<%
CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)BusinessProcess.getBusinessProcess(request);
%>
   <fieldset class="fieldset">
	<table class="Panel w-100">
      <tr><td colspan=4>
      <div class="Group card"><table>      
	  <tr>         
		<% bp.getController().writeFormField(out,"terzoSearch");%>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"nome");%></td>
		<td><% bp.getController().writeFormInput(out,"nome");%>
		    <% bp.getController().writeFormLabel(out,"cognome");%>
		    <% bp.getController().writeFormInput(out,"cognome");%></td>
	  </tr>
	  <tr>
		<% bp.getController().writeFormField(out,"ragione_sociale"); %>
	  </tr>
	  <tr>
		<% bp.getController().writeFormField(out,"indirizzoTerzo");%>
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
  