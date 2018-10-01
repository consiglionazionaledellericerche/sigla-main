package it.cnr.contab.progettiric00.tabrif.bulk;

import java.util.Dictionary;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.action.CRUDBP;

public class Voce_piano_economico_prgBulk extends Voce_piano_economico_prgBase {
	private Unita_organizzativaBulk unita_organizzativa;

	public final static Dictionary tipoVoceEconomicaKeys;

	public final static String PERSONALE_DETER = "PDT";
	public final static String PERSONALE_INDET = "PIN";
	public final static String PERSONALE_OTHER = "POT";
	
	static {
		tipoVoceEconomicaKeys = new it.cnr.jada.util.OrderedHashtable();
		tipoVoceEconomicaKeys.put(PERSONALE_DETER,"Personale a tempo determinato");
		tipoVoceEconomicaKeys.put(PERSONALE_INDET,"Personale a tempo indeterminato");
		tipoVoceEconomicaKeys.put(PERSONALE_OTHER,"Altro tipo di personale");
	}

	public Voce_piano_economico_prgBulk() {
		super();
	}

	public Voce_piano_economico_prgBulk(java.lang.String cd_unita_organizzativa, java.lang.String cd_voce_piano) {
		super(cd_unita_organizzativa, cd_voce_piano);
	}
	
	public Unita_organizzativaBulk getUnita_organizzativa() {
		return unita_organizzativa;
	}
	
	public void setUnita_organizzativa(Unita_organizzativaBulk unita_organizzativa) {
		this.unita_organizzativa = unita_organizzativa;
	}
	
	public java.lang.String getCd_unita_organizzativa() {
		it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = this.getUnita_organizzativa();
		if (unita_organizzativa == null)
			return null;
		return unita_organizzativa.getCd_unita_organizzativa();
	}

	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
		this.getUnita_organizzativa().setCd_unita_organizzativa(cd_unita_organizzativa);
	}
	
	public static Dictionary getTipovoceeconomicakeys() {
		return tipoVoceEconomicaKeys;
	}
}
