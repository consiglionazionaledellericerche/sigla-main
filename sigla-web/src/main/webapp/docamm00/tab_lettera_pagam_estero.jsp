<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.docamm00.tabrif.bulk.*,
		it.cnr.contab.docamm00.docs.bulk.*,
		it.cnr.contab.doccont00.core.bulk.*,
		it.cnr.contab.docamm00.bp.*"
%>

<%	CRUDFatturaPassivaIBP bp = (CRUDFatturaPassivaIBP)BusinessProcess.getBusinessProcess(request);
	Fattura_passivaBulk fatturaPassiva = (Fattura_passivaBulk)bp.getModel(); %>

<div class="Group card p-3" style="width:100%">
	<table width="100%">
		<tr>
			<% bp.writeFormFieldDoc1210(out,"creaLettera");%>
			<% bp.writeFormFieldDoc1210(out,"cancellaLettera");%>
			<% bp.writeFormFieldDoc1210(out,"disassociaLettera");%>
		</tr>
	</table>
</div>
<div class="Group card p-3" style="width:100%">
	<div class="GroupLabel h3 text-primary"><% bp.writeFormInput(out, "displayStatoTrasmissione");%></div>
	<table width="100%">
		<tr>
			<% bp.writeFormFieldDoc1210(out,"esercizio_lettera");%>
			<% bp.writeFormFieldDoc1210(out,"pg_lettera");%>
		</tr>
		<tr>
			<% bp.writeFormFieldDoc1210(out,"dt_registrazione_lettera");%>
		</tr>
		<tr>
			<% bp.writeFormFieldDoc1210(out,"im_pagamento");%>
		</tr>
		<tr>
			<% bp.writeFormFieldDoc1210(out,"im_commissioni_lettera");%>
		</tr>
		<tr>
			<td colspan="4">
				<div class="GroupLabel">Stampa documento</div>
				<div class="Group" style="width:90%">
					<table>
						<tr>
							<% bp.writeFormFieldDoc1210(out, "bonifico_mezzo");%>
						</tr>
						<tr>
							<% bp.writeFormFieldDoc1210(out, "divisa");%>
						</tr>
						<tr>
							<% bp.writeFormFieldDoc1210(out, "beneficiario");%>
						</tr>
						<tr>
							<% bp.writeFormFieldDoc1210(out, "num_conto_ben");%>
						</tr>
						<tr>
							<% bp.writeFormFieldDoc1210(out, "iban");%>
						</tr>
						<tr>
							<% bp.writeFormFieldDoc1210(out, "indirizzo");%>
						</tr>
						<tr>
							<% bp.writeFormFieldDoc1210(out, "indirizzo_swift");%>
						</tr>
						<tr>
							<% bp.writeFormFieldDoc1210(out, "motivo_pag");%>
						</tr>
						<tr>
							<% bp.writeFormFieldDoc1210(out, "ammontare_debito");%>
						</tr>
						<tr>
							<% bp.writeFormFieldDoc1210(out, "conto_debito");%>
						</tr>
						<tr>
							<% bp.writeFormFieldDoc1210(out, "commissioni_spese");%>
						</tr>
						<tr>
							<% bp.writeFormFieldDoc1210(out, "commissioni_spese_estere");%>
						</tr>
					</table>
				</div>
			</td>
		</tr>
		<tr>
			<td colspan="4">
				<div class="GroupLabel h3 text-primary">Istruzioni Speciali</div>
				<div class="Group card" style="width:100%">
					<table width="100%">
						<tr>
							<% bp.writeFormFieldDoc1210(out, "istruzioni_speciali_1");%>
						</tr>
						<tr>
							<% bp.writeFormFieldDoc1210(out, "istruzioni_speciali_2");%>
						</tr>
						<tr>
							<% bp.writeFormFieldDoc1210(out, "istruzioni_speciali_3");%>
						</tr>
					</table>
				</div>
			</td>
		</tr>

		<tr>
			<td colspan="4">
				<div class="GroupLabel h3 text-primary">Sospeso</div>
				<div class="Group card" style="width:100%">
					<table width="100%">
						<tr>
							<% bp.writeFormFieldDoc1210(out,"cd_sospeso"); %>
							<td colspan="2">
								<% bp.writeFormInput(out,null, "sospeso",(fatturaPassiva.getLettera_pagamento_estero()!=null && fatturaPassiva.getLettera_pagamento_estero().getStato_trasmissione().compareTo(MandatoBulk.STATO_TRASMISSIONE_TRASMESSO)!=0),null,""); %>
							</td>

						</tr>
						<tr>
							<% bp.writeFormFieldDoc1210(out,"esercizio_sospeso"); %>
							<% bp.writeFormFieldDoc1210(out,"cd_cds_sospeso"); %>
						</tr>
					</table>
				</div>
			</td>
		</tr>
	</table>
</div>