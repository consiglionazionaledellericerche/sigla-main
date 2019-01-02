<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*,it.cnr.contab.doccont00.bp.*"
%>


<%  
		CRUDReversaleBP bp = (CRUDReversaleBP)BusinessProcess.getBusinessProcess(request);
		it.cnr.contab.doccont00.core.bulk.ReversaleIBulk reversale = (it.cnr.contab.doccont00.core.bulk.ReversaleIBulk)bp.getModel();
%>

	
	<table border="0" cellspacing="0" cellpadding="2" class="w-100">
		<tr>
			<td colspan=2>
			      <b ALIGN="CENTER"><font size=2 class="h3 text-primary">Doc.contabili associati.</font></b>
			      <% bp.getMandatiRev().setEnabled( false );
			         bp.getMandatiRev().writeHTMLTable(pageContext,"doc_cont_coll",false,false,false,"100%","100px", true); %>
			</td>
		</tr>
	</table>