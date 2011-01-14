<%@ page 
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
  <div class="Group" style="width:100%">
  <table>
	<td width=18%><% bp.getController().writeFormLabel(out,"cd_regione_irap"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_regione_irap"); %></td>
	<td><% bp.getController().writeFormInput(out,"ds_regione_irap"); %></td>
	<td><% bp.getController().writeFormInput(out,null,"find_regione_irap", bp.isROFindDatiLiquidazione(),null, null); %></td>
  </table>
  </div>
  <% } %>

  <% if (compenso.isVisualizzaVoceIva()) { %>
  <div class="Group" style="width:100%">
  <table>
	<td width=18%><% bp.getController().writeFormLabel(out,"cd_voce_iva"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_voce_iva"); %></td>
	<td><% bp.getController().writeFormInput(out,"ds_voce_iva"); %></td>
	<td><% bp.getController().writeFormInput(out,null,"find_voce_iva", bp.isROFindDatiLiquidazione(),null, null); %></td>
  </table>
  </div>
  <% } %>

  <% if (compenso.isVisualizzaTipologiaRischio()) { %>
  <div class="Group" style="width:100%">
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

<div class="Group" style="width:100%">
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
  
</table>
</div>


<fieldset class="fieldset">
<legend class="GroupLabel">Detrazioni art. 13 TUIR</legend>
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"detrazioni_personali"); %></td>
	<td><% bp.getController().writeFormInput(out,null,"detrazioni_personali", !compenso.isSenzaCalcoli(),null, null); %></td>
	<td><% bp.getController().writeFormLabel(out,"detrazioni_la"); %></td>
	<td><% bp.getController().writeFormInput(out,null,"detrazioni_la", !compenso.isSenzaCalcoli(),null, null); %></td>
  </tr>
 </table>
 </fieldset>
  
 <fieldset class="fieldset">
<legend class="GroupLabel">Detrazioni per carichi di famiglia</legend>
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
 </fieldset>


<div class="Panel" style="width:100%">
<table width="100%">
  <tr>
	<td colspan=2><% JSPUtils.button(out,null,null,"Esegui Calcolo","javascript:submitForm('doEseguiCalcolo')",null,bp.isBottoneEseguiCalcoloEnabled());%></td>
  </tr>
</table>
</div>

<div class="Group" style="width:100%">
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