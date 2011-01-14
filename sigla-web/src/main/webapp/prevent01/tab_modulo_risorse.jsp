<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page 
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.prevent01.bp.*"
%>

<%
	CRUDDettagliModuloCostiBP bp = (CRUDDettagliModuloCostiBP)BusinessProcess.getBusinessProcess(request);
%>

<table border="0" cellspacing="0" cellpadding="2">	
	<tr>
	  <td></td>
	  <td align=center><% bp.getController().writeFormLabel(out,"label_risorse_es_prec");%></td>
	  <td align=center><% bp.getController().writeFormLabel(out,"label_risorse_presunte_es_prec");%></td>
	</tr>
	<tr>
	  <td><% bp.getController().writeFormLabel(out,"ris_es_prec_tit_i");%></td>
	  <td><table><tr><td><% bp.getController().writeFormInput(out,"ris_es_prec_tit_i");%></td></tr></table></td>
	  <td><table><tr><td><% bp.getController().writeFormInput(out,"ris_pres_es_prec_tit_i");%></td></tr></table></td>	  
	</tr>
	<tr>
	  <td><% bp.getController().writeFormLabel(out,"ris_es_prec_tit_ii");%></td>
	  <td><table><tr><td><% bp.getController().writeFormInput(out,"ris_es_prec_tit_ii");%></td></tr></table></td>
	  <td><table><tr><td><% bp.getController().writeFormInput(out,"ris_pres_es_prec_tit_ii");%></td></tr></table></td>	  
	</tr>
	<tr>
	  <td align=right><% bp.getController().writeFormLabel(out,"tot_risorse_provenienti_es_prec");%></td>
	  <td><% bp.getController().writeFormInput(out,"tot_risorse_provenienti_es_prec");%></td>
	  <td><% bp.getController().writeFormInput(out,"tot_risorse_presunte_es_prec");%></td>	  
	</tr>
	
</table>
