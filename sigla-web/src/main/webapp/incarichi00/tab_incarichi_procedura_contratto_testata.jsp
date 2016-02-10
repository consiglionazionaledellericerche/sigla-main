<%@ page 
	import="it.cnr.jada.util.jsp.*,
	    it.cnr.jada.action.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.incarichi00.bp.*,
		it.cnr.contab.incarichi00.bulk.*"
%>

<%
CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)BusinessProcess.getBusinessProcess(request);
SimpleDetailCRUDController controller = bp.getIncarichiColl();
Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)controller.getModel(); 
Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)bp.getModel(); 
boolean multiContratto = false;
String widthTable="width=100%";
if (bp.getModel()!=null &&
    procedura.getNr_contratti().compareTo(new Integer(1))==1){
	multiContratto = true;
	widthTable="width=70%";
}

%>
<table class="Panel" <%if (multiContratto){%>width=90%<%} else {%>width=100%<%}%>>
<%if (procedura!=null && 
	  procedura.getProcedura_amministrativa()!=null && procedura.getProcedura_amministrativa().getCd_proc_amm()!=null &&
	  procedura.getTipo_attivita()!=null && procedura.getTipo_attivita().getCd_tipo_attivita()!=null &&
	  procedura.getTipo_incarico()!=null && procedura.getTipo_incarico().getCd_tipo_incarico()!=null &&
	  procedura.getTipo_natura()!=null) {%>
	  <%if (!multiContratto) {%>
	      <tr><td colspan=4>
	      <div class="Group"><table width=70%>
		  <tr>
		    <td><% controller.writeFormLabel(out,"pg_repertorio");%></td>
		    <td><% controller.writeFormInput(out,"pg_repertorio");%></td>
		    <%if (controller.countDetails()==0) {%>
	          	<td><%JSPUtils.button(out, "img/edit16.gif", "img/edit16.gif", "Carica Contratto", "if (disableDblClick()) javascript:submitForm('doAddToCRUD(main.IncarichiColl)')", null, true);%></td>
            <% } else if (bp.getIncarichiParametri()==null || bp.getIncarichiParametri().getFl_invio_fp()==null || 
            		  bp.getIncarichiParametri().getFl_invio_fp().equals("Y")) {%>
		        <td><% controller.writeFormLabel(out,"fl_inviato_corte_conti");%></td>
		        <td><% controller.writeFormInput(out,"fl_inviato_corte_conti");%></td>
            <% } %>
	      </tr>      
	      </table></div>
	      </td></tr>
      <% } else { %>
	      <tr><td colspan=4>
	      <div class="Group"><table>
		  <tr>
	        <td><% controller.writeFormField(out,"fl_inviato_corte_conti");%></td>
	      </tr>      
	      </table></div>
	      </td></tr>
      <% } %>
<% } %>      
	  <% if (incarico!=null && incarico.getFl_inviato_corte_conti().booleanValue()) {%>
	      <tr><td colspan=4>
	      <div class="Group"><table <%=widthTable%>>
			  <tr>
			    <td><% controller.writeFormLabel(out,"dt_stipula");%></td>
			    <td><% controller.writeFormInput(out,"dt_stipula");%></td>
		        <td><% controller.writeFormLabel(out,"dt_invio_corte_conti");%></td>
		        <td><% controller.writeFormInput(out,"dt_invio_corte_conti");%></td>
		      </tr>
			  <tr>
		        <td><% controller.writeFormLabel(out,"esito_corte_conti");%></td>
		        <td><% controller.writeFormInput(out,"esito_corte_conti");%></td>
		        <td><% controller.writeFormLabel(out,"dt_efficacia");%></td>
		        <td><% controller.writeFormInput(out,"dt_efficacia");%></td>
		      </tr>
	      </table></div>
	      </td></tr>
	      <tr><td colspan=4>
	      <div class="Group"><table <%=widthTable%>>
			  <tr>
		        <td><% controller.writeFormLabel(out,"dt_inizio_validita");%></td>
		        <td><% controller.writeFormInput(out,"dt_inizio_validita");%></td>
		        <td><% controller.writeFormLabel(out,"dt_fine_validita");%></td>
		        <td><% controller.writeFormInput(out,"dt_fine_validita");%></td>
		      </tr>
			  <tr>
		        <td><% controller.writeFormLabel(out,"dt_proroga");%></td>
				<td><% controller.writeFormInput(out,"default","dt_proroga",bp.isViewing()||!(bp.isUoEnte()||bp.isUtenteAbilitatoFunzioniIncarichi()),"FormInput",null); %></td>
			  <% if (bp.isUoEnte()) { %>
		        <td><% controller.writeFormLabel(out,"dt_proroga_pagam");%></td>
			    <td><% controller.writeFormInput(out,"dt_proroga_pagam");%></td>
			  <% } %>
		      </tr>
	      </table></div>
	      </td></tr>
      <% } else { %>
	      <tr><td colspan=4>
	      <div class="Group"><table <%=widthTable%>>
			  <tr>
			    <td><% controller.writeFormLabel(out,"dt_stipula");%></td>
			    <td><% controller.writeFormInput(out,"dt_stipula");%></td>
		        <td><% controller.writeFormLabel(out,"dt_inizio_validita");%></td>
		        <td><% controller.writeFormInput(out,"dt_inizio_validita");%></td>                	    
		      </tr>      
			  <tr>
		        <td><% controller.writeFormLabel(out,"dt_fine_validita");%></td>
		        <td><% controller.writeFormInput(out,"dt_fine_validita");%></td>        
		        <td><% controller.writeFormLabel(out,"dt_proroga");%></td>
				<td><% controller.writeFormInput(out,"default","dt_proroga",bp.isViewing()||!(bp.isUoEnte()||bp.isUtenteAbilitatoFunzioniIncarichi()),"FormInput",null); %></td>
		      </tr>
			  <% if (bp.isUoEnte()) { %>
				  <tr>
			        <td><% controller.writeFormLabel(out,"dt_proroga_pagam");%></td>
			        <td><% controller.writeFormInput(out,"dt_proroga_pagam");%></td>        
			      </tr>
			  <% } %>
	      </table></div>
	      </td></tr>
	  <% } %>
      <tr><td colspan=4>
      <div class="Group"><table <%=widthTable%>>
  	  <tr>
		<td><% bp.writeFormLabel(out,"firmatario");%></td>
		<td><% bp.writeFormInput(out,"default","cd_firmatario",true,"FormInput",null);%>
		    <% bp.writeFormInput(out,"ds_firmatario");%></td>
	  </tr>            
      </table></div>
      </td></tr>
      <tr><td colspan=4>
      <div class="Group">
	  <fieldset>
	  <legend class="GroupLabel">Provvedimento di nomina</legend>
      <table <%=widthTable%>>
		  <tr>
		    <td><% controller.writeFormLabel(out,"cd_provv");%></td>
			<td><% controller.writeFormInput(out,"default","cd_provv",bp.isViewing()||(!bp.isSuperUtente()&&incarico!=null&&incarico.isROIncarico()),"FormInput",null); %></td>
		    <td><% controller.writeFormLabel(out,"nr_provv");%></td>
			<td><% controller.writeFormInput(out,"default","nr_provv",bp.isViewing()||(!bp.isSuperUtente()&&incarico!=null&&incarico.isROIncarico()),"FormInput",null); %></td>
		    <td><% controller.writeFormLabel(out,"dt_provv");%></td>
			<td><% controller.writeFormInput(out,"default","dt_provv",bp.isViewing()||(!bp.isSuperUtente()&&incarico!=null&&incarico.isROIncarico()),"FormInput",null); %></td>
	      </tr>
      </table>
      </fieldset>
      </div>
      </td></tr>
      <tr><td colspan=4>
      <div class="Group"><table <%=widthTable%>>
	  <tr>         
		<% if (!bp.isSearching()) {%>
			<td><% controller.writeFormLabel(out,"terzo");%></td>
			<td><% controller.writeFormInput(out,"default", "cd_terzo", false,"FormInput",null); %>
			    <% controller.writeFormInput(out,"default", "terzo", false,"FormInput",null); %></td>
		<% } else {%>
			<td><% controller.writeFormLabel(out,"terzoSearch");%></td>
			<td><% controller.writeFormInput(out,"default", "cd_terzoSearch", false,"FormInput",null); %>
			    <% controller.writeFormInput(out,"default", "terzoSearch", false,"FormInput",null); %></td>
		<% } %>
	  </tr>
	  <tr>
		<td><% controller.writeFormLabel(out,"nome");%></td>
		<td><% controller.writeFormInput(out,"nome");%>
		    <% controller.writeFormLabel(out,"cognome");%>
		    <% controller.writeFormInput(out,"cognome");%></td>
	  </tr>
	  <tr>
		<td><% controller.writeFormLabel(out,"ragione_sociale"); %></td>
		<td><% controller.writeFormInput(out,"ragione_sociale"); %></td>
	  </tr>
	  <tr>
		<td><% controller.writeFormLabel(out,"indirizzoTerzo");%></td>
		<td><% controller.writeFormInput(out,"indirizzoTerzo");%></td>
	  </tr>
	  <tr>
		<td><% controller.writeFormLabel(out,"ds_comune");%></td>
		<td><% controller.writeFormInput(out,"ds_comune");%>
		    <% controller.writeFormLabel(out,"ds_provincia");%>
		    <% controller.writeFormInput(out,"ds_provincia");%></td>
	  </tr>
	  <tr>
		<td><% controller.writeFormLabel(out,"codice_fiscale"); %></td>
		<td><% controller.writeFormInput(out,"codice_fiscale"); %>
		    <% controller.writeFormLabel(out,"partita_iva"); %>
		    <% controller.writeFormInput(out,"partita_iva"); %></td>	
	  </tr>
	  
      </table></div>
      </td></tr>
	  <%if (incarico!=null && incarico.getDt_inizio_validita()!=null && incarico.getDt_fine_validita()!=null) {%>
	      <tr><td colspan=4>
	      <div class="Group"><table>
			  <tr>
				<td><% controller.writeFormLabel(out,"ti_istituz_commerc"); %></td>
				<td><% controller.writeFormInput(out,"ti_istituz_commerc");%></td>
			  </tr>
			  <tr>
			 	<td><% controller.writeFormLabel(out,"tipo_rapporto");%></td>
				<td><% controller.writeFormInput(out,"tipo_rapporto"); %></td>
			  </tr>
	      </table></div>
	      </td></tr>
	  <% } %>
	</table>
