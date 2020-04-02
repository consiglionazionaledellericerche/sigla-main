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
	        it.cnr.contab.config00.bp.*,
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
<title>Piano dei conti CNR - Gestione entrate</title>
<body class="Form">

<% CRUDConfigPdcCNREntrateCapitoloBP bp = (CRUDConfigPdcCNREntrateCapitoloBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); 
	 UserContext uc = HttpActionContext.getUserContext(session);
	 Elemento_voceBulk voce = (Elemento_voceBulk)bp.getModel();
	 %>

	<% if (!bp.isFlNuovoPdg()){%>
		<div><CENTER><h3 class="text-primary">Gestione Capitolo</h3></CENTER></div>
	<% } else { %>
		<div><CENTER><h3 class="text-primary">Gestione Voce di Bilancio di Entrata</h3></CENTER></div>
	<% } %>
	<table class="Panel card p-3 mb-2">
	<tr>
		<% bp.getController().writeFormField( out, "esercizio"); %>
	</tr>
	<% if (!bp.isFlNuovoPdg()){%>
		<tr>
			<% bp.getController().writeFormField( out, "cd_proprio_elemento"); %>
		</tr>
		<tr>
		<td><span class="FormLabel">Categoria</span></td>	
		<td>
				<% bp.getController().writeFormInputByStatus( out, "cd_elemento_padre"); %>
				<% bp.getController().writeFormInput( out, "ds_elemento_padre"); %>
				<% bp.getController().writeFormInputByStatus( out, "find_elemento_padre"); %>				
		</td>
		</tr>
		<tr>
		<td><% bp.getController().writeFormLabel( out, "cd_titolo"); %></td>	
		<td>
		    <% bp.getController().writeFormInput( out, "cd_titolo"); %>
		    <% bp.getController().writeFormInput( out, "ds_titolo"); %>
		</td>
		</tr>
		<tr>
		<td><% bp.getController().writeFormLabel( out, "cd_elemento_voce"); %></td>	
		<td><% bp.getController().writeFormInput( out, "cd_elemento_voce"); %>
			<% bp.getController().writeFormLabel( out, "fl_partita_giro"); %>
			<% bp.getController().writeFormInput( out,null,"fl_partita_giro",true,null,null); %>
			<% bp.getController().writeFormLabel( out, "fl_voce_sac"); %>
			<% bp.getController().writeFormInput( out, "fl_voce_sac"); %></td>
		</tr>	
	<% } else {%>
		<tr>
			<% bp.getController().writeFormField( out, "cd_proprio_elemento"); %>
		</tr>
	<% } %>
	<tr>
		<% bp.getController().writeFormField( out, "ds_elemento_voce"); %>
	</tr>
	<tr>
	  <% bp.getController().writeFormField(out,"classificazione_entrate");%>
	</tr>				
	<tr>
	  <% bp.getController().writeFormField(out,"find_classificazione_voci");%>
	</tr>				
	</table>

	<table class="Panel card p-3">
	<tr>
	  <% bp.getController().writeFormField( out, "fl_partita_giro"); %>	
	</tr>
	<tr>
	  <% bp.getController().writeFormField( out, "fl_voce_sac"); %>	
	</tr>
	<tr>
	  <% bp.getController().writeFormField( out, "fl_recon"); %>	
	</tr>
	<tr>
	  <% bp.getController().writeFormField( out, "fl_inv_beni_patr"); %>	
	</tr>
	<tr>
	  <td><% bp.getController().writeFormLabel( out, "fl_check_terzo_siope"); %></td>	
	  <td><% bp.getController().writeFormInput( out,null,"fl_check_terzo_siope",(voce!=null?!voce.isGestoreOk(uc):false),null,null); %></td>
	</tr>
	<tr>
	  <% bp.getController().writeFormField( out, "fl_soggetto_prelievo"); %>
	</tr>
	<tr>
	  <% bp.getController().writeFormField( out, "perc_prelievo_pdgp_entrate"); %>	
	</tr>
	<tr>
	  <% bp.getController().writeFormField( out, "fl_solo_competenza"); %>	
	</tr>
	<tr>
	  <% bp.getController().writeFormField( out, "fl_solo_residuo"); %>	
	</tr>
	<tr>
	  <% bp.getController().writeFormField( out, "fl_azzera_residui"); %>	
	</tr>
	<tr>
	  <% bp.getController().writeFormField( out, "fl_trovato"); %>	
	</tr>	
	</table>
<%	bp.closeFormWindow(pageContext); %>
</body>