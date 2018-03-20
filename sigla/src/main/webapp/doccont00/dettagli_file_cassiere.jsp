<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.doccont00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<script language="JavaScript" src="scripts/util.js"></script>
<% JSPUtils.printBaseUrl(pageContext);%>
</head>
<script language="javascript" src="scripts/css.js"></script>

<% DettagliFileCassiereBP bp = (DettagliFileCassiereBP)BusinessProcess.getBusinessProcess(request); %>

	<title>Dettagli File Cassiere</title>

<body class="Form">


<% bp.openFormWindow(pageContext); %>

	<table style="width:100%">
		<tr><td>
		<% bp.writeHTMLTable(pageContext,"100%","200px"); %>
		</td></tr>
		<tr><td>
		<% bp.writeHTMLNavigator(out); %>
		</td></tr>
	</table>
	<br>
	<table style="width:100%">
       <tr>
    	<td>
		  <span class="FormLabel" style="color:blue">Data</span>
		</td>
       </tr>
       <tr>
         <td>
			<% bp.writeFormInput(out, "data"); %>
   		</td>
       </tr>
    </table>
<%bp.closeFormWindow(pageContext); %>
</body>