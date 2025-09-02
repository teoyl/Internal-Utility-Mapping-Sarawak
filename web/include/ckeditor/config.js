/**
 * @license Copyright (c) 2003-2017, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 * http://nightly.ckeditor.com/18-03-05-07-04/full/samples/toolbarconfigurator/index.html#basic
 */

CKEDITOR.editorConfig = function(config) {
    /*config.toolbarGroups = [
        {name: 'document', groups: ['mode', 'document', 'doctools']},
        {name: 'forms', groups: ['forms']},
        '/',
        {name: 'editing', groups: ['find', 'selection', 'spellchecker', 'editing']},
        {name: 'basicstyles', groups: ['basicstyles', 'cleanup']},
        {name: 'clipboard', groups: ['clipboard', 'undo']},
        {name: 'paragraph', groups: ['list', 'indent', 'blocks', 'align', 'bidi', 'paragraph']},
        {name: 'links', groups: ['links']},
        {name: 'insert', groups: ['insert']},
        '/',
        {name: 'styles', groups: ['styles']},
        {name: 'colors', groups: ['colors']},
        {name: 'tools', groups: ['tools']},
        {name: 'others', groups: ['others']},
        {name: 'about', groups: ['about']}
    ];*/
	/*config.toolbarGroups = [
		{ name: 'document', groups: [ 'mode', 'document', 'doctools' ] },
		{ name: 'clipboard', groups: [ 'clipboard', 'undo' ] },
		{ name: 'editing', groups: [ 'find', 'selection', 'spellchecker', 'editing' ] },
		{ name: 'forms', groups: [ 'forms' ] },
		'/',
		{ name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ] },
		{ name: 'paragraph', groups: [ 'list', 'indent', 'blocks', 'align', 'bidi', 'paragraph' ] },
		{ name: 'links', groups: [ 'links' ] },
		{ name: 'insert', groups: [ 'insert' ] },
		'/',
		{ name: 'styles', groups: [ 'styles' ] },
		{ name: 'colors', groups: [ 'colors' ] },
		{ name: 'tools', groups: [ 'tools' ] },
		{ name: 'others', groups: [ 'others' ] },
		{ name: 'about', groups: [ 'about' ] }
	];*/
        config.toolbarGroups = [
		{ name: 'document', groups: [ 'mode', 'document', 'doctools' ] },
		{ name: 'editing', groups: [ 'find', 'selection', 'spellchecker', 'editing' ] },
		{ name: 'forms', groups: [ 'forms' ] },
		'/',
		{ name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ] },
		{ name: 'paragraph', groups: [ 'align', 'list', 'indent', 'blocks', 'bidi', 'paragraph' ] },
		{ name: 'clipboard', groups: [ 'clipboard', 'undo' ] },
		{ name: 'links', groups: [ 'links' ] },
		{ name: 'insert', groups: [ 'insert' ] },
		'/',
		{ name: 'styles', groups: [ 'styles' ] },
		{ name: 'colors', groups: [ 'colors' ] },
		{ name: 'tools', groups: [ 'tools' ] },
		{ name: 'others', groups: [ 'others' ] },
		{ name: 'about', groups: [ 'about' ] }
	];

	config.removeButtons = 'Source,Table,Save,NewPage,Preview,Print,PasteFromWord,PasteText,Paste,Copy,Cut,Templates,Find,Replace,SelectAll,Scayt,Form,Checkbox,Radio,TextField,Textarea,Select,Button,ImageButton,HiddenField,Strike,Subscript,Superscript,CopyFormatting,RemoveFormat,CreateDiv,Blockquote,BidiLtr,Image,Flash,Unlink,BidiRtl,Language,Anchor,HorizontalRule,Smiley,SpecialChar,PageBreak,Iframe,Styles,Format,Font,FontSize,TextColor,BGColor,Maximize,ShowBlocks,About';
//	config.removeButtons = 'Source,Table,Save,NewPage,Preview,Print,PasteFromWord,PasteText,Paste,Copy,Cut,Templates,Find,Replace,SelectAll,Scayt,Form,Checkbox,Radio,TextField,Textarea,Select,Button,ImageButton,HiddenField,Strike,Subscript,Superscript,CopyFormatting,RemoveFormat,CreateDiv,Blockquote,JustifyLeft,JustifyCenter,JustifyRight,JustifyBlock,BidiLtr,Link,Image,Flash,Unlink,BidiRtl,Language,Anchor,HorizontalRule,Smiley,SpecialChar,PageBreak,Iframe,Styles,Format,Font,FontSize,TextColor,BGColor,Maximize,ShowBlocks,About';
//    config.removeButtons = 'Source，Save,Templates,NewPage,Preview,Print,PasteFromWord,PasteText,Paste,Copy,Cut,Find,Replace,SelectAll,Scayt,Form,Checkbox,Radio,TextField,Textarea,Select,Button,ImageButton,HiddenField,Strike,Subscript,Superscript,CopyFormatting,RemoveFormat,Blockquote,CreateDiv,BidiLtr,BidiRtl,Anchor,Unlink,Link,Image,Flash,Table,HorizontalRule,Smiley,SpecialChar,PageBreak,Iframe,Styles,Format,Font,FontSize,BGColor,ShowBlocks,Maximize,TextColor,About,Language';
    config.removePlugins = 'elementspath';
    config.extraPlugins = 'justify,wordcount,notification,contextmenu,dialog,dialogui,menu,floatpanel,panel,liststyle,lineheight'; 
//    config.enterMode = CKEDITOR.ENTER_BR;

    config.enterMode = CKEDITOR.ENTER_BR;
    config.shiftEnterMode = CKEDITOR.ENTER_BR;
    config.wordcount = {
        // Whether or not you want to show the Paragraphs Count
        showParagraphs: false,

        // Whether or not you want to show the Word Count
        showWordCount: false,

        // Whether or not you want to show the Char Count
        showCharCount: true,

        // Whether or not you want to count Spaces as Chars
        countSpacesAsChars: true,

        // Whether or not to include Html chars in the Char Count
        countHTML: false,

        // Maximum allowed Word Count, -1 is default for unlimited
        maxWordCount: 4000,

        // Maximum allowed Char Count, -1 is default for unlimited
        maxCharCount: 4000};
//    config.extraAllowedContent = 'table tr td';
//    config.allowedContent = true;

};
//
//CKEDITOR.on('instanceReady', function (ev) {
//        ev.editor.dataProcessor.writer.setRules('br',
//         {
//             indent: false,
//             breakBeforeOpen: false,
//             breakAfterOpen: false,
//             breakBeforeClose: false,
//             breakAfterClose: false
//         });
//    });


CKEDITOR.on('instanceReady', function( ev ) {
  var blockTags = ['div','h1','h2','h3','h4','h5','h6','p','pre','li','blockquote','ul','ol',
  'table','thead','tbody','tfoot','td','th','br',];

  for (var i = 0; i < blockTags.length; i++)
  {
     ev.editor.dataProcessor.writer.setRules( blockTags[i], {
        indent : false,
        breakBeforeOpen : false,
        breakAfterOpen : false,
        breakBeforeClose : false,
        breakAfterClose : false
     });
  }
});




