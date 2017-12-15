<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.contab.incarichi00.bp.*"
%>

<%
CRUDConfigRepertorioLimitiBP bp = (CRUDConfigRepertorioLimitiBP)BusinessProcess.getBusinessProcess(request);
%>
<table class="w-100">
	<tr><td>
		<% bp.getRepertorioLimiti().writeHTMLTable(
								pageContext,
								bp.isUoEnte()?"columnsUoEnte":null,
								(bp.isSearching()||bp.isInserting())?false:true,
								false,
								(bp.isSearching()||bp.isInserting())?false:true,
								"100%",
								"150px"); 
		%>
	</td></tr>
</table>
