$( 'a[name=reply]' ).click(function() {
        const id = $(this).data('id');
        const depth = $(this).data('depth');

        $( '#nestedCommentForm_' + id ).css( 'margin-left', 20 * depth );
        $( '#putCommentForm_' + id ).hide();
        $( '#nestedCommentForm_' + id ).toggle();
});

$( 'a[name=edit]' ).click(function() {
        const id = $(this).data('id');
        const depth = $(this).data('depth');

        $( '#putCommentForm_' + id ).css( 'margin-left', 20 * depth );
        $( '#nestedCommentForm_' + id ).hide();
        $( '#putCommentForm_' + id ).toggle();

        console.log(id + ", " + depth);
});

$( 'a[name=delete]' ).click(async function() {
        const id = $(this).data('id');
        const login = $(this).data('login');

        const token = $( "meta[name='_csrf']" ).attr('content');
        const header = $( "meta[name='_csrf_header']").attr("content");

        let dataJSON;
        if (login === 'N') {
            await Swal.fire({
                title: 'Input Password',
                input: 'text',
                icon: 'info',
                showCancelButton: true
            }).then((result) => {
                const data = { "password" : result.value };
                dataJSON = JSON.stringify(data);
            });
        } else {
            const data = { "password" : "" };
            dataJSON = JSON.stringify(data);
        }
        //input 창에서 cancel 눌렀을 경우 함수 종료
        if (dataJSON === '{}') {
            return;
        }

        $.ajax({
            url : "/comment/" + id,
            method : "DELETE",
            contentType : "application/json; UTF-8",
            data : dataJSON,
            async : false,
            beforeSend : function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success : function(result) {
                location.reload();
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

$( 'button[name=putComment_submit]').click(function() {
        const id = $(this).data('id');
        const token = $( "meta[name= '_csrf']" ).attr('content');
        const header = $( "meta[name='_csrf_header']").attr("content");
        const password = $( '#putComment_password_' + id ).val();
        const content = $( '#putComment_content_' + id ).val();

        const data = { "password" : password, "content" : content };
        const dataJSON = JSON.stringify(data);
        $.ajax({
            url : "/comment/" + id,
            method : "PUT",
            contentType : "application/json; UTF-8",
            data : dataJSON,
            beforeSend : function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success : function(result) {
                location.reload();
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