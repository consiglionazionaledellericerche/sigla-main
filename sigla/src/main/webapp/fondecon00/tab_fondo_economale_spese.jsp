<%@ page pageEncoding="UTF-8"
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

<table class="Panel w-100">
	<tr>
		<td>
			<div class="GroupLabel text-primary h3 pl-2">Ricerca scadenze</div>
			<div class="Group card p-2 w-100">
			<table class="w-100">
				<tr>
                    <td class="w-10"><% bp.getController().writeFormLabel( out, "scadenza_ricerca"); %></td>
                    <td><% bp.getController().writeFormInput( out, "scadenza_ricerca"); %></td>
				</tr>
				<tr>
					<td colspan="2">
						<DIV class="GroupLabel text-primary h3">Creditore</div>
						<div class="Group card p-2 w-100">
                            <table class="w-100">
                                <tr>
                                    <td class="w-10"><% bp.getController().writeFormLabel( out, "creditore_scadenza"); %></td>
                                    <td><% bp.getController().writeFormInput( out, "creditore_scadenza"); %></td>
                                </tr>
                                <tr>
                                    <% bp.getController().writeFormField( out, "cd_precedente_creditore"); %>
                                </tr>
                                <tr>
                                    <% bp.getController().writeFormField( out, "codice_fiscale_creditore"); %>
                                </tr>
                                <tr>
                                    <% bp.getController().writeFormField( out, "partita_iva_creditore"); %>
                                </tr>
                            </table>
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<table class="w-100">
							<tr>
							    <td class="text-right pr-2"><% bp.getController().writeFormLabel( out, "cd_cds_scadenza_ricerca"); %></td>
								<td><% bp.getController().writeFormInput( out, "cd_cds_scadenza_ricerca"); %></td>
								<td class="text-right pr-2"><% bp.getController().writeFormLabel( out, "dt_scadenza_scadenza_ricerca"); %></td>
								<td><% bp.getController().writeFormInput( out, "dt_scadenza_scadenza_ricerca"); %></td>
								<td class="text-right pr-2"><% bp.getController().writeFormLabel( out, "im_scadenza_scadenza_ricerca"); %></td>
								<td><% bp.getController().writeFormInput( out, "im_scadenza_scadenza_ricerca"); %></td>
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
			<DIV class="GroupLabel text-primary h3 pl-2">Filtri spese</div>
			<div class="Group card p-2 w-100">
			<table class="w-100">
				<tr>
					<td class="text-right pr-2"><% bp.getController().writeFormLabel( out, "spesaDocumentataRadioGroup"); %></td>
                    <td><% bp.getController().writeFormInput( out, "spesaDocumentataRadioGroup"); %></td>
					<td class="text-right pr-2"><% bp.getController().writeFormLabel( out, "spesaReintegrataRadioGroup"); %></td>
                    <td><% bp.getController().writeFormInput( out, "spesaReintegrataRadioGroup"); %></td>
				</tr>
			</table>
			</div>
		</td>
	</tr>
	<tr>
		<td>
			<DIV class="GroupLabel text-primary h3 pl-2">Spese</div>
			<div class="Group card p-2 w-100">
			<table class="w-100">
				<tr>
					<td>
						<%JSPUtils.button(out,
						    bp.getParentRoot().isBootstrap()? "fa fa-fw fa-2x fa-calculator": "img/information24.gif",
						    bp.getParentRoot().isBootstrap()? "fa fa-fw fa-2x fa-calculator": "img/information24.gif",
						    "Calcola totale",
						    "if (disableDblClick()) javascript:submitForm('doCalcolaTotaleSpesePerObblig')",
						    "btn-block btn-title btn-outline-primary",
						    (bp.isEditing() && fondo.getScadenza_ricerca() != null),
						    bp.getParentRoot().isBootstrap());%>
					</td>
					<td class="text-right pr-2"><% bp.getController().writeFormLabel( out, "importo_totale_scadenze_non_doc"); %></td>
					<td><% bp.getController().writeFormInput( out, "importo_totale_scadenze_non_doc"); %></td>
					<td>
						<%JSPUtils.button(out,
						    bp.getParentRoot().isBootstrap()? "fa fa-fw fa-2x fa-search": "img/search24.gif",
						    bp.getParentRoot().isBootstrap()? "fa fa-fw fa-2x fa-search": "img/search24.gif",
						    "Ricerca spese",
						    "if (disableDblClick()) javascript:submitForm('doRicercaSpeseAssociate')",
						    "btn-block btn-title btn-outline-primary",
						    (bp.isEditing() && fondo.getScadenza_ricerca() != null),
						    bp.getParentRoot().isBootstrap());%>
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
</table>