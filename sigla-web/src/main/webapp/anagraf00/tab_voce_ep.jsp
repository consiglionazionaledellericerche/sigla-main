<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.anagraf00.bp.*"
%>

<%
  CRUDAnagraficaBP bp = (CRUDAnagraficaBP)BusinessProcess.getBusinessProcess(request);
  UserContext uc = HttpActionContext.getUserContext(session);
%>
<%@page import="it.cnr.jada.UserContext"%>
<%@page import="javax.naming.Context"%>
<%@page import="it.cnr.contab.utenze00.bp.CNRUserContext"%>
<table>
		<tr>
			<td><% bp.getController().writeFormField(out,"find_conto_credito");%></td>
		</tr>
		<tr>
			<td><% bp.getController().writeFormField(out,"find_conto_debito");%></td>
		</tr>
</table>