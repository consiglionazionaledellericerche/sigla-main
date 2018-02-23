<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	it.cnr.jada.action.*,
	java.util.*,
	it.cnr.jada.util.action.*,
	it.cnr.contab.doccont00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Associazione manuale mandato/reversali</title>
</head>

<body class="Form">

<%	CRUDAssManualeMandatoReversaleBP bp = (CRUDAssManualeMandatoReversaleBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext); %>

<div class="Panel" style="width:100%">
<table>
  <tr>
	<td><% bp.getController().writeFormLabel( out, "pg_mandato"); %></td>
	<td><% bp.getController().writeFormInput( out,null,"pg_mandato",bp.isROMandato(),null,null);%></td>
  </tr>
  <tr>
  <tr>
	<td><% bp.getController().writeFormLabel( out, "ds_mandato"); %></td>
	<td><% bp.getController().writeFormInput( out,null,"ds_mandato",bp.isROMandato(),null,null);%></td>
  </tr>
</table>
</div>

<table>
  <tr>
	<td><span class="GroupLabel">Reversali disponibili</span></td>
	<td></td>
	<td><span class="GroupLabel">Reversali selezionate</span></td>
  </tr>
  <tr>
	<td rowspan="2"><%bp.getReversaliDisponibiliCRUDController().writeHTMLTable(pageContext,"ASS_MANUALE_MAN_REV",false,false,false,"100%","200px"); %></td>
	<td><% JSPUtils.button(pageContext,bp.encodePath("img/doublerightarrow24.gif"),"javascript:submitForm('doAggiungiReversali')", bp.getParentRoot().isBootstrap()); %></td>
	<td rowspan="2"><%	bp.getReversaliAssociateCRUDController().writeHTMLTable(pageContext,"ASS_MANUALE_MAN_REV",false,false,false,"100%","200px"); %></td>
  </tr>
  <tr>
	<td><% JSPUtils.button(pageContext,bp.encodePath("img/doubleleftarrow24.gif"),"javascript:submitForm('doRimuoviReversali')", bp.getParentRoot().isBootstrap()); %></td>
  </tr>
</table>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>