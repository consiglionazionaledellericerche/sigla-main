<%@ page 
	import="it.cnr.jada.action.*,
  	    it.cnr.jada.UserContext,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.config00.bp.*,
		it.cnr.contab.config00.bulk.*"
%>

<%
	CRUDConfigParametriCnrBP bp = (CRUDConfigParametriCnrBP)BusinessProcess.getBusinessProcess(request);
	UserContext uc = HttpActionContext.getUserContext(session);
	boolean isLivelloPdgDecisionaleSpeEnabled = !bp.isLivelloPdgDecisionaleSpeEnabled(uc, (Parametri_cnrBulk)bp.getModel());
	boolean isLivelloPdgDecisionaleEtrEnabled = !bp.isLivelloPdgDecisionaleEtrEnabled(uc, (Parametri_cnrBulk)bp.getModel());
	boolean isLivelloPdgContrattazioneSpeEnabled = !bp.isLivelloPdgContrattazioneSpeEnabled(uc, (Parametri_cnrBulk)bp.getModel());
%>
	<table class="Panel">
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"esercizio");%>
			</TD><TD>
			<% bp.getController().writeFormInput(out,"esercizio");%>
		</TD></TR>
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"tipo_rapporto");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"tipo_rapporto");%>
		</TD></TR>
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"importo_franchigia_occa");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"importo_franchigia_occa");%>
		</TD></TR>
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"fl_versamenti_cori");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"fl_versamenti_cori");%>
		</TD></TR>
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"versamenti_cori_giorno");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"versamenti_cori_giorno");%>
		</TD></TR>
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"tipo_rapporto_prof");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"tipo_rapporto_prof");%>
		</TD></TR>
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"livello_pdg_decis_spe");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"default","livello_pdg_decis_spe", isLivelloPdgDecisionaleSpeEnabled, null, null);%>
		</TD></TR>
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"livello_pdg_decis_etr");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"default","livello_pdg_decis_etr", isLivelloPdgDecisionaleEtrEnabled, null, null);%>
		</TD></TR>
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"livello_contratt_pdg_spe");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"default","livello_contratt_pdg_spe", isLivelloPdgContrattazioneSpeEnabled, null, null);%>
		</TD></TR>
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"fl_regolamento_2006");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"fl_regolamento_2006");%>
		</TD></TR>		
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"fl_diaria_miss_italia");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"fl_diaria_miss_italia");%>
		</TD></TR>		
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"fl_motivazione_su_imp");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"fl_motivazione_su_imp");%>
		</TD></TR>		
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"importo_max_imp");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"importo_max_imp");%>
		</TD></TR>	
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"fl_deduzione_irpef");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"fl_deduzione_irpef");%>
		</TD></TR>		
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"fl_deduzione_family");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"fl_deduzione_family");%>
		</TD></TR>		
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"fl_detrazioni_altre");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"fl_detrazioni_altre");%>
		</TD></TR>		
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"fl_detrazioni_family");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"fl_detrazioni_family");%>
		</TD></TR>			
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"fl_siope");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"fl_siope");%>
		</TD></TR>			
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"ricerca_prof_int_giorni_pubbl");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"ricerca_prof_int_giorni_pubbl");%>
		</TD></TR>			
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"ricerca_prof_int_giorni_scad");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"ricerca_prof_int_giorni_scad");%>
		</TD></TR>
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"fl_incarico");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"fl_incarico");%>
		</TD></TR>
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"fl_obb_intrastat");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"fl_obb_intrastat");%>
		</TD></TR>		
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"fl_cup");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"fl_cup");%>
		</TD></TR>	
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"fl_siope_cup");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"fl_siope_cup");%>
		</TD></TR>		
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"livello_pdg_cofog");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"default","livello_pdg_cofog", isLivelloPdgDecisionaleSpeEnabled, null, null);%>
		</TD></TR>
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"fl_credito_irpef");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"fl_credito_irpef");%>
		</TD></TR>			
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"data_stipula_contratti");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"data_stipula_contratti");%>
		</TD></TR>		
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"livello_eco");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"default","livello_eco", false, null, null);%>
		</TD></TR>
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"livello_pat");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"default","livello_pat", false, null, null);%>
		</TD></TR>			
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"fl_pdg_codlast");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"fl_pdg_codlast");%>
		</TD></TR>					
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"fl_pdg_contrattazione");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"fl_pdg_contrattazione");%>
		</TD></TR>					
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"fl_pdg_quadra_fonti_esterne");%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,"fl_pdg_quadra_fonti_esterne");%>
		</TD></TR>					
	</table>