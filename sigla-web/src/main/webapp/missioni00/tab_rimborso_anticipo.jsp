<%@ page pageEncoding="UTF-8"  import = "it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.missioni00.bp.*, it.cnr.contab.missioni00.docs.bulk.*, it.cnr.jada.bulk.*"%>

<%  
	CRUDAnticipoBP bp = (CRUDAnticipoBP)BusinessProcess.getBusinessProcess(request);
	AnticipoBulk anticipo = (AnticipoBulk) bp.getModel();

	if(anticipo == null)
		anticipo = new AnticipoBulk();
%>

<div class="Group" style="width:100%">
<table width="100%">
	<tr>
   		<td><% bp.getController().writeFormLabel(out,"find_latt");%></td>
   		<td>
   		    <% bp.getController().writeFormInput(out,"cd_linea_attivita");%>
   		    <% bp.getController().writeFormInput(out,"cd_centro_responsabilita");%>
   		    <% bp.getController().writeFormInput(out,"ds_linea_attivita");%>
			<% bp.getController().writeFormInput(out,"find_latt"); %>
   		</td>   		
	</tr>

	<tr>
   		<td><% bp.getController().writeFormLabel(out,"rimborso_pg_rimborso");%></td>
   		<td><% bp.getController().writeFormInput(out,"rimborso_pg_rimborso");%></td>   		
	</tr>
		<tr>
   		<td><% bp.getController().writeFormLabel(out,"rimborso_dt_registrazione");%></td>
   		<td><% bp.getController().writeFormInput(out,"rimborso_dt_registrazione");%></td>   		
	</tr>
	<tr>
   		<td><% bp.getController().writeFormLabel(out,"rimborso_ds_rimborso");%></td>
   		<td><% bp.getController().writeFormInput(out,"rimborso_ds_rimborso");%></td>   		
	</tr>
	<tr>
   		<td><% bp.getController().writeFormLabel(out,"rimborso_im_rimborso");%></td>
   		<td><% bp.getController().writeFormInput(out,"rimborso_im_rimborso");%></td>   		
	</tr>
</table>
</div>
	
<div class="Group" style="width:100%">
<table>	
	<tr>
   		<td><% bp.getController().writeFormLabel(out,"rimborso_cd_cds_accertamento");%></td>
   		<td><% bp.getController().writeFormInput(out,"rimborso_cd_cds_accertamento");%></td>   		
	</tr>
	<tr>
   		<td><% bp.getController().writeFormLabel(out,"rimborso_esercizio_ori_accertamento");%></td>
   		<td><% bp.getController().writeFormInput(out,"rimborso_esercizio_ori_accertamento");%></td>   		
	</tr>
	<tr>
   		<td><% bp.getController().writeFormLabel(out,"rimborso_pg_accertamento");%></td>
   		<td><% bp.getController().writeFormInput(out,"rimborso_pg_accertamento");%></td>   		
	</tr>
</table>
</div>