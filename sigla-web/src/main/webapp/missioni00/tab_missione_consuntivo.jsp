<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"  import = "it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.missioni00.bp.*, it.cnr.contab.missioni00.docs.bulk.*"%>

<%  
	CRUDMissioneBP bp = (CRUDMissioneBP)BusinessProcess.getBusinessProcess(request);
%>

<div class="Group" style="width:100%">
<table width="100%">
	<tr></tr>
	<tr></tr>
		
	<tr>
  	<td><% bp.getController().writeFormLabel( out, "giorno_consuntivo");%></td>	
	<td><% bp.getBulkInfo().writeFormInput(out,bp.getModel(),null,"giorno_consuntivo",false,null,"onChange=\"submitForm('doOnGiornoConsuntivoChange')\"",bp.getInputPrefix(),2,bp.getFieldValidationMap(), bp.getParentRoot().isBootstrap());%></td>  	
	<td></td>  	
	<td></td>  	
	<td></td>  	
	<td></td>  			
  	</tr>
  	
  	<tr>
	<td><% bp.getController().writeFormLabel( out, "totale_spese_del_giorno"); %></td>
	<td><% bp.getController().writeFormInput( out, "totale_spese_del_giorno"); %></td>
	<td><% bp.getController().writeFormLabel( out, "totale_netto_diaria_del_giorno"); %></td>
	<td><% bp.getController().writeFormInput( out, "totale_netto_diaria_del_giorno"); %></td>
	<td><% bp.getController().writeFormLabel( out, "totale_esente_diaria_del_giorno"); %></td>
	<td><% bp.getController().writeFormInput( out, "totale_esente_diaria_del_giorno"); %></td>
	</tr>
	
	<tr></tr>
	<tr></tr>	
</table>

<table width="100%">
	<tr></tr>
	<tr></tr>

	<tr>
	<td><% bp.getConsuntivoController().writeHTMLTable(pageContext, "dettaglioSpesaSet",false,false,false,"700px","100px");%></td>
	</tr>

	<tr></tr>
	<tr></tr>
</table>
</div>

<div class="Group" style="width:100%">
<table width="100%">
	<tr></tr>
	<tr></tr>

	<tr>
  	<td><span>TOTALE :  </span></td>
  	<td></td>
	<td><% bp.getController().writeFormLabel( out, "im_totale_missione"); %></td>
	<td><% bp.getController().writeFormInput( out, "im_totale_missione"); %></td>
	<td><% bp.getController().writeFormLabel( out, "im_netto_pecepiente"); %></td>
	<td><% bp.getController().writeFormInput( out, "im_netto_pecepiente"); %></td>
	<td><% bp.getController().writeFormLabel( out, "im_lordo_percepiente"); %></td>
	<td><% bp.getController().writeFormInput( out, "im_lordo_percepiente"); %></td>	
	</tr>

	<tr></tr>
	<tr></tr>	
	<tr></tr>
		
	<tr>
  	<td><span>TOTALI SPESE :  </span></td>
  	<td></td>  	
	<td><% bp.getController().writeFormLabel( out, "im_spese_anticipate"); %></td>
	<td><% bp.getController().writeFormInput( out, "im_spese_anticipate"); %></td>
	<td><% bp.getController().writeFormLabel( out, "im_spese"); %></td>
	<td><% bp.getController().writeFormInput( out, "im_spese"); %></td>
  	<td></td>
  	<td></td>  		
	</tr>

	<tr></tr>
	<tr></tr>
	<tr></tr>
	
	<tr>
  	<td><span>TOTALE DIARIA :  </span></td>
  	<td></td>
	<td><% bp.getController().writeFormLabel( out, "im_diaria_netto"); %></td>
	<td><% bp.getController().writeFormInput( out, "im_diaria_netto"); %></td>
	<td><% bp.getController().writeFormLabel( out, "im_quota_esente"); %></td>
	<td><% bp.getController().writeFormInput( out, "im_quota_esente"); %></td>
	<td><% bp.getController().writeFormLabel( out, "im_diaria_lorda"); %></td>
	<td><% bp.getController().writeFormInput( out, "im_diaria_lorda"); %></td>
	</tr>	
	
	<tr>
	<td><span>TOTALE QUOTA RIMBORSO :  </span></td>
	<td></td>
	<td><% bp.getController().writeFormLabel( out, "im_rimborso"); %></td>
	<td><% bp.getController().writeFormInput( out, "im_rimborso"); %></td>
	<td><% bp.getController().writeFormLabel( out, "im_quota_esente"); %></td>
	<td><% bp.getController().writeFormInput( out, "im_quota_esente"); %></td>
	</tr>
</table>
</div>