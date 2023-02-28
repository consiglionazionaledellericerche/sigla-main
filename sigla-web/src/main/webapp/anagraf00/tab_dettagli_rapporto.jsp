<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.anagraf00.bp.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.anagraf00.core.bulk.*"
%>

<%
  CRUDAnagraficaBP bp = (CRUDAnagraficaBP)BusinessProcess.getBusinessProcess(request);
  AnagraficoBulk anagrafico = (AnagraficoBulk)bp.getModel();  
%>

<table border="0" cellspacing="0" cellpadding="2" align=center>
<tr>
  <% bp.getCrudRapporti().writeFormField(out,"find_tipo_rapporto");%>
</tr>
<tr>
  <td><% bp.getCrudRapporti().writeFormLabel(out,"dt_ini_validita");%></td>
  <td colspan="2"><% bp.getCrudRapporti().writeFormInput(out,"dt_ini_validita");%></td>
</tr>
<tr>
  <td><% bp.getCrudRapporti().writeFormLabel(out,"dt_fin_validita");%></td>
  <td colspan="2"><% bp.getCrudRapporti().writeFormInput(out,"dt_fin_validita");%></td>
</tr>
<tr>
  <td><% bp.getCrudRapporti().writeFormLabel(out,"causale_fine_rapporto");%></td>
  <td colspan="2"><% bp.getCrudRapporti().writeFormInput(out,"causale_fine_rapporto");%></td>
</tr>
</table>