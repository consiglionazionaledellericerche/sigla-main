package it.cnr.contab.web.rest.model;

import it.cnr.contab.missioni00.docs.bulk.MissioneBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.bulk.OggettoBulk;

public class UserContextBulk <T extends OggettoBulk> {
	private CNRUserContext userContext;
	private T oggettoBulk;

	public UserContextBulk() {
		super();
	}

	public CNRUserContext getUserContext() {
		return userContext;
	}

	public void setUserContext(CNRUserContext userContext) {
		this.userContext = userContext;
	}

	public T getOggettoBulk() {
		return oggettoBulk;
	}

	public void setOggettoBulk(T oggettoBulk) {
		this.oggettoBulk = oggettoBulk;
	}

	@Override
	public String toString() {
		return "UserContextBulk [userContext=" + userContext
				+ ", oggettoBulk=" + oggettoBulk + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((oggettoBulk == null) ? 0 : oggettoBulk.hashCode());
		result = prime * result
				+ ((userContext == null) ? 0 : userContext.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserContextBulk<T> other = (UserContextBulk<T>) obj;
		if (oggettoBulk == null) {
			if (other.oggettoBulk != null)
				return false;
		} else if (!oggettoBulk.equals(other.oggettoBulk))
			return false;
		if (userContext == null) {
			if (other.userContext != null)
				return false;
		} else if (!userContext.equals(other.userContext))
			return false;
		return true;
	}
	
}
