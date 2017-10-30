<%@ page 
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
<% bp.getCrudDettagliSpese().writeHTMLTable(pageContext,(bp.isFlNuovoPdg()?"without_area":"default"),true,false,true,"100%","200px"); 
%>
<table border="0" cellspacing="0" cellpadding="2">	
<tr>
 <td align=left>
  <table border="0" cellspacing="0" cellpadding="2">	
	<% if (bp.isFlPdgCodlast()) { %> 
		<tr>
	  	  <td NOWRAP><% bp.getCrudDettagliSpese().writeFormLabel(out,"classificazione_codlast");%></td>
		  <td><% bp.getCrudDettagliSpese().writeFormInput(out,"classificazione_codlast");%>
		</tr>
	<% } else { %>
		<tr>
	  	  <td NOWRAP><% bp.getCrudDettagliSpese().writeFormLabel(out,"classificazione");%></td>
		  <td><% bp.getCrudDettagliSpese().writeFormInput(out,"classificazione");%>
		</tr>
	<% } %>
	<br>
	<tr>
	  <td NOWRAP><% bp.getCrudDettagliSpese().writeFormLabel(out,"label_gestione_decentrata");%></td>
	</tr>	
	<tr>
	  <td NOWRAP><% bp.getCrudDettagliSpese().writeFormLabel(out,"im_spese_gest_decentrata_int");%></td>
	  <td><% bp.getCrudDettagliSpese().writeFormInput(out,"im_spese_gest_decentrata_int");%></td>
	</tr>
	<tr>
	  <td NOWRAP><% bp.getCrudDettagliSpese().writeFormLabel(out,"im_spese_gest_decentrata_est");%></td>
	  <td><% bp.getCrudDettagliSpese().writeFormInput(out,"im_spese_gest_decentrata_est");%></td>
	</tr>
	<tr>
	  <td NOWRAP><% bp.getCrudDettagliSpese().writeFormLabel(out,"tot_gest_decentrata");%></td>
	  <td><% bp.getCrudDettagliSpese().writeFormInput(out,"tot_gest_decentrata");%></td>
	</tr>	
	<tr>
	  <td NOWRAP><% bp.getCrudDettagliSpese().writeFormLabel(out,"label_gestione_accentrata");%></td>
	</tr>	
	<tr>
	  <td NOWRAP><% bp.getCrudDettagliSpese().writeFormLabel(out,"im_spese_gest_accentrata_int");%></td>
	  <td><% bp.getCrudDettagliSpese().writeFormInput(out,"im_spese_gest_accentrata_int");%></td>
	</tr>
	<tr>
	  <td NOWRAP><% bp.getCrudDettagliSpese().writeFormLabel(out,"im_spese_gest_accentrata_est");%></td>
	  <td><% bp.getCrudDettagliSpese().writeFormInput(out,"im_spese_gest_accentrata_est");%></td>
	</tr>
	<tr>
	  <td NOWRAP><% bp.getCrudDettagliSpese().writeFormLabel(out,"tot_gest_accentrata");%></td>
	  <td><% bp.getCrudDettagliSpese().writeFormInput(out,"tot_gest_accentrata");%></td>
	</tr>	
	<tr>
	  <td NOWRAP><% bp.getCrudDettagliSpese().writeFormLabel(out,"tot_competenza_anno_in_corso");%></td>
	  <td><% bp.getCrudDettagliSpese().writeFormInput(out,"tot_competenza_anno_in_corso");%></td>
	</tr>	
	<tr>
	  <td NOWRAP><% bp.getCrudDettagliSpese().writeFormLabel(out,"im_spese_a2");%></td>
	  <td><% bp.getCrudDettagliSpese().writeFormInput(out,"im_spese_a2");%></td>
	</tr>	
	<tr>
	  <td NOWRAP><% bp.getCrudDettagliSpese().writeFormLabel(out,"im_spese_a3");%></td>
	  <td><% bp.getCrudDettagliSpese().writeFormInput(out,"im_spese_a3");%></td>
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
	<% if (bp.getParametriEnte().getFl_prg_pianoeco() && progetto.getFl_piano_economico()) {%>
	<tr>
		<%bp.getCrudDettagliSpese().writeFormField(out,"voce_piano"); %>				
	</tr>
	<% } %>
  </table>
 </td>
 <td align=right valign=top>
  <table style="border-style: inset;" border="1" cellspacing="0" cellpadding="2">	
	<tr>
	  <td colspan="3" align="center">&nbsp;</td>
	</tr>	
  
	<tr>
	  <td colspan="3" align="center"><% bp.getCrudDettagliSpese().writeFormLabel(out,"label_previsione_assestata_impegno");%></td>
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
