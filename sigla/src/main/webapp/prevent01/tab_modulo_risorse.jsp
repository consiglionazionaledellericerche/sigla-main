<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.prevent01.bp.*"
%>

<%
	CRUDDettagliModuloCostiBP bp = (CRUDDettagliModuloCostiBP)BusinessProcess.getBusinessProcess(request);
%>

<div class="card">
<table border="0" cellspacing="0" cellpadding="2">	
	<tr>
	  <td></td>
	  <td align=center><span class="FormLabel font-weight-bold text-primary"><% bp.getController().writeFormLabel(out,"label_risorse_es_prec");%></span></td>
	  <td align=center><span class="FormLabel font-weight-bold text-primary"><% bp.getController().writeFormLabel(out,"label_risorse_presunte_es_prec");%></span></td>
	</tr>
	<tr>
	  <td><% bp.getController().writeFormLabel(out,"ris_es_prec_tit_i");%></td>
	  <td><% bp.getController().writeFormInput(out,"ris_es_prec_tit_i");%></td>
	  <td><% bp.getController().writeFormInput(out,"ris_pres_es_prec_tit_i");%></td>	  
	</tr>
	<tr>
	  <td><% bp.getController().writeFormLabel(out,"ris_es_prec_tit_ii");%></td>
	  <td><% bp.getController().writeFormInput(out,"ris_es_prec_tit_ii");%></td>
	  <td><% bp.getController().writeFormInput(out,"ris_pres_es_prec_tit_ii");%></td>	  
	</tr>
	<tr>
	  <td align=right><span class="FormLabel font-weight-bold"><% bp.getController().writeFormLabel(out,"tot_risorse_provenienti_es_prec");%></span></td>
	  <td><% bp.getController().writeFormInput(out,"tot_risorse_provenienti_es_prec");%></td>
	  <td><% bp.getController().writeFormInput(out,"tot_risorse_presunte_es_prec");%></td>	  
	</tr>
	
</table>
</div>