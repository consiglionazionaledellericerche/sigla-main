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
	<tr><td><span class="FormLabel">Impegni da contrarre</span></td></tr>
	<tr><% bp.getController().writeFormField(out,"im_rac_a2_spese_odc");%>
		<td><%bp.getController().writeFormInput(out,"im_rac_a2_spese_odc_mod");%></td></tr>
	<tr><% bp.getController().writeFormField(out,"im_rad_a2_spese_odc_altra_uo");%>
		<td><%bp.getController().writeFormInput(out,"im_rad_a2_spese_odc_altra_uo_mod");%></td></tr>
	<tr><td><span class="FormLabel">Impegni in essere</span></td></tr>
	<tr><% bp.getController().writeFormField(out,"im_rae_a2_spese_ogc");%>
		<td><%bp.getController().writeFormInput(out,"im_rae_a2_spese_ogc_mod");%></td></tr>
	<tr><% bp.getController().writeFormField(out,"im_raf_a2_spese_ogc_altra_uo");%>
		<td><% bp.getController().writeFormInput(out,"im_raf_a2_spese_ogc_altra_uo_mod");%></td></tr>
</table>