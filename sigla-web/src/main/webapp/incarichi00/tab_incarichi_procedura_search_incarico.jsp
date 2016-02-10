<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.contab.incarichi00.bp.*"
%>

<%
CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)BusinessProcess.getBusinessProcess(request);
%>
   <fieldset class="fieldset">
    <legend class="GroupLabel"><% bp.getController().writeFormInput(out,null,"statoText",true,"GroupLabel","style=\"background: #F5F5DC;background-color:transparent;border-style : none; cursor:default;font-size : 16px;\"");%></legend>    
	<table class="Panel">
      <tr><td>
      <div class="Group">
	      <table>      
			  <tr>         
				<td><% bp.getController().writeFormLabel(out,"incaricoRepertorioSearch");%></td>
				<td><% bp.getController().writeFormInput(out,"default", "esercizio_repertorioSearch", false,"FormInput",null); %>
				    <% bp.getController().writeFormInput(out,"default", "pg_repertorioSearch", false,"FormInput",null); %>
				    <% bp.getController().writeFormInput(out,"default", "incaricoRepertorioSearch", false,"FormInput",null); %></td> 
			  </tr>
	      </table>
	  </div>
      </td></tr>
	</table>
   </fieldset>
  