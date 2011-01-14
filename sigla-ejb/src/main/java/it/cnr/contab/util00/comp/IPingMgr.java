package it.cnr.contab.util00.comp;
public interface IPingMgr {
Integer TIPO_PING_SERVER_ATTIVO = new Integer("0");
Integer TIPO_PING_LOGIN_ATTIVO = new Integer("1");
/**
 * Test attivazione server
 *  PreCondition: 
 *      Richiesto stato del server
 *  PostCondition:
 *      Effettua una query dummy per testare l'attivazione del meccanismo di accesso a DB e l'attivazione dell'EJB server.
 *
 * @return true se il ping ha successo
 */

public boolean ping(String hostname, Integer tipoPing);
}