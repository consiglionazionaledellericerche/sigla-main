<%@ page pageEncoding="UTF-8"
        import="it.cnr.jada.util.jsp.*,
                it.cnr.jada.action.*,
                java.util.*,
                it.cnr.jada.util.action.*,
                it.cnr.contab.utenze00.bp.*,
                it.cnr.contab.utenze00.bulk.*"
%>

<html>
<head>
<script language="JavaScript" src="../scripts/util.js"></script>
<script language="JavaScript">
 function chiudi(){    
   if (document.all.conferma.disabled == true)
     window.close();
 }
</script>
</head>
<body class="Form" onfocus="chiudi();">
<%
   it.cnr.jada.util.action.BulkListPrintBP bp = (it.cnr.jada.util.action.BulkListPrintBP)session.getAttribute("BP");
   String Pages = request.getParameter("main.Pages");
   int j = bp.numeriPagina(pageContext);
   int nrPage = 0;
   if (request.getParameter("nrPage") != null)
     nrPage = new Integer(request.getParameter("nrPage")).intValue();
   if (Pages != null)
   {
     response.resetBuffer();
     response.addHeader("Content-Disposition", "attachment; filename=\""+bp.getTitle()+".xls\"");
     response.setContentType("application/vnd.ms-excel");
     if (Pages.equals("one")) 
       bp.printExcel(pageContext,nrPage);
     else
       bp.printExcel(pageContext);  
     response.flushBuffer();
   }
   else
   {
   %>
     <title><%=bp.getTitle()%></title>
     <link rel="stylesheet" type="text/css" href="../style.css">   
     <form method=post name="excel">
       <table id="mainWindow" class="Form" width="100%" height="100%" cellspacing="0" cellpadding="2">
         <tr>
           <td colspan=2 align="left" valign="top"><input type="radio" name="main.Pages" value="all" onclick="javascript:document.all.nrPage.disabled = true;">
                        <span class="FormLabel">Tutte le Pagine</span></td>
         </tr>
         <tr>
           <td align="left" valign="top"><input type="radio" name="main.Pages" value="one" onclick="javascript:document.all.nrPage.disabled = false;">
                        <span class="FormLabel">Nr. Pagina :</span>
           </td>
           <td align="left" valign="top">             
              <select name="nrPage" disabled>
                <% for(int i=0;i<j;i++){%> 
                <option value="<%=i%>"><%=i%></option>
                <%}%>
              </select>          
           </td>
         </tr>

         <tr>
           <td align="left" valign="bottom"><input class="Button" style="width:90px;" name="annulla" type="button" value="Annulla" onClick="javascript:window.close();"></td>         
           <td align="right" valign="bottom"><input class="Button" style="width:90px;" name="conferma" type="submit" value="Conferma" onclick="javascript:document.all.conferma.disabled = true;javascript:document.forms[0].submit();"></td>
         </tr>
       </table>
     </form>
   <%
   }
%>
</body>
</html>
