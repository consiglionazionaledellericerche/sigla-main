/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 26/11/2007
 */
package it.cnr.contab.inventario00.consultazioni.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.action.CRUDBP;
public class V_inventario_per_contoBulk extends OggettoBulk implements Persistent {
	public V_inventario_per_contoBulk() {
		super();
	}
//  ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;
 
//    CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private java.lang.String cd_unita_organizzativa;
 
//    CD_VOCE_EP VARCHAR(45) NOT NULL
	private java.lang.String cd_voce_ep;
 
//    DS_VOCE_EP VARCHAR(100)
	private java.lang.String ds_voce_ep;
 
//    CONTABILIZZAZIONE VARCHAR(13)
	private java.lang.String contabilizzazione;
 
//    DARE DECIMAL(22,0)
	private java.math.BigDecimal dare;
 
//    AVERE DECIMAL(22,0)
	private java.math.BigDecimal avere;
 
//    SALDO DECIMAL(22,0)
	private java.math.BigDecimal saldo;
 
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.String getCd_unita_organizzativa() {
		return cd_unita_organizzativa;
	}
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa)  {
		this.cd_unita_organizzativa=cd_unita_organizzativa;
	}
	public java.lang.String getCd_voce_ep() {
		return cd_voce_ep;
	}
	public void setCd_voce_ep(java.lang.String cd_voce_ep)  {
		this.cd_voce_ep=cd_voce_ep;
	}
	public java.lang.String getDs_voce_ep() {
		return ds_voce_ep;
	}
	public void setDs_voce_ep(java.lang.String ds_voce_ep)  {
		this.ds_voce_ep=ds_voce_ep;
	}
	public java.lang.String getContabilizzazione() {
		return contabilizzazione;
	}
	public void setContabilizzazione(java.lang.String contabilizzazione)  {
		this.contabilizzazione=contabilizzazione;
	}
	public java.math.BigDecimal getDare() {
		return dare;
	}
	public void setDare(java.math.BigDecimal dare)  {
		this.dare=dare;
	}
	public java.math.BigDecimal getAvere() {
		return avere;
	}
	public void setAvere(java.math.BigDecimal avere)  {
		this.avere=avere;
	}
	public java.math.BigDecimal getSaldo() {
		return saldo;
	}
	public void setSaldo(java.math.BigDecimal saldo)  {
		this.saldo=saldo;
	}
}