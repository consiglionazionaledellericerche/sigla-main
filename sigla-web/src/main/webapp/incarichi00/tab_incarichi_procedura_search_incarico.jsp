<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.contab.incarichi00.bp.*"
%>

<%
CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)BusinessProcess.getBusinessProcess(request);
%>
   <fieldset class="fieldset">
	<table class="Panel w-100">
      <tr><td>
      <div class="Group card">
	      <table>      
			  <tr>         
				<td><% bp.getController().writeFormLabel(out,"incaricoRepertorioSearch");%></td>
				<td><% bp.getController().writeFormInput(out,"default", "esercizio_repertorioSearch", false,null,null); %>
				    <% bp.getController().writeFormInput(out,"default", "pg_repertorioSearch", false,null,null); %>
				    <% bp.getController().writeFormInput(out,"default", "incaricoRepertorioSearch", false,null,null); %></td> 
			  </tr>
	      </table>
	  </div>
      </td></tr>
	</table>
   </fieldset>
  