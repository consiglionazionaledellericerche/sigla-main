--------------------------------------------------------
--  DDL for Package CAL_ASSESTATO
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CAL_ASSESTATO" IS
FUNCTION ASSESTATO_ENTRATA(
  P_Anno        IN number ,
  P_dip         IN varchar2 ,
  P_CDS         IN varchar2 ,
  P_UO          IN varchar2 ,
  P_CDR         IN varchar2 ,
  P_ELE_VOCE    IN varchar2 ,
  P_LINEA       IN varchar2 ,
  P_PG_VAR      IN varchar2 ,
  P_LIV1        IN varchar2 ,
  P_LIV2        IN varchar2 ,
  P_LIV3        IN varchar2 ,
  P_LIV4        IN varchar2 ,
  P_LIV5        IN varchar2 ,
  P_LIV6        IN varchar2 ,
  P_LIV7        IN varchar2 ,
  P_Stringa     IN varchar2 )
RETURN NUMBER;

FUNCTION ASSESTATO_SPESA(
  P_Anno        IN number ,
  P_dip         IN varchar2 ,
  P_CDS         IN varchar2 ,
  P_UO          IN varchar2 ,
  P_CDR         IN varchar2 ,
  P_ELE_VOCE    IN varchar2 ,
  P_LINEA       IN varchar2 ,
  P_PG_VAR      IN varchar2 ,
  P_LIV1        IN varchar2 ,
  P_LIV2        IN varchar2 ,
  P_LIV3        IN varchar2 ,
  P_LIV4        IN varchar2 ,
  P_LIV5        IN varchar2 ,
  P_LIV6        IN varchar2 ,
  P_LIV7        IN varchar2 ,
  P_Stringa     IN varchar2 )
RETURN NUMBER;

FUNCTION ASSESTATO_SPESA_RES(
  P_Anno        IN number ,
  P_Anno_RES    IN number ,
  P_CDS         IN varchar2 ,
  P_UO          IN varchar2 ,
  P_CDR         IN varchar2 ,
  P_ELE_VOCE    IN varchar2 ,
  P_LINEA       IN varchar2 ,
  P_Stringa     IN varchar2 )
RETURN NUMBER;


END CAL_ASSESTATO;
