<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.prevent01.bp.CRUDPdgMissioneBP"
%>

<%
	CRUDPdgMissioneBP bp = (CRUDPdgMissioneBP)BusinessProcess.getBusinessProcess(request);
%>

<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title><%=bp.getBulkInfo().getShortDescription()%></title>
</head>

<body class="Form">
<% bp.openFormWindow(pageContext);%>
	<table class="Panel">
      <% bp.getController().writeForm(out);%>
   	</table>
<% if (!bp.isSearching() && bp.getModel().getUtcr()!=null) {%>   	
	<table class="Form" width="100%">
  		<tr>
			<td><span class="GroupLabel">Tipi UO disponibili</span></td>
			<td></td>
			<td><span class="GroupLabel">Tipi UO collegati</span></td>
		</tr>
		<tr>
			<td rowspan="2">
      <%	bp.getCrudTipiUoAssociabili().writeHTMLTable(pageContext,"default",false,false,false,"100%","300px"); %>
		</td>
		<td>
		<% JSPUtils.button(pageContext,bp.encodePath("img/doublerightarrow24.gif"),"javascript:submitForm('doAggiungiTipoUo')", bp.getParentRoot().isBootstrap()); %>
		</td>
		<td rowspan="2">
      <%	bp.getCrudAssPdgMissioneTipiUo().writeHTMLTable(pageContext,"csAssPdgMissione",false,false,false,"100%","300px"); %>
		</td>
	</tr>
	<tr>
		<td>
		<% JSPUtils.button(pageContext,bp.encodePath("img/doubleleftarrow24.gif"),"javascript:submitForm('doRimuoviTipoUo')", bp.getParentRoot().isBootstrap()); %>
		</td>
	</tr>
</table>   	
<% } %>
<% bp.closeFormWindow(pageContext); %>
</body>