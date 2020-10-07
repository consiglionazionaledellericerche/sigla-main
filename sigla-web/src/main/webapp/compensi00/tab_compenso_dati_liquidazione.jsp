<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.compensi00.bp.*,
		it.cnr.contab.compensi00.docs.bulk.*"
%>

<%	CRUDCompensoBP bp = (CRUDCompensoBP)BusinessProcess.getBusinessProcess(request);
	CompensoBulk compenso = (CompensoBulk)bp.getModel();
%>

<% if (!bp.isSearching()) { %>
  <% if (compenso.isVisualizzaRegioneIrap()) { %>
  <div class="Group card">
  <table>
	<td width=18%><% bp.getController().writeFormLabel(out,"cd_regione_irap"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_regione_irap"); %></td>
	<td><% bp.getController().writeFormInput(out,"ds_regione_irap"); %></td>
	<td><% bp.getController().writeFormInput(out,null,"find_regione_irap", bp.isROFindDatiLiquidazione(),null, null); %></td>
  </table>
  </div>
  <% } %>

  <% if (compenso.isVisualizzaVoceIva()) { %>
  <div class="Group card">
  <table>
	<td width=18%><% bp.getController().writeFormLabel(out,"cd_voce_iva"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_voce_iva"); %></td>
	<td><% bp.getController().writeFormInput(out,"ds_voce_iva"); %></td>
	<td><% bp.getController().writeFormInput(out,null,"find_voce_iva", bp.isROFindDatiLiquidazione(),null, null); %></td>
  </table>
  </div>
  <% } %>

  <% if (compenso.isVisualizzaTipologiaRischio()) { %>
  <div class="Group card">
  <table>
  <tr>
	<td width=18%><% bp.getController().writeFormLabel(out,"cd_tipologia_rischio"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_tipologia_rischio"); %></td>
	<td><% bp.getController().writeFormInput(out,"ds_tipologia_rischio"); %></td>
	<td><% bp.getController().writeFormInput(out,null,"find_tipologia_rischio", bp.isROFindDatiLiquidazione(),null, null); %></td>
  </tr>
  <tr>
	<td width=18%><% bp.getController().writeFormLabel(out,"imponibile_inail"); %></td>
	<td colspan=3><% bp.getController().writeFormInput(out,null,"imponibile_inail",bp.isROFindDatiLiquidazione(),null,null); %></td>
  <tr>
  </table>
  </div>
  <% } %>
<% } %>

  <% if (!bp.isSearching() && !compenso.isCompensoSoloInailEnte()) { %>
<div class="Group card">
<table>
  <tr>
	<td width="25%"><% bp.getController().writeFormLabel(out,"im_lordo_percipiente"); %></td>
	<td><% bp.getController().writeFormInput(out,null,"im_lordo_percipiente",compenso.isAssociatoADocumento(),null,"");%></td>
	<td width="25%"><% bp.getController().writeFormLabel(out,"im_netto_percipiente"); %></td>
	<td><% bp.getController().writeFormInput(out,"im_netto_percipiente"); %></td>
  </tr>
  <tr>
	<td width="25%"><% bp.getController().writeFormLabel(out,"im_no_fiscale"); %></td>
	<td><% bp.getController().writeFormInput(out,"im_no_fiscale"); %></td>
	<td width="25%"><% bp.getController().writeFormLabel(out,"im_netto_da_trattenere"); %></td>
	<td><% bp.getController().writeFormInput(out,"im_netto_da_trattenere"); %></td>
  </tr>
  <tr>
	<td width="25%"><% bp.getController().writeFormLabel(out,"quota_esente"); %></td>
	<td><% bp.getController().writeFormInput(out,"quota_esente"); %></td>
  </tr>
  <tr>
	<td width="25%"><% bp.getController().writeFormLabel(out,"quota_esente_no_iva"); %></td>
	<td><% bp.getController().writeFormInput(out,"quota_esente_no_iva"); %></td>
  </tr>
  <tr>
	<td width="25%"><% bp.getController().writeFormLabel(out,"quota_esente_inps"); %></td>
	<td><% bp.getController().writeFormInput(out,"quota_esente_inps"); %></td>	
  </tr>
  <% } %>
  
</table>
</div>

<fieldset class="fieldset card">
<legend class="GroupLabel card-header text-primary">Detrazioni art. 13 TUIR</legend>
<div class="Panel">
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"detrazioni_personali"); %></td>
	<td><% bp.getController().writeFormInput(out,null,"detrazioni_personali", !compenso.isSenzaCalcoli(),null, null); %></td>
	<td><% bp.getController().writeFormLabel(out,"detrazioni_la"); %></td>
	<td><% bp.getController().writeFormInput(out,null,"detrazioni_la", !compenso.isSenzaCalcoli(),null, null); %></td>
  </tr>
 </table>
</div>
</fieldset>

<fieldset class="fieldset card">
<legend class="GroupLabel card-header text-primary">Detrazioni per carichi di famiglia</legend>
<div class="Panel">
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"detrazione_coniuge"); %></td>
	<td><% bp.getController().writeFormInput(out,null,"detrazione_coniuge", !compenso.isSenzaCalcoli(),null, null); %></td>
	<td><% bp.getController().writeFormLabel(out,"detrazione_figli"); %></td>
	<td><% bp.getController().writeFormInput(out,null,"detrazione_figli", !compenso.isSenzaCalcoli(),null, null); %></td>
	<td><% bp.getController().writeFormLabel(out,"detrazione_altri"); %></td>
	<td><% bp.getController().writeFormInput(out,null,"detrazione_altri", !compenso.isSenzaCalcoli(),null, null); %></td>
  </tr>
</table>
</div>
</fieldset>

<fieldset class="fieldset card">
<legend class="GroupLabel card-header text-primary">Detrazione per riduzione cuneo fiscale DL 3/2020</legend>
<div class="Panel">
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"detrazioneRiduzioneCuneo"); %></td>
	<td><% bp.getController().writeFormInput(out,null,"detrazioneRiduzioneCuneo", !compenso.isSenzaCalcoli(),null, null); %></td>
  </tr>
</table>
</div>
</fieldset>

<div class="Panel card">
<table>
  <tr>
	<td colspan=2>
	<% JSPUtils.button(out,
			bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-calculator text-primary" : null,
			bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-calculator text-primary" : null,
			"Esegui Calcolo",
			"javascript:submitForm('doEseguiCalcolo')",
			"btn-secondary btn-outline-secondary btn-title text-primary",
			bp.isBottoneEseguiCalcoloEnabled(), 
			bp.getParentRoot().isBootstrap());%>
	</td>
  </tr>
</table>
</div>

<div class="Group card">
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_linea_attivita_genrc"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_linea_attivita_genrc"); %>
		<% bp.getController().writeFormInput(out,"ds_linea_attivita"); %>
		<% bp.getController().writeFormInput(out,"find_linea_attivita"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_cdr_genrc"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_cdr_genrc"); %></td>
  </tr>
</table>
</div>