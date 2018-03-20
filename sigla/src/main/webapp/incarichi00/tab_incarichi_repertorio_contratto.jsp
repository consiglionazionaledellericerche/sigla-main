<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.contab.incarichi00.bp.*"
%>

<%
CRUDIncarichiRepertorioBP bp = (CRUDIncarichiRepertorioBP)BusinessProcess.getBusinessProcess(request);
%>
   <fieldset class="fieldset">
    <legend class="GroupLabel"><% bp.getController().writeFormInput(out,null,"statoText",true,"GroupLabel","style=\"background: #F5F5DC;background-color:transparent;border-style : none; cursor:default;font-size : 16px;\"");%></legend>    
	<table class="Panel">
      <tr><td colspan=4>
      <div class="Group"><table>            
	  <tr>
         <td><% bp.getController().writeFormLabel(out,"incarichi_procedura");%></td>
         <td colspan=3><% bp.getController().writeFormInput(out,"incarichi_procedura"); %></td>
      </tr>
      </table></div>
      </td></tr>
      <tr><td colspan=4>
      <div class="Group"><table>      
	  <tr>
	    <td><% bp.getController().writeFormLabel(out,"dt_stipula");%></td>
	    <td><% bp.getController().writeFormInput(out,"dt_stipula");%></td>
        <td><% bp.getController().writeFormLabel(out,"dt_inizio_validita");%></td>
        <td><% bp.getController().writeFormInput(out,"dt_inizio_validita");%></td>                	    
      </tr>      
	  <tr>
        <td><% bp.getController().writeFormLabel(out,"dt_fine_validita");%></td>
        <td><% bp.getController().writeFormInput(out,"dt_fine_validita");%></td>        
        <td><% bp.getController().writeFormLabel(out,"dt_proroga");%></td>
		<td><% bp.getController().writeFormInput(out,"default","dt_proroga",bp.isViewing()&&!(bp.isUoEnte()||bp.isUtenteAbilitatoFunzioniIncarichi()),null,null); %></td>
      </tr>
      </table></div>
      </td></tr>
      <tr><td colspan=4>
      <div class="Group"><table>      
	  <tr>         
		<% if (!bp.isSearching()) {%>
			<td><% bp.getController().writeFormLabel(out,"terzo");%></td>
			<td><% bp.getController().writeFormInput(out,"default", "cd_terzo", false,null,null); %>
			    <% bp.getController().writeFormInput(out,"default", "terzo", false,null,null); %></td>
		<% } else {%>
			<td><% bp.getController().writeFormLabel(out,"terzoSearch");%></td>
			<td><% bp.getController().writeFormInput(out,"default", "cd_terzoSearch", false,null,null); %>
			    <% bp.getController().writeFormInput(out,"default", "terzoSearch", false,null,null); %></td>
		<% } %>
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
      <tr><td colspan=4>
      <div class="Group"><table>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ti_istituz_commerc"); %></td>
		<td><% bp.getController().writeFormInput(out,"ti_istituz_commerc"); %></td>
	  </tr>
	  <tr>
	 	<td><% bp.getController().writeFormLabel(out,"tipo_rapporto");%></td>
		<td><% bp.getController().writeFormInput(out,"tipo_rapporto");%></td>
	  </tr>
      </table></div>
      </td></tr>
	</table>
   </fieldset>
	
