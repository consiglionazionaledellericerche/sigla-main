<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.varstanz00.bp.*,
		it.cnr.contab.varstanz00.bulk.*"
%>

<%
	CRUDVar_stanz_resBP bp = (CRUDVar_stanz_resBP)BusinessProcess.getBusinessProcess(request);
	SimpleDetailCRUDController controller = ( (CRUDVar_stanz_resBP)bp ).getCrudAssCDR();
	Var_stanz_resBulk var_stanz_res = (Var_stanz_resBulk)bp.getModel();	
	boolean isTableEnabled = (bp.isCdrScrivania() && var_stanz_res.isPropostaProvvisoria());
	boolean isFieldEnabled = !isTableEnabled;	
        controller.writeHTMLTable(pageContext,"associazioneCDR",isTableEnabled,false,isTableEnabled,"100%","100px"); 
%>
<div class="card p-2">
<table class="Panel" cellspacing=2 width="100%" align=right>
   <tr valign="top" align="right">
		<td>
			<% bp.getController().writeFormLabel(out,"totale_da_ripartire"); %>
			<% bp.getController().writeFormInput(out,"totale_da_ripartire"); %>
		</td>
  </tr>
</table>
</div>
<div class="Group card p-2">
<table class="Panel w-100" cellspacing="2">
	<tr>
	    <td class="w-20"><% controller.writeFormLabel(out,"centro_di_responsabilita"); %></td>
	    <td colspan="2"><% controller.writeFormInput(out,"default","centro_di_responsabilita",isFieldEnabled,null,null); %></td>
	</tr>
	<tr>
	    <td><% controller.writeFormLabel(out,"im_spesa"); %></td>
	    <td><% controller.writeFormInput(out,"default","im_spesa",isFieldEnabled,null,null); %></td>
	</tr>	
	<tr>
	    <td><% controller.writeFormLabel(out,"spesa_ripartita"); %></td>
	    <td><% controller.writeFormInput(out,"spesa_ripartita"); %></td>				
	</tr>	
	<tr>
	    <td><% controller.writeFormLabel(out,"spesa_diff"); %></td>
	    <td><% controller.writeFormInput(out,"spesa_diff"); %></td>				
	</tr>	
	<tr>
	    <td>&nbsp;</td>
	    <td align="left"><% controller.writeFormInput(out,"default","dettagliSpese"); %></td>
	</tr>	
</table>
</div>  
