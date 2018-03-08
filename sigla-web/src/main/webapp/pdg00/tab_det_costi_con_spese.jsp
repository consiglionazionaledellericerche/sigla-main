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
		it.cnr.contab.pdg00.action.*,
		it.cnr.contab.pdg00.bp.*"
%>

<%
	CRUDSpeDetPdGBP bp = (CRUDSpeDetPdGBP)BusinessProcess.getBusinessProcess(request);
%>

<table border="0" cellspacing="0" cellpadding="2">
	<tr><% bp.getController().writeFormField(out,"im_rh_ccs_costi");%></tr>
	<tr><td><b><span class="FormLabel">Impegni da contrarre</span></b></td></tr>
	<tr><% bp.getController().writeFormField(out,"im_ri_ccs_spese_odc");%></tr>
	<tr><% bp.getController().writeFormField(out,"im_rj_ccs_spese_odc_altra_uo");%></tr>
	<tr><td><b><span class="FormLabel">Impegni in essere</span></b></td></tr>
	<tr><% bp.getController().writeFormField(out,"im_rk_ccs_spese_ogc");%></tr>
	<tr><% bp.getController().writeFormField(out,"im_rl_ccs_spese_ogc_altra_uo");%></tr>
</table>