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

<% CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); 
	 UserContext uc = HttpActionContext.getUserContext(session);
	 Elemento_voceBulk voce = (Elemento_voceBulk)bp.getModel();
	 %>

	<table class="Panel">
<tr>
	<td colspan=2><CENTER><h3>Gestione Capitolo</h3></CENTER></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "esercizio"); %></td>
	<td><% bp.getController().writeFormInputByStatus( out, "esercizio"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "cd_proprio_elemento"); %></td>	
	<td><% bp.getController().writeFormInputByStatus( out, "cd_proprio_elemento"); %></td>
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
		<% bp.getController().writeFormInput( out, "fl_partita_giro"); %>
		<% bp.getController().writeFormLabel( out, "fl_voce_sac"); %>
		<% bp.getController().writeFormInput( out, "fl_voce_sac"); %></td>
	</tr>	
	<tr>
	<td><% bp.getController().writeFormLabel( out, "ds_elemento_voce"); %></td>	
	<td><% bp.getController().writeFormInput( out, "ds_elemento_voce"); %></td>
	</tr>
	<tr>
	  <td><% bp.getController().writeFormLabel(out,"classificazione_entrate");%></td>
	  <td> 
	   <TABLE cellspacing="0">
	   <tr> 
	      <td><% bp.getController().writeFormInput(out,"esercizio_cla_e");%></td> 
	  	  <td><% bp.getController().writeFormInput(out,"cod_cla_e");%></td> 
	      <td><% bp.getController().writeFormInput(out,"classificazione_entrate");%></td> 
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
	<table>
	<tr>
	  <td><% bp.getController().writeFormLabel( out, "fl_recon"); %></td>	
	  <td><% bp.getController().writeFormInput( out, "fl_recon"); %></td>
	</tr>
	</table>
	<table>
	<tr>
	  <td><% bp.getController().writeFormLabel( out, "fl_inv_beni_patr"); %></td>	
	  <td><% bp.getController().writeFormInput( out, "fl_inv_beni_patr"); %></td>
	</tr>
	<tr>
	  <td><% bp.getController().writeFormLabel( out, "fl_check_terzo_siope"); %></td>	
	  <td><% bp.getController().writeFormInput( out,null,"fl_check_terzo_siope",(voce!=null?!voce.isGestoreOk(uc):false),null,null); %></td>
	</tr>
	<tr>
	  <td><% bp.getController().writeFormLabel( out, "fl_soggetto_prelievo"); %></td>	
	  <td><% bp.getController().writeFormInput( out, "fl_soggetto_prelievo"); %></td>
	</tr>
	<tr>
	  <td><% bp.getController().writeFormLabel( out, "perc_prelievo_pdgp_entrate"); %></td>	
	  <td><% bp.getController().writeFormInput( out, "perc_prelievo_pdgp_entrate"); %></td>
	</tr>
	
	
	</table>

<%	bp.closeFormWindow(pageContext); %>
</body>