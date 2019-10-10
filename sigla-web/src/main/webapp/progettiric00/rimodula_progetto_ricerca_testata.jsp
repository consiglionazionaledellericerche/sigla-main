<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		java.util.Optional,
		it.cnr.contab.progettiric00.bp.*,
		it.cnr.contab.progettiric00.core.bulk.*"
%>

<%
	RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)BusinessProcess.getBusinessProcess(request);
	Progetto_rimodulazioneBulk bulk = (Progetto_rimodulazioneBulk)bp.getModel();
	ProgettoBulk progetto = Optional.ofNullable(bulk).map(Progetto_rimodulazioneBulk::getProgetto).orElse(null);
%>
	<div class="GroupLabel h3 text-primary" style="border-style: none; cursor:default; background-color:initial;">
	Versione
	<%=Optional.ofNullable(bulk).filter(Progetto_rimodulazioneBulk::isStatoProvvisorio).map(el->" Provvisoria").orElse("")%>
	<%=Optional.ofNullable(bulk).filter(Progetto_rimodulazioneBulk::isStatoDefinitivo).map(el->" Definitiva").orElse("")%>
	<%=Optional.ofNullable(bulk).filter(Progetto_rimodulazioneBulk::isStatoValidato).map(el->" Validata").orElse("")%>
	<%=Optional.ofNullable(bulk).filter(Progetto_rimodulazioneBulk::isStatoApprovato).map(el->" Approvata").orElse("")%>
	<%=Optional.ofNullable(bulk).filter(Progetto_rimodulazioneBulk::isStatoRespinto).map(el->" Respinta").orElse("")%>
	<%=bulk.getPg_rimodulazione()!=null?" n."+bulk.getPg_rimodulazione():""%>
	<%=bulk.getPg_gen_rimodulazione()!=null?" - Rimodulazione n."+bulk.getPg_gen_rimodulazione():""%>
	</div>
	<div class="Group">
	<table class="Panel card border-primary p-2 mb-2">
	<% if (bp.isSearching() || Optional.ofNullable(bulk).flatMap(el->Optional.ofNullable(el.getPg_gen_rimodulazione())).isPresent()) {%>
	  <TR><% bp.getController().writeFormField(out,"pg_gen_rimodulazione");%>
	  <% if (Optional.ofNullable(bulk).flatMap(el->Optional.ofNullable(el.getDtStatoDefinitivo())).isPresent()) {%>
    	  <% bp.getController().writeFormField(out,"dtStatoDefinitivo");%>
      <% } %>
      </TR>
	<% } %>
	  <TR>
	    <td><% bp.getController().writeFormLabel(out,"find_progetto");%></td>
	    <td colspan="3"><% bp.getController().writeFormInput(out,"find_progetto");%></td>
	  </TR>
	  <TR><% bp.getController().writeFormField(out,"unita_organizzativa_progetto");%></TR>
	</table>
	</div> 

    <% if (!bp.isSearching() && Optional.ofNullable(progetto).flatMap(el->Optional.ofNullable(el.getOtherField())).isPresent()) { %>
  	 <div class="GroupLabel h3 text-primary" style="border-style: none; cursor:default; background-color:initial;">Dati Contabili</div>
	 <div class="Group">
     <table class="Panel card border-info p-2">
	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"statoOf");%></td>
	  	<td colspan="3">
	      <% if (Optional.ofNullable(progetto).flatMap(el->Optional.ofNullable(el.getOtherField())).filter(Progetto_other_fieldBulk::isStatoChiuso).isPresent()) { %>
	      	<div class="GroupLabel h3 text-danger" style="border-style: none; cursor:default; background-color:initial;color:red">CHIUSO</div>
	      <% } else
		  	bp.getController().writeFormInput( out, "statoOf");
	      %>
	    </td>
	  </tr>	
	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"tipoFinanziamentoOf");%></td>
	  	<td colspan="3"><% bp.getController().writeFormInput( out, "tipoFinanziamentoOf"); %></td>
	  </tr>	  
      <% if (Optional.ofNullable(progetto).filter(ProgettoBulk::isDatePianoEconomicoRequired).isPresent()) { %>
	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"dtInizioRimodulato"+(bulk.isRimodulatoDtInizio()?"Modificato":""));%></td>
	  	<td colspan="3"><% bp.getController().writeFormInput( out, "dtInizioRimodulato"); %></td>
	  </tr>
	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"dtFineRimodulato"+(bulk.isRimodulatoDtFine()?"Modificato":""));%></td>
	  	<td><% bp.getController().writeFormInput(out,"dtFineRimodulato");%></td>
	  	<td><% bp.getController().writeFormLabel(out,"dtProrogaRimodulato"+(bulk.isRimodulatoDtProroga()?"Modificato":""));%></td>
	  	<td><% bp.getController().writeFormInput(out,"dtProrogaRimodulato");%></td>
	  </tr>
	  <% } %> 
	  <tr>
	  	<% bp.getController().writeFormField(out,"imFinanziatoRimodulato"+(bulk.isRimodulatoImportoFinanziato()?"Modificato":""));%>
	  	<% bp.getController().writeFormField(out,"imCofinanziatoRimodulato"+(bulk.isRimodulatoImportoCofinanziato()?"Modificato":""));%>
	  </tr>
     </table>
	 </div>
 <% } %> 	