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
	<tr><% bp.getController().writeFormField(out,"im_rah_a3_costi_finali");%>
		<td><% bp.getController().writeFormInput(out,"im_rah_a3_costi_finali_mod");%></td></tr>
	<tr><% bp.getController().writeFormField(out,"im_rai_a3_costi_altro_cdr");%>
		<td><% bp.getController().writeFormInput(out,"im_rai_a3_costi_altro_cdr_mod");%></td></tr>
</table>