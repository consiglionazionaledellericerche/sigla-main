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
	ProgettoBulk bulk = (ProgettoBulk)bp.getModel();
%>
    <div class="GroupLabel">
      	<% if (isFlNuovoPdg) {
			   bp.getController().writeFormInput(out,null,"livello2016",true,"GroupLabel h3 text-primary","style=\"border-style : none; cursor:default;\"");
		  } else {
			   bp.getController().writeFormInput(out,null,"livello",true,"GroupLabel h3 text-primary","style=\"border-style : none; cursor:default;\"");
		  } 
		%>
   	</div>
  	<div class="Group">
	<table class="Panel card border-primary p-2 mb-2">
	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"cd_progetto");%>
	  	</TD><TD>
	  	<% bp.getController().writeFormInput(out,"cd_progetto");%>
	  </TD></TR>
	  <% if (!(bp instanceof TestataProgettiRicercaNuovoBP)){%>
	  <tr>
	    <td>
	  	<% bp.getController().writeFormLabel(out,"cd_progetto_padre");%>
	    </td>
	    <td colspan="3">
	  	<% bp.getController().writeFormInput(out,"cd_progetto_padre");%>
	  	<% bp.getController().writeFormInput(out,"ds_progetto_padre");%>
	  	<% bp.getController().writeFormInput(out,"find_nodo_padre");%>
	    </td>
	  </tr>
	  <% } 
	  	 if (!isFlNuovoPdg) {%>
	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"tipo_fase");%>
	  	</TD><TD colspan="3">
	  	<% bp.getController().writeFormInput(out,"tipo_fase");%>
	  </TD></TR>
		<% } %>
	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"ds_progetto");%>
	  	</TD><TD colspan="3">
	  	<% bp.getController().writeFormInput(out,"ds_progetto");%>
	  </TD></TR>
	  <TR>
	    <% bp.getController().writeFormField( out, "find_dipartimento"); %>	
	  </TR>
	  <% if (!isFlNuovoPdg) { %>
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
		  	<% bp.getController().writeFormLabel(out,"dt_inizio");%>
		  	</TD><TD>
		  	<% bp.getController().writeFormInput(out,"dt_inizio");%>
		  	</TD><TD>
		  	<% bp.getController().writeFormLabel(out,"dt_fine");%>
		  	</TD><TD>
		  	<% bp.getController().writeFormInput(out,"dt_fine");%>
		  </TD></TR>
		  <TR><TD>
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
		  <TR><TD>
		  	<% bp.getController().writeFormLabel(out,"durata_progetto");%>
		  	</TD><TD colspan="3">
		  	<% bp.getController().writeFormInput(out,"durata_progetto");%>
		  </TD></TR>
		  <TR><TD>
		  	<% bp.getController().writeFormLabel(out,"stato");%>
		  	</TD><TD colspan="3">
		  	<% bp.getController().writeFormInput(out,"stato");%>
		  </TD></TR>
	  <% } %>          
	  <% if (!isFlInformix) {%>
		  <TR><TD>
		  	<% bp.getController().writeFormLabel(out,"find_programma");%>
		  	</TD><TD colspan="3">
	      <% bp.getController().writeFormInput( out, "cd_programma");
	         bp.getController().writeFormInput( out, "ds_programma");
	         bp.getController().writeFormInput( out, "find_programma");%>
		  </TD></TR>
	  <% } %>          
	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"note");%>
	  	</TD><TD colspan="3">
	  	<% bp.getController().writeFormInput(out,"note");%>
	  </TD></TR>
	</table>  
	</div> 