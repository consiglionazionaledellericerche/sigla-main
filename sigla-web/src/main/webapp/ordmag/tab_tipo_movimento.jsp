<%@ page pageEncoding="UTF-8"
	import = "it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.ordmag.ordini.bp.CRUDTipoMovimentoMagBP,
		it.cnr.contab.ordmag.anag00.TipoMovimentoMagBulk,
		it.cnr.contab.ordmag.anag00.*"
%>

<% CRUDBP bp = (CRUDTipoMovimentoMagBP)BusinessProcess.getBusinessProcess(request);
TipoMovimentoMagBulk ordine = (TipoMovimentoMagBulk)bp.getModel();
%>
<div class="Group card p-2 m-1">
	<table class="w-100" cellpadding="2">
		<tr>				
			<td>
				<% bp.getController().writeFormField(out, "cdTipoMovimento");%>																		
			</td>
		</tr>
		<tr>				
			<td>
				<% bp.getController().writeFormField( out, "dsTipoMovimento"); %>
			</td>
		</tr>
		<tr>				
			<td>
				<% bp.getController().writeFormField( out, "tipo"); %>
			</td>
		</tr>
		<tr>				
			<td>
				<% bp.getController().writeFormField( out, "segno"); %>				
			</td>
		</tr>
		<tr>
			<td>
				<% bp.getController().writeFormField(out, "findTipoMovimentoStorno");%>
			</td>				
		</tr>	
		<tr>
			<td>
				<% bp.getController().writeFormField(out, "findTipoMovimentoAlt");%>
			</td>	
		</tr>	
		<tr>
			<td>
				<% bp.getController().writeFormField(out, "dtCancellazione");%>
			</td>	
		</tr>	
	</table>
</div> 
