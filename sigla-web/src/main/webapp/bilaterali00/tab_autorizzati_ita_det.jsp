<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.bilaterali00.bulk.Blt_accordiBulk,
		it.cnr.contab.bilaterali00.bp.CRUDBltAccordiBP"%>
<%
  CRUDBltAccordiBP bp = (CRUDBltAccordiBP)BusinessProcess.getBusinessProcess(request);
  Blt_accordiBulk  model = (Blt_accordiBulk)bp.getModel();
%>

<% bp.getCrudBltAutorizzatiDettIta().writeHTMLTable(
								pageContext,
								null,
								!bp.isSearching(),
								false,
								!bp.isSearching(),
								"100%",
								"100px");
%>
&nbsp;
<fieldset class="fieldset">
	<table>
		<tr>
			<td><% bp.getCrudBltAutorizzatiDettIta().writeFormLabel(out,"annoVisita");%></td>
			<td><% bp.getCrudBltAutorizzatiDettIta().writeFormInput(out,"annoVisita");%></td>
			<td><% bp.getCrudBltAutorizzatiDettIta().writeFormLabel(out,"numMaxGgVisita");%></td>
			<td><% bp.getCrudBltAutorizzatiDettIta().writeFormInput(out,"numMaxGgVisita");%></td>
		</tr>
	</table>
</fieldset>
	