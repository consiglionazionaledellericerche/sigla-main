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
	<tr><% bp.getController().writeFormField(out,"im_rf_a3_ricavi");%>
		<td><% bp.getController().writeFormInput(out,"im_rf_a3_ricavi_mod");%></td></tr>
	<tr><% bp.getController().writeFormField(out,"im_rg_a3_entrate");%>
		<td><% bp.getController().writeFormInput(out,"im_rg_a3_entrate_mod");%></td></tr>
</table>