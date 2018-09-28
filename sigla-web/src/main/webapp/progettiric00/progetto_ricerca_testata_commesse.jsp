<%@ page pageEncoding="UTF-8"
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
	boolean isROFieldInformix = !bp.isSearching()&&isFlInformix;
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
			<% bp.getController().writeFormInput( out, "default","cd_progetto_padre", isROFieldInformix,null,null); %>
			<% bp.getController().writeFormInput( out, "default","ds_progetto_padre", isROFieldInformix,null,null); %>
			<% bp.getController().writeFormInput( out, "default","find_nodo_padre", isROFieldInformix,null,null); %>
		    </td>
		<% } %>	    
	  </tr>	  
	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"dipartimento_padre");%>
	  	</TD><TD colspan="3">
		<% bp.getController().writeFormInput( out, "default","dipartimento_padre", isROFieldInformix,null,null); %>
	  </TD></TR>
	  <% if (!isFlNuovoPdg) { %>
		  <TR><TD>
		  	<% bp.getController().writeFormLabel(out,"stato_padre");%>
		  	</TD><TD colspan="3">
			<% bp.getController().writeFormInput( out, "default","stato_padre", isROFieldInformix,null,null); %>
		  </TD></TR>         
		  <TR><TD>
		  	<% bp.getController().writeFormLabel(out,"dt_inizio_padre");%>
		  	</TD><TD colspan="3">
			<% bp.getController().writeFormInput( out, "default","dt_inizio_padre", isROFieldInformix,null,null); %>
		  </TD></TR>
		<% } %>
		<% if (!isFlInformix) {%>
		  <TR><TD>
		  	<% bp.getController().writeFormLabel(out,"programma_padre");%>
		  	</TD><TD colspan="3">
			<% bp.getController().writeFormInput( out, "default","programma_padre", isROFieldInformix,null,null); %>
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
		<% bp.getController().writeFormInput( out, "default","cd_progetto", isROFieldInformix,null,null); %>
	  </TD></TR>

	  <% if (!isFlNuovoPdg) {%>
		  <TR><TD>
		  	<% bp.getController().writeFormLabel(out,"tipo_fase");%>
		  	</TD><TD colspan="3">
			<% bp.getController().writeFormInput( out, "default","tipo_fase", isROFieldInformix,null,null); %>
		  </TD></TR>
	  <% } else {%>
		  <% if (bp.isSearching()) {%>
			  <TR><TD>
			  	<% bp.getController().writeFormLabel(out,"tipoFaseToSearch");%>
			  	</TD><TD colspan="3">
				<% bp.getController().writeFormInput( out, "default","tipoFaseToSearch", isROFieldInformix,null,null); %>
			  </TD></TR>
		  <% } else { %>
			  <TR><TD>
			  	<% bp.getController().writeFormLabel(out,"tipo_fase");%>
			  	</TD><TD colspan="3">
				<% bp.getController().writeFormInput( out, "default","fl_previsione", isROFieldInformix,null,null); %>
			  	<% bp.getController().writeFormLabel(out,"fl_previsione");%>
				<% bp.getController().writeFormInput( out, "default","fl_gestione", isROFieldInformix,null,null); %>
			  	<% bp.getController().writeFormLabel(out,"fl_gestione");%>
			  </TD></TR>
		  <% } %>
	  <% } %>

	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"tipo");%>
	  	</TD><TD colspan="3">
		<% bp.getController().writeFormInput( out, "default","tipo", isROFieldInformix,null,null); %>
	  </TD></TR>	  
	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"ds_progetto");%>
	  	</TD><TD colspan="3">
		<% bp.getController().writeFormInput( out, "default","ds_progetto", isROFieldInformix,null,null); %>
	  </TD></TR>
	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"unita_organizzativa");%>
	  	</TD><TD colspan="3">
	  	<div class="input-group input-group-searchtool w-100 ">
		<% bp.getController().writeFormInput( out, "default","cd_unita_organizzativa", isROFieldInformix,null,null); %>
		<% bp.getController().writeFormInput( out, "default","ds_unita_organizzativa", isROFieldInformix,null,null); %>
	  	</div>
	  </TD></TR>
	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"responsabile");%>
	  	</TD><TD colspan="3">
		<% bp.getController().writeFormInput( out, "default","responsabile", isROFieldInformix,null,null); %>
	  </TD></TR>
	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"durata_progetto");%>
	  	</TD><TD colspan="3">
		<% bp.getController().writeFormInput( out, "default","durata_progetto", isROFieldInformix,null,null); %>
	  </TD></TR>	  
	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"tipoFinanziamentoOf");%>
	  	</TD><TD colspan="3">
		<% bp.getController().writeFormInput( out, "tipoFinanziamentoOf"); %>
	  </TD></TR>	  
	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"dtInizioOf");%>
	  	</TD><TD colspan="3">
		<% bp.getController().writeFormInput( out, "dtInizioOf"); %>
	  </TD></TR>	  
	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"dtFineOf");%>
	  	</TD><TD>
		<% bp.getController().writeFormInput( out, "dtFineOf"); %>
	  	</TD><TD>
	  	<% bp.getController().writeFormLabel(out,"dtProrogaOf");%>
	  	</TD><TD>
		<% bp.getController().writeFormInput( out, "dtProrogaOf"); %>
	  </TD></TR>
	    <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"imFinanziatoOf");%>
	  	</TD><TD>
		<% bp.getController().writeFormInput( out, "imFinanziatoOf"); %>
	  	</TD><TD>
	  	<% bp.getController().writeFormLabel(out,"imCofinanziatoOf");%>
	  	</TD><TD>
		<% bp.getController().writeFormInput( out, "imCofinanziatoOf"); %>
	    </TD></TR>
          
	  <% if (!isFlInformix) {%>
		  <TR><TD>
		  	<% bp.getController().writeFormLabel(out,"find_missione");%>
		  	</TD><TD colspan="3">
	      <% bp.getController().writeFormInput( out, "find_missione");%>
		  </TD></TR>
	  <% } %>  

	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"note");%>
	  	</TD><TD colspan="3">
		<% bp.getController().writeFormInput( out, "default","note", isROFieldInformix,null,null); %>
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
			<% bp.getController().writeFormInput( out, "default","cd_progetto", isROFieldInformix,null,null); %>
	  	</TD>
	  </TR>
	  <% if (!(bp instanceof TestataProgettiRicercaNuovoBP)){%>
	  <tr>
      	<% if (isFlNuovoPdg) { %>
		  	<TD><% bp.getController().writeFormLabel(out,"find_nodo_padre_area");%></TD>
		  	<TD colspan="3"><% bp.getController().writeFormInput( out, "default","find_nodo_padre_area", isROFieldInformix,null,null); %></TD>
 		<%  } else { %>      	
		  	<TD><% bp.getController().writeFormLabel(out,"cd_progetto_padre");%></TD>
		  	<TD colspan="3"><% bp.getController().writeFormInput( out, "default","find_nodo_padre", isROFieldInformix,null,null); %></TD>
		<% } %>      	
	  </tr>
	  <% } %>
	  <TR>
	  	<TD><% bp.getController().writeFormLabel(out,"tipo_fase");%></TD>
	  	<TD colspan="3"><% bp.getController().writeFormInput( out, "default","tipo_fase", isROFieldInformix,null,null); %></TD>
	  </TR>	  	
	  <TR>
	  	<TD><% bp.getController().writeFormLabel(out,"ds_progetto");%></TD>
	  	<TD colspan="3"><% bp.getController().writeFormInput( out, "default","ds_progetto", isROFieldInformix,null,null); %></TD>
	  </TR>	  	
	  <TR>
	  	<TD><% bp.getController().writeFormLabel(out,"find_dipartimento");%></TD>
	  	<TD colspan="3"><% bp.getController().writeFormInput( out, "default","find_dipartimento", isROFieldInformix,null,null); %></TD>
	  </TR>	  	
	  <TR>
	  	<TD><% bp.getController().writeFormLabel(out,"unita_organizzativa");%></TD>
	  	<TD colspan="3"><% bp.getController().writeFormInput( out, "default","unita_organizzativa", isROFieldInformix,null,null); %></TD>
	  </TR>
	  <TR>
	  	<TD><% bp.getController().writeFormLabel(out,"responsabile");%></TD>
	  	<TD colspan="3"><% bp.getController().writeFormInput( out, "default","responsabile", isROFieldInformix,null,null); %></TD>
	  </TR>
	  <TR>
	  	<TD><% bp.getController().writeFormLabel(out,"dtInizioOf");%></TD>
	  	<TD colspan="3"><% bp.getController().writeFormInput( out, "dtInizioOf"); %></TD>
	  	<TD colspan="2">
	  		<table>
	  			<tr>
	  				<TD><% bp.getController().writeFormLabel(out,"dtFineOf");%></TD>
	  				<TD><% bp.getController().writeFormInput( out, "dtFineOf"); %></TD>
	  			</tr>
	  		</table>
	  	</TD>
	  </TR>
	  <TR>
	  	<TD><% bp.getController().writeFormLabel(out,"dtProrogaOf");%></TD>
	  	<TD colspan="3"><% bp.getController().writeFormInput( out, "dtProrogaOf"); %></TD>
  	  </TR>
	  <% if (bp.getStatus() == bp.SEARCH){%>
	  <TR>
	  	<TD><% bp.getController().writeFormLabel(out,"imFinanziatoOf");%></TD>
	  	<TD><% bp.getController().writeFormInput(out,"imFinanziatoOf");%></TD>
	  	<TD colspan="2">
	  		<table>
	  			<tr>
				  	<% bp.getController().writeFormField(out,"imCofinanziatoOf");%>
	  			</tr>
	  		</table>
	  	</TD>
	  </TR>
      <%}%>
	  <TR>
	  	<TD><% bp.getController().writeFormLabel(out,"durata_progetto");%></TD>
	  	<TD colspan="3"><% bp.getController().writeFormInput( out, "default","durata_progetto", isROFieldInformix,null,null); %></TD>
	  </TR>
	  <TR>
	  	<TD><% bp.getController().writeFormLabel(out,"stato");%></TD>
	  	<TD colspan="3"><% bp.getController().writeFormInput( out, "default","stato", isROFieldInformix,null,null); %></TD>
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