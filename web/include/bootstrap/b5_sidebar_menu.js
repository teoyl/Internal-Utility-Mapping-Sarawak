$(document).ready(function () {          
    $.each($("#navbarVerticalCollapse .nav-link.dropdown-indicator"), function (i, theMenu) {
        var _menu = getCookie("_menu");
        var openedArr = _menu.split(',');
        for (var i = 0; i < openedArr.length; i++) {      
            var openMenu = openedArr[i];
            if (openMenu === $(theMenu).attr("href")) {
                $(theMenu)[0].click();
                break;
            }
        }
    });
    
    $("#navbarVerticalCollapse .nav-link.dropdown-indicator").click(function(e) {
        if (typeof $(this).attr("aria-expanded") === 'undefined') {
            removeMenu($(this).attr("href"));
        } else {
            if ($(this).attr("aria-expanded") === 'true') {
                addMenu($(this).attr("href"));
            } else {
                removeMenu($(this).attr("href"));
            }
        }
    });
});

function removeMenu(menuId) {
    var _menu = getCookie("_menu");
    if (typeof _menu === 'undefined') {
        return;
    } else {
        var menuArr = _menu.split(',');
        var newMenu = '';
        for(var i = 0; i < menuArr.length; i++) {
            var addedMenu = menuArr[i];
            if (addedMenu !== menuId) {
                if (newMenu === '') {
                    newMenu = addedMenu;
                } else {
                    newMenu += ','+addedMenu;
                }
            }
        }
        _menu = newMenu;
        document.cookie = "_menu=" + _menu;
    }
}
function addMenu(menuId) {
    var _menu = getCookie("_menu");
    if (typeof _menu === 'undefined') {
        document.cookie = "_menu=" + menuId;
    } else {
        if (_menu === '') {
            _menu = menuId;
        } else {
            _menu += ','+menuId;
        }
        document.cookie = "_menu=" + _menu;
    }
}
function getCookie(cname) {
  var name = cname + "=";
  var ca = document.cookie.split(';');
  for(var i = 0; i < ca.length; i++) {
    var c = ca[i];
    while (c.charAt(0) == ' ') {
      c = c.substring(1);
    }
    if (c.indexOf(name) == 0) {
      return c.substring(name.length, c.length);
    }
  }
  return "";
}