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
		it.cnr.contab.prevent00.action.*,
		it.cnr.contab.prevent00.bp.*"
%>

<%
	CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
%>

<table border="0" cellspacing="0" cellpadding="2" align=center>
	<tr><% bp.getController().writeFormField(out,"im_rh_ccs_costi");%> 
		<td><% bp.getController().writeFormInput(out,"im_rh_ccs_costi_mod");%></td></tr>
	<tr><td><b><span class="FormLabel">Impegni da contrarre</span></b></td></tr>
	<tr><% bp.getController().writeFormField(out,"im_ri_ccs_spese_odc");%> 
		<td><% bp.getController().writeFormInput(out,"im_ri_ccs_spese_odc_mod");%></td></tr>
	<tr><% bp.getController().writeFormField(out,"im_rj_ccs_spese_odc_altra_uo");%> 
		<td><% bp.getController().writeFormInput(out,"im_rj_ccs_spese_odc_altra_uo_mod");%></td></tr>
	<tr><td><b><span class="FormLabel">Impegni in essere</span></b></td></tr>
	<tr><% bp.getController().writeFormField(out,"im_rk_ccs_spese_ogc");%> 
		<td><% bp.getController().writeFormInput(out,"im_rk_ccs_spese_ogc_mod");%></td></tr>
	<tr><% bp.getController().writeFormField(out,"im_rl_ccs_spese_ogc_altra_uo");%> 
		<td><% bp.getController().writeFormInput(out,"im_rl_ccs_spese_ogc_altra_uo_mod");%></td></tr>
</table>