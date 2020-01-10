<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*,it.cnr.contab.doccont00.bp.*"
%>


<%  
		CRUDAbstractMandatoBP bp = (CRUDAbstractMandatoBP)BusinessProcess.getBusinessProcess(request);
		it.cnr.contab.doccont00.core.bulk.MandatoBulk mandato = (it.cnr.contab.doccont00.core.bulk.MandatoBulk)bp.getModel();
%>
	<table border="0" cellspacing="0" cellpadding="2" class="w-100 h-100">
		<tr>
			<td>
			      <b class="h3 text-primary">Sospesi associati al mandato</b>
			      <% bp.getSospesiSelezionati().writeHTMLTable(pageContext,null,bp.isCaricaSospesiButtonEnabled() && bp.isEditable(),false,bp.isRimuoviSospesiButtonEnabled(),"100%","200px"); %>
			</td>
		</tr>
	</table>