$( '#putNickname' ).click(function() {
                        var token = $( "meta[name= '_csrf']" ).attr('content');
                        var header = $( "meta[name='_csrf_header']").attr("content");
                        var nickname = $( '#put_nickname' ).val();

                        var data = { "nickname" : nickname };
                        var dataJSON = JSON.stringify(data);
                        $.ajax({
                            url : "/info/nickname",
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