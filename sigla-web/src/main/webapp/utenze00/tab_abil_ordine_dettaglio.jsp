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
		if ( riga==null)
		    riga= new AbilUtenteUopOperBulk();
		System.out.println(riga);

%>
<div class="Group card p-2 mb-2">
	<table class="w-100">
		<tr>
			<td><% bp.getCrudUtente_abil_ordini().writeFormLabel(out, "findUnitaOperativaOrd"); %></td>
			<td><% bp.getCrudUtente_abil_ordini().writeFormInput(out, "findUnitaOperativaOrd"); %></td>
		</tr>
		<tr>
			<td><% bp.getCrudUtente_abil_ordini().writeFormLabel(out, "findTipoOperazioneOrd"); %></td>
			<td><% bp.getCrudUtente_abil_ordini().writeFormInput(out, "findTipoOperazioneOrd"); %></td>
		</tr>
		
		<tr>
			<td><% bp.getCrudUtente_abil_ordini().writeFormLabel(out, "tuttiMagazzini");%></td>		
			<td><% bp.getCrudUtente_abil_ordini().writeFormInput(out, "tuttiMagazzini"); %></td>	
		</tr>
		
	</table>
</div>
