<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.varstanz00.bp.*"
%>
<%
	CRUDVar_stanz_resRigaBP bp = (CRUDVar_stanz_resRigaBP)BusinessProcess.getBusinessProcess(request);
	SimpleDetailCRUDController controller = bp.getRigaVariazione();
%>

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title><%=bp.getBulkInfo().getLongDescription()%></title>
</head>

<body class="Form">
<% bp.openFormWindow(pageContext); %>
<div class="card p-2">
	<table class="w-100">
		<tr>
	        <td><% bp.getController().writeFormLabel(out,"label_variazione_riga"); %></td>
	        <td><% bp.getController().writeFormInput(out,"pg_variazione"); %>
	            <% bp.getController().writeFormInput(out,"ds_variazione_riga"); %>
	        </td>
		</tr>
		<tr>
	        <td><% bp.getController().writeFormLabel(out,"cdr"); %></td>
	        <td><% bp.getController().writeFormInput(out,"cdr"); %></td>
		</tr>	
	</table>
</div>
<table class="Panel" width="100%">
	<tr>
        <td><%controller.writeHTMLTable(pageContext,"rigaVariazione",true,false,true,"100%","200px"); %></td>
    </tr>
</table>
<table class="Panel" cellspacing=2 width="100%">
   <tr valign="top">
    <td>
      <table class="Panel" cellspacing=2 align=right>
			<tr>
		                <td><% bp.getController().writeFormLabel(out,"totale_righe_variazione"); %></td>
		                <td><% bp.getController().writeFormInput(out,"totale_righe_variazione"); %></td>
			</tr>
      </table>
    </td>	
  </tr>		
  <tr valign="top">
   <td>
	<div class="card p-2">
		<table class="w-100"> 
			<tr>
		        <td><% controller.writeFormLabel(out,"linea_di_attivita"); %></td>
		        <td colspan="3"><% controller.writeFormInput(out,"linea_di_attivita"); %></td>
			</tr>  
			 <% if (bp.getParametriCnr().getFl_nuovo_pdg().booleanValue()) { %>
			   <tr>
					<% controller.writeFormField(out,"missione"); %>
		        	<% controller.writeFormField(out,"programma"); %>
		        	<% controller.writeFormField(out,"natura"); %>
		        </tr>   
	        <% } %>
			<tr>
				<% if (!bp.getParametriCnr().getFl_nuovo_pdg()) { %> 	
		        	<td><% controller.writeFormLabel(out,"modulo_di_attivita"); %></td>
		        	<td colspan="3"><% controller.writeFormInput(out,"modulo_di_attivita"); %></td>
		        <% } else { %>
		        	<td><% controller.writeFormLabel(out,"progetto_liv2"); %></td>
		        	<td colspan="3"><% controller.writeFormInput(out,"progetto_liv2"); %></td>
				<% } %>
			</tr>
			<tr>
			<%	if (bp.isVariazioneApprovata()) { %> 
				<td><% controller.writeFormLabel(out,"elemento_voce_vis"); %></td>
				<td colspan="3"><% controller.writeFormInput(out,"elemento_voce_vis"); %></td>
			<% } else {%>
				<td><% controller.writeFormLabel(out,"elemento_voce"); %></td>
				<td colspan="3"><% controller.writeFormInput(out,"elemento_voce"); %></td>
			<% } %>
			</tr>
			<%	if (!bp.getParametriCnr().getFl_nuovo_pdg()) { %> 
			<tr>
				<td><% controller.writeFormLabel(out,"voce_f"); %></td>
				<td colspan="3"><% controller.writeFormInput(out,"voce_f"); %></td>
			</tr>
			<% } %>
			<tr>
				<td><% controller.writeFormLabel(out,"disponibilita_stanz_res"); %></td>
				<td><% controller.writeFormInput(out,"disponibilita_stanz_res"); %></td>
			</tr>
			<tr>
				<td><% controller.writeFormLabel(out,"im_variazione"); %></td>
				<td><% controller.writeFormInput(out,"im_variazione"); %></td>
			</tr>
		</table>
	</div>
   </td>
  </tr>
 </table>    	
<% bp.closeFormWindow(pageContext); %>
</body>