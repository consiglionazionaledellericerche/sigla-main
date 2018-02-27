<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.contab.inventario00.bp.*"
%>
<% AssBeneFatturaBP bp = (AssBeneFatturaBP)BusinessProcess.getBusinessProcess(request); %>
	
	<table>			
		<tr>
			<td>
				<% bp.getBuonoController().writeFormLabel(out,"esercizio"); %>
			</td>
			<td>
				<% bp.getBuonoController().writeFormInput(out,"esercizio"); %>
			</td>
		</tr>				
		<tr>
			<td>
				<% bp.getBuonoController().writeFormLabel(out,"pg_buono_c_s"); %>
			</td>
			<td>
				<% bp.getBuonoController().writeFormInput(out,"pg_buono_c_s"); %>
			</td>
		</tr>
		<tr>
			<td>
				<% bp.getBuonoController().writeFormLabel(out,"cd_uo_consegnataria"); %>
			</td>
			<td colspan="2">
				<% bp.getBuonoController().writeFormInput(out,"cd_uo_consegnataria"); %>
				<% bp.getBuonoController().writeFormInput(out,"ds_uo_consegnataria"); %>
			</td>
		</tr>
		<tr>
			<td>
				<% bp.getBuonoController().writeFormLabel(out,"cd_consegnatario"); %>
			</td>
			<td colspan="2">
				<% bp.getBuonoController().writeFormInput(out,"cd_consegnatario"); %>
				<% bp.getBuonoController().writeFormInput(out,"cognome_consegnatario"); %>
			</td>
		</tr>
		<tr>
			<td>
				<% bp.getBuonoController().writeFormLabel(out,"cd_delegato"); %>
			</td>
		<td colspan="2">
				<% bp.getBuonoController().writeFormInput(out,"cd_delegato"); %>
				<% bp.getBuonoController().writeFormInput(out,"cognome_delegato"); %>
			</td>
		</tr>
		<tr>
			<td>
				<% bp.getBuonoController().writeFormLabel(out,"pg_inventario"); %>
			</td>
		   <td  colspan="4">
				<% bp.getBuonoController().writeFormInput(out,"pg_inventario"); %>
				<% bp.getBuonoController().writeFormLabel(out,"data_registrazione"); %>
				<% bp.getBuonoController().writeFormInput(out,"data_registrazione"); %>
			</td>
		</tr>
		<tr>
			<td>
				<% bp.getBuonoController().writeFormLabel(out,"tipoMovimento"); %>
			</td>
			<td colspan="6">
				<% bp.getBuonoController().writeFormInput(out,"tipoMovimento"); %>
			</td>
		</tr>
		<tr>
			<td>
				<% bp.getBuonoController().writeFormLabel(out,"ds_buono_carico_scarico"); %>
			</td>
			<td colspan="8">
				<% bp.getBuonoController().writeFormInput(out,"ds_buono_carico_scarico"); %>
			</td>
		</tr>		
	</table>	
