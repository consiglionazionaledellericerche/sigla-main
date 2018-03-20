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
		it.cnr.contab.ordmag.anag00.*,
		it.cnr.contab.ordmag.richieste.*,
		it.cnr.contab.ordmag.ordini.bp.GenerazioneOrdineDaRichiesteBP"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<script language="JavaScript" src="scripts/util.js"></script>
<% JSPUtils.printBaseUrl(pageContext);%>
</head>
<script language="javascript" src="scripts/css.js"></script>

<% GenerazioneOrdineDaRichiesteBP bp = (GenerazioneOrdineDaRichiesteBP)BusinessProcess.getBusinessProcess(request); %>

	<title>Generazione Ordine da Richieste</title>

<body class="Form">

<%  bp.openFormWindow(pageContext); %>
	<div class="Group card p-2 mb-2">
		<table cellpadding="2">
			<tr><% bp.getController().writeFormField(out, "findUnitaOperativaOrd");%></tr>
			<tr><% bp.getController().writeFormField(out, "findNumerazioneOrd");%></tr>
		<tr>
			<td colspan=2 align="center">
				<% JSPUtils.button(out,bp.encodePath("img/find24.gif"),bp.encodePath("Ricerca Richieste"), "javascript:submitForm('doCercaRichieste')",null, bp.getParentRoot().isBootstrap()); %>
			</td>
		</tr>
		</table>
	</div>

	<table class="Panel">
		<tr>
			<td colspan=2>
			    <% bp.getRichieste().writeHTMLTable(pageContext,"default",false,false,false,"100%","350px", true); %>
			</td>
		</tr>
      <div>
	      <table>
		  <tr>
	         <td><% bp.getRichieste().writeFormField(out,"cdUnitaOperativa");%></td>
	         <td><% bp.getRichieste().writeFormField(out,"esercizio");%></td>
	         <td><% bp.getRichieste().writeFormField(out,"cdNumeratore");%></td>
	         <td><% bp.getRichieste().writeFormField(out,"numero");%></td>
	         <td><% bp.getRichieste().writeFormField(out,"riga");%></td>
	      </tr>  	      
	      </table>
	      <table>
		  <tr>
	         <td><% bp.getRichieste().writeFormField(out,"findBeneServizio");%></td>
	      </tr>  	
	      </table>
	      <table>
			  <tr>         
		         <td><% bp.getRichieste().writeFormLabel(out,"notaRiga");%></td>
	    	     <td><% bp.getRichieste().writeFormInput(out,"default","notaRiga",true, null, null);%></td>
	      	  </tr>            
	      </table>
	      <table>
		  <tr>
	         <td><% bp.getRichieste().writeFormField(out,"cdUnitaMisuraMinima");%></td>
	         <td><% bp.getRichieste().writeFormField(out,"findUnitaMisura");%></td>
	         <td><% bp.getRichieste().writeFormField(out,"coefConv");%></td>
	         <td><% bp.getRichieste().writeFormField(out,"quantitaRichiesta");%></td>
	         <td><% bp.getRichieste().writeFormField(out,"quantitaAutorizzata");%></td>
	      </tr>  	
	      </table>
	      <table>
			  <tr>         
		         <td><% bp.getRichieste().writeFormLabel(out,"notaUopDest");%></td>
	    	     <td><% bp.getRichieste().writeFormInput(out,"default","notaUopDest",true, null, null);%></td>
	      	  </tr>            
	      </table>
	      <table>
		  <tr>         
	         <td><% bp.getRichieste().writeFormLabel(out,"categoriaGruppo");%></td>
	         <td><% bp.getRichieste().writeFormInput(out,"default","categoriaGruppo",true, null, null);%></td>
	         <td><% bp.getRichieste().writeFormLabel(out,"descObbligazioneScadenzario");%></td>
	         <td><% bp.getRichieste().writeFormInput(out,"default","descObbligazioneScadenzario", true,null,null);%></td>
	      </tr>            
	      </table>
	      <table>
		  <tr>         
	         <td><% bp.getRichieste().writeFormLabel(out,"imImponibile");%></td>
	         <td><% bp.getRichieste().writeFormInput(out,"default","imImponibile",true, null, null);%></td>
	         <td><% bp.getRichieste().writeFormLabel(out,"imIva");%></td>
	         <td><% bp.getRichieste().writeFormInput(out,"default","imIva",true, null, null);%></td>
	         <td><% bp.getRichieste().writeFormLabel(out,"imIvaD");%></td>
	         <td><% bp.getRichieste().writeFormInput(out,"default","imIvaD",true, null, null);%></td>
	         <td><% bp.getRichieste().writeFormLabel(out,"imTotaleConsegna");%></td>
	         <td><% bp.getRichieste().writeFormInput(out,"default","imTotaleConsegna",true, null, null);%></td>
	      </tr>
	      </table>
	      <table>
			  <tr>         
		         <td><% bp.getRichieste().writeFormLabel(out,"notaRigaEstesa");%></td>
	    	     <td><% bp.getRichieste().writeFormInput(out,"default","notaRigaEstesa",true, null, null);%></td>
	      	  </tr>            
	      </table>
      </div>
	</table>


<%bp.closeFormWindow(pageContext); %>
</body>