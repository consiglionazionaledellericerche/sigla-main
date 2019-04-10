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
	<div class="Group">
	<table class="Panel card border-primary p-2 mb-2">
	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"find_progetto");%>
	  	</TD><TD>
	  	<% bp.getController().writeFormInput(out,"find_progetto");%>
	  </TD></TR>
	  <TR><% bp.getController().writeFormField(out,"pg_rimodulazione");%></TR>
	  <TR><% bp.getController().writeFormField(out,"stato");%></TR>
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
	  	<td><% bp.getController().writeFormLabel(out,"dtInizioOf");%></td>
	  	<td colspan="3"><% bp.getController().writeFormInput( out, "dtInizioOf"); %></td>
	  </tr>
	  <% } %>	  
      <% if (Optional.ofNullable(progetto).filter(ProgettoBulk::isDatePianoEconomicoRequired).isPresent() ||
    		  Optional.ofNullable(progetto).flatMap(el->Optional.ofNullable(el.getOtherField())).map(Progetto_other_fieldBulk::isStatoChiuso).orElse(Boolean.FALSE)) { %>
	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"dtFineOf");%></td>
	  	<td><% bp.getController().writeFormInput( out, "dtFineOf"); %></td>
      <% if (Optional.ofNullable(progetto).filter(ProgettoBulk::isDatePianoEconomicoRequired).isPresent()) { %>
	  	<td><% bp.getController().writeFormLabel(out,"dtProrogaOf");%></td>
	  	<td><% bp.getController().writeFormInput( out, "dtProrogaOf"); %></td>
	  <% } %> 
	  </tr>
	  <% } %> 
	  <tr>
	  	<% bp.getController().writeFormField(out,"imFinanziatoOf");%>
	  	<% bp.getController().writeFormField(out,"imCofinanziatoOf");%>
	  </tr>
	  <tr>
	  	<% bp.getController().writeFormField(out,"imFinanziatoRimodulato");%>
	  	<% bp.getController().writeFormField(out,"imCofinanziatoRimodulato");%>
	  </tr>
     </table>
	 </div>
	 <% } %> 	