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
<title>Movimentazione Progetto - Piano Economico - Voce - GAE</title>
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
                <td><% bp.getController().writeFormLabel(out,"findProgettoForPrint");%></td>
                <td><% bp.getController().writeFormInput(out,"findProgettoForPrint"); %></td>
              </tr>
    	</table>
		
	</div>
	  <div class="Group card p-2 mb-2">
	    <table class="w-50">
	      <tr>
			<td>
				<% bp.getController().writeFormLabel(out,"tipoStampa");%>
			</td>
			<td>
				<% bp.getController().writeFormInput(out,null,"tipoStampa",false,null,"");%>
			</td>
		  </tr>
	    </table>
	  </div>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html> 
