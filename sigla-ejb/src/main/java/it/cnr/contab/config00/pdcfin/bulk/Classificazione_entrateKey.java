package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

/**
 * @author aimprota
 *
 */
public class Classificazione_entrateKey extends OggettoBulk implements KeyedPersistent
{
	private java.lang.Integer esercizio;
	private String codice_cla_e;

    /**
     * 
     */
    public Classificazione_entrateKey()
    {
        super();
    }

	public Classificazione_entrateKey(java.lang.Integer esercizio, String codice_cla_e)
	{
		super();
		this.esercizio = esercizio;
		this.codice_cla_e = codice_cla_e;
	}
	
	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) return true;
		if (!(o instanceof Classificazione_entrateKey)) return false;
		Classificazione_entrateKey k = (Classificazione_entrateKey)o;
		if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
		if(!compareKey(getCodice_cla_e(),k.getCodice_cla_e())) return false;
		return true;
	}

	public int primaryKeyHashCode() {
		return
			calculateKeyHashCode(getEsercizio())+
		    calculateKeyHashCode(getCodice_cla_e());
	}

    /**
     * @return
     */
    public String getCodice_cla_e()
    {
        return codice_cla_e;
    }

    /**
     * @return
     */
    public java.lang.Integer getEsercizio()
    {
        return esercizio;
    }

    /**
     * @param string
     */
    public void setCodice_cla_e(String string)
    {
        codice_cla_e = string;
    }

    /**
     * @param integer
     */
    public void setEsercizio(java.lang.Integer integer)
    {
        esercizio = integer;
    }

}
