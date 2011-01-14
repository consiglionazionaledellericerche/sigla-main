<%@ page 
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,it.cnr.contab.config00.latt.bulk.*"
%>

<% it.cnr.contab.config00.bp.CRUDWorkpackageBP bp = (it.cnr.contab.config00.bp.CRUDWorkpackageBP)BusinessProcess.getBusinessProcess(request); %>

<table class="Panel">
	<tr>
		<% bp.getController().writeFormField(out,"esercizio_inizio"); %>
	</tr>
	<tr>
		<% bp.getController().writeFormField(out,bp.isInserting() ? "cd_linea_attivita7" : "cd_linea_attivita"); %>
	</tr>
	<tr>
		<% if (!bp.isInserting()) 
			bp.getController().writeFormField(out,"tipo_linea_attivita"); %>
	</tr>
	<tr>
	  <td>
		<% bp.getController().writeFormLabel(out,"cd_progetto_padre");%>
	  </td>
	  <td>
		<% bp.getController().writeFormInput(out,"cd_progetto_padre");%>
		<% bp.getController().writeFormInput(out,"ds_progetto_padre");%>
		<% bp.getController().writeFormInput(out,"find_nodo_progettopadre");%>
	  </td>
	</tr>
	<tr>
	  <td>
		<% bp.getController().writeFormLabel(out,"cd_progetto");%>
	  </td>
	  <td>
		<% bp.getController().writeFormInput(out,"cd_progetto");%>
		<% bp.getController().writeFormInput(out,"ds_progetto");%>
		<% bp.getController().writeFormInput(out,"find_nodo_padre");%>
		<!--<% bp.getController().writeFormInput(out,"crea_modulo");%>-->
	  </td>
	</tr>
	<tr>
		<% bp.getController().writeFormField(out,"centro_responsabilita"); %>
	</tr>
	<TR>
	    <TD>
	  	 <%  bp.getController().writeFormLabel(out,"responsabile");%>
	  	</TD>
	  	<TD colspan="3">
	  	 <% bp.getController().writeFormInput(out,"responsabile");%>
	    </TD>
	</TR>	
	<tr>
		<% bp.getController().writeFormField( out, "insieme_la"); %>
	</tr>
	<tr>
		<td>
		<% bp.getController().writeFormLabel(out,"ti_gestione"); %>
		</td>
		<td>
		<% bp.getController().writeFormInput(out,null,"ti_gestione",false,null,"onclick=\"submitForm('doCambiaGestione')\""); %>
		</td>
	</tr>
	<tr>
		<% WorkpackageBulk workpackage = (WorkpackageBulk)bp.getModel();
		   if (workpackage.getTi_gestione() == null || workpackage.getTi_gestione().equals(workpackage.TI_GESTIONE_SPESE))
			   bp.getController().writeFormField(out,"funzione"); %>
	</tr>
	<tr>
		<% bp.getController().writeFormField(out,"natura"); %>
	</tr>
	<tr>
		<% bp.getController().writeFormField(out,"gruppo_linea_attivita"); %>
	</tr>
	<tr>
		<% bp.getController().writeFormField(out,"denominazione"); %>
	</tr>
	<tr>
		<% bp.getController().writeFormField(out,"ds_linea_attivita"); %>
	</tr>
	<tr>
		<% bp.getController().writeFormField( out, "esercizio_fine"); %>
	</tr>
	<tr>
		<% bp.getController().writeFormField( out, "fl_limite_ass_obblig"); %>
	</tr>	
</table>