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
 <table class="Panel w-100">
  <tr valign=top>
<% if (!bp.isFlNuovoPdg()){ %>
  <td>
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
  </td>
<% } %>
  <td>
  <table class="Panel w-100">
    <tr>
<% if (bp.isFlNuovoPdg()){ %>
    <td valign=top>
     	<div class="GroupLabel font-weight-bold text-primary"><% bp.getController().writeFormLabel(out,"label_fonti_interne");%></div>
     	<div class="Group card"> 
       	<table class="Panel">       
	   <tr>
		<% bp.getController().writeFormField( out, "totale_spese_da_fonti_interne_accentrate"); %>
	   </tr>           
	   <tr>
		<% bp.getController().writeFormField( out, "totale_spese_da_fonti_interne_decentrate"); %>
	   </tr>           
	   <tr>
		<% bp.getController().writeFormField( out, "totale_spese_da_fonti_interne_totale"); %>
	   </tr>
       </table>		
     </div>
    </td>
<% } %>
    <td valign=top>
     <div class="GroupLabel font-weight-bold text-primary"><% bp.getController().writeFormLabel(out,"label_fonti_esterne");%></div>
     <div class="Group card">  
      <table class="Panel">
	    <tr>
		  <% bp.getController().writeFormField( out, "tot_entr_fonti_est_anno_in_corso"); %>
		</tr>  
	    <tr>		
		  <% bp.getController().writeFormField( out, "tot_spese_coperte_fonti_esterne_anno_in_corso"); %>
		</tr>  
	    <tr>				
		  <% bp.getController().writeFormField( out, "differenza"); %>
		</tr>  
       </table>		
     </div>
     </td>
     <td valign=top>  
     <div class="GroupLabel font-weight-bold text-primary"><% bp.getController().writeFormLabel(out,"label_totali_generali");%></div>
     <div class="Group card">  
      <table class="Panel">
	    <tr>
		  <% bp.getController().writeFormField( out, "tot_massa_spendibile_anno_prec"); %>
		</tr>  
	    <tr>		
		  <% bp.getController().writeFormField( out, "tot_massa_spendibile_anno_in_corso"); %>
		</tr>  
	    <tr>				
		  <% bp.getController().writeFormField( out, "valore_presunto_anno_in_corso"); %>
		</tr>
		<tr><td>&nbsp;</td></tr>  		  
       </table>				
     </div>  
     </td></tr>
   </table>
   <div class="card p-2">
     <div class="GroupLabel font-weight-bold text-primary"><% bp.getController().writeFormLabel(out,"label_contrattazione");%></div>
		<% 	if (bp.isFlNuovoPdg()){
   				bp.getCrudDettagliContrSpese().writeHTMLTable(pageContext,(bp.getLivelloContrattazione().compareTo(new Integer(0))==0)?"csNewPdgPSenzaVoce":"csNewPdgP",false,false,false,"100%","130px");
   			} else {
   				bp.getCrudDettagliContrSpese().writeHTMLTable(pageContext,(bp.getLivelloContrattazione().compareTo(new Integer(0))==0)?"csPdgPSenzaVoce":"csPdgP",false,false,false,"100%","130px");
   			}
   		%>
	</div>
     <div class="Group card">  
      <table class="Panel w-100" cellpadding=2>
		<tr>
		  <td colspan=2><span class="FormLabel font-weight-bold text-primary" style="color:blue">Fonti Interne</td>
		  <td colspan=2><span class="FormLabel font-weight-bold text-primary" style="color:blue">Fonti Esterne</td>
		</tr>
		<tr>
		  <% bp.getCrudDettagliContrSpese().writeFormField(out,"totalePropostoModificatoFI");%>
		  <% bp.getCrudDettagliContrSpese().writeFormField(out,"totalePropostoModificatoFE");%>
		</tr>
		<tr>
			<% if (bp.isContrSpeseEnabled()) {%>
		    	<td><% bp.getCrudDettagliContrSpese().writeFormLabel(out,"appr_tot_spese_decentr_int");%></td>
		    	<td><% bp.getCrudDettagliContrSpese().writeFormInput(out,null,"appr_tot_spese_decentr_int",!bp.isContrSpeseAggiornabile(),null,null);%></td>
			<%} else {%>
		  		<% bp.getCrudDettagliContrSpese().writeFormField(out,"appr_tot_spese_decentr_int_disabled");%>
			<%}%>
			<% if (bp.isContrSpeseEnabled()) {%>
		    	<td><% bp.getCrudDettagliContrSpese().writeFormLabel(out,"appr_tot_spese_decentr_est");%></td>
		    	<td><% bp.getCrudDettagliContrSpese().writeFormInput(out,null,"appr_tot_spese_decentr_est",!bp.isContrSpeseAggiornabile(),null,null);%></td>
			<%} else {%>
		  		<% bp.getCrudDettagliContrSpese().writeFormField(out,"appr_tot_spese_decentr_est_disabled");%>
			<%}%>
		</tr>
		<tr>
		  <% bp.getCrudDettagliContrSpese().writeFormField(out,"differenzaFI");%>
		  <% bp.getCrudDettagliContrSpese().writeFormField(out,"differenzaFE");%>
		</tr>
       </table>				
     </div>  
  </table> 