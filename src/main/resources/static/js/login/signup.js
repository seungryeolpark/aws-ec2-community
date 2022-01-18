$( '#email' ).focusout(function() {
                        var token = $( "meta[name='_csrf']" ).attr('content');
                        var header = $( "meta[name='_csrf_header']").attr("content");
                        var email = $( '#email' ).val();

                        var data = { "address" : email };
                        var dataJSON = JSON.stringify(data);
                        $.ajax({
                            url : "/email",
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
                                $( '#email' ).attr('class', 'form-control field-error');
                            }
                        });
                  });

      $( '#email_auth_button' ).click(function() {
                        var token = $( "meta[name='_csrf']" ).attr('content');
                        var header = $( "meta[name='_csrf_header']").attr("content");
                        var email = $( '#email' ).val();

                        var data = { "address" : email };
                        var dataJSON = JSON.stringify(data);
                        $.ajax({
                            url : "/email/auth",
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