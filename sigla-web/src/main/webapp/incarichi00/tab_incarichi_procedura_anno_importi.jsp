<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.contab.incarichi00.bp.*,
		it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk"
%>

<%
CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)BusinessProcess.getBusinessProcess(request);
Incarichi_proceduraBulk model = (Incarichi_proceduraBulk)bp.getModel();
boolean existsVariazioni=false;
if (model!=null)
	existsVariazioni = model.hasVariazioni(); 
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
	<% if (model.getFaseProcesso().compareTo(Incarichi_proceduraBulk.FASE_INSERIMENTO_CONTRATTO)!=-1) { %> 
		<tr>
	  	    <td><% bp.getRipartizionePerAnno().writeFormLabel(out,"importo_complessivo"); %></td>
			<td colspan="3"><% bp.getRipartizionePerAnno().writeFormInput(out,"importo_complessivo"); %></td>
		</tr>	
	<% } %>
</table>
&nbsp;
&nbsp;
&nbsp;
&nbsp;
<fieldset class="fieldset">
<legend class="GroupLabel">Riepilogo Importi</legend>
<table>
<% if (existsVariazioni) {%>
  <tr>
  	<td colspan=3>&nbsp;</td>
    <td align="center"><span class="FormLabel">Iniziale</span></td>
	<td>&nbsp;</td>
    <td align="center"><span class="FormLabel">Variazioni</span></td>
  </tr>
<% } %>
  <tr>         
    <td><span class="FormLabel">Totale Procedura</span></td>
    <td><% bp.getController().writeFormInput(out,"importo_complessivo_procedura");%></td>
<% if (existsVariazioni) {%>
    <td> = </td>
    <td><% bp.getController().writeFormInput(out,"importo_complessivo_iniziale");%></td>
	<td> + </td>
    <td><% bp.getController().writeFormInput(out,"importo_complessivo_variazioni");%></td>
<% } else {%>
	<td>-</td>
<% } %>
  </tr>                     	
<% if (existsVariazioni) {%>
  <tr>         
  	<td>&nbsp;</td>
    <td align="right"> - </td>
    <td colspan=4>&nbsp;</td>
  </tr>                     	
<% } %>
  <tr>         
    <td><span class="FormLabel">Ripartiti</span></td>
    <td><% bp.getController().writeFormInput(out,"im_complessivo_ripartito");%></td>
<% if (existsVariazioni) {%>
    <td colspan=4>&nbsp;</td>
<% } else {%>
	<td>=</td>
<% } %>
  </tr>                     	
<% if (existsVariazioni) {%>
  <tr>         
  	<td>&nbsp;</td>
    <td align="right"> = </td>
    <td colspan=4>&nbsp;</td>
  </tr>                     	
<% } %>
  <tr>         
    <td><span class="FormLabel" style="color:red">da Ripartire</span></td>
    <td><% bp.getController().writeFormInput(out,"importo_da_ripartire");%></td>
    <td colspan=4>&nbsp;</td>
  </tr>                     	
</table>
</fieldset>
