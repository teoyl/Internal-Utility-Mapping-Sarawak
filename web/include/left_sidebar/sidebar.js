if (typeof jQuery === "undefined") {
    throw new Error("jQuery plugins need to be before this file");
}

$.espa = {};
/* Left Sidebar - Function ================================================================================================
*  You can manage the right sidebar menu options
*  
*/
$.espa.leftSideBar = {
    activate: function () {
        var _this = this;
        var $sidebar = $('#leftsidebar');
        var $switch = $('.espa-menu-switch');

        //onload to open the menu by default
//        $switch.attr('title', 'Click to show navigation.');
//        $sidebar.animate({left: "0px"}, 300, 'linear').addClass('visible');
//        $switch.animate({left: "-1px"}, 300, 'linear').addClass('visible');
            
        $(".espa-menu-switch").click(function() {
            if ($(this).hasClass('visible')) {
                $(".espa-menu-switch").attr('title', 'Click to show navigation.');
                $sidebar.animate({left: "-300px"}, 300, 'linear').removeClass('visible');
                $switch.animate({left: "-301px"}, 300, 'linear').removeClass('visible');
            } else {
                $(this).attr('title', 'Click to hide navigation.');
                $sidebar.animate({left: "0px"}, 300, 'linear').addClass('visible');
                $switch.animate({left: "-1px"}, 300, 'linear').addClass('visible');
            }
        });
    }
}
//==========================================================================================================================


/* Browser - Function ======================================================================================================
*  You can manage browser
*  
*/
var edge = 'Microsoft Edge';
var ie10 = 'Internet Explorer 10';
var ie11 = 'Internet Explorer 11';
var opera = 'Opera';
var firefox = 'Mozilla Firefox';
var chrome = 'Google Chrome';
var safari = 'Safari';

$.espa.browser = {
    activate: function () {
        var _this = this;
        var className = _this.getClassName();

        if (className !== '') $('html').addClass(_this.getClassName());
    },
    getBrowser: function () {
        var userAgent = navigator.userAgent.toLowerCase();

        if (/edge/i.test(userAgent)) {
            return edge;
        } else if (/rv:11/i.test(userAgent)) {
            return ie11;
        } else if (/msie 10/i.test(userAgent)) {
            return ie10;
        } else if (/opr/i.test(userAgent)) {
            return opera;
        } else if (/chrome/i.test(userAgent)) {
            return chrome;
        } else if (/firefox/i.test(userAgent)) {
            return firefox;
        } else if (!!navigator.userAgent.match(/Version\/[\d\.]+.*Safari/)) {
            return safari;
        }

        return undefined;
    },
    getClassName: function () {
        var browser = this.getBrowser();

        if (browser === edge) {
            return 'edge';
        } else if (browser === ie11) {
            return 'ie11';
        } else if (browser === ie10) {
            return 'ie10';
        } else if (browser === opera) {
            return 'opera';
        } else if (browser === chrome) {
            return 'chrome';
        } else if (browser === firefox) {
            return 'firefox';
        } else if (browser === safari) {
            return 'safari';
        } else {
            return '';
        }
    }
}
//==========================================================================================================================

$(function () {
    $.espa.browser.activate();
    $.espa.leftSideBar.activate();
    setTimeout(function () { $('.page-loader-wrapper').fadeOut(); }, 50);

    activateNotificationAndTasksScroll();
    setSkinListHeightAndScroll(true);
    setSettingListHeightAndScroll(true);
    $(window).resize(function () {
        setSkinListHeightAndScroll(false);
        setSettingListHeightAndScroll(false);
    });
});



//Skin tab content set height and show scroll
function setSkinListHeightAndScroll(isFirstTime) {
    var height = $(window).height()  - ($('.navbar').innerHeight() + $('.left-sidebar .nav-tabs').outerHeight());
    var $el = $('.demo-choose-skin');

    if (!isFirstTime){
      $el.slimScroll({ destroy: true }).height('auto');
      $el.parent().find('.slimScrollBar, .slimScrollRail').remove();
    }
    $el.slimscroll({
        height: height + 'px',
        color: 'rgba(0,0,0,0.5)',
        size: '6px',
        alwaysVisible: false,
        borderRadius: '0',
        railBorderRadius: '0'
    });
}

//Setting tab content set height and show scroll
function setSettingListHeightAndScroll(isFirstTime) {
    var height = $(window).height() - ($('.navbar').innerHeight() + $('.left-sidebar .nav-tabs').outerHeight() + 30);
    var $el = $('.left-sidebar .demo-settings');

    if (!isFirstTime){
      $el.slimScroll({ destroy: true }).height('auto');
      $el.parent().find('.slimScrollBar, .slimScrollRail').remove();
    }

    $el.slimscroll({
        height: height + 'px',
        color: 'rgba(0,0,0,0.5)',
        size: '6px',
        alwaysVisible: false,
        borderRadius: '0',
        railBorderRadius: '0'
    });
}

//Activate notification and task dropdown on top right menu
function activateNotificationAndTasksScroll() {
    $('.navbar-right .dropdown-menu .body .menu').slimscroll({
        height: '254px',
        color: 'rgba(0,0,0,0.5)',
        size: '4px',
        alwaysVisible: false,
        borderRadius: '0',
        railBorderRadius: '0'
    });
}
