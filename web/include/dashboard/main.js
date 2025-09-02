$(document).ready(function() {

var $container = $('#container');

$('#container').isotope({
  masonry: {
    columnWidth: 73,
    gutter: 5
  }
});

$('#filters a').click(function(){
  var selector = $(this).attr('data-filter');
  $container.isotope({ filter: selector });
  return false;
});

$('.remove-link').click(function(){
  $('.remove-link').addClass('hide');
  });
$('#filters a').click(function(){
  $('.remove-link').removeClass('hide');
});



// $( ".reveal h2" ).hover (function() {
//  $(this).siblings(".reveally").show();
// }, function() {
//  $(this).siblings(".reveally").hide();
// });



$(function()
{
    var $dropdowns = $('li.dropdown'); // Specifying the element is faster for older browsers

    /**
     * Mouse events
     *
     * @description Mimic hoverIntent plugin by waiting for the mouse to 'settle' within the target before triggering
     */
    $dropdowns
        .on('mouseover', function() // Mouseenter (used with .hover()) does not trigger when user enters from outside document window
        {
            var $this = $(this);

            if ($this.prop('hoverTimeout'))
            {
                $this.prop('hoverTimeout', clearTimeout($this.prop('hoverTimeout')));
            }

            $this.prop('hoverIntent', setTimeout(function()
            {
                $this.addClass('hover');
            }, 250));
        })
        .on('mouseleave', function()
        {
            var $this = $(this);

            if ($this.prop('hoverIntent'))
            {
                $this.prop('hoverIntent', clearTimeout($this.prop('hoverIntent')));
            }

            $this.prop('hoverTimeout', setTimeout(function()
            {
                $this.removeClass('hover');
            }, 250));
        });

    /**
     * Touch events
     *
     * @description Support click to open if we're dealing with a touchscreen
     */
    if ('ontouchstart' in document.documentElement)
    {
        $dropdowns.each(function()
        {
            var $this = $(this);

            this.addEventListener('touchstart', function(e)
            {
                if (e.touches.length === 1)
                {
                    // Prevent touch events within dropdown bubbling down to document
                    e.stopPropagation();

                    // Toggle hover
                    if (!$this.hasClass('hover'))
                    {
                        // Prevent link on first touch
                        if (e.target === this || e.target.parentNode === this)
                        {
                            e.preventDefault();
                        }

                        // Hide other open dropdowns
                        $dropdowns.removeClass('hover');
                        $this.addClass('hover');

                        // Hide dropdown on touch outside
                        document.addEventListener('touchstart', closeDropdown = function(e)
                        {
                            e.stopPropagation();

                            $this.removeClass('hover');
                            document.removeEventListener('touchstart', closeDropdown);
                        });
                    }
                }
            }, false);
        });
    }
    
});

$('div.accordionButton').click(function() {
if ($('div.accordionContent').hasClass('openDiv')) {
$('div.accordionContent').slideUp('normal');
$(this).next().removeClass('openDiv');
}
else {
$('div.accordionContent').slideUp('normal');
$(this).next().slideDown('normal');
$(this).next().addClass('openDiv');
}
});
$("div.accordionContent").hide();

$('div.baccordionButton').click(function() {
if ($('div.baccordionContent').hasClass('openDiv')) {
$('div.baccordionContent').slideUp('normal');
$(this).next().removeClass('openDiv');
}
else {
$('div.baccordionContent').slideUp('normal');
$(this).next().slideDown('normal');
$(this).next().addClass('openDiv');
}
});
$("div.baccordionContent").hide();


});