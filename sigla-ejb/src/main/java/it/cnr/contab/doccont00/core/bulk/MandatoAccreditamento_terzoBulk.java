package it.cnr.contab.doccont00.core.bulk;

public class MandatoAccreditamento_terzoBulk extends Mandato_terzoBulk {
	protected MandatoAccreditamentoBulk mandatoAccreditamento;

	
public MandatoAccreditamento_terzoBulk() {
	super();
}
public MandatoAccreditamento_terzoBulk( MandatoBulk mandato, Mandato_terzoBulk terzo)
{
	super( mandato, terzo );
}
public MandatoAccreditamento_terzoBulk(String cd_cds, Integer cd_terzo, Integer esercizio, Long pg_mandato) {
	super(cd_cds, cd_terzo, esercizio, pg_mandato);
}
/**
 * @return it.cnr.contab.doccont00.core.bulk.MandatoAccreditamentoBulk
 */
public MandatoBulk getMandato() {
	return mandatoAccreditamento;
}
/**
 * @return it.cnr.contab.doccont00.core.bulk.MandatoAccreditamentoBulk
 */
public MandatoAccreditamentoBulk getMandatoAccreditamento() {
	return mandatoAccreditamento;
}
/**
 * @param newMandatoI it.cnr.contab.doccont00.core.bulk.MandatoIBulk
 */
public void setMandato(MandatoBulk newMandato) {
	setMandatoAccreditamento( (MandatoAccreditamentoBulk) newMandato);;
}
/**
 * @param newMandatoAccreditamento it.cnr.contab.doccont00.core.bulk.MandatoAccreditamentoBulk
 */
public void setMandatoAccreditamento(MandatoAccreditamentoBulk newMandatoAccreditamento) {
	mandatoAccreditamento = newMandatoAccreditamento;
}
}
