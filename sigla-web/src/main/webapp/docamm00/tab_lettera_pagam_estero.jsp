<%@ page 
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.docamm00.tabrif.bulk.*,
		it.cnr.contab.docamm00.docs.bulk.*,
		it.cnr.contab.docamm00.bp.*"
%>

<%	CRUDFatturaPassivaIBP bp = (CRUDFatturaPassivaIBP)BusinessProcess.getBusinessProcess(request);
	Fattura_passivaBulk fatturaPassiva = (Fattura_passivaBulk)bp.getModel(); %>

<div class="Group" style="width:100%">
	<table width="100%">
		<tr>
			<% bp.writeFormFieldDoc1210(out,"creaLettera");%>
			<% bp.writeFormFieldDoc1210(out,"cancellaLettera");%>
		</tr>
	</table>
</div>
<div class="Group" style="width:100%">
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
							<% bp.getController().writeFormField(out, "bonifico_mezzo");%>
						</tr>
						<tr>
							<% bp.getController().writeFormField(out, "divisa");%>
						</tr>
						<tr>
							<% bp.getController().writeFormField(out, "beneficiario");%>
						</tr>
						<tr>
							<% bp.getController().writeFormField(out, "num_conto_ben");%>
						</tr>
						<tr>
							<% bp.getController().writeFormField(out, "iban");%>
						</tr>
						<tr>
							<% bp.getController().writeFormField(out, "indirizzo");%>
						</tr>
						<tr>
							<% bp.getController().writeFormField(out, "indirizzo_swift");%>
						</tr>
						<tr>
							<% bp.getController().writeFormField(out, "motivo_pag");%>
						</tr>
						<tr>
							<% bp.getController().writeFormField(out, "ammontare_debito");%>
						</tr>
						<tr>
							<% bp.getController().writeFormField(out, "conto_debito");%>
						</tr>
						<tr>
							<% bp.getController().writeFormField(out, "commissioni_spese");%>
						</tr>
						<tr>
							<% bp.getController().writeFormField(out, "commissioni_spese_estere");%>
						</tr>
					</table>
				</div>
			</td>
		</tr>		
		<tr>
			<td colspan="4">
				<div class="GroupLabel">Sospeso</div>
				<div class="Group" style="width:100%">
					<table width="100%">
						<tr>
							<% bp.writeFormFieldDoc1210(out,"cd_sospeso"); %>
							<td colspan="2">
								<% bp.writeFormInputDoc1210(out, "sospeso"); %>
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