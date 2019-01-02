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

<div class="Panel card p-2 mb-2" style="width:100%">
<table>
  <tr>
	<td><% bp.getController().writeFormLabel( out, "pg_mandato"); %></td>
	<td><% bp.getController().writeFormInput( out,null,"pg_mandato",bp.isROMandato(),null,null);%></td>
  </tr>
  <tr>
  <tr>
	<td><% bp.getController().writeFormLabel( out, "ds_mandato"); %></td>
	<td class="w-90"><% bp.getController().writeFormInput( out,null,"ds_mandato",bp.isROMandato(),null,null);%></td>
  </tr>
</table>
</div>

<table class="Panel card p-2">
  <tr>
	<td><span class="GroupLabel h3 text-primary">Reversali disponibili</span></td>
	<td></td>
	<td><span class="GroupLabel h3 text-primary">Reversali selezionate</span></td>
  </tr>
  <tr>
	<td rowspan="2" class="w-50"><%bp.getReversaliDisponibiliCRUDController().writeHTMLTable(pageContext,"ASS_MANUALE_MAN_REV",false,false,false,"100%","200px"); %></td>
	<td>
	    <% JSPUtils.button(out,
                bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-long-arrow-right faa-passing" : "img/doublerightarrow24.gif",
                bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-long-arrow-right" : "img/doublerightarrow24.gif",
                null,
                "javascript:submitForm('doAggiungiReversali')",
                "btn-outline-primary faa-parent animated-hover",
                true,
                bp.getParentRoot().isBootstrap()); %>
	</td>
	<td rowspan="2" class="w-50"><%	bp.getReversaliAssociateCRUDController().writeHTMLTable(pageContext,"ASS_MANUALE_MAN_REV",false,false,false,"100%","200px"); %></td>
  </tr>
  <tr>
	<td>
        <% JSPUtils.button(out,
                bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-long-arrow-left faa-passing-reverse" : "img/doubleleftarrow24.gif",
                bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-long-arrow-left" : "img/doubleleftarrow24.gif",
                null,
                "javascript:submitForm('doRimuoviReversali')",
                "btn-outline-primary faa-parent animated-hover",
                true,
                bp.getParentRoot().isBootstrap()); %>
	</td>
  </tr>
</table>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>