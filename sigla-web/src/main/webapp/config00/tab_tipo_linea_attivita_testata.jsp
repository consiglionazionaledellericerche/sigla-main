<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,it.cnr.contab.config00.bp.*,it.cnr.contab.config00.latt.bulk.*"
%>
<% CRUDTipo_linea_attivitaBP bp = (CRUDTipo_linea_attivitaBP)BusinessProcess.getBusinessProcess(request); %>
<table class="Panel">
<tr>
	<% bp.getController().writeFormField(out,"cd_tipo_linea_attivita"); %>
<tr>
</tr>
	<% bp.getController().writeFormField(out,"ds_tipo_linea_attivita"); %>
<tr>
</tr>
	<td>
	<% bp.getController().writeFormLabel(out,"ti_gestione"); %>
	</td>
	<td>
	<% bp.getController().writeFormInput(out,null,"ti_gestione",false,null,"onclick=\"submitForm('doDefault')\""); %>
	</td>
<tr>
	<% Tipo_linea_attivitaBulk tipo_linea_attivita = (Tipo_linea_attivitaBulk)bp.getModel();
	   if (tipo_linea_attivita.getTi_gestione() == null || tipo_linea_attivita.getTi_gestione().equals(tipo_linea_attivita.TI_GESTIONE_SPESE))
		   bp.getController().writeFormField(out,"funzione"); %>
</tr>
<tr>
	<% bp.getController().writeFormField(out,"natura"); %>
</tr>
<tr>
	<% bp.getController().writeFormField(out,"cd_cdr_creatore"); %>
</tr>
</table>