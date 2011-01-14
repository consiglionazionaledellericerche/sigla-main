<%@ page 
	import="it.cnr.jada.action.*,
		    it.cnr.contab.incarichi00.bp.*,
  		    it.cnr.contab.incarichi00.bulk.*"
%>

<%
	CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)BusinessProcess.getBusinessProcess(request);
 	Incarichi_proceduraBulk  model = (Incarichi_proceduraBulk)bp.getModel();
%>

   <%@page import="java.math.BigDecimal"%>
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
      <div class="Group">
      <table>
	  <tr>
         <td><% bp.getController().writeFormLabel(out,"cds");%></td>
		 <td colspan=3><% bp.getController().writeFormInput(out,"default","cds",!bp.isUoEnte()||bp.isROIncaricoRichiesta(),"FormInput",null); %></td>
      </tr>  	      
	  <tr>
         <td><% bp.getController().writeFormLabel(out,"unita_organizzativa");%></td>
		 <td colspan=3><% bp.getController().writeFormInput(out,"default","unita_organizzativa",!(bp.isUoEnte()||bp.isUtenteAbilitatoFunzioniIncarichi())||bp.isROIncaricoRichiesta(),"FormInput",null); %></td>
      </tr>  	      
	  <tr>
         <td><% bp.getController().writeFormLabel(out,"indirizzo_unita_organizzativa");%></td>
		 <td><% bp.getController().writeFormInput(out,"indirizzo_unita_organizzativa");%></td>
         <td colspan=2><% bp.getController().writeFormLabel(out,"citta");%>
		 		       <% bp.getController().writeFormInput(out,"citta");%></td>
      </tr>  	      
	  <tr>         
         <td><% bp.getController().writeFormLabel(out,"firmatario");%></td>
         <td colspan=3><% bp.getController().writeFormInput(out,"cd_firmatario");%>
             		   <% bp.getController().writeFormInput(out,"ds_firmatario");%>
		               <% bp.getController().writeFormInput(out,"firmatario");%></td>
      </tr>            
      </table></div>
      </td></tr>
	  <%if (bp.isSearching()||
			model.getDt_pubblicazione()!=null || model.getDt_fine_pubblicazione()!=null ||
			model.getDt_scadenza()!=null) {%>
	      <tr><td colspan=4>
	      <div class="Group"><table>            
		  <tr>
		    <td><% bp.getController().writeFormField(out,"dt_pubblicazione");%></td>
		    <td>&nbsp;&nbsp;</td>
	        <td><% bp.getController().writeFormField(out,"dt_fine_pubblicazione");%></td>
	        <td>&nbsp;&nbsp;</td>
	        <td><% bp.getController().writeFormField(out,"dt_scadenza");%></td>
	      </tr>      
	      </table></div>
	      </td></tr>
	  <%}%>
      <tr><td colspan=4>
      <div class="Group"><table>      
	  <tr>         
         <td><% bp.getController().writeFormLabel(out,"atto");%></td>
         <td><% bp.getController().writeFormInput(out,"atto");%></td>
      </tr>
	  <tr>         
         <td><% bp.getController().writeFormLabel(out,"ds_atto");%></td>
         <td><% bp.getController().writeFormInput(out,"ds_atto");%></td>
      </tr>
      <tr>
         <td><% bp.getController().writeFormLabel(out,"terzo_resp");%></td>
         <td><% bp.getController().writeFormInput(out,"cd_terzo_resp");%>
             <% bp.getController().writeFormInput(out,"ds_terzo_resp");%>
		     <% bp.getController().writeFormInput(out,"terzo_resp");%></td>
      </tr>
      </table></div>
      </td></tr>
      <tr><td colspan=4>
      <div class="Group"><table>
	  <tr>         
         <td><% bp.getController().writeFormLabel(out,"oggetto");%></td>
         <td colspan=4><% bp.getController().writeFormInput(out,"oggetto");%></td>
      </tr>                     	
	  <tr>
        <td><% bp.getController().writeFormLabel(out,"procedura_amministrativa");%></td>
        <td><% bp.getController().writeFormInput(out,"procedura_amministrativa");%></td>
        <td>&nbsp;&nbsp;</td>
        <td><% bp.getController().writeFormLabel(out,"fl_art51");%></td>
        <td><% bp.getController().writeFormInput(out,"fl_art51");%></td>
      </tr>
      </table></div>
      </td></tr>
      <tr><td colspan=4>
      <div class="Group"><table>            
	  <tr>         
         <td><% bp.getController().writeFormLabel(out,"find_tipo_attivita");%></td>
         <td><% bp.getController().writeFormInput(out,"find_tipo_attivita");%></td>
         <td>&nbsp;</td>
	 	 <td><% bp.getController().writeFormLabel(out,"tipo_natura");%></td>
		 <td><% bp.getController().writeFormInput(out,"tipo_natura");%></td>
      </tr>
	  <tr>
	  	 <td><% bp.getController().writeFormLabel(out,"find_tipo_incarico");%></td>
	 	 <td><% bp.getController().writeFormInput(out,"find_tipo_incarico");%></td>
		<% if (model.isMeramenteOccasionaleEnabled()) { %>
			 <td>&nbsp;</td>
		 	 <td><% bp.getController().writeFormLabel(out,"fl_meramente_occasionale");%></td>
			 <td><% bp.getController().writeFormInput(out,"fl_meramente_occasionale");%></td>
		<% } %>
	  </tr>
      </table></div>
      </td></tr>
      <tr><td colspan=4>
      <div class="Group" width="100%"><table width="100%">            
<% if (model.getNr_contratti()!=null && model.getNr_contratti().compareTo(new Integer(1))==1) {
    String nrRighe="3";
	if (model.hasVariazioni()) nrRighe="5";%>
	  <tr>
	  <% if (!bp.isSearching()) { %>
         <td rowspan=<%=nrRighe%> valign="middle"><% bp.getController().writeFormLabel(out,"nr_contratti");%></td>
         <td rowspan=<%=nrRighe%> valign="middle"><% bp.getController().writeFormInput(out,"nr_contratti");%></td>
	  <% } else { %>
	     <td rowspan=<%=nrRighe%> valign="middle"><% bp.getController().writeFormLabel(out,"nr_contratti_search");%></td>
	     <td rowspan=<%=nrRighe%> valign="middle"><% bp.getController().writeFormInput(out,"nr_contratti_search");%></td>
  	  <% } %>
         <td rowspan=<%=nrRighe%> valign="middle">&nbsp;</td>
         <td>&nbsp;</td>
         <td><span class="FormLabel">Lordo Percipiente</span></td>
         <td><span class="FormLabel">Spesa complessiva<BR>presunta calcolata</span></td>
	  </tr>
	  <% if (!bp.isSearching()) { %>
		  <tr>
	         <td><span class="FormLabel">Singolo Contratto</span></td>
	         <td><% bp.getController().writeFormInput(out,"importo_lordo");%></td>
	         <td><% bp.getController().writeFormInput(out,"importo_complessivo");%></td>
		  </tr>
		  <% if (model.hasVariazioni()) {%>		  
		  <tr>
	         <td><span class="FormLabel">Totale Parziale</span></td>
	         <td><% bp.getController().writeFormInput(out,"importo_lordo_iniziale");%></td>
	         <td><% bp.getController().writeFormInput(out,"importo_complessivo_iniziale");%></td>
		  </tr>
		  <tr>
	         <td><span class="FormLabel">Variazioni</span></td>
	         <td><% bp.getController().writeFormInput(out,"importo_lordo_variazioni");%></td>
	         <td><% bp.getController().writeFormInput(out,"importo_complessivo_variazioni");%></td>
		  </tr>
	  	  <% } %>
		  <tr>
	         <td><span class="FormLabel">Totale</span></td>
	         <td><% bp.getController().writeFormInput(out,"importo_lordo_procedura");%></td>
	         <td><% bp.getController().writeFormInput(out,"importo_complessivo_procedura");%></td>
		  </tr>
	<% } else { %>
		  <tr>
	         <td><span class="FormLabel">Singolo Contratto</span></td>
	         <td><% bp.getController().writeFormInput(out,"importo_lordo_search");%></td>
	         <td><% bp.getController().writeFormInput(out,"importo_complessivo_search");%></td>
		  </tr>
	<% } %>
<% } else { %>	  
	  <% if (!bp.isSearching()) { %>
		  <% if (model.getImporto_complessivo_variazioni().compareTo(BigDecimal.ZERO)==0) {%>		  
		  <tr>
	         <td><% bp.getController().writeFormLabel(out,"nr_contratti");%></td>
	         <td><% bp.getController().writeFormInput(out,"nr_contratti");%></td>
	         <td>&nbsp;</td>
	         <td><% bp.getController().writeFormLabel(out,"importo_lordo");%></td>
	         <td><% bp.getController().writeFormInput(out,"importo_lordo");%></td>
	         <td>&nbsp;</td>
	         <td><% bp.getController().writeFormLabel(out,"importo_complessivo");%></td>
	         <td><% bp.getController().writeFormInput(out,"importo_complessivo");%></td>
		  </tr>
		<% } else { %>
		  <tr>
	         <td rowspan="4" valign="middle"><% bp.getController().writeFormLabel(out,"nr_contratti");%></td>
	         <td rowspan="4" valign="middle"><% bp.getController().writeFormInput(out,"nr_contratti");%></td>
	         <td rowspan="4" valign="middle">&nbsp;</td>
	         <td>&nbsp;</td>
	         <td><span class="FormLabel">Lordo Percipiente</span></td>
	         <td><span class="FormLabel">Spesa complessiva<BR>presunta calcolata</span></td>
		  </tr>
		  <tr>
	         <td><span class="FormLabel">Importo Iniziale</span></td>
	         <td><% bp.getController().writeFormInput(out,"importo_lordo");%></td>
	         <td><% bp.getController().writeFormInput(out,"importo_complessivo");%></td>
		  </tr>
		  <tr>
	         <td><span class="FormLabel">Variazioni</span></td>
	         <td><% bp.getController().writeFormInput(out,"importo_lordo_variazioni");%></td>
	         <td><% bp.getController().writeFormInput(out,"importo_complessivo_variazioni");%></td>
		  </tr>
		  <tr>
	         <td><span class="FormLabel">Totale</span></td>
	         <td><% bp.getController().writeFormInput(out,"importo_lordo_procedura");%></td>
	         <td><% bp.getController().writeFormInput(out,"importo_complessivo_procedura");%></td>
		  </tr>
		<% } %>
	<% } else { %>
		  <tr>
	         <td><% bp.getController().writeFormLabel(out,"nr_contratti_search");%></td>
	         <td><% bp.getController().writeFormInput(out,"nr_contratti_search");%></td>
	         <td>&nbsp;</td>
	         <td><% bp.getController().writeFormLabel(out,"importo_lordo_search");%></td>
	         <td><% bp.getController().writeFormInput(out,"importo_lordo_search");%></td>
	         <td>&nbsp;</td>
	         <td><% bp.getController().writeFormLabel(out,"importo_complessivo_search");%></td>
	         <td><% bp.getController().writeFormInput(out,"importo_complessivo_search");%></td>
		  </tr>
	<% } %>
<% } %>	  
      </table></div>
      </td></tr>
      <tr><td colspan=4>
      <div class="Group"><table>            
	  <tr>
         <td><% bp.getController().writeFormLabel(out,"incarichi_richiesta");%></td>
         <td colspan=3><% bp.getController().writeFormInput(out,"default","incarichi_richiesta",bp.isROIncaricoRichiesta(),"FormInput",null); %></td>
      </tr>
      </table></div>
      </td></tr>
      <tr><td colspan=4>
      <div class="Group"><table>            
	  <tr>
         <td><% bp.getController().writeFormLabel(out,"incarichi_procedura_padre");%></td>
         <td colspan=3><% bp.getController().writeFormInput(out,"incarichi_procedura_padre");%></td>
      </tr>
      </table></div>
      </td></tr>		
 	</table>   
   </fieldset>
