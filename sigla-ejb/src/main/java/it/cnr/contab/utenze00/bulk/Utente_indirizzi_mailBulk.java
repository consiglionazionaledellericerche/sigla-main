/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
* Created by Generator 1.0
* Date 23/02/2006
*/
package it.cnr.contab.utenze00.bulk;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.CRUDBP;
public class Utente_indirizzi_mailBulk extends Utente_indirizzi_mailBase {
	private UtenteBulk utente;
	public Utente_indirizzi_mailBulk() {
		super();
	}
	public Utente_indirizzi_mailBulk(java.lang.String cd_utente, java.lang.String indirizzo_mail) {
		super(cd_utente, indirizzo_mail);
		setUtente(new UtenteBulk(cd_utente));
	}
	public UtenteBulk getUtente() {
		return utente;
	}
	
	public void setUtente(UtenteBulk utente) {
		this.utente = utente;
	}
	public void setCd_utente(String cd_utente) {
		getUtente().setCd_utente(cd_utente);
	}
	public String getCd_utente() {
		return getUtente().getCd_utente();
	}
	public boolean isCdrEnte(){
		if (utente != null && (utente instanceof UtenteComuneBulk) &&
				((UtenteComuneBulk)utente).getCdr() != null &&
				((UtenteComuneBulk)utente).getCdr().isCdrAC())
			return false;
		return true;
	}
	public void validate(OggettoBulk oggettobulk) throws ValidationException {
		if (getIndirizzo_mail() != null){
			try {
				new InternetAddress(getIndirizzo_mail()).validate();
			} catch (AddressException e) {
				throw new ValidationException("Indirizzo E-Mail non valido!");
			}
		}
		super.validate(oggettobulk);
	}
	@Override
	public OggettoBulk initializeForInsert(CRUDBP bp, ActionContext context) {

		super.initializeForInsert(bp, context);
		resetFlags();
		return this;
	}
	private void resetFlags() {
		setFl_com_app_var_stanz_comp(false);
		setFl_com_app_var_stanz_res(false); 
		setFl_err_appr_var_bil_cnr_comp(false);
		setFl_err_appr_var_bil_cnr_res(false);
		setFlEsitoNegFattElettr(false);
		setFlEsitoPosFattElettr(false);
	}
}