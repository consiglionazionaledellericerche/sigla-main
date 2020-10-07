<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.compensi00.bp.*,
		it.cnr.contab.compensi00.docs.bulk.*,
		it.cnr.contab.anagraf00.tabrif.bulk.*"
%>

<% CRUDConguaglioBP bp = (CRUDConguaglioBP)BusinessProcess.getBusinessProcess(request);
	ConguaglioBulk conguaglio = (ConguaglioBulk)bp.getModel(); %>

<div class="Group" style="width:100%">
<table>
  <tr>
  	<td align="center"><span class="FormLabel">IRPEF</span></td>
  	<td align="center"><span class="FormLabel">Totale dovuto</span></td>
  	<td align="center"><span class="FormLabel">Totale goduto</span></td>
  	<td align="center"><span class="FormLabel">Conguaglio</span></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"im_irpef"); %></td>
	<td><% bp.getController().writeFormInput(out,"im_irpef_dovuto"); %></td>
	<td><% bp.getController().writeFormInput(out,"im_irpef_goduto"); %></td>
	<td><% bp.getController().writeFormInput(out,"im_irpef"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"im_addreg"); %></td>
	<td><% bp.getController().writeFormInput(out,"im_addreg_dovuto"); %></td>
	<td><% bp.getController().writeFormInput(out,"im_addreg_goduto"); %></td>
	<td><% bp.getController().writeFormInput(out,"im_addreg"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"im_addprov"); %></td>
	<td><% bp.getController().writeFormInput(out,"im_addprov_dovuto"); %></td>
	<td><% bp.getController().writeFormInput(out,"im_addprov_goduto"); %></td>
	<td><% bp.getController().writeFormInput(out,"im_addprov"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"im_addcom"); %></td>
	<td><% bp.getController().writeFormInput(out,"im_addcom_dovuto"); %></td>
	<td><% bp.getController().writeFormInput(out,"im_addcom_goduto"); %></td>
	<td><% bp.getController().writeFormInput(out,"im_addcom"); %></td>
  </tr>
  <tr>
	<td colspan=3 align="right"><% bp.getController().writeFormLabel(out,"totale_irpef"); %></td>
	<td><% bp.getController().writeFormInput(out,"totale_irpef"); %></td>
  </tr>
</table>
</div>
<div class="Group" style="width:100%">
<table>
  <tr>
  	<td align="center"><span class="FormLabel"></span></td>
  	<td align="center"><span class="FormLabel">Dovuto</span></td>
  	<td align="center"><span class="FormLabel">Goduto</span></td>
  	<td align="center"><span class="FormLabel">Conguaglio</span></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"im_bonus_irpef"); %></td>
	<td><% bp.getController().writeFormInput(out,"im_bonus_irpef_dovuto"); %></td>
	<td><% bp.getController().writeFormInput(out,"im_bonus_irpef_goduto"); %></td>
	<td><% bp.getController().writeFormInput(out,"im_bonus_irpef"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"im_credito_irpef"); %></td>
	<td><% bp.getController().writeFormInput(out,"im_credito_irpef_dovuto"); %></td>
	<td><% bp.getController().writeFormInput(out,"im_credito_irpef_goduto"); %></td>
	<td><% bp.getController().writeFormInput(out,"im_credito_irpef"); %></td>
  </tr>
</table>
</div>
<div class="Group" style="width:100%">
<table>
  <tr>
  	<td align="center"><span class="FormLabel">DEDUZIONI</span></td>
  	<td align="center"><span class="FormLabel">Totale dovuto</span></td>
  	<td align="center"><span class="FormLabel">Totale goduto</span></td>
  	<td align="center"><span class="FormLabel">Conguaglio</span></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"im_deduzione"); %></td>
	<td><% bp.getController().writeFormInput(out,"im_deduzione_dovuto"); %></td>
	<td><% bp.getController().writeFormInput(out,"im_deduzione_goduto"); %></td>
	<td><% bp.getController().writeFormInput(out,"im_deduzione"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"im_deduzione_family"); %></td>
	<td><% bp.getController().writeFormInput(out,"im_deduzione_family_dovuto"); %></td>
	<td><% bp.getController().writeFormInput(out,"im_deduzione_family_goduto"); %></td>
	<td><% bp.getController().writeFormInput(out,"im_deduzione_family"); %></td>
  </tr>
  
</table>
</div>
<div class="Group" style="width:100%">
<table>
  <tr>
  	<td align="center"><span class="FormLabel">DETRAZIONI</span></td>
  	<td align="center"><span class="FormLabel">Totale dovuto</span></td>
  	<td align="center"><span class="FormLabel">Totale goduto</span></td>
  	<td align="center"><span class="FormLabel">Conguaglio</span></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"detrazioni_la"); %></td>
	<td><% bp.getController().writeFormInput(out,"detrazioni_la_dovuto"); %></td>
	<td><% bp.getController().writeFormInput(out,"detrazioni_la_goduto"); %></td>
	<td><% bp.getController().writeFormInput(out,"detrazioni_la"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"detrazioni_pe"); %></td>
	<td><% bp.getController().writeFormInput(out,"detrazioni_pe_dovuto"); %></td>
	<td><% bp.getController().writeFormInput(out,"detrazioni_pe_goduto"); %></td>
	<td><% bp.getController().writeFormInput(out,"detrazioni_pe"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"detrazioni_co"); %></td>
	<td><% bp.getController().writeFormInput(out,"detrazioni_co_dovuto"); %></td>
	<td><% bp.getController().writeFormInput(out,"detrazioni_co_goduto"); %></td>
	<td><% bp.getController().writeFormInput(out,"detrazioni_co"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"detrazioni_fi"); %></td>
	<td><% bp.getController().writeFormInput(out,"detrazioni_fi_dovuto"); %></td>
	<td><% bp.getController().writeFormInput(out,"detrazioni_fi_goduto"); %></td>
	<td><% bp.getController().writeFormInput(out,"detrazioni_fi"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"detrazioni_al"); %></td>
	<td><% bp.getController().writeFormInput(out,"detrazioni_al_dovuto"); %></td>
	<td><% bp.getController().writeFormInput(out,"detrazioni_al_goduto"); %></td>
	<td><% bp.getController().writeFormInput(out,"detrazioni_al"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"detrazioni_rid_cuneo"); %></td>
	<td><% bp.getController().writeFormInput(out,"detrazioni_rid_cuneo_dovuto"); %></td>
	<td><% bp.getController().writeFormInput(out,"detrazioni_rid_cuneo_goduto"); %></td>
	<td><% bp.getController().writeFormInput(out,"detrazioni_rid_cuneo"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"totale_detrazioni"); %></td>
	<td><% bp.getController().writeFormInput(out,"totale_detrazioni_dovuto"); %></td>
	<td><% bp.getController().writeFormInput(out,"totale_detrazioni_goduto"); %></td>
	<td><% bp.getController().writeFormInput(out,"totale_detrazioni"); %></td>
  </tr>
</table>
</div>