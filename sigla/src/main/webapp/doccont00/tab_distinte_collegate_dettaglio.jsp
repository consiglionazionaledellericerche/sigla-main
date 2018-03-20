<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*"
%>

<%
   it.cnr.contab.doccont00.bp.CRUDDistintaCassiereBP bp = (it.cnr.contab.doccont00.bp.CRUDDistintaCassiereBP)BusinessProcess.getBusinessProcess(request);
%>
    <table class="Panel">	   
	<tr>
		<td>   
   			 <%bp.getDistinteCassCollegateDet().writeHTMLTable(pageContext,"elencoConUoeDistinta",false,false,false,"100%","300px", true); %>
		</td>
	</tr>
	</table>