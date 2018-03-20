<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.doccont00.bp.*"
%>


<%  
		CRUDObbligazioneBP bp = (CRUDObbligazioneBP)BusinessProcess.getBusinessProcess(request);
		it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = (it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk)bp.getModel();
		obbligazione.refreshDettagliScadenzarioPerCdrECapitoli();
%>

<table class="Form" width="100%">
  <tr>
	<td>
	<b ALIGN="CENTER"><big>Centri di responsabilità</big></b></td>
  </tr>
  <tr>	
	<td>
	    <%	bp.getAggregatoPerCdr().setEnabled( false );
	        bp.getAggregatoPerCdr().writeHTMLTable(pageContext,null,false,false,false,"100%","100px"); %>
	</td>
  </tr>  
</table>