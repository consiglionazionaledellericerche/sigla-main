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
	   <td colspan="3">	
		<% bp.getController().writeFormInput(out,null,"cd_progetto",bp.isUoArea(),null,null); %>
		<% bp.getController().writeFormInput(out,null,"ds_progetto",bp.isUoArea(),null,null); %>
		<% bp.getController().writeFormInput(out,null,"find_nodo_padre",bp.isUoArea(),null,null); %>
		<!--<% bp.getController().writeFormInput(out,"crea_modulo");%>-->
	  </td>
	</tr>
	<tr>
	 <td>
		<% bp.getController().writeFormLabel(out,"centro_responsabilita"); %>
	</td>
	 <td colspan="3">	
		<% bp.getController().writeFormInput(out,null,"centro_responsabilita",bp.isUoArea(),null,null); %> 
	 </td>
	</tr>
	
	<tr>
	 <td>
		<% bp.getController().writeFormLabel(out,"responsabile"); %>
	</td>
	 <td colspan="3">	
		<% bp.getController().writeFormInput(out,"responsabile"); %> 
	 </td>
	</tr>
		
	<tr>
	 <td>
		<% bp.getController().writeFormLabel(out,"insieme_la"); %>
	</td>
	 <td colspan="3">	
		<% bp.getController().writeFormInput(out,null,"insieme_la",bp.isUoArea(),null,null); %> 
	 </td>
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
	 <td>
		<% bp.getController().writeFormLabel(out,"gruppo_linea_attivita"); %>
	</td>
	 <td colspan="3">	
		<% bp.getController().writeFormInput(out,null,"gruppo_linea_attivita",bp.isUoArea(),null,null); %> 
	 </td>
	 </tr>
	<tr>
	 <td>
		<% bp.getController().writeFormLabel(out,"denominazione"); %>
	</td>
	 <td colspan="3">	
		<% bp.getController().writeFormInput(out,null,"denominazione",bp.isUoArea(),null,null); %> 
	 </td>
	</tr>
	
	<tr>
	 <td>
		<% bp.getController().writeFormLabel(out,"ds_linea_attivita"); %>
	</td>
	 <td colspan="3">	
		<% bp.getController().writeFormInput(out,null,"ds_linea_attivita",bp.isUoArea(),null,null); %> 
	 </td>
	</tr>
	
	<tr>
		<% bp.getController().writeFormField( out, "esercizio_fine"); %>
	</tr>
	<tr>
		<% bp.getController().writeFormField( out, "fl_limite_ass_obblig"); %>
	</tr>	
	<tr>
		<% if (workpackage.getTi_gestione() != null && workpackage.getTi_gestione().equals(workpackage.TI_GESTIONE_SPESE))
			bp.getController().writeFormField(out,"cofog");%>
	</tr>
</table>