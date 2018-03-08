<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.doccont00.bp.*,
		it.cnr.contab.doccont00.core.bulk.*"
%>

	<%  
			CRUDMandatoAccreditamentoBP bp = (CRUDMandatoAccreditamentoBP)BusinessProcess.getBusinessProcess(request);
			it.cnr.contab.doccont00.core.bulk.MandatoAccreditamentoBulk mandatoAccreditamento = (it.cnr.contab.doccont00.core.bulk.MandatoAccreditamentoBulk)bp.getModel();
	%>

	<div class="Group">		
	<table class="Panel">
		<tr>
			<td><% bp.getController().writeFormLabel( out, "esercizio"); %></td>
	   	<td colspan=4><% bp.getController().writeFormInput( out, "esercizio"); %></td>
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel( out, "cd_cds"); %></td>
			<td><% bp.getController().writeFormInput( out, "cd_cds"); %></td>
			<td colspan=2><% bp.getController().writeFormInput( out, "ds_cds"); %></td>
			<td></td>
<!-- 			<% JSPUtils.button(out,bp.encodePath("img/zoom16.gif"), "Visualizza entrate", "javascript:submitForm('doVisualizzaEntrate')" , bp.getParentRoot().isBootstrap()); %>
			<% JSPUtils.button(out,bp.encodePath("img/zoom16.gif"), "Visualizza spese", "javascript:submitForm('doVisualizzaSpese')", bp.getParentRoot().isBootstrap() ); %>
-->			
			
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel( out, "pg_mandato"); %></td>	
			<td><% bp.getController().writeFormInput( out, "pg_mandato"); %></td>
			<td><% bp.getController().writeFormLabel( out, "im_righe_mandato"); %></td>
			<td><% bp.getController().writeFormInput( out, "im_righe_mandato"); %></td>
			<td><% bp.getController().writeFormLabel( out, "stato"); %>
				<% bp.getController().writeFormInput( out, "stato"); %></td>			    
		</tr>
		<tr>

			<td><% bp.getController().writeFormLabel( out, "dt_emissione"); %></td>
			<td><% bp.getController().writeFormInput( out, "dt_emissione"); %></td>			
			<td><% bp.getController().writeFormLabel( out, "ti_competenza_residuo");%></td>
			<td><% bp.getController().writeFormInput( out, "ti_competenza_residuo");%></td>
			<td><% bp.getController().writeFormLabel( out, "stato_trasmissione"); %>
				<% bp.getController().writeFormInput( out, "stato_trasmissione"); %></td>
		</tr>
		<tr>

			<td><% bp.getController().writeFormLabel( out, "ds_mandato"); %></td>
			<td colspan=4><% bp.getController().writeFormInput( out, "ds_mandato"); %></td>		
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel( out, "terzo_cd_terzo"); %></td>
			<td colspan=4><% bp.getController().writeFormInput( out, "terzo_cd_terzo"); %>
			    <% bp.getController().writeFormInput( out, "terzo_ds_terzo"); %>
			    <% bp.getController().writeFormLabel( out, "codice_cds"); %>
			    <% bp.getController().writeFormInput( out, "codice_cds"); %>			    
			 </td>						
		</tr>
	</table>
	</div>
	
	<table class="Panel">
		<tr>
			<td><b>Mandato righe</b></td>
		</tr>
		<tr>
			<td>		
		   <% bp.getImpegni().writeHTMLTable(pageContext,"impegni_man",bp.isAddImpegniButtonEnabled(), false,bp.isDeleteImpegniButtonEnabled(),"100%","100px", false);%>
		   </td>
		</tr>
	</table>
	<div class="Group">		
	<table border="0" cellspacing="0" cellpadding="2">
		<tr>
			<td><% bp.getController().writeFormLabel( out, "cd_modalita_pag"); %></td>
			<td><% bp.getController().writeFormInput( out, "cd_modalita_pag"); %></td>
			<td><% bp.getController().writeFormLabel( out, "banca"); %></td>
			<td><% bp.getController().writeFormInput( out, "banca"); %></td>
		</tr>

	</table>
	</div>	