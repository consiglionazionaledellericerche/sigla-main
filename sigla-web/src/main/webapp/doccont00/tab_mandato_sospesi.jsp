<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page 
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*,it.cnr.contab.doccont00.bp.*"
%>


<%  
		CRUDAbstractMandatoBP bp = (CRUDAbstractMandatoBP)BusinessProcess.getBusinessProcess(request);
		it.cnr.contab.doccont00.core.bulk.MandatoBulk mandato = (it.cnr.contab.doccont00.core.bulk.MandatoBulk)bp.getModel();
%>
	<table border="0" cellspacing="0" cellpadding="2">
		<tr>
			<td>
			      <b><font size=2>Sospesi associati al mandato</font></b>
			      <% bp.getSospesiSelezionati().writeHTMLTable(pageContext,null,bp.isCaricaSospesiButtonEnabled() && bp.isEditable() ,false,bp.isRimuoviSospesiButtonEnabled(),"100%","200px", false); %>
			</td>
		</tr>
	</table>