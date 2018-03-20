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
	<tr><% bp.getController().writeFormField(out,"im_ra_rce");%>
		<td><% bp.getController().writeFormInput(out,"im_ra_rce_mod");%></td></tr>
	<tr><% bp.getController().writeFormField(out,"im_rb_rse");%>
		<td><% bp.getController().writeFormInput(out,"im_rb_rse_mod");%></td></tr>
	<tr><% bp.getController().writeFormField(out,"im_rc_esr");%>
		<td><% bp.getController().writeFormInput(out,"im_rc_esr_mod");%></td></tr>
</table>