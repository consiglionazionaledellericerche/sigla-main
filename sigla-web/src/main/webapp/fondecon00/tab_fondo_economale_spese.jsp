<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		java.util.*,
		it.cnr.contab.fondecon00.bp.*,
		it.cnr.contab.fondecon00.core.bulk.*,
		it.cnr.contab.anagraf00.core.bulk.*,
		it.cnr.contab.anagraf00.tabrif.bulk.*"
%>

<%
	FondoEconomaleBP bp = (FondoEconomaleBP)BusinessProcess.getBusinessProcess(request);
	Fondo_economaleBulk fondo = (Fondo_economaleBulk)bp.getModel();
	TerzoBulk economo = fondo.getEconomo();
%>

<TABLE class="Panel" width="100%">
	<tr>
		<td>
			<DIV class="GroupLabel">Ricerca scadenze</div>
			<div class="Group" style="width:100%">
			<table width="100%">
				<tr>
					<td>
						<table>
							<tr>
								<% bp.getController().writeFormField( out, "esercizio_ori_obbligazione_scadenza_ricerca"); %>
								<% bp.getController().writeFormField( out, "pg_obbligazione_scadenza_ricerca"); %>
								<% bp.getController().writeFormField( out, "pg_obbligazione_scadenzario_scadenza_ricerca"); %>
							</tr>
							<tr>
								<% bp.getController().writeFormField( out, "ds_scadenza_scadenza_ricerca"); %>
								<td>
									<% bp.getController().writeFormInput( out, "scadenza_ricerca"); %>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<DIV class="GroupLabel">Creditore</div>
						<div class="Group" style="width:100%">
						<table>
							<tr>
								<% bp.getController().writeFormField( out, "cd_creditore_scadenza"); %>
								<td colspan="2">
									<% bp.getController().writeFormInput( out, "ds_creditore_scadenza"); %>
								</td>
								<td>
									<% bp.getController().writeFormInput( out, "creditore_scadenza"); %>
								</td>
							</tr>
							<tr>
								<% bp.getController().writeFormField( out, "cd_precedente_creditore"); %>
							</tr>
							<tr>
								<% bp.getController().writeFormField( out, "codice_fiscale_creditore"); %>
								<% bp.getController().writeFormField( out, "partita_iva_creditore"); %>
							</tr>
						</table>
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<table>
							<tr>
								<% bp.getController().writeFormField( out, "cd_cds_scadenza_ricerca"); %>
								<% bp.getController().writeFormField( out, "dt_scadenza_scadenza_ricerca"); %>
							</tr>
							<tr>
								<% bp.getController().writeFormField( out, "im_scadenza_scadenza_ricerca"); %>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			</div>
		</td>
	</tr>
	<tr>
		<td>
			<DIV class="GroupLabel">Filtri spese</div>
			<div class="Group" style="width:100%">
			<table width="100%">
				<tr>
					<% bp.getController().writeFormField( out, "spesaDocumentataRadioGroup"); %>
					<% bp.getController().writeFormField( out, "spesaReintegrataRadioGroup"); %>
				</tr>
			</table>
			</div>
		</td>
	</tr>
	<tr>
		<td>
			<DIV class="GroupLabel">Spese</div>
			<div class="Group" style="width:100%">
			<table width="100%">
				<tr>
					<td>
						<%JSPUtils.button(out, "img/information24.gif", "img/information24.gif", "Calcola totale", "if (disableDblClick()) javascript:submitForm('doCalcolaTotaleSpesePerObblig')", null, (bp.isEditing() && fondo.getScadenza_ricerca() != null), bp.getParentRoot().isBootstrap());%>
					</td>
					<% bp.getController().writeFormField( out, "importo_totale_scadenze_non_doc"); %>
					<td>
						<%JSPUtils.button(out, "img/search24.gif", "img/search24.gif", "Ricerca spese", "if (disableDblClick()) javascript:submitForm('doRicercaSpeseAssociate')", null, (bp.isEditing() && fondo.getScadenza_ricerca() != null), bp.getParentRoot().isBootstrap());%>
					</td>
				</tr>
				<tr>
					<td colspan="4">
					<%	if (fondo.getSquared() != null) {
							if (!fondo.getSquared().booleanValue()) { %>
									<span class="FormLabel" style="color:red">
										L'importo delle spese non documentate non è in quadratura con l'importo della scadenza!
									</span>
							<%	} else { %>
									<span class="FormLabel" style="color:blu">
										L'importo delle spese non documentate è in quadratura con l'importo della scadenza!
									</span>
					<%		}
						} %>
					</td>
				</tr>
			</table>
			</div>
		</td>
	</tr>
</TABLE>