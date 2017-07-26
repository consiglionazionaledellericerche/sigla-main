package it.cnr.contab.gestiva00.core.bulk;

public class Liquidazione_iva_variazioniBulk extends Liquidazione_iva_variazioniBase {
	private static final long serialVersionUID = 1L;

	public Liquidazione_iva_variazioniBulk() {
		super();
	}
	
	public Liquidazione_iva_variazioniBulk(java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.String cd_unita_organizzativa,java.lang.String tipo_liquidazione,java.sql.Timestamp dt_inizio,java.sql.Timestamp dt_fine,Long pg_dettaglio) {
		super(cd_cds,esercizio,cd_unita_organizzativa,tipo_liquidazione,dt_inizio,dt_fine,pg_dettaglio);
	}
}