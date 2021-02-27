<%@ page pageEncoding="UTF-8"
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
    <%if (model!=null & model.getStatoText()!=null) { %>
	<legend class="GroupLabel">
    	<% bp.getController().writeFormInput(out,null,"statoText",true,"GroupLabel text-primary h3 inputFieldReadOnly",
		 	bp.getParentRoot().isBootstrap()?null:"style=\"background: #F5F5DC;background-color:transparent;border-style : none; cursor:default;font-size : 16px;\"");%>
	</legend>
	<% } %>

    <div class="Group card p-2 mb-2 sigla-mb-2">
	<table class="Panel w-100">
	  <tr>
	    <% bp.getController().writeFormField(out,"dt_registrazione");%>
        <% bp.getController().writeFormField(out,"cd_protocollo");%>
      </tr>
    </table>
    </div>  	

    <div class="Group card p-2 mb-2 sigla-mb-2">
	<table class="Panel w-100">
	  <tr>
         <td><% bp.getController().writeFormLabel(out,"cds");%></td>
		 <td colspan="2"><% bp.getController().writeFormInput(out,"default","cds",!bp.isUoEnte()||bp.isROIncaricoRichiesta(),null,null); %></td>
      </tr>  	      
	  <tr>
         <td><% bp.getController().writeFormLabel(out,"unita_organizzativa");%></td>
		 <td colspan="2"><% bp.getController().writeFormInput(out,"default","unita_organizzativa",!(bp.isUoEnte()||bp.isUtenteAbilitatoFunzioniIncarichi())||bp.isROIncaricoRichiesta(),null,null); %></td>
      </tr>  	      
	  <tr>
         <% bp.getController().writeFormField(out,"indirizzo_unita_organizzativa");%>
         <td>
         	<% bp.getController().writeFormLabel(out,"citta");%>
		 	<% bp.getController().writeFormInput(out,"citta");%>
		 </td>
      </tr>  	      
	  <tr>         
         <td><% bp.getController().writeFormLabel(out,"firmatario");%></td>
         <td colspan="2">
		    <% bp.getController().writeFormInput(out,"firmatario");%>
		 </td>
      </tr>            
    </table>
    </div>

	<%if (bp.isSearching()||
		  model.getDt_pubblicazione()!=null || model.getDt_fine_pubblicazione()!=null ||
		  model.getDt_scadenza()!=null) {%>
    <div class="Group card p-2 mb-2 sigla-mb-2">
	<table class="Panel w-100">
	  <tr>
	    <td><% bp.getController().writeFormField(out,"dt_pubblicazione");%></td>
	    <td>&nbsp;&nbsp;</td>
        <td><% bp.getController().writeFormField(out,"dt_fine_pubblicazione");%></td>
        <td>&nbsp;&nbsp;</td>
        <td><% bp.getController().writeFormField(out,"dt_scadenza");%></td>
      </tr>      
    </table>
    </div>
    <%}%>

    <div class="Group card p-2 mb-2 sigla-mb-2">
	<table class="Panel w-100">
	  <tr><% bp.getController().writeFormField(out,"atto");%></tr>
	  <tr><% bp.getController().writeFormField(out,"ds_atto");%></tr>
      <tr><% bp.getController().writeFormField(out,"terzo_resp");%></tr>
    </table>
    </div>

    <div class="Group card p-2 mb-2 sigla-mb-2">
	<table class="Panel w-100">
	  <tr>         
         <td><% bp.getController().writeFormLabel(out,"oggetto");%></td>
         <td colspan=4><% bp.getController().writeFormInput(out,"oggetto");%></td>
      </tr>                     	
	  <%if (bp==null||bp.isIncarichiProceduraBP()) {%>
		  <tr>
	        <% bp.getController().writeFormField(out,"procedura_amministrativa");%>
	      </tr>
	  <%}%>
    </table>
    </div>

	<% boolean writeTipoAttivita = bp==null||bp.isIncarichiProceduraBP()||bp.isBorseStudioBP(); %>
    <div class="Group card p-2 mb-2 sigla-mb-2">
	<table class="Panel <% if (writeTipoAttivita) { %>w-100<% } %>">
	  <tr>
		 <%if (writeTipoAttivita) {%>
		    <div>
	        <% bp.getController().writeFormField(out,"find_tipo_attivita");%>
	        </div>
	        <td>&nbsp;</td>
	     <%}%>
	     <div>
	 	 <% bp.getController().writeFormField(out,"tipo_natura");%>
	 	 </div>
      </tr>
	  <%if (bp==null||bp.isIncarichiProceduraBP()) {%>
		  <tr>
		  	<% bp.getController().writeFormField(out,"find_tipo_incarico");%>
			<% if (model.isMeramenteOccasionaleEnabled()) { %>
				 <td>&nbsp;</td>
			 	 <% bp.getController().writeFormField(out,"fl_meramente_occasionale");%>
			<% } %>
		  </tr>
		  <tr>
		  	 <% bp.getController().writeFormField(out,"tipo_prestazione");%>
		  </tr>
      <%}%>
    </table>
    </div>

    <div class="Group card p-2 mb-2 sigla-mb-2" width="100%">
	<table class="Panel w-100" width="100%">
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
	         <% bp.getController().writeFormField(out,"nr_contratti");%>
	         <td>&nbsp;</td>
	         <% bp.getController().writeFormField(out,"importo_lordo");%>
	         <td>&nbsp;</td>
	         <% bp.getController().writeFormField(out,"importo_complessivo");%>
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
	         <% bp.getController().writeFormField(out,"nr_contratti_search");%>
	         <td>&nbsp;</td>
	         <% bp.getController().writeFormField(out,"importo_lordo_search");%>
	         <td>&nbsp;</td>
	         <% bp.getController().writeFormField(out,"importo_complessivo_search");%>
		  </tr>
	  <% } %>
	<% } %>	  
    </table>
    </div>

    <%if (bp==null||bp.isIncarichiProceduraBP()) {%>
    <div class="Group card p-2 mb-2 sigla-mb-2">
	<table class="Panel w-100">
	  <tr>
         <td class="w-25"><% bp.getController().writeFormLabel(out,"incarichi_richiesta");%></td>
         <td><% bp.getController().writeFormInput(out,"default","incarichi_richiesta",bp.isROIncaricoRichiesta(),null,null); %></td>
      </tr>
    </table>
    </div>
	<% } %>	  

    <div class="Group card p-2 mb-2 sigla-mb-2">
	<table class="Panel w-100 d-flex flex-row">
	  <tr>
         <% bp.getController().writeFormField(out,"incarichi_procedura_padre");%>
      </tr>
    </table>
    </div>
</fieldset>
