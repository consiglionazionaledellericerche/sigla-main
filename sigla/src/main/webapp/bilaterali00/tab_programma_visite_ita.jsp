<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.bilaterali00.bulk.Blt_accordiBulk,
		it.cnr.contab.bilaterali00.bp.CRUDBltAccordiBP"%>
<%
  CRUDBltAccordiBP bp = (CRUDBltAccordiBP)BusinessProcess.getBusinessProcess(request);
  Blt_accordiBulk  model = (Blt_accordiBulk)bp.getModel();
%>

<% bp.getCrudBltProgrammaVisiteIta().writeHTMLTable(
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
		<legend class="GroupLabel">Dati Generali Programma Visite</legend>
		<table>
			<tr>
				<td><% bp.getCrudBltProgrammaVisiteIta().writeFormLabel(out,"annoVisita");%></td>
				<td><% bp.getCrudBltProgrammaVisiteIta().writeFormInput(out,"annoVisita");%></td>
			</tr>
			<tr>
				<td><% bp.getCrudBltProgrammaVisiteIta().writeFormLabel(out,"numVisiteAutorizzate");%></td>
				<td><% bp.getCrudBltProgrammaVisiteIta().writeFormInput(out,"numVisiteAutorizzate");%></td>
				<td><% bp.getCrudBltProgrammaVisiteIta().writeFormLabel(out,"numMaxGgVisita");%></td>
				<td><% bp.getCrudBltProgrammaVisiteIta().writeFormInput(out,"numMaxGgVisita");%></td>
			</tr>
		</table>
	</fieldset>
