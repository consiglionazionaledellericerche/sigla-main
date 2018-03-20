<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.inventario00.tabrif.bulk.*,
		it.cnr.contab.inventario01.bulk.*,
		it.cnr.contab.inventario01.bp.*"
%>
<% CRUDCaricoScaricoInventarioBP bp = (CRUDCaricoScaricoInventarioBP)BusinessProcess.getBusinessProcess(request);
	Buono_carico_scaricoBulk buonoCarico = (Buono_carico_scaricoBulk)bp.getModel(); %>
	<table>			
		<tr>
			<td>
				<% bp.getController().writeFormLabel(out,"esercizio"); %>
			</td>
			<td>
				<% bp.getController().writeFormInput(out,"esercizio"); %>
			</td>
		</tr>				
		<tr>
			<td>
				<% bp.getController().writeFormLabel(out,"pg_buono_c_s"); %>
			</td>
			<td>
				<% bp.getController().writeFormInput(out,"pg_buono_c_s"); %>
			</td>
		</tr>

		<tr>
			<td>
				<% bp.getController().writeFormLabel(out,"cd_uo_consegnataria"); %>
			</td>
			<td colspan="2">
				<% bp.getController().writeFormInput(out,"cd_uo_consegnataria"); %>
				<% bp.getController().writeFormInput(out,"ds_uo_consegnataria"); %>
			</td>
		</tr>
		<tr>
		
			<td >
				<% bp.getController().writeFormLabel(out,"cd_consegnatario"); %>
			</td>
			<td colspan="2">
				<% bp.getController().writeFormInput(out,"cd_consegnatario"); %>
				<% bp.getController().writeFormInput(out,"cognome_consegnatario"); %>
			</td>
		</tr>
		<tr>
			<td>
				<% bp.getController().writeFormLabel(out,"cd_delegato"); %>
			</td>
			<td colspan="2">
				<% bp.getController().writeFormInput(out,"cd_delegato"); %>
				<% bp.getController().writeFormInput(out,"cognome_delegato"); %>
			</td>
		</tr>

		<tr>
			<td>
				<% bp.getController().writeFormLabel(out,"pg_inventario"); %>
			</td>
			<td  colspan="4">
				<% bp.getController().writeFormInput(out,"pg_inventario"); %>
				<% bp.getController().writeFormLabel(out,"data_registrazione"); %>				
				<% bp.getController().writeFormInput(out,null,"data_registrazione",bp.isEditing(),null,null); %>
			</td>
		</tr>
		<tr>
			<td>
				<% bp.getController().writeFormLabel(out,"tipoMovimento"); %>
			</td>
			<td colspan="6">
				<% bp.getController().writeFormInput(out,null,"tipoMovimento",bp.isEditing()&&((buonoCarico.getBuono_carico_scarico_dettColl()!=null)&&((Buono_carico_scarico_dettBulk)buonoCarico.getBuono_carico_scarico_dettColl().get(0)).isROValore_unitario()||
				 (buonoCarico.getTipoMovimento().getFl_buono_per_trasferimento().booleanValue())),null,"onChange=\"submitForm('doSelezionaTipoMovimento')\""); %>
			</td>
		</tr>
		<tr>
			<td>
				<% bp.getController().writeFormLabel(out,"ds_buono_carico_scarico"); %>
			</td>
			<td colspan="8">
				<% bp.getController().writeFormInput(out,"ds_buono_carico_scarico"); %>
			</td>
		</tr>

	</table>	
