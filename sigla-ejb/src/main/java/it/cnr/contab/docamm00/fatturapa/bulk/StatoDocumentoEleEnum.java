package it.cnr.contab.docamm00.fatturapa.bulk;

public enum StatoDocumentoEleEnum {
	INIZIALE("SF01"), AGGIORNATO("SF02"), COMPLETO("SF02"), REGISTRATO("SF03"), RIFIUTATO("SF04");
	
	private final String statoSDI;

	private StatoDocumentoEleEnum(String statoSDI) {
		this.statoSDI = statoSDI;
	}

	public String statoSDI() {
		return statoSDI;
	}

	public static StatoDocumentoEleEnum fromStatoSDI(String v) {
		for (StatoDocumentoEleEnum c : StatoDocumentoEleEnum.values()) {
			if (c.statoSDI.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}	
	
}