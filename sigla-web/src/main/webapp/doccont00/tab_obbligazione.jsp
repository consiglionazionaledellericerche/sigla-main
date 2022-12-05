<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,it.cnr.contab.doccont00.bp.*"
%>


<%  
		CRUDObbligazioneBP bp = (CRUDObbligazioneBP)BusinessProcess.getBusinessProcess(request);
		it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = (it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk)bp.getModel();
%>
<div class="card p-3 m-1">
  <table border="0" cellspacing="0" cellpadding="2" class="w-100">
	<tr colspan=3>
<!--
	<td><% bp.getController().writeFormLabel( out, "esercizio_competenza"); %></td>
	<td><% bp.getController().writeFormInput(out,"default","esercizio_competenza", bp.isFromDocAmm(),null,null); %></td>	
-->	
	<td><% bp.getController().writeFormLabel( out, "esercizio_originale"); %></td>
	<td><% bp.getController().writeFormInput( out,"default","esercizio_originale",false,null,null); %></td>

	<td align="right">
	    <% bp.getController().writeFormInput(out,"default","fl_calcolo_automatico",false,null,"onclick=\"submitForm('doCambiaFl_calcolo_automatico')\""); %>
	    <% bp.getController().writeFormLabel( out, "fl_calcolo_automatico"); %></td>
<!--	<td align="right">
	    <% bp.getController().writeFormInput(out,"fl_pluriennale");%>
	    <% bp.getController().writeFormLabel(out,"fl_pluriennale");%></td> -->

	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "cd_unita_organizzativa"); %></td>
	<td colspan=2>
		<div class="input-group input-group-searchtool w-100 ">
	    	<% bp.getController().writeFormInput( out, "cd_unita_organizzativa"); %>
			<% bp.getController().writeFormInput( out, "ds_unita_organizzativa"); %></td>
		</div>				 
	</tr>
	<tr>  
	<td><% bp.getController().writeFormLabel( out, "pg_obbligazione"); %></td>	
	<td colspan=2>
		<% bp.getController().writeFormInput( out, "pg_obbligazione"); %>
		<% bp.getController().writeFormLabel( out, "dt_registrazione"); %>
		<% bp.getController().writeFormInput( out, "dt_registrazione"); %>
		<% bp.getController().writeFormLabel( out, "stato_obbligazione"); %>
		<% bp.getController().writeFormInput( out, "stato_obbligazione"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "pg_obbligazione_ori_riporto"); %></td>
	<td colspan=2>
		<% bp.getController().writeFormInput( out, "pg_obbligazione_ori_riporto"); %></td>
	</tr>

	<%  if( bp instanceof CRUDObbligazioneResBP  && ((CRUDObbligazioneResBP)bp).isStatoResiduoVisibile()) { %>
		<tr>
			<td><% bp.getController().writeFormLabel( out, ((CRUDObbligazioneResBP)bp).isROStato() ?"statoResiduoObbligazione_ro":"statoResiduoObbligazione"); %></td>
			<td colspan=2>
			<% bp.getController().writeFormInput( out, "default", ((CRUDObbligazioneResBP)bp).isROStato() ?"statoResiduoObbligazione_ro":"statoResiduoObbligazione",bp.isRoCampiResiduoProprio(),null,null); %>
			</td>
		</tr>		  		
	<%  } %>
	
	<tr>
	<td><% bp.getController().writeFormLabel( out, "ds_obbligazione"); %></td>
	<td colspan="2">
		<% bp.getController().writeFormInput( out,"default", "ds_obbligazione",bp.isRoCampiResiduoProprio(),null,null); %></td>
	</tr>

<!-- 
	<tr>
	<td><% bp.getController().writeFormLabel( out, "motivazione"); %></td>
	<td colspan=2>
		<% bp.getController().writeFormInput( out, "motivazione"); %></td>
	</tr>
 -->
	</table>
</div>
	
<div class="Group card p-3 m-1">
	<table class="w-100">
    <tr>
    <td colspan=3>
	    <% if (obbligazione!=null && (obbligazione.isCompetenza() || obbligazione.isObbligazioneResiduoImproprio() ||
	    (obbligazione.isObbligazioneResiduo() && obbligazione.isProvvisoria()))) { %>
	    <table border="0" cellspacing="0" cellpadding="2" class="w-100">
		    <tr><td>
			    <table border="0" cellspacing="0" cellpadding="2" class="w-100"> 
	    		<tr>
	     			<td><% bp.getController().writeFormInput( out, "fl_gara_in_corso"); %></td>
					<td><% bp.getController().writeFormLabel( out, "fl_gara_in_corso"); %></td>
					<% if (obbligazione!=null && (obbligazione.getDs_gara_in_corso()!=null || 
						        (obbligazione.getFl_gara_in_corso()!=null && obbligazione.getFl_gara_in_corso().booleanValue()))) {%>
	     				<td><% bp.getController().writeFormInput( out, "ds_gara_in_corso"); %></td>
	     			<% } %>
	    		</tr>
	    		</table>
	    	</td></tr>
	    	<tr>
				<td>
		<% } %>
				<div class="GroupLabel font-weight-bold text-primary ml-2">Repertorio</div>  
				<div class="Group card p-3 m-1 w-100">
				<table class="w-100">
				  <tr>
				    <td><% bp.getController().writeFormLabel( out, "find_contratto"); %></td>
					<td colspan=2>
						<% bp.getController().writeFormInput( out, "find_contratto"); %>
					</td>				 
				  </tr>
				  <% if (bp.isIncarichi_repertorio_attiva()) {%>
			      <tr>
			         <td><% bp.getController().writeFormLabel(out,"find_incarico_repertorio");%></td>
			         <td colspan=2>
			         	 <% bp.getController().writeFormInput(out,"find_incarico_repertorio");%>
			         </td>
			      </tr>
			      <% } %>
				</table>	
			    </div>
		<% if (obbligazione!=null && (obbligazione.isCompetenza() || obbligazione.isObbligazioneResiduoImproprio())) { %>
		  	</td></tr>
		</table>
		<% } %>
	</td></tr>    
	</table>
</div>

    <% if (!bp.getParentRoot().isBootstrap()) { %>
	    <br>
	<% } %>
    
<div class="Group card p-3 m-1">
	<table class="w-100">
	  <tr>
	   <td><% bp.getController().writeFormLabel( out, "find_creditore"); %></td>
	   <td colspan="2"><% bp.getController().writeFormInput( out,"default","find_creditore",bp.isRoCampiResiduoProprioRibaltato(),null,null); %>
	   </td>
	  </tr>
	  <tr>
	   <td><% bp.getController().writeFormLabel( out, "cd_terzo_precedente"); %></td>
	   <td colspan="2"><% bp.getController().writeFormInput( out, "cd_terzo_precedente"); %></td>
	  </tr>
	  <tr>
	   <td>	<% bp.getController().writeFormLabel( out, "codice_fiscale"); %></td>
	   <td>	<% bp.getController().writeFormInput( out, "codice_fiscale"); %></td>
	   <td>
            <% bp.getController().writeFormLabel( out, "partita_iva"); %>
	        <% bp.getController().writeFormInput( out, "partita_iva"); %>	        
	   </td>
	  </tr>
	</table>	
</div>

    <% if (!bp.getParentRoot().isBootstrap()) { %>
	    <br>
	<% } %>
	
	<div class="card p-3 m-1">
	<table class="w-100">
	<tr>
  	<% if (bp instanceof CRUDObbligazioneResBP){ %>
  		<% if (((CRUDObbligazioneResBP)bp).isROImporto()){ %>
			<td><% bp.getController().writeFormLabel( out, "im_obbligazione_ro" ); %></td>
			<td><% bp.getController().writeFormInput( out, "im_obbligazione_ro" ); %></td>
	  	<%} else {%>
			<td><% bp.getController().writeFormLabel( out, "im_obbligazione" ); %></td>
			<td><% bp.getController().writeFormInput( out, "im_obbligazione" ); %></td>
	  	<%}%>
  	<%} else {%>
		<td><% bp.getController().writeFormLabel( out, "im_obbligazione"); %></td>
		<td><% bp.getController().writeFormInput( out, "im_obbligazione"); %></td>
  	<%}%>
	<td align="right">
	  <% if (!bp.isAttivoRegolamento_2006()){ %>
			<% bp.getController().writeFormInput( out, "fl_spese_costi_altrui"); %>
			<% bp.getController().writeFormLabel( out, "fl_spese_costi_altrui"); %>
	  <%}%>
	</td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "find_elemento_voce"); %></td>
		<td colspan=2>
			<table>
			<tr>
			<td>
			    <% bp.getController().writeFormInput(out,"default","find_elemento_voce",bp.isRoCampiResiduoProprio() || bp.isROFindElemento_voce(),null,null); %>
			</td>
			<td>
			<%JSPUtils.button(out, 
					bp.getParentRoot().isBootstrap() ? "fa fa-external-link faa-shake" : "img/find16.gif",
					bp.getParentRoot().isBootstrap() ? "fa fa-external-link faa-shake" : "img/find16.gif",
					bp.getParentRoot().isBootstrap() ? "Disponibilità Voce":"Disponibilità<BR>Voce",
					"if (disableDblClick()) submitForm('doConsultaInserisciVoce')",
					"btn-outline-primary btn-title faa-parent animated-hover",
					!bp.isViewing() && (bp.isRoCampiResiduoProprio() || bp.isROFindElemento_voce()),
					bp.getParentRoot().isBootstrap());%>
			</td>
			</tr>
			</table>			
		</td>				 
	</tr>
  	<% if (bp.isElementoVoceNewVisible()){ %>
	<tr>
		<td colspan="3">
			<div class="Group" style="border-color:red">
			<table>
			<tr>
				<td><% bp.getController().writeFormLabel( out, "cd_elemento_voce_next"); %><label> <%=Integer.valueOf(obbligazione.getEsercizio()+1).toString()%></label></td>
				<td colspan=2>
				    <% bp.getController().writeFormInput(out,"default","cd_elemento_voce_next"); %>
				    <% bp.getController().writeFormInput(out,"default","ds_elemento_voce_next"); %>
				    <% bp.getController().writeFormInput(out,"default","find_elemento_voce_next"); %>
				</td>				 
			</tr>
			</table>
			</div>
		</td>
	</tr>
    <%}%>
  	<% if (bp.isVariazioneAutomaticaEnabled() && !bp.isSearching() && obbligazione.getPg_obbligazione()==null){ %>
	<tr>
		<td colspan="3">
			<div class="Group" style="border-color:red">
			<table>
			<tr>
				<% bp.getController().writeFormField(out,"default","findGaeDestinazioneFinale"); %>
			</tr>
			<tr>
        		<td><% bp.getController().writeFormLabel( out, "cdCdrDestinazioneFinale"); %></td>
        		<td><% bp.getController().writeFormInput(out,"default","cdCdrDestinazioneFinale"); %>
				    <% bp.getController().writeFormInput(out,"default","dsCdrDestinazioneFinale"); %></td>
			</tr>
			</table>
			</div>
		</td>
	</tr>
    <%}%>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "cd_riferimento_contratto"); %></td>
		<td><% bp.getController().writeFormInput( out,"default", "cd_riferimento_contratto", bp.isRoCampiResiduoProprio(),null,null); %></td>
		<td><% bp.getController().writeFormLabel( out, "dt_scadenza_contratto"); %>
		    <% bp.getController().writeFormInput( out,"default", "dt_scadenza_contratto", bp.isRoCampiResiduoProprio(),null,null); %></td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "find_tipo_obbligazione"); %></td>
		<td colspan=2>
			<% bp.getController().writeFormInput( out,"default", "find_tipo_obbligazione", bp.isRoCampiResiduoProprio(),null,null); %>
		</td>
	</tr>
  </table>
  </div>