<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.doccont00.ordine.bulk.*,
		it.cnr.contab.doccont00.bp.*"
%>
<% CRUDOrdineBP bp = (CRUDOrdineBP)BusinessProcess.getBusinessProcess(request); 
   OrdineBulk ordine = (OrdineBulk)bp.getModel(); %>
   
<div class="Group">

  <table>
	<tr>
	  <td colspan = "4">
		<% bp.getDettagliCRUDController().writeHTMLTable(
				pageContext,
				null,
				true,
				false,
				true,
				null,
				"100px",
				true); %>
	  </td>
	</tr>

  </table>

  <table class="Group">			
	<tr>
	  <td><% bp.getDettagliCRUDController().writeFormLabel(out,"ds_dettaglio"); %></td>
	  <td colspan=2><% bp.getDettagliCRUDController().writeFormInput(out,"ds_dettaglio"); %></td>
	</tr>
	<tr>
	  <td><% bp.getDettagliCRUDController().writeFormLabel(out,"quantita"); %></td>
	  <td><% bp.getDettagliCRUDController().writeFormInput(out,"quantita"); %></td>
	</tr>
	<tr>
	  <td><% bp.getDettagliCRUDController().writeFormLabel(out,"im_unitario"); %></td>
	  <td><% bp.getDettagliCRUDController().writeFormInput(out,"im_unitario"); %></td>
	</tr>
	<tr>
	  <td><% bp.getDettagliCRUDController().writeFormLabel(out,"imponibile"); %></td>
	  <td><% bp.getDettagliCRUDController().writeFormInput(out,"imponibile"); %></td>
	</tr>
	<tr>
	  <td><% bp.getDettagliCRUDController().writeFormLabel(out,"im_iva"); %></td>
	  <td><% bp.getDettagliCRUDController().writeFormInput(out,"im_iva"); %></td>
	</tr>
	<tr>
		<td><% bp.getDettagliCRUDController().writeFormLabel(out,"importoTotale"); %></td>
		<td><% bp.getDettagliCRUDController().writeFormInput(out,"importoTotale"); %></td>
	</tr>
  </table>

</div>