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
	boolean roField = fondo.isOnlyForClose();
%>

<table class="Panel" width="100%">
	<tr>
		<% bp.getController().writeFormField( out, "mandato"); %>
	</tr>
	<tr>
	    <td><% bp.getController().writeFormLabel( out, "pg_reversale"); %></td>
		<td>
		    <% bp.getController().writeFormInput( out, "pg_reversale"); %>
			<% bp.getController().writeFormInput( out, "ds_reversale"); %>
		</td>
	</tr>

	<TR>
		<TD colspan="4">
			<DIV class="GroupLabel text-primary h3">Fondo</div>
			<div class="Group card p-2 w-100">
				<TABLE class="Panel w-100">
					<TR>
						<% bp.getController().writeFormField(out,"im_ammontare_fondo");%>
						<% bp.getController().writeFormField(out,"im_ammontare_iniziale");%>
					</TR>
					<TR>
						<% bp.getController().writeFormField(out,"im_totale_spese");%>
						<% bp.getController().writeFormField(out,"im_totale_reintegri");%>
					</TR>
					<TR>
						<% bp.getController().writeFormField(out,"im_residuo_fondo");%>
						<td>
							<% bp.getController().writeFormLabel(out, "im_limite_min_reintegro"); %>
						</td>
						<td>
							<% bp.getController().writeFormInput(out, null, "im_limite_min_reintegro", roField, null, ""); %>
						</td>
					</TR>
					<TR>
						<% bp.getController().writeFormField(out,"im_totale_netto_spese");%>
					</TR>
					<TR>
				 		<TD colspan="2"><center><DIV class="GroupLabel text-primary h3">Spese Documentate</div></center></TD>
				 		<TD colspan="2"><center><DIV class="GroupLabel text-primary h3">Spese non Documentate</div></center></TD>
				    </TR>
					<TR>
						<td>
							<% bp.getController().writeFormLabel(out, "im_max_mm_spesa_doc"); %>
						</td>
						<td>
							<% bp.getController().writeFormInput(out, null, "im_max_mm_spesa_doc", roField, null, ""); %>
						</td>
						<td>
							<% bp.getController().writeFormLabel(out, "im_max_mm_spesa_non_doc"); %>
						</td>
						<td>
							<% bp.getController().writeFormInput(out, null, "im_max_mm_spesa_non_doc", roField, null, ""); %>
						</td>
					</TR>
					<TR>
						<td>
							<% bp.getController().writeFormLabel(out, "im_max_gg_spesa_doc"); %>
						</td>
						<td>
							<% bp.getController().writeFormInput(out, null, "im_max_gg_spesa_doc", roField, null, ""); %>
						</td>
						<td>
							<% bp.getController().writeFormLabel(out, "im_max_gg_spesa_non_doc"); %>
						</td>
						<td>
							<% bp.getController().writeFormInput(out, null, "im_max_gg_spesa_non_doc", roField, null, ""); %>
						</td>
					</TR>
				</TABLE>
			</DIV>
		</TD>
	</TR>
</TABLE>