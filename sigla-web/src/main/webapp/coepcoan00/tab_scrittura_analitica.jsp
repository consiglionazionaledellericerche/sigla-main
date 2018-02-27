<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,it.cnr.contab.coepcoan00.bp.*,it.cnr.contab.coepcoan00.core.bulk.*"
%>
<%CRUDScritturaAnaliticaBP bp = (CRUDScritturaAnaliticaBP)BusinessProcess.getBusinessProcess(request);%>

<table class="Panel">
<tr>
	<td><% bp.getController().writeFormLabel(out,"dt_contabilizzazione"); %></td>
	<td><% bp.getController().writeFormInput(out,"dt_contabilizzazione"); %></td>	
</tr>

<tr>
	<td><% bp.getController().writeFormLabel(out,"ds_scrittura"); %></td>
	<td><% bp.getController().writeFormInput(out,"default","ds_scrittura",bp.isEditing(),null, null); %></td>	
</tr>
<tr>
	<td><% bp.getController().writeFormLabel(out,"cd_terzo"); %></td>
	<td><% bp.getController().writeFormInput(out,"default","cd_terzo", bp.isEditing(),null, null); 
	       bp.getController().writeFormInput(out,"ds_terzo");
	       bp.getController().writeFormInput(out,"default","find_terzo",bp.isEditing(),null, null); %></td>
</tr>

<tr>
	<td><% bp.getController().writeFormLabel(out,"attiva"); %></td>
	<td><% bp.getController().writeFormInput(out,"attiva"); %>
		<% bp.getController().writeFormLabel(out,"pg_scrittura_annullata"); %>
		<% bp.getController().writeFormInput(out,"pg_scrittura_annullata"); %>
	</td>
</tr>

</table>