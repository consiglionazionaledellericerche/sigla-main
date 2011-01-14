/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 26/07/2007
 */
package it.cnr.contab.incarichi00.xmlfp.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Incarichi_archivio_xml_fpKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer id_archivio;

	public Incarichi_archivio_xml_fpKey() {
		super();
	}

	public Incarichi_archivio_xml_fpKey(java.lang.Integer id_archivio) {
		super();
		this.id_archivio=id_archivio;
	}
	
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Incarichi_archivio_xml_fpKey)) return false;
		Incarichi_archivio_xml_fpKey k = (Incarichi_archivio_xml_fpKey) o;
		if (!compareKey(getId_archivio(), k.getId_archivio())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getId_archivio());
		return i;
	}

	public java.lang.Integer getId_archivio() {
		return id_archivio;
	}

	public void setId_archivio(java.lang.Integer idArchivio) {
		id_archivio = idArchivio;
	}
}