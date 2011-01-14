package it.cnr.contab.config00.latt.bulk;
/**
 * Interfaccia di definizione delle costanti di gestione entrata/spesa
 */

public interface CostantiTi_gestione extends java.io.Serializable {
	public final static String TI_GESTIONE_ENTRATE = "E" ;
	public final static String TI_GESTIONE_SPESE   = "S" ;

/**
 * Ritorna true se il ricevente eguagli o
 *
 * @param o	oggetti di confronto
 * @return 
 */
boolean equals(Object o);
}