package it.cnr.contab.bollo00.tabrif.bulk;

import java.util.Dictionary;
import java.util.Optional;

import it.cnr.jada.bulk.ValidationException;

public class Tipo_atto_bolloBulk extends Tipo_atto_bolloBase {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public static final Dictionary<String, String> tipoCalcoloKeys = new it.cnr.jada.util.OrderedHashtable();

	public static final String TIPO_FOGLIO = "F";
	public static final String TIPO_ESEMPLARE = "E";
	public static final String TIPO_IMPORTO = "I";

	static {
		tipoCalcoloKeys.put(TIPO_FOGLIO,"Foglio");
		tipoCalcoloKeys.put(TIPO_ESEMPLARE,"Esemplare");
		tipoCalcoloKeys.put(TIPO_IMPORTO,"Importo");
	}

	public Tipo_atto_bolloBulk() {
		super();
	}

	public Tipo_atto_bolloBulk(java.lang.Integer id) {
		super(id);
	}
	
	public static Dictionary<String, String> getTipoCalcoloKeys() {
		return tipoCalcoloKeys;
	}
	
	public boolean isCalcoloPerFoglio() {
		return TIPO_FOGLIO.equals(getTipoCalcolo());
	}

	public boolean isCalcoloPerImporto() {
		return TIPO_IMPORTO.equals(getTipoCalcolo());
	}
	
	public boolean isCalcoloPerEsemplare() {
		return TIPO_ESEMPLARE.equals(getTipoCalcolo());
	}

	@Override
	public void validate() throws ValidationException {
        if (isCalcoloPerFoglio() && !Optional.ofNullable(getRigheFoglio()).isPresent()) 
        	throw new ValidationException("Valorizzare il numero di righe per foglio!");
		super.validate();
	}
}