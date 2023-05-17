<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.config00.bp.*"
%>

<%
	CRUDConfigAnagContrattoBP bp = (CRUDConfigAnagContrattoBP)BusinessProcess.getBusinessProcess(request);
	SimpleDetailCRUDController controller = bp.getCrudAssDitte(); 
%>
<table class="Form" width="100%">
  <tr>
		<td colspan="5">
        <%bp.getCrudAssDitte().writeHTMLTable(pageContext,"ditte",true,false,true,"100%","200px"); %>
		</td>	 
	</tr>
	</table>
	<div class="Group card"><table>   
       <tr>
    	<td>
		  <span class="FormLabel" style="color:blue">File excel da caricare</span>
		</td>
		<td>
			<input type="file" name="file">			
		</td>
		<td>
			<% JSPUtils.button(out,null,null,
					"Carica Ditte Invitate","javascript:submitForm('doCaricaDitteInvitate')",
					true,
					//bp.getCrudAssDitte()==null||bp.getCrudAssDitte().countDetails()==0,
					bp.getParentRoot().isBootstrap()); %>
		</td>
	</tr>
	<tr>
	
	</tr>
	</table></div>
	<table> 
	  	<tr>
	        <td><% controller.writeFormLabel(out,"default","denominazione"); %></td>
	        <td colspan="2"><% controller.writeFormInput(out,"default","denominazione"); %></td>
		</tr>
		<tr>
		     <td><% controller.writeFormLabel(out,"default","codice_fiscale"); %></td>
		     <td><% controller.writeFormInput(out,"default","codice_fiscale"); %></td>
		</tr>
	    <tr>
	        <td><% controller.writeFormLabel(out,"default","id_fiscale"); %></td>
	        <td><% controller.writeFormInput(out,"default","id_fiscale"); %></td>
		<tr>
	        <td><% controller.writeFormLabel(out,"default","denominazione_rti"); %></td>
	        <td colspan="2"><% controller.writeFormInput(out,"default","denominazione_rti"); %></td>
		</tr>
		<tr>
	        <td><% controller.writeFormLabel(out,"default","ruolo"); %></td>
	        <td><% controller.writeFormInput(out,"default","ruolo"); %></td>
		</tr>	
		<tr>
	        <td><% controller.writeFormLabel(out,"default","fl_offerta_presentata"); %></td>
	        <td><% controller.writeFormInput(out,"default","fl_offerta_presentata"); %></td>
		</tr>
</table>