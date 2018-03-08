<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	it.cnr.jada.action.*,
	java.util.*,
	it.cnr.jada.util.action.*,
	it.cnr.contab.docamm00.docs.bulk.Stampa_docamm_per_voce_del_pianoVBulk"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Stampa elenco fatture per voce del piano</title>
</head>
<body class="Form"> 

<%	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	Stampa_docamm_per_voce_del_pianoVBulk bulk = (Stampa_docamm_per_voce_del_pianoVBulk)bp.getModel();
	bp.openFormWindow(pageContext); %>

<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"esercizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"esercizio"); %></td>
	<td></td>
	<td><% bp.getController().writeFormLabel(out,"cd_cds"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_cds"); %></td>
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
    <td><% bp.getController().writeFormLabel(out,"findVoceDPForPrint"); %></td>
	<td>
		<% bp.getController().writeFormInput(out,"cd_elemento_voce"); %>
		<% bp.getController().writeFormInput(out,"ds_elemento_voce"); %>
		<% bp.getController().writeFormInput(out,"findVoceDPForPrint"); %>
	</td>
  </tr>
  <tr>
    <td><% bp.getController().writeFormLabel(out,"findTerzo"); %></td>
	<td>
		<% bp.getController().writeFormInput(out,"cd_terzo"); %>
		<% bp.getController().writeFormInput(out,"ds_terzo"); %>
		<% bp.getController().writeFormInput(out,"findTerzo"); %>
	</td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"stato"); %></td>
	<td><% bp.getController().writeFormInput(out,"stato"); %></td>
  </tr>
</table>

<% bp.closeFormWindow(pageContext); %>

</body>
</html>