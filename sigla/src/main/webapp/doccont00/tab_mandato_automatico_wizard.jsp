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

<html>
	<%  
			MandatoAutomaticoWizardBP bp = (MandatoAutomaticoWizardBP)BusinessProcess.getBusinessProcess(request);
			it.cnr.contab.doccont00.core.bulk.MandatoAutomaticoWizardBulk mandatoAutomatico = (it.cnr.contab.doccont00.core.bulk.MandatoAutomaticoWizardBulk)bp.getModel();
	%>

	<div class="Group">		
	<table class="Panel">
		<tr>
			<td><% bp.getController().writeFormLabel( out, "esercizio"); %></td>
	   		<td><% bp.getController().writeFormInput( out, "esercizio"); %></td>
			<td colspan="4" align="right"><% bp.getController().writeFormLabel( out, "dt_emissione"); %>
				<% bp.getController().writeFormInput( out, "dt_emissione"); %></td>	   		
		</tr>
	<% if (mandatoAutomatico.isAutomatismoDaImpegni()) { %>
		<tr>
			<td><% bp.getController().writeFormLabel( out, "terzo_cd_terzo"); %></td>
			<td colspan="3"><% bp.getController().writeFormInput( out, "terzo_cd_terzo"); %>
							<% bp.getController().writeFormInput( out, "terzo_ds_terzo"); %> </td>
			<td colspan="2" align="right"><% bp.getController().writeFormLabel( out, "ti_istituz_commerc"); %>
		    							  <% bp.getController().writeFormInput( out, "ti_istituz_commerc"); %></td>
		</tr>			    
		<tr>
			<td><% bp.getController().writeFormLabel( out, "cd_modalita_pag"); %></td>
		    <td colspan= "5"><% bp.getController().writeFormInput( out,"default", "cd_modalita_pag", false, null,"onchange=\"submitForm('doCambiaModalitaPagamento')\"" ); %>	
						     <% bp.getController().writeFormLabel( out, "banca"); %>
		  					 <% bp.getController().writeFormInput( out, "banca"); %></td>
		</tr>
		<tr>			    
			<td><% bp.getController().writeFormLabel( out, "dt_da_competenza_coge"); %></td>
			<td colspan= "5"><% bp.getController().writeFormInput( out, "dt_da_competenza_coge" ); %>
							 <% bp.getController().writeFormLabel( out, "dt_a_competenza_coge"); %>
							 <% bp.getController().writeFormInput( out, "dt_a_competenza_coge"); %></td>
		</tr>
    <% } %>
		<tr>			    
			<td><% bp.getController().writeFormLabel( out, "terzo_tipo_bollo"); %></td>
			<td colspan= "5"><% bp.getController().writeFormInput( out, "terzo_tipo_bollo" ); %></td>
		</tr>
	</table>
	</div>
	<table class="Panel">	
		<% if (mandatoAutomatico.isAutomatismoDaImpegni()) { %>
			<tr>
				<td><b ALIGN="CENTER">Impegni</b></td>
				<% if (bp.isFlCalcoloAutomaticoCheckboxVisible()) { %>
				  <td align="right"><% bp.getController().writeFormInput(out,null,"fl_imputazione_manuale",!bp.isFlCalcoloAutomaticoCheckboxEnabled(),null,"onclick=\"submitForm('doCambiaFl_imputazione_manuale')\""); %>
	 	   			 			    <% bp.getController().writeFormLabel( out, "fl_imputazione_manuale"); %></td>
			    <% } %>
			</tr>
			<tr>
				<td colspan="2">
					<% if ( mandatoAutomatico.isFl_imputazione_manuale() )
							bp.getImpegni().writeHTMLTable(pageContext,"impegni_man",false,false,false,"100%","100px", false); 
					   else
							bp.getImpegni().writeHTMLTable(pageContext,"impegni_auto",false,false,false,"100%","100px", false);
					%>	
				</td>
			</tr>
		<% } else if (mandatoAutomatico.isAutomatismoDaDocumentiPassivi()) { %>
			<tr>
				<td colspan=2>
				      <b ALIGN="CENTER"><font size=2>Documenti passivi disponibili</font></b>
				      <% if ( mandatoAutomatico.isMandatiCreati() )
							bp.getDocumentiPassivi().writeHTMLTable(pageContext,"mandatoAutomatico",false,false,false,"100%","200px", true);
						 else
							bp.getDocumentiPassivi().writeHTMLTable(pageContext,null,false,false,false,"100%","200px", true);
					  %>
				</td>
			</tr>
	    <% } %>
		<tr>		
		<td colspan="2" align="center"><% JSPUtils.button(out,bp.encodePath("img/save24.gif"),bp.encodePath("img/save24.gif"),"Genera", "javascript:submitForm('doEmettiMandato')", bp.isEmettiMandatoButtonEnabled(),bp.getParentRoot().isBootstrap()); %></td>
		</tr>
		<tr>
		<td colspan="2"><b>Mandati Generati</b></td>
		</tr>
		
		<tr>
			<td colspan="2">		
		   <% bp.getMandati().writeHTMLTable(pageContext,"mandatoAutomatico",false,false,false,"100%","100px", true);%>
		   </td>
		</tr>
		<tr>		
		   <td colspan="2" align="center"><% JSPUtils.button(out,bp.encodePath("img/edit24.gif"),bp.encodePath("img/edit24.gif"),"Visualizza", "javascript:submitForm('doVisualizzaMandato')", bp.isVisualizzaMandatoButtonEnabled(),bp.getParentRoot().isBootstrap()); %></td>
		</tr>		
	</table>
</html>