<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.progettiric00.bp.*,
		it.cnr.contab.progettiric00.core.bulk.*"
%>
<%
	TestataProgettiRicercaBP bp = (TestataProgettiRicercaBP)BusinessProcess.getBusinessProcess(request);
	boolean isFlNuovoPdg = bp.isFlNuovoPdg();
	boolean isFlInformix = bp.isFlInformix();
	ProgettoBulk bulk = (ProgettoBulk)bp.getModel();
%>
	<% if ((bp.getStatus() == bp.INSERT || bp.getStatus() == bp.EDIT || bp.getStatus() == bp.VIEW)){%>
     <div class="GroupLabel">
      	<% if (isFlNuovoPdg) {
			   bp.getController().writeFormInput(out,null,"livello_padre2016",true,"GroupLabel h3 h-100 text-primary","style=\"border-style : none; cursor:default;\"");
		  } else {
			   bp.getController().writeFormInput(out,null,"livello_padre",true,"GroupLabel h3 h-100 text-primary","style=\"border-style : none; cursor:default;\"");
		  } 
		%>
	 </div>
	 <div class="Group">
     <table class="Panel card border-primary p-2 mb-2">
	  <tr>
      	<% if (isFlNuovoPdg) {
				bp.getController().writeFormField(out,"find_nodo_padre_area");
 		   } else {
      	%>      	
		    <td>
	      	<% bp.getController().writeFormLabel(out,"cd_progetto_padre"); %>      	
		    </td>
		    <td colspan="2">
		  	<% bp.getController().writeFormInput(out,"cd_progetto_padre");%>
		  	<% bp.getController().writeFormInput(out,"ds_progetto_padre");%>
		  	<% bp.getController().writeFormInput(out,"find_nodo_padre");%>
		    </td>
		<% } %>	    
	  </tr>	  
	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"dipartimento_padre");%>
	  	</TD><TD colspan="3">
	  	<% bp.getController().writeFormInput(out,"dipartimento_padre");%>
	  </TD></TR>
	  <% if (!isFlNuovoPdg) { %>
		  <TR><TD>
		  	<% bp.getController().writeFormLabel(out,"stato_padre");%>
		  	</TD><TD colspan="3">
		  	<% bp.getController().writeFormInput(out,"stato_padre");%>
		  </TD></TR>         
		  <TR><TD>
		  	<% bp.getController().writeFormLabel(out,"dt_inizio_padre");%>
		  	</TD><TD colspan="3">
		  	<% bp.getController().writeFormInput(out,"dt_inizio_padre");%>
		  </TD></TR>
		<% } %>
		<% if (!isFlInformix) {%>
		  <TR><TD>
		  	<% bp.getController().writeFormLabel(out,"programma_padre");%>
		  	</TD><TD colspan="3">
		  	<% bp.getController().writeFormInput(out,"programma_padre");%>
		  </TD></TR>
	  	<% } %>          
		
	 </table>
	</div>
  <div class="GroupLabel">
      	<% if (isFlNuovoPdg) {
			   bp.getController().writeFormInput(out,null,"livello2016",true,"GroupLabel h3 h-100 text-info","style=\"border-style : none; cursor:default;\"");
		  } else {
			   bp.getController().writeFormInput(out,null,"livello",true,"GroupLabel h3 h-100 text-info","style=\"border-style : none; cursor:default;\"");
		  } 
		%>
     <table class="Panel card border-info p-2">
	  <TR><TD>
      	<% if (isFlNuovoPdg) {
				bp.getController().writeFormLabel(out,"cd_progetto_area");
 		   } else {
				bp.getController().writeFormLabel(out,"cd_progetto");
		   }
      	%>      	
	  	</TD><TD>
	  	<% bp.getController().writeFormInput(out,"cd_progetto");%>
	  </TD></TR>

	  <% if (!isFlNuovoPdg) {%>
		  <TR><TD>
		  	<% bp.getController().writeFormLabel(out,"tipo_fase");%>
		  	</TD><TD colspan="3">
		  	<% bp.getController().writeFormInput(out,"tipo_fase");%>
		  </TD></TR>
	  <% } else {%>
		  <% if (bp.isSearching()) {%>
			  <TR><TD>
			  	<% bp.getController().writeFormLabel(out,"tipoFaseToSearch");%>
			  	</TD><TD colspan="3">
			  	<% bp.getController().writeFormInput(out,"tipoFaseToSearch");%>
			  </TD></TR>
		  <% } else { %>
			  <TR><TD>
			  	<% bp.getController().writeFormLabel(out,"tipo_fase");%>
			  	</TD><TD colspan="3">
			  	<% bp.getController().writeFormInput(out,"fl_previsione");%>
			  	<% bp.getController().writeFormLabel(out,"fl_previsione");%>
			  	<% bp.getController().writeFormInput(out,"fl_gestione");%>
			  	<% bp.getController().writeFormLabel(out,"fl_gestione");%>
			  </TD></TR>
		  <% } %>
	  <% } %>

	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"tipo");%>
	  	</TD><TD colspan="3">
	  	<% bp.getController().writeFormInput(out,"tipo");%>
	  </TD></TR>	  
	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"ds_progetto");%>
	  	</TD><TD colspan="3">
	  	<% bp.getController().writeFormInput(out,"ds_progetto");%>
	  </TD></TR>
	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"unita_organizzativa");%>
	  	</TD><TD colspan="3">
	  	<% bp.getController().writeFormInput(out,"cd_unita_organizzativa");%>
	  	<% bp.getController().writeFormInput(out,"ds_unita_organizzativa");%>
	  </TD></TR>
	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"responsabile");%>
	  	</TD><TD colspan="3">
	  	<% bp.getController().writeFormInput(out,"responsabile");%>
	  </TD></TR>
	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"durata_progetto");%>
	  	</TD><TD colspan="3">
	  	<% bp.getController().writeFormInput(out,"durata_progetto");%>
	  </TD></TR>	  
	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"dt_inizio");%>
	  	</TD><TD colspan="3">
	  	<% bp.getController().writeFormInput(out,"dt_inizio");%>
	  </TD></TR>	  
	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"dt_fine");%>
	  	</TD><TD>
	  	<% bp.getController().writeFormInput(out,"dt_fine");%>
	  	</TD><TD>
	  	<% bp.getController().writeFormLabel(out,"dt_proroga");%>
	  	</TD><TD>
	  	<% bp.getController().writeFormInput(out,"dt_proroga");%>
	  </TD></TR>
	    <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"importo_progetto");%>
	  	</TD><TD>
	  	<% bp.getController().writeFormInput(out,"importo_progetto");%>
	  	</TD><TD>
	  	<% bp.getController().writeFormLabel(out,"importo_divisa");%>
	  	</TD><TD>
	  	<% bp.getController().writeFormInput(out,"importo_divisa");%>
	    </TD></TR>
          
	    <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"divisa");%>
	  	</TD><TD colspan="3">
	  	<% bp.getController().writeFormInput(out,"divisa");%>
	    </TD></TR>

	  <% if (!isFlInformix) {%>
		  <TR><TD>
		  	<% bp.getController().writeFormLabel(out,"find_missione");%>
		  	</TD><TD colspan="3">
	      <% bp.getController().writeFormInput( out, "find_missione");%>
		  </TD></TR>
	  <% } %>  
	  
	  <% if (bp.isFlPrgPianoEconomico()) {%>
	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"fl_piano_economico");%>
	  	</TD><TD colspan="3">
	  	<% bp.getController().writeFormInput(out,"fl_piano_economico");%>
	  </TD></TR>
	  <% } %>  

	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"note");%>
	  	</TD><TD colspan="3">
	  	<% bp.getController().writeFormInput(out,"note");%>
	  </TD></TR>
	  
         </table>
        </div> 
	<%}else{%>
        <div class="GroupLabel">
           	<% if (isFlNuovoPdg) {
				   bp.getController().writeFormInput(out,null,"livello2016",true,"GroupLabel h3 h-100 text-info","style=\"border-style : none; cursor:default;\"");
			  } else {
				   bp.getController().writeFormInput(out,null,"livello",true,"GroupLabel h3 h-100 text-info","style=\"border-style : none; cursor:default;\"");
			  } 
			%>
        </div>
 <div class="Group">
	<table class="Panel card border-info p-2">
	  <TR>
	  	<TD>
	      	<% if (isFlNuovoPdg) {
					bp.getController().writeFormLabel(out,"cd_progetto_area");
	 		   } else {
					bp.getController().writeFormLabel(out,"cd_progetto");
			   }
	      	%>      	
		</TD>
		<TD colspan="3">
	  		<% bp.getController().writeFormInput(out,"cd_progetto");%>
	  	</TD>
	  </TR>
	  <% if (!(bp instanceof TestataProgettiRicercaNuovoBP)){%>
	  <tr>
      	<% if (isFlNuovoPdg) { %>
		  	<TD><% bp.getController().writeFormLabel(out,"find_nodo_padre_area");%></TD>
		  	<TD colspan="3"><% bp.getController().writeFormInput(out,"find_nodo_padre_area");%></TD>
 		<%  } else { %>      	
		  	<TD><% bp.getController().writeFormLabel(out,"cd_progetto_padre");%></TD>
		  	<TD colspan="3"><% bp.getController().writeFormInput(out,"find_nodo_padre");%></TD>
		<% } %>      	
	  </tr>
	  <% } %>
	  <TR>
	  	<TD><% bp.getController().writeFormLabel(out,"tipo_fase");%></TD>
	  	<TD colspan="3"><% bp.getController().writeFormInput(out,"tipo_fase");%></TD>
	  </TR>	  	
	  <TR>
	  	<TD><% bp.getController().writeFormLabel(out,"ds_progetto");%></TD>
	  	<TD colspan="3"><% bp.getController().writeFormInput(out,"ds_progetto");%></TD>
	  </TR>	  	
	  <TR>
	  	<TD><% bp.getController().writeFormLabel(out,"find_dipartimento");%></TD>
	  	<TD colspan="3"><% bp.getController().writeFormInput(out,"find_dipartimento");%></TD>
	  </TR>	  	
	  <TR>
	  	<TD><% bp.getController().writeFormLabel(out,"unita_organizzativa");%></TD>
	  	<TD colspan="3"><% bp.getController().writeFormInput(out,"unita_organizzativa");%></TD>
	  </TR>
	  <TR>
	  	<TD><% bp.getController().writeFormLabel(out,"responsabile");%></TD>
	  	<TD colspan="3"><% bp.getController().writeFormInput(out,"responsabile");%></TD>
	  </TR>
	  <TR>
	  	<TD><% bp.getController().writeFormLabel(out,"dt_inizio");%></TD>
	  	<TD><% bp.getController().writeFormInput(out,"dt_inizio");%></TD>
	  	<TD colspan="2">
	  		<table>
	  			<tr>
				  	<% bp.getController().writeFormField(out,"dt_fine");%>
	  			</tr>
	  		</table>
	  	</TD>
	  </TR>
	  <TR>
	  	<TD><% bp.getController().writeFormLabel(out,"dt_proroga");%></TD>
	  	<TD colspan="3"><% bp.getController().writeFormInput(out,"dt_proroga");%></TD>
  	  </TR>
	  <% if (bp.getStatus() == bp.SEARCH){%>
	  <TR>
	  	<TD><% bp.getController().writeFormLabel(out,"importo_progetto");%></TD>
	  	<TD><% bp.getController().writeFormInput(out,"importo_progetto");%></TD>
	  	<TD colspan="2">
	  		<table>
	  			<tr>
				  	<% bp.getController().writeFormField(out,"importo_divisa");%>
	  			</tr>
	  		</table>
	  	</TD>
	  </TR>
      <TR>
	  	<TD><% bp.getController().writeFormLabel(out,"divisa");%></TD>
	  	<TD colspan="3"><% bp.getController().writeFormInput(out,"divisa");%></TD>
	  </TR>
      <%}%>
	  <TR>
	  	<TD><% bp.getController().writeFormLabel(out,"durata_progetto");%></TD>
	  	<TD colspan="3"><% bp.getController().writeFormInput(out,"durata_progetto");%></TD>
	  </TR>
	  <TR>
	  	<TD><% bp.getController().writeFormLabel(out,"stato");%></TD>
	  	<TD colspan="3"><% bp.getController().writeFormInput(out,"stato");%></TD>
	  </TR>
	  <% if (!isFlInformix) {%>
	  <TR>
	  	<TD><% bp.getController().writeFormLabel(out,"find_missione");%></TD>
	  	<TD colspan="3"><% bp.getController().writeFormInput(out,"find_missione");%></TD>
	  </TR>
	  <% } %>  
	  <TR>
	  	<TD><% bp.getController().writeFormLabel(out,"note");%></TD>
	  	<TD colspan="3"><% bp.getController().writeFormInput(out,"note");%></TD>
	  </TR>
	</table>  
	</div>
	<%}%>  