<%@ page 
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.action.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.incarichi00.bulk.*,
		it.cnr.contab.incarichi00.bp.*" %>


<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Incarichi, Assegni di Ricerca, Borse di Studio</title>
</head>
<body class="Form">

<% 
	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	ConsIncarAssRicBorseStBP bp1 = (ConsIncarAssRicBorseStBP)BusinessProcess.getBusinessProcess(request);
    VIncarichiAssRicBorseStBulk bulk = (VIncarichiAssRicBorseStBulk)bp1.getModel();
	bp.openFormWindow(pageContext);
	boolean isFieldReadOnly = true;
%>

	<div class="Group card" style="width:100%">
	  <div class="Group card border-primary p-2 mb-2">
	    <table>    	  
	  	  <tr><% bp.getController().writeFormField(out,"tipologiaAssegni"); %></tr>
	  	  <tr><% bp.getController().writeFormField(out,"tipologiaBorsaDiStudio"); %></tr>
	  	  <tr><% bp.getController().writeFormField(out,"tipologiaCollOcc"); %></tr>
	  	  <tr><% bp.getController().writeFormField(out,"tipologiaCollProf"); %></tr>
	  	  <tr><% bp.getController().writeFormField(out,"tipologiaCococo"); %></tr>
	    </table>
	  </div>
	  
	  <div class="p-2 mb-2">
		<table width="100%">
		    <tr>
				<% bp.getController().writeFormField(out,"esercizioValidita");%>
				<% bp.getController().writeFormField(out,"dt_validita");%>
	  			<td><% bp.getController().writeFormLabel(out,"findUoForPrint"); %></td>
				<td>
					<% bp.getController().writeFormInput(out,null,"cdUoForPrint",(bulk!=null?!bulk.isUOForPrintEnabled():false),null,null); %>
					<% bp.getController().writeFormInput(out,"dsUoForPrint"); %>
					<% bp.getController().writeFormInput(out,null,"findUoForPrint",(bulk!=null?!bulk.isUOForPrintEnabled():false),null,null); %>
				</td>
			</tr> 
		</table>
	  </div>
	  
	  <div class="Group card border-primary p-2 mb-2" style="width:100%">
		<table class="w-100">
			<tr><% bp.getController().writeFormField(out,"filtroSoggetto");%></tr>
			<tr><% bp.getController().writeFormField(out,"cd_precedente");%></tr>
			<tr><% bp.getController().writeFormField(out,"codice_fiscale");%></tr>
			<tr><% bp.getController().writeFormField(out,"filtroCognome");%></tr>
			<tr><% bp.getController().writeFormField(out,"filtroNome");%></tr>
		</table>	
	  </div>

	  <div class="Group card border-primary p-2 mb-2">
	    <table>    	  
	  	  <tr><% bp.getController().writeFormField(out,"statoProvvisorio"); %></tr>
	  	  <tr><% bp.getController().writeFormField(out,"statoDefinitivo"); %></tr>
	  	  <tr><% bp.getController().writeFormField(out,"statoAnnullatoEliminato"); %></tr>
	  	  <tr><% bp.getController().writeFormField(out,"statoInviato"); %></tr>
	  	  <tr><% bp.getController().writeFormField(out,"statoChiuso"); %></tr>
	    </table>
	  </div>

	  <div class="Group card border-primary p-2 mb-2">
	    <table class="w-50">    	  
	      <tr>
		    <td><% bp.getController().writeFormInput(out,"seleziona"); %></td>
	  	  </tr>	
	      <tr>
			<td>
				<% bp.getController().writeFormLabel(out,"fonti");%>
			</td>
			<td>
				<% bp.getController().writeFormInput(out,null,"fonti",false,null,"");%>
			</td>
		  </tr>
	    </table>
	  </div>
	</div>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html> 
