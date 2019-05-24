package it.cnr.contab.progettiric00.core.bulk;

import java.util.Dictionary;
import java.util.Optional;

import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.jada.bulk.OggettoBulk;

public class Progetto_rimodulazione_variazioneBulk extends OggettoBulk {
	private static final long serialVersionUID = 1L;

	public static final String TIPO_COMPETENZA = "COM";
	public static final String TIPO_RESIDUO = "RES";

	public final static Dictionary tiVariazioneKeys;
	public final static Dictionary statoVariazioneKeys;
	static {
		tiVariazioneKeys = new it.cnr.jada.util.OrderedHashtable();
		tiVariazioneKeys.put(TIPO_COMPETENZA,"Competenza");
		tiVariazioneKeys.put(TIPO_RESIDUO,"Residuo");
		
		statoVariazioneKeys = new it.cnr.jada.util.OrderedHashtable();
		statoVariazioneKeys.put(Pdg_variazioneBulk.STATO_PROPOSTA_PROVVISORIA,"Proposta Provvisoria");
		statoVariazioneKeys.put(Pdg_variazioneBulk.STATO_PROPOSTA_DEFINITIVA,"Proposta Definitiva");
		statoVariazioneKeys.put(Pdg_variazioneBulk.STATO_APPROVATA,"Approvata");
		statoVariazioneKeys.put(Pdg_variazioneBulk.STATO_APPROVAZIONE_FORMALE,"Approvazione Formale");
		statoVariazioneKeys.put(Pdg_variazioneBulk.STATO_RESPINTA,"Respinta");
		statoVariazioneKeys.put(Pdg_variazioneBulk.STATO_ANNULLATA,"Annullata");		
	};

	private Pdg_variazioneBulk variazioneCompetenza;
	
	private Var_stanz_resBulk variazioneResiduo;
	
	public Progetto_rimodulazione_variazioneBulk() {
		super();
	}

	public Pdg_variazioneBulk getVariazioneCompetenza() {
		return variazioneCompetenza;
	}

	public void setVariazioneCompetenza(Pdg_variazioneBulk variazioneCompetenza) {
		this.variazioneCompetenza = variazioneCompetenza;
	}

	public Var_stanz_resBulk getVariazioneResiduo() {
		return variazioneResiduo;
	}

	public void setVariazioneResiduo(Var_stanz_resBulk variazioneResiduo) {
		this.variazioneResiduo = variazioneResiduo;
	}
	
	public String getCdrVariazione(){
		if (Optional.ofNullable(getVariazioneCompetenza()).isPresent())
			return getVariazioneCompetenza().getCd_centro_responsabilita();
		if (Optional.ofNullable(getVariazioneResiduo()).isPresent())
			return getVariazioneResiduo().getCd_centro_responsabilita();
		return null;
	}

	public String getTipoVariazione(){
		if (Optional.ofNullable(getVariazioneCompetenza()).isPresent())
			return TIPO_COMPETENZA;
		if (Optional.ofNullable(getVariazioneResiduo()).isPresent())
			return TIPO_RESIDUO;
		return null;
	}

	public String getStatoVariazione(){
		if (Optional.ofNullable(getVariazioneCompetenza()).isPresent())
			return getVariazioneCompetenza().getStato();
		if (Optional.ofNullable(getVariazioneResiduo()).isPresent())
			return getVariazioneResiduo().getStato();
		return null;
	}
	
	public java.lang.Integer getEsercizioVariazione() {
		if (Optional.ofNullable(getVariazioneCompetenza()).isPresent())
			return getVariazioneCompetenza().getEsercizio();
		if (Optional.ofNullable(getVariazioneResiduo()).isPresent())
			return getVariazioneResiduo().getEsercizio();
		return null;
	}

	public java.lang.Long getPg_variazione() {
		if (Optional.ofNullable(getVariazioneCompetenza()).isPresent())
			return getVariazioneCompetenza().getPg_variazione_pdg();
		if (Optional.ofNullable(getVariazioneResiduo()).isPresent())
			return getVariazioneResiduo().getPg_variazione();
		return null;
	}
}