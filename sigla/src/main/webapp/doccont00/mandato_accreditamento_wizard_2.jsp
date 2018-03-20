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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
	<head>
		<script language="JavaScript" src="scripts/util.js"></script>
		<% JSPUtils.printBaseUrl(pageContext);%>
	</head>
	<script language="javascript" src="scripts/css.js"></script>
	<title>Mandato di accreditamento CNR -> Cds</title>
	<body class="Form">

	<%  
			MandatoAccreditamentoWizardBP bp = (MandatoAccreditamentoWizardBP)BusinessProcess.getBusinessProcess(request);
			it.cnr.contab.doccont00.core.bulk.MandatoAccreditamentoWizardBulk mandatoAccreditamento = (it.cnr.contab.doccont00.core.bulk.MandatoAccreditamentoWizardBulk)bp.getModel();
			bp.openFormWindow(pageContext); 
	%>

	<div class="Group">		
	<table class="Panel">
		<tr>
			<td><% bp.getController().writeFormLabel( out, "esercizio"); %></td>
	   		<td><% bp.getController().writeFormInput( out, "esercizio"); %></td>
			<td colspan="2"><% bp.getController().writeFormLabel( out, "dt_emissione"); %>
				<% bp.getController().writeFormInput( out, "dt_emissione"); %></td>	   		
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel( out, "terzo_cd_terzo"); %></td>
			<td ><% bp.getController().writeFormInput( out, "terzo_cd_terzo"); %></td>
			<td ><% bp.getController().writeFormInput( out, "terzo_ds_terzo"); %></td>
			<td ><% JSPUtils.button(out,bp.encodePath("img/zoom16.gif"), "Visualizza entrate", "javascript:submitForm('doVisualizzaEntrate')", bp.getParentRoot().isBootstrap() ); %>
				 <% JSPUtils.button(out,bp.encodePath("img/zoom16.gif"), "Visualizza spese", "javascript:submitForm('doVisualizzaSpese')", bp.getParentRoot().isBootstrap()); %>							
			</td>
		<tr>			    
			<td><% bp.getController().writeFormLabel( out, "terzo_tipo_bollo"); %></td>
			<td colspan= "3"><% bp.getController().writeFormInput( out,"default", "terzo_tipo_bollo", false, null,"onchange=\"submitForm('doCambiaTipoBollo')\"" ); %>
			    <% bp.getController().writeFormInput( out, "terzo_im_tipo_bollo"); %></td>
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel( out, "cd_modalita_pag"); %></td>
		   <td><% bp.getController().writeFormInput( out,"default", "cd_modalita_pag", false, null,"onchange=\"submitForm('doCambiaModalitaPagamento')\"" ); %></td>			
			<td colspan= "2"><% bp.getController().writeFormLabel( out, "banca"); %>
			  <% bp.getController().writeFormInput( out, "banca"); %></td>
		</tr>
		
		<tr>
			<td><% bp.getController().writeFormLabel( out, "im_mandato"); %></td>
			<td colspan="3"><% bp.getController().writeFormInput( out, "im_mandato"); %></td>
		</tr>
	</table>	
	</div>
	<table class="Panel">	
		<tr>
			<td><b ALIGN="CENTER">Voci Bilancio CNR</b></td>
			<td align="right"><% bp.getController().writeFormInput(out,null,"fl_imputazione_manuale",!bp.isFlCalcoloAutomaticoCheckboxEnabled(),null,"onclick=\"submitForm('doCambiaFl_imputazione_manuale')\""); %>
	 	   					  <% bp.getController().writeFormLabel( out, "fl_imputazione_manuale"); %></td>
		</tr>
		<tr>
			<td colspan="2">
				<% if ( mandatoAccreditamento.getFl_imputazione_manuale() )
						bp.getImpegni().writeHTMLTable(pageContext,"impegni_man",false,false,false,"100%","100px", false); 
				   else
						bp.getImpegni().writeHTMLTable(pageContext,"impegni_auto",false,false,false,"100%","100px", false);
				%>	
			</td>
		</tr>
		<tr>		
		<td colspan="2" align="center"><% JSPUtils.button(out,bp.encodePath("img/save24.gif"),bp.encodePath("img/save24.gif"),"Genera", "javascript:submitForm('doEmettiMandato')", bp.isEmettiMandatoButtonEnabled(),bp.getParentRoot().isBootstrap()); %></td>
		</tr>
		<tr>
		<td colspan="2"><b>Mandati Generati</b></td>
		</tr>
		
		<tr>
			<td colspan="2">		
		   <% bp.getMandati().writeHTMLTable(pageContext,null,false,false,false,"100%","100px", true);%>
		   </td>
		</tr>
		<tr>		
		   <td colspan="2" align="center"><% JSPUtils.button(out,bp.encodePath("img/edit24.gif"),bp.encodePath("img/edit24.gif"),"Visualizza", "javascript:submitForm('doVisualizzaMandato')", bp.isVisualizzaMandatoButtonEnabled(),bp.getParentRoot().isBootstrap()); %></td>
		</tr>		
	</table>
	<%	bp.closeFormWindow(pageContext); %>
</body>