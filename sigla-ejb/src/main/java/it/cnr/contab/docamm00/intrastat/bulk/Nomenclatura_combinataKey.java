package it.cnr.contab.docamm00.intrastat.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Nomenclatura_combinataKey extends OggettoBulk implements KeyedPersistent {

	private java.lang.Long id_nomenclatura_combinata;

public Nomenclatura_combinataKey() {
	super();
}
public Nomenclatura_combinataKey(java.lang.Long id_nomenclatura_combinata) {
	super();
	this.id_nomenclatura_combinata = id_nomenclatura_combinata;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Nomenclatura_combinataKey)) return false;
	Nomenclatura_combinataKey k = (Nomenclatura_combinataKey)o;
	if(!compareKey(getId_nomenclatura_combinata(),k.getId_nomenclatura_combinata())) return false;
	return true;
}

public java.lang.Long getId_nomenclatura_combinata() {
	return id_nomenclatura_combinata;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getId_nomenclatura_combinata());
}
public void setId_nomenclatura_combinata(java.lang.Long id_nomenclatura_combinata) {
	this.id_nomenclatura_combinata = id_nomenclatura_combinata;
}
}
