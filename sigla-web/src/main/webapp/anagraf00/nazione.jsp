<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.anagraf00.bp.*"
%>

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Nazione</title>
</head>
<body class="Form">

<%	CRUDNazioneBP bp = (CRUDNazioneBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext); %>

<table class="Panel card p-2">
  <tr>
	<td><% bp.getController().writeFormLabel(out,"pg_nazione"); %></td>
	<td><% bp.getController().writeFormInput(out,"pg_nazione"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ds_nazione"); %></td>
	<td><% bp.getController().writeFormInput(out,"ds_nazione"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"nazionalita"); %></td>
	<td><% bp.getController().writeFormInput(out,"nazionalita"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ti_nazione"); %></td>
	<td><% bp.getController().writeFormInput(out,null,"ti_nazione",false,null,"onClick=\"submitForm('doOnTipoNazioneChange')\""); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_divisa"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_divisa"); %>
		<% bp.getController().writeFormInput(out,"ds_divisa"); %>
		<% bp.getController().writeFormInput(out,"find_divisa"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_divisa_per_missione"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_divisa_per_missione"); %>
		<% bp.getController().writeFormInput(out,"ds_divisa_per_missione"); %>
		<% bp.getController().writeFormInput(out,"find_divisa_per_missione"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_catastale"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_catastale"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_nazione"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_nazione"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_nazione_770"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_nazione_770"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_iso"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_iso"); %></td>
  </tr>
    <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_area_estera"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_area_estera"); %></td>
  </tr>
    </tr>
      <tr>
  	<td><% bp.getController().writeFormLabel(out,"fl_sepa"); %></td>
  	<td><% bp.getController().writeFormInput(out,"fl_sepa"); %></td>
    </tr>
</table>

<%	bp.closeFormWindow(pageContext); %>

</body>
</html>