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
	ProgettoBulk bulk = (ProgettoBulk)bp.getModel();
%>
<% if (bp.getStatus() == bp.INSERT || bp.getStatus() == bp.EDIT || bp.getStatus() == bp.VIEW) {%>
     <div class="GroupLabel">
      	<% if (isFlNuovoPdg) {
			   bp.getController().writeFormInput(out,null,"livello_padre2016",isROFieldInformix,"GroupLabel h3 h-100 text-primary","style=\"border-style : none; cursor:default; background-color:initial;\"");
		  } else {
			   bp.getController().writeFormInput(out,null,"livello_padre",isROFieldInformix,"GroupLabel h3 h-100 text-primary","style=\"border-style : none; cursor:default; background-color:initial;\"");
		  } 
		%>
	 </div>
	 <div class="Group">
	 <table class="Panel card border-primary p-2 mb-2">
	  <tr>
      	<% if (isFlNuovoPdg) { %>
      		<td><% bp.getController().writeFormLabel(out, "find_nodo_padre_area"); %></td>
      		<td><% bp.getController().writeFormInput(out, "default", "find_nodo_padre_area", isROFieldInformix,null,null); %></td>
 		<% } else { %>      	
		    <td><% bp.getController().writeFormLabel(out,"cd_progetto_padre"); %></td>
		    <td colspan="2">
				<% bp.getController().writeFormInput( out, "default","cd_progetto_padre", isROFieldInformix,null,null); %>
				<% bp.getController().writeFormInput( out, "default","ds_progetto_padre", isROFieldInformix,null,null); %>
				<% bp.getController().writeFormInput( out, "default","find_nodo_padre", isROFieldInformix,null,null); %>
		    </td>
		<% } %>	    
	  </tr>	  

	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"dipartimento_padre");%></td>
	  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","dipartimento_padre", isROFieldInformix,null,null); %></td>
	  </tr>

	  <% if (!isFlNuovoPdg) { %>
		  <tr>
		  	<td><% bp.getController().writeFormLabel(out,"stato_padre");%></td>
		  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","stato_padre", isROFieldInformix,null,null); %></td>
		  </tr>         
		  <tr>
		  	<td><% bp.getController().writeFormLabel(out,"dt_inizio_padre");%></td>
		  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","dt_inizio_padre", isROFieldInformix,null,null); %></td>
		  </tr>
	  <% } %>
	  <% if (!isFlInformix) {%>
		  <tr>
		  	<td><% bp.getController().writeFormLabel(out,"programma_padre");%></td>
		  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","programma_padre", isROFieldInformix,null,null); %></td>
		  </tr>
	  <% } %>          
	 </table>
	 </div>
  	 
  	 <div class="GroupLabel">
      	<% if (isFlNuovoPdg) {
			 bp.getController().writeFormInput(out,null,"livello2016",isROFieldInformix,"GroupLabel h3 h-100 text-primary","style=\"border-style : none; cursor:default; background-color:initial;\"");
		  } else {
			   bp.getController().writeFormInput(out,null,"livello",isROFieldInformix,"GroupLabel h3 h-100 text-primary","style=\"border-style : none; cursor:default; background-color:initial;\"");
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
	  	<td><% bp.getController().writeFormInput( out, "default","cd_progetto", isROFieldInformix,null,null); %></td>
	  </tr>

	  <% if (!isFlNuovoPdg) {%>
		  <tr>
			<td><% bp.getController().writeFormLabel(out,"tipo_fase");%></td>
			<td colspan="3"><% bp.getController().writeFormInput( out, "default","tipo_fase", isROFieldInformix,null,null); %></td>
		  </tr>
	  <% } else {%>
		  <% if (bp.isSearching()) {%>
			  <tr>
			  	<td><% bp.getController().writeFormLabel(out,"tipoFaseToSearch");%></td>
			  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","tipoFaseToSearch", isROFieldInformix,null,null); %></td>
			  </tr>
		  <% } else { %>
			  <tr>
			  	<td><% bp.getController().writeFormLabel(out,"tipo_fase");%></td>
			  	<td colspan="3">
					<% bp.getController().writeFormInput( out, "default","fl_previsione", isROFieldInformix,null,null); %>
				  	<% bp.getController().writeFormLabel(out,"fl_previsione");%>
					<% bp.getController().writeFormInput( out, "default","fl_gestione", isROFieldInformix,null,null); %>
				  	<% bp.getController().writeFormLabel(out,"fl_gestione");%>
				</td>
			 </tr>
		  <% } %>
	  <% } %>
	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"tipo");%></td>
	  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","tipo", isROFieldInformix,null,null); %></td>
	  </tr>	  
	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"ds_progetto");%></td>
	  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","ds_progetto", isROFieldInformix,null,null); %></td>
	  </tr>
	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"unita_organizzativa");%></td>
	  	<td colspan="3">
	  		<div class="input-group input-group-searchtool w-100 ">
				<% bp.getController().writeFormInput( out, "default","cd_unita_organizzativa", isROFieldInformix,null,null); %>
				<% bp.getController().writeFormInput( out, "default","ds_unita_organizzativa", isROFieldInformix,null,null); %>
		  	</div>
	  	</td>
	  </tr>
	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"responsabile");%></td>
	  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","responsabile", isROFieldInformix,null,null); %></td>
	  </tr>
	  <% if (!isFlInformix) {%>
		  <tr>
		  	<td><% bp.getController().writeFormLabel(out,"find_missione");%></td>
		  	<td colspan="3"><% bp.getController().writeFormInput( out, "find_missione");%></td>
		  </tr>
	  <% } %>  
	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"note");%></td>
	  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","note", isROFieldInformix,null,null); %></td>
	  </tr>
     </table>
	 </div> 

    <% if (Optional.ofNullable(bulk).flatMap(el->Optional.ofNullable(el.getOtherField())).isPresent()) { %>
  	 <div class="GroupLabel h3 h-100 text-primary" style="border-style: none; cursor:default; background-color:initial;">Dati Contabili</div>
	 <div class="Group">
     <table class="Panel card border-info p-2">
	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"statoOf");%></td>
	  	<td colspan="3"><% bp.getController().writeFormInput( out, "statoOf"); %></td>
	  </tr>	  
	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"tipoFinanziamentoOf");%></td>
	  	<td colspan="3"><% bp.getController().writeFormInput( out, "tipoFinanziamentoOf"); %></td>
	  </tr>	  
      <% if (Optional.ofNullable(bulk).filter(ProgettoBulk::isDatePianoEconomicoRequired).isPresent()) { %>
	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"dtInizioOf");%></td>
	  	<td colspan="3"><% bp.getController().writeFormInput( out, "dtInizioOf"); %></td>
	  </tr>	  
	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"dtFineOf");%></td>
	  	<td><% bp.getController().writeFormInput( out, "dtFineOf"); %></td>
	  	<td><% bp.getController().writeFormLabel(out,"dtProrogaOf");%></td>
	  	<td><% bp.getController().writeFormInput( out, "dtProrogaOf"); %></td>
	  </tr>
	  <% } %> 
	  <tr>
	  	<td><% bp.getController().writeFormLabel(out,"imFinanziatoOf");%></td>
	  	<td><% bp.getController().writeFormInput( out, "imFinanziatoOf"); %></td>
	  	<td><% bp.getController().writeFormLabel(out,"imCofinanziatoOf");%></td>
	  	<td><% bp.getController().writeFormInput( out, "imCofinanziatoOf"); %></td>
	  </tr>
     </table>
	 </div>
	 <% } %> 
<% } else { %>
     <div class="GroupLabel">
       	<% if (isFlNuovoPdg) {
		   	 bp.getController().writeFormInput(out,null,"livello2016",true,"GroupLabel h3 h-100 text-primary","style=\"border-style : none; cursor:default; background-color:initial;\"");
		   } else {
			 bp.getController().writeFormInput(out,null,"livello",true,"GroupLabel h3 h-100 text-primary","style=\"border-style : none; cursor:default; background-color:initial;\"");
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
				<td colspan="3"><% bp.getController().writeFormInput( out, "default","cd_progetto", isROFieldInformix,null,null); %></td>
	  		</tr>
		    <% if (!(bp instanceof TestataProgettiRicercaNuovoBP)){%>
	  		<tr>
		      	<% if (isFlNuovoPdg) { %>
				  	<td><% bp.getController().writeFormLabel(out,"find_nodo_padre_area");%></td>
				  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","find_nodo_padre_area", isROFieldInformix,null,null); %></td>
		 		<%  } else { %>      	
				  	<td><% bp.getController().writeFormLabel(out,"cd_progetto_padre");%></td>
				  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","find_nodo_padre", isROFieldInformix,null,null); %></td>
				<% } %>      	
	  		</tr>
		  	<% } %>
			<tr>
				<td><% bp.getController().writeFormLabel(out,"tipo_fase");%></td>
			  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","tipo_fase", isROFieldInformix,null,null); %></td>
			</tr>	  	
			<tr>
			  	<td><% bp.getController().writeFormLabel(out,"ds_progetto");%></td>
			  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","ds_progetto", isROFieldInformix,null,null); %></td>
			</tr>	  	
			<tr>
			  	<td><% bp.getController().writeFormLabel(out,"find_dipartimento");%></td>
			  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","find_dipartimento", isROFieldInformix,null,null); %></td>
			</tr>	  	
			<tr>
			  	<td><% bp.getController().writeFormLabel(out,"unita_organizzativa");%></td>
			  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","unita_organizzativa", isROFieldInformix,null,null); %></td>
			</tr>
			<tr>
			  	<td><% bp.getController().writeFormLabel(out,"responsabile");%></td>
			  	<td colspan="3"><% bp.getController().writeFormInput( out, "default","responsabile", isROFieldInformix,null,null); %></td>
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
	<div class="GroupLabel h3 h-100 text-primary" style="border-style:none; cursor:default; background-color:initial;">Dati Contabili</div>
	<div class="Group">
    <table class="Panel card border-info p-2">
		<tr>
		  	<% bp.getController().writeFormField(out,"statoOf");%>
		</tr>
		<tr>
		  	<td colspan="4">
		  		<table>
		  			<tr><% bp.getController().writeFormField( out, "tipoFinanziamentoOf"); %></tr>
		  		</table>
		  	</td>
		</tr>	  
		<tr>
		  	<% bp.getController().writeFormField(out,"dtInizioOf");%>
		  	<% bp.getController().writeFormField(out,"dtFineOf");%>
		</tr>
		<tr>
		  	<% bp.getController().writeFormField(out,"dtProrogaOf");%>
	  	</tr>
		<tr>
		  	<% bp.getController().writeFormField(out,"imFinanziatoOf");%>
		  	<% bp.getController().writeFormField(out,"imCofinanziatoOf");%>
		</tr>
	</table>
	</div>
	<% } %>  
<%}%>  