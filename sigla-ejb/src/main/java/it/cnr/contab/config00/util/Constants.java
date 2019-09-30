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

package it.cnr.contab.config00.util;

/**
 * Classe che definisce alcune costanti
 */
public class Constants 
{
	public static final Integer LIVELLO_CDS = new Integer( 1 );
	public static final Integer LIVELLO_UO =  new Integer( 2 );

	
	public static final String  TIPO_CDS = new String( "C" );
	public static final String  TIPO_UO  = new String( "U" );
	public static final String  TIPO_CDR = new String( "R" );	

	public static final Integer ERRORE_SIP_100 = new Integer(100);
	public static final Integer ERRORE_SIP_101 = new Integer(101);
	public static final Integer ERRORE_SIP_102 = new Integer(102);
	public static final Integer ERRORE_SIP_103 = new Integer(103);
	public static final Integer ERRORE_SIP_104 = new Integer(104);
	public static final Integer ERRORE_SIP_105 = new Integer(105);
	public static final Integer ERRORE_SIP_106 = new Integer(106);
	public static final Integer ERRORE_SIP_107 = new Integer(107);
	public static final Integer ERRORE_SIP_108 = new Integer(108);
	public static final Integer ERRORE_SIP_109 = new Integer(109);
	public static final Integer ERRORE_SIP_110 = new Integer(110);
	public static final Integer ERRORE_SIP_111 = new Integer(111);
	public static final Integer ERRORE_SIP_112 = new Integer(112);
	public static final Integer ERRORE_SIP_113 = new Integer(113);
	public static final Integer ERRORE_SIP_114 = new Integer(114);
	public static final Integer ERRORE_SIP_115 = new Integer(115);
	public static final Integer ERRORE_SIP_116 = new Integer(116);
	public static final Integer ERRORE_SIP_117 = new Integer(117);
	public static final Integer ERRORE_SIP_118 = new Integer(118);
	public static final Integer ERRORE_SIP_119 = new Integer(119);
	public static final Integer ERRORE_SIP_120 = new Integer(120);
	public final static java.util.Dictionary<Integer,String> erroriSIP;
	static {
		erroriSIP = new it.cnr.jada.util.OrderedHashtable();
		erroriSIP.put(ERRORE_SIP_100, "Errore generico, causa sconosciuta");
		erroriSIP.put(ERRORE_SIP_101, "Non è stata definita la query per la ricerca");
		erroriSIP.put(ERRORE_SIP_102, "Non è stato definito il dominio per la ricerca");
		erroriSIP.put(ERRORE_SIP_103, "Non è stato definito il tipo di servizio");
		erroriSIP.put(ERRORE_SIP_104, "Non è stato definito il tipo di soggetto terzo: persona fisica o persona giuridica");
		erroriSIP.put(ERRORE_SIP_105, "I dati per l'inserimento di un nuovo soggetto terzo non consentono la generazione di un nuovo codice terzo");
		erroriSIP.put(ERRORE_SIP_106, "Il soggetto terzo che si sta cercando di aggiungere risulta già presente all'interno del database, poichè Codice Fiscale risulta già inserito");
		erroriSIP.put(ERRORE_SIP_107, "Dati incompleti per l'inserimento di un nuovo soggetto terzo");
		erroriSIP.put(ERRORE_SIP_108, "Il soggetto terzo che si sta cercando di eliminare non risulta essere attualmente presente nel database");
		erroriSIP.put(ERRORE_SIP_109, "Non è consentita l'eliminazione del soggetto terzo specificato");
		erroriSIP.put(ERRORE_SIP_110, "Errore di autenticazione");
		erroriSIP.put(ERRORE_SIP_111, "Errore interno del sistema, il sistema potrebbe avere un problema tecnico in corso");
		erroriSIP.put(ERRORE_SIP_112, "Servizio non disponibile (il S.I.C. ha reso momentaneamente inattivo il servizio)");
		erroriSIP.put(ERRORE_SIP_113, "Non è stata definita l'Unita Organizzativa per la ricerca");
		erroriSIP.put(ERRORE_SIP_114, "Non è stato definito l'Esercizio per la ricerca");
		erroriSIP.put(ERRORE_SIP_115, "Non è stato definito il periodo della rendicontazione");
		erroriSIP.put(ERRORE_SIP_116, "Formato date errato per il periodo della rendicontazione");
		erroriSIP.put(ERRORE_SIP_117, "Non è stato definito il CDR per la ricerca");
		erroriSIP.put(ERRORE_SIP_118, "Non è stato definito il tipo: 'E' Entrata o 'S' Spesa");
		erroriSIP.put(ERRORE_SIP_119, "Formato Matricola errato");
		erroriSIP.put(ERRORE_SIP_120, "Non è stato definito un parametro obbligatorio");
	}	
	
	public static final String  RICHIESTE_IN_CORSO = "C";
	public static final String  RICHIESTE_SCADUTE = "S";

	public static final Integer ERRORE_INC_100 = new Integer(100);
	public static final Integer ERRORE_INC_101 = new Integer(101);
	public static final Integer ERRORE_INC_102 = new Integer(102);
	public static final Integer ERRORE_INC_103 = new Integer(103);
	public static final Integer ERRORE_INC_104 = new Integer(104);
	public static final Integer ERRORE_INC_105 = new Integer(105);
	public static final Integer ERRORE_INC_106 = new Integer(106);
	
	public final static java.util.Dictionary<Integer,String> erroriINC;
	static {
		erroriINC = new it.cnr.jada.util.OrderedHashtable();
		erroriINC.put(ERRORE_INC_100, "Errore generico, causa sconosciuta");
		erroriINC.put(ERRORE_INC_101, "Non è stata definita la query per la ricerca");
		erroriINC.put(ERRORE_INC_102, "Non è stato definito il dominio per la ricerca");
		erroriINC.put(ERRORE_INC_103, "Non è stato definito il tipo di servizio");
		erroriINC.put(ERRORE_INC_104, "Il parametro per la ricerca non è consentito");
		erroriINC.put(ERRORE_INC_105, "Errore di autenticazione");
		erroriINC.put(ERRORE_INC_106, "Errore interno del sistema, il sistema potrebbe avere un problema tecnico in corso");
	}	
	public static final Integer ERRORE_CON_200 = new Integer(200);
	public static final Integer ERRORE_CON_201 = new Integer(201);
	public static final Integer ERRORE_CON_202 = new Integer(202);
	public static final Integer ERRORE_CON_203 = new Integer(203);
	public static final Integer ERRORE_CON_204 = new Integer(204);
	public static final Integer ERRORE_CON_205 = new Integer(205);
	
	
	public final static java.util.Dictionary<Integer,String> erroriCON;
	static {
		erroriCON = new it.cnr.jada.util.OrderedHashtable();
		erroriCON.put(ERRORE_CON_200, "Errore generico, causa sconosciuta");
		erroriCON.put(ERRORE_CON_201, "Non è stata definito un parametro obbligatorio");
		erroriCON.put(ERRORE_CON_202, "Uno o più paramentri sono malformati");
		erroriCON.put(ERRORE_CON_203, "Errore di autenticazione");
		erroriCON.put(ERRORE_CON_204, "Errore interno del sistema, il sistema potrebbe avere un problema tecnico");
		erroriCON.put(ERRORE_CON_205, "Servizio attualmente non disponibile");
		
	}
/**
 * Constants constructor comment.
 */
public Constants() {
	super();
}
}
