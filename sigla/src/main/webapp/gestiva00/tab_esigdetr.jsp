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
					<% bp.getDettaglio().writeFormLabel(out,"iva_vendite");%>
				</td>
				<td>
					<% bp.getDettaglio().writeFormInput(out,null,"iva_vendite",false,null,"");%>
				</td>
				<td>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getDettaglio().writeFormLabel(out,"iva_vendite_diff");%>
				</td>
				<td>
					<% bp.getDettaglio().writeFormInput(out,null,"iva_vendite_diff",false,null,"");%>
				</td>
				<td>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getDettaglio().writeFormLabel(out,"iva_vend_diff_esig");%>
				</td>
				<td>
					<% bp.getDettaglio().writeFormInput(out,null,"iva_vend_diff_esig",false,null,"");%>
				</td>
				<td>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getDettaglio().writeFormLabel(out,"iva_autofatt");%>
				</td>
				<td>
					<% bp.getDettaglio().writeFormInput(out,null,"iva_autofatt",false,null,"");%>
				</td>
				<td>
				</td>
			</tr>	
			<tr>
				<td>
					<% bp.getDettaglio().writeFormLabel(out,"iva_intraue");%>
				</td>
				<td>
					<% bp.getDettaglio().writeFormInput(out,null,"iva_intraue",false,null,"");%>
				</td>
				<td>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"totale_vendite");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"totale_vendite",false,null,"");%>
				</td>
				<td>
				</td>
			</tr>
			<tr></tr>
			<tr>
				<td>
					<% bp.getDettaglio().writeFormLabel(out,"iva_acquisti");%>
				</td>
				<td>
				</td>
				<td>
					<% bp.getDettaglio().writeFormInput(out,null,"iva_acquisti",false,null,"");%>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getDettaglio().writeFormLabel(out,"iva_acq_non_detr");%>
				</td>
				<td>
				</td>
				<td>
					<% bp.getDettaglio().writeFormInput(out,null,"iva_acq_non_detr",false,null,"");%>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getDettaglio().writeFormLabel(out,"iva_acquisti_diff");%>
				</td>
				<td>
				</td>
				<td>
					<% bp.getDettaglio().writeFormInput(out,null,"iva_acquisti_diff",false,null,"");%>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getDettaglio().writeFormLabel(out,"iva_acq_diff_esig");%>
				</td>
				<td>
				</td>
				<td>
					<% bp.getDettaglio().writeFormInput(out,null,"iva_acq_diff_esig",false,null,"");%>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"totale_acquisti");%>
				</td>
				<td>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"totale_acquisti",false,null,"");%>
				</td>				
			</tr>
		</table>
	</div>