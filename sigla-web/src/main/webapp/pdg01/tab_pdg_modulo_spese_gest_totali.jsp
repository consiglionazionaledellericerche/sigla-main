<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page 
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.pdg01.bp.*"
%>
<%
	CRUDPdgModuloSpeseGestBP bp = (CRUDPdgModuloSpeseGestBP)BusinessProcess.getBusinessProcess(request);
%>
<table class="Panel">
  <tr valign=top>
	<td>
     	<div class="GroupLabel"><% bp.getController().writeFormLabel(out,"label_spese_decentrate");%></div>
     	<div class="Group">  
       	<table class="Panel">       
         	<tr>
         		<td NOWRAP colspan=2><% bp.getController().writeFormLabel( out, "label_fonti_interne"); %></td>
         	</tr>
	     	<tr>
		  		<td NOWRAP><% bp.getController().writeFormLabel( out, "label_previsione"); %></td>
		  		<td align=right><% bp.getController().writeFormInput( out, "ROim_spese_gest_decentrata_int"); %></td>		  
		 	</tr>           
	     	<tr>
		  		<td NOWRAP><% bp.getController().writeFormLabel( out, "label_gestione"); %></td>
		  		<td align=right><% bp.getController().writeFormInput( out, "totale_spese_decentrate_interne_gest"); %></td>		  
			</tr>           
	     	<tr>
		  		<td NOWRAP><% bp.getController().writeFormLabel( out, "label_importo_da_ripartire"); %></td>
		  		<td align=right><% bp.getController().writeFormInput( out, "importo_da_ripartire_dec_int_gest"); %></td>		  
		 	</tr>         
		 	<tr>
		 		<td colspan=2>&nbsp;</td>
		 	</tr>  
         	<tr>
         		<td NOWRAP colspan=2><% bp.getController().writeFormLabel( out, "label_fonti_esterne"); %></td>
         	</tr>
	     	<tr>
		  		<td NOWRAP><% bp.getController().writeFormLabel( out, "label_previsione"); %></td>
		  		<td align=right><% bp.getController().writeFormInput( out, "ROim_spese_gest_decentrata_est"); %></td>		  
		 	</tr>           
	     	<tr>
		 		<td NOWRAP><% bp.getController().writeFormLabel( out, "label_gestione"); %></td>
		  		<td align=right><% bp.getController().writeFormInput( out, "totale_spese_decentrate_esterne_gest"); %></td>		  
		 	</tr>           
	     	<tr>
		  		<td NOWRAP><% bp.getController().writeFormLabel( out, "label_importo_da_ripartire"); %></td>
		  		<td align=right><% bp.getController().writeFormInput( out, "importo_da_ripartire_dec_est_gest"); %></td>		  
		 	</tr>           
       	</table>
     	</div>
	</td>
    <td>    
     	<div class="GroupLabel"><% bp.getController().writeFormLabel(out,"label_spese_accentrate");%></div>
     	<div class="Group"> 
       	<table class="Panel">       
         	<tr>
         		<td NOWRAP colspan=2><% bp.getController().writeFormLabel( out, "label_fonti_interne"); %></td>
         	</tr>
	     	<tr>
		  		<td NOWRAP><% bp.getController().writeFormLabel( out, "label_previsione"); %></td>
		  		<td align=right><% bp.getController().writeFormInput( out, "ROim_spese_gest_accentrata_int"); %></td>		  
		 	</tr>           
	     	<tr>
		 		<td NOWRAP><% bp.getController().writeFormLabel( out, "label_gestione"); %></td>
		  		<td align=right><% bp.getController().writeFormInput( out, "totale_spese_accentrate_interne_gest"); %></td>		  
		 	</tr>           
	     	<tr>
		  		<td NOWRAP><% bp.getController().writeFormLabel( out, "label_importo_da_ripartire"); %></td>
		  		<td align=right><% bp.getController().writeFormInput( out, "importo_da_ripartire_acc_int_gest"); %></td>		  
		 	</tr>         
		 	<tr>
		 		<td colspan=2>&nbsp;</td>
		 	</tr>  
         	<tr>
         		<td NOWRAP colspan=2><% bp.getController().writeFormLabel( out, "label_fonti_esterne"); %></td>
         	</tr>
	     	<tr>
		  		<td NOWRAP><% bp.getController().writeFormLabel( out, "label_previsione"); %></td>
		  		<td align=right><% bp.getController().writeFormInput( out, "ROim_spese_gest_accentrata_est"); %></td>		  
		 	</tr>           
	     	<tr>
		  		<td NOWRAP><% bp.getController().writeFormLabel( out, "label_gestione"); %></td>
		  		<td align=right><% bp.getController().writeFormInput( out, "totale_spese_accentrate_esterne_gest"); %></td>		  
		 	</tr>           
	     	<tr>
		  		<td NOWRAP><% bp.getController().writeFormLabel( out, "label_importo_da_ripartire"); %></td>
		  		<td align=right><% bp.getController().writeFormInput( out, "importo_da_ripartire_acc_est_gest"); %></td>		  
		 	</tr>           
       	</table>
     	</div> 
  	</td>
  </tr>
</table> 