<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page 
	import="it.cnr.jada.util.jsp.*,
	        it.cnr.jada.action.*,
	        java.util.*,
	        it.cnr.jada.util.action.*,
	        it.cnr.contab.config00.bp.CRUDConfigPdcCDSSpeseBP,
	        it.cnr.contab.config00.pdcfin.bulk.EV_cds_spese_capitoloBulk,
	        it.cnr.contab.config00.pdcfin.bulk.*,
	        it.cnr.jada.UserContext"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script> 
</head>
<title>Piano dei conti CDS - Gestione spese</title>
<body class="Form">

	
<% CRUDConfigPdcCDSSpeseBP bp = (CRUDConfigPdcCDSSpeseBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); 
	 UserContext uc = HttpActionContext.getUserContext(session);
	 Elemento_voceBulk voce = (Elemento_voceBulk)bp.getModel();%>

	<table class="Panel card border-primary p-3 mb-2">
	<% if (!bp.isFlNuovoPdg()){%>
		<tr>
		<td colspan=4><CENTER><h3 class="text-primary">Gestione Capitolo Parte I</h3></CENTER></td>
		</tr>
	<% } else { %>
		<tr>
		<td colspan=4><CENTER><h3 class="text-primary">Gestione Voce di Bilancio di Spesa</h3></CENTER></td>
		</tr>
	<% } %>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "esercizio"); %></td>
	<td colspan=3><% bp.getController().writeFormInputByStatus( out, "esercizio"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "cd_proprio_elemento"); %></td>	
	<td colspan=3><% bp.getController().writeFormInputByStatus( out, "cd_proprio_elemento"); %></td>
	</tr>
	<% if (!bp.isFlNuovoPdg()){%>
		<tr>
		<td><span class="FormLabel">Titolo</span></td>	
		<td colspan=3>
				<% bp.getController().writeFormInputByStatus( out, "cd_elemento_padre"); %>
				<% bp.getController().writeFormInput( out, "ds_elemento_padre"); %>
				<% bp.getController().writeFormInputByStatus( out, "find_elemento_padre"); %>				
		</td>
		</tr>
	<% } %>
	<tr>
	<td><span class="FormLabel">Categoria Economica/Finanziaria</span></td>	
	<td colspan=3>
			<% bp.getController().writeFormInput( out, "cd_capoconto_fin"); %>
			<% bp.getController().writeFormInput( out, "ds_capoconto_fin"); %>
			<% bp.getController().writeFormInput( out, "find_capoconto_fin"); %>				
	</td>
	</tr>
	<% if (!bp.isFlNuovoPdg()){%>
		<tr>
		<td><% bp.getController().writeFormLabel( out, "cd_elemento_voce"); %></td>	
		<td colspan=3><% bp.getController().writeFormInput( out, "cd_elemento_voce"); %></td>
		</tr>	
	<% } %>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "ds_elemento_voce"); %></td>	
	<td colspan=3><% bp.getController().writeFormInput( out, "ds_elemento_voce"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "fl_limite_ass_obblig"); %></td>	
	<td colspan=3><% bp.getController().writeFormInput( out, "fl_limite_ass_obblig"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "fl_voce_personale"); %></td>	
	<td colspan=3><% bp.getController().writeFormInput( out, "fl_voce_personale"); %></td>
	</tr>
	<tr>
	  <td><% bp.getController().writeFormLabel(out,"classificazione_spese");%></td>
	  <td>
	  <TABLE cellspacing="0">
	   <tr>
	      <TD><% bp.getController().writeFormInput(out,"esercizio_cla_s");%></TD>
	  	  <TD><% bp.getController().writeFormInput(out,"cod_cla_s");%></TD>
	      <TD><% bp.getController().writeFormInput(out,"classificazione_spese");%></TD>
	   </tr>
	  </TABLE>
	  </td>
	</tr>	
	<tr>
	  <td><% bp.getController().writeFormLabel(out,"find_classificazione_voci");%></td>
	  <td> 
	   <TABLE cellspacing="0">
	   <tr> 
	      <td><% bp.getController().writeFormInput(out,"find_classificazione_voci");%></td> 
	  </tr>
	  </TABLE> 
	  </td> 	  
	</tr>				
	</table>
	<table class="Panel card border-info p-3">
	<tr>
	  <td><% bp.getController().writeFormLabel( out, "fl_recon"); %></td>	
	  <td><% bp.getController().writeFormInput( out, "fl_recon"); %></td>
	</tr>
	<tr>
	  <td><% bp.getController().writeFormLabel( out, "fl_inv_beni_patr"); %></td>	
	  <td><% bp.getController().writeFormInput( out, "fl_inv_beni_patr"); %></td>
	</tr>
	<tr>
	  <td><% bp.getController().writeFormLabel( out, "fl_check_terzo_siope"); %></td>	
	  <td><% bp.getController().writeFormInput( out,null,"fl_check_terzo_siope",(voce!=null?!voce.isGestoreOk(uc):false),null,null); %></td>
	</tr>
	<tr>
	  <td><% bp.getController().writeFormLabel( out, "fl_prelievo"); %></td>	
	  <td><% bp.getController().writeFormInput( out, "fl_prelievo"); %></td>
	</tr>
	<tr>
	  <td><% bp.getController().writeFormLabel( out, "fl_limite_spesa"); %></td>	
	  <td><% bp.getController().writeFormInput( out, "fl_limite_spesa"); %></td>
	</tr>
	<tr>
	  <td><% bp.getController().writeFormLabel( out, "fl_solo_residuo"); %></td>	
	  <td><% bp.getController().writeFormInput( out, "fl_solo_residuo"); %></td>
	  <td><% bp.getController().writeFormLabel( out, "fl_azzera_residui"); %></td>	
	  <td><% bp.getController().writeFormInput( out, "fl_azzera_residui"); %></td>
	  <td><% bp.getController().writeFormLabel( out, "fl_solo_competenza"); %></td>	
	  <td><% bp.getController().writeFormInput( out, "fl_solo_competenza"); %></td>
	</tr>
	<tr>
	  <td><% bp.getController().writeFormLabel( out, "fl_trovato"); %></td>
	  <td><% bp.getController().writeFormInput( out, "fl_trovato"); %></td>
	</tr>	
	<tr>
	  <td><% bp.getController().writeFormLabel( out, "fl_missioni"); %></td>
	  <td><% bp.getController().writeFormInput( out, "fl_missioni"); %></td>
	</tr>	
	</table>		
	<% if (!bp.isFlNuovoPdg()) ((EV_cds_spese_capitoloBulk) bp.getModel()).writeTable( out, ((EV_cds_spese_capitoloBulk) bp.getModel()).getAssociazioni() ); %>

<%	bp.closeFormWindow(pageContext); %>
</body>