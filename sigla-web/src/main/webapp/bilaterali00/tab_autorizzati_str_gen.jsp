<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.bilaterali00.bulk.Blt_accordiBulk,
		it.cnr.contab.bilaterali00.bp.CRUDBltAccordiBP"%>
<%
  CRUDBltAccordiBP bp = (CRUDBltAccordiBP)BusinessProcess.getBusinessProcess(request);
  Blt_accordiBulk  model = (Blt_accordiBulk)bp.getModel();
%>
	<fieldset class="fieldset">
		<legend class="GroupLabel">Dati Generali Autorizzato</legend>
		<table width="90%">
			<tr>
				<td><% bp.getCrudBltAutorizzatiStr().writeFormLabel(out,"cdTerzo");%></td>
				<td colspan=4>
				  <% bp.getCrudBltAutorizzatiStr().writeFormInput(out,"cdTerzo");%>
				  <% bp.getCrudBltAutorizzatiStr().writeFormInput(out,"dsTerzo");%>
				  <% bp.getCrudBltAutorizzatiStr().writeFormInput(out,"findTerzo");%>
				</td>
			</tr>
			<tr>
		  		<td><span class="FormLabel">Cdr Ospitante</span></td>
				<td colspan=4>
					<% bp.getCrudBltAutorizzatiStr().writeFormInput(out,"cdCdrTerzoStr");%>
					<% bp.getCrudBltAutorizzatiStr().writeFormInput(out,"dsCdrTerzo");%>
					<% bp.getCrudBltAutorizzatiStr().writeFormInput(out,"findCdrTerzo");%>
				</td>
			</tr>
			<tr>
				<td><% bp.getCrudBltAutorizzatiStr().writeFormLabel(out,"ds_indirizzo");%></td>
				<td><% bp.getCrudBltAutorizzatiStr().writeFormInput(out,"ds_indirizzo");%></td>
				<td><% bp.getCrudBltAutorizzatiStr().writeFormLabel(out,"ds_co_indirizzo");%></td>
				<td><% bp.getCrudBltAutorizzatiStr().writeFormInput(out,"ds_co_indirizzo");%></td>
				<td><% bp.getCrudBltAutorizzatiStr().writeFormLabel(out,"ds_comune");%></td>
				<td><% bp.getCrudBltAutorizzatiStr().writeFormInput(out,"ds_comune");%></td>
			</tr>
			<tr>
				<td><% bp.getCrudBltAutorizzatiStr().writeFormLabel(out,"telefTerzo");%></td>
				<td colspan=2><% bp.getCrudBltAutorizzatiStr().writeFormInput(out,"telefTerzo");%></td>
				<td><% bp.getCrudBltAutorizzatiStr().writeFormLabel(out,"faxTerzo");%>
					<% bp.getCrudBltAutorizzatiStr().writeFormInput(out,"faxTerzo");%></td>
			</tr>
			<tr>
				<td><% bp.getCrudBltAutorizzatiStr().writeFormLabel(out,"emailTerzo");%></td>
				<td colspan=2><% bp.getCrudBltAutorizzatiStr().writeFormInput(out,"emailTerzo");%></td>
			</tr>
		</table>
	</fieldset>
