<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.contab.incarichi00.bp.*"
%>

<%
CRUDConfigRepertorioLimitiBP bp = (CRUDConfigRepertorioLimitiBP)BusinessProcess.getBusinessProcess(request);
%>
<table>
	<tr><td>
		<% bp.getIncarichiXUo().writeHTMLTable(
								pageContext,
								"incarichiUo",
								false,
								false,
								false,
								"100%",
								"150px"); 
		%>
	</td></tr>
</table>