<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.compensi00.bp.*,
		it.cnr.contab.compensi00.docs.bulk.*,
		it.cnr.contab.docamm00.bp.*"
%>

<% CRUDCompensoBP bp = (CRUDCompensoBP)BusinessProcess.getBusinessProcess(request);
	CompensoBulk compenso = (CompensoBulk)bp.getModel(); %>

<fieldset class="fieldset card">
<legend ACCESSKEY=G TABINDEX=1 class="GroupLabel card-header text-primary">Documento Principale</legend>
<div class="Panel">
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"pgDocContPrincipale"); %></td>
	<td><% bp.getController().writeFormInput(out,"pgDocContPrincipale"); %></td>
	<td><% bp.getController().writeFormInput(out,"dsDocContPrincipale"); %></td>
	<td><% it.cnr.jada.util.jsp.Button.write(out,
				bp.getParentRoot().isBootstrap()?"fa fa-fw fa-list text-primary":"img/uncheckall16.gif",
				bp.getParentRoot().isBootstrap()?"fa fa-fw fa-list text-primary":"img/uncheckall16.gif",
				null,
				"javascript:submitForm('doVisualizzaDocContPrincipale')",
				null,
				"Visualizza documento contabile",
				bp.isBottoneVisualizzaDocContPrincipaleEnabled(), 
				bp.getParentRoot().isBootstrap()); %></td>
  </tr>
</table>
</div>
</fieldset>
<br>
<div class="Panel" style="width:100%">
<% bp.getDocContAssociatiCRUDController().writeHTMLTable(
		pageContext,
		null,
		false,
		false,
		false,
		"100%",
		"150px",
		true); %>
</div>

<br>

<% if (compenso.isDaMissione()) { %>
<fieldset class="fieldset card">
<legend ACCESSKEY=G TABINDEX=1 class="GroupLabel card-header text-primary">Missione associata</legend>
<div class="Panel">
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"pg_missione"); %></td>
	<td><% bp.getController().writeFormInput(out,"pg_missione"); %></td>
  </tr>
</table>
</div>
</fieldset>
<% } %>

<% if (compenso.isDaConguaglio()) { %>
<fieldset class="fieldset card">
<legend ACCESSKEY=G TABINDEX=1 class="GroupLabel card-header text-primary">Conguaglio associato</legend>
<div class="Panel">
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"pg_conguaglio"); %></td>
	<td><% bp.getController().writeFormInput(out,"pg_conguaglio"); %></td>
  </tr>
</table>
</div>
</fieldset>
<% } %>

<% if (compenso.isDaMinicarriera()) { %>
<fieldset class="fieldset card">
<legend ACCESSKEY=G TABINDEX=1 class="GroupLabel card-header text-primary">Minicarriera associata</legend>
<div class="Panel" style="width:100%">
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"pg_minicarriera"); %></td>
	<td><% bp.getController().writeFormInput(out,"pg_minicarriera"); %></td>
  </tr>
</table>
</div>
</fieldset>
<% } %>