<%@ page pageEncoding="UTF-8"
	import = "it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.ordmag.ordini.bp.CRUDTipoMovimentoMagBP,
		it.cnr.contab.ordmag.anag00.TipoMovimentoMagBulk,
		it.cnr.contab.ordmag.anag00.*"
%>

<% 
CRUDTipoMovimentoMagBP bp = (CRUDTipoMovimentoMagBP)BusinessProcess.getBusinessProcess(request);
TipoMovimentoMagAzBulk azioni = (TipoMovimentoMagAzBulk)bp.getRigheMovimentoAzioni().getModel();
/* bp.getRigheMovimentoAzioni().writeHTMLTable(pageContext,"default",true,false,true,"100%","140px"); */

%>
<div class="Group card p-2 m-1">
	<table class="w-100" cellpadding="2">
		<tr>				
			<td>
				<% bp.getRigheMovimentoAzioni().writeFormField(out, "aggDataUltimoConsumo");%>																		
			</td>
			<td>
				<% bp.getRigheMovimentoAzioni().writeFormField(out, "aggTipmovUltimoConsumo");%>																		
			</td>
		</tr>
		<tr>				
			<td>
				<% bp.getRigheMovimentoAzioni().writeFormField(out, "flMovimentaLottiBloccati");%>																		
			</td>
			<td>
				<% bp.getRigheMovimentoAzioni().writeFormField(out, "modAggProgrValCarichi");%>																		
			</td>			
		</tr>			
	</table>
</div> 