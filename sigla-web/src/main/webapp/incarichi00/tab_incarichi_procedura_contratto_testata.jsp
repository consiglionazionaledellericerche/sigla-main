<%@ page pageEncoding="UTF-8"
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
	      <div class="Group card">
	      <table width=70%>
			  <tr>
			    <% controller.writeFormField(out,"pg_repertorio");%>
			    <%if (controller.countDetails()==0) {%>
		          	<td>
		          		<%JSPUtils.button(out, 
		          			bp.getParentRoot().isBootstrap()?"fa fa-fw fa-handshake-o text-primary":"img/edit16.gif", 
		          			bp.getParentRoot().isBootstrap()?"fa fa-fw fa-handshake-o text-primary":"img/edit16.gif", 
		          			"Carica Contratto", 
		          			"if (disableDblClick()) javascript:submitForm('doAddToCRUD(main.IncarichiColl)')", 
		          			"btn-secondary btn-outline-secondary btn-title text-primary", 
		          			true, 
		          			bp.getParentRoot().isBootstrap());%>
		          	</td>
	            <% } else if (incarico.getFl_inviato_corte_conti().booleanValue()) {%>
			        <% controller.writeFormField(out,"fl_inviato_corte_conti");%>
	            <% } %>
		      </tr>      
	      </table>
	      </div>
	      </td></tr>
      <% } else {
        if (incarico.getFl_inviato_corte_conti().booleanValue()) { %>
	      <tr><td colspan=4>
	      <div class="Group card">
	      <table>
			  <tr>
		        <% controller.writeFormField(out,"fl_inviato_corte_conti");%>
		      </tr>      
	      </table>
	      </div>
	      </td></tr>
      <% } %>
     <% } %>
<% } %>
	  <% if (incarico!=null && incarico.getFl_inviato_corte_conti().booleanValue()) {%>
	      <tr><td colspan=4>
	      <div class="Group card">
	      <table <%=widthTable%>>
			  <tr>
			    <% controller.writeFormField(out,"dt_stipula");%>
		        <% controller.writeFormField(out,"dt_invio_corte_conti");%>
		      </tr>
			  <tr>
		        <% controller.writeFormField(out,"esito_corte_conti");%>
		        <% controller.writeFormField(out,"dt_efficacia");%>
		      </tr>
	      </table>
	      </div>
	      </td></tr>
	      
	      <tr><td colspan=4>
	      <div class="Group card">
	      <table <%=widthTable%>>
			  <tr>
		        <% controller.writeFormField(out,"dt_inizio_validita");%>
		        <% controller.writeFormField(out,"dt_fine_validita");%>
		      </tr>
			  <tr>
		        <td><% controller.writeFormLabel(out,"dt_proroga");%></td>
				<td><% controller.writeFormInput(out,"default","dt_proroga",bp.isViewing()||!(bp.isUoEnte()||bp.isUtenteAbilitatoFunzioniIncarichi()),null,null); %></td>
			  <% if (bp.isUoEnte()) { %>
		        <% controller.writeFormField(out,"dt_proroga_pagam");%>
			  <% } %>
		      </tr>
	      </table>
	      </div>
	      </td></tr>
      <% } else { %>
	      <tr><td colspan=4>
	      <div class="Group card">
	      <table <%=widthTable%>>
			  <tr>
			    <% controller.writeFormField(out,"dt_stipula");%>
		        <% controller.writeFormField(out,"dt_inizio_validita");%>
		      </tr>      
			  <tr>
		        <% controller.writeFormField(out,"dt_fine_validita");%>
		        <td><% controller.writeFormLabel(out,"dt_proroga");%></td>
				<td><% controller.writeFormInput(out,"default","dt_proroga",bp.isViewing()||!(bp.isUoEnte()||bp.isUtenteAbilitatoFunzioniIncarichi()),null,null); %></td>
		      </tr>
			  <% if (bp.isUoEnte()) { %>
				  <tr>
				  	<% controller.writeFormField(out,"dt_proroga_pagam");%>
				  </tr>
			  <% } %>
	      </table>
	      </div>
	      </td></tr>
	  <% } %>

      <tr><td colspan=4>
      <div class="Group card">
      <table <%=widthTable%>>
	  	  <tr>
			<td><% bp.writeFormLabel(out,"firmatario");%></td>
			<td><% bp.writeFormInput(out,null,"firmatario",true,null,null);%></td>
		  </tr>            
      </table>
      </div>
      </td></tr>

      <tr><td colspan=4>
      <div class="Group card">
      <table <%=widthTable%>>
	  	  <tr>
			<td><% controller.writeFormLabel(out,"id_sede_ace");%></td>
			<td><% controller.writeFormInput(out,null,"id_sede_ace",true,null,null);%></td>
		  </tr>
      </table>
      </div>
      </td></tr>

      <tr><td colspan=4>
      <div class="Group card">
	  <fieldset>
	  <legend class="GroupLabel h3 text-primary">Provvedimento di nomina</legend>
      <table <%=widthTable%>>
		  <tr>
		    <td><% controller.writeFormLabel(out,"cd_provv");%></td>
			<td><% controller.writeFormInput(out,"default","cd_provv",bp.isViewing()||(!bp.isSuperUtente()&&incarico!=null&&incarico.isROIncarico()),null,null); %></td>
		    <td><% controller.writeFormLabel(out,"nr_provv");%></td>
			<td><% controller.writeFormInput(out,"default","nr_provv",bp.isViewing()||(!bp.isSuperUtente()&&incarico!=null&&incarico.isROIncarico()),null,null); %></td>
		    <td><% controller.writeFormLabel(out,"dt_provv");%></td>
			<td><% controller.writeFormInput(out,"default","dt_provv",bp.isViewing()||(!bp.isSuperUtente()&&incarico!=null&&incarico.isROIncarico()),null,null); %></td>
	      </tr>
      </table>
      </fieldset>
      </div>
      </td></tr>
      
      <tr><td colspan=4>
      <div class="Group card"><table <%=widthTable%>>
	  <tr>         
		<% if (!bp.isSearching()) {%>
			<% controller.writeFormField(out,"terzo");%>
		<% } else {%>
			<td><% controller.writeFormLabel(out,"terzoSearch");%></td>
			<td><% controller.writeFormInput(out,"default", "cd_terzoSearch", false,null,null); %>
			    <% controller.writeFormInput(out,"default", "terzoSearch", false,null,null); %></td>
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
	  
      </table>
      </div>
      </td></tr>
	  
	  <%if (incarico!=null && incarico.getDt_inizio_validita()!=null && incarico.getDt_fine_validita()!=null) {%>
	      <tr><td colspan=4>
	      <div class="Group card"><table>
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
