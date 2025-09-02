$(".accordion-heading").click(function(e) {
    if (!fromStart) {
        if (typeof $(this).attr("aria-expanded") === 'undefined') {
            addMenu($(this).attr("data-target"));
        } else {
            if ($(this).attr("aria-expanded") === 'true') {
                removeMenu($(this).attr("data-target"));
            } else {
                addMenu($(this).attr("data-target"));
            }
        }
    }
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

$("#menu-toggle").click(function(e) {
	e.preventDefault();
	$("#wrapper").toggleClass("toggled");
});
 $("#menu-toggle-2").click(function(e) {
	e.preventDefault();
	$("#wrapper").toggleClass("toggled-2");
	//$('#menu ul').hide();
	$('.profile').toggle('show');
	$(".sidebar-nav-fixed").toggleClass('largeWidth');
        if ($('#wrapper').hasClass('toggled-2')) {
            $.get("menuSideBar?msb=H");
        } else {
            $.get("menuSideBar?msb=S");
        }
        reset_select2_size();
});

function reset_select2_size(obj)
{
    $('.select2-container').filter(':visible').parent().each(function() {
        $(this).find('.select2-container').css({"width":'100%'});
    });
}
/*
 function initMenu() {
  $('#menu ul').hide();
  $('#menu ul').children('.current').parent().show();
  $('#menu li a').click(
	function() {
	  var checkElement = $(this).next();
	  if((checkElement.is('ul')) && (checkElement.is(':visible'))) {
		return false;
		}
	  if((checkElement.is('ul')) && (!checkElement.is(':visible'))) {
		$('#menu ul:visible').slideUp('normal');
		checkElement.slideDown('normal');
		return false;
		}
	  }
	);
  }
$(document).ready(function() {initMenu();});
*/	
	
	