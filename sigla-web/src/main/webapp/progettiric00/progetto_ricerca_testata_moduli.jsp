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
	ProgettoBulk bulk = (ProgettoBulk)bp.getModel();
%>
      <div class="GroupLabel"><% bp.getController().writeFormInput(out,null,"livello_padre",true,"GroupLabel","style=\"border-style : none; cursor:default;\"");%></div><div class="Group">
      <table class="Panel">	
	  <tr>
	    <td>
	  	<% bp.getController().writeFormLabel(out,"cd_progetto_padre_commessa");%>
	    </td>
	    <td colspan="2">
	  	<% bp.getController().writeFormInput(out,"cd_progetto_padre_commessa");%>
	  	<% bp.getController().writeFormInput(out,"ds_progetto_padre");%>
	  	<% bp.getController().writeFormInput(out,"find_nodo_padre");%>
	    </td>	    
	  </tr>	  
	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"dipartimento_padre");%>
	  	</TD><TD colspan="3">
	  	<% bp.getController().writeFormInput(out,"dipartimento_padre");%>
	  </TD></TR>
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
	 </table>
	</div>
        <div class="GroupLabel"><% bp.getController().writeFormInput(out,null,"livello",true,"GroupLabel","style=\"border-style : none; cursor:default;\"");%></div><div class="Group">
         <table class="Panel">		  
	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"cd_progetto");%>
	  	</TD><TD>
	  	<% bp.getController().writeFormInput(out,"cd_progetto");%>
	  </TD></TR>
	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"tipo");%>
	  	</TD><TD colspan="3">
	  	<% bp.getController().writeFormInput(out,"tipo");%>
	  </TD></TR>	 
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
	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"unita_organizzativa");%>
	  	</TD><TD colspan="3">
	  	<% bp.getController().writeFormInput(out,"unita_organizzativa");%>
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
	  	<% bp.getController().writeFormLabel(out,"note");%>
	  	</TD><TD colspan="3">
	  	<% bp.getController().writeFormInput(out,"note");%>
	  </TD></TR>
         </table>
        </div> 
