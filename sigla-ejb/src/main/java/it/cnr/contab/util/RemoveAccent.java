package it.cnr.contab.util;

public class RemoveAccent {

	private static final String PLAIN_ASCII =
	      "AaEeIiOoUu"    // grave
	    + "AaEeIiOoUuYy"  // acute
	    + "AaEeIiOoUuYy"  // circumflex
	    + "AaEeIiOoUuYy"  // tilde
	    + "AaEeIiOoUuYy"  // umlaut
	    + "Aa"            // ring
	    + "Cc"            // cedilla
	    ;

	private static final String UNICODE =
	      "\u00C0\u00E0\u00C8\u00E8\u00CC\u00EC\u00D2\u00F2\u00D9\u00F9"             // grave
	    + "\u00C1\u00E1\u00C9\u00E9\u00CD\u00ED\u00D3\u00F3\u00DA\u00FA\u00DD\u00FD" // acute
	    + "\u00C2\u00E2\u00CA\u00EA\u00CE\u00EE\u00D4\u00F4\u00DB\u00FB\u0176\u0177" // circumflex
	    + "\u00C2\u00E2\u00CA\u00EA\u00CE\u00EE\u00D4\u00F4\u00DB\u00FB\u0176\u0177" // tilde
	    + "\u00C4\u00E4\u00CB\u00EB\u00CF\u00EF\u00D6\u00F6\u00DC\u00FC\u0178\u00FF" // umlaut
	    + "\u00C5\u00E5"                                                             // ring
	    + "\u00C7\u00E7"                                                             // cedilla
	    ;

	    // remove accentued from a string and replace with ascii equivalent
	    public static String convert(String s) {
	       StringBuffer sb = new StringBuffer();
	       int n = s.length();
	       for (int i = 0; i < n; i++) {
	          char c = s.charAt(i);
	          int pos = UNICODE.indexOf(c);
	          if (pos > -1){
	              sb.append(PLAIN_ASCII.charAt(pos));
	          }
	          else {
	              sb.append(c);
	          }
	       }
	       return sb.toString();
	    }
	    public static Boolean isOk(String s) {
	        if (s != null && s.length() != 0) {
	            for (int i = 0; i < s.length(); i++) {
	                if ((((int) RemoveAccent.convert(s).charAt(i)) < 31 ||
	                        ((int) RemoveAccent.convert(s).charAt(i)) > 126) &&
	                        (int) RemoveAccent.convert(s).charAt(i) != 13 &&
	                        (int) RemoveAccent.convert(s).charAt(i) != 10)
	                    return false;
	            }
	        }
	        return true;
	    }
}