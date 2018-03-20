<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.bilaterali00.bulk.*,
		it.cnr.contab.bilaterali00.bp.CRUDBltAccordiBP"%>
<%
  CRUDBltAccordiBP bp = (CRUDBltAccordiBP)BusinessProcess.getBusinessProcess(request);
  Blt_autorizzatiBulk autorizzato = (Blt_autorizzatiBulk)bp.getCrudBltAutorizzatiIta().getModel();
%>
	<fieldset class="fieldset">
		<legend class="GroupLabel">Dati Generali Autorizzato</legend>
		<table width="90%">
			<tr>
				<td><% bp.getCrudBltAutorizzatiIta().writeFormLabel(out,"cdTerzo");%></td>
				<td colspan=4>
				  <% bp.getCrudBltAutorizzatiIta().writeFormInput(out,"cdTerzo");%>
				  <% bp.getCrudBltAutorizzatiIta().writeFormInput(out,"dsTerzo");%>
				  <% bp.getCrudBltAutorizzatiIta().writeFormInput(out,"findTerzo");%>
				</td>
			</tr>
			<tr>
				<td><% bp.getCrudBltAutorizzatiIta().writeFormLabel(out,"telefTerzo");%></td>
				<td colspan=2><% bp.getCrudBltAutorizzatiIta().writeFormInput(out,"telefTerzo");%></td>
				<td><% bp.getCrudBltAutorizzatiIta().writeFormLabel(out,"faxTerzo");%>
					<% bp.getCrudBltAutorizzatiIta().writeFormInput(out,"faxTerzo");%></td>
			</tr>
			<tr>
				<td><% bp.getCrudBltAutorizzatiIta().writeFormLabel(out,"emailTerzo");%></td>
				<td colspan=2><% bp.getCrudBltAutorizzatiIta().writeFormInput(out,"emailTerzo");%></td>
				<td>
					<% bp.getCrudBltAutorizzatiIta().writeFormLabel(out,"flAssimilatoDip");%>
					<% bp.getCrudBltAutorizzatiIta().writeFormInput(out,"flAssimilatoDip");%></td>
				<% if (autorizzato!=null && autorizzato.getFlAssimilatoDip()) {%>
				<td>
					<% bp.getCrudBltAutorizzatiIta().writeFormLabel(out,"flAssociato");%>
					<% bp.getCrudBltAutorizzatiIta().writeFormInput(out,"flAssociato");%></td>
				<% } %>
			</tr>
		</table>
	</fieldset>
	<% if (autorizzato!=null) {%>
	<fieldset class="fieldset">
		<legend class="GroupLabel">Dati Ente di Appartenenza</legend>
		<table width="90%">
		<% if (autorizzato.getFlAssimilatoDip()) {%>
			<tr>
		  		<td><span class="FormLabel">Codice Cdr</span></td>
				<td colspan=4>
					<% bp.getCrudBltAutorizzatiIta().writeFormInput(out,"cdCdrTerzoIta");%>
					<% bp.getCrudBltAutorizzatiIta().writeFormInput(out,"dsCdrTerzo");%>
					<% bp.getCrudBltAutorizzatiIta().writeFormInput(out,"findCdrTerzo");%>
				</td>
			</tr>
			<tr>
				<td><% bp.getCrudBltAutorizzatiIta().writeFormLabel(out,"ds_indirizzo");%></td>
				<td><% bp.getCrudBltAutorizzatiIta().writeFormInput(out,"ds_indirizzo");%></td>
				<td><% bp.getCrudBltAutorizzatiIta().writeFormLabel(out,"ds_co_indirizzo");%></td>
				<td><% bp.getCrudBltAutorizzatiIta().writeFormInput(out,"ds_co_indirizzo");%></td>
				<td><% bp.getCrudBltAutorizzatiIta().writeFormLabel(out,"ds_comune");%></td>
				<td><% bp.getCrudBltAutorizzatiIta().writeFormInput(out,"ds_comune");%></td>
			</tr>
		<% } %>
		<% if (!autorizzato.getFlAssimilatoDip()||autorizzato.getFlAssociato()) {%>
			<tr>
				<td><% bp.getCrudBltAutorizzatiIta().writeFormLabel(out,"enteDiAppartenenza");%></td>
				<td><% bp.getCrudBltAutorizzatiIta().writeFormInput(out,"enteDiAppartenenza");%></td>
			</tr>
			<tr>
				<td><% bp.getCrudBltAutorizzatiIta().writeFormLabel(out,"dsComuneEnteDiAppartenenza");%></td>
				<td><% bp.getCrudBltAutorizzatiIta().writeFormInput(out,"dsComuneEnteDiAppartenenza");%>
					<% bp.getCrudBltAutorizzatiIta().writeFormInput(out,"findComuneEnteDiAppartenenza");%></td>
				<% bp.getCrudBltAutorizzatiIta().writeFormField(out,"capEnteDiAppartenenza");%>
			</tr>
			<tr>
				<td><% bp.getCrudBltAutorizzatiIta().writeFormLabel(out,"indirizzoEnteDiAppartenenza");%></td>
				<td><% bp.getCrudBltAutorizzatiIta().writeFormInput(out,"indirizzoEnteDiAppartenenza");%></td>
			</tr>
		<% } %>
		</table>
	</fieldset>
<% } %>
	