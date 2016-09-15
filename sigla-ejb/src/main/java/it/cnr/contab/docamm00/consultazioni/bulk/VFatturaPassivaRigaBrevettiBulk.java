/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 12/09/2016
 */
package it.cnr.contab.docamm00.consultazioni.bulk;
public class VFatturaPassivaRigaBrevettiBulk extends VFatturaPassivaRigaBrevettiBase {
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_FATTURA_PASSIVA_RIGA_BREVETTI
	 **/
	public VFatturaPassivaRigaBrevettiBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_FATTURA_PASSIVA_RIGA_BREVETTI
	 **/
	public VFatturaPassivaRigaBrevettiBulk(String cd_cds,String cd_unita_organizzativa,Integer esercizio, Long pg_fattura_attiva,Long progressivo_riga) {
		super(cd_cds,cd_unita_organizzativa,esercizio,pg_fattura_attiva,progressivo_riga);
	}
}