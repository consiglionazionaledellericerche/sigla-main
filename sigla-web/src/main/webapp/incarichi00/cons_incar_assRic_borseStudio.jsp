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

	<div class="Group" style="width:100%">
	  <div class="Group">
	    <table>    	  
	  	  <tr>
			<td><% bp.getController().writeFormLabel(out,"tipologiaAssegni"); %></td>
			<td><% bp.getController().writeFormInput(out,"tipologiaAssegni"); %></td>
	  	  </tr>
	  	  <tr>
			<td><% bp.getController().writeFormLabel(out,"tipologiaBorsaDiStudio"); %></td>
			<td><% bp.getController().writeFormInput(out,"tipologiaBorsaDiStudio"); %></td>
	  	  </tr>
	  	  <tr>
			<td><% bp.getController().writeFormLabel(out,"tipologiaCollOcc"); %></td>
			<td><% bp.getController().writeFormInput(out,"tipologiaCollOcc"); %></td>
	  	  </tr>
	  	  <tr>
			<td><% bp.getController().writeFormLabel(out,"tipologiaCollProf"); %></td>
			<td><% bp.getController().writeFormInput(out,"tipologiaCollProf"); %></td>
	  	  </tr>
	  	  <tr>
			<td><% bp.getController().writeFormLabel(out,"tipologiaCococo"); %></td>
			<td><% bp.getController().writeFormInput(out,"tipologiaCococo"); %></td>
	  	  </tr>
	    </table>
	  </div>
		<table width="100%">
		    <tr>
				<td> <% bp.getController().writeFormLabel(out,"esercizioValidita");%></td>
				<td> <% bp.getController().writeFormInput(out,null,"esercizioValidita",false,null,"");%></td>
				<td> <% bp.getController().writeFormLabel(out,"dt_validita");%></td>
				<td> <% bp.getController().writeFormInput(out,null,"dt_validita",false,null,"");%></td>
  			<td><% bp.getController().writeFormLabel(out,"findUoForPrint"); %></td>
			<td>
				<% bp.getController().writeFormInput(out,null,"cdUoForPrint",(bulk!=null?!bulk.isUOForPrintEnabled():false),null,null); %>
				<% bp.getController().writeFormInput(out,"dsUoForPrint"); %>
				<% bp.getController().writeFormInput(out,null,"findUoForPrint",(bulk!=null?!bulk.isUOForPrintEnabled():false),null,null); %>
			</td>
			</tr> 
		</table>

		<div class="Group" style="width:100%">
			<table >
				<tr>
					<td>
						<% bp.getController().writeFormLabel(out,"cd_soggetto");%>
					</td>
					<td colspan="3">
						<% bp.getController().writeFormInput(out,"cd_soggetto");%>
						<% bp.getController().writeFormInput(out,"filtroSoggetto");%>
					</td>
				</tr>
				<tr>
					<td>
						<% bp.getController().writeFormLabel(out,"cd_precedente");%>
					</td>
					<td colspan="3">
						<% bp.getController().writeFormInput(out, "cd_precedente");%>
					</td>
				</tr>
				<tr>
					<% bp.getController().writeFormField(out,"codice_fiscale");%>
				</tr>	
				<tr>
					<td>
						<% bp.getController().writeFormLabel(out,"filtroCognome");%>
					</td>
					<td colspan="3">
						<% bp.getController().writeFormInput(out,"filtroCognome");%>
					</td>
				</tr>
				<tr>
					<td>
						<% bp.getController().writeFormLabel(out,"filtroNome");%>
					</td>
					<td colspan="3">
						<% bp.getController().writeFormInput(out,"filtroNome");%>
					</td>
				</tr>
			</table>	
		</div>
		<table>  
  <tr>
    <td>	
	  <div class="Group">
	    <table>    	  
	  	  <tr>
			<td><% bp.getController().writeFormLabel(out,"statoProvvisorio"); %></td>
			<td><% bp.getController().writeFormInput(out,"statoProvvisorio"); %></td>
	  	  </tr>
	  	  <tr>
			<td><% bp.getController().writeFormLabel(out,"statoDefinitivo"); %></td>
			<td><% bp.getController().writeFormInput(out,"statoDefinitivo"); %></td>
	  	  </tr>
	  	  <tr>
			<td><% bp.getController().writeFormLabel(out,"statoAnnullatoEliminato"); %></td>
			<td><% bp.getController().writeFormInput(out,"statoAnnullatoEliminato"); %></td>
	  	  </tr>
	  	  <tr>
			<td><% bp.getController().writeFormLabel(out,"statoInviato"); %></td>
			<td><% bp.getController().writeFormInput(out,"statoInviato"); %></td>
	  	  </tr>
	  	  <tr>
			<td><% bp.getController().writeFormLabel(out,"statoChiuso"); %></td>
			<td><% bp.getController().writeFormInput(out,"statoChiuso"); %></td>
	  	  </tr>
	    </table>
	  </div>
	  <div class="Group">
	    <table>    	  
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
    </td>
  </tr>	    
</table> 
	</div>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html> 
