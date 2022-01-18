$( '#putPassword' ).click(function() {
                        var token = $( "meta[name= '_csrf']" ).attr('content');
                        var header = $( "meta[name='_csrf_header']").attr("content");
                        var email = $( "input[name= 'email']").val();
                        var password = $( '#put_password' ).val();

                        var data = { "email" : email, "password" : password };
                        var dataJSON = JSON.stringify(data);
                        $.ajax({
                            url : "/info/password",
                            method : "PUT",
                            contentType : "application/json; UTF-8",
                            data : dataJSON,
                            beforeSend : function(xhr) {
                                xhr.setRequestHeader(header, token);
                            },
                            success : function(result) {
                                location.replace('/');
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