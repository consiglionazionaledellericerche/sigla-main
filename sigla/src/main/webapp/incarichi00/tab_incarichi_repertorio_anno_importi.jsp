<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.contab.incarichi00.bp.*"
%>

<%
CRUDIncarichiRepertorioBP bp = (CRUDIncarichiRepertorioBP)BusinessProcess.getBusinessProcess(request);
%>
<table class="Panel">
	<tr>
		<td><% bp.getRipartizionePerAnno().writeFormLabel(out,"esercizio_limite"); %></td>
		<td colspan="3"><% bp.getRipartizionePerAnno().writeFormInput(out,"esercizio_limite"); %></td>
	</tr>
	<tr>
  	    <td><% bp.getRipartizionePerAnno().writeFormLabel(out,"importo_iniziale"); %></td>
		<td colspan="3"><% bp.getRipartizionePerAnno().writeFormInput(out,"importo_iniziale"); %></td>
	</tr>
	<tr>
  	    <td><% bp.getRipartizionePerAnno().writeFormLabel(out,"importo_complessivo"); %></td>
		<td colspan="3"><% bp.getRipartizionePerAnno().writeFormInput(out,"importo_complessivo"); %></td>
	</tr>	
</table>
