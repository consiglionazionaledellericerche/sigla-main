<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page 
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.utenze00.bp.*"
%>


<%  
		CRUDUtenzaBP bp = (CRUDUtenzaBP)BusinessProcess.getBusinessProcess(request);
%>

<table class="Form" width="100%">
  <tr>
		<td><%bp.getController().writeFormLabel( out, "cd_uo_per_ruolo"); %></td>
		<td>
    		<%bp.getController().writeFormInput( out, "cd_uo_per_ruolo"); %>
				<%bp.getController().writeFormInput( out, "ds_uo_per_ruolo"); %>
				<%bp.getController().writeFormInput( out, "find_uo_per_ruolo"); %>								
		</td>
  </tr>
</table>


<table class="Form" width="100%">
  <tr>
		<td><span class="GroupLabel">Ruoli disponibili</span></td>
		<td></td>
		<td><span class="GroupLabel">Ruoli assegnati</span></td>
  </tr>
  <tr>
		<td rowspan="2">
      <%	bp.getCrudRuoli_disponibili().writeHTMLTable(pageContext,null,false,false,false,"100%","300px"); %>
		</td>
		<td>
		<% JSPUtils.button(pageContext,bp.encodePath("img/doublerightarrow24.gif"),"javascript:submitForm('doAggiungiRuolo')"); %>
		</td>
		<td rowspan="2">
      <%	bp.getCrudRuoli().writeHTMLTable(pageContext,null,false,false,false,"100%","300px"); %>
		</td>
	</tr>
	<tr>
		<td>
		<% JSPUtils.button(pageContext,bp.encodePath("img/doubleleftarrow24.gif"),"javascript:submitForm('doRimuoviRuolo')"); %>
		</td>
	</tr>
</table>