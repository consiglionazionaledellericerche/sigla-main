<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.anagraf00.bp.*,
		it.cnr.contab.anagraf00.core.bulk.Stampa_previdenziale_dipendentiVBulk"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Stampa Iscrizione Previdenziale Dipendenti</title>
</head>
<body class="Form"> 

<%	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	Stampa_previdenziale_dipendentiVBulk bulk = (Stampa_previdenziale_dipendentiVBulk)bp.getModel();
	bp.openFormWindow(pageContext); %>

<table>
  <tr>
	<td>
		<% bp.getController().writeFormLabel(out,"cd_cds"); %>
		<% bp.getController().writeFormInput(out,"cd_cds"); %>
	</td>	
	<!-- <td>
		<% //bp.getController().writeFormLabel(out,"cd_uo_scrivania"); %> 
		<% //bp.getController().writeFormInput(out,"cd_uo_scrivania"); %>
		<% //bp.getController().writeFormInput(out,"ds_uo_scrivania"); %>
	</td> -->
  </tr>
</table>
<br>
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findUOForPrint"); %></td>
	<td>
		<% bp.getController().writeFormInput(out,null,"cdUOForPrint",(bulk!=null?!bulk.isUOForPrintEnabled():false),null,null); %>
		<% bp.getController().writeFormInput(out,"dsUOForPrint"); %>
		<% bp.getController().writeFormInput(out,null,"findUOForPrint",(bulk!=null?!bulk.isUOForPrintEnabled():false),null,null); %>
	</td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findMatricolaForPrint"); %></td>
	<td>
		<% bp.getController().writeFormInput(out,null,"matricola",(bulk!=null?!bulk.isMatricolaForPrintEnabled():false),null,null); %>
		<% bp.getController().writeFormInput(out,"nominativo"); %>
		<% bp.getController().writeFormInput(out,null,"findMatricolaForPrint",(bulk!=null?!bulk.isMatricolaForPrintEnabled():false),null,null); %>
	</td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findTerzoForPrint"); %></td>
	<td>
		<% bp.getController().writeFormInput(out,null,"cdTerzoForPrint",(bulk!=null?!bulk.isTerzoForPrintEnabled():false),null,null); %>
		<% bp.getController().writeFormInput(out,"dsTerzoForPrint"); %>
		<% bp.getController().writeFormInput(out,null,"findTerzoForPrint",(bulk!=null?!bulk.isTerzoForPrintEnabled():false),null,null); %>
	</td>
  </tr>
</table>

<% bp.closeFormWindow(pageContext); %>

</body>
</html>