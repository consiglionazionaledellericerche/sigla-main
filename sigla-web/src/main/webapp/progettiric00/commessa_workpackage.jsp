<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page 
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.utenze00.bp.*,
		it.cnr.contab.progettiric00.bp.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<script language="JavaScript" src="scripts/util.js"></script>
<% JSPUtils.printBaseUrl(pageContext); %>
</head>
<script language="javascript" src="scripts/css.js"></script>
<title>Associazione Modulo di Attività/GAE</title>
<body class="Form">
<%  CRUDBP bpt = (CRUDBP)BusinessProcess.getBusinessProcess(request);
    bpt.openFormWindow(pageContext);
	CommessaWorkpackageBP bp = (CommessaWorkpackageBP)BusinessProcess.getBusinessProcess(request);
%>
<table class="Form" width="100%">
  <tr>
		<td width="107"><%bp.getController().writeFormLabel( out, "cd_progetto_comm_wp"); %></td>
		<td width="536"><%bp.getController().writeFormInput( out, "cd_progetto_comm_wp"); %>
				<%bp.getController().writeFormInput( out, "ds_progetto_comm_wp"); %>
				<%bp.getController().writeFormInput( out, "find_wp_per_commessa"); %>
		</td>
  </tr>
</table>
<table class="Form" width="100%">
  <tr>
		<td><span class="GroupLabel">GAE disponibili</span></td>
		<td></td>
		<td><span class="GroupLabel">GAE collegati</span></td>
  </tr>
  <tr>
		<td rowspan="2">
      <%	bp.getCrudWorkpackage_disponibili().writeHTMLTable(pageContext,"csCommessaWorkpackageDisponibili",false,false,false,"100%","300px"); %>
		</td>
		<td>
		<% JSPUtils.button(pageContext,bp.encodePath("img/doublerightarrow24.gif"),"javascript:submitForm('doAggiungiWorkpackage')", bp.getParentRoot().isBootstrap()); %>
		</td>
		<td rowspan="2">
      <%	bp.getCrudWorkpackage_collegati().writeHTMLTable(pageContext,"csCommessaWorkpackageCollegati",false,false,false,"100%","300px"); %>
		</td>
	</tr>
	<tr>
		<td>
		<%-- <% JSPUtils.button(pageContext,bp.encodePath("img/doubleleftarrow24.gif"),"javascript:submitForm('doRimuoviWorkpackage')"); %> --%>
		</td>
	</tr>
</table>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>