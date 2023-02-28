<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->
<%@page import="it.cnr.contab.doccont00.core.bulk.ReversaleBulk"%>
<%@ page pageEncoding="UTF-8"
 
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*,it.cnr.contab.doccont00.bp.*"
%>


<%  
		CRUDReversaleBP bp = (CRUDReversaleBP)BusinessProcess.getBusinessProcess(request);
		it.cnr.contab.doccont00.core.bulk.ReversaleIBulk reversale = (it.cnr.contab.doccont00.core.bulk.ReversaleIBulk)bp.getModel();
%>
  <div class="Group card p-2">
  <table border="0" cellspacing="0" cellpadding="2">
	<tr>
			<td><% bp.getController().writeFormLabel( out, "esercizio"); %></td>
	   	<td><% bp.getController().writeFormInput( out, "esercizio"); %></td>
	   	<% if ( reversale.getCd_unita_organizzativa() != null && reversale.getCd_unita_organizzativa().equals( reversale.getCd_uo_ente())) { %>
	   	<td><% bp.getController().writeFormLabel( out, "ti_competenza_residuo"); %></td>
	   	<td><% bp.getController().writeFormInput( out, "ti_competenza_residuo"); %></td>
	   	<% } else { %>
		   <td colspan=2></td>
	   	<% } %>		   
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "unita_organizzativa"); %></td>
		<td><% bp.getController().writeFormInput( out,"default", "unita_organizzativa", false, null,"onchange=\"submitForm('doCambiaUnitaOrganizzativa')\"" ); %></td>
		<td colspan=2></td>						
	</tr>
  </table>
  </div>
  
  <div class="Group card p-2">
  <table border="0" cellspacing="0" cellpadding="2">
	<tr>
		<td>
			<%
			if (bp.isReversaleIncassoAbilitata())
				bp.getController().writeFormInput( out, "ti_reversale" );
			else
				bp.getController().writeFormInput( out, "ti_reversale_disab" );
			%>
		</td>
	</tr>
  </table>
  </div>
  
  <div class="Group card p-2">
  <table border="0" cellspacing="0" cellpadding="2" class="w-100">
	<tr>
			<td><% bp.getController().writeFormLabel( out, "pg_reversale"); %></td>
			<td><% bp.getController().writeFormInput( out, "pg_reversale"); %></td>
			<td><% bp.getController().writeFormLabel( out, "dt_emissione"); %></td>
			<td><% bp.getController().writeFormInput( out, "dt_emissione"); %></td>
			<td><% bp.getController().writeFormLabel( out, "stato"); %></td>
			<td><% bp.getController().writeFormInput( out, "stato"); %></td>
	</tr>
	<tr>
			<td><% bp.getController().writeFormLabel( out, "dt_trasmissione"); %></td>
			<td><% bp.getController().writeFormInput( out, "dt_trasmissione"); %></td>
			<td><% bp.getController().writeFormLabel( out, "dt_ritrasmissione"); %></td>
			<td><% bp.getController().writeFormInput( out, "dt_ritrasmissione"); %></td>
			<td><% bp.getController().writeFormLabel( out, "stato_trasmissione"); %></td>
			<td><% bp.getController().writeFormInput( out, "stato_trasmissione"); %></td>
	</tr>
	<tr>
			<td><% bp.getController().writeFormLabel( out, "ds_reversale"); %></td>
			<td colspan=7><% bp.getController().writeFormInput( out,"default", "ds_reversale",reversale.isAnnullato(),null,null); %></td>
	</tr>
	
	<% if (!bp.isSearching() && reversale!=null && reversale.getStato().equals(ReversaleBulk.STATO_REVERSALE_ANNULLATO) && reversale.getDt_trasmissione() !=null) {%>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "stato_trasmissione_annullo"); %></td>
		<td><% bp.getController().writeFormInput( out, "stato_trasmissione_annullo"); %></td>
	</tr>	
		<% if ( reversale.getFl_riemissione().booleanValue()) {%>
		<tr>
			<td><% bp.getController().writeFormLabel( out, "pg_reversale_riemissione"); %></td>
			<td colspan=3><% bp.getController().writeFormInput( out, "default","pg_reversale_riemissione",!(reversale.getStato_trasmissione_annullo().equals(ReversaleBulk.STATO_TRASMISSIONE_NON_INSERITO)),null,null); %>
				<% bp.getController().writeFormInput( out, "ds_documento_cont"); %>
				<% bp.getController().writeFormInput( out, "default","find_documento_cont",!(reversale.getStato_trasmissione_annullo().equals(ReversaleBulk.STATO_TRASMISSIONE_NON_INSERITO)),null,null); %>
			</td>
		</tr>	
		<% } %>
	<% } %>
  </table>
  </div>

  <div class="Group card p-2">
  <table border="0" cellspacing="0" cellpadding="2" class="w-100">
  	<tr>
			<td><% bp.getController().writeFormLabel( out, "im_reversale"); %></td>
    		<td><% bp.getController().writeFormInput( out, "im_reversale"); %></td>
    		<td class="text-right"><% bp.getController().writeFormLabel( out, "im_incassato"); %></td>
    		<td><% bp.getController().writeFormInput( out, "im_incassato"); %></td>
	</tr>
	
  </table>
  </div>
<% if (!bp.isSearching() && bp.isSiope_attiva() && reversale.isRequiredSiope()) {%>
  <div class="Group">
	<fieldset class="fieldset">
	<legend class="GroupLabel text-primary ml-2 h2">Codici SIOPE</legend>
	<div class="card p-2 w-100">
	  <table border="0" cellspacing="0" cellpadding="2" class="w-100">
	  	<tr>
				<td><% bp.getController().writeFormLabel( out, "im_associato_siope"); %></td>
	    		<td><% bp.getController().writeFormInput( out, "im_associato_siope"); %></td>
				<td class="text-right"><% bp.getController().writeFormLabel( out, "im_da_associare_siope");%></td>
	    		<td><% bp.getController().writeFormInput( out, "im_da_associare_siope");%></td>
		</tr>
	  </table>
	  </div>
	</fieldset>
  </div>  
<% } %>
<% if (bp.isAttivoSiopeplus()) {%>
  <div class="Group">
	<fieldset class="fieldset">
	<legend class="GroupLabel text-primary ml-2 h2">SIOPE+</legend>
	  <div class="card p-2 w-100">
          <table border="0" cellspacing="0" cellpadding="2" class="w-100">
            <tr>
                    <% bp.getController().writeFormField( out, "esitoOperazione"); %>
                    <% bp.getController().writeFormField( out, "statoVarSos"); %>
                    <td class="text-right"><% bp.getController().writeFormLabel( out, "dtOraEsitoOperazione"); %></td>
                    <td><% bp.getController().writeFormInput( out, "dtOraEsitoOperazione"); %></td>
            </tr>
            <tr>
                <td><% bp.getController().writeFormLabel( out, "erroreSiopePlus"); %></td>
                <td colspan="6"><% bp.getController().writeFormInput( out, "erroreSiopePlus"); %></td>
            </tr>
          </table>
	  </div>
	</fieldset>
  </div>
<% } %>
  