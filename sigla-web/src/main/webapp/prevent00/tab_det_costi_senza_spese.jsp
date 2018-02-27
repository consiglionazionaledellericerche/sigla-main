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
	<tr><% bp.getController().writeFormField(out,"im_rm_css_ammortamenti");%>
		<td><% bp.getController().writeFormInput(out,"im_rm_css_ammortamenti_mod");%></td></tr>
	<tr><% bp.getController().writeFormField(out,"im_rn_css_rimanenze");%>
		<td><% bp.getController().writeFormInput(out,"im_rn_css_rimanenze_mod");%></td></tr>
	<tr><% bp.getController().writeFormField(out,"im_ro_css_altri_costi");%>
		<td><% bp.getController().writeFormInput(out,"im_ro_css_altri_costi_mod");%></td></tr>
	<tr><% bp.getController().writeFormField(out,"im_rp_css_verso_altro_cdr");%>
		<td><% bp.getController().writeFormInput(out,"im_rp_css_verso_altro_cdr_mod");%></td></tr>
</table>