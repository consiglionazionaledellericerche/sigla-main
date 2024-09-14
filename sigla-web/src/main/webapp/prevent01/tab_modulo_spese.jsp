<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.progettiric00.core.bulk.*,
		it.cnr.contab.prevent01.bulk.*,
		it.cnr.contab.prevent01.bp.*"
%>

<%
	CRUDDettagliModuloCostiBP bp = (CRUDDettagliModuloCostiBP)BusinessProcess.getBusinessProcess(request);
	Progetto_sipBulk progetto = ((Pdg_modulo_costiBulk)bp.getModel()).getPdg_modulo().getProgetto();
%>
<% bp.getCrudDettagliSpese().writeHTMLTable(pageContext,(bp.isFlNuovoPdg()?"without_area":"default"),true,false,true,"100%","auto;max-height:60vh");
%>
<table border="0" cellspacing="0" cellpadding="2" class="w-100">
<tr>
 <td align=left class="w-75">
  <table border="0" cellspacing="0" cellpadding="2" class="w-100">
	<tr>
	<% if (bp.isFlPdgCodlast()) { 
	  	  bp.getCrudDettagliSpese().writeFormField(out,"classificazione_codlast");
	   } else {
  	      bp.getCrudDettagliSpese().writeFormField(out,"classificazione");
	   } %>
	</tr>
	<br>
	<tr>
	  <td><span class="FormLabel font-weight-bold text-primary"><% bp.getCrudDettagliSpese().writeFormLabel(out,"label_gestione_decentrata");%></span></td>
	</tr>	
	<tr>
	  <% bp.getCrudDettagliSpese().writeFormField(out,"im_spese_gest_decentrata_int");%>
	</tr>
	<tr>
	  <% bp.getCrudDettagliSpese().writeFormField(out,"im_spese_gest_decentrata_est");%>
	</tr>
	</div>
	<tr>
	  <td><span class="FormLabel font-weight-bold"><% bp.getCrudDettagliSpese().writeFormLabel(out,"tot_gest_decentrata");%></span></td>
	  <td><% bp.getCrudDettagliSpese().writeFormInput(out,"tot_gest_decentrata");%></td>
	</tr>	
	<tr>
	  <td><span class="FormLabel font-weight-bold text-primary"><% bp.getCrudDettagliSpese().writeFormLabel(out,"label_gestione_accentrata");%></span></td>
	</tr>	
	<tr>
	  <% bp.getCrudDettagliSpese().writeFormField(out,"im_spese_gest_accentrata_int");%>
	</tr>
	<tr>
	  <% bp.getCrudDettagliSpese().writeFormField(out,"im_spese_gest_accentrata_est");%>
	</tr>
	<tr>
	  <td><span class="FormLabel font-weight-bold"><% bp.getCrudDettagliSpese().writeFormLabel(out,"tot_gest_accentrata");%></span></td>
	  <td><% bp.getCrudDettagliSpese().writeFormInput(out,"tot_gest_accentrata");%></td>
	</tr>	
	<tr>
	  <td><span class="FormLabel font-weight-bold"><% bp.getCrudDettagliSpese().writeFormLabel(out,"tot_competenza_anno_in_corso");%></span></td>
	  <td><% bp.getCrudDettagliSpese().writeFormInput(out,"tot_competenza_anno_in_corso");%></td>
	</tr>	
	<tr>
	  <% bp.getCrudDettagliSpese().writeFormField(out,"im_spese_a2");%>
	</tr>	
	<tr>
	  <% bp.getCrudDettagliSpese().writeFormField(out,"im_spese_a3");%>
	</tr>	
 <% if (!bp.isFlNuovoPdg()){%>
	<tr>
	  <% bp.getCrudDettagliSpese().writeFormField(out,"area");%>
	</tr>
 <% } else{%>
 	<tr>
	  <% bp.getCrudDettagliSpese().writeFormField(out,"pdgMissione");%>
	</tr>
	<% }%>	
 <% if (bp.isCofogObb()){%>
	<tr>
	  <% bp.getCrudDettagliSpese().writeFormField(out,"cofog");%>
	</tr>
 <% } %>
	<% if (bp.getParametriEnte().getFl_prg_pianoeco() && progetto.isPianoEconomicoRequired()) {%>
	<tr>
		<%bp.getCrudDettagliSpese().writeFormField(out,"voce_piano"); %>				
	</tr>
	<% } %>
  </table>
 </td>
 
 <td align=middle valign=top class="w-25">
  <table style="border-style: inset;" border="1" cellspacing="0" cellpadding="2" class="w-100 card">
	<tr>
	  <td colspan="3" align="center">&nbsp;</td>
	</tr>	
  
	<tr>
	  <td colspan="3" align="center"><span class="FormLabel font-weight-bold text-primary"><% bp.getCrudDettagliSpese().writeFormLabel(out,"label_previsione_assestata_impegno");%></span></td>
	</tr>	
	<tr>
	  <td align="center"><% bp.getCrudDettagliSpese().writeFormLabel(out,"prev_ass_imp_int");%></td>
	  <td align="center"><% bp.getCrudDettagliSpese().writeFormLabel(out,"prev_ass_imp_est");%></td>
	  <td align="center"><% bp.getCrudDettagliSpese().writeFormLabel(out,"tot_prev_ass_imp");%></td>
	</tr>	
	<tr>
	  <td><% bp.getCrudDettagliSpese().writeFormInput(out,"prev_ass_imp_int");%></td>
	  <td><% bp.getCrudDettagliSpese().writeFormInput(out,"prev_ass_imp_est");%></td>
	  <td><% bp.getCrudDettagliSpese().writeFormInput(out,"tot_prev_ass_imp");%></td>
	</tr>	  
  </table>    	
</td></tr>  
</table>
