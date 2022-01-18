$( '#findPassword' ).click(function() {
                        var token = $( "meta[name= '_csrf']" ).attr('content');
                        var header = $( "meta[name='_csrf_header']").attr("content");
                        var email = $( '#find_email' ).val();

                        var data = { "address" : email };
                        var dataJSON = JSON.stringify(data);
                        $.ajax({
                            url : "/email/findPassword",
                            method : "POST",
                            contentType : "application/json; UTF-8",
                            data : dataJSON,
                            beforeSend : function(xhr) {
                                xhr.setRequestHeader(header, token);
                            },
                            success : function(result) {
                            },
                            error : function(xhr, status, error) {
                                Swal.fire({
                                    icon: 'error',
                                    title: xhr.responseJSON.code,
                                    text: xhr.responseJSON.message
                                  });
                            }
                        });
                  });