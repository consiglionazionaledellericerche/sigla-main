package it.cnr.contab.doccont00.core.bulk;

import java.util.*;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class Obbligazione_scad_voce_aggregatoBulk extends it.cnr.jada.bulk.OggettoBulk {
	private String codice;
	private Timestamp dt_scadenza;
	private BigDecimal importo;
public Obbligazione_scad_voce_aggregatoBulk() {
	super();
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param codice	
 * @param dt_scadenza	
 * @param importo	
 */
public Obbligazione_scad_voce_aggregatoBulk( String codice, Timestamp dt_scadenza, BigDecimal importo ) 
{
	super();
	this.codice = codice;
	this.dt_scadenza = dt_scadenza;
	this.importo = importo;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'codice'
 *
 * @return Il valore della proprietà 'codice'
 */
public java.lang.String getCodice() {
	return codice;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'dt_scadenza'
 *
 * @return Il valore della proprietà 'dt_scadenza'
 */
public java.sql.Timestamp getDt_scadenza() {
	return dt_scadenza;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'importo'
 *
 * @return Il valore della proprietà 'importo'
 */
public java.math.BigDecimal getImporto() {
	return importo;
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param aggregati	
 * @return 
 */
public Vector ordina( Vector aggregati ) 
{
	
	Collections.sort(aggregati,new Comparator() {	

		public int compare(Object o1, Object o2) 
		{
			Obbligazione_scad_voce_aggregatoBulk ag1 = (Obbligazione_scad_voce_aggregatoBulk) o1;
			Obbligazione_scad_voce_aggregatoBulk ag2 = (Obbligazione_scad_voce_aggregatoBulk) o2;
			if ( !ag1.getCodice().equals( ag2.getCodice()))
				return ag1.getCodice().compareTo( ag2.getCodice()); 
			else if ( ag1.getDt_scadenza() == null )
				return 1;
			else if ( ag2.getDt_scadenza() == null )
				return 2;
			else	
				return ag1.getDt_scadenza().compareTo( ag2.getDt_scadenza());
		}
		public boolean equals(Object o)  
		{
			Obbligazione_scad_voce_aggregatoBulk ag = (Obbligazione_scad_voce_aggregatoBulk) o;
			if ( (getDt_scadenza() == null && ag.getDt_scadenza() != null) ||
				 (getDt_scadenza() != null && ag.getDt_scadenza() == null) 	)
				return false;
			else if ( getDt_scadenza() == null && ag.getDt_scadenza() == null)
				return getCodice().equals( ag.getCodice() );
			else	
				return getCodice().equals( ag.getCodice() ) && getDt_scadenza().equals( ag.getDt_scadenza());
		}
	});

	return aggregati;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'codice'
 *
 * @param newCodice	Il valore da assegnare a 'codice'
 */
public void setCodice(java.lang.String newCodice) {
	codice = newCodice;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'dt_scadenza'
 *
 * @param newDt_scadenza	Il valore da assegnare a 'dt_scadenza'
 */
public void setDt_scadenza(java.sql.Timestamp newDt_scadenza) {
	dt_scadenza = newDt_scadenza;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'importo'
 *
 * @param newImporto	Il valore da assegnare a 'importo'
 */
public void setImporto(java.math.BigDecimal newImporto) {
	importo = newImporto;
}
}
