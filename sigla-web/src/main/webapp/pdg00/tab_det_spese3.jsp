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
	<tr><td><span class="FormLabel">Impegni da contrarre</span></td></tr>
	<tr><% bp.getController().writeFormField(out,"im_ral_a3_spese_odc");%></tr>
	<tr><% bp.getController().writeFormField(out,"im_ram_a3_spese_odc_altra_uo");%></tr>
	<tr><td><span class="FormLabel">Impegni in essere</span></td></tr>
	<tr><% bp.getController().writeFormField(out,"im_ran_a3_spese_ogc");%></tr>
	<tr><% bp.getController().writeFormField(out,"im_rao_a3_spese_ogc_altra_uo");%></tr>
</table>