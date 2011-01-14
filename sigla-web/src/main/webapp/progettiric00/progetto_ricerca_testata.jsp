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
	ProgettoBulk bulk = (ProgettoBulk)bp.getModel();
%>
        <div class="GroupLabel"><% bp.getController().writeFormInput(out,null,"livello",true,"GroupLabel","style=\"border-style : none; cursor:default;\"");%></div><div class="Group">
	<table class="Panel">	
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
	  <% } %>
	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"tipo_fase");%>
	  	</TD><TD colspan="3">
	  	<% bp.getController().writeFormInput(out,"tipo_fase");%>
	  </TD></TR>
	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"ds_progetto");%>
	  	</TD><TD colspan="3">
	  	<% bp.getController().writeFormInput(out,"ds_progetto");%>
	  </TD></TR>
	  <TR>
	    <TD><% bp.getController().writeFormLabel( out, "find_dipartimento"); %></TD>	
	    <TD colspan="3">
	      <% bp.getController().writeFormInput( out, "cd_dipartimento");
	         bp.getController().writeFormInput( out, "ds_dipartimento");
	         bp.getController().writeFormInput( out, "find_dipartimento");%>
	      <!-- <%  bp.getController().writeFormInput( out, "crea_dipartimento"); %> -->
	    </TD>
	  </TR>          
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
          
	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"note");%>
	  	</TD><TD colspan="3">
	  	<% bp.getController().writeFormInput(out,"note");%>
	  </TD></TR>
	</table>  
	</div> 