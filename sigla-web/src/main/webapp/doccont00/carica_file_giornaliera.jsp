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
<script language="javascript" src="scripts/css.js"></script>
<title>Carica File Giornaliera</title>

<% JSPUtils.printBaseUrl(pageContext);%>
</head>


<% CaricaFileGiornalieraBP bp = (CaricaFileGiornalieraBP)BusinessProcess.getBusinessProcess(request); %>


<body class="Form">


<% bp.openFormWindow(pageContext); %>

	<div class="Group card">
	<table style="width:100%">
       <tr>
    	<td>
		  <span class="FormLabel" style="color:blue">Carica Nuovo File xml</span>
		</td>
       </tr>
		<tr>
			<td>
				<input type="file" name="fileGiornaliera">
				<% JSPUtils.button(out,null,null,"Carica File","javascript:submitForm('doCaricaFile')", true, bp.getParentRoot().isBootstrap()); %>
			</td>
		</tr>
	</table>
	</div>	
<%bp.closeFormWindow(pageContext); %>
</body>