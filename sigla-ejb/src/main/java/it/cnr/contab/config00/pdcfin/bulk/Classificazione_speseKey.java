package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

/**
 * @author aimprota
 *
 */
public class Classificazione_speseKey extends OggettoBulk implements KeyedPersistent
{
	private java.lang.Integer esercizio;
	private String codice_cla_s;

    /**
     * 
     */
    public Classificazione_speseKey()
    {
        super();
    }

	public Classificazione_speseKey(java.lang.Integer esercizio, String codice_cla_s)
	{
		super();
		this.esercizio = esercizio;
		this.codice_cla_s = codice_cla_s;
	}
	
	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) return true;
		if (!(o instanceof Classificazione_speseKey)) return false;
		Classificazione_speseKey k = (Classificazione_speseKey)o;
		if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
		if(!compareKey(getCodice_cla_s(),k.getCodice_cla_s())) return false;
		return true;
	}

	public int primaryKeyHashCode() {
		return
			calculateKeyHashCode(getEsercizio())+
		    calculateKeyHashCode(getCodice_cla_s());
	}

    /**
     * @return
     */
    public String getCodice_cla_s()
    {
        return codice_cla_s;
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
    public void setCodice_cla_s(String string)
    {
        codice_cla_s = string;
    }

    /**
     * @param integer
     */
    public void setEsercizio(java.lang.Integer integer)
    {
        esercizio = integer;
    }

}
