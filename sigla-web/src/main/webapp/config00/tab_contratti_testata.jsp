<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.config00.bp.*,
		it.cnr.contab.config00.contratto.bulk.*"
%>

<%
	SimpleCRUDBP bp = (SimpleCRUDBP)BusinessProcess.getBusinessProcess(request);
%>

   <fieldset class="fieldset">
    <legend class="GroupLabel"><% bp.getController().writeFormInput(out,null,"statoText",true,"GroupLabel","style=\"background: #F5F5DC;background-color:transparent;border-style : none; cursor:default;font-size : 16px;\"");%></legend>    
	<table class="Panel">
	  <tr>
	    <td><% bp.getController().writeFormLabel(out,"dt_registrazione");%></td>
	    <td><% bp.getController().writeFormInput(out,"dt_registrazione");%></td>
        <td><% bp.getController().writeFormLabel(out,"cd_protocollo");%></td>
        <td><% bp.getController().writeFormInput(out,"cd_protocollo");%></td>
      </tr>  	
      <tr><td colspan=4>
      <div class="GroupLabel">Estremi del protocollo informatico</div>          
      <div class="Group">
      <table>      
      <tr>
         <td><% bp.getController().writeFormField(out,"esercizio_protocollo");%></td>
         <td><% bp.getController().writeFormField(out,"cd_protocollo_generale");%></td>         
      </tr>            
      </table></div>
      </td></tr>	      
      <tr><td colspan=4>
      <div class="Group"><table>
	  <tr>
         <td><% bp.getController().writeFormLabel(out,"unita_organizzativa");%></td>
         <td><% bp.getController().writeFormInput(out,"unita_organizzativa");%></td>
      </tr>  	      
	  <tr>
         <td><% bp.getController().writeFormLabel(out,"figura_giuridica_interna");%></td>
         <td><% bp.getController().writeFormInput(out,"figura_giuridica_interna");%></td>
      </tr>  	
	  <tr>         
         <td><% bp.getController().writeFormLabel(out,"firmatario");%></td>
         <td><% bp.getController().writeFormInput(out,"cd_terzo_firmatario");%>
             <% bp.getController().writeFormInput(out,"ds_firmatario");%>
             <% bp.getController().writeFormInput(out,"firmatario");%></td>
      </tr>            
	  <tr>         
         <td><% bp.getController().writeFormLabel(out,"responsabile");%></td>
         <td><% bp.getController().writeFormInput(out,"cd_terzo_resp");%>
             <% bp.getController().writeFormInput(out,"ds_responsabile");%>
             <% bp.getController().writeFormInput(out,"responsabile");%></td>
      </tr>
      </table></div>
      </td></tr>
      <tr><td colspan=4>
      <div class="Group"><table>      
	  <tr>         
         <td><% bp.getController().writeFormLabel(out,"figura_giuridica_esterna");%></td>
         <td colspan="5"><% bp.getController().writeFormInput(out,"figura_giuridica_esterna");%></td>
      </tr>      
	  <tr>         
         <td><% bp.getController().writeFormLabel(out,"resp_esterno");%></td>
         <td colspan="5"><% bp.getController().writeFormInput(out,"resp_esterno");%></td>
      </tr>            
      </table></div>
      </td></tr>   
      <tr><td colspan=4>
      <div class="Group"><table>      
	  <tr>         
         <td><% bp.getController().writeFormLabel(out,"atto");%></td>
         <td><% bp.getController().writeFormInput(out,"atto");%></td>
         <td><% bp.getController().writeFormInput(out,"crea_atto");%></td>
      </tr>
      <% if (((ContrattoBulk)bp.getModel()).isDs_atto_non_definitoVisible()){%>
	  <tr>         
         <td><% bp.getController().writeFormLabel(out,"ds_atto_non_definito");%></td>
         <td colspan=2><% bp.getController().writeFormInput(out,"ds_atto_non_definito");%></td>
      </tr>      
      <%}%>
	  <tr>         
         <td><% bp.getController().writeFormLabel(out,"ds_atto");%></td>
         <td colspan=2><% bp.getController().writeFormInput(out,"ds_atto");%></td>
      </tr>
	  <tr>         
         <td><% bp.getController().writeFormLabel(out,"organo");%></td>
         <td><% bp.getController().writeFormInput(out,"organo");%></td>
         <td><% bp.getController().writeFormInput(out,"crea_organo");%></td>
      </tr>
      <% if (((ContrattoBulk)bp.getModel()).isDs_organo_non_definitoVisible()){%>
	  <tr>         
         <td><% bp.getController().writeFormLabel(out,"ds_organo_non_definito");%></td>
         <td colspan=2><% bp.getController().writeFormInput(out,"ds_organo_non_definito");%></td>
      </tr>      
      <%}%>                        
      </table></div>
      </td></tr>          
      <tr><td colspan=4>
      <div class="Group"><table>            
	  <tr>         
         <td><% bp.getController().writeFormLabel(out,"oggetto");%></td>
         <td colspan="5"><% bp.getController().writeFormInput(out,"oggetto");%></td>
      </tr>                     	
	  <tr>      
	    <td><% bp.getController().writeFormLabel(out,"natura_contabile");%></td>
	    <td colspan="5"><% bp.getController().writeFormInput(out,null,"natura_contabile",false,null,"onChange=\"submitForm('doOnTipoChange')\""); %></td>
      </tr>                        
	  <tr>         
         <td><% bp.getController().writeFormLabel(out,"tipo_contratto");%></td>
         <td><% bp.getController().writeFormInput(out,"tipo_contratto");%></td>
         <td align=left colspan=2><% bp.getController().writeFormInput(out,"crea_tipo_contratto");%></td>
      </tr>
	  <tr>
        <td><% bp.getController().writeFormLabel(out,"procedura_amministrativa");%></td>
        <td><% bp.getController().writeFormInput(out,"procedura_amministrativa");%></td>	  
        <td align=left colspan=2><% bp.getController().writeFormInput(out,"crea_procedura_amministrativa");%></td>        
      </tr>
      </table></div>
      </td></tr>
      <tr><td colspan=4>
      <div class="Group"><table>
      <% if (((ContrattoBulk)bp.getModel()).isAttivo_e_Passivo()){%>
	  <tr>
        <td><% bp.getController().writeFormLabel(out,"im_contratto_attivo");%></td>
        <td><% bp.getController().writeFormInput(out,"im_contratto_attivo");%></td>                	    
        <td><% bp.getController().writeFormLabel(out,"im_contratto_passivo");%></td>
        <td><% bp.getController().writeFormInput(out,"im_contratto_passivo");%></td>                	            
      </tr>      
      <%} else if(((ContrattoBulk)bp.getModel()).isAttivo()){%>
	  <tr>
        <td><% bp.getController().writeFormLabel(out,"im_contratto_attivo");%></td>
        <td colspan="5"><% bp.getController().writeFormInput(out,"im_contratto_attivo");%></td>                	    
      </tr>            
      <%} else if(((ContrattoBulk)bp.getModel()).isPassivo()){%>
	  <tr>
        <td><% bp.getController().writeFormLabel(out,"im_contratto_passivo");%></td>
        <td colspan="5"><% bp.getController().writeFormInput(out,"im_contratto_passivo");%></td>                	    
      </tr>                  
      <%}%>
	  <tr>
	    <td><% bp.getController().writeFormLabel(out,"dt_stipula");%></td>
	    <td><% bp.getController().writeFormInput(out,"dt_stipula");%></td>
        <td><% bp.getController().writeFormLabel(out,"dt_inizio_validita");%></td>
        <td><% bp.getController().writeFormInput(out,"dt_inizio_validita");%></td>                	    
      </tr>      
	  <tr>
        <td><% bp.getController().writeFormLabel(out,"dt_fine_validita");%></td>
        <td><% bp.getController().writeFormInput(out,"dt_fine_validita");%></td>        
        <td><% bp.getController().writeFormLabel(out,"dt_proroga");%></td>
        <td><% bp.getController().writeFormInput(out,"dt_proroga");%></td>	          
      </tr>
	  <tr>         
         <td><% bp.getController().writeFormLabel(out,"contratto_padre");%></td>
         <td colspan="5"><% bp.getController().writeFormInput(out,"contratto_padre");%></td>
      </tr>
      </table></div>
      </td></tr>
      <tr>                        
         <td colspan=5>
		   <div class="Group" style="width:100%;">		
		   <table border="0" cellspacing="0" cellpadding="2">
			<tr>
			<td><% bp.getController().writeFormLabel( out, "tot_doc_cont_etr"); %></td>
			<td><% bp.getController().writeFormInput( out, "tot_doc_cont_etr"); %></td>
			<td><% Button.write(out,bp.encodePath("img/book_opened.gif"),bp.encodePath("img/book_closed.gif"),"Documenti contabili associati","javascript:submitForm('doVisualizzaDocContEtr')", null, "Visualizza i documenti contabili associati al contratto", ((CRUDConfigAnagContrattoBP)bp).isVisualizzaDocContEtrButtonEnabled()  ); %></td>
			<td rowspan="2"><% Button.write(out,bp.encodePath("img/book_opened.gif"),bp.encodePath("img/book_closed.gif"),"Commesse associate","javascript:submitForm('doVisualizzaDocContForCommessaContratto')", null, "Visualizza le commesse associate al contratto", ((CRUDConfigAnagContrattoBP)bp).isVisualizzaCommessaButtonEnabled() ); %></td>			
			</tr>
			<tr>
			<td><% bp.getController().writeFormLabel( out, "tot_doc_cont_spe"); %></td>
			<td><% bp.getController().writeFormInput( out, "tot_doc_cont_spe"); %></td>
			<td><% Button.write(out,bp.encodePath("img/book_opened.gif"),bp.encodePath("img/book_closed.gif"),"Documenti contabili associati","javascript:submitForm('doVisualizzaDocContSpe')", null, "Visualizza i documenti contabili associati al contratto", ((CRUDConfigAnagContrattoBP)bp).isVisualizzaDocContSpeButtonEnabled()  ); %></td>
			<td></td>
			</tr>
		   </table>
		   </div>            
         </td>
      </tr>   
	</table>   
   </fieldset>