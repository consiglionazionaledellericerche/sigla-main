<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.action.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.docamm00.consultazioni.bulk.*,
		it.cnr.contab.docamm00.consultazioni.bp.*" %>


<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Consultazione Riepilogo Compensi</title>
</head>
<body class="Form">

<% 
	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	ConsRiepilogoCompensiBP bp1 = (ConsRiepilogoCompensiBP)BusinessProcess.getBusinessProcess(request);
    VConsRiepCompensiBulk bulk = (VConsRiepCompensiBulk)bp1.getModel();
	bp.openFormWindow(pageContext);
	boolean isFieldReadOnly = true;
%>

	<div class="Group" style="width:100%">
		<table width="100%">
		  	<tr>
				<td><% bp.getController().writeFormLabel(out,"dettagliata"); %></td>
				<td><% bp.getController().writeFormInput(out,"dettagliata"); %></td>
				<td><% bp.getController().writeFormLabel(out,"groupTrattamento"); %></td>
				<td><% bp.getController().writeFormInput(out,null,"groupTrattamento",(bulk!=null?bulk.getDettagliata():false),null,null); %></td>
	  	  	</tr>
		    <tr>
	  			<td> <% bp.getController().writeFormLabel(out,"findUoForPrint"); %></td>
				<td>
					<% bp.getController().writeFormInput(out,null,"cdUoForPrint",(bulk!=null?!bulk.isUOForPrintEnabled():false),null,null); %>
					<% bp.getController().writeFormInput(out,"dsUoForPrint"); %>
					<% bp.getController().writeFormInput(out,null,"findUoForPrint",(bulk!=null?!bulk.isUOForPrintEnabled():false),null,null); %>
				</td>
			</tr> 
		</table>

		<div class="Group" style="width:100%">
			<table >
				<tr>
					<td>
						<% bp.getController().writeFormLabel(out,"cd_soggetto");%>
					</td>
					<td colspan="3">
						<% bp.getController().writeFormInput(out,"cd_soggetto");%>
						<% bp.getController().writeFormInput(out,"filtroSoggetto");%>
					</td>
				</tr>
				<tr>
					<td>
						<% bp.getController().writeFormLabel(out,"cd_precedente");%>
					</td>
					<td colspan="3">
						<% bp.getController().writeFormInput(out, "cd_precedente");%>
					</td>
				</tr>
				<tr>
					<% bp.getController().writeFormField(out,"codice_fiscale");%>
				</tr>	
				<tr>
					<td>
						<% bp.getController().writeFormLabel(out,"filtroCognome");%>
					</td>
					<td colspan="3">
						<% bp.getController().writeFormInput(out,"filtroCognome");%>
					</td>
				</tr>
				<tr>
					<td>
						<% bp.getController().writeFormLabel(out,"filtroNome");%>
					</td>
					<td colspan="3">
						<% bp.getController().writeFormInput(out,"filtroNome");%>
					</td>
				</tr>
			</table>	
		</div>
		<table>  
  <tr>
    <td>	
	  <div class="Group">
	    <table>    	  
	  	  <tr>
				<td> <% bp.getController().writeFormLabel(out,"da_dt_competenza");%></td>
				<td> <% bp.getController().writeFormInput(out,null,"da_dt_competenza",false,null,"");%></td>
	  	  </tr>
	  	  <tr>
				<td> <% bp.getController().writeFormLabel(out,"a_dt_competenza");%></td>
				<td> <% bp.getController().writeFormInput(out,null,"a_dt_competenza",false,null,"");%></td>
	  	  </tr>
	    </table>
	  </div>
	  <div class="Group">
	    <table>    	  
	  	  <tr>
				<td> <% bp.getController().writeFormLabel(out,"da_dt_pagamento");%></td>
				<td> <% bp.getController().writeFormInput(out,null,"da_dt_pagamento",false,null,"");%></td>
	  	  </tr>
	  	  <tr>
				<td> <% bp.getController().writeFormLabel(out,"a_dt_pagamento");%></td>
				<td> <% bp.getController().writeFormInput(out,null,"a_dt_pagamento",false,null,"");%></td>
	  	  </tr>
	    </table>
	  </div>
    </td>
  </tr>	    
</table> 
	</div>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html> 
