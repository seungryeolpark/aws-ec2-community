//board image size
$( document ).ready( function() {
            $( '.image_resized' ).each( function(index, item) {
                var ratio = $( item ).attr('style');
                $( item ).children( 'img' ).prop('style', ratio);
            });
        });