<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page 
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.ordmag.anag00.*,
		it.cnr.contab.ordmag.richieste.*,
		it.cnr.contab.ordmag.richieste.bp.GenerazioneOrdineDaRichiesteBP"
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


<% bp.openFormWindow(pageContext); %>

	<table class="Panel">
		<tr>
			<td colspan=2>
				<% bp.writeHTMLTable(pageContext,"righeSet",false,false,false,"100%","400px", true); %>
			</td>
		</tr>
      <div>
	      <table>
		  <tr>
	         <td><% bp.writeFormLabel(out,"cdUnitaOperativa");%></td>
	         <td><% bp.writeFormInput(out,"default","cdUnitaOperativa",true, null, null);%></td>
	         <td><% bp.writeFormLabel(out,"esercizio");%></td>
	         <td><% bp.writeFormInput(out,"default","esercizio",true, null, null);%></td>
	         <td><% bp.writeFormLabel(out,"cdNumeratore");%></td>
	         <td><% bp.writeFormInput(out,"default","cdNumeratore",true, null, null);%></td>
	         <td><% bp.writeFormLabel(out,"numero");%></td>
	         <td><% bp.writeFormInput(out,"default","numero",true, null, null);%></td>
	         <td><% bp.writeFormLabel(out,"riga");%></td>
	         <td><% bp.writeFormInput(out,"default","riga",true, null, null);%></td>
	      </tr>  	      
		  <tr>
	         <td><% bp.writeFormField(out,"findBeneServizio");%></td>
	      </tr>  	
	      <table>
			  <tr>         
		         <td><% bp.writeFormLabel(out,"notaRiga");%></td>
	    	     <td><% bp.writeFormInput(out,"default","notaRiga",true, null, null);%></td>
	      	  </tr>            
	      </table>
		  <tr>
	         <td><% bp.writeFormField(out,"cdUnitaMisuraMinima");%></td>
	         <td><% bp.writeFormField(out,"findUnitaMisura");%></td>
	         <td><% bp.writeFormField(out,"coefConv");%></td>
	         <td><% bp.writeFormField(out,"quantitaRichiesta");%></td>
	         <td><% bp.writeFormField(out,"quantitaAutorizzata");%></td>
	      </tr>  	
	      <table>
			  <tr>         
		         <td><% bp.writeFormLabel(out,"notaUopDest");%></td>
	    	     <td><% bp.writeFormInput(out,"default","notaUopDest",true, null, null);%></td>
	      	  </tr>            
	      </table>
		  <tr>         
	         <td><% bp.writeFormLabel(out,"categoriaGruppo");%></td>
	         <td><% bp.writeFormInput(out,"default","categoriaGruppo",true, null, null);%></td>
	         <td><% bp.writeFormLabel(out,"descObbligazioneScadenzario");%></td>
	         <td><% bp.writeFormInput(out,"default","descObbligazioneScadenzario", true,null,null);%></td>
	      </tr>            
		  <tr>         
	         <td><% bp.writeFormLabel(out,"imImponibile");%></td>
	         <td><% bp.writeFormInput(out,"default","imImponibile",true, null, null);%></td>
	         <td><% bp.writeFormLabel(out,"imIva");%></td>
	         <td><% bp.writeFormInput(out,"default","imIva",true, null, null);%></td>
	         <td><% bp.writeFormLabel(out,"imIvaD");%></td>
	         <td><% bp.writeFormInput(out,"default","imIvaD",true, null, null);%></td>
	         <td><% bp.writeFormLabel(out,"imTotaleConsegna");%></td>
	         <td><% bp.writeFormInput(out,"default","imTotaleConsegna",true, null, null);%></td>
	      </tr>
	      </table>
	      <table>
			  <tr>         
		         <td><% bp.writeFormLabel(out,"notaRigaEstesa");%></td>
	    	     <td><% bp.writeFormInput(out,"default","notaRigaEstesa",true, null, null);%></td>
	      	  </tr>            
	      </table>
      </div>
		<tr><td>
		<% bp.writeHTMLNavigator(out); %>
		</td></tr>
	</table>


<%bp.closeFormWindow(pageContext); %>
</body>