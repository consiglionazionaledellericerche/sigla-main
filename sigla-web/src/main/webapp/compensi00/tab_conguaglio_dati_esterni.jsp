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

<fieldset>
<legend ACCESSKEY=G TABINDEX=1 style="font-weight:bold; font-family:sans-serif; font-size:14px; color:blue">Dati del CUD da conguagliare (Datore di lavoro diverso dal CNR)</legend>
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"codice_fiscale_esterno"); %></td>
	<td><% bp.getController().writeFormInput(out,"codice_fiscale_esterno"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dt_da_competenza_esterno"); %></td>
	<td><% bp.getController().writeFormInput(out,"dt_da_competenza_esterno"); %></td>
	<td><% bp.getController().writeFormLabel(out,"dt_a_competenza_esterno"); %></td>
	<td><% bp.getController().writeFormInput(out,"dt_a_competenza_esterno"); %></td>
  </tr>  
  <tr>
	<td><% bp.getController().writeFormLabel(out,"imponibile_fiscale_esterno"); %></td>
	<td><% bp.getController().writeFormInput(out,"imponibile_fiscale_esterno"); %></td>
	<td><% bp.getController().writeFormLabel(out,"im_irpef_esterno"); %></td>
	<td><% bp.getController().writeFormInput(out,"im_irpef_esterno"); %></td>
  </tr>
</table>
</fieldset>

<fieldset>
<legend ACCESSKEY=G TABINDEX=1 style="font-weight:bold; font-family:sans-serif; font-size:14px; color:blue">Addizionali</legend>
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"im_addreg_esterno"); %></td>
	<td><% bp.getController().writeFormInput(out,"im_addreg_esterno"); %></td>
	<td><% bp.getController().writeFormLabel(out,"im_addcom_esterno"); %></td>
	<td><% bp.getController().writeFormInput(out,"im_addcom_esterno"); %></td>
  </tr>
    <tr>
   	<td><% bp.getController().writeFormLabel(out,"ds_comune_addcom_esterno");%></td>
  	<td colspan=2><% bp.getController().writeFormInput(out,"ds_comune_addcom_esterno");%>
	    <% bp.getController().writeFormInput(out,"find_comune_esterno");%></td>
	<td><% bp.getController().writeFormLabel(out,"ds_provincia_esterno");%></td>
	<td><% bp.getController().writeFormInput(out,"ds_provincia_esterno");%></td>
  </tr> 
</table>
</fieldset>

<fieldset>
<legend ACCESSKEY=G TABINDEX=1 style="font-weight:bold; font-family:sans-serif; font-size:14px; color:blue">Detrazioni</legend>
<legend ACCESSKEY=G TABINDEX=1 style="font-weight:bold; font-family:sans-serif; font-size:12px; color:blue">- Personali</legend>
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"detrazioni_pe_esterno"); %></td>
	<td><% bp.getController().writeFormInput(out,"detrazioni_pe_esterno"); %></td>
  </tr>
</table>
<legend ACCESSKEY=G TABINDEX=1 style="font-weight:bold; font-family:sans-serif; font-size:12px; color:blue">- Per Carichi di famiglia</legend>
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"detrazioni_co_esterno"); %></td>
	<td><% bp.getController().writeFormInput(out,"detrazioni_co_esterno"); %></td>
	<td><% bp.getController().writeFormLabel(out,"detrazioni_fi_esterno"); %></td>
	<td><% bp.getController().writeFormInput(out,"detrazioni_fi_esterno"); %></td>
	<td><% bp.getController().writeFormLabel(out,"detrazioni_al_esterno"); %></td>
	<td><% bp.getController().writeFormInput(out,"detrazioni_al_esterno"); %></td>
	<td><% bp.getController().writeFormLabel(out,"detrazioni_rid_cuneo_esterno"); %></td>
	<td><% bp.getController().writeFormInput(out,"detrazioni_rid_cuneo_esterno"); %></td>
  </tr>
</table>
</fieldset>

<div class="Panel" style="width:100%">
<table>
  <tr>
	<td><% JSPUtils.button(out,null,null,"Crea Conguaglio","javascript:submitForm('doCreaCompensoConguaglio')",null,bp.isBottoneCreaCompensoConguaglioEnabled(), bp.getParentRoot().isBootstrap());%></td>
  </tr>
</table>
</div>