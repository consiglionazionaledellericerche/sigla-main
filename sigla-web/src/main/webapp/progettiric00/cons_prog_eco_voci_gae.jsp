<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.action.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.progettiric00.consultazioni.bp.*" %>
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Consultazione Progetto/Piano Economico/Voce/GAE</title>
</head>
<body class="Form">

<% 
	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
	boolean isFieldReadOnly = true;
%>

	<div class="Group" style="width:100%">
		<table width="100%">

              <tr>
                <td><% bp.getController().writeFormLabel(out,"findProgetto");%></td>
                <td><% bp.getController().writeFormInput(out,"findProgetto"); %></td>
              </tr>
		  <tr>
				<td> <% bp.getController().writeFormLabel(out,"esercizio_piano"); %></td>
    			<td> <% bp.getController().writeFormInput(out,null,"esercizio_piano",false,null,"");%></td>
    		 </tr>

    	</table>
		
	</div>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html> 
