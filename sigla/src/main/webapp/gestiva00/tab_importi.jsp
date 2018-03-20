<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.gestiva00.bp.*"
%>

<% LiquidazioneIvaBP bp = (LiquidazioneIvaBP)BusinessProcess.getBusinessProcess(request);%>
	<div class="Group" style="width:100%">
		<table width="100%">	
			<tr>
				<td>					
				</td>
				<td>
					<span class="FormLabel">Debito</span>
				</td>
				<td>
					<span class="FormLabel">Credito</span>
				</td>
			</tr>	
	
			<tr>
				<td>
					<% bp.getDettaglio().writeFormLabel(out,"var_imp_per_prec");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"variazioni_imposta_deb",false,null,"");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"variazioni_imposta_cre",false,null,"");%>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getDettaglio().writeFormLabel(out,"iva_liq_esterna");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"variazioni_imposta_esterna_deb",false,null,"");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"variazioni_imposta_esterna_cre",false,null,"");%>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getDettaglio().writeFormLabel(out,"iva_non_vers_per_prec");%>
				</td>
				<td>
					<% bp.getDettaglio().writeFormInput(out,null,"iva_non_vers_per_prec",false,null,"");%>
				</td>
				<td>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getDettaglio().writeFormLabel(out,"iva_deb_cred_per_prec");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"imp_der_per_prec_deb",false,null,"");%>
				</td>
				<td>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getDettaglio().writeFormLabel(out,"iva_deb_cred_per_prec");%>
				</td>
				<td>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"imp_der_per_prec_cre",false,null,"");%>
				</td>				
			</tr>
			<tr>
				<td>
					<% bp.getDettaglio().writeFormLabel(out,"cred_iva_infrann_comp");%>
				</td>
				<td>
				</td>
				<td>
					<% bp.getDettaglio().writeFormInput(out,null,"cred_iva_infrann_comp",false,null,"");%>
				</td>
			</tr>			
			<tr>
				<td>
					<% bp.getDettaglio().writeFormLabel(out,"iva_deb_cred");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"iva_dovuta_deb",false,null,"");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"iva_dovuta_cre",false,null,"");%>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getDettaglio().writeFormLabel(out,"int_deb_liq_trim");%>
				</td>
				<td>
					<% bp.getDettaglio().writeFormInput(out,null,"int_deb_liq_trim",false,null,"");%>
				</td>
				<td>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getDettaglio().writeFormLabel(out,"cred_iva_spec_detr");%>
				</td>
				<td>
				</td>
				<td>
					<% bp.getDettaglio().writeFormInput(out,null,"cred_iva_spec_detr",false,null,"");%>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getDettaglio().writeFormLabel(out,"acconto_iva_vers");%>
				</td>
				<td>
				</td>
				<td>
					<% bp.getDettaglio().writeFormInput(out,null,"acconto_iva_vers",false,null,"");%>
				</td>
			</tr>
						<tr>
				<td>
					<% bp.getDettaglio().writeFormLabel(out,"iva_da_versare");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"imp_da_vers_deb",false,null,"");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"imp_da_vers_cre",false,null,"");%>
				</td>
			</tr>			
			<tr>
				<td>
					<% bp.getDettaglio().writeFormLabel(out,"iva_versata");%>
				</td>
				<td>
					<% bp.getDettaglio().writeFormInput(out,null,"iva_versata",false,null,"");%>
				</td>
				<td>
				</td>
			</tr>			
		</table>
	</div>