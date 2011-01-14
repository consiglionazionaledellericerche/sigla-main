package it.cnr.contab.config00.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

/**
 * @author aimprota
 *
 */
public class Parametri_enteKey extends OggettoBulk implements KeyedPersistent{
	// ID NUMBER(9) NOT NULL (PK)
	private java.lang.Integer id;
    /**
     * 
     */
    public Parametri_enteKey()
    {
        super();
    }
	public Parametri_enteKey(java.lang.Integer id)
	{
		super();
		this.id = id;
	}  
	
	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) return true;
		if (!(o instanceof Parametri_enteKey)) return false;
		Parametri_enteKey k = (Parametri_enteKey)o;
		if(!compareKey(getId(),k.getId())) return false;
		return true;
	}
	
	/* 
	 * Getter dell'attributo id
	 */
	public java.lang.Integer getId() {
		return id;
	}
	public int primaryKeyHashCode() {
		return
			calculateKeyHashCode(getId());
	}
	/* 
	 * Setter dell'attributo id
	 */
	public void setId(java.lang.Integer id) {
		this.id = id;
	}  

}
