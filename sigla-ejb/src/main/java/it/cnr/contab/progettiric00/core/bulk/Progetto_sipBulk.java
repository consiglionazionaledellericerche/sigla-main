/*
 * Created on Oct 25, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.progettiric00.core.bulk;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.config00.sto.bulk.DipartimentoBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk;
import it.cnr.contab.prevent01.bulk.Pdg_missioneBulk;
import it.cnr.contab.prevent01.bulk.Pdg_programmaBulk;
import it.cnr.contab.progettiric00.tabrif.bulk.Tipo_progettoBulk;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Progetto_sipBulk extends ProgettoBase {
	/**
	 * 
	 */
	private Unita_organizzativaBulk unita_organizzativa;
	private TerzoBulk responsabile;
	protected Progetto_sipBulk progettopadre;
	private DipartimentoBulk dipartimento;
	private Tipo_progettoBulk tipo;
	private DivisaBulk divisa;
	private Pdg_programmaBulk pdgProgramma;
	private Pdg_missioneBulk pdgMissione;
	private Progetto_other_fieldBulk otherField;
	
	public Progetto_sipBulk() {
		super();
	}

	/**
	 * @param pg_progetto
	 */
	public Progetto_sipBulk(java.lang.Integer esercizio,java.lang.Integer pg_progetto,java.lang.String tipo_fase) {
		super(esercizio,pg_progetto,tipo_fase);
	}

	/**
	 * @return
	 */
	public DipartimentoBulk getDipartimento() {
		return dipartimento;
	}

	/**
	 * @return
	 */
	public Progetto_sipBulk getProgettopadre() {
		return progettopadre;
	}

	/**
	 * @return
	 */
	public TerzoBulk getResponsabile() {
		return responsabile;
	}

	/**
	 * @return
	 */
	public Unita_organizzativaBulk getUnita_organizzativa() {
		return unita_organizzativa;
	}

	/**
	 * @param bulk
	 */
	public void setDipartimento(DipartimentoBulk bulk) {
		dipartimento = bulk;
	}

	/**
	 * @param bulk
	 */
	public void setProgettopadre(Progetto_sipBulk bulk) {
		progettopadre = bulk;
	}

	/**
	 * @param bulk
	 */
	public void setResponsabile(TerzoBulk bulk) {
		responsabile = bulk;
	}

	/**
	 * @param bulk
	 */
	public void setUnita_organizzativa(Unita_organizzativaBulk bulk) {
		unita_organizzativa = bulk;
	}
	public java.lang.Integer getCd_responsabile_terzo() {
		it.cnr.contab.anagraf00.core.bulk.TerzoBulk responsabile = this.getResponsabile();
		if (responsabile == null)
			return null;
		return responsabile.getCd_terzo();
	}
	public java.lang.String getCd_unita_organizzativa() {
		it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = this.getUnita_organizzativa();
		if (unita_organizzativa == null)
			return null;
		return unita_organizzativa.getCd_unita_organizzativa();
	}
	public void setCd_responsabile_terzo(java.lang.Integer cd_responsabile_terzo) {
		this.getResponsabile().setCd_terzo(cd_responsabile_terzo);
	}
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
		this.getUnita_organizzativa().setCd_unita_organizzativa(cd_unita_organizzativa);
	}
	public java.lang.Integer getPg_progetto_padre() {
		it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk progettopadre = this.getProgettopadre();
		if (progettopadre == null)
			return null;
		return progettopadre.getPg_progetto();
	}
	public Integer getEsercizio_progetto_padre() {
		it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk progettopadre = this.getProgettopadre();
		if (progettopadre == null)
			return null;
		return progettopadre.getEsercizio();
	}
	public String getTipo_fase_progetto_padre() {
		it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk progettopadre = this.getProgettopadre();
		if (progettopadre == null)
			return null;
		return progettopadre.getTipo_fase();
	}
	/**
	 * Sets the pg_progetto_padre.
	 * @param pg_progetto_padre The pg_progetto_padre to set
	 */
	public void setPg_progetto_padre(java.lang.Integer progetto_padre) {
		it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk progettopadre = this.getProgettopadre();
		if (progettopadre == null)
			setProgettopadre(new Progetto_sipBulk());
		this.getProgettopadre().setPg_progetto(progetto_padre);
	}
    public void setEsercizio_progetto_padre(Integer esercizio_progetto_padre) {
		it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk progettopadre = this.getProgettopadre();
		if (progettopadre == null)
			setProgettopadre(new Progetto_sipBulk());
		this.getProgettopadre().setEsercizio(esercizio_progetto_padre);
    }
    public void setTipo_fase_progetto_padre(String tipo_fase_progetto_padre) {
		it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk progettopadre = this.getProgettopadre();
		if (progettopadre == null)
			setProgettopadre(new Progetto_sipBulk());
		this.getProgettopadre().setTipo_fase(tipo_fase_progetto_padre);
    }
	public java.lang.String getCd_dipartimento() {
		DipartimentoBulk dipartimento = this.getDipartimento();
		if (dipartimento == null)
			return null;
		return dipartimento.getCd_dipartimento();
	}
	public void setCd_dipartimento(java.lang.String cd_dipartimento) {
		this.getDipartimento().setCd_dipartimento(cd_dipartimento);
	}

	/**
	 * @return
	 */
	public Tipo_progettoBulk getTipo() {
		return tipo;
	}

	/**
	 * @param bulk
	 */
	public void setTipo(Tipo_progettoBulk bulk) {
		tipo = bulk;
	}
	public java.lang.String getCd_tipo_progetto() {
		Tipo_progettoBulk tipo = this.getTipo();
		if (tipo == null)
			return null;
		return tipo.getCd_tipo_progetto();
	}
	public void setCd_tipo_progetto(java.lang.String cd_tipo_fondo) {
		this.getTipo().setCd_tipo_progetto(cd_tipo_fondo);
	}

	/**
	 * @return
	 */
	public DivisaBulk getDivisa() {
		return divisa;
	}

	/**
	 * @param bulk
	 */
	public void setDivisa(DivisaBulk bulk) {
		divisa = bulk;
	}
	public java.lang.String getCd_divisa() {
		it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk divisa = this.getDivisa();
		if (divisa == null)
			return null;
		return divisa.getCd_divisa();
	}
	public void setCd_divisa(java.lang.String cd_divisa) {
		this.getDivisa().setCd_divisa(cd_divisa);
	}
	public boolean isProgetto(){
		return getLivello().equals(ProgettoBulk.LIVELLO_PROGETTO_PRIMO);
	}
	public boolean isCommessa(){
		return getLivello().equals(ProgettoBulk.LIVELLO_PROGETTO_SECONDO);
	}
	public boolean isModulo(){
		return getLivello().equals(ProgettoBulk.LIVELLO_PROGETTO_TERZO);
	}

	public Pdg_programmaBulk getPdgProgramma() {
		return pdgProgramma;
	}
	
	public void setPdgProgramma(Pdg_programmaBulk pdgProgramma) {
		this.pdgProgramma = pdgProgramma;
	}
	@Override
	public String getCd_programma() {
		Pdg_programmaBulk pdgProgramma = this.getPdgProgramma();
		if (pdgProgramma == null)
			return null;
		return pdgProgramma.getCd_programma();
	}
	
	@Override
	public void setCd_programma(String cd_programma) {
		this.getPdgProgramma().setCd_programma(cd_programma);
	}

	public Pdg_missioneBulk getPdgMissione() {
		return pdgMissione;
	}
	
	public void setPdgMissione(Pdg_missioneBulk pdgMissione) {
		this.pdgMissione = pdgMissione;
	}
	@Override
	public String getCd_missione() {
		Pdg_missioneBulk pdgMissione = this.getPdgMissione();
		if (pdgMissione == null)
			return null;
		return pdgMissione.getCd_missione();
	}
	
	@Override
	public void setCd_missione(String cd_missione) {
		this.getPdgMissione().setCd_missione(cd_missione);
	}

	public void setOtherField(Progetto_other_fieldBulk otherField) {
		this.otherField = otherField;
	}
	
	public Progetto_other_fieldBulk getOtherField() {
		return otherField;
	}

	public String getStato() {
		return Optional.ofNullable(this.getOtherField()).flatMap(el->Optional.ofNullable(el.getStato())).orElse(null);
	}
	
	public void setStato(String stato) {
		if (getOtherField()==null) {
			Progetto_other_fieldBulk bulk = new Progetto_other_fieldBulk(this.getPg_progetto());
			bulk.setToBeCreated();
			setOtherField(bulk); 
		}
		if (getStato()!=null && stato!=null && 
				getStato()!=stato) {
			getOtherField().setStato(stato);
			getOtherField().setToBeUpdated();	
		}
	}
	
	public TipoFinanziamentoBulk getTipoFinanziamento(){
		return Optional.ofNullable(this.getOtherField()).flatMap(el->Optional.ofNullable(el.getTipoFinanziamento())).orElse(null);
	}

	public void setTipoFinanziamento(TipoFinanziamentoBulk tipoFinanziamento) {
		if (getOtherField()==null) {
			Progetto_other_fieldBulk bulk = new Progetto_other_fieldBulk(this.getPg_progetto());
			bulk.setToBeCreated();
			setOtherField(bulk); 
		}
		if (getTipoFinanziamento()!=null && tipoFinanziamento!=null && 
				getTipoFinanziamento()!=tipoFinanziamento) {
			getOtherField().setTipoFinanziamento(tipoFinanziamento);
			getOtherField().setToBeUpdated();	
		}
	}
		
	@Override
	public Timestamp getDt_inizio() {
		return Optional.ofNullable(this.getOtherField()).flatMap(el->Optional.ofNullable(el.getDtInizio())).orElse(null);
	}
	
	@Override
	public void setDt_inizio(Timestamp dt_inizio) {
		if (getOtherField()==null) {
			Progetto_other_fieldBulk bulk = new Progetto_other_fieldBulk(this.getPg_progetto());
			bulk.setToBeCreated();
			setOtherField(bulk); 
		}
		if (getDt_inizio()!=null && dt_inizio!=null && 
				getDt_inizio()!=dt_inizio) {
			getOtherField().setDtInizio(dt_inizio);
			getOtherField().setToBeUpdated();	
		}
	}

	@Override
	public Timestamp getDt_fine() {
		return Optional.ofNullable(this.getOtherField()).flatMap(el->Optional.ofNullable(el.getDtFine())).orElse(null);
	}
	
	@Override
	public void setDt_fine(Timestamp dt_fine) {
		if (getOtherField()==null) {
			Progetto_other_fieldBulk bulk = new Progetto_other_fieldBulk(this.getPg_progetto());
			bulk.setToBeCreated();
			setOtherField(bulk); 
		}
		if (getDt_fine()!=null && dt_fine!=null && 
				getDt_fine()!=dt_fine) {
			getOtherField().setDtFine(dt_fine);
			getOtherField().setToBeUpdated();	
		}
	}
	
	@Override
	public Timestamp getDt_proroga() {
		return Optional.ofNullable(this.getOtherField()).flatMap(el->Optional.ofNullable(el.getDtProroga())).orElse(null);
	}
	
	@Override
	public void setDt_proroga(Timestamp dt_proroga) {
		if (getOtherField()==null) {
			Progetto_other_fieldBulk bulk = new Progetto_other_fieldBulk(this.getPg_progetto());
			bulk.setToBeCreated();
			setOtherField(bulk); 
		}
		if (getDt_proroga()!=null && dt_proroga!=null && 
				getDt_proroga()!=dt_proroga) {
			getOtherField().setDtProroga(dt_proroga);
			getOtherField().setToBeUpdated();	
		}
	}
	
	public BigDecimal getImFinanziato() {
		return Optional.ofNullable(this.getOtherField()).flatMap(el->Optional.ofNullable(el.getImFinanziato())).orElse(null);
	}
	
	public void setImFinanziato(BigDecimal imFinanziato) {
		if (getOtherField()==null) {
			Progetto_other_fieldBulk bulk = new Progetto_other_fieldBulk(this.getPg_progetto());
			bulk.setToBeCreated();
			setOtherField(bulk); 
		}
		if (getImFinanziato()!=null && imFinanziato!=null && 
				getImFinanziato()!=imFinanziato) {
			getOtherField().setImFinanziato(imFinanziato);
			getOtherField().setToBeUpdated();	
		}
	}

	public BigDecimal getImCofinanziato() {
		return Optional.ofNullable(this.getOtherField()).flatMap(el->Optional.ofNullable(el.getImCofinanziato())).orElse(null);
	}
	
	public void setImCofinanziato(BigDecimal imCofinanziato) {
		if (getOtherField()==null) {
			Progetto_other_fieldBulk bulk = new Progetto_other_fieldBulk(this.getPg_progetto());
			bulk.setToBeCreated();
			setOtherField(bulk); 
		}
		if (getImCofinanziato()!=null && imCofinanziato!=null && 
				getImCofinanziato()!=imCofinanziato) {
			getOtherField().setImCofinanziato(imCofinanziato);
			getOtherField().setToBeUpdated();	
		}
	}
}
