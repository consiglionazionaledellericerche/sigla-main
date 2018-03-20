//
// htmlArea v2.02 - Copyright (c) 2002 interactivetools.com, inc.
// This copyright notice MUST stay intact for use (see license.txt).
//
// A free WYSIWYG editor replacement for <textarea> fields.
//

// write out styles for UI buttons
document.write('<style type="text/css">\n');
document.write('.btn     { width: 22px; height: 22px; border: 1px solid buttonface; margin: 0; padding: 0 }\n');
document.write('.btnOver { width: 22px; height: 22px; border: 1px outset; }\n');
document.write('.btnDown { width: 22px; height: 22px; border: 1px inset; background-color: buttonhighlight; }\n');
document.write('.btnNA   { width: 22px; height: 22px; border: 1px solid buttonface; filter: alpha(opacity=25); }\n');
document.write('.cMenu     { background-color: threedface; color: menutext; cursor: Default; font-family: MS Sans Serif; font-size: 8pt; padding: 2 12 2 16; }');
document.write('.cMenuOver { background-color: highlight; color: highlighttext; cursor: Default; font-family: MS Sans Serif; font-size: 8pt; padding: 2 12 2 16; }');
document.write('.cMenuDivOuter { background-color: threedface; height: 9 }');
document.write('.cMenuDivInner { margin: 0 4 0 4; border-width: 1; border-style: solid; border-color: threedshadow threedhighlight threedhighlight threedshadow; }');
document.write('</style>\n');

/* ---------------------------------------------------------------------- *\
  Function    : editor_defaultConfig
  Description : default configuration settings for wysiwyg editor
\* ---------------------------------------------------------------------- */
function editor_defaultConfig(objname) {

this.version = "2.03.1"

this.width =  "640px";
this.height = "auto";
this.bodyStyle = 'background-color: white; font-family: "Times New Roman"; font-size: x-small; border-style: ridge; border-color: #D8D8D8';
this.bordersStyle = 'border:1px dotted #ff0000;height:20px';
this.imgURL = _editor_url + 'img/';
this.debug  = 0;

this.replaceNextlines = 0; // replace nextlines from spaces (on output)
this.plaintextInput = 0;   // replace nextlines with breaks (on input)

this.toolbar = [
//Row 1: Font Related Tools
    ['fontname'],['fontsize'],
//      ['fontstyle'],['formatblock'],
        //Persistent Buttons
        ['linebreak'],
//      ['help'],


//Row 2: File, Edit, Format Tools
        //File

//      'preview','print','separator',
        //Edit
        ['selectall','cut','copy','paste','delete','remove','undo','redo',

        'separator',
        //Format Text Style
        'bold','italic','underline','strikethrough','subscript','superscript','forecolor','backcolor','separator',
        // Format Text Alignment
        'justifyleft','justifycenter','justifyright','justifyfull','justifynone','separator',
        //Format Text Block
        'OrderedList','UnOrderedList','Outdent','Indent','specchar'
        //Format Text Direction
        //,'separator','cleancode','separator','Smiley'
        ]
    ];

this.fontnames = {
    "Arial":           "arial, helvetica, sans-serif",
        //"Aharoni":          "Aharoni",
    "Courier New":     "courier new, courier, mono",
    "Georgia":         "Georgia, Times New Roman, Times, Serif",
    "Tahoma":          "Tahoma, Arial, Helvetica, sans-serif",
    "Times New Roman": "times new roman, times, serif",
    "Verdana":         "Verdana, Arial, Helvetica, sans-serif",
        //"David":           "David",
    "Impact":          "impact",
        "WingDings":       "WingDings"};

this.fontsizes = {
    "1 (8 pt)":  "1",
    "2 (10 pt)": "2",
    "3 (12 pt)": "3",
    "4 (14 pt)": "4",
    "5 (18 pt)": "5",
    "6 (24 pt)": "6",
    "7 (36 pt)": "7"
  };


this.btnList = {
//       buttonName:  commandID,               title,                               onclick,                           image,
//Row 1: Font Related Tools
//Persistent Buttons
    "htmlmode":       ['HtmlMode',             'View HTML Source',                  'editor_setmode(\''+objname+'\')', 'ed_html.gif'],
    "popupeditor":    ['popupeditor',          'Allarga finestra',                  'editor_action(this.id)',          'fullscreen_maximize.gif'],
    "about":          ['about',                'About',                             'editor_about(\''+objname+'\')',   'ed_about.gif'],
    "help":           ['showhelp',             'Help',                              'editor_action(this.id)',          'ed_help.gif'],
//Row 2: File, Edit, Format Tools
//File
    "refresh":        ['Refresh',              'New',                               'editor_action(this.id)',          'ed_refresh.gif'],
//Edit
    "selectall":      ['selectall',            'Seleziona tutto',                        'editor_action(this.id)',          'ed_selectall.gif'],
    "cut":            ['Cut',                  'Taglia',                               'editor_action(this.id)',          'ed_cut.gif'],
    "copy":           ['Copy',                 'Copia',                              'editor_action(this.id)',          'ed_copy.gif'],
    "paste":          ['Paste',                'Incolla',                             'editor_action(this.id)',          'ed_paste.gif'],
    "delete":         ['delete',               'Cancella',                  'editor_action(this.id)',          'ed_delete.gif'],
    "remove":         ['RemoveFormat',         'Rimuovi formattazione nel testo selezionato',    'editor_action(this.id)',          'ed_remove.gif'],
    "undo":           ['Undo',                 'Indietro',                  'editor_action(this.id)',          'ed_undo.gif'],
    "redo":           ['Redo',                 'Avanti',                              'editor_action(this.id)',          'ed_redo.gif'],
    "find":           ['Find',                 'Trova',                              'editor_action(this.id)',          'ed_find.gif'],
//Format Text Style
    "bold":           ['Bold',                 'Grassetto',                  'editor_action(this.id)',          'ed_format_bold.gif'],
    "italic":         ['Italic',               'Corsivo',                        'editor_action(this.id)',          'ed_format_italic.gif'],
    "underline":          ['Underline',            'Sottolineato',                                         'editor_action(this.id)',  'ed_format_underline.gif'],
    "strikethrough":      ['StrikeThrough',        'Strikethrough',                                     'editor_action(this.id)',  'ed_format_strike.gif'],
    "subscript":          ['SubScript',            'Pedice',                                         'editor_action(this.id)',  'ed_format_sub.gif'],
    "superscript":        ['SuperScript',          'Apice',                                       'editor_action(this.id)',  'ed_format_sup.gif'],
// Format Text Alignment
        "justifyleft":            ['JustifyLeft',          'Allinea a sinistra',                                      'editor_action(this.id)',  'ed_align_left.gif'],
    "justifycenter":      ['JustifyCenter',        'Centra',                                    'editor_action(this.id)',  'ed_align_center.gif'],
    "justifyright":       ['JustifyRight',         'Allinea a destra',                                     'editor_action(this.id)',  'ed_align_right.gif'],
    "justifyfull":        ['JustifyFull',          'Giustifica',                                              'editor_action(this.id)', 'ed_align_justify.gif'],
        "justifynone":            ['JustifyNone',                  'Elimina allineamento',                                   'editor_action(this.id)', 'ed_align_none.gif'],
//Format Text Block
        "orderedlist":            ['InsertOrderedList',    'Elenco numerato',                                      'editor_action(this.id)',  'ed_list_num.gif'],
    "unorderedlist":      ['InsertUnorderedList',  'Elenco puntato',                                     'editor_action(this.id)',  'ed_list_bullet.gif'],
    "outdent":            ['Outdent',              'Riduci rientro',                                   'editor_action(this.id)',  'ed_indent_less.gif'],
    "indent":             ['Indent',               'Aumenta rientro',                                   'editor_action(this.id)',  'ed_indent_more.gif'],
    "forecolor":          ['ForeColor',            'Colore carattere',                                        'editor_action(this.id)',  'ed_color_fg.gif'],
    "backcolor":          ['BackColor',            'Evidenzia',                                  'editor_action(this.id)',  'ed_color_bg.gif'],
//Format Text Direction  and cleanup

    "cleancode":          ['cleancode',             'Sweep MS-WORD tags" ',  'editor_action(this.id)',  'ed_word.gif'],
        // Upload files and images
        "upload":                       ['upload',                                      'Upload File',                                          'editor_action(this.id)',       'ed_upload.gif'],
//#Row 3: Insert/Modify Tools
        "paragraph":              ['InsertParagraph',      'New paragraph at insertion point',  'editor_action(this.id)',       'ed_paragraph.gif'],
    "marquee":                    ['marquee',              'Marquee',                                           'editor_action(this.id)',       'ed_marquee.gif'],
    "line":                   ['line',                 'Horizontal Rule',                               'editor_action(this.id)',  'ed_line.gif'],
    "specchar":                   ['SpecChar',             'Inserisci caratteri speciali',                 'editor_action(this.id)',       'ed_spec_char.gif'],
//Hyperlinks and Images
        "insertlink":             ['insertlink',           'Hyperlink',                                 'editor_action(this.id)',  'ed_link.gif'],
    "unlink":                     ['unlink',               'Remove Link',                                       'editor_action(this.id)',   'ed_unlink.gif'],
    "anchor":                     ['anchor',               'Anchor',                                            'editor_action(this.id)',   'ed_anchor.gif'],
    "insertimage":        ['InsertImage',          'Insert Image',                                      'editor_action(this.id)',  'ed_image.gif'],
 //  "createlink":                ['createlink',           'Hyperlink in Image',                        'editor_action(this.id)',  'ed_linkimage.gif'],

//Tables
        "inserttable":            ['InsertTable',          'Insert Table',                                      'editor_action(this.id)',  'insert_table.gif'],
        "tableproperties":    ['TableProperties',      'Table Properties',                              'editor_action(this.id)',  'ed_tableprop.gif'],
    "showborder":                 ['ShowBorder',           'Show 0 borders',                                    'editor_action(this.id)',  'ed_show_border.gif'],
// Table Properties inserted by lvn
//Table Rows
        "rowproperties":      ['RowProperties',        'Row Properties',                                'editor_action(this.id)',  'ed_rowprop.gif'],
    "insertrowbefore":    ['InsertRowBefore',      'Insert Row Before',                                 'editor_action(this.id)',  'ed_insabove.gif'],
    "insertrowafter":     ['InsertRowAfter',       'Insert Row After',                                  'editor_action(this.id)',  'ed_insunder.gif'],
    "deleterow":          ['DeleteRow',            'Delete Row',                                        'editor_action(this.id)',  'ed_delrow.gif'],
        "splitrow":           ['SplitRow',             'Split row',                                             'editor_action(this.id)',  'ed_splitrow.gif'],
    "mergerows":          ['MergeRows',            'Merge rows',                                        'editor_action(this.id)',  'ed_mergerows.gif'],
//Table Columns
        "insertcolumnbefore": ['InsertColumnBefore',   'Insert Column Before',                          'editor_action(this.id)',  'ed_insleft.gif'],
    "insertcolumnafter":  ['InsertColumnAfter',    'Insert Column Afer',                                'editor_action(this.id)',  'ed_insright.gif'],
    "deletecolumn":       ['DeleteColumn',         'Delete Column',                                     'editor_action(this.id)',  'ed_delcol.gif'],
//Table Cells
        "cellproperties":     ['CellProperties',       'Cell Properties',                               'editor_action(this.id)',  'ed_cellprop.gif'],
    "insertcellbefore":   ['InsertCellBefore',     'Insert Cell Before',                                'editor_action(this.id)',  'ed_inscellft.gif'],
    "insertcellafter":    ['InsertCellAfter',      'Insert Cell After',                                 'editor_action(this.id)',  'ed_inscelrgt.gif'],
    "deletecell":         ['DeleteCell',           'Delete Cell',                                       'editor_action(this.id)',  'ed_delcel.gif'],
    "splitcell":          ['SplitCell',            'Split Cell',                                        'editor_action(this.id)',  'ed_splitcel.gif'],
    "mergecells":         ['MergeCells',           'Merge Cells',                                       'editor_action(this.id)',  'ed_mergecels.gif'],
// end insert by lvn
//Forms
        "inputform"       :       ['InputForm',            'Form',                                              'editor_action(this.id)',       'ed_form.gif'],
        "inputformelement":       ['InputFormElement',     'Form Element',                                      'editor_action(this.id)',       'ed_form_element.gif'],
// Add custom buttons here:
        "smiley":               ['smiley',         'Insert smiley',                                             'editor_action(this.id)',  'ed_smiley.gif'],
        "changecase":           ['changecase',        'Cambia Maiuscolo/Minuscolo',                                     'editor_action(this.id)',  'ed_changecase.gif']
// end: custom buttons

    };

// insert by lvn : check editor changes
this.checkChanges = 0;
}
/* ---------------------------------------------------------------------- *\
  Function    : editor_generate
  Description : replace textarea with wysiwyg editor
  Usage       : editor_generate("textarea_id",[height],[width]);
  Arguments   : objname - ID of textarea to replace
                w       - width of wysiwyg editor
                h       - height of wysiwyg editor
\* ---------------------------------------------------------------------- */
                function editor_generate(objname,userConfig) {
                	
// Default Settings
                var config = new editor_defaultConfig(objname);
                if (userConfig) {
                  for (var thisName in userConfig) {
                    if (userConfig[thisName]) { 
                    	config[thisName] = userConfig[thisName]; 
                    }
                   }
                }
  document.getElementById(objname).config = config; // store configuration settings
  // set size to specified size or size of original object
  var obj    = document.getElementById(objname);
  if (!config.width || config.width == "auto") {
    if      (obj.style.width) { config.width = obj.style.width; }      // use css style
    else if (obj.cols)        { config.width = (obj.cols * 8) + 22; }  // col width + toolbar
    else                      { config.width = '100%'; }               // default
  }
  if (!config.height || config.height == "auto") {
    if      (obj.style.height) { config.height = obj.style.height; }   // use css style
    else if (obj.rows)         { config.height = obj.rows * 17 }       // row height
    else                       { config.height = '200'; }              // default
  }

  var tblOpen  = '<table border=1 cellspacing=0 cellpadding=0 style="float: left;"  unselectable="on"><tr><td style="border: none; padding: 1 0 0 0; font-family: MS Shell Dlg;"><nobr>';
  var tblClose = '</nobr></td></tr></table>\n';

  // build button toolbar

  var toolbar = '';
  var btnGroup, btnItem, aboutEditor;
  for (var btnGroup in config.toolbar) {

    // linebreak
    if (config.toolbar[btnGroup].length == 1 &&
        config.toolbar[btnGroup][0].toLowerCase() == "linebreak") {
      toolbar += '<br clear="all">';
      continue;
    }

    toolbar += tblOpen;
    for (var btnItem in config.toolbar[btnGroup]) {
      var btnName = config.toolbar[btnGroup][btnItem].toLowerCase();

// inserted by lvn
      // formatblock
      if (btnName == "formatblock") {
        toolbar += '  Format <select id="_' +objname+ '_FormatBlock" onchange="editor_action(this.id)" unselectable="on" style="margin: 1 2 0 2; font-size: 12px;">';
        for (var i in config.formatblocks) {
           var fbObj = config.formatblocks[i];
           var fbvalue = "";
           var fbname  = "";
           for (var j in fbObj.formatblocklangs) {
               var fblangObj = fbObj.formatblocklangs[j];
               if (fblangObj.lang == config.systemLang[0]) {fbvalue = fblangObj.name;}
               if (fblangObj.lang == config.browserLang[0]) {fbname = fblangObj.name;}
           }
                      toolbar += '<option value="' +fbvalue+ '">' + fbname + '</option>';
        }
        toolbar += '</select>';
        continue;
      }
// end insert by lvn
      // fontname
      if (btnName == "fontname") {
        toolbar += ' <span class="FormLabel">Tipo di carattere</span> <select id="_' +objname+ '_FontName" onchange="editor_action(this.id)" unselectable="on" style="margin: 1 2 0 2; font-size: 12px;">';
        for (var fontname in config.fontnames) {
          toolbar += '<option value="' +config.fontnames[fontname]+ '">' +fontname+ '</option>'
        }
        toolbar += '</select>';
        continue;
      }

      // fontsize
      if (btnName == "fontsize") {
        toolbar += '  <span class="FormLabel">Dimensione carattere</span> <select id="_' +objname+ '_FontSize" onchange="editor_action(this.id)" unselectable="on" style="margin: 1 2 0 0; font-size: 12px;       scrollbar-face-color : Fuchsia;">';
        for (var fontsize in config.fontsizes) {
          toolbar += '<option value="' +config.fontsizes[fontsize]+ '">' +fontsize+ '</option>'
        }
        toolbar += '</select>\n';
        continue;
      }

      // font style
      if (btnName == "fontstyle") {
        toolbar += '  Font Style <select id="_' +objname+ '_FontStyle" onchange="editor_action(this.id)" unselectable="on" style="margin: 1 2 0 0; font-size: 12px;">';
        + '<option value="">Font Style</option>';
        for (var i in config.fontstyles) {
          var fontstyle = config.fontstyles[i];
          toolbar += '<option value="' +fontstyle.className+ '">' +fontstyle.name+ '</option>'
        }
        toolbar += '</select>';
        continue;
      }
      // separator
      if (btnName == "separator") {
        toolbar += '<span style="border: 1px inset; width: 1px; font-size: 16px; height: 16px; margin: 0 3 0 3"></span>';
        continue;
      }

      // buttons
      var btnObj = config.btnList[btnName];
      if (btnName == 'linebreak') { alert("htmlArea error: 'linebreak' must be in a subgroup by itself, not with other buttons.\n\nhtmlArea wysiwyg editor not created."); return; }
      if (!btnObj) { alert("htmlArea error: button '" +btnName+ "' not found in button list when creating the wysiwyg editor for '"+objname+"'.\nPlease make sure you entered the button name correctly.\n\nhtmlArea wysiwyg editor not created."); return; }
      var btnCmdID   = btnObj[0];
      var btnTitle   = btnObj[1];
      var btnOnClick = btnObj[2];
      var btnImage   = btnObj[3];
      toolbar += '<button title="' +btnTitle+ '" id="_' +objname+ '_' +btnCmdID+ '" class="btn" onClick="' +btnOnClick+ '" onmouseover="if(this.className==\'btn\'){this.className=\'btnOver\'}" onmouseout="if(this.className==\'btnOver\'){this.className=\'btn\'}" unselectable="on"><img src="' +config.imgURL + btnImage+ '" border=0 unselectable="on"></button>';


    } // end of button sub-group
    toolbar += tblClose;
  } // end of entire button set

  // build editor

  var editor = '<span id="_editor_toolbar"><table border=1 cellspacing=0 cellpadding=0 bgcolor="buttonface" style="padding: 1 0 0 2" width=' + config.width + ' unselectable="on"><tr><td>\n'
  + toolbar
  + '</td></tr></table>\n'
  + '</td></tr></table></span>\n'
  + '<textarea ID="_' +objname + '_editor" style="width:' +config.width+ '; height:' +config.height+ '; margin-top: -1px; margin-bottom: -1px;" wrap=soft></textarea>';

  // add context menu
  editor += '<div id="_' +objname + '_cMenu" style="position: absolute; visibility: hidden;"></textarea>';

  //  hide original textarea and insert htmlarea after it
  if (!config.debug) { document.getElementById(objname).style.display = "none"; }

  if (config.plaintextInput) {     // replace nextlines with breaks
    var contents = document.getElementById(objname).value;
    contents = contents.replace(/\r\n/g, '<br>');
    contents = contents.replace(/\n/g, '<br>');
    contents = contents.replace(/\r/g, '<br>');
    document.getElementById(objname).value = contents;
  }
  // insert wysiwyg
  if ( document.getElementById(objname).insertAdjacentHTML)
    document.getElementById(objname).insertAdjacentHTML('afterEnd', editor)
  else
    document.write(editor);
  // convert htmlarea from textarea to wysiwyg editor
  editor_setmode(objname, 'init');
  if (document.all[objname].form) {
  	// we have a form, on submit get the HTMLArea content and
  	// update original textarea.
  	document.all[objname].form.onsubmit = function() {
  		document.all[objname].value = editor_getHTML(objname);
  	};
  }
  // call filterOutput when user submits form
  //for (var idx=0; idx < document.forms.length; idx++) {
  //  var r = document.forms[idx].attachEvent('onsubmit', function() {  editor_filterOutput(objname); });
  //  if (!r) { alert("Error attaching event to form!"); }
  //}
return true;
}
/* ---------------------------------------------------------------------- *\
  Function    : editor_action
  Description : perform an editor command on selected editor content
  Usage       :
  Arguments   : button_id - button id string with editor and action name
\* ---------------------------------------------------------------------- */

function editor_action(button_id) {
  // split up button name into "editorID" and "cmdID"
  var BtnParts = Array();
  BtnParts = button_id.split("_");
  var objname    = button_id.replace(/^_(.*)_[^_]*$/, '$1');
  var cmdID      = BtnParts[ BtnParts.length-1 ];
  var button_obj = document.getElementById(button_id);
  var editor_obj = document.getElementById("_" +objname + "_editor");
  var config     = document.getElementById(objname).config;

  // help popup
  if (cmdID == 'showhelp') {
    window.open(_editor_url + "popups/editor_help.html", 'EditorHelp',
        'toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,resizable=yes');
    return;
  }

  // popup editor
  if (cmdID == 'popupeditor') {
    showModelessDialog(_editor_url + "popups/fullscreen.html?"+objname,window, "resizable: yes; help: no; status: no; scroll: no; ");
    return;
  }

// inserted by lvn
  // show 0 borders
  if (cmdID == 'ShowBorder'){
     var btnObj = document.getElementById("_" +objname+ "_ShowBorder");
     if (config.showborders){ // toggle is on : put borders off
        nullBorders(editor_obj.contentWindow.document,'hide');
        btnObj.className = 'btn';
        config.showborders = false;
     } else {
        nullBorders(editor_obj.contentWindow.document,'show');
        btnObj.className = 'btnDown';
        config.showborders = true;
     }
     return;
  }
// inserted by lvn (find function)
 // Find
// Changed by Eivind Savio to also work in HTML-mode
  if (cmdID == 'Find') {
     setGlobalVar ("_editor_field",objname);
     var findRange = editor_obj.document.body.createTextRange();
     showModelessDialog(_editor_url + "popups/find.html",window, "resizable: no; help: no; status: no; scroll: no; ");
     return;
  }
// end insert by lvn



// check editor mode (don't perform actions in textedit mode)
  if (editor_obj.tagName.toLowerCase() == 'textarea') { return; }

  var editdoc = editor_obj.contentWindow.document;

  // new
  //if (cmdID == 'New'){document.location.reload()
  //document.location = "test.html"

  //}


  editor_focus(editor_obj);
 // get index and value for pulldowns
  var idx = button_obj.selectedIndex;
  var val = (idx != null) ? button_obj[ idx ].value : null;

  if (0) {}   // use else if for easy cutting and pasting

 // CUSTOM BUTTONS START HERE
else if (cmdID == 'smiley') {
    var myTitle = "Insert Smileys";
    var myText = window.showModalDialog(_editor_url + "smiley_select.asp",
                                 myTitle, "resizable: no; help: no; status: no; scroll: no; ");
    if (myText) { editor_insertHTML(objname, myText); }
   }
  // Custom1
  //else if (cmdID == 'custom1') {
    //alert("Hello, I am custom button 1!");
  //}

// cleancode
  else if (cmdID == 'cleancode') {
  var oTags = editdoc.all.tags("SPAN");
  if (oTags != null) {
  for (var i = oTags.length - 1; i >= 0; i--) {
  oTags[i].outerHTML = oTags[i].innerHTML;
  }
}

oTags = editdoc.all.tags("FONT");
if (oTags != null) {
for (var i = oTags.length - 1; i >= 0; i--) {
oTags[i].outerHTML = oTags[i].innerHTML;
}
}

  oTags = editdoc.all.tags("P");
  if (oTags != null) {
  for (var i = oTags.length - 1; i >= 0; i--) {
  cleanEmptyTag(oTags[i]);
  }
}
  oTags = editdoc.all.tags("H1");
  if (oTags != null) {
  for (var i = oTags.length - 1; i >= 0; i--) {
  cleanEmptyTag(oTags[i]);
}
}
  oTags = editdoc.all.tags("H2");
  if (oTags != null) {
  for (var i = oTags.length - 1; i >= 0; i--) {
  cleanEmptyTag(oTags[i]);
}
}
  oTags = editdoc.all.tags("H3");
  if (oTags != null) {
  for (var i = oTags.length - 1; i >= 0; i--) {
  cleanEmptyTag(oTags[i]);
}
}
  oTags = editdoc.all.tags("H4");
  if (oTags != null) {
  for (var i = oTags.length - 1; i >= 0; i--) {
  cleanEmptyTag(oTags[i]);
}
}
  oTags = editdoc.all.tags("OL");
  if (oTags != null) {
  for (var i = oTags.length - 1; i >= 0; i--) {
  cleanEmptyTag(oTags[i]);
}
}
  oTags = editdoc.all.tags("UL");
  if (oTags != null) {
  for (var i = oTags.length - 1; i >= 0; i--) {
  cleanEmptyTag(oTags[i]);              }
}
}

  // Custom3
  //else if (cmdID == 'custom3') {  // insert some text
 //   editor_insertHTML(objname, "It's easy to add buttons that insert text!");
 // }

  //
  // END OF CUSTOM BUTTONS
  //

// inserted by lvn : table operations
  else if ( cmdID == 'TableProperties' ||
       cmdID == 'RowProperties' ||
       cmdID == 'InsertRowBefore' ||
       cmdID == 'InsertRowAfter' ||
       cmdID == 'DeleteRow' ||
       cmdID == 'InsertColumnBefore' ||
       cmdID == 'InsertColumnAfter' ||
       cmdID == 'DeleteColumn' ||
       cmdID == 'CellProperties' ||
       cmdID == 'InsertCellBefore' ||
       cmdID == 'InsertCellAfter' ||
       cmdID == 'SplitCell' ||
       cmdID == 'MergeRows' ||
       cmdID == 'SplitRow' ||
       cmdID == 'MergeCells' ||
       cmdID == 'DeleteCell' ) {
     // table operations
     var table_src_element = editdoc.selection.createRange().parentElement();
     while (table_src_element != null && table_src_element.tagName != 'TD' && table_src_element.tagName != 'TH'){
        table_src_element = table_src_element.parentElement;
     }
     if (table_src_element == null) {
        alert('Table operations not allowed here');
     } else {
        tables_action(button_id,table_src_element);
     }
  }
// end insert by lvn

  // FontName
  else if (cmdID == 'FontName' && val) {
    editdoc.execCommand(cmdID,0,val);
  }




// inserted by lvn
  // Formatblock
  else if (cmdID == 'FormatBlock' && val) {
    editdoc.execCommand(cmdID,0,val);
  }

  // special characters
  else if (cmdID == 'SpecChar') {
    var newchar = showModalDialog(_editor_url + "popups/insert_char.html", '', "dialogWidth:493px; dialogHeight: 140px; resizable: no; help: no; status: no; scroll: no;");
    if (newchar == '') {
       return;
    } else {
       editor_insertHTML(objname,newchar);
    }
  }


// end insert by lvn
  // FontSize
  else if (cmdID == 'FontSize' && val) {
    editdoc.execCommand(cmdID,0,val);
  }

  // FontStyle (change CSS className)
  else if (cmdID == 'FontStyle' && val) {
    editdoc.execCommand('RemoveFormat');
    editdoc.execCommand('FontName',0,'636c6173734e616d6520706c616365686f6c646572');
    var fontArray = editdoc.all.tags("FONT");
    for (i=0; i<fontArray.length; i++) {
      if (fontArray[i].face == '636c6173734e616d6520706c616365686f6c646572') {
        fontArray[i].face = "";
        fontArray[i].className = val;
        fontArray[i].outerHTML = fontArray[i].outerHTML.replace(/face=['"]+/, "");
        }
    }
    button_obj.selectedIndex =0;
  }

  // fgColor and bgColor
  else if (cmdID == 'ForeColor' || cmdID == 'BackColor') {
    var oldcolor = _dec_to_rgb(editdoc.queryCommandValue(cmdID));
    var newcolor = showModalDialog(_editor_url + "popups/select_color.html", oldcolor, "resizable: no; help: no; status: no; scroll: no;");
    if (newcolor != null) { editdoc.execCommand(cmdID, false, "#"+newcolor); }
  }

  // execute command for buttons - if we didn't catch the cmdID by here we'll assume it's a
  else {
    // subscript & superscript, disable one before enabling the other
    if (cmdID.toLowerCase() == 'subscript' && editdoc.queryCommandState('superscript')) { editdoc.execCommand('superscript'); }
    if (cmdID.toLowerCase() == 'superscript' && editdoc.queryCommandState('subscript')) { editdoc.execCommand('subscript'); }


// insert link (modified)
    if (cmdID.toLowerCase() == 'insertlink') {
        var theRange = editdoc.selection.createRange();

        var highlightedText = "";
        var linkText = '';

        var href_attribute = '';
        var tar_attribute = '';

        var elmSelectedImage;
        var htmlSelectionControl = "Control";
        if (editdoc.selection.type == htmlSelectionControl) {
                // actully we have an image.
                elmSelectedImage = theRange.item(0);
                highlightedText = elmSelectedImage.outerHTML;

                //convert the ControlRange to a TextRange
                theRange = editdoc.body.createTextRange();
                theRange.moveToElementText(elmSelectedImage);
                theRange.select();

                fullElement = theRange.htmlText;
        } else {
                if (theRange.text != '') {
                        highlightedText = theRange.htmlText;
                }
                fullElement = theRange.parentElement().outerHTML;
        }

        //in case we happen to select the link itself!
        if (highlightedText.search(/^\<[A|a]/) != -1) {
                fullElement = highlightedText;
        }

        //extrect attributes from HTML
        if (fullElement.search(/^\<[A|a]/) != -1) {

                fullElement = fullElement.replace(/\"/g, "");
                fullElement = fullElement.replace(/\'/g, "");

                // here, we have an <a> tag. Now let's extract...
                // 1. the href attribute
                var href_value = fullElement.split(/href=/);
                href_value2 = href_value[1].split(/\s|>/);
                href_attribute = href_value2[0];

                // 2. the target attribute
                if (fullElement.search(/target=/) != -1) {
                        var tar = fullElement.split(/target=/);
                        tar2 = tar[1].split(/\s|>/);
                        tar_attribute = tar2[0];
                }

                // 3. the link text (more robust as includes all html code aswell)
                pos1 = fullElement.indexOf(">");
                pos2 = fullElement.lastIndexOf("<");
                linkText = fullElement.substring(pos1+1,pos2);
        }

        var myValues = new Object();
        myValues.highlightedText = highlightedText;
        myValues.tar_attribute = tar_attribute;
        myValues.href_attribute = href_attribute;
        myValues.linkText = linkText;

        var myText = showModalDialog(_editor_url + "popups/insert_file.html", myValues, "DialogHeight=15em;DialogWidth=55em;status=no; scroll=no");

        if (linkText != '') {
                if (myText) {
                        theRange.parentElement().outerHTML = '';
                        editor_insertHTML(objname, unescape( myText) );
                }
        } else {
                if (myText) {
                        //if (editdoc.selection.type == htmlSelectionControl) {
                        //      theRange.execCommand('Delete');
                        //}
                        editor_insertHTML(objname, unescape(myText) ); // this function ALWAYS puts in an absolute link
                }
        }

    }

    // insert image
    else if (cmdID.toLowerCase() == 'insertimage'){
      showModalDialog(_editor_url + "popups/insert_image.asp", editdoc, "dialogWidth:450px; dialogHeight: 510px; resizable: no; help: no; status: no; scroll: no; ");
    }

        //upload file
    else if (cmdID.toLowerCase() == 'upload'){
      showModalDialog(_editor_url + "popups/upload.asp", editdoc, "resizable: no; help: no; status: no; scroll: no; ");
    }
// insert form
  else if (cmdID.toLowerCase() == 'inputform') {
        var myText = showModalDialog(_editor_url + "popups/mpc.html","","resizable: no; help: no; status: no; scroll: no; ");
        if (myText) {
                editor_insertHTML(objname, unescape( myText) );
        }
  }

// insert formelement
  else if (cmdID.toLowerCase() == 'inputformelement') {
                if (editdoc.selection.createRange().text != "") {
                        var highlightedText = editdoc.selection.createRange().text;
                } else {
                        var highlightedText = "";
                }
                var myText = showModalDialog(_editor_url + "popups/insert_formelement.html",
                                                                        highlightedText,
                                                                        "resizable: no; help: no; status: no; scroll: no; ");
                if (myText) {
                        editor_insertHTML(objname, unescape( myText) );
                }
  }
    // insert table
    else if (cmdID.toLowerCase() == 'inserttable'){
      setGlobalVar('_editor_field',objname);
      showModalDialog(_editor_url + "popups/insert_table.html?"+objname,
                                 window,
                                 "resizable: yes; help: no; status: no; scroll: no; ");
      // inserted by lvn
      if (config.showborders) { nullBorders(editdoc,'show')};
      // end insert by lvn
    }

// insert line
  else if (cmdID == 'line') {  // insert horizontal rule
    setGlobalVar('_editor_field',objname);
    var myText = showModalDialog(_editor_url + "popups/insert_line.html",
                                 window,
                                 "resizable: yes; help: no; status: no; scroll: no; ");
    if (myText) { editor_insertHTML(objname, unescape( myText) ); }
  }

// Change Case
        else if (cmdID == 'changecase') {
        if (editdoc.selection.createRange().htmlText != "") {
        var highlightedText = editdoc.selection.createRange().htmlText;
        editdoc.execCommand('copy');
        editdoc.execCommand('FormatBlock','','Normal')
        editdoc.execCommand('RemoveFormat');
        editdoc.execCommand('unlink');
        var myText = showModalDialog(_editor_url + "popups/changecase.html",
                highlightedText, "resizable: yes; help: no; status: no; scroll: no; ");
                if (myText) { editor_insertHTML(objname, unescape( myText) );}
                else {editdoc.execCommand('paste');}
                window.clipboardData.clearData();
                }
        else {alert('\nYou need to select some text first.');}
 }

// insert marquee
  else if (cmdID == 'marquee') {  // insert some text from a popup window
   var myTitle = "";
    var myText = showModalDialog(_editor_url + "popups/insert_marquee.html",
                                 myTitle,      // str or obj specified here can be read from dialog as "window.dialogArguments"
                                 "resizable: yes; help: no; status: no; scroll: no; ");
    if (myText) { editor_insertHTML(objname, unescape( myText) ); }
  }

// Insert anchor
  else if (cmdID == 'anchor') {  // insert some text from a popup window
   var myTitle = "";
    var myText = showModalDialog(_editor_url + "popups/insert_anchor.html",
                                 myTitle,      // str or obj specified here can be read from dialog as "window.dialogArguments"
                                 "resizable: yes; help: no; status: no; scroll: no; ");
    if (myText) { editor_insertHTML(objname, unescape( myText) ); }
  }

    // all other commands microsoft Command Identifiers
    else { editdoc.execCommand(cmdID); }
  }

  editor_event(objname);
// inserted by lvn
  editor_obj.focus();
// end insert by lvn
}
// inserted by lvn : table operations
/* ---------------------------------------------------------------------- *\
  Function    : tables_action
  Description : perform an action on selected table
  Usage       :
  Arguments   : table_action - objectname + action to execute
                td - startpoint cell
\* ---------------------------------------------------------------------- */

function tables_action(table_action,td) {

  // operations only valid on table cells
  if (td.tagName == 'TD' || td.tagName == 'TH' ) {
     var TableParts = table_action.split("_");
     var objname    = table_action.replace(/^_(.*)_[^_]*$/, '$1');
     var cmdID      = TableParts[ TableParts.length-1 ];
     var editor_obj = document.getElementById("_" +objname + "_editor");
     var config     = document.getElementById(objname).config;
     var tr,td,tbody,table,newtr;
     // get the table object model
     tr = td.parentNode;
     while(tr != null && tr.tagName != 'TR'){tr = tr.parentNode;}
     if (tr != null) {
        var tbody = tr.parentNode;
        while(tbody != null && tbody.tagName != 'TBODY' && tbody.tagName != 'THEAD' && tbody.tagName != 'TFOOT'){tbody = tbody.parentNode;}
        if (tbody != null) {
           table = tbody.parentNode;
           while(table!= null && table.tagName != 'TABLE'){table = table.parentNode;}
        }
     }
     // only execute commands if table object model is complete
     if (table != null) {
        // local functions to insert rowdetails and columns
        function insertRowDetails(tr,newtr) {
           //for (var i=0;i < tr.childNodes.length;i++) {
           for (var i=0;i < tr.cells.length;i++) {
              newtr.insertCell(-1);
           }
        }
        function insertColumn(tbody,where) {
           //for (var i=0;i < tbody.childNodes.length;i++) {
           for (var i=0;i < tbody.rows.length;i++) {
              //tr = tbody.childNodes(i);
              tr = tbody.rows(i);
              //if (where > tr.childNodes.length){
              if (where > tr.cells.length){
                 tr.insertCell();
              } else {
                 tr.insertCell(where);
              }
           }
        }
        function deleteColumn(tbody,where) {
           //for (var i=0;i <  tbody.childNodes.length;i++) {
           for (var i=0;i <  tbody.rows.length;i++) {
             //var tr = tbody.childNodes(i);
             var tr = tbody.rows(i);
             //if (tr.childNodes.length - 1 < where){
             if (tr.cells.length - 1 < where){
                //tr.deleteCell(tr.childNodes.length - 1);
                tr.deleteCell(tr.cells.length - 1);
             } else {
                tr.deleteCell(where);
             }
             //tr = tbody.childNodes(i);
             tr = tbody.rows(i);
             //if (tr.childNodes.length == 0){
             if (tr.cells.length == 0){
                tbody.deleteRow(i);
             }
           }
        }
        function splitCell(tbody,currTr,currTd){
           if (currTd.colSpan > 1) {
              // rowspan > 1 just insert cell and decrease colspan
              currTd.colSpan = currTd.colSpan - 1;
              currTr.insertCell(currTd.cellIndex + 1);
           } else {
              // rowspan = 1 increase colspan for all other rows and insert cell in current row
              for (var i=0;i <  tbody.rows.length;i++) {
                 var tr = tbody.rows(i);
                 var td = tr.cells(currTd.cellIndex);
                 if (i == currTr.rowIndex) {
                    tr.insertCell(currTd.cellIndex + 1);
                 } else {
                    td.colSpan = td.colSpan + 1;
                 }
              }
           }
        }
        // commented out!
        /* function mergeCells(tbody,currTr,currTd){
           // check if leftmost of cells to merge
           var left = false;
           for (var i=0;i <  tbody.rows.length;i++) {
              var tr = tbody.rows(i);
              var allTd = tr.cells;
              if (currTd.cellIndex + 2 > allTd.length) {
                 left = false;
                 break;
              } else {
                 var td = tr.cells(currTd.cellIndex);
                 if (i != currTr.rowIndex) {
                    if (td.colSpan > 1) {
                       left = true;
                    } else {
                       left = false;
                       break;
                    }
                 }
              }
           }
           if (left){
              for (var i=0;i < tbody.rows.length;i++){
                 var tr = tbody.rows(i);
                 var td = tr.cells(currTd.cellIndex);
                 if (currTd.cellIndex + 2 > tr.length) {
                    alert("You can't merge cells here.");
                    return;
                 } else {
                    var mergeCell = tr.cells(currTd.cellIndex + 1);
                    if (i == currTr.rowIndex) {
                       // merge the contents of the current cell with the one on the right
                       currTd.innerHTML = currTd.innerHTML + mergeCell.innerHTML;
                       currTr.deleteCell(currTd.cellIndex + 1);
                    } else {
                       // decrease colspan for non current rows
                       td.colSpan = td.colSpan - 1;
                    }
                 }
              }
           } else {
              alert('Select the leftmost cell of the split to merge.');
           }
        } */
        function mergeCells(tbody,currTr,currTd){
        //first check if there are cells to the right
           if (currTd.cellIndex < currTr.cells.length-1) {
              //get current colspan and cell to be merged's colspan
              //add the two together to get the new one,
              //move the conetent and delete the right one
              var currColSpan = currTd.colSpan ;
              var mergeCellColSpan = currTr.cells(currTd.cellIndex+1).colSpan;
              var mergeCell = currTr.cells(currTd.cellIndex+1);
              currTd.innerHTML = currTd.innerHTML + mergeCell.innerHTML;
              currTr.deleteCell(currTd.cellIndex + 1);
              currTd.colSpan = currColSpan+mergeCellColSpan ;
           } else {
              alert('Select the leftmost cell of the split to merge.');
           }
        }
        function splitRow(tbody,currTr,currTd){
           // check rowspan on other cells
           if (currTd.rowSpan > 1){
             currTd.rowSpan = currTd.rowSpan - 1;
             var tr = tbody.rows(currTr.rowIndex + 1);
             var where = 0;
             for (var i=0;i <  currTr.cells.length;i++) {
                if (i < currTd.cellIndex){
                   if (currTr.cells(i).rowSpan < 2){where++;}
                }
             }
             tr.insertCell(where);
           } else {
              for (var i=0;i <  currTr.cells.length;i++) {
                 var td = currTr.cells(i);
                 if (i == currTd.cellIndex) {
                    tr = tbody.insertRow(currTr.rowIndex + 1);
                    tr.insertCell(0);
                 } else {
                    td.rowSpan = td.rowSpan + 1;
                 }
              }
           }
        }
        function mergeRows(tbody,currTr,currTd){
           // check if topmost of cells to merge
           var top = false;
           if (currTd.rowSpan < 2){
              for (var i=0;i <  currTr.cells.length;i++) {
                 if (i !== currTd.cellIndex) {
                    if (currTr.cells(i).rowSpan > 1){
                       top = true;
                       break;
                    }
                 }
              }
           }
           if (top){
              return;
           } else {
              alert('Select the topmost row of the split to merge.');
           }
        }
        // execute the operation depending on the given command
        switch(cmdID) {
           case 'CreateCaption'      : table.createCaption();break;
           case 'DeleteCaption'      : table.deleteCaption();break;
           case 'CreateTHead'        : table.createTHead();break;
           case 'DeleteTHead'        : table.deleteTHead();break;
           case 'CreateTFoot'        : table.createTFoot();break;
           case 'DeleteTFoot'        : table.deleteTFoot();break;
           case 'InsertRowTop'       : newtr = tbody.insertRow(0);insertRowDetails(tr,newtr);break;
           case 'InsertRowBottom'    : newtr = tbody.insertRow(-1);insertRowDetails(tr,newtr);break;
           case 'InsertRowBefore'    : newtr = tbody.insertRow(tr.rowIndex);insertRowDetails(tr,newtr);break;
           case 'InsertRowAfter'     : newtr = tbody.insertRow(tr.rowIndex+1);insertRowDetails(tr,newtr);break;
           case 'InsertRowStart'     : newtr = tbody.insertRow(0);insertRowDetails(tr,newtr);break;
           case 'DeleteRow'          : tbody.deleteRow(tr.rowIndex);break;
           case 'InsertColumnLeft'   : insertColumn(tbody,0);break;
           case 'InsertColumnRight'  : insertColumn(tbody,-1);break;
           case 'InsertColumnBefore' : insertColumn(tbody,td.cellIndex);break;
           case 'InsertColumnAfter'  : insertColumn(tbody,td.cellIndex+1);break;
           case 'DeleteColumn'       : deleteColumn(tbody,td.cellIndex);break;
           case 'InsertCellLeft'     : tr.insertCell(0);break;
           case 'InsertCellRight'    : tr.insertCell(-1);break;
           case 'InsertCellBefore'   : tr.insertCell(td.cellIndex);break;
           case 'InsertCellAfter'    : tr.insertCell(td.cellIndex+1);break;
           case 'InsertCellStart'    : tr.insertCell(0);break;
           case 'DeleteCell'         : tr.deleteCell(td.cellIndex);break;
           case 'SplitCell'          : splitCell(tbody,tr,td);break;
           case 'MergeCells'         : mergeCells(tbody,tr,td);break;
           case 'SplitRow'           : splitRow(tbody,tr,td);break;
           case 'MergeRows'          : mergeRows(tbody,tr,td);break;
// inserted by lvn : property pallettes
           case 'TableProperties'    : nullBorders(editor_obj.contentWindow.document,'hide');
                                       setGlobalVar('_editor_field',objname);
                                       setGlobalVar('_editor_table',table);
                                       showModalDialog(_editor_url + "popups/tableprop.html?"+objname,
                                                       window,
                                                       "resizable: yes; help: no; status: no; scroll: no; ");
                                       td.focus();
                                       break;
           case 'RowProperties'      : setGlobalVar('_editor_field',objname);
                                       setGlobalVar('_editor_row',tr);
                                       showModalDialog(_editor_url + "popups/rowprop.html?"+objname,
                                                       window,
                                                       "resizable: yes; help: no; status: no; scroll: no; ");
                                       td.focus();
                                       break;
           case 'CellProperties'     : setGlobalVar('_editor_field',objname);
                                       setGlobalVar('_editor_cell',td);
                                       showModalDialog(_editor_url + "popups/cellprop.html?"+objname,
                                                       window,
                                                       "resizable: yes; help: no; status: no; scroll: no; ");
                                       td.focus();
                                       break;
// end insert lvn property pallettes
           default                   : break;
        }
        // if 0 table borders and the switch to show them is on: show them
        if (config.showborders){ // toggle is on : show null borders
           nullBorders(editor_obj.contentWindow.document,'show');
        }
     }
  }
  return;
}
// end insert by lvn

/* ---------------------------------------------------------------------- *\
  Function    : editor_event
  Description : called everytime an editor event occurs
  Usage       : editor_event(objname, runDelay, eventName)
  Arguments   : objname - ID of textarea to replace
                runDelay: -1 = run now, no matter what
                          0  = run now, if allowed
                        1000 = run in 1 sec, if allowed at that point
\* ---------------------------------------------------------------------- */

function editor_event(objname,runDelay) {
  var config = document.getElementById(objname).config;
  var editor_obj  = document.getElementById("_" +objname+  "_editor");       // html editor object
  if (runDelay == null) { runDelay = 0; }
  var editdoc;
  var editEvent = editor_obj.contentWindow ? editor_obj.contentWindow.event : event;

  // catch keypress events
    if (editEvent && editEvent.keyCode) {
      var ord       = editEvent.keyCode;    // ascii order of key pressed
      var ctrlKey   = editEvent.ctrlKey;
      var altKey    = editEvent.altKey;
      var shiftKey  = editEvent.shiftKey;

      if (ord == 16) { return; }  // ignore shift key by itself
      if (ord == 17) { return; }  // ignore ctrl key by itself
      if (ord == 18) { return; }  // ignore alt key by itself


       // cancel ENTER key and insert <BR> instead
//       if (ord == 13 && editEvent.type == 'keypress') {
//         editEvent.returnValue = false;
//         editor_insertHTML(objname, "<br>");
//         return;
//       }

      if (ctrlKey && (ord == 122 || ord == 90)) {     // catch ctrl-z (UNDO)
//      TODO: Add our own undo/redo functionality
//        editEvent.cancelBubble = true;
        return;
      }
      if ((ctrlKey && (ord == 121 || ord == 89)) ||
          ctrlKey && shiftKey && (ord == 122 || ord == 90)) {     // catch ctrl-y, ctrl-shift-z (REDO)
//      TODO: Add our own undo/redo functionality
        return;
      }
    }

  // setup timer for delayed updates (some events take time to complete)
  if (runDelay > 0) { return setTimeout(function(){ editor_event(objname); }, runDelay); }

  // don't execute more than 3 times a second (eg: too soon after last execution)
  if (this.tooSoon == 1 && runDelay >= 0) { this.queue = 1; return; } // queue all but urgent events
  this.tooSoon = 1;
  setTimeout(function(){
    this.tooSoon = 0;
    if (this.queue) { editor_event(objname,-1); };
    this.queue = 0;
    }, 333);  // 1/3 second


//  editor_updateOutput(objname);
  editor_updateToolbar(objname);

}

/* ---------------------------------------------------------------------- *\
  Function    : editor_updateToolbar
  Description : update toolbar state
  Usage       :
  Arguments   : objname - ID of textarea to replace
                action  - enable, disable, or update (default action)
\* ---------------------------------------------------------------------- */

function editor_updateToolbar(objname,action) {
  var config = document.getElementById(objname).config;
  var editor_obj  = document.getElementById("_" +objname+  "_editor");

  // disable or enable toolbar

  if (action == "enable" || action == "disable") {
//    var tbItems = new Array('FontName','FontSize','FontStyle');                           // add pulldowns
// updated by lvn
    var tbItems = new Array('FontName','FontSize','FontStyle','FormatBlock');                           // add pulldowns
// end update by lvn
// inserted by lvn (find function)
    if (action == "disable") {
       setGlobalVar("_editor_field","_editor_disabled");   // set _editor_disabled to close the open modeless dialogs
    } else {
       setGlobalVar("_editor_field",objname);
    }
// end update by lvn

  for (var btnName in config.btnList) { tbItems.push(config.btnList[btnName][0]); } // add buttons

    for (var idxN in tbItems) {
      var cmdID = tbItems[idxN].toLowerCase();
      var tbObj = document.getElementById("_" +objname+ "_" +tbItems[idxN]);
      if (cmdID == "htmlmode" ||  cmdID == "showhelp" || cmdID == "about" || cmdID == "popupeditor" || cmdID == "find") { continue; } // don't change these buttons
      if (tbObj == null) { continue; }
      var isBtn = (tbObj.tagName.toLowerCase() == "button") ? true : false;

      if (action == "enable")  { tbObj.disabled = false; if (isBtn) { tbObj.className = 'btn' }}
      if (action == "disable") { tbObj.disabled = true;  if (isBtn) { tbObj.className = 'btnNA' }}
    }
    return;
  }

  // update toolbar state

  if (editor_obj.tagName.toLowerCase() == 'textarea') { return; }   // don't update state in textedit mode
  var editdoc = editor_obj.contentWindow.document;

  // Set FontName pulldown
  var fontname_obj = document.getElementById("_" +objname+ "_FontName");
  if (fontname_obj) {
    var fontname = editdoc.queryCommandValue('FontName');
    if (fontname == null) { fontname_obj.value = null; }
    else {
      var found = 0;
      for (i=0; i<fontname_obj.length; i++) {
        if (fontname.toLowerCase() == fontname_obj[i].text.toLowerCase()) {
          fontname_obj.selectedIndex = i;
          found = 1;
        }
      }
      if (found != 1) { fontname_obj.value = null; }     // for fonts not in list
    }
  }
// inserted by lvn
  // inserted by lvn
  // Set Formatblock pulldown
  var formatblock_obj = document.getElementById("_" +objname+ "_FormatBlock");
  if (formatblock_obj) {
    var formatblock = editdoc.queryCommandValue('FormatBlock');
    if (formatblock == null) { formatblock_obj.value = null; }
    else {

      var found = 0;
      for (i=0; i<formatblock_obj.length; i++) {
        if (formatblock == formatblock_obj[i].value) {
          formatblock_obj.selectedIndex = i;
          found = 1;
        }
      }
      if (found != 1) { formatblock_obj.value = null; }     // for formatblocks not in list
    }
  }
// end insert by lvn

  // Set FontSize pulldown
  var fontsize_obj = document.getElementById("_" +objname+ "_FontSize");
  if (fontsize_obj) {
    var fontsize = editdoc.queryCommandValue('FontSize');
    if (fontsize == null) { fontsize_obj.value = null; }
    else {
      var found = 0;
      for (i=0; i<fontsize_obj.length; i++) {
        if (fontsize == fontsize_obj[i].value) { fontsize_obj.selectedIndex = i; found=1; }
      }
      if (found != 1) { fontsize_obj.value = null; }     // for sizes not in list
    }
  }

  // Set FontStyle pulldown
  var classname_obj = document.getElementById("_" +objname+ "_FontStyle");
  if (classname_obj) {
    var curRange = editdoc.selection.createRange();

    // check element and element parents for class names
    var pElement;
    if (curRange.length) { pElement = curRange[0]; }              // control tange
    else                 { pElement = curRange.parentElement(); } // text range
    while (pElement && !pElement.className) { pElement = pElement.parentElement; }  // keep going up

    var thisClass = pElement ? pElement.className.toLowerCase() : "";
    if (!thisClass && classname_obj.value) { classname_obj.value = null; }
    else {
      var found = 0;
      for (i=0; i<classname_obj.length; i++) {
        if (thisClass == classname_obj[i].value.toLowerCase()) {
          classname_obj.selectedIndex = i;
          found=1;
        }
      }
      if (found != 1) { classname_obj.value = null; }     // for classes not in list
    }
  }

  // update button states
  var IDList = Array('Bold','Italic','Underline','StrikeThrough','SubScript','SuperScript','JustifyLeft','JustifyCenter','JustifyRight','justifyfull','justifynone','InsertOrderedList','InsertUnorderedList','BlockDirLTR','BlockDirRTL');
  for (i=0; i<IDList.length; i++) {
    var btnObj = document.getElementById("_" +objname+ "_" +IDList[i]);
    if (btnObj == null) { continue; }
    var cmdActive = editdoc.queryCommandState( IDList[i] );

    if (!cmdActive)  {                                  // option is OK
      if (btnObj.className != 'btn') { btnObj.className = 'btn'; }
      if (btnObj.disabled  != false) { btnObj.disabled = false; }
    } else if (cmdActive)  {                            // option already applied or mixed content
      if (btnObj.className != 'btnDown') { btnObj.className = 'btnDown'; }
      if (btnObj.disabled  != false)   { btnObj.disabled = false; }
    }
  }
// inserted by lvn: table operations
// disable table handling buttons when not in a table cell
  var table_src_element = null;
  // only works on non-control ranges
  if (editdoc.selection.type != 'Control'){
     table_src_element = editdoc.selection.createRange().parentElement();
     while (table_src_element != null && table_src_element.tagName != 'TD' && table_src_element.tagName != 'TH'){
        table_src_element = table_src_element.parentElement;
     }
  }
  // check if buttons are set in the config
  var IDList = Array('TableProperties','RowProperties','InsertRowBefore','InsertRowAfter','DeleteRow','InsertColumnBefore','InsertColumnAfter','DeleteColumn','CellProperties','InsertCellBefore','InsertCellAfter','DeleteCell','SplitCell','MergeCells','SplitRow','MergeRows');
   for (var i=0; i<IDList.length; i++) {
    var found = false;
    for (var j=0;j<config.toolbar.length;j++){
       if(config.toolbar[j]) {
          for (var k=0;k<config.toolbar[j].length;k++){
            if ( IDList[i] ==  config.toolbar[j][k]){found = true;}
          }
       }
    }
    // if in cell enable buttons, else disable them
    if (found) {
       var btnObj = document.getElementById("_" +objname+ "_" +IDList[i]);
       if (table_src_element == null) {
          btnObj.disabled = true;
          btnObj.className = 'btnNA';
       } else {
          btnObj.disabled = false;
          btnObj.className = 'btn';
       }
    }
  }
// end insert by lvn

}

/* ---------------------------------------------------------------------- *\
  Function    : editor_updateOutput
  Description : update hidden output field with data from wysiwg
\* ---------------------------------------------------------------------- */

function editor_updateOutput(objname) {
  var config     = document.getElementById(objname).config;
  var editor_obj  = document.getElementById("_" +objname+  "_editor");       // html editor object
  var editEvent = editor_obj.contentWindow ? editor_obj.contentWindow.event : event;
  var isTextarea = (editor_obj.tagName.toLowerCase() == 'textarea');
  var editdoc = isTextarea ? null : editor_obj.contentWindow.document;

  // get contents of edit field
  var contents;
  if (isTextarea) { contents = editor_obj.value; }
  else            { contents = editdoc.body.innerHTML; }

  // check if contents has changed since the last time we ran this routine
  if (config.lastUpdateOutput && config.lastUpdateOutput == contents) { return; }
  else { config.lastUpdateOutput = contents; }

  // update hidden output field
  document.getElementById(objname).value = contents;

}

/* ---------------------------------------------------------------------- *\
  Function    : editor_filterOutput
  Description :
\* ---------------------------------------------------------------------- */

function editor_filterOutput(objname) {
  editor_updateOutput(objname);
  var contents = document.getElementById(objname).value;
  var config   = document.getElementById(objname).config;

  // ignore blank contents
  if (contents.toLowerCase() == '<p>&nbsp;</p>') { contents = ""; }

  // filter tag - this code is run for each HTML tag matched
  var filterTag = function(tagBody,tagName,tagAttr) {
    tagName = tagName.toLowerCase();
    var closingTag = (tagBody.match(/^<\//)) ? true : false;

    // fix placeholder URLS - remove absolute paths that IE adds
    if (tagName == 'img') { tagBody = tagBody.replace(/(src\s*=\s*.)[^*]*(\*\*\*)/, "$1$2"); }
    if (tagName == 'a')   { tagBody = tagBody.replace(/(href\s*=\s*.)[^*]*(\*\*\*)/, "$1$2"); }

    // add additional tag filtering here

    // convert to vbCode
//    if      (tagName == 'b' || tagName == 'strong') {
//      if (closingTag) { tagBody = "[/b]"; } else { tagBody = "[b]"; }
//    }
//    else if (tagName == 'i' || tagName == 'em') {
//      if (closingTag) { tagBody = "[/i]"; } else { tagBody = "[i]"; }
//    }
//    else if (tagName == 'u') {
//      if (closingTag) { tagBody = "[/u]"; } else { tagBody = "[u]"; }
//    }
//    else {
//      tagBody = ""; // disallow all other tags!
//    }

    return tagBody;
  };

  // match tags and call filterTag
  RegExp.lastIndex = 0;
    var matchTag = /<\/?(\w+)((?:[^'">]*|'[^']*'|"[^"]*")*)>/g;   // this will match tags, but still doesn't handle container tags (textarea, comments, etc)

  contents = contents.replace(matchTag, filterTag);

  // remove nextlines from output (if requested)
  if (config.replaceNextlines) {
    contents = contents.replace(/\r\n/g, ' ');
    contents = contents.replace(/\n/g, ' ');
    contents = contents.replace(/\r/g, ' ');
  }

  // update output with filtered content
  document.getElementById(objname).value = contents;

}

// inserted by lvn
/* ---------------------------------------------------------------------- *\
  Function    : nullBorders
  Description : show 'dotted' borders for tables with border=0
  Usage       : nullBorders(doc,status);
  Arguments   : doc - document object in wich the borders must be shown
                status - show or hide
\* ---------------------------------------------------------------------- */

function nullBorders(doc,status) {
  // show table borders
  var edit_Tables = doc.body.getElementsByTagName("TABLE");
  for (i=0; i < edit_Tables.length; i++) {
      if (edit_Tables[i].border == '' || edit_Tables[i].border == '0' ) {
         if (status == 'show' ) {
            edit_Tables[i].style.border = "1px dotted #C0C0C0";
         } else {
            edit_Tables[i].removeAttribute("style");
         }
      }
      edit_Rows = edit_Tables[i].rows;
      for (j=0; j < edit_Rows.length; j++) {
          edit_Cells = edit_Rows[j].cells;
          for (k=0; k < edit_Cells.length; k++) {
             if (edit_Tables[i].border == '' || edit_Tables[i].border == '0' ) {
                if (!edit_Cells[k].border || edit_Cells[k].border == '' || edit_Cells[k].border == '0' ) {
                   if (status == 'show' ) {
                      edit_Cells[k].style.border = "1px dotted #C0C0C0";
                   } else {
                      edit_Cells[k].removeAttribute("style");
                   }
                }
             } else {
                if ( edit_Cells[k].border == '0' ) {
                   if (status == 'show' ) {
                      edit_Cells[k].style.border = "1px dotted #C0C0C0";
                   } else {
                      edit_Cells[k].removeAttribute("style");
                   }
                }
             }
          }
      }
   }
}

// end insert by lvn

/* ---------------------------------------------------------------------- *\
  Function    : editor_setmode
  Description : change mode between WYSIWYG and HTML editor
  Usage       : editor_setmode(objname, mode);
  Arguments   : objname - button id string with editor and action name
                mode      - init, textedit, or wysiwyg
\* ---------------------------------------------------------------------- */

function editor_setmode(objname, mode) {
  var config     = document.getElementById(objname).config;
  var editor_obj = document.getElementById("_" +objname + "_editor");

  // wait until document is fully loaded
  if (document.readyState != 'complete') {
    setTimeout(function() { editor_setmode(objname,mode) }, 25);
    return;
  }

  // define different editors
  var TextEdit   = '<textarea ID="_' +objname + '_editor" style="width:' +editor_obj.style.width+ '; height:' +editor_obj.style.height+ '; margin-top: -1px; margin-bottom: -1px;"></textarea>';
  var RichEdit   = '<iframe ID="_' +objname+ '_editor"    style="width:' +editor_obj.style.width+ '; height:' +editor_obj.style.height+ ';"></iframe>';

 // src="' +_editor_url+ 'popups/blank.html"

  //
  // Switch to TEXTEDIT mode
  //

  if (mode == "textedit" || editor_obj.tagName.toLowerCase() == 'iframe') {
    config.mode = "textedit";
    var editdoc = editor_obj.contentWindow.document;
    // inserted by lvn
    // show table borders
    nullBorders(editdoc,'hide');
    // end insert by lvn
    var contents = editdoc.body.createTextRange().htmlText;
    editor_obj.outerHTML = TextEdit;
    editor_obj = document.getElementById("_" +objname + "_editor");
    editor_obj.value = contents;
    editor_event(objname);
    // inserted by lvn
    if (config.showborders) {
      editor_updateToolbar(objname, "disable");
      config.showborders =  true;
    } else {
    // end insert by lvn
    editor_updateToolbar(objname, "disable");  // disable toolbar items
    // insert by lvn
    }
    // end insert by lvn

// set event handlers
    editor_obj.onkeydown   = function() { editor_event(objname); }
    editor_obj.onkeypress  = function() { editor_event(objname); }
    editor_obj.onkeyup     = function() { editor_event(objname); }
    editor_obj.onmouseup   = function() { editor_event(objname); }
    editor_obj.ondrop      = function() { editor_event(objname, 100); }     // these events fire before they occur
    editor_obj.oncut       = function() { editor_event(objname, 100); }
    editor_obj.onpaste     = function() { editor_event(objname, 100); }
    editor_obj.onblur      = function() { editor_event(objname, -1); }

    editor_updateOutput(objname);
    editor_focus(editor_obj);
  }

  //
  // Switch to WYSIWYG mode
  //

  else {
    config.mode = "wysiwyg";
    var contents = editor_obj.value;
    if (mode == 'init') { contents = document.getElementById(objname).value; } // on init use original textarea content

    // create editor
    editor_obj.outerHTML = RichEdit;
    editor_obj = document.getElementById("_" +objname + "_editor");

    // get iframe document object

    // create editor contents (and default styles for editor)
    var html = "";
    html += '<html><head>\n';
    if (config.stylesheet) {
      html += '<link href="' +config.stylesheet+ '" rel="stylesheet" type="text/css">\n';
    }
    html += '<style>\n';
    html += 'body {' +config.bodyStyle+ '} \n';
        html += '.borders {' +config.bordersStyle+ '} \n';
    for (var i in config.fontstyles) {
      var fontstyle = config.fontstyles[i];
      if (fontstyle.classStyle) {
        html += '.' +fontstyle.className+ ' {' +fontstyle.classStyle+ '}\n';
      }
    }
    html += '</style>\n'
      + '</head>\n'
      + '<body contenteditable="true" topmargin=1 leftmargin=1'

// still working on this
// updated by lvn: table actions (uncommented next line to show in popupmenu)
      + ' oncontextmenu="parent.editor_cMenu_generate(window,\'' +objname+ '\');"'
      +'>'
      + contents
      + '</body>\n'
      + '</html>\n';

    // write to editor window
    var editdoc = editor_obj.contentWindow.document;

    editdoc.open();
    editdoc.write(html);
    editdoc.close();

        editor_updateToolbar(objname, "enable");  // enable toolbar items

    // store objname under editdoc
    editdoc.objname = objname;

    // set event handlers
    editdoc.onkeydown      = function() { editor_event(objname); }
    editdoc.onkeypress     = function() { editor_event(objname); }
    editdoc.onkeyup        = function() { editor_event(objname); }
    editdoc.onmouseup      = function() { editor_event(objname); }
    editdoc.body.ondrop    = function() { editor_event(objname, 100); }     // these events fire before they occur
    editdoc.body.oncut     = function() { editor_event(objname, 100); }
    editdoc.body.onpaste   = function() { editor_event(objname, 100); }
    editdoc.body.onblur    = function() { editor_event(objname, -1); }

    // inserted by lvn
    // show table borders

        // show table borders
if (config.showborders) {
nullBorders(editdoc,'show');
var btnObj = document.getElementById("_" +objname+ "_ShowBorder");
if(btnObj) { btnObj.className = 'btnDown'; }
}

    // end insert by lvn

    // bring focus to editor
    if (mode != 'init') {             // don't focus on page load, only on mode switch
      editor_focus(editor_obj);
// insert by lvn : check editor changes)
    } else {
         if (config.checkChanges == 1) {
            var localVar = getGlobalVar("objnames");
            if (localVar == null){
               setGlobalVar("objnames",objname);
            } else {
               localVar = localVar + ',' + objname;
               setGlobalVar("objnames",localVar);
            }
            setGlobalVar("_" +objname + "_initialText",editdoc.body.innerHTML);
            if (window.onbeforeunload == null){window.onbeforeunload = function() {discardOnExit();}}
         }
// end insert by lvn


    }

  }

  // Call update UI

  if (mode != 'init') {             // don't update UI on page load, only on mode switch
    editor_event(objname);
  }

}

/* ---------------------------------------------------------------------- *\
  Function    : editor_focus
  Description : bring focus to the editor
  Usage       : editor_focus(editor_obj);
  Arguments   : editor_obj - editor object
\* ---------------------------------------------------------------------- */

function editor_focus(editor_obj) {

  // check editor mode
  if (editor_obj.tagName.toLowerCase() == 'textarea') {         // textarea
    var myfunc = function() { editor_obj.focus(); };
    setTimeout(myfunc,100);                                     // doesn't work all the time without delay
  }

  else {                                                        // wysiwyg
    var editdoc = editor_obj.contentWindow.document;            // get iframe editor document object
    var editorRange = editdoc.body.createTextRange();           // editor range
    var curRange    = editdoc.selection.createRange();          // selection range

    if (curRange.length == null &&                              // make sure it's not a controlRange
        !editorRange.inRange(curRange)) {                       // is selection in editor range
      editorRange.collapse();                                   // move to start of range
      editorRange.select();                                     // select
      curRange = editorRange;
    }
  }

}

/* ---------------------------------------------------------------------- *\
  Function    : editor_about
  Description : display "about this editor" popup
\* ---------------------------------------------------------------------- */

function editor_about(objname) {
  showModalDialog(_editor_url + "popups/about.html", window, "resizable: yes; help: no; status: no; scroll: no; ");
}

/* ---------------------------------------------------------------------- *\
  Function    : _dec_to_rgb
  Description : convert dec color value to rgb hex
  Usage       : var hex = _dec_to_rgb('65535');   // returns FFFF00
  Arguments   : value   - dec value
\* ---------------------------------------------------------------------- */

function _dec_to_rgb(value) {
  var hex_string = "";
  for (var hexpair = 0; hexpair < 3; hexpair++) {
    var myByte = value & 0xFF;            // get low byte
    value >>= 8;                        // drop low byte
    var nybble2 = myByte & 0x0F;          // get low nybble (4 bits)
    var nybble1 = (myByte >> 4) & 0x0F;   // get high nybble
    hex_string += nybble1.toString(16); // convert nybble to hex
    hex_string += nybble2.toString(16); // convert nybble to hex
  }
  return hex_string.toUpperCase();
}

/* ---------------------------------------------------------------------- *\
  Function    : editor_insertHTML
  Description : insert string at current cursor position in editor.
                                If two strings are specifed, surround selected text with them.
  Usage       : editor_insertHTML(objname, str1, [str2], reqSelection)
  Arguments   : objname - ID of textarea
                str1 - HTML or text to insert
                str2 - HTML or text to insert (optional argument)
                reqSelection - (1 or 0) give error if no text selected
\* ---------------------------------------------------------------------- */
                function editor_insertHTML(objname, str1,str2, reqSel) {
                var config     = document.getElementById(objname).config;
                var editor_obj = document.getElementById("_" +objname + "_editor");    // editor object
                if (str1 == null) { str1 = ''; }
                if (str2 == null) { str2 = ''; }

// for non-wysiwyg capable browsers just add to end of textbox
                if (document.getElementById(objname) && editor_obj == null) {
                document.getElementById(objname).focus();
                document.getElementById(objname).value = document.getElementById(objname).value + str1 + str2;
                return;
                }

// error checking
  if (editor_obj == null) { return alert("Unable to insert HTML.  Invalid object name '" +objname+ "'."); }

  editor_focus(editor_obj);

  var tagname = editor_obj.tagName.toLowerCase();
  var sRange;

 // insertHTML for wysiwyg iframe
  if (tagname == 'iframe') {
    var editdoc = editor_obj.contentWindow.document;
    sRange  = editdoc.selection.createRange();
    var sHtml   = sRange.htmlText;

    // check for control ranges
    if (sRange.length) { return alert("Unable to insert HTML.  Try highlighting content instead of selecting it."); }

    // insert HTML
    var oldHandler = window.onerror;
    window.onerror = function() { alert("Unable to insert HTML for current selection."); return true; } // partial table selections cause errors
    if (sHtml.length) {                                 // if content selected
      if (str2) { sRange.pasteHTML(str1 +sHtml+ str2) } // surround
      else      { sRange.pasteHTML(str1); }             // overwrite
    } else {                                            // if insertion point only
      if (reqSel) { return alert("Unable to insert HTML.  You must select something first."); }
      sRange.pasteHTML(str1 + str2);                    // insert strings
    }
    window.onerror = oldHandler;
  }

  // insertHTML for plaintext textarea
  else if (tagname == 'textarea') {
    editor_obj.focus();
    sRange  = document.selection.createRange();
    var sText   = sRange.text;

    // insert HTML
    if (sText.length) {                                 // if content selected
      if (str2) { sRange.text = str1 +sText+ str2; }  // surround
      else      { sRange.text = str1; }               // overwrite
    } else {                                            // if insertion point only
      if (reqSel) { return alert("Unable to insert HTML.  You must select something first."); }
      sRange.text = str1 + str2;                        // insert strings
    }
  }
  else { alert("Unable to insert HTML.  Unknown object tag type '" +tagname+ "'."); }

  // move to end of new content
  sRange.collapse(false); // move to end of range
  sRange.select();        // re-select

}

/* ---------------------------------------------------------------------- *\
  Function    : editor_getHTML
  Description : return HTML contents of editor (in either wywisyg or html mode)
  Usage       : var myHTML = editor_getHTML('objname');
\* ---------------------------------------------------------------------- */

function editor_getHTML(objname) {
  var editor_obj = document.getElementById("_" +objname + "_editor");
  var isTextarea = (editor_obj.tagName.toLowerCase() == 'textarea');

  if (isTextarea) { return editor_obj.value; }
  else            { return editor_obj.contentWindow.document.body.innerHTML; }
}

/* ---------------------------------------------------------------------- *\
  Function    : editor_setHTML
  Description : set HTML contents of editor (in either wywisyg or html mode)
  Usage       : editor_setHTML('objname',"<b>html</b> <u>here</u>");
\* ---------------------------------------------------------------------- */

function editor_setHTML(objname, html) {
  var editor_obj = document.getElementById("_" +objname + "_editor");
  var isTextarea = (editor_obj.tagName.toLowerCase() == 'textarea');

  if (isTextarea) { editor_obj.value = html; }
  else            { editor_obj.contentWindow.document.body.innerHTML = html; }
}

/* ---------------------------------------------------------------------- *\
  Function    : editor_appendHTML
  Description : append HTML contents to editor (in either wywisyg or html mode)
  Usage       : editor_appendHTML('objname',"<b>html</b> <u>here</u>");
\* ---------------------------------------------------------------------- */

function editor_appendHTML(objname, html) {
  var editor_obj = document.getElementById("_" +objname + "_editor");
  var isTextarea = (editor_obj.tagName.toLowerCase() == 'textarea');

  if (isTextarea) { editor_obj.value += html; }
  else            { editor_obj.contentWindow.document.body.innerHTML += html; }
}

/* ---------------------------------------------------------------- */

function _isMouseOver(obj,event) {       // determine if mouse is over object
  var mouseX    = event.clientX;
  var mouseY    = event.clientY;

  var objTop    = obj.offsetTop;
  var objBottom = obj.offsetTop + obj.offsetHeight;
  var objLeft   = obj.offsetLeft;
  var objRight  = obj.offsetLeft + obj.offsetWidth;

  if (mouseX >= objLeft && mouseX <= objRight &&
      mouseY >= objTop  && mouseY <= objBottom) { return true; }

  return false;
}

/* ---------------------------------------------------------------- */

function editor_cMenu_generate(editorWin,objname) {
  var parentWin = window;
  editorWin.event.returnValue = false;  // cancel default context menu
// inserted by lvn : table operations
  var table_object      = document.getElementById("_" +objname + "_editor");
  var table_src_element = table_object.contentWindow.event.srcElement;
// end insert bylvn

  // define content menu options
  var cMenuOptions = [ // menu name, shortcut displayed, javascript code
    ['Cut', 'Ctrl-X', function() {}],
    ['Copy', 'Ctrl-C', function() {}],
    ['Paste', 'Ctrl-C', function() {}],
    ['Delete', 'DEL', function() {}],
    ['---', null, null],
    ['Select All', 'Ctrl-A', function() {}],
    ['Clear All', '', function() {}],
    ['---', null, null],
    ['About this editor...', '', function() {
      alert("about this editor");    }]
        ];
// inserted by lvn: table operations
   // (uncomment to have more elements in popup menu)
   if (table_src_element.tagName == 'TD') {
      // set the contextmenu for tableactions when clicked in a table
       cMenuOptions = [
   //    ['Insert Table' , ''            , function() {editor_action('_' + objname + '_' + 'InsertTable');}],
   //    ['Delete Table' , ''            , function() {tables_action('_' + objname + '_' + 'DeleteTable',table_src_element);}],
   //    ['Insert Caption' , ''          , function() {tables_action('_' + objname + '_' + 'CreateCaption',table_src_element);}],
   //    ['Delete Caption' , ''          , function() {tables_action('_' + objname + '_' + 'DeleteCaption',table_src_element);}],
   //    ['Insert Head' , ''             , function() {tables_action('_' + objname + '_' + 'CreateTHead',table_src_element);}],
   //    ['Delete Head' , ''             , function() {tables_action('_' + objname + '_' + 'DeleteTHead',table_src_element);}],
   //    ['Insert Foot' , ''             , function() {tables_action('_' + objname + '_' + 'CreateTFoot',table_src_element);}],
   //    ['Delete Foot' , ''             , function() {tables_action('_' + objname + '_' + 'DeleteTFoot',table_src_element);}],
   //    ['Insert Row at Top'   , ''     , function() {tables_action('_' + objname + '_' + 'InsertRowTop',table_src_element );}],
   //    ['Insert Row at Bottom'   , ''  , function() {tables_action('_' + objname + '_' + 'InsertRowBottom',table_src_element );}],
   //    ['Insert Row before'   , ''     , function() {tables_action('_' + objname + '_' + 'InsertRowBefore',table_src_element );}],
   //    ['Insert Row after'   , ''      , function() {tables_action('_' + objname + '_' + 'InsertRowAfter',table_src_element );}],
   //    ['Delete Row'   , ''            , function() {tables_action('_' + objname + '_' + 'DeleteRow',table_src_element );}],
   //    ['Insert Column leftmost', ''   , function() {tables_action('_' + objname + '_' + 'InsertColumnLeft',table_src_element );}],
   //    ['Insert Column righttmost', '' , function() {tables_action('_' + objname + '_' + 'InsertColumnRight',table_src_element );}],
         ['Insert Column before', ''     , function() {tables_action('_' + objname + '_' + 'InsertColumnBefore',table_src_element );}],
         ['Insert Column after', ''      , function() {tables_action('_' + objname + '_' + 'InsertColumnAfter',table_src_element );}],

                 ['Delete Column', ''            , function() {tables_action('_' + objname + '_' + 'DeleteColumn',table_src_element );}],
   //    ['Insert Cell leftmost', ''     , function() {tables_action('_' + objname + '_' + 'InsertCellLeft',table_src_element );}],
   //    ['Insert Cell rightmost'  , ''  , function() {tables_action('_' + objname + '_' + 'InsertCellRight',table_src_element );}],
         ['Insert Cell before'  , ''     , function() {tables_action('_' + objname + '_' + 'InsertCellBefore',table_src_element );}],
         ['Insert Cell after'  , ''      , function() {tables_action('_' + objname + '_' + 'InsertCellAfter',table_src_element );}],
         ['Delete Cell'  , ''            , function() {tables_action('_' + objname + '_' + 'DeleteCell',table_src_element );}],
         ['Split Cell'  , ''             , function() {tables_action('_' + objname + '_' + 'SplitCell',table_src_element );}],
         ['Merge Cells'  , ''            , function() {tables_action('_' + objname + '_' + 'MergeCells',table_src_element );}],
         ['Split Row'  , ''              , function() {tables_action('_' + objname + '_' + 'SplitRow',table_src_element );}],
     //   ['Merge Rows'  , ''             , function() {tables_action('_' + objname + '_' + 'MergeRows',table_src_element );}],
// inserted by lvn : property pallettes
         ['Table Properties'  , ''       , function() {tables_action('_' + objname + '_' + 'TableProperties',table_src_element );}],
         ['Row Properties'  , ''         , function() {tables_action('_' + objname + '_' + 'RowProperties',table_src_element );}],
         ['Cell Properties'  , ''        , function() {tables_action('_' + objname + '_' + 'CellProperties',table_src_element );}]
// end insert lvn property pallettes
      ];
   } else {
      // reset to de default browser contextmenu
      editorWin.event.returnValue = true;
      return;
   }
// end insert by lvn

    editor_cMenu.options = cMenuOptions; // save options

  // generate context menu
  var cMenuHeader = ''
    + '<div id="_'+objname+'_cMenu" onblur="editor_cMenu(this);" oncontextmenu="return false;" onselectstart="return false"'
    + 'style="position: absolute; visibility: hidden; cursor: default; width: 167px; background-color: threedface;'
    + ' border: solid 1px; border-color: threedlightshadow threeddarkshadow threeddarkshadow threedlightshadow;">'
    + '<table border=0 cellspacing=0 cellpadding=0 width="100%" style="width: 167px; background-color: threedface; border: solid 1px; border-color: threedhighlight threedshadow threedshadow threedhighlight;">'
    + ' <tr><td colspan=2 height=1></td></tr>';

  var cMenuList = '';

  var cMenuFooter = ''
    + ' <tr><td colspan=2 height=1></td></tr>'
    + '</table></div>';

  for (var menuIdx in editor_cMenu.options) {
    var menuName = editor_cMenu.options[menuIdx][0];
    var menuKey  = editor_cMenu.options[menuIdx][1];
    var menuCode = editor_cMenu.options[menuIdx][2];

    // separator
    if (menuName == "---" || menuName == "separator") {
      cMenuList += ' <tr><td colspan=2 class="cMenuDivOuter"><div class="cMenuDivInner"></div></td></tr>';
    }

    // menu option
    else {
      cMenuList += '<tr class="cMenu" onmouseover="editor_cMenu(this)" onmouseout="editor_cMenu(this)" onclick="editor_cMenu(this, \'' +menuIdx+ '\',\'' +objname+ '\')">';
      if (menuKey) { cMenuList += ' <td align=left class="cMenu">' +menuName+ '</td><td align=right class="cMenu">' +menuKey+ '</td>'; }
      else         { cMenuList += ' <td colspan=2 class="cMenu">' +menuName+ '</td>'; }
      cMenuList += '</tr>';
    }
  }

  var cMenuHTML = cMenuHeader + cMenuList + cMenuFooter;


  document.getElementById('_'+objname+'_cMenu').outerHTML = cMenuHTML;

  editor_cMenu_setPosition(parentWin, editorWin, objname);

  parentWin['_'+objname+'_cMenu'].style.visibility = 'visible';
  parentWin['_'+objname+'_cMenu'].focus();

}

/* ---------------------------------------------------------------- */

function editor_cMenu_setPosition(parentWin, editorWin, objname) {      // set object position that won't overlap window edge
  var event    = editorWin.event;
  var cMenuObj = parentWin['_'+objname+'_cMenu'];
  var mouseX   = event.clientX + parentWin.document.getElementById('_'+objname+'_editor').offsetLeft;
  var mouseY   = event.clientY + parentWin.document.getElementById('_'+objname+'_editor').offsetTop;
  var cMenuH   = cMenuObj.offsetHeight;
  var cMenuW   = cMenuObj.offsetWidth;
  var pageH    = document.body.clientHeight + document.body.scrollTop;
  var pageW    = document.body.clientWidth + document.body.scrollLeft;

  // set horzontal position
  if (mouseX + 5 + cMenuW > pageW) { var left = mouseX - cMenuW - 5; } // too far right
  else                            { var left = mouseX + 5; }

  // set vertical position
  if (mouseY + 5 + cMenuH > pageH) { var top = mouseY - cMenuH + 5; } // too far down
  else                            { var top = mouseY + 5; }

  cMenuObj.style.top = top;
  cMenuObj.style.left = left;

}

/* ---------------------------------------------------------------- */

function editor_cMenu(obj,menuIdx,objname) {
  var action = event.type;
  if      (action == "mouseover" && !obj.disabled && obj.tagName.toLowerCase() == 'tr') {
    obj.className = 'cMenuOver';
    for (var i=0; i < obj.cells.length; i++) { obj.cells[i].className = 'cMenuOver'; }
  }
  else if (action == "mouseout" && !obj.disabled && obj.tagName.toLowerCase() == 'tr')  {
    obj.className = 'cMenu';
    for (var i=0; i < obj.cells.length; i++) { obj.cells[i].className = 'cMenu'; }
  }
  else if (action == "click" && !obj.disabled) {
    document.getElementById('_'+objname+'_cMenu').style.visibility = "hidden";
    var menucode = editor_cMenu.options[menuIdx][2];
    menucode();
  }
  else if (action == "blur") {
    if (!_isMouseOver(obj,event)) { obj.style.visibility = 'hidden'; }
    else {
      if (obj.style.visibility != "hidden") { obj.focus(); }
    }
  }
  else { alert("editor_cMenu, unknown action: " + action); }
}

/* ---------------------------------------------------------------------- */
// insert by lvn : find + insertTable
// insert by lvn : check editor changes
/* ---------------------------------------------------------------------- *\
  Function    : setGlobalVar
  Description : set a variable with a global scope
  Usage       : setGlobalVar(varName, value);
  Arguments   : varName - name of the global variable to set
                value - value of the global variable to set
\* ---------------------------------------------------------------------- */
function setGlobalVar(varName, value) {
   if (this.cache == null) {this.cache = new Object();}
   this.cache[varName] = value;
}
/* ---------------------------------------------------------------------- *\
  Function    : getGlobalVar
  Description : get a variable in a global scope
  Usage       : value = getGlobalVar(varName);
  Arguments   : varName - name of the global variable to get
                value - value of the global variable to get
\* ---------------------------------------------------------------------- */
function getGlobalVar(varName, value) {
   if (this.cache == null) {
     return null;
   } else {
     return this.cache[varName];
   }
}
// insert by lvn : check editor changes
/* ---------------------------------------------------------------------- *\
  Function    : discardOnExit
  Description : check if contents have been changed and ask user confirmation
                to discard changes
  Usage       : discardOnExit();
\* ---------------------------------------------------------------------- */
function discardOnExit(){
   var objNames = getGlobalVar("objnames").split(",");
   for (var i=0;i < objNames.length;i++){
       if (document.getElementById("_" +objNames[i] + "_editor").contentWindow.document.body.innerHTML
           != getGlobalVar("_" + objNames[i] + "_initialText")) {
          event.returnValue = "Your document has been changed. Discard changes?";
       }
   }
}
// end insert by lvn

// WME: MS-Word clean-up (begin)
/* ---------------------------------------------------------------------- *\
  Function    : MS-Word clean-up
  Description : replace textarea with wysiwyg editor
  Usage       : editor_generate("textarea_id",[height],[width]);
  Arguments   : objname - ID of textarea to replace
                w       - width of wysiwyg editor
                h       - height of wysiwyg editor
\* ---------------------------------------------------------------------- */


function cleanEmptyTag(oElem) {
if (oElem.hasChildNodes) {
var tmp = oElem
for (var k = tmp.children.length; k >= 0; k--) {
if (tmp.children[k] != null) {
cleanEmptyTag(tmp.children[k]);
}
}

}

var oAttribs = oElem.attributes;
if (oAttribs != null) {
for (var j = oAttribs.length - 1; j >=0; j--) {
var oAttrib = oAttribs[j];
if (oAttrib.nodeValue != null) {
oAttribs.removeNamedItem('class')
}
        }
        }
        oElem.style.cssText = '';
if (oElem.innerHTML == '' || oElem.innerHTML == ' ') {
oElem.outerHTML = '';   }
}

function cleanTable(oElem) {
        oElem.style.cssText = '';
        var oAttribs = oElem.attributes;
        if (oAttribs != null) {
                for (var j = oAttribs.length - 1; j >=0; j--) {
                        var oAttrib = oAttribs[j];
                        if (oAttrib.nodeValue != null) {
                                oAttribs.removeNamedItem('class')
                        }
                }
        }
        var oTR = oElem.rows;
        if (oTR != null) {
                for (var r = oTR.length - 1; r >= 0; r--) {
                        oTR[r].style.cssText = '';
                }
        }
        var oTD = oElem.cells;
        if (oTD != null) {
                for (var t = oTD.length - 1; t >= 0; t--) {
                        oTD[t].style.cssText = '';
                }
        }
}