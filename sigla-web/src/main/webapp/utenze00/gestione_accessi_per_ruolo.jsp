<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page 
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.utenze00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<title>Gestione Accessi per Ruolo</title>
<body class="Form">

<% CRUDRuoloBP bp = (CRUDRuoloBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>

	<table class="Panel">
	<tr>
	<td><% bp.getController().writeFormLabel( out, "cd_ruolo"); %></td>
	<td colspan=3><% bp.getController().writeFormInput( out, "cd_ruolo"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "ds_ruolo"); %></td>
	<td colspan=3><% bp.getController().writeFormInput( out, "ds_ruolo"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "tipo_ruolo"); %></td>
    <td colspan=2><% bp.getController().writeFormInput( out, "tipo_ruolo"); %></td>
	<td ><% bp.getController().writeFormInput( out, "crea_tipo_ruolo"); %></td>
	</tr>
	<tr>	
		 <td><% bp.getController().writeFormLabel( out, "find_cds"); %></td>
		 <td colspan=3><% bp.getController().writeFormInput( out, null, "find_cds",!bp.isCdsFieldEnabled(),null,null); %></td>
	</tr>			 

	</table>
	<table class="Form" width="100%">
  <tr>
		<td><span class="GroupLabel">Accessi disponibili</span></td>
		<td></td>
		<td><span class="GroupLabel">Accessi assegnati</span></td>
  </tr>
  <tr>
		<td rowspan="2">
      <%	bp.getCrudAccessi_disponibili().writeHTMLTable(pageContext,null,false,false,false,"100%","300px"); %>
		</td>
		<td>
		<% JSPUtils.button(pageContext,bp.encodePath("img/doublerightarrow24.gif"),"javascript:submitForm('doAggiungiAccesso')"); %>
		</td>
		<td rowspan="2">
      <%	bp.getCrudAccessi().writeHTMLTable(pageContext,null,false,false,false,"100%","300px"); %>
		</td>
	</tr>
	<tr>
		<td>
		<% JSPUtils.button(pageContext,bp.encodePath("img/doubleleftarrow24.gif"),"javascript:submitForm('doRimuoviAccesso')"); %>
		</td>
	</tr>
	</table>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>