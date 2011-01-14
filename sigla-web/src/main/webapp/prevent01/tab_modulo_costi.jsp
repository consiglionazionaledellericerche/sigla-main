<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page 
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.prevent01.bp.*"
%>

<%
	CRUDDettagliModuloCostiBP bp = (CRUDDettagliModuloCostiBP)BusinessProcess.getBusinessProcess(request);
%>

<table border="0" cellspacing="0" cellpadding="2">
	<tr>
	  <td><% bp.getController().writeFormLabel(out,"im_costi_generali");%></td>
	  <td><% bp.getController().writeFormInput(out,"im_costi_generali");%></td>	  
	</tr>
	<tr><td colspan=2>
		<fieldset class="fieldset">
			<legend class="GroupLabel"><% bp.getController().writeFormLabel(out,"label_costi_figurativi");%></legend>
		    <table border="0" cellspacing="0" cellpadding="2">	
			<tr>
			  <td><% bp.getController().writeFormLabel(out,"im_cf_tfr");%></td>
			  <td><% bp.getController().writeFormInput(out,"im_cf_tfr");%><td>
			</tr>
			<tr>
			  <td><% bp.getController().writeFormLabel(out,"im_cf_tfr_det");%></td>
			  <td><% bp.getController().writeFormInput(out,"im_cf_tfr_det");%><td>
			</tr>			
			<tr>
			  <td><% bp.getController().writeFormLabel(out,"im_cf_amm_immobili");%></td>
			  <td><% bp.getController().writeFormInput(out,"im_cf_amm_immobili");%></td>
			</tr>
			<tr>
			  <td><% bp.getController().writeFormLabel(out,"im_cf_amm_attrezz");%></td>
			  <td><% bp.getController().writeFormInput(out,"im_cf_amm_attrezz");%></td>
			</tr>
			<tr>
			  <td><% bp.getController().writeFormLabel(out,"im_cf_amm_altro");%></td>
			  <td><% bp.getController().writeFormInput(out,"im_cf_amm_altro");%></td>
			</tr>
			<tr>
			  <td><% bp.getController().writeFormLabel(out,"tot_costi_figurativi");%></td>
			  <td><% bp.getController().writeFormInput(out,"tot_costi_figurativi");%></td>
			</tr>	
			</table>
		</fieldset>	
	</td></tr>	
	<tr>
	  <td><% bp.getController().writeFormLabel(out,"tot_costi");%></td>
	  <td><% bp.getController().writeFormInput(out,"tot_costi");%></td>
	</tr>	
</table>
