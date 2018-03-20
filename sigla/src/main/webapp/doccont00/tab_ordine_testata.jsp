<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.doccont00.ordine.bulk.*,
		it.cnr.contab.doccont00.bp.*"
%>
<% CRUDOrdineBP bp = (CRUDOrdineBP)BusinessProcess.getBusinessProcess(request); %>
	
<div class="Group">	
<table>			

  <tr>
	<td><% bp.getController().writeFormLabel(out,"dt_registrazione"); %></td>
	<td><% bp.getController().writeFormInput(out,"dt_registrazione"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cdTipoConsegna"); %></td>
	<td colspan=3><% bp.getController().writeFormInput(out,"cdTipoConsegna"); %>
		<% bp.getController().writeFormInput(out,"dsTipoConsegna"); %></td>
	<td><% bp.getController().writeFormInput(out,"findTipoConsegna"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"note"); %></td>
	<td colspan=4><% bp.getController().writeFormInput(out,"note"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"esercizio_ori_obbligazione"); %></td>
	<td colspan=4><% bp.getController().writeFormInput(out,"esercizio_ori_obbligazione"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"pg_obblig"); %></td>
	<td colspan=4><% bp.getController().writeFormInput(out,"pg_obblig"); %>
		<% bp.getController().writeFormInput(out,"ds_obbligazione"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"im_obbligazione"); %></td>
	<td colspan=4><% bp.getController().writeFormInput(out,"im_obbligazione"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_elemento_voce"); %></td>
	<td colspan=4><% bp.getController().writeFormInput(out,"cd_elemento_voce"); %>
		<% bp.getController().writeFormInput(out,"dsElementoVoce"); %></td>
  </tr>
   <tr>
	<td><% bp.getController().writeFormLabel( out, "clausola"); %></td>
	<td><% bp.getController().writeFormInput( out, "clausola"); %></td>
  </tr>
</table>

</div>