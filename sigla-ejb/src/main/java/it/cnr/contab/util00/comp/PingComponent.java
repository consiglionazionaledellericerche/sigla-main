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

package it.cnr.contab.util00.comp;

import it.cnr.contab.util00.ejb.PingComponentSession;

/**
 * Componente per il ping su server applicativo per verificarne lo stato di attivazione
 */
public class PingComponent extends it.cnr.jada.comp.GenericComponent implements IPingMgr {
/**
 * PingComponent constructor comment.
 */
public PingComponent() {
	super();
}

/**
 * Test attivazione server
 *  PreCondition: 
 *      Richiesto stato del server
 *  PostCondition:
 *      Effettua una query dummy per testare l'attivazione del meccanismo di accesso a DB e l'attivazione dell'EJB server.
 *
 * @return true se il ping ha successo
 */
public boolean ping(String hostname, Integer tipoPing) {
	try {
		String campo="";
		if (tipoPing.equals(TIPO_PING_LOGIN_ATTIVO))
			campo="LOGIN";
		else
			campo="ATTIVO";
		java.sql.Connection conn = it.cnr.jada.util.ejb.EJBCommonServices.getConnection();
		try {
			java.sql.Statement stm = conn.createStatement();
			try {
				java.sql.ResultSet rs;
				if (hostname==null) {
					rs = stm.executeQuery("SELECT COUNT(*) FROM ALBERO_MAIN");
					rs.next();
					rs.getInt(1);
					try{rs.close();}catch( java.sql.SQLException e ){};
					return true;
				}
				else {
					// se è presente una riga con hostname = '*' 
					// la regola vale per tutti i server 
					rs = stm.executeQuery("SELECT "+campo+" FROM APPLICATION_SERVER WHERE HOSTNAME='*'");
					if(rs.next()) {
						String attivo = (String) rs.getObject(1);
						try{rs.close();}catch( java.sql.SQLException e ){};
						if (attivo.equals("N"))
							return false;
						else
							return true;
					}					
					else
						try{rs.close();}catch( java.sql.SQLException e ){};
					// se non trova la riga con hostname = '*' cerca i server specifici 

					rs = stm.executeQuery("SELECT COUNT(*) FROM APPLICATION_SERVER WHERE HOSTNAME="+
					"'"+hostname+"' and "+campo+" = 'N'");
					rs.next();
					int count = rs.getInt(1);
					try{rs.close();}catch( java.sql.SQLException e ){};
					if (count>0)
						return false;
					else
						return true;
				}
			} finally {
				try{stm.close();}catch( java.sql.SQLException e ){};
			}
		} finally {
			conn.close();
		}
	} catch(Throwable e) {
		return false;
	}
}
}