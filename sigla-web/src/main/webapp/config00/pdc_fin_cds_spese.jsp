<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
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

	<% if (!bp.isFlNuovoPdg()){%>
		<div><CENTER><h3 class="text-primary">Gestione Capitolo Parte I</h3></CENTER></div>
	<% } else { %>
		<div><CENTER><h3 class="text-primary">Gestione Voce di Bilancio di Spesa</h3></CENTER></div>
	<% } %>
	<table class="Panel card p-3 mb-2">
	<tr>
	  <% bp.getController().writeFormField(out,"esercizio");%>
	</tr>
	<tr>
	  <% bp.getController().writeFormField(out,"cd_proprio_elemento");%>
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
	  <% bp.getController().writeFormField(out,"find_capoconto_fin");%>
	</tr>
	<% if (!bp.isFlNuovoPdg()){%>
		<tr>
		  <% bp.getController().writeFormField(out,"cd_elemento_voce");%>
		</tr>	
	<% } %>
	<tr>
	  <% bp.getController().writeFormField(out,"ds_elemento_voce");%>
	</tr>
	<tr>
	  <% bp.getController().writeFormField(out,"classificazione_spese");%>
	</tr>	
	<tr>
	  <% bp.getController().writeFormField(out,"find_classificazione_voci");%>
	</tr>				
	<% if (bp.isFlPrgPianoeco()) { %>
	<tr>
	  <% bp.getController().writeFormField(out,"voce_piano_economico");%>
	</tr>
	<% } %>			
	</table>
	
	<table class="Panel card p-3">
	<tr>
	  <% bp.getController().writeFormField(out,"fl_limite_ass_obblig");%>
	</tr>
	<tr>
	  <% bp.getController().writeFormField(out,"fl_voce_personale");%>
	</tr>
	<tr>
	  <% bp.getController().writeFormField(out,"fl_partita_giro");%>
	</tr>
	<tr>
	  <% bp.getController().writeFormField(out,"fl_recon");%>
	</tr>
	<tr>
	  <% bp.getController().writeFormField(out,"fl_inv_beni_patr");%>
	</tr>
	<tr>
	  <% bp.getController().writeFormField(out,"fl_inv_beni_comp");%>
	</tr>
	<tr>
	  <td><% bp.getController().writeFormLabel( out, "fl_check_terzo_siope"); %></td>	
	  <td><% bp.getController().writeFormInput( out,null,"fl_check_terzo_siope",(voce!=null?!voce.isGestoreOk(uc):false),null,null); %></td>
	</tr>
	<tr>
	  <% bp.getController().writeFormField(out,"fl_prelievo");%>
	</tr>
	<tr>
	  <% bp.getController().writeFormField(out,"fl_limite_spesa");%>
	</tr>
	<tr>
	  <% bp.getController().writeFormField(out,"fl_solo_competenza");%>
	</tr>
	<tr>
	  <% bp.getController().writeFormField(out,"fl_solo_residuo");%>
	</tr>
	<tr>
	  <% bp.getController().writeFormField(out,"fl_azzera_residui");%>
	</tr>
	<tr>
	  <% bp.getController().writeFormField(out,"fl_trovato");%>
	</tr>	
	<tr>
	  <% bp.getController().writeFormField(out,"fl_missioni");%>
	</tr>
	<tr>
	  <% bp.getController().writeFormField(out,"gg_deroga_obbl_comp_prg_scad");%>
	</tr>
	<tr>
	  <% bp.getController().writeFormField(out,"gg_deroga_obbl_res_prg_scad");%>
	</tr>	
	<tr>
	  <% bp.getController().writeFormField(out,"flComunicaPagamenti");%>
	</tr>
    <% if (voce.isVoceSpesa()) { %>
        <tr>
            <% bp.getController().writeFormField(out,"fl_limite_competenza");%>
        </tr>
        <tr>
            <% bp.getController().writeFormField(out,"blocco_impegni_natfin");%>
        </tr>
    <% } %>
	</table>
	<% if (!bp.isFlNuovoPdg()) ((EV_cds_spese_capitoloBulk) bp.getModel()).writeTable( out, ((EV_cds_spese_capitoloBulk) bp.getModel()).getAssociazioni() ); %>

<%	bp.closeFormWindow(pageContext); %>
</body>