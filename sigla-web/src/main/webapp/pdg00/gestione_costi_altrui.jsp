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

<CENTER>
	<table border="0" cellspacing="0" cellpadding="2" align=center>
		<tr><%bp.getController().writeFormLabel(out,"cd_anag");%></tr>
		<tr><%bp.getController().writeFormInput(out, "default", "cd_anag", true, "FormLabel", null);%></tr>
	</table>
</CENTER>