<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.bilaterali00.bulk.*,
		it.cnr.contab.bilaterali00.bp.CRUDBltAccordiBP"%>
<%
  CRUDBltAccordiBP bp = (CRUDBltAccordiBP)BusinessProcess.getBusinessProcess(request);
  Blt_progettiBulk progetto = (Blt_progettiBulk)bp.getCrudBltProgetti().getModel();
%>
<% bp.getCrudBltProgetti().writeHTMLTable(
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
		<legend class="GroupLabel">Dati Generali Progetto</legend>
		<table>
			<tr>
				<td><% bp.getCrudBltProgetti().writeFormLabel(out,"cd_progetto");%></td>
				<td><% bp.getCrudBltProgetti().writeFormInput(out,"cd_progetto");%></td>
			</tr>
			<tr>
				<td><% bp.getCrudBltProgetti().writeFormLabel(out,"ds_progetto_ita");%></td>
				<td><% bp.getCrudBltProgetti().writeFormInput(out,"ds_progetto_ita");%></td>
			</tr>
			<tr>
				<td><% bp.getCrudBltProgetti().writeFormLabel(out,"ds_progetto_eng");%></td>
				<td><% bp.getCrudBltProgetti().writeFormInput(out,"ds_progetto_eng");%></td>
			</tr>
		</table>
	</fieldset>

	<fieldset class="fieldset">
		<legend class="GroupLabel">Responsabile Italiano</legend>
		<table>
			<tr>
				<td><% bp.getCrudBltProgetti().writeFormLabel(out,"cd_respons_ita");%></td>
				<td colspan=3>
					<% bp.getCrudBltProgetti().writeFormInput(out,"cd_respons_ita");%>
					<% bp.getCrudBltProgetti().writeFormInput(out,"ds_respons_ita");%>
					<% bp.getCrudBltProgetti().writeFormInput(out,"find_responsabile_ita");%>
				</td>
			</tr>
			<tr>
				<td><% bp.getCrudBltProgetti().writeFormLabel(out,"cd_cdr_respons_ita");%></td>
				<td colspan=3>
					<% bp.getCrudBltProgetti().writeFormInput(out,"cd_cdr_respons_ita");%>
					<% bp.getCrudBltProgetti().writeFormInput(out,"ds_cdr_respons_ita");%>
					<% bp.getCrudBltProgetti().writeFormInput(out,"find_cdr_respons_ita");%>
				</td>
			</tr>
			<tr>
				<td><% bp.getCrudBltProgetti().writeFormLabel(out,"ds_indirizzo");%></td>
				<td ><% bp.getCrudBltProgetti().writeFormInput(out,"ds_indirizzo");%></td>				
				<td><% bp.getCrudBltProgetti().writeFormLabel(out,"ds_co_indirizzo");%></td>
				<td ><% bp.getCrudBltProgetti().writeFormInput(out,"ds_co_indirizzo");%></td>	
				<td><% bp.getCrudBltProgetti().writeFormLabel(out,"ds_comune");%></td>
				<td ><% bp.getCrudBltProgetti().writeFormInput(out,"ds_comune");%></td>				
			</tr>
			<tr>
				<td><% bp.getCrudBltProgetti().writeFormLabel(out,"telef_respons_ita");%></td>
				<td><% bp.getCrudBltProgetti().writeFormInput(out,"telef_respons_ita");%></td>
				<td><% bp.getCrudBltProgetti().writeFormLabel(out,"fax_respons_ita");%></td>
				<td><% bp.getCrudBltProgetti().writeFormInput(out,"fax_respons_ita");%></td>
			</tr>
			<tr>
				<td><% bp.getCrudBltProgetti().writeFormLabel(out,"email_respons_ita");%></td>
				<td><% bp.getCrudBltProgetti().writeFormInput(out,"email_respons_ita");%></td>
				<td colspan="2">
					<% bp.getCrudBltProgetti().writeFormLabel(out,"fl_associato_respons_ita");%>
					<% bp.getCrudBltProgetti().writeFormInput(out,"fl_associato_respons_ita");%>
				</td>
			</tr>
			<% if (progetto!=null && progetto.getFl_associato_respons_ita()) {%>
			<tr>
				<td colspan=4>
					<fieldset class="fieldset">
					<legend class="GroupLabel" style="color:red">Istituzione Appartenenza</legend>
					<table>
						<tr>
							<td><% bp.getCrudBltProgetti().writeFormLabel(out,"ente_respons_ita");%></td>
							<td><% bp.getCrudBltProgetti().writeFormInput(out,"ente_respons_ita");%></td>
						</tr>
						<tr>
							<td><% bp.getCrudBltProgetti().writeFormLabel(out,"ds_comune_ente_respons_ita");%></td>
							<td><% bp.getCrudBltProgetti().writeFormInput(out,"ds_comune_ente_respons_ita");%>
								<% bp.getCrudBltProgetti().writeFormInput(out,"findComuneEnteResponsIta");%></td>
							<% bp.getCrudBltProgetti().writeFormField(out,"cap_ente_respons_ita");%>
						</tr>
						<tr>
							<td><% bp.getCrudBltProgetti().writeFormLabel(out,"indirizzo_ente_respons_ita");%></td>
							<td><% bp.getCrudBltProgetti().writeFormInput(out,"indirizzo_ente_respons_ita");%></td>
						</tr>
					</table>
					</fieldset>
				</td>
			</tr>
			<% } %>
		</table>
	</fieldset>

	<fieldset class="fieldset">
		<legend class="GroupLabel">Responsabile Straniero</legend>
		<table>
			<tr>
				<td><% bp.getCrudBltProgetti().writeFormLabel(out,"cd_respons_str");%></td>
				<td colspan=3>
					<% bp.getCrudBltProgetti().writeFormInput(out,"cd_respons_str");%>
					<% bp.getCrudBltProgetti().writeFormInput(out,"ds_respons_str");%>
					<% bp.getCrudBltProgetti().writeFormInput(out,"find_responsabile_str");%>
				</td>
			</tr>
			<tr>
				<td><% bp.getCrudBltProgetti().writeFormLabel(out,"telef_respons_str");%></td>
				<td><% bp.getCrudBltProgetti().writeFormInput(out,"telef_respons_str");%></td>
				<td><% bp.getCrudBltProgetti().writeFormLabel(out,"fax_respons_str");%></td>
				<td><% bp.getCrudBltProgetti().writeFormInput(out,"fax_respons_str");%></td>
			</tr>
			<tr>
				<td><% bp.getCrudBltProgetti().writeFormLabel(out,"email_respons_str");%></td>
				<td colspan=3><% bp.getCrudBltProgetti().writeFormInput(out,"email_respons_str");%></td>
			</tr>
			<tr>
				<td colspan=4>
					<fieldset class="fieldset">
					<legend class="GroupLabel" style="color:red">Istituzione Appartenenza</legend>
					<table>
						<tr>
							<td><% bp.getCrudBltProgetti().writeFormLabel(out,"nome_istituzione_str");%></td>
							<td colspan=3><% bp.getCrudBltProgetti().writeFormInput(out,"nome_istituzione_str");%></td>
						</tr>
						<tr>
							<td><% bp.getCrudBltProgetti().writeFormLabel(out,"sede_istituzione_str");%></td>
							<td colspan=3><% bp.getCrudBltProgetti().writeFormInput(out,"sede_istituzione_str");%></td>
						</tr>
					</table>
					</fieldset>
				</td>
			</tr>
		</table>
	</fieldset>
