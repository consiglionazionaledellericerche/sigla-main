<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	        it.cnr.jada.action.*,
	        it.cnr.contab.utenze00.bp.*"	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Tipo Ruolo</title>
</head>
<body class="Form"> 

<% CRUDTipoRuoloBP bp = (CRUDTipoRuoloBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>

	<table class="Panel">
		<tr>
		  <td>
			<% bp.getController().writeFormLabel(out,"tipo");%>
		  </td>
		  <td>
			<% bp.getController().writeFormInput(out,"tipo");%>
		  </td>
		</tr>
		<tr>
		  <td>
			<% bp.getController().writeFormLabel(out,"ds_tipo");%>
		  </td>
		  <td>
			<% bp.getController().writeFormInput(out,"ds_tipo");%>
		  </td>
		</tr>
	</table>
	<table class="Form" width="100%">
    <tr>
		<td><span class="GroupLabel">Privilegi disponibili</span></td>
		<td></td>
		<td><span class="GroupLabel">Privilegi assegnati</span></td>
    </tr>
    <tr>
		<td rowspan="2">
      <%	bp.getCrudPrivilegi_disponibili().writeHTMLTable(pageContext,null,false,false,false,"100%","300px"); %>
		</td>
		<td>
		<% JSPUtils.button(pageContext,bp.encodePath("img/doublerightarrow24.gif"),"javascript:submitForm('doAggiungiPrivilegio')", bp.getParentRoot().isBootstrap()); %>
		</td>
		<td rowspan="2">
      <%	bp.getCrudPrivilegi().writeHTMLTable(pageContext,null,false,false,false,"100%","300px"); %>
		</td>
	</tr>
	<tr>
		<td>
		<% JSPUtils.button(pageContext,bp.encodePath("img/doubleleftarrow24.gif"),"javascript:submitForm('doRimuoviPrivilegio')", bp.getParentRoot().isBootstrap()); %>
		</td>
	</tr>
	</table>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
