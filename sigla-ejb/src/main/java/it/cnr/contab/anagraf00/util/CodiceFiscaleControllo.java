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

package it.cnr.contab.anagraf00.util;

import java.util.*;

import it.cnr.jada.util.*;

/**
 * Classe per il controllo dell'esattezza del codice Fiscale
 * Gestisce il controllo e il calcolo del codice fiscale sulla base di dizionari statici interni
 */

public class CodiceFiscaleControllo {
	
 static String CODICE_NON_VALIDO = "Codice fiscale non valido: ";
 
 static String[] Vocali = {"A","E", "I", "U", "O"};
 static String[] Consonanti = {"B","C", "D", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "V", "W", "X", "Y", "Z"};
 static String[] Numeri = {"0", "1","2", "3", "4", "5", "6", "7", "8", "9"};

 static String[] Alfanum = {"A","B","C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1","2", "3", "4", "5", "6", "7", "8", "9"};
 
 static String[] CodificaMesi = {"A","B", "C", "D", "E", "H","L", "M", "P", "R", "S", "T"};
 
 static String[] MapCC   = {"A","B","C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};


 
 static Hashtable MapVocaliAccentate;
 static Hashtable  MapCCDispari, MapCCPari;

 static {
   MapVocaliAccentate = new Hashtable();
   MapVocaliAccentate.put("à".toUpperCase(),"A");
   MapVocaliAccentate.put("á".toUpperCase(),"A");
   MapVocaliAccentate.put("è".toUpperCase(),"E");
   MapVocaliAccentate.put("é".toUpperCase(),"E");
   MapVocaliAccentate.put("ò".toUpperCase(),"O");
   MapVocaliAccentate.put("ó".toUpperCase(),"O");
   MapVocaliAccentate.put("ù".toUpperCase(),"U");
   MapVocaliAccentate.put("ú".toUpperCase(),"U");
   MapVocaliAccentate.put("ì".toUpperCase(),"I");	 
   MapVocaliAccentate.put("í".toUpperCase(),"I");	 

   MapCCDispari = new Hashtable();
   MapCCDispari.put("A", new Integer(1));
   MapCCDispari.put("0", new Integer(1));
   MapCCDispari.put("B", new Integer(0));
   MapCCDispari.put("1", new Integer(0));
   MapCCDispari.put("C", new Integer(5));
   MapCCDispari.put("2", new Integer(5));
   MapCCDispari.put("D", new Integer(7));
   MapCCDispari.put("3", new Integer(7));
   MapCCDispari.put("E", new Integer(9));
   MapCCDispari.put("4", new Integer(9));
   MapCCDispari.put("F", new Integer(13));
   MapCCDispari.put("5", new Integer(13));
   MapCCDispari.put("G", new Integer(15));
   MapCCDispari.put("6", new Integer(15));
   MapCCDispari.put("H", new Integer(17));
   MapCCDispari.put("7", new Integer(17));
   MapCCDispari.put("I", new Integer(19));
   MapCCDispari.put("8", new Integer(19));
   MapCCDispari.put("J", new Integer(21));
   MapCCDispari.put("9", new Integer(21));
   MapCCDispari.put("K", new Integer(2));
   MapCCDispari.put("L", new Integer(4));
   MapCCDispari.put("M", new Integer(18));
   MapCCDispari.put("N", new Integer(20));
   MapCCDispari.put("O", new Integer(11));
   MapCCDispari.put("P", new Integer(3));
   MapCCDispari.put("Q", new Integer(6));
   MapCCDispari.put("R", new Integer(8));
   MapCCDispari.put("S", new Integer(12));
   MapCCDispari.put("T", new Integer(14));
   MapCCDispari.put("U", new Integer(16));
   MapCCDispari.put("V", new Integer(10));
   MapCCDispari.put("W", new Integer(22));
   MapCCDispari.put("X", new Integer(25));
   MapCCDispari.put("Y", new Integer(24));
   MapCCDispari.put("Z", new Integer(23));

   MapCCPari = new Hashtable();
   MapCCPari.put("A", new Integer(0));
   MapCCPari.put("0", new Integer(0));
   MapCCPari.put("B", new Integer(1));
   MapCCPari.put("1", new Integer(1));
   MapCCPari.put("C", new Integer(2));
   MapCCPari.put("2", new Integer(2));
   MapCCPari.put("D", new Integer(3));
   MapCCPari.put("3", new Integer(3));
   MapCCPari.put("E", new Integer(4));
   MapCCPari.put("4", new Integer(4));
   MapCCPari.put("F", new Integer(5));
   MapCCPari.put("5", new Integer(5));
   MapCCPari.put("G", new Integer(6));
   MapCCPari.put("6", new Integer(6));
   MapCCPari.put("H", new Integer(7));
   MapCCPari.put("7", new Integer(7));
   MapCCPari.put("I", new Integer(8));
   MapCCPari.put("8", new Integer(8));
   MapCCPari.put("J", new Integer(9));
   MapCCPari.put("9", new Integer(9));
   MapCCPari.put("K", new Integer(10));
   MapCCPari.put("L", new Integer(11));
   MapCCPari.put("M", new Integer(12));
   MapCCPari.put("N", new Integer(13));
   MapCCPari.put("O", new Integer(14));
   MapCCPari.put("P", new Integer(15));
   MapCCPari.put("Q", new Integer(16));
   MapCCPari.put("R", new Integer(17));
   MapCCPari.put("S", new Integer(18));
   MapCCPari.put("T", new Integer(19));
   MapCCPari.put("U", new Integer(20));
   MapCCPari.put("V", new Integer(21));
   MapCCPari.put("W", new Integer(22));
   MapCCPari.put("X", new Integer(23));
   MapCCPari.put("Y", new Integer(24));
   MapCCPari.put("Z", new Integer(25)); 
 };
/**
 * Calcola la parte di CF corrispondente al cognome
 * @param aS cognome
 */

public static String calcolaCodCognome(String aS) {
  
  String aCodCognomeCalcolato="";

  String aSVocali = vocali(aS);
  String aSConsonanti = consonanti(aS);

  if(aSConsonanti.length() >= 3)
   aCodCognomeCalcolato = aSConsonanti.substring(0,3);
  else if(aS.length() >= 3)
   aCodCognomeCalcolato = aSConsonanti + aSVocali.substring(0, (3 - aSConsonanti.length()) );

  if(aS.length() == 2) {
   aCodCognomeCalcolato = aSConsonanti + aSVocali + "X";
  }
  if(aS.length() == 1) {
   aCodCognomeCalcolato = aSConsonanti + aSVocali + "XX";
  }
  
  return aCodCognomeCalcolato;
}
/**
 * Calcola la parte di CF corrispondente al nome
 * @param aS nome
 */

public static String calcolaCodNome(String aS) {
  
  String aCodNomeCalcolato="";

  String aSVocali = vocali(aS);
  String aSConsonanti = consonanti(aS);

  if(aSConsonanti.length() >= 4)
   aSConsonanti = aSConsonanti.substring(0,1) + aSConsonanti.substring(2,4);

  if(aSConsonanti.length() >= 3)
   aCodNomeCalcolato = aSConsonanti.substring(0,3);
  else if(aS.length() >= 3)
   aCodNomeCalcolato = aSConsonanti + aSVocali.substring(0, (3 - aSConsonanti.length()));

  if(aS.length() == 2) {
   aCodNomeCalcolato = aSConsonanti + aSVocali + "X";
  }
  if(aS.length() == 1) {
   aCodNomeCalcolato = aSConsonanti + aSVocali + "XX";
  }
  return aCodNomeCalcolato;
}
/**
 * Controllo correttezza del carattere di controllo terminale del CF
 * @param codice CF completo da verificare
 */

 public static boolean checkCC(String codice){
  // Il codice passato per il controllo ha lunghezza n-1 dove n è la lunghezza del codice fiscale
  return getCC(codice.substring(0,codice.length() - 1)).equals(codice.substring(15,16));
 }                                   
/**
 * Ritorna una stringa composta dalle consonanti di aS
 * @param aS Stringa da cui estrarre le consonanti
 */

 private static String consonanti(String aS){

  String aOut="";
  for(int iC=0;iC<aS.length();iC++) {
   String aC=aS.substring(iC,iC + 1);
   if(isConsonante(aC))
	aOut+=aC;
  }
  return aOut;
 }                     
/**
 * Ritorna una stringa composta della sola parte di caratteri alfanumerici uppercase di aS
 * @param aS Stringa su cui si opera
 */

 private static String getAlfanumUppercase(String aS){

  if (aS == null) return aS;
  String aLocS=aS.toUpperCase();
  String aOutS="";
  for(int i=0;i < aLocS.length(); i++) {
   String aC=aLocS.substring(i,i+1);
   if(isAlfanum(aC))
	aOutS+=aLocS.substring(i,i+1);
   if(isVocaleAccentata(aC))
	aOutS+=MapVocaliAccentate.get(aC);
  }
  
  return aOutS;

 }                             
/**
 * Ritorna il carattere di controllo del (CC) CF
 * @param codice Il CF tranne l'ultimo carattere su cui viene calcolato il CC
 */

 private static String getCC(String codice){

  int aTotDispari=0;
  int aTotPari=0;

  // ATTENZIONE!!! La posizione 0 rappresenta la prima posizione dispari! 
  for(int i=0;i<codice.length();i+=2) // parse dispari
   aTotDispari+=((Integer)MapCCDispari.get(codice.substring(i,i+1))).intValue();

  for(int i=1;i<codice.length();i+=2) // parse pari
   aTotPari+=((Integer)MapCCPari.get(codice.substring(i,i+1))).intValue();

  int aTotResto = (aTotPari + aTotDispari)%26;

  return MapCC[aTotResto];
 }                                    
/**
 * Ritorna una stringa composta della sola parte di caratteri alfanumerici uppercase di aS
 *
 * Le stringhe in ingresso sono filtrate sulla base dei dizionari statici impostati nella classe.
 *
 * In particolare vengono soppressi caratteri non alfanumerici base e si lavora su Uppercase
 * Solo le vocali accentate vengono gestite in automatico come vocali: in particolare nel calcolo
 * del CF vengono sostitutite con le rispettive lettere maiuscole non accentate.
 *
 * @param aCognome cognome
 * @param aNome nome
 * @param aAnnoNascita anno di nascita (ultimi due caratteri dell'anno)
 * @param aMeseNascita mese di nascita (2 caratteri range: 01-12)
 * @param aGiornoNascita giorno di nascita (2 caratteri  range: 01-31)
 * @param aSesso sesso ("M"=Maschile "F"=Femminile)
 * @param aCdComuneNascita codice qualificatore del Comune di Nascita o Stato Estero
 * @return il codice fiscale calcolato
 */

 public static String getCodiceFiscale(
  String aCognome,
  String aNome,
  String aAnnoNascita,
  String aMeseNascita,
  String aGiornoNascita,
  String aSesso,
  String aCdComuneNascita
 ) {

  String cognome = getAlfanumUppercase(aCognome);
  String nome = getAlfanumUppercase(aNome);
  String annoNascita = getNumber(aAnnoNascita);
  String meseNascita = getNumber(aMeseNascita);
  String giornoNascita = getNumber(aGiornoNascita);
  String sesso = getAlfanumUppercase(aSesso);
  String cdComuneNascita = getAlfanumUppercase(aCdComuneNascita);
  
  String aCdFiscale="";

  aCdFiscale+=calcolaCodCognome(cognome);	
  aCdFiscale+=calcolaCodNome(nome);	
  aCdFiscale+=annoNascita;	
  aCdFiscale+=CodificaMesi[new Integer(meseNascita).intValue() - 1];
  
  if(sesso.equals("M")) { // x i maschi
   aCdFiscale+=StrServ.lpad(giornoNascita,2,"0");
  } else { // x le femmine
   aCdFiscale+=(new Integer(new Integer(giornoNascita).intValue() + 40)).toString();
  }
  
  aCdFiscale+=cdComuneNascita;

  aCdFiscale+=getCC(aCdFiscale);

  return aCdFiscale;
}                                                                    
 private static String getNumber(String aS){

  String aLocS=aS.toUpperCase();
  String aOutS="";
  for(int i=0;i < aLocS.length(); i++) {
   String aC=aLocS.substring(i,i+1);
   if(isNumero(aC))
	aOutS+=aC;
  }
  if(aOutS.length() == 1) aOutS = "0"+aOutS;
  return aOutS;

 }                      
 private static boolean isAlfanum(String aC){
  
  for(int i=0;i < Alfanum.length; i++)
   if(Alfanum[i].equals(aC)) return true;

  return false;

 }                    
 private static boolean isConsonante(String aC){
  
  for(int i=0;i < Consonanti.length; i++)
   if(Consonanti[i].equals(aC)) return true;

  return false;

 }                  
 protected static boolean isNumero(String aC){
  
  for(int i=0;i < Numeri.length; i++)
   if(Numeri[i].equals(aC)) return true;

  return false;

 }               
 private static boolean isVocale(String aC){
  
  for(int i=0;i < Vocali.length; i++)
   if(Vocali[i].equals(aC)) return true;

  return false;

 }                
 private static boolean isVocaleAccentata(String aC){
  return MapVocaliAccentate.containsKey(aC);
 }                      


	/**
	 * Controlla il codice fiscale sulla base degli altri parametri passati
	 *
	 * Le stringhe in ingresso sono filtrate sulla base dei dizionari statici impostati nella classe.
	 *
	 * In particolare vengono soppressi caratteri non alfanumerici base e si lavora su Uppercase
	 * Solo le vocali accentate vengono gestite in automatico come vocali: in particolare nel calcolo
	 * del CF vengono sostitutite con le rispettive lettere maiuscole non accentate.
	 *
	 * @param cognome cognome
	 * @param nome nome
	 * @param annoNascita anno di nascita (ultimi due caratteri dell'anno)
	 * @param meseNascita mese di nascita (range: 1-12)
	 * @param giornoNascita giorno di nascita (caratteri  range: 1-31)
	 * @param sesso sesso ("M"=Maschile "F"=Femminile)
	 * @param pgComuneNascita codice qualificatore del Comune di Nascita o Stato Estero
	 * @param connection connessione di lavoro
	 */
	public static String makeCodiceFiscale(
		String cognome,
		String nome,
		String annoNascita,
		String meseNascita,
		String giornoNascita,
		String sesso,
		String codiceCatastale)
	{

		String codice = "";

		cognome = getAlfanumUppercase(cognome);
		nome = getAlfanumUppercase(nome);
		annoNascita = getNumber(annoNascita);
		meseNascita = getNumber(meseNascita);
		giornoNascita = getNumber(giornoNascita);
		sesso = getAlfanumUppercase(sesso);

		//Cognome
		codice += calcolaCodCognome(cognome);

		//Nome
		codice += calcolaCodNome(nome);

		//Anno di nascita
		codice += annoNascita;

		 //Mese di nascita
		codice += CodificaMesi[new Integer(meseNascita).intValue()];

		//Giorno di nascita-sesso
		if(sesso.equals("M")) // x i maschi
			codice += giornoNascita;
		else // x le femmine
			codice += (new Integer(new Integer(giornoNascita).intValue() + 40)).toString();

		try {
			//Comune di nascita
			codice += codiceCatastale;
		} catch(Throwable e) {
			codice += "XXXX";
		}

		//Carattere di controllo
		codice += getCC(codice);

		return codice;
	}

/**
 * Controlla il codice fiscale sulla base degli altri parametri passati
 *
 * Le stringhe in ingresso sono filtrate sulla base dei dizionari statici impostati nella classe.
 *
 * In particolare vengono soppressi caratteri non alfanumerici base e si lavora su Uppercase
 * Solo le vocali accentate vengono gestite in automatico come vocali: in particolare nel calcolo
 * del CF vengono sostitutite con le rispettive lettere maiuscole non accentate.
 *
 * @param aCognome cognome
 * @param aNome nome
 * @param aAnnoNascita anno di nascita (ultimi due caratteri dell'anno)
 * @param aMeseNascita mese di nascita (range: 1-12)
 * @param aGiornoNascita giorno di nascita (caratteri  range: 1-31)
 * @param aSesso sesso ("M"=Maschile "F"=Femminile)
 * @param aCdComuneNascita codice qualificatore del Comune di Nascita o Stato Estero
 * @param aCdFiscale CF da controllare
 */

 public static void parseCodiceFiscale(
  String aCognome,
  String aNome,
  String aAnnoNascita,
  String aMeseNascita,
  String aGiornoNascita,
  String aSesso,
  String aCdComuneNascita,
  String aCdFiscale
 ) throws ExCodiceFiscale{

  String cognome = getAlfanumUppercase(aCognome);
  String nome = getAlfanumUppercase(aNome);
  String annoNascita = getNumber(aAnnoNascita);
  String meseNascita = getNumber(aMeseNascita);
  String giornoNascita = getNumber(aGiornoNascita);
  String sesso = getAlfanumUppercase(aSesso);
  String cdComuneNascita = getAlfanumUppercase(aCdComuneNascita);
   
  String codice = getAlfanumUppercase(aCdFiscale);

  if(codice == null)
   throw new ExCodiceFiscale(CODICE_NON_VALIDO + "codice fiscale nullo!");

  if(codice.length() != 16)
   throw new ExCodiceFiscale(CODICE_NON_VALIDO + "lunghezza del codice errata!");

  String aCodCognome = codice.substring(0,3);
  String aCodNome = codice.substring(3,6);
  String aCodAnnoNascita = codice.substring(6,8);
  String aCodMeseNascita = codice.substring(8,9);
  String aCodGiornoNascita = codice.substring(9,11);
  String aCodComuneNascita = codice.substring(11,15);
  String aCodControllo = codice.substring(15,16);

 // Controllo Cognome
  
  String aCodCognomeCalcolato=calcolaCodCognome(cognome);	
  if(!aCodCognome.equals(aCodCognomeCalcolato))
   throw new ExCodiceFiscale(CODICE_NON_VALIDO + "cognome!");

 // Controllo nome

  String aCodNomeCalcolato=calcolaCodNome(nome);	
  if(!aCodNome.equals(aCodNomeCalcolato))
   throw new ExCodiceFiscale(CODICE_NON_VALIDO + "nome!");

 //Controllo anno di nascita
  
  if(!annoNascita.equals(aCodAnnoNascita))
   throw new ExCodiceFiscale(CODICE_NON_VALIDO + "anno!");

 //Controllo mese di nascita

  if(!CodificaMesi[new Integer(meseNascita).intValue()].equals(aCodMeseNascita.substring(0,1)))
   throw new ExCodiceFiscale(CODICE_NON_VALIDO + "mese!");

 //Controllo giorno di nascita-sesso
// Err. 783 - BORRIELLO: gestita l'eccezione NumberFormatException, che viene generata nel caso in cui nel giorno sia presente una lettera al posto di un numero. 
  try{
	  if(sesso.equals("M")) { // x i maschi
	   if(!new Integer(giornoNascita).equals(new Integer(aCodGiornoNascita)))
		throw new ExCodiceFiscale(CODICE_NON_VALIDO + "giorno di nascita o sesso non corretti!");
	  } else { // x le femmine
	   if(!new Integer(new Integer(giornoNascita).intValue() + 40).equals(new Integer(aCodGiornoNascita)))
		throw new ExCodiceFiscale(CODICE_NON_VALIDO + "giorno di nascita o sesso non corretti!");
	  }
  } catch (NumberFormatException nfe){
	  throw new ExCodiceFiscale(CODICE_NON_VALIDO + "giorno di nascita o sesso non corretti!");
  }
  
 // Controllo comune di nascita: attualmente non effettuato
 if (aCdComuneNascita != null && !cdComuneNascita.equals(aCodComuneNascita))
   throw new ExCodiceFiscale(CODICE_NON_VALIDO + "codice comune di nascita non corretto!");

  if(!checkCC(codice))
   throw new ExCodiceFiscale(CODICE_NON_VALIDO + "errato carattere di controllo!");
 }                                                                              
/**
 * Ritorna una stringa composta dalle vocali di aS
 * @param aS Stringa da cui estrarre le vocali
 */

 private static String vocali(String aS){

  String aOut="";
  for(int iC=0;iC<aS.length();iC++) {
   String aC = aS.substring(iC,iC+1);
   if(isVocale(aC))
	aOut+=aC;
  }
  return aOut;
 }                      
}
