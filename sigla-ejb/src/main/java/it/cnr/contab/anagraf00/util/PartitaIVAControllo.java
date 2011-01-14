package it.cnr.contab.anagraf00.util;

/**
 * Classe per il controllo dell'esattezza della partita i.v.a.
 */

public class PartitaIVAControllo extends CodiceFiscaleControllo {
	 static String CODICE_NON_VALIDO = "Partita IVA non valida: ";
/**
 * Controlla la partita IVA
 *
 * Le stringhe in ingresso sono filtrate sulla base dei dizionari statici impostati nella classe.
 *
 * In particolare viene controllata la lunghezza, il fatto che sia formato solo da numeri 
 * e la correttezza del carattere di controllo finale
 *
 * @param codice codice partita IVA da controllare
 */

 public static int makePartitaIVA_CC(
  String codice
 ) throws ExPartitaIVA {

  if(codice == null)
   throw new ExPartitaIVA(CODICE_NON_VALIDO + "codice nullo!");

  if(codice.length() != 10)
   throw new ExPartitaIVA(CODICE_NON_VALIDO + "lunghezza del codice errata!");

  for(int i=0;i<codice.length();i++)
   if (!isNumero(codice.substring(i,i+1)))
	throw new ExPartitaIVA(CODICE_NON_VALIDO + "codice non numerico!");

  int aTotDispari=0;
  for(int idx=0;idx < codice.length() - 1; idx+=2)
   aTotDispari+=(new Integer(codice.substring(idx,idx+1))).intValue();

  int x=0;
  int aTot=aTotDispari;
  for(int idx=1;idx < codice.length(); idx+=2) {
   x=(new Integer(codice.substring(idx,idx+1))).intValue()*2;
   aTot+=x/10 + x%10;
  }

  if (aTot%10 == 0)
   return 0;
  else
   return 10-aTot%10;
 }     
/**
 * Controlla la partita IVA
 *
 * Le stringhe in ingresso sono filtrate sulla base dei dizionari statici impostati nella classe.
 *
 * In particolare viene controllata la lunghezza, il fatto che sia formato solo da numeri 
 * e la correttezza del carattere di controllo finale
 *
 * @param codice codice partita IVA da controllare
 */

 public static void parsePartitaIVA(
  String codice
 ) throws ExPartitaIVA {

  if(codice == null)
   throw new ExPartitaIVA(CODICE_NON_VALIDO + "codice nullo!");

  if(codice.length() != 11)
   throw new ExPartitaIVA(CODICE_NON_VALIDO + "lunghezza del codice errata!");

  for(int i=0;i<codice.length();i++)
   if (!isNumero(codice.substring(i,i+1)))
	throw new ExPartitaIVA(CODICE_NON_VALIDO + "codice non numerico!");

  int aTotDispari=0;
  for(int idx=0;idx < codice.length() - 1; idx+=2)
   aTotDispari+=(new Integer(codice.substring(idx,idx+1))).intValue();

  int x=0;
  int aTot=aTotDispari;
  for(int idx=1;idx < codice.length(); idx+=2) {
   x=(new Integer(codice.substring(idx,idx+1))).intValue()*2;
   aTot+=x/10 + x%10;
  }

  int aCC=(new Integer(codice.substring(10,11))).intValue();
  if ((aTot%10 == 0) && (aCC == 0))
   return;
  else if(10-aTot%10 == aCC)
   return;
  else
   throw new ExPartitaIVA(CODICE_NON_VALIDO + "codice malformato!");
 }  
}
