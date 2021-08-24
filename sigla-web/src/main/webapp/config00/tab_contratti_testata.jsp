<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.config00.bp.*,
		it.cnr.contab.config00.contratto.bulk.*"
%>

<%
	CRUDConfigAnagContrattoBP bp = (CRUDConfigAnagContrattoBP)BusinessProcess.getBusinessProcess(request);
	ContrattoBulk contratto = (ContrattoBulk)bp.getModel();
%>

   <fieldset class="fieldset">
    <legend class="GroupLabel">
    	<% bp.getController().writeFormInput(out,null,"statoText",true,
    			"GroupLabel text-primary h3 inputFieldReadOnly", null);%>
    </legend>
	<table class="Panel w-75">
	  <tr>
	    <td><% bp.getController().writeFormLabel(out,"dt_registrazione");%></td>
	    <td><% bp.getController().writeFormInput(out,"default","dt_registrazione", bp.isPublishHidden(),null,null);%></td>
        <td><% bp.getController().writeFormLabel(out,"cd_protocollo");%></td>
        <td><% bp.getController().writeFormInput(out,"cd_protocollo");%></td>
        <%if(contratto.isPassivo() && bp.isAttivoOrdini()){%>
            <td><% bp.getController().writeFormLabel(out,"tipo_dettaglio_contratto");%></td>
            <td><% bp.getController().writeFormInput(out,"tipo_dettaglio_contratto");%></td>
         <%}%>
      </tr>  	
      <tr><td colspan=4>
	      <div class="GroupLabel h3 text-primary">Estremi del protocollo informatico</div>          
	      <div class="Group card">
		      <table class="Panel w-100">      
		      <tr>
		         <td><% bp.getController().writeFormField(out,"esercizio_protocollo");%></td>
		         <td><% bp.getController().writeFormField(out,"cd_protocollo_generale");%></td>         
		      </tr>            
		      </table>
		  </div>
      </td></tr>	      
      <tr><td colspan=4>
      <div class="Group card">
	      <table class="w-100">
		  <tr>
	         <td><% bp.getController().writeFormLabel(out,"unita_organizzativa");%></td>
	         <td><% bp.getController().writeFormInput(out,"unita_organizzativa");%></td>
	      </tr>  	      
		  <tr>
	         <td><% bp.getController().writeFormLabel(out, "figura_giuridica_interna");%></td>
	         <td><% bp.getController().writeFormInput(out, "default", "figura_giuridica_interna", bp.isPublishHidden(),null,null);%></td>
	      </tr>  	
		  <tr>         
	         <td><% bp.getController().writeFormLabel(out,"firmatario");%></td>
	         <td><% bp.getController().writeFormInput(out,"default","firmatario", bp.isPublishHidden() || bp.isFromFlussoAcquisti(),null,null);%></td>
	      </tr>            
		  <tr>         
	         <td><% bp.getController().writeFormLabel(out,"responsabile");%></td>
	         <td><% bp.getController().writeFormInput(out,"default","cd_terzo_resp", bp.isPublishHidden() || bp.isFromFlussoAcquisti(),null,null);%>
	             <% bp.getController().writeFormInput(out,"ds_responsabile");%>
	             <% bp.getController().writeFormInput(out,"default","responsabile", bp.isPublishHidden() || bp.isFromFlussoAcquisti(),null,null);%></td>
	      </tr>
		  <tr>         
	         <td><% bp.getController().writeFormLabel(out,"direttore");%></td>
	         <td><% bp.getController().writeFormInput(out,"default","direttore", bp.isPublishHidden(),null,null);%></td>
	      </tr>
	      </table>
      </div>
      </td></tr>
      <tr><td colspan=4>
      <div class="Group card">
	      <table class="w-100">      
			  <tr>         
		         <td><% bp.getController().writeFormLabel(out,"figura_giuridica_esterna");%></td>
		         <td colspan="5"><% bp.getController().writeFormInput(out,"default", "figura_giuridica_esterna", bp.isContrattoDefinitivo(),null,null);%></td>
		      </tr>      
			  <tr>         
		         <td><% bp.getController().writeFormLabel(out,"resp_esterno");%></td>
		         <td colspan="5"><% bp.getController().writeFormInput(out,"default","resp_esterno", bp.isPublishHidden(),null,null);%></td>
		      </tr>            
	      </table>
      </div>
      </td></tr>   
      <tr><td colspan=4>
      <div class="Group card">
	      <table class="w-100">      
			  <tr>         
		         <td><% bp.getController().writeFormLabel(out,"atto");%></td>
		         <td><% bp.getController().writeFormInput(out,"default","atto", bp.isPublishHidden(),null,null);%></td>
		         <td><% bp.getController().writeFormInput(out,"default","crea_atto", bp.isPublishHidden(),null,null);%></td>
		      </tr>
		      <% if (((ContrattoBulk)bp.getModel()).isDs_atto_non_definitoVisible()){%>
			  <tr>         
		         <td><% bp.getController().writeFormLabel(out,"ds_atto_non_definito");%></td>
		         <td colspan=2><% bp.getController().writeFormInput(out,"default","ds_atto_non_definito", bp.isPublishHidden(),null,null);%></td>
		      </tr>      
		      <%}%>
			  <tr>         
		         <td><% bp.getController().writeFormLabel(out,"ds_atto");%></td>
		         <td colspan=2><% bp.getController().writeFormInput(out,"default","ds_atto", bp.isPublishHidden(),null,null);%></td>
		      </tr>
			  <tr>         
		         <td><% bp.getController().writeFormLabel(out,"organo");%></td>
		         <td><% bp.getController().writeFormInput(out,"default","organo", bp.isPublishHidden(),null,null);%></td>
		         <td><% bp.getController().writeFormInput(out,"default","crea_organo", bp.isPublishHidden(),null,null);%></td>
		      </tr>
		      <% if (((ContrattoBulk)bp.getModel()).isDs_organo_non_definitoVisible()){%>
			  <tr>         
		         <td><% bp.getController().writeFormLabel(out,"ds_organo_non_definito");%></td>
		         <td colspan=2><% bp.getController().writeFormInput(out,"default","ds_organo_non_definito", bp.isPublishHidden(),null,null);%></td>
		      </tr>      
		      <%}%>                        
	      </table>
      </div>
      </td></tr>          
      <tr><td colspan=4>
      <div class="Group card">
	      <table class="w-100">            
			  <tr>         
		         <td><% bp.getController().writeFormLabel(out,"oggetto");%></td>
		         <td colspan="5"><% bp.getController().writeFormInput(out,"default","oggetto", bp.isPublishHidden(),null,null);%></td>
		      </tr>                     	
			  <tr>      
			    <td><% bp.getController().writeFormLabel(out,"natura_contabile");%></td>
			    <td colspan="5"><% bp.getController().writeFormInput(out,null,"natura_contabile",bp.isPublishHidden() || bp.isFromFlussoAcquisti(),null,"onChange=\"submitForm('doOnTipoChange')\""); %></td>
		      </tr>        
		      <% if ((ContrattoBulk)bp.getModel() != null && ((ContrattoBulk)bp.getModel()).isPassivo() || ((ContrattoBulk)bp.getModel()).isAttivo_e_Passivo()){%>
			  <tr>         
		         <td><% bp.getController().writeFormLabel(out,"fl_mepa");%></td>
		         <td><% bp.getController().writeFormInput(out,"default","fl_mepa", bp.isPublishHidden() || bp.isFromFlussoAcquisti(),null,null);%></td>
		      </tr>
			 <%}%>
			  <tr>         
		         <td><% bp.getController().writeFormLabel(out,"tipo_contratto");%></td>
		         <td>
		         	<div style="float:left"><% bp.getController().writeFormInput(out,"default","tipo_contratto", bp.isPublishHidden(),null,null);%></div>
		         	<div style="float:left"><% bp.getController().writeFormInput(out,"default","crea_tipo_contratto", bp.isPublishHidden(),null,null);%></div>
		         </td>
		      </tr>
		      <% if (((ContrattoBulk)bp.getModel()).isCIGVisible()){%>
			  <tr>         
		         <td><% bp.getController().writeFormLabel(out,"cig");%></td>
		         <td>
		         	<div style="float:left"><% bp.getController().writeFormInput(out,"default","cig", bp.isPublishHidden(),null,null);%></div>
		         	<div style="float:left"><% bp.getController().writeFormInput(out,"default","crea_cig", bp.isPublishHidden(),null,null);%></div>
		         </td>
		      </tr>
		      <%}%>
			  <tr>
		        <td><% bp.getController().writeFormLabel(out,"procedura_amministrativa");%></td>
		        <td>
		        	<div style="float:left"><% bp.getController().writeFormInput(out,"default","procedura_amministrativa", bp.isPublishHidden(),null,null);%></div>
		        	<div style="float:left"><% bp.getController().writeFormInput(out,"default","crea_procedura_amministrativa", bp.isPublishHidden(),null,null);%></div>
		        </td>	  
		      </tr>
		      <% if ((ContrattoBulk)bp.getModel() != null && ((ContrattoBulk)bp.getModel()).isPassivo() || ((ContrattoBulk)bp.getModel()).isAttivo_e_Passivo()){%>
			  <tr>         
		         <td><% bp.getController().writeFormLabel(out,"tipoNormaPerla");%></td>
		         <td><% bp.getController().writeFormInput(out,"default","tipoNormaPerla", bp.isPublishHidden(),null,null);%></td>
		      </tr>
		      <%}%>
			  <tr>         
		        <td><% bp.getController().writeFormLabel(out,"cup");%></td>
		      <% if (((ContrattoBulk)bp.getModel() == null)  || ((ContrattoBulk)bp.getModel() != null && (((ContrattoBulk)bp.getModel()).getCup() == null || ((ContrattoBulk)bp.getModel()).getCup().getCdCup() == null ))){%>
        			<td><% bp.getController().writeFormInput(out,"default","cup",false,null,"");%></td>                	            
		      <%} else {%>
        			<td><% bp.getController().writeFormInput(out,"default","cup", bp.isPublishHidden(),null,null);%></td>                	            
		      <%}%>
		      </tr>
		      <% if (bp.isSearching() ||
		    		  ((ContrattoBulk)bp.getModel() != null && (((ContrattoBulk)bp.getModel()).isAttivo() || ((ContrattoBulk)bp.getModel()).isAttivo_e_Passivo()))){%>
			  <tr><% bp.getController().writeFormField(out,"findProgetto");%></tr>
			  <tr>         
		        <td><% bp.getController().writeFormLabel(out,"cdCigFatturaAttiva");%></td>
		      	  <% if (((ContrattoBulk)bp.getModel()).getCdCigFatturaAttiva() == null ){%>
        			<td><% bp.getController().writeFormInput(out,null,"cdCigFatturaAttiva",false,null,"");%></td>                	            
			      <%} else {%>
        			<td><% bp.getController().writeFormInput(out,"default","cdCigFatturaAttiva", bp.isPublishHidden(),null,null);%></td>                	            
			      <%}%>
		      <%}%>
	      </table>
      </div>
      </td></tr>
      <tr><td colspan=4>
      <div class="Group card">
      <table class="w-100">
	      <% if (((ContrattoBulk)bp.getModel()).isAttivo_e_Passivo()){%>
		  <tr>
	        <td><% bp.getController().writeFormLabel(out,"im_contratto_attivo");%></td>
	        <td><% bp.getController().writeFormInput(out,"default","im_contratto_attivo", bp.isPublishHidden(),null,null);%></td>                	    
	        <td><% bp.getController().writeFormLabel(out,"im_contratto_passivo");%></td>
	        <td><% bp.getController().writeFormInput(out,"default","im_contratto_passivo", bp.isPublishHidden(),null,null);%></td>
	        <td><% bp.getController().writeFormLabel(out,"im_contratto_passivo_netto");%></td>
	       	<td><% bp.getController().writeFormInput(out,null,"im_contratto_passivo_netto", false,null,"");%></td>                	            
	      </tr>      
	      <%} else if(((ContrattoBulk)bp.getModel()).isAttivo()){%>
		  <tr>
	        <td><% bp.getController().writeFormLabel(out,"im_contratto_attivo");%></td>
	        <td colspan="5"><% bp.getController().writeFormInput(out,"default","im_contratto_attivo", bp.isPublishHidden(),null,null);%></td>                	    
	      </tr>            
	      <%} else if(((ContrattoBulk)bp.getModel()).isPassivo()){%>
		  <tr>
	        <td><% bp.getController().writeFormLabel(out,"im_contratto_passivo");%></td>
	        <td><% bp.getController().writeFormInput(out,"default","im_contratto_passivo", bp.isPublishHidden() || bp.isFromFlussoAcquisti(),null,null);%></td>
	       	<td><% bp.getController().writeFormLabel(out,"im_contratto_passivo_netto");%></td>
	       	<td><% bp.getController().writeFormInput(out,null,"im_contratto_passivo_netto", false,null,"");%></td>                 	    
	      </tr>                  
	      <%}%>
		  <tr>
		    <td><% bp.getController().writeFormLabel(out,"dt_stipula");%></td>
		    <td><% bp.getController().writeFormInput(out,"default","dt_stipula", bp.isPublishHidden() || bp.isFromFlussoAcquisti(),null,null);%></td>
	        <td><% bp.getController().writeFormLabel(out,"dt_inizio_validita");%></td>
	        <td><% bp.getController().writeFormInput(out,"default","dt_inizio_validita", bp.isPublishHidden(),null,null);%></td>                	    
	      </tr>      
		  <tr>
	        <td><% bp.getController().writeFormLabel(out,"dt_fine_validita");%></td>
	        <td><% bp.getController().writeFormInput(out,"default","dt_fine_validita", bp.isPublishHidden(),null,null);%></td>        
	        <td><% bp.getController().writeFormLabel(out,"dt_proroga");%></td>
	        <td><% bp.getController().writeFormInput(out,null,"dt_proroga",false,null,"");%></td>        	          
	      </tr>
		  <tr>         
	         <td><% bp.getController().writeFormLabel(out,"contratto_padre");%></td>
	         <td colspan="5"><% bp.getController().writeFormInput(out,"default","contratto_padre", bp.isPublishHidden(),null,null);%></td>
	      </tr>
      </table>
      </div>
      </td></tr>
      <tr>                        
         <td colspan=5>
		   <div class="Group card w-100">		
		   <table class="w-100" cellspacing="0" cellpadding="2">
			<tr>
				<td><% bp.getController().writeFormLabel( out, "tot_doc_cont_etr"); %></td>
				<td><% bp.getController().writeFormInput( out, "tot_doc_cont_etr"); %></td>
				<td><% Button.write(out,
						bp.getParentRoot().isBootstrap() ? "fa fa-external-link faa-horizontal" : "img/book_opened.gif",
						bp.getParentRoot().isBootstrap() ? "fa fa-external-link faa-horizontal" : "img/book_opened.gif",
						"Accertato",
						"javascript:submitForm('doVisualizzaDocContEtr')", 
						"btn-secondary btn-outline-primary btn-title btn-block faa-parent animated-hover",
						bp.getParentRoot().isBootstrap() ? "Accertato" : "Visualizza gli accertamenti associati al contratto", 
						((CRUDConfigAnagContrattoBP)bp).isVisualizzaDocContEtrButtonEnabled(), 
						bp.getParentRoot().isBootstrap()); %>
				</td>			
				<td><% bp.getController().writeFormLabel( out, "tot_doc_cont_spe"); %></td>
				<td><% bp.getController().writeFormInput( out, "tot_doc_cont_spe"); %></td>
				<td><% Button.write(out,
						bp.getParentRoot().isBootstrap() ? "fa fa-external-link faa-horizontal" : "img/book_opened.gif",
						bp.getParentRoot().isBootstrap() ? "fa fa-external-link faa-horizontal" : "img/book_opened.gif",
						"Impegnato",
						"javascript:submitForm('doVisualizzaDocContSpe')", 
						"btn-secondary btn-outline-primary btn-title btn-block faa-parent animated-hover",
						bp.getParentRoot().isBootstrap() ? "Impegnato" : "Visualizza gli impegni associati al contratto", 
						((CRUDConfigAnagContrattoBP)bp).isVisualizzaDocContSpeButtonEnabled(), 
						bp.getParentRoot().isBootstrap()); %>
				</td>
				
				<td rowspan="2">
					<% Button.write(out,
							bp.getParentRoot().isBootstrap() ? "fa fa-external-link faa-horizontal" : "img/book_opened.gif",
							bp.getParentRoot().isBootstrap() ? "fa fa-external-link faa-horizontal" : "img/book_opened.gif",
							"Commesse associate",
							"javascript:submitForm('doVisualizzaDocContForCommessaContratto')", 
							"btn-secondary btn-outline-primary btn-title btn-block faa-parent animated-hover",
							bp.getParentRoot().isBootstrap() ? "Commesse associate" : "Visualizza le commesse associate al contratto", 
							((CRUDConfigAnagContrattoBP)bp).isVisualizzaCommessaButtonEnabled(), 
							bp.getParentRoot().isBootstrap()); %>
				</td>			
			</tr>
			<tr>				
				<td><% bp.getController().writeFormLabel( out, "tot_docamm_cont_etr"); %></td>
				<td><% bp.getController().writeFormInput( out, "tot_docamm_cont_etr"); %></td>
				<td>
					<% Button.write(out,
							bp.getParentRoot().isBootstrap() ? "fa fa-external-link faa-horizontal" : "img/book_opened.gif",
							bp.getParentRoot().isBootstrap() ? "fa fa-external-link faa-horizontal" : "img/book_opened.gif",
							"Liquidato Entrate",
							"javascript:submitForm('doVisualizzaDocammContEtr')", 
							"btn-secondary btn-outline-primary btn-title btn-block faa-parent animated-hover",
							bp.getParentRoot().isBootstrap() ? "Liquidato Entrate" : "Visualizza i documenti amministrativi associati al contratto", 
							((CRUDConfigAnagContrattoBP)bp).isVisualizzaDocammContEtrButtonEnabled(), 
							bp.getParentRoot().isBootstrap()); %>
				</td>				
				<td><% bp.getController().writeFormLabel( out, "tot_docamm_cont_spe"); %></td>
				<td><% bp.getController().writeFormInput( out, "tot_docamm_cont_spe"); %></td>
				<td>
					<% Button.write(out,
							bp.getParentRoot().isBootstrap() ? "fa fa-external-link faa-horizontal" : "img/book_opened.gif",
							bp.getParentRoot().isBootstrap() ? "fa fa-external-link faa-horizontal" : "img/book_opened.gif",
							"Liquidato Spese",
							"javascript:submitForm('doVisualizzaDocammContSpe')", 
							"btn-secondary btn-outline-primary btn-title btn-block faa-parent animated-hover",
							bp.getParentRoot().isBootstrap() ? "Liquidato Spese" : "Visualizza  i documenti amministrativi associati al contratto", 
							((CRUDConfigAnagContrattoBP)bp).isVisualizzaDocammContSpeButtonEnabled(), 
							bp.getParentRoot().isBootstrap()); %>
				</td>
				<td></td>
			</tr>
			<tr>			
				<td><% bp.getController().writeFormLabel( out, "tot_doccont_cont_etr"); %></td>
				<td><% bp.getController().writeFormInput( out, "tot_doccont_cont_etr"); %></td>
				<td>
					<% Button.write(out,
							bp.getParentRoot().isBootstrap() ? "fa fa-external-link faa-horizontal" : "img/book_opened.gif",
							bp.getParentRoot().isBootstrap() ? "fa fa-external-link faa-horizontal" : "img/book_opened.gif",
							"Incassato",
							"javascript:submitForm('doVisualizzaDoccontContEtr')", 
							"btn-secondary btn-outline-primary btn-title btn-block faa-parent animated-hover",
							bp.getParentRoot().isBootstrap() ? "Incassato" : "Visualizza le reversali associati al contratto", 
							((CRUDConfigAnagContrattoBP)bp).isVisualizzaDoccontContEtrButtonEnabled(), 
							bp.getParentRoot().isBootstrap()); %>
				</td>				
				<td><% bp.getController().writeFormLabel( out, "tot_doccont_cont_spe"); %></td>
				<td><% bp.getController().writeFormInput( out, "tot_doccont_cont_spe"); %></td>
				<td>
					<% Button.write(out,
							bp.getParentRoot().isBootstrap() ? "fa fa-external-link faa-horizontal" : "img/book_opened.gif",
							bp.getParentRoot().isBootstrap() ? "fa fa-external-link faa-horizontal" : "img/book_opened.gif",
							"Pagato",
							"javascript:submitForm('doVisualizzaDoccontContSpe')", 
							"btn-secondary btn-outline-primary btn-title btn-block faa-parent animated-hover",
							bp.getParentRoot().isBootstrap() ? "Pagato" : "Visualizza i mandati associati al contratto", 
							((CRUDConfigAnagContrattoBP)bp).isVisualizzaDoccontContSpeButtonEnabled(), 
							bp.getParentRoot().isBootstrap()); %>
				</td>
				<td></td>
			</tr>
			<tr>				
				<td><% bp.getController().writeFormLabel( out, "tot_ordini"); %></td>
				<td><% bp.getController().writeFormInput( out, "tot_ordini"); %></td>
				<td></td>
			</tr>
		   </table>
		   </div>            
         </td>
      </tr>   
	</table>   
   </fieldset>