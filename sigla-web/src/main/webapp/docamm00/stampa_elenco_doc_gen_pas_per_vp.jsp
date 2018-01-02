<%@ page 
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
<title>Stampa elenco docum. gen. passivi per voce del piano</title>
</head>
<body class="Form"> 

<%	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	Stampa_docamm_per_voce_del_pianoVBulk bulk = (Stampa_docamm_per_voce_del_pianoVBulk)bp.getModel();
	bp.openFormWindow(pageContext); %>

<div class="Group card">
<table class="w-50">
  <tr>
	<% bp.getController().writeFormField(out,"esercizio"); %>
	<% bp.getController().writeFormField(out,"cd_cds"); %>
  </tr>
</table>
</div>
<div class="Group card">
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findUOForPrint"); %></td>
	<td><% bp.getController().writeFormInput(out,null,"findUOForPrint",(bulk!=null?!bulk.isUOForPrintEnabled():false),null,null); %></td>
  </tr>
  <tr>
    <td><% bp.getController().writeFormLabel(out,"findVoceDPForPrint"); %></td>
	<td>
		<% bp.getController().writeFormInput(out,"cd_elemento_voce"); %>
		<% bp.getController().writeFormInput(out,"ds_elemento_voce"); %>
		<% bp.getController().writeFormInput(out,"findVoceDPForPrint"); %>
	</td>
  </tr>
</table>
</div>
<% bp.closeFormWindow(pageContext); %>

</body>
</html>