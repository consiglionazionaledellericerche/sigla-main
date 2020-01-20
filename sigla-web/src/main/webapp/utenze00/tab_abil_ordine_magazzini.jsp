<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@page import="it.cnr.contab.ordmag.anag00.AbilUtenteUopOperBulk"%>
<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.utenze00.bp.*"
%>

<%  
		CRUDUtenzaBP bp = (CRUDUtenzaBP)BusinessProcess.getBusinessProcess(request);
		AbilUtenteUopOperBulk riga = (AbilUtenteUopOperBulk)bp.getCrudUtente_abil_ordini().getModel();
		bp.getCrudUtente_abil_ordini_mag().writeHTMLTable(pageContext,"ColumnsAbilOrdiniMag",true,false,true,"100%","140px");
%>
<div class="Group card p-2 mb-2">
	<table class="w-100">
		<tr>
			<td><% bp.getCrudUtente_abil_ordini_mag().writeFormLabel(out, "findMagazzino"); %></td>
			<td><% bp.getCrudUtente_abil_ordini_mag().writeFormInput(out, "findMagazzino"); %></td>
		</tr>


	</table>
</div>
