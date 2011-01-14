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
		it.cnr.contab.prevent01.bp.*"
%>

<%
	CRUDDettagliModuloCostiBP bp = (CRUDDettagliModuloCostiBP)BusinessProcess.getBusinessProcess(request);
%>
 <table class="Panel">
  <tr valign=top><td>    
     <div class="GroupLabel"><% bp.getController().writeFormLabel(out,"label_fonti_interne");%></div>
     <div class="Group"> 
       <table class="Panel">       
         <tr><td NOWRAP colspan=2><% bp.getController().writeFormLabel( out, "label_spese_decentrate_fonti_interne"); %></td></tr>
	     <tr>
		  <td NOWRAP><% bp.getController().writeFormLabel( out, "spese_decentrate_fonti_interne_istituto"); %></td>
		  <td align=right><% bp.getController().writeFormInput( out, "spese_decentrate_fonti_interne_istituto"); %></td>		  
		 </tr>           
	     <tr>
		  <td NOWRAP><% bp.getController().writeFormLabel( out, "spese_decentrate_fonti_interne_aree"); %></td>
		  <td align=right><% bp.getController().writeFormInput( out, "spese_decentrate_fonti_interne_aree"); %></td>		  
		 </tr>           
	     <tr>
		  <td NOWRAP><% bp.getController().writeFormLabel( out, "totale_spese_decentrate_fonti_interne"); %></td>
		  <td align=right><% bp.getController().writeFormInput( out, "totale_spese_decentrate_fonti_interne"); %></td>		  
		 </tr>         
		 <tr><td colspan=2>&nbsp;</td></tr>  
         <tr><td NOWRAP colspan=2><% bp.getController().writeFormLabel( out, "label_spese_accentrate_fonti_interne"); %></td></tr>
	     <tr>
		  <td NOWRAP><% bp.getController().writeFormLabel( out, "spese_accentrate_fonti_interne_istituto"); %></td>
		  <td align=right><% bp.getController().writeFormInput( out, "spese_accentrate_fonti_interne_istituto"); %></td>		  
		 </tr>           
	     <tr>
		  <td NOWRAP><% bp.getController().writeFormLabel( out, "spese_accentrate_fonti_interne_aree"); %></td>
		  <td align=right><% bp.getController().writeFormInput( out, "spese_accentrate_fonti_interne_aree"); %></td>		  
		 </tr>           
	     <tr>
		  <td NOWRAP><% bp.getController().writeFormLabel( out, "totale_spese_accentrate_fonti_interne"); %></td>
		  <td align=right><% bp.getController().writeFormInput( out, "totale_spese_accentrate_fonti_interne"); %></td>		  
		 </tr>           
         <tr><td colspan=2>&nbsp;</td></tr>  
         <tr><td NOWRAP colspan=2><% bp.getController().writeFormLabel( out, "label_totale_spese_da_fonti_interne"); %></td></tr>
	     <tr>
		  <td NOWRAP><% bp.getController().writeFormLabel( out, "totale_spese_da_fonti_interne_istituto"); %></td>
		  <td align=right><% bp.getController().writeFormInput( out, "totale_spese_da_fonti_interne_istituto"); %></td>		  
		 </tr>           
	     <tr>
		  <td NOWRAP><% bp.getController().writeFormLabel( out, "totale_spese_da_fonti_interne_aree"); %></td>
		  <td align=right><% bp.getController().writeFormInput( out, "totale_spese_da_fonti_interne_aree"); %></td>		  
		 </tr>           
	     <tr>
		  <td NOWRAP><% bp.getController().writeFormLabel( out, "totale_spese_da_fonti_interne_totale"); %></td>
		  <td align=right><% bp.getController().writeFormInput( out, "totale_spese_da_fonti_interne_totale"); %></td>		  
		 </tr>           
       </table>
     </div> 
  </td><td>
  <table class="Panel">    
    <tr><td valign=top>
     <div class="GroupLabel"><% bp.getController().writeFormLabel(out,"label_fonti_esterne");%></div>
     <div class="Group">  
      <table class="Panel">
	    <tr>
		  <td NOWRAP><% bp.getController().writeFormLabel( out, "tot_entr_fonti_est_anno_in_corso"); %></td>
		  <td align=right><% bp.getController().writeFormInput( out, "tot_entr_fonti_est_anno_in_corso"); %></td>		  
		</tr>  
	    <tr>		
		  <td NOWRAP><% bp.getController().writeFormLabel( out, "tot_spese_coperte_fonti_esterne_anno_in_corso"); %></td>
		  <td align=right><% bp.getController().writeFormInput( out, "tot_spese_coperte_fonti_esterne_anno_in_corso"); %></td>		  
		</tr>  
	    <tr>				
		  <td NOWRAP><% bp.getController().writeFormLabel( out, "differenza"); %></td>
		  <td align=right><% bp.getController().writeFormInput( out, "differenza"); %></td>		  
		</tr>  
       </table>		
     </div>
     </td>
     <td valign=top>  
     <div class="GroupLabel"><% bp.getController().writeFormLabel(out,"label_totali_generali");%></div>
     <div class="Group">  
      <table class="Panel">
	    <tr>
		  <td NOWRAP><% bp.getController().writeFormLabel( out, "tot_massa_spendibile_anno_prec"); %></td>
		  <td><% bp.getController().writeFormInput( out, "tot_massa_spendibile_anno_prec"); %></td>
		</tr>  
	    <tr>		
		  <td NOWRAP><% bp.getController().writeFormLabel( out, "tot_massa_spendibile_anno_in_corso"); %></td>
		  <td><% bp.getController().writeFormInput( out, "tot_massa_spendibile_anno_in_corso"); %></td>
		</tr>  
	    <tr>				
		  <td NOWRAP><% bp.getController().writeFormLabel( out, "valore_presunto_anno_in_corso"); %></td>
		  <td><% bp.getController().writeFormInput( out, "valore_presunto_anno_in_corso"); %></td>
		</tr>
		<tr><td>&nbsp;</td></tr>  		  
       </table>				
     </div>  
     </td></tr>
   </table>
     <div class="GroupLabel"><% bp.getController().writeFormLabel(out,"label_contrattazione");%></div>
   		<%bp.getCrudDettagliContrSpese().writeHTMLTable(pageContext,(bp.getLivelloContrattazione().compareTo(new Integer(0))==0)?"csPdgPSenzaVoce":"csPdgP",false,false,false,"100%","130px"); %>
     <div class="Group">  
      <table class="Panel" cellpadding=2>
		<tr>
		  <td NOWRAP colspan=2><span class="FormLabel" style="color:blue">Fonti Interne</td>
		  <td NOWRAP colspan=2><span class="FormLabel" style="color:blue">Fonti Esterne</td>
		</tr>
		<tr>
		  <td NOWRAP><% bp.getCrudDettagliContrSpese().writeFormLabel(out,"totalePropostoModificatoFI");%></td>
		  <td><% bp.getCrudDettagliContrSpese().writeFormInput(out,"totalePropostoModificatoFI");%></td>
		  <td NOWRAP><% bp.getCrudDettagliContrSpese().writeFormLabel(out,"totalePropostoModificatoFE");%></td>
		  <td><% bp.getCrudDettagliContrSpese().writeFormInput(out,"totalePropostoModificatoFE");%></td>
		</tr>
		<tr>
			<% if (bp.isContrSpeseEnabled()) {%>
		    	<td NOWRAP><% bp.getCrudDettagliContrSpese().writeFormLabel(out,"appr_tot_spese_decentr_int");%></td>
		    	<td><% bp.getCrudDettagliContrSpese().writeFormInput(out,null,"appr_tot_spese_decentr_int",!bp.isContrSpeseAggiornabile(),null,null);%></td>
			<%} else {%>
		  		<td NOWRAP><% bp.getCrudDettagliContrSpese().writeFormLabel(out,"appr_tot_spese_decentr_int_disabled");%></td>
		  		<td><% bp.getCrudDettagliContrSpese().writeFormInput(out,"appr_tot_spese_decentr_int_disabled");%></td>
			<%}%>
			<% if (bp.isContrSpeseEnabled()) {%>
		    	<td NOWRAP><% bp.getCrudDettagliContrSpese().writeFormLabel(out,"appr_tot_spese_decentr_est");%></td>
		    	<td><% bp.getCrudDettagliContrSpese().writeFormInput(out,null,"appr_tot_spese_decentr_est",!bp.isContrSpeseAggiornabile(),null,null);%></td>
			<%} else {%>
		  		<td NOWRAP><% bp.getCrudDettagliContrSpese().writeFormLabel(out,"appr_tot_spese_decentr_est_disabled");%></td>
		  		<td><% bp.getCrudDettagliContrSpese().writeFormInput(out,"appr_tot_spese_decentr_est_disabled");%></td>
			<%}%>
		</tr>
		<tr>
		  <td NOWRAP><% bp.getCrudDettagliContrSpese().writeFormLabel(out,"differenzaFI");%></td>
		  <td><% bp.getCrudDettagliContrSpese().writeFormInput(out,"differenzaFI");%></td>
		  <td NOWRAP><% bp.getCrudDettagliContrSpese().writeFormLabel(out,"differenzaFE");%></td>
		  <td><% bp.getCrudDettagliContrSpese().writeFormInput(out,"differenzaFE");%></td>
		</tr>
       </table>				
     </div>  
  </table> 