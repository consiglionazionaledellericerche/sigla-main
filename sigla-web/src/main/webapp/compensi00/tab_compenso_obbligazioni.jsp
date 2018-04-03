<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.compensi00.bp.*,
		it.cnr.contab.compensi00.docs.bulk.*"
%>

<%	CRUDCompensoBP bp = (CRUDCompensoBP)BusinessProcess.getBusinessProcess(request);
	CompensoBulk compenso = (CompensoBulk)bp.getModel(); %>

<div class="Group card">
<% bp.getCompensoRigheController().writeHTMLTable(pageContext,"default",true,false,true,"100%","150px",true); %>
<table width="100%">
  <tr><td width="70%">
	<div class="card">
		<fieldset class="fieldset mb-2">
		<legend class="GroupLabel card-header text-primary">Impegno</legend>
		<table class="m-2 p-2">
		  <tr>
			<% bp.getCompensoRigheController().writeFormField(out,"cd_cds_obbligazione"); %>
		  </tr>
		  <tr>
			<% bp.getCompensoRigheController().writeFormField(out,"esercizio_ori_obbligazione"); %>
		  </tr>
		  <tr>
			<% bp.getCompensoRigheController().writeFormField(out,"pg_obbligazione"); %>
		  </tr>
		  <tr>
			<% bp.getCompensoRigheController().writeFormField(out,"pg_obbligazione_scadenzario"); %>
		  </tr>
		  <tr>
			<% bp.getCompensoRigheController().writeFormField(out,"scadenza_dt_scadenza"); %>
		  </tr>
		  <tr>
			<% bp.getCompensoRigheController().writeFormField(out,"scadenza_im_scadenza"); %>
		  </tr>
		  <tr>
			<% bp.getCompensoRigheController().writeFormField(out,"im_totale_riga_compenso"); %>
		  </tr>
		  <tr>
			<% bp.getCompensoRigheController().writeFormField(out,"scadenza_ds_scadenza"); %>
		  </tr>
		  <tr>
			<% bp.getCompensoRigheController().writeFormField(out,"cig"); %>
		  </tr>
		  <tr>
			<td colspan="2">
				<% if (compenso.isStatoCompensoEseguiCalcolo()) { %>
					<span class="FormLabel" style="color:red">E' necessario eseguire il calcolo prima di continuare</span>
				<% } %>
			</td>
		  </tr>
		</table>
		</fieldset>
	</div>
  </td>
  <td>
	<div class="card">
		<fieldset class="fieldset mb-2">
		<legend class="GroupLabel card-header text-primary">Riepilogo</legend>
		<table class="m-2 p-2">
		  <tr>         
		    <td><span class="FormLabel">Compenso</span></td>
		    <td><% bp.getController().writeFormInput(out,"im_totale_compenso");%></td>
			<td>-</td>
		  </tr>                     	
		  <tr>         
		    <td><span class="FormLabel">Impegnato</span></td>
		    <td><% bp.getController().writeFormInput(out,"im_totale_impegnato");%></td>
			<td>=</td>
		  </tr>                     	
		  <tr>         
		    <td><span class="FormLabel" style="color:red">da Impegnare</span></td>
		    <td><% bp.getController().writeFormInput(out,"im_totale_da_impegnare");%></td>
		    <td colspan=4>&nbsp;</td>
		  </tr>                     	
		</table>
		</fieldset>
	</div>
  </td></tr>
</table>	
</div>