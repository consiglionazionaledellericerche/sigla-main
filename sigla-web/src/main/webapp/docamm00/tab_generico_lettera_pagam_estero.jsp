<%@ page 
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.docamm00.tabrif.bulk.*,
		it.cnr.contab.docamm00.docs.bulk.*,
		it.cnr.contab.docamm00.bp.*"
%>

<%	CRUDDocumentoGenericoPassivoBP bp = (CRUDDocumentoGenericoPassivoBP)BusinessProcess.getBusinessProcess(request);%>

<div class="Group" style="width:100%">
	<table width="100%">
		<tr>
			<% bp.getController().writeFormField(out,"creaLettera");%>
			<% bp.getController().writeFormField(out,"cancellaLettera");%>
		</tr>
	</table>
</div>
<div class="Group" style="width:100%">
	<table width="100%">
		<tr>
			<% bp.getController().writeFormField(out,"esercizio_lettera");%>
			<% bp.getController().writeFormField(out,"pg_lettera");%>
		</tr>
		<tr>
			<% bp.getController().writeFormField(out,"dt_registrazione_lettera");%>
		</tr>
		<tr>
			<% bp.getController().writeFormField(out,"im_pagamento");%>
		</tr>
		<tr>
			<% bp.getController().writeFormField(out,"im_commissioni_lettera");%>
		</tr>
		<tr>
			<td colspan="4">
				<div class="GroupLabel">Sospeso</div>
				<div class="Group" style="width:100%">
					<table width="100%">
						<tr>
							<% bp.getController().writeFormField(out,"cd_sospeso"); %>
							<td colspan="2">
								<% bp.getController().writeFormInput(out, "sospeso"); %>
							</td>
						</tr>
						<tr>
							<% bp.getController().writeFormField(out,"esercizio_sospeso"); %>
							<% bp.getController().writeFormField(out,"cd_cds_sospeso"); %>
						</tr>
					</table>
				</div>
			</td>
		</tr>
	</table>
</div>