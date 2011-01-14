/*
* Creted by Generator 1.0
* Date 19/04/2005
*/
package it.cnr.contab.doccont00.consultazioni.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.action.CRUDBP;
public class V_cons_imp_pag_per_tipoBulk extends OggettoBulk implements Persistent {
//	ESERCIZIO DECIMAL(4,0)
  private java.lang.Integer esercizio;
 
//    TIPO VARCHAR2(20)
	private java.lang.String voce;
 
//    DESCRIZIONE VARCHAR(100)
	private java.lang.String descrizione;
	
//	TIPO VARCHAR(100)
  private java.lang.String tipo;	
 
//    IMPEGNATO DECIMAL(15,2)
	private java.math.BigDecimal impegnato;
 
//    PAGATO DECIMAL(15,2)
	private java.math.BigDecimal pagato;
 
	
	public V_cons_imp_pag_per_tipoBulk() {
		super();
	}
	
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.String getVoce () {
		return voce;
	}
	public void setVoce(java.lang.String voce)  {
		this.voce=voce;
	}	
	public java.lang.String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(java.lang.String descrizione)  {
		this.descrizione=descrizione;
	}
	public java.lang.String getTipo () {
		return tipo;
	}
	public void setTipo(java.lang.String tipo)  {
		this.tipo=tipo;
	}
	public java.math.BigDecimal getImpegnato () {
		return impegnato;
	}
	public void setImpegnato(java.math.BigDecimal impegnato)  {
		this.impegnato=impegnato;
	}
	public java.math.BigDecimal getPagato() {
		return pagato;
	}
	public void setPagato(java.math.BigDecimal pagato)  {
		this.pagato=pagato;
	}
	
}