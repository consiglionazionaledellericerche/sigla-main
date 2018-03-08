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
					
				</td>
				<td>
					
				</td>
			</tr>	
	
			<tr>
				<td>
					<% bp.getDettaglio().writeFormLabel(out,"dt_versamento");%>
				</td>
				<td>
					<% bp.getDettaglio().writeFormInput(out,null,"dt_versamento",false,null,"");%>
				</td>
				
			</tr>
			<tr>
				<td>
					<% bp.getDettaglio().writeFormLabel(out,"abi");%>
				</td>
				<td>
					<% bp.getDettaglio().writeFormInput(out,null,"abi",false,null,"");%>
				</td>
				
			</tr>
			<tr>
				<td>
					<% bp.getDettaglio().writeFormLabel(out,"cab");%>
				</td>
				<td>
					<% bp.getDettaglio().writeFormInput(out,null,"cab",false,null,"");%>
				</td>
				
			</tr>
			<tr>
				<td>
					<% bp.getDettaglio().writeFormLabel(out,"cred_iva_infrann_rimb");%>
				</td>
				
				<td>
					<% bp.getDettaglio().writeFormInput(out,null,"cred_iva_infrann_rimb",false,null,"");%>
				</td>
			</tr>		
			<tr>
				<td>
					<% bp.getDettaglio().writeFormLabel(out,"cred_iva_infrann_comp");%>
				</td>
				
				<td>
					<% bp.getDettaglio().writeFormInput(out,null,"cred_iva_infrann_comp",false,null,"");%>
				</td>
			</tr>
				
			
			
			<tr>
				<td>
					<% bp.getDettaglio().writeFormLabel(out,"annotazioni");%>
				</td>
				<td>
					<% bp.getDettaglio().writeFormInput(out,null,"annotazioni",false,null,"");%>
				</td>
				
			</tr>			
		</table>
	</div>