package it.cnr.contab.prevent00.bulk;

/**
 * Interfaccia che implementa i metodi comuni delle classi Bulk delle Entrate 
 * 		e delle Spese iniziali. Essa permette di presentare, oltre ai dati
 * 		iniziali, anche quelli modificati, adattando {@link Pdg_aggregato_det }.
 *
 * @author: Vincenzo Bisquadro
 */
public interface Pdg_aggregato_det_iniziale extends Pdg_aggregato_det {
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'det_modificato'
 *
 * @return Il valore della proprietà 'det_modificato'
 */
Pdg_aggregato_det getDet_modificato();
}
