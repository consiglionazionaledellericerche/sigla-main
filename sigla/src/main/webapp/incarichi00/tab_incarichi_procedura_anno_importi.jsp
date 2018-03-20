<%@ page pageEncoding="UTF-8"
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
<div class="Group card p-2 mb-2 sigla-mb-2">
<table class="Panel w-100 d-flex">
	<tr><% bp.getRipartizionePerAnno().writeFormField(out,"esercizio_limite"); %></tr>
	<tr><% bp.getRipartizionePerAnno().writeFormField(out,"importo_iniziale"); %></tr>
	<% if (model.getFaseProcesso().compareTo(Incarichi_proceduraBulk.FASE_INSERIMENTO_CONTRATTO)!=-1) { %> 
		<tr><% bp.getRipartizionePerAnno().writeFormField(out,"importo_complessivo"); %></tr>	
	<% } %>
</table>
&nbsp;
&nbsp;
&nbsp;
&nbsp;
<div class="card">
<fieldset class="fieldset mb-2">
<legend class="GroupLabel card-header text-primary">Riepilogo Importi</legend>
<table class="m-2 p-2">
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
</div>
