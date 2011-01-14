package it.cnr.contab.inventario00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;

public class Ass_inv_bene_fatturaKey extends OggettoBulk implements KeyedPersistent {

private java.lang.Long pg_riga;
public Ass_inv_bene_fatturaKey() {
	super();
}

public Ass_inv_bene_fatturaKey(Long riga) {
	
	super();
	this.pg_riga=riga;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this== o) return true;
	if (!(o instanceof Ass_inv_bene_fatturaKey)) return false;
	Ass_inv_bene_fatturaKey k = (Ass_inv_bene_fatturaKey) o;
	if (!compareKey(getPg_riga(), k.getPg_riga())) return false;
		return true;
}
public int primaryKeyHashCode() {
	int i = 0;
	i = i + calculateKeyHashCode(getPg_riga());
	return i;
}
public void setPg_riga(java.lang.Long pg_riga)  {
	this.pg_riga=pg_riga;
}
public java.lang.Long getPg_riga () {
	return pg_riga;
}
}
