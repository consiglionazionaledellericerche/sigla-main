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
	<div>
	<table border="0" cellspacing="0" cellpadding="2">	
  		<tr>
			<td><% bp.getController().writeFormLabel( out, "im_tot_reversale"); %></td>
    		<td><% bp.getController().writeFormInput( out, "im_tot_reversale"); %>
    			<% bp.getController().writeFormLabel( out, "im_residuo_reversale"); %>
    			<% bp.getController().writeFormInput( out, "im_residuo_reversale"); %></td>
		</tr>
	</table>
	</div>
	
	<table border="0" cellspacing="0" cellpadding="2" class="w-100">
		<tr>
			<td>
			      <b><font size=2 class="h3 text-primary">Sospesi associati alla reversale</font></b>
			      <% bp.getSospesiSelezionati().writeHTMLTable(pageContext,null,bp.isCaricaSospesiButtonEnabled() && bp.isEditable(),false,bp.isRimuoviSospesiButtonEnabled(),"100%","200px", false); %>
			</td>
		</tr>
	</table>