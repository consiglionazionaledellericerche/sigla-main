<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.fondecon00.bp.*,
		it.cnr.contab.fondecon00.core.bulk.*,
		it.cnr.contab.docamm00.docs.bulk.*"
%>

<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title>Spesa del Fondo Economale</title>
</head>

<body class="Form">
<%
	FondoSpesaBP bp = (FondoSpesaBP)BusinessProcess.getBusinessProcess(request);
	Fondo_spesaBulk bulk = (Fondo_spesaBulk)bp.getModel();

	bp.openFormWindow(pageContext);
%>

	<TABLE class="Panel">
		<TR>
			<% bp.getController().writeFormField(out,"pg_fondo_spesa"); %>
		</TR>
		<%	if (bp.isSearching()) { %>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"spesaDocumentataRadioGroup"); %>
				</td>
				<td>
					<% bp.getController().writeFormInput( out, null, "spesaDocumentataRadioGroup", false, null, "onClick=\"submitForm('doOnFlSpesaDocumentataForSearchChange')\""); %>
				</td>
				<td>
					<% bp.getController().writeFormLabel(out,"spesaReintegrataRadioGroup"); %>
				</td>
				<td>
					<% bp.getController().writeFormInput( out, null, "spesaReintegrataRadioGroup", false, null, "onClick=\"submitForm('doDefault')\""); %>
				</td>
				<td>
					<% bp.getController().writeFormLabel(out,"spesaAssociataRadioGroup"); %>
				</td>
				<td>
					<% bp.getController().writeFormInput( out, null, "spesaAssociataRadioGroup", false, null, "onClick=\"submitForm('doDefault')\""); %>
				</td>
			</tr>
		<%	} else { %>
			<tr>
				<% bp.getController().writeFormField(out,"fl_documentata"); %>
				<% bp.getController().writeFormField(out,"fl_reintegrata"); %>
			</tr>
		<%	} %>
		<TR>
			<TD><% bp.getController().writeFormLabel(out,"ds_spesa"); %></TD>
			<TD colspan="5"><% bp.getController().writeFormInput(out,"ds_spesa"); %></TD>
		</TR>
		<TR>
			<% bp.getController().writeFormField(out,"im_ammontare_spesa"); %>
			<% bp.getController().writeFormField(out,"dt_spesa"); %>
		</TR>
		<TR>
			<% bp.getController().writeFormField(out,"importoNettoSpesa"); %>
		</TR>
		<% if(bulk.isSpesa_documentata()) { %>
			<tr>
				<td colspan="6">
					<div class="GroupLabel">
						Dati Documento Passivo
					</div>
					<div class="Group">
						<table class="Panel">
							<tr>
								<td>
									<% bp.getController().writeFormLabel(out,"tipoDocumentoRadioGroup"); %>
								</td>
								<td>
									<% bp.getController().writeFormInput(out,"tipoDocumentoRadioGroup"); %>
								</td>
								<td><br></td>
								<td>
									<% bp.getController().writeFormInput(out,"cercaDocumento"); %>
								</td>
							</tr>
							<tr>
								<td>
									<% bp.getController().writeFormLabel(out,"pg_documento_amm"); %>
								</td>
								<td colspan="3">
									<% bp.getController().writeFormInput(out,"pg_documento_amm"); %>
								</td>
							</tr>
							<tr>
								<td>
									<% bp.getController().writeFormLabel(out,"cd_cds_doc_amm"); %>
								</td>
								<td colspan="3">
									<% bp.getController().writeFormInput(out,"cd_cds_doc_amm"); %>
								</td>
							</tr>
							<tr>
								<td>
									<% bp.getController().writeFormLabel(out,"cd_uo_doc_amm"); %>
								</td>
								<td colspan="3">
									<% bp.getController().writeFormInput(out,"cd_uo_doc_amm"); %>
								</td>
							</tr>
						</table>
					</div>
				</td>
			</tr>
		<% } else { %>
		      <tr>      	
					<% bp.getController().writeFormField(out,"dt_da_competenza_coge");%>
					<% bp.getController().writeFormField(out,"dt_a_competenza_coge");%>			  
		      </tr>
		<% } %>

		<TR>
			<TD colspan="6">
				<DIV class="GroupLabel">Dati Fornitore</div><div class="Group">
					<TABLE class="Panel">
						<%	if (bp.isSearching()) { %>
							<tr>
								<% bp.getController().writeFormField(out,"fornitoreSaltuarioRadioGroup"); %>
							</tr>
						<%	} else { %>
							<tr>
								<% bp.getController().writeFormField(out,"fl_fornitore_saltuario"); %>
							</tr>
						<%	} %>
						<TR>
							<TD><% bp.getController().writeFormLabel(out,"fornitore"); %></TD>
							<TD colspan="3"><% bp.getController().writeFormInput(out,"fornitore"); %></TD>
						</TR>
						<tr>
							<td><% bp.getController().writeFormLabel(out,"cd_precedente"); %></td>
							<td colspan="3"><% bp.getController().writeFormInput(out,"cd_precedente"); %></td>
						</tr>
						<TR>
							<TD><% bp.getController().writeFormLabel(out,"denominazione_fornitore"); %></TD>
							<TD colspan="3"><% bp.getController().writeFormInput(out,"denominazione_fornitore"); %></TD>
						</TR>
						<TR>
							<% bp.getController().writeFormField(out,"codice_fiscale"); %>
							<% bp.getController().writeFormField(out,"partita_iva"); %>
						</TR>
						<TR>
							<TD><% bp.getController().writeFormLabel(out,"indirizzo_fornitore"); %></TD>
							<TD colspan="3"><% bp.getController().writeFormInput(out,"indirizzo_fornitore"); %></TD>
						</TR>
						<TR>
							<% bp.getController().writeFormField(out,"citta"); %>
							<% bp.getController().writeFormField(out,"citta_provincia"); %>
						</TR>
						<TR>
							<% bp.getController().writeFormField(out,"citta_nazione"); %>
							<% bp.getController().writeFormField(
													out,
													((bulk.isFornitore_saltuario()) ?
																"cap_fornitore_saltuario" : 
																"cap_fornitore_non_saltuario")); %>
						</TR>
						<% if( bulk.isFornitore_saltuario() )  { %>
							<tr>
								<% bp.getController().writeFormField(out,"tel_fornitore"); %>
							</tr>
						<% } %>
						<TR>
							<TD><% bp.getController().writeFormLabel(out,"ds_fornitore"); %></TD>
							<TD colspan="3"><% bp.getController().writeFormInput(out,"ds_fornitore"); %></TD>
						</TR>
					</TABLE>
				</DIV>
			</TD>
		</TR>
		<% if(bulk.getFl_obbligazione() != null && bulk.getFl_obbligazione().booleanValue()) { %>
			<TR>
				<TD colspan="6">
					<DIV class="GroupLabel">Impegno Scadenzario</div><div class="Group">
						<TABLE class="Panel">
							<TR>
								<% bp.getController().writeFormField(out,"esercizio_ori_obbligazione"); %>
								<% bp.getController().writeFormField(out,"pg_obbligazione"); %>
							</TR>
							<TR>
								<% bp.getController().writeFormField(out,"dt_scadenza"); %>
								<% bp.getController().writeFormField(out,"im_scadenza"); %>
							</TR>
							<TR>
								<TD>
									<% bp.getController().writeFormLabel(out,"ds_scadenza"); %>
								</TD>
								<TD colspan="3">
									<% bp.getController().writeFormInput(out,"ds_scadenza"); %>
								</TD>
							</TR>
						</TABLE>
					</DIV>
				</TD>
			</TR>
		<% } %>
	</TABLE>
<%
	bp.closeFormWindow(pageContext);
%>
</body>