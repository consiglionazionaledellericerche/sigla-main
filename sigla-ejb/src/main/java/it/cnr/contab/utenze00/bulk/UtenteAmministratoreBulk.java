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

package it.cnr.contab.utenze00.bulk;

/**
 * Definisce una Utente di tipo Amministratore di Utenti (tipo utente = AMMINISTRATORE)
 *	
 */

import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.*;

public class UtenteAmministratoreBulk extends UtenteBulk 
{
	private CdsBulk cds = new CdsBulk();
public UtenteAmministratoreBulk() {
	super();
	inizializza();	
}
public UtenteAmministratoreBulk(String cd_utente) {
	super(cd_utente);
	inizializza();
}
/**
 * @return it.cnr.contab.config00.sto.bulk.CdsBulk
 */
public it.cnr.contab.config00.sto.bulk.CdsBulk getCds() {
	return cds;
}
/**
 * Inizializza gli attributi specifici dell'Utente Amministratore
 */

private void inizializza() 
{
	setTi_utente(UtenteHome.TIPO_AMMINISTRATORE);

}
/**
 * Esegue una inizializzazione degli attributi dell'UtenteAmministratoreBulk prima di eseguire 
 * l'inserimento di un record nel db
 */

public void insertingUsing(it.cnr.jada.persistency.Persister persister) 
{
	updatingUsing( persister );

}
/**
 * Determina quando abilitare o meno nell'interfaccia utente il campo cds 
 * @return boolean true se il campo deve essere disabilitato
 */

public boolean isROCds() {
	return cds == null || cds.getCrudStatus() == NORMAL;
}
/**
 * @param newCds it.cnr.contab.config00.sto.bulk.CdsBulk
 */
public void setCds(it.cnr.contab.config00.sto.bulk.CdsBulk newCds) {
	cds = newCds;
}
/**
 * Esegue una inizializzazione degli attributi dell'UtenteAmministratoreBulk prima di eseguire 
 * l'aggiornamento di un record nel db
 */

public void updatingUsing(it.cnr.jada.persistency.Persister persister) 
{
	if ( cds != null )
		setCd_cds_configuratore( cds.getCd_unita_organizzativa() );
	
}
/**
 * Esegue la validazione formale dei campi di input
 */

public void validate() throws ValidationException
{
	super.validate();
	
	if ( cds.getCd_unita_organizzativa() == null  )
		throw new ValidationException( "Il campo CODICE CDS AMMINISTRATO è obbligatorio." );

}
}
