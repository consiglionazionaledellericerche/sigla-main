<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.bilaterali00.bulk.Blt_accordiBulk,
		it.cnr.contab.bilaterali00.bp.CRUDBltAccordiBP"%>
<%
  CRUDBltAccordiBP bp = (CRUDBltAccordiBP)BusinessProcess.getBusinessProcess(request);
  Blt_accordiBulk  model = (Blt_accordiBulk)bp.getModel();
%>

<% bp.getCrudBltProgrammaVisiteStr().writeHTMLTable(
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
				<td><% bp.getCrudBltProgrammaVisiteStr().writeFormLabel(out,"annoVisita");%></td>
				<td><% bp.getCrudBltProgrammaVisiteStr().writeFormInput(out,"annoVisita");%></td>
			</tr>
			<tr>
				<td><% bp.getCrudBltProgrammaVisiteStr().writeFormLabel(out,"numVisiteAutorizzate");%></td>
				<td><% bp.getCrudBltProgrammaVisiteStr().writeFormInput(out,"numVisiteAutorizzate");%></td>
				<td><% bp.getCrudBltProgrammaVisiteStr().writeFormLabel(out,"numMaxGgVisita");%></td>
				<td><% bp.getCrudBltProgrammaVisiteStr().writeFormInput(out,"numMaxGgVisita");%></td>
			</tr>
		</table>
	</fieldset>
