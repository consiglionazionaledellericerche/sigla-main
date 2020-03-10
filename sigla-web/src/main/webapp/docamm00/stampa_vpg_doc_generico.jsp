<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	it.cnr.jada.action.*,
	it.cnr.contab.docamm00.docs.bulk.*,
	java.util.*,
	it.cnr.jada.util.action.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Stampa documento generico</title>
</head>
<body class="Form"> 

<%	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	Stampa_vpg_doc_genericoBulk bulk = (Stampa_vpg_doc_genericoBulk)bp.getModel();
	bp.openFormWindow(pageContext); %>
<div class="Group card">
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"esercizio"); %></td>
	<td colspan="3"><% bp.getController().writeFormInput(out,"esercizio"); %></td>
  </tr>
  <tr>
	<% bp.getController().writeFormField(out,"cd_cds"); %>
	<% bp.getController().writeFormField(out,"cd_unita_organizzativa"); %>
  </tr>
</table>
</div>
<div class="Group card">
<table class="w-50">
  <tr>
	<% bp.getController().writeFormField(out,"pgInizio"); %></td>
	<% bp.getController().writeFormField(out,"pgFine"); %>
  </tr>
  <tr>
	<% bp.getController().writeFormField(out,"dataInizio"); %>
	<% bp.getController().writeFormField(out,"dataFine"); %>
  </tr>
</table>
</div>
<div class="Group card">
<table class="w-75">
  <tr>
	<td><% bp.getController().writeFormLabel(out,"tipo_doc_for_search"); %></td>
	<td><% bp.getController().writeFormInput(out, null,"tipo_doc_for_search",false,null,"onChange=\"submitForm('doOnTipoDocumentoChange')\""); %></td>	
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findTerzo"); %></td>
	<td><% bp.getController().writeFormInput(out,"findTerzo"); %></td>
  </tr>
  <%	if (Optional.ofNullable(bulk).flatMap(el->Optional.ofNullable(el.getTipo_documento())).flatMap(el->Optional.ofNullable(el.getCd_tipo_documento_amm()))
  .map(el->el.equals(Stampa_vpg_doc_genericoBulk.GENERICO_E)).orElse(Boolean.FALSE)) { %>
  <tr>
			<% bp.getController().writeFormField(out,"findTipoDocumentoGenerico"); %>
  </tr>
  <% } %>
</table>
</div>
<% bp.closeFormWindow(pageContext); %>

</body>
</html>