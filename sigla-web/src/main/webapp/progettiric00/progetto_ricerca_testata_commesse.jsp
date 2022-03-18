<%@page import="it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk"%>
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
	TestataProgettiRicercaBP bp = (TestataProgettiRicercaBP)BusinessProcess.getBusinessProcess(request);
	boolean isFlNuovoPdg = bp.isFlNuovoPdg();
	boolean isFlInformix = bp.isFlInformix();
	boolean isROFieldInformix = !bp.isSearching()&&isFlInformix;
	boolean isROField = isROFieldInformix || bp.isROProgettoForStato();
	boolean isUoEnte = Optional.ofNullable(bp.getUoScrivania()).filter(Unita_organizzativaBulk::isUoEnte).isPresent();
	ProgettoBulk bulk = (ProgettoBulk)bp.getModel();
	boolean isROImporti = !(bp instanceof AmministraTestataProgettiRicercaBP) && Optional.ofNullable(bulk).flatMap(el->Optional.ofNullable(el.getOtherField()))
			.map(el->el.isStatoAnnullato()||el.isStatoChiuso()||(el.isStatoApprovato() && el.isPianoEconomicoRequired()))
			.orElse(Boolean.FALSE);
%>
<% if (bp.getStatus() == bp.INSERT || bp.getStatus() == bp.EDIT || bp.getStatus() == bp.VIEW) {%>
     <div class="GroupLabel">
      	<% if (isFlNuovoPdg) {
			   bp.getController().writeFormInput(out,null,"livello_padre2016",isROField,"GroupLabel h3 text-primary","style=\"border-style : none; cursor:default; background-color:initial;\"");
		  } else {
			   bp.getController().writeFormInput(out,null,"livello_padre",isROField,"GroupLabel h3 text-primary","style=\"border-style : none; cursor:default; background-color:initial;\"");
		  } 
		%>
		<div style="float: right;" class="GroupLabel h3 text-primary ">Ver.<%=bulk.getVersione()%></div>
	 </div>
	 <div class="Group">
	 <table class="Panel card border-primary p-2 mb-2">
	  <tr>
      	<% if (isFlNuovoPdg) { %>
      		<td><% bp.getController().writeFormLabel(out, "find_nodo_padre_area"); %></td>
      		<td><% bp.getController().writeFormInput(out, "default", "find_nodo_padre_area", isROField,null,null); %></td>
 		<% } else { %>      	
		    <td><% bp.getController().writeFormLabel(out,"cd_progetto_padre"); %></td>
		    <td colspan="2">
				<% bp.getController().writeFormInput( out, "default","cd_progetto_padre", isROField,null,null); %>
				<% bp.getController().writeFormInput( out, "default","ds_progetto_padre", isROField,null,null); %>
				<% bp.getController().writeFormInput( out, "default","find_nodo_padre", isROField,null,null); %>
		    </td>
		<% } %>	    
	  </tr>	  

	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"dipartimento_padre");%></td>
	  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","dipartimento_padre", isROField,null,null); %></td>
	  </tr>

	  <% if (!isFlNuovoPdg) { %>
		  <tr>
		  	<td><% bp.getController().writeFormLabel(out,"stato_padre");%></td>
		  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","stato_padre", isROField,null,null); %></td>
		  </tr>         
		  <tr>
		  	<td><% bp.getController().writeFormLabel(out,"dt_inizio_padre");%></td>
		  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","dt_inizio_padre", isROField,null,null); %></td>
		  </tr>
	  <% } %>
	  <% if (!isFlInformix) {%>
		  <tr>
		  	<td><% bp.getController().writeFormLabel(out,"programma_padre");%></td>
		  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","programma_padre", isROField,null,null); %></td>
		  </tr>
	  <% } %>          
	 </table>
	 </div>
  	 
  	 <div class="GroupLabel">
      	<% if (isFlNuovoPdg) {
			 bp.getController().writeFormInput(out,null,"livello2016",isROField,"GroupLabel h3 text-primary","style=\"border-style : none; cursor:default; background-color:initial;\"");
		  } else {
			   bp.getController().writeFormInput(out,null,"livello",isROField,"GroupLabel h3 text-primary","style=\"border-style : none; cursor:default; background-color:initial;\"");
		  } 
		%>
	 </div>
	 <div class="Group">
     <table class="Panel card border-info p-2">
	  <tr>
	  	<td>
      		<% if (isFlNuovoPdg) {
				bp.getController().writeFormLabel(out,"cd_progetto_area");
 		   	} else {
				bp.getController().writeFormLabel(out,"cd_progetto");
		   	}
      		%>      	
	  	</td>
	  	<td><% bp.getController().writeFormInput( out, "default","cd_progetto", isROField,null,null); %>
	        (<%=bulk.getPg_progetto()%>)</td>
	  </tr>

	  <% if (!isFlNuovoPdg) {%>
		  <tr>
			<td><% bp.getController().writeFormLabel(out,"tipo_fase");%></td>
			<td colspan="3"><% bp.getController().writeFormInput( out, "default","tipo_fase", isROField,null,null); %></td>
		  </tr>
	  <% } else {%>
		  <% if (bp.isSearching()) {%>
			  <tr>
			  	<td><% bp.getController().writeFormLabel(out,"tipoFaseToSearch");%></td>
			  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","tipoFaseToSearch", isROField,null,null); %></td>
			  </tr>
		  <% } else { %>
			  <tr>
			  	<td><% bp.getController().writeFormLabel(out,"tipo_fase");%></td>
			  	<td colspan="3">
					<% bp.getController().writeFormInput( out, "default","fl_previsione", isROField,null,null); %>
				  	<% bp.getController().writeFormLabel(out,"fl_previsione");%>
					<% bp.getController().writeFormInput( out, "default","fl_gestione", isROField,null,null); %>
				  	<% bp.getController().writeFormLabel(out,"fl_gestione");%>
				</td>
			 </tr>
		  <% } %>
	  <% } %>
	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"tipo");%></td>
	  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","tipo", isROField,null,null); %></td>
	  </tr>	  
	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"ds_progetto");%></td>
	  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","ds_progetto", isROField,null,null); %></td>
	  </tr>
	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"unita_organizzativa");%></td>
	  	<td colspan="3">
	  		<div class="input-group input-group-searchtool w-100 ">
				<% bp.getController().writeFormInput( out, "default","cd_unita_organizzativa", isROField,null,null); %>
				<% bp.getController().writeFormInput( out, "default","ds_unita_organizzativa", isROField,null,null); %>
		  	</div>
	  	</td>
	  </tr>
	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"responsabile");%></td>
	  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","responsabile", isROField,null,null); %></td>
	  </tr>
	  <% if (!isFlInformix) {%>
		  <tr>
		  	<td><% bp.getController().writeFormLabel(out,"find_missione");%></td>
    	  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","find_missione", isROField,null,null); %></td>
		  </tr>
	  <% } %>  
	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"note");%></td>
	  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","note", isROField,null,null); %></td>
	  </tr>
     </table>
	 </div> 

    <% if (Optional.ofNullable(bulk).flatMap(el->Optional.ofNullable(el.getOtherField())).isPresent()) { %>
  	 <div class="GroupLabel h3 text-primary" style="border-style: none; cursor:default; background-color:initial;">Dati Contabili</div>
	 <div class="Group">
     <table class="Panel card border-info p-2">
	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"statoOf");%></td>
	  	<td colspan="3">
	      <% if (Optional.ofNullable(bulk).flatMap(el->Optional.ofNullable(el.getOtherField())).filter(Progetto_other_fieldBulk::isStatoChiuso).isPresent()) { %>
	      	<div class="GroupLabel h3 text-danger" style="border-style: none; cursor:default; background-color:initial;color:red">CHIUSO</div>
	      <% } else
		  	bp.getController().writeFormInput( out, "statoOf");
	      %>
	    </td>
	  </tr>	  
	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"tipoFinanziamentoOf");%></td>
	  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","tipoFinanziamentoOf",bp.isROProgettoForStato(),null,null); %></td>
	  </tr>	  
      <% if (Optional.ofNullable(bulk).filter(ProgettoBulk::isDatePianoEconomicoRequired).isPresent()) { %>
	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"dtInizioOf");%></td>
	  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","dtInizioOf",bp.isRODatiContabili(),null,null); %></td>
	  </tr>
	  <% } %>	  
      <% if (Optional.ofNullable(bulk).filter(ProgettoBulk::isDatePianoEconomicoRequired).isPresent() ||
    		  Optional.ofNullable(bulk).flatMap(el->Optional.ofNullable(el.getOtherField())).map(Progetto_other_fieldBulk::isStatoChiuso).orElse(Boolean.FALSE)) { %>
	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"dtFineOf");%></td>
	  	<td><% bp.getController().writeFormInput(out, "default","dtFineOf",bp.isRODatiContabili(),null,null); %></td>
      <% if (Optional.ofNullable(bulk).filter(ProgettoBulk::isDatePianoEconomicoRequired).isPresent()) { %>
	  	<td><% bp.getController().writeFormLabel(out,"dtProrogaOf");%></td>
		<td><% bp.getController().writeFormInput( out, "default","dtProrogaOf",bp.isRODatiContabili(),null,null); %></td>	  
	  <% } %> 
	  </tr>
	  <% } %> 
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"imFinanziatoOf");%></td>
		<td><% bp.getController().writeFormInput( out, "default","imFinanziatoOf",isROImporti,null,null); %></td>
		<td><% bp.getController().writeFormLabel(out,"imCofinanziatoOf");%></td>
		<td><% bp.getController().writeFormInput( out, "default","imCofinanziatoOf",isROImporti,null,null); %></td>	  
	  </tr>
     </table>
	 </div>
    <% if (isUoEnte ||
    		Optional.ofNullable(bulk).flatMap(el->Optional.ofNullable(el.getOtherField()))
    		.filter(other->Optional.ofNullable(other.getImFideiussione()).isPresent()||
    				Optional.ofNullable(other.getDtInizioFideiussione()).isPresent()||
    				Optional.ofNullable(other.getDtFineFideiussione()).isPresent()||
    				Optional.ofNullable(other.getDtRilascioFideiussione()).isPresent()||
    				Optional.ofNullable(other.getDtSvincoloFideiussione()).isPresent() ||
					Optional.ofNullable(other.getImSvincolatoFideiussione()).isPresent() ||
					Optional.ofNullable(other.getIdEsternoFideiussione()).isPresent())
    		.isPresent()) { %>
    <div class="GroupLabel h3 text-primary" style="border-style: none; cursor:default; background-color:initial;">Fideiussione</div>
	 <div class="Group">
     <table class="Panel card border-info p-2">
	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"imFideiussioneOf");%></td>
		<td><% bp.getController().writeFormInput( out, "default","imFideiussioneOf",!isUoEnte,null,null); %></td>
	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"dtInizioFideiussioneOf");%></td>
		<td><% bp.getController().writeFormInput( out, "default","dtInizioFideiussioneOf",!isUoEnte,null,null); %></td>
	  	<td><% bp.getController().writeFormLabel(out,"dtFineFideiussioneOf");%></td>
		<td><% bp.getController().writeFormInput( out, "default","dtFineFideiussioneOf",!isUoEnte,null,null); %></td>
	  </tr>
	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"dtRilascioFideiussioneOf");%></td>
		<td><% bp.getController().writeFormInput( out, "default","dtRilascioFideiussioneOf",!isUoEnte,null,null); %></td>
	  	<td><% bp.getController().writeFormLabel(out,"idEsternoFideiussioneOf");%></td>
		<td><% bp.getController().writeFormInput( out, "default","idEsternoFideiussioneOf",!isUoEnte,null,null); %></td>
	  </tr>
	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"dtSvincoloFideiussioneOf");%></td>
		<td><% bp.getController().writeFormInput( out, "default","dtSvincoloFideiussioneOf",!isUoEnte,null,null); %></td>
	  	<td><% bp.getController().writeFormLabel(out,"imSvincolatoFideiussioneOf");%></td>
		<td><% bp.getController().writeFormInput( out, "default","imSvincolatoFideiussioneOf",!isUoEnte,null,null); %></td>
	  </tr>	  
     </table>
	</div>
	<% } %> 	 
	<% } %> 
<% } else { %>
     <div class="GroupLabel">
       	<% if (isFlNuovoPdg) {
		   	 bp.getController().writeFormInput(out,null,"livello2016",true,"GroupLabel h3 text-primary","style=\"border-style : none; cursor:default; background-color:initial;\"");
		   } else {
			 bp.getController().writeFormInput(out,null,"livello",true,"GroupLabel h3 text-primary","style=\"border-style : none; cursor:default; background-color:initial;\"");
		   } 
		%>
     </div>
	 <div class="Group">
		<table class="Panel card border-info p-2">
	  		<tr>
	  			<td>
			      	<% if (isFlNuovoPdg) {
							bp.getController().writeFormLabel(out,"cd_progetto_area");
			 		   } else {
							bp.getController().writeFormLabel(out,"cd_progetto");
					   }
			      	%>      	
				</td>
				<td colspan="3">
					<% bp.getController().writeFormInput( out, "default","cd_progetto", isROField,null,null); %>
			  	</td>
	  		</tr>
		    <% if (!(bp instanceof TestataProgettiRicercaNuovoBP)){%>
	  		<tr>
		      	<% if (isFlNuovoPdg) { %>
				  	<td><% bp.getController().writeFormLabel(out,"find_nodo_padre_area");%></td>
				  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","find_nodo_padre_area", isROField,null,null); %></td>
		 		<%  } else { %>      	
				  	<td><% bp.getController().writeFormLabel(out,"cd_progetto_padre");%></td>
				  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","find_nodo_padre", isROField,null,null); %></td>
				<% } %>      	
	  		</tr>
		  	<% } %>
			<tr>
				<td><% bp.getController().writeFormLabel(out,"tipo_fase");%></td>
			  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","tipo_fase", isROField,null,null); %></td>
			</tr>	  	
			<tr>
			  	<td><% bp.getController().writeFormLabel(out,"ds_progetto");%></td>
			  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","ds_progetto", isROField,null,null); %></td>
			</tr>	  	
			<tr>
			  	<td><% bp.getController().writeFormLabel(out,"find_dipartimento");%></td>
			  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","find_dipartimento", isROField,null,null); %></td>
			</tr>	  	
			<tr>
			  	<td><% bp.getController().writeFormLabel(out,"unita_organizzativa");%></td>
			  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","unita_organizzativa", isROField,null,null); %></td>
			</tr>
			<tr>
			  	<td><% bp.getController().writeFormLabel(out,"responsabile");%></td>
			  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","responsabile", isROField,null,null); %></td>
			</tr>
			<% if (!isFlInformix) {%>
			<tr>
			  	<td><% bp.getController().writeFormLabel(out,"find_missione");%></td>
			  	<td colspan="3"><% bp.getController().writeFormInput(out,"find_missione");%></td>
			</tr>
			<% } %>  
		  	<tr>
			  	<td><% bp.getController().writeFormLabel(out,"note");%></td>
			  	<td colspan="3"><% bp.getController().writeFormInput(out,"note");%></td>
			</tr>
		</table>
	</div>
	
    <% if (Optional.ofNullable(bulk).flatMap(el->Optional.ofNullable(el.getOtherField())).isPresent()) { %>
	<div class="GroupLabel h3 text-primary" style="border-style:none; cursor:default; background-color:initial;">Dati Contabili</div>
	<div class="Group">
    <table class="Panel card border-info p-2 w-100">
		<tr>
		  	<% bp.getController().writeFormField(out,"statoOf");%>
		</tr>
		<tr>
            <td><% bp.getController().writeFormLabel(out,"tipoFinanziamentoOf");%></td>
		  	<td colspan="4"><% bp.getController().writeFormInput( out, "default","tipoFinanziamentoOf",bp.isROProgettoForStato(),null,null); %></td>
		</tr>	  
		<tr>
			<td><% bp.getController().writeFormLabel(out,"dtInizioOf");%></td>
			<td><% bp.getController().writeFormInput( out, "default","dtInizioOf",bp.isRODatiContabili(),null,null); %></td>
			<td><% bp.getController().writeFormLabel(out,"dtFineOf");%></td>
			<td><% bp.getController().writeFormInput( out, "default","dtFineOf",bp.isRODatiContabili(),null,null); %></td>
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel(out,"dtProrogaOf");%></td>
			<td><% bp.getController().writeFormInput( out, "default","dtProrogaOf",bp.isRODatiContabili(),null,null); %></td>
	  	</tr>
		<tr>
			<td><% bp.getController().writeFormLabel(out,"imFinanziatoOf");%></td>
			<td><% bp.getController().writeFormInput( out, "default","imFinanziatoOf",isROImporti,null,null); %></td>
			<td><% bp.getController().writeFormLabel(out,"imCofinanziatoOf");%></td>
			<td><% bp.getController().writeFormInput( out, "default","imCofinanziatoOf",isROImporti,null,null); %></td>
		</tr>
	</table>
	</div>
    <% if (isUoEnte ||
    		Optional.ofNullable(bulk).flatMap(el->Optional.ofNullable(el.getOtherField()))
    		.filter(other->Optional.ofNullable(other.getImFideiussione()).isPresent()||
    				Optional.ofNullable(other.getDtInizioFideiussione()).isPresent()||
    				Optional.ofNullable(other.getDtFineFideiussione()).isPresent()||
    				Optional.ofNullable(other.getDtRilascioFideiussione()).isPresent()||
    				Optional.ofNullable(other.getDtSvincoloFideiussione()).isPresent() ||
					Optional.ofNullable(other.getImSvincolatoFideiussione()).isPresent() ||
					Optional.ofNullable(other.getIdEsternoFideiussione()).isPresent())
    		.isPresent()) { %>
    <div class="GroupLabel h3 text-primary" style="border-style: none; cursor:default; background-color:initial;">Fideiussione</div>
	 <div class="Group">
     <table class="Panel card border-info p-2">
	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"imFideiussioneOf");%></td>
		<td><% bp.getController().writeFormInput( out, "default","imFideiussioneOf",!isUoEnte,null,null); %></td>
	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"dtInizioFideiussioneOf");%></td>
		<td><% bp.getController().writeFormInput( out, "default","dtInizioFideiussioneOf",!isUoEnte,null,null); %></td>
	  	<td><% bp.getController().writeFormLabel(out,"dtFineFideiussioneOf");%></td>
		<td><% bp.getController().writeFormInput( out, "default","dtFineFideiussioneOf",!isUoEnte,null,null); %></td>
	  </tr>
	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"dtRilascioFideiussioneOf");%></td>
		<td><% bp.getController().writeFormInput( out, "default","dtRilascioFideiussioneOf",!isUoEnte,null,null); %></td>
	  	<td><% bp.getController().writeFormLabel(out,"idEsternoFideiussioneOf");%></td>
		<td><% bp.getController().writeFormInput( out, "default","idEsternoFideiussioneOf",!isUoEnte,null,null); %></td>
	  </tr>
	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"dtSvincoloFideiussioneOf");%></td>
		<td><% bp.getController().writeFormInput( out, "default","dtSvincoloFideiussioneOf",!isUoEnte,null,null); %></td>
	  	<td><% bp.getController().writeFormLabel(out,"imSvincolatoFideiussioneOf");%></td>
		<td><% bp.getController().writeFormInput( out, "default","imSvincolatoFideiussioneOf",!isUoEnte,null,null); %></td>
	  </tr>	  
     </table>
	</div>
	<% } %> 
	<% } %>  
<%}%>  