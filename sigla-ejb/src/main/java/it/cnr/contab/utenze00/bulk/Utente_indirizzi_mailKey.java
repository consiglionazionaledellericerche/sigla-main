/*
* Created by Generator 1.0
* Date 23/02/2006
*/
package it.cnr.contab.utenze00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Utente_indirizzi_mailKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_utente;
	private java.lang.String indirizzo_mail;
	public Utente_indirizzi_mailKey() {
		super();
	}
	public Utente_indirizzi_mailKey(java.lang.String cd_utente, java.lang.String indirizzo_mail) {
		super();
		this.cd_utente=cd_utente;
		this.indirizzo_mail=indirizzo_mail;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Utente_indirizzi_mailKey)) return false;
		Utente_indirizzi_mailKey k = (Utente_indirizzi_mailKey) o;
		if (!compareKey(getCd_utente(), k.getCd_utente())) return false;
		if (!compareKey(getIndirizzo_mail(), k.getIndirizzo_mail())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_utente());
		i = i + calculateKeyHashCode(getIndirizzo_mail());
		return i;
	}
	public void setCd_utente(java.lang.String cd_utente)  {
		this.cd_utente=cd_utente;
	}
	public java.lang.String getCd_utente () {
		return cd_utente;
	}
	public void setIndirizzo_mail(java.lang.String indirizzo_mail)  {
		this.indirizzo_mail=indirizzo_mail;
	}
	public java.lang.String getIndirizzo_mail () {
		return indirizzo_mail;
	}
}