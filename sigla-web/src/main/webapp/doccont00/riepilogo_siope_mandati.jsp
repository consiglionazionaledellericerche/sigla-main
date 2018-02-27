<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.action.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.doccont00.bp.*" %>


<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Riepilogo Siope su Mandati</title>
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
				<td> <% bp.getController().writeFormLabel(out,"esercizio"); %></td>
    			<td colspan=1> <% bp.getController().writeFormInput(out,null,"esercizio",false,null,"");%></td>
    		 </tr>
    		
    		<tr>
    			<td><% bp.getController().writeFormLabel( out, "find_cds"); %></td>		
				<td colspan=4><% bp.getController().writeFormInput( out, "cds_cd");
								 bp.getController().writeFormInput( out, "cds_ds");
						         bp.getController().writeFormInput( out, "find_cds");%></td>
			 </tr>

 			 <tr>
				<td> <% bp.getController().writeFormLabel(out,"dt_emissione_da");%></td>
				<td> <% bp.getController().writeFormInput(out,null,"dt_emissione_da",false,null,"");%></td>
			  	<td> <% bp.getController().writeFormLabel(out,"dt_emissione_a");%></td>
				<td> <% bp.getController().writeFormInput(out,null,"dt_emissione_a",false,null,"");%></td>
			 </tr>
				
			  
 			 <tr>
				<td> <% bp.getController().writeFormLabel(out,"dt_pagamento_da");%></td>
				<td> <% bp.getController().writeFormInput(out,null,"dt_pagamento_da",false,null,"");%></td>
			 	<td> <% bp.getController().writeFormLabel(out,"dt_pagamento_a");%></td>
				<td> <% bp.getController().writeFormInput(out,null,"dt_pagamento_a",false,null,"");%></td>
			 </tr>
				
			 
			 <tr>
				<td> <% bp.getController().writeFormLabel(out,"dt_trasmissione_da");%></td>
				<td> <% bp.getController().writeFormInput(out,null,"dt_trasmissione_da",false,null,"");%></td>
			  	<td> <% bp.getController().writeFormLabel(out,"dt_trasmissione_a");%></td>
				<td> <% bp.getController().writeFormInput(out,null,"dt_trasmissione_a",false,null,"");%></td>
			 </tr>
				
 			 
	</table>
		
	</div>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html> 
