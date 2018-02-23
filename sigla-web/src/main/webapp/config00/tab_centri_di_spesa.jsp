<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*,it.cnr.contab.config00.bp.*"
%>


<%  
		CRUDConfigCdSBP bp = (CRUDConfigCdSBP)BusinessProcess.getBusinessProcess(request);
		it.cnr.contab.config00.sto.bulk.CdsBulk cds = (it.cnr.contab.config00.sto.bulk.CdsBulk)bp.getModel();
%>

	<table border="0" cellspacing="0" cellpadding="2">
	<tr colspan=2>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "cd_proprio_unita"); %></td>
	<td><% bp.getController().writeFormInputByStatus( out, "cd_proprio_unita"); %></td>	
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "esercizio_inizio"); %></td>
	<td><% bp.getController().writeFormInputByStatus( out, "esercizio_inizio"); %></td>
	</tr>

	<tr>
	<td><% bp.getController().writeFormLabel( out, "ds_unita_organizzativa"); %></td>	
	<td><% bp.getController().writeFormInput( out, "ds_unita_organizzativa"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "cd_responsabile"); %></td>	
	<td> 
	    <% 
       	      bp.getController().writeFormInput( out, "cd_responsabile");
	      bp.getController().writeFormInput( out, "ds_responsabile");
              bp.getController().writeFormInput( out, "find_responsabile" );
	      bp.getController().writeFormInput( out, "crea_responsabile" );			  
            %>	
	</td>
	<tr>
		<% bp.getController().writeFormField( out, "area_scientifica" ); %>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "cd_tipo_unita"); %></td>
	<td><% bp.getController().writeFormInputByStatus( out, "cd_tipo_unita"); %>
	</td>
	</tr>	
	<tr>
		<td><% bp.getController().writeFormLabel( out, "esercizio_fine"); %></td>	
		<td><% bp.getController().writeFormInput( out, "esercizio_fine"); %></td>
	</tr>
	</table>