<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.pdg00.bp.*,
		it.cnr.contab.pdg00.bulk.*"
%>

<%
	PdGVariazioneBP bp = (PdGVariazioneBP)BusinessProcess.getBusinessProcess(request);
	SimpleDetailCRUDController controller = ( (PdGVariazioneBP)bp ).getCrudAssCDR();
	Pdg_variazioneBulk pdg = (Pdg_variazioneBulk)bp.getModel();
//	boolean isTableEnabled = (bp.isCdrScrivania() && pdg.isPropostaProvvisoria()) || bp.isUoEnte();
	boolean isTableEnabled = (bp.isCdrScrivania() && pdg.isPropostaProvvisoria());
	boolean isFieldEnabled = !isTableEnabled;
    if (bp.isUoEnte() && pdg.isApprovata())
    	controller.writeHTMLTable(pageContext,"dipAssociazioneCDR",isTableEnabled,false,isTableEnabled,"100%","130px"); 
    else
    	controller.writeHTMLTable(pageContext,"associazioneCDR",isTableEnabled,false,isTableEnabled,"100%","130px"); 
%>

<div class="card p-2">
<table class="Panel w-100" cellspacing="2">
	<tr>
  	    <td class="w-20"><% controller.writeFormLabel(out,"default","centro_responsabilita"); %></td>
		<td colspan="4"><% controller.writeFormInput(out,"default","centro_responsabilita",isFieldEnabled,null,null); %></td>
	</tr>
	<tr>
  	    <td><% controller.writeFormLabel(out,"default","im_entrata"); %></td>
		<td class="w-20"><% controller.writeFormInput(out,"default","im_entrata",isFieldEnabled,null,null); %></td>		
		<td class="w-10">&nbsp;</td>
  	    <td class="w-20"><% controller.writeFormLabel(out,"default","im_spesa"); %></td>
		<td class="w-20"><% controller.writeFormInput(out,"default","im_spesa",isFieldEnabled,null,null); %></td>				
	</tr>	
	<tr>
  	    <% controller.writeFormField(out,"default","entrata_ripartita"); %>
		<td>&nbsp;</td>
  	    <% controller.writeFormField(out,"default","spesa_ripartita"); %>				
	</tr>	
	<tr>
  	    <td><% controller.writeFormLabel(out,"default","entrata_diff"); %></td>
		<td><% controller.writeFormInput(out,"default","entrata_diff"); %></td>		
		<td>&nbsp;</td>
  	    <td><% controller.writeFormLabel(out,"default","spesa_diff"); %></td>
		<td><% controller.writeFormInput(out,"default","spesa_diff"); %></td>				
	</tr>	
	
	<tr>
	    <td>&nbsp;</td>
		<td align=center><% controller.writeFormInput(out,"default","dettagliEntrate"); %></td>	
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td align=center><% controller.writeFormInput(out,"default","dettagliSpese"); %></td>
	</tr>	
	
</table>
</div>