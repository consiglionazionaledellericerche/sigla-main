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
	<tr><% bp.getController().writeFormField(out,"im_rm_css_ammortamenti");%></tr>
	<tr><% bp.getController().writeFormField(out,"im_rn_css_rimanenze");%></tr>
	<tr><% bp.getController().writeFormField(out,"im_ro_css_altri_costi");%></tr>
	<tr><% bp.getController().writeFormField(out,"im_rp_css_verso_altro_cdr");%></tr>
</table>