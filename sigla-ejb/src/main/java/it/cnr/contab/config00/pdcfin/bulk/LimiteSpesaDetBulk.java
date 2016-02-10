/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 15/12/2010
 */
package it.cnr.contab.config00.pdcfin.bulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class LimiteSpesaDetBulk extends LimiteSpesaDetBase {
	/**
	 * [LIMITE_SPESA Tabella di associazione tra elemento voce - fonte ed importo limite utilizzata nella gestione degli impegni per controllare il limite da utilizzare]
	 **/
	private LimiteSpesaBulk limiteSpesa =  new LimiteSpesaBulk();
	private it.cnr.contab.config00.sto.bulk.CdsBulk cds = new CdsBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: LIMITE_SPESA_DET
	 **/
	public LimiteSpesaDetBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: LIMITE_SPESA_DET
	 **/
	public LimiteSpesaDetBulk(java.lang.Integer esercizio, java.lang.String cdCds, java.lang.Integer esercizio_voce, java.lang.String tiAppartenenza, java.lang.String tiGestione, java.lang.String cdElementoVoce, java.lang.String fonte) {
		super(esercizio, cdCds, esercizio_voce, tiAppartenenza, tiGestione, cdElementoVoce, fonte);
		setLimiteSpesa( new LimiteSpesaBulk(esercizio,esercizio_voce,tiAppartenenza,tiGestione,cdElementoVoce,fonte) );
		setCds( new CdsBulk(cdCds) );
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Tabella di associazione tra elemento voce - fonte ed importo limite utilizzata nella gestione degli impegni per controllare il limite da utilizzare]
	 **/
	public LimiteSpesaBulk getLimiteSpesa() {
		return limiteSpesa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Tabella di associazione tra elemento voce - fonte ed importo limite utilizzata nella gestione degli impegni per controllare il limite da utilizzare]
	 **/
	public void setLimiteSpesa(LimiteSpesaBulk limiteSpesa)  {
		this.limiteSpesa=limiteSpesa;
	}
	
	
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizio]
	 **/
	public java.lang.Integer getEsercizio() {
		LimiteSpesaBulk limiteSpesa = this.getLimiteSpesa();
		if (limiteSpesa == null)
			return null;
		return getLimiteSpesa().getEsercizio();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio]
	 **/
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.getLimiteSpesa().setEsercizio(esercizio);
	}
	
	@Override
	public Integer getEsercizio_voce() {
		LimiteSpesaBulk limiteSpesa = this.getLimiteSpesa();
		if (limiteSpesa == null)
			return null;
		return getLimiteSpesa().getEsercizio_voce();
	}
	
	@Override
	public void setEsercizio_voce(Integer esercizio_voce) {
		this.getLimiteSpesa().setEsercizio_voce(esercizio_voce);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tiAppartenenza]
	 **/
	public java.lang.String getTi_appartenenza() {
		LimiteSpesaBulk limiteSpesa = this.getLimiteSpesa();
		if (limiteSpesa == null)
			return null;
		return getLimiteSpesa().getTi_appartenenza();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tiAppartenenza]
	 **/
	public void setTi_appartenenza(java.lang.String tiAppartenenza)  {
		this.getLimiteSpesa().setTi_appartenenza(tiAppartenenza);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tiGestione]
	 **/
	public java.lang.String getTi_gestione() {
		LimiteSpesaBulk limiteSpesa = this.getLimiteSpesa();
		if (limiteSpesa == null)
			return null;
		return getLimiteSpesa().getTi_gestione();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tiGestione]
	 **/
	public void setTi_gestione(java.lang.String tiGestione)  {
		this.getLimiteSpesa().setTi_gestione(tiGestione);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdElementoVoce]
	 **/
	public java.lang.String getCd_elemento_voce() {
		LimiteSpesaBulk limiteSpesa = this.getLimiteSpesa();
		if (limiteSpesa == null)
			return null;
		return getLimiteSpesa().getCd_elemento_voce();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdElementoVoce]
	 **/
	public void setCd_elemento_voce(java.lang.String cdElementoVoce)  {
		this.getLimiteSpesa().setCd_elemento_voce(cdElementoVoce);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [fonte]
	 **/
	public java.lang.String getFonte() {
		LimiteSpesaBulk limiteSpesa = this.getLimiteSpesa();
		if (limiteSpesa == null)
			return null;
		return getLimiteSpesa().getFonte();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [fonte]
	 **/
	public void setFonte(java.lang.String fonte)  {
		this.getLimiteSpesa().setFonte(fonte);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public java.lang.String getCd_cds() {
		CdsBulk cds = this.getCds();
		if (cds == null)
			return null;
		return getCds().getCd_unita_organizzativa();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCd_cds(java.lang.String cdCds)  {
		this.getCds().setCd_unita_organizzativa(cdCds);
	}
	public it.cnr.contab.config00.sto.bulk.CdsBulk getCds() {
		return cds;
	}
	public void setCds(it.cnr.contab.config00.sto.bulk.CdsBulk cds) {
		this.cds = cds;
	}
	public boolean isUtilizzato(){
		if (getImpegni_assunti()!=null)
			return getImpegni_assunti().compareTo(Utility.ZERO)!=0;
		else
			return false;
	}
}