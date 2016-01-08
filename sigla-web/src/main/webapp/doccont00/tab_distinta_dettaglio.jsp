<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page 
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*"
%>

<% 	it.cnr.contab.doccont00.bp.CRUDDistintaCassiereBP bp = (it.cnr.contab.doccont00.bp.CRUDDistintaCassiereBP)BusinessProcess.getBusinessProcess(request);
	String setCol = null;
	if (bp.isElencoConUo())
		setCol = "elencoConUo";
%>
    
    <table class="Panel">	   
	<tr>
		<td>   
   			 <%bp.getDistintaCassDet().writeHTMLTable(pageContext,setCol,bp.isAddDocContabiliButtonEnabled(),false,bp.isRemoveDocContabiliButtonEnabled(),"100%","300px", true); %>
		</td>
	</tr>
	</table>
	<table>
	 <tr>
		<td><big>Totale Mandati</big></td>
		<td><% bp.getController().writeFormInput( out, "totMandati"); %></td>
	
		<td><big>Totale Reversali</big></td>
		<td><% bp.getController().writeFormInput( out, "totReversali"); %></td>
	</tr>
	</table>