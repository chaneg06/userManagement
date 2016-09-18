$(document).ready(function() {
    $('#btnFilter').click(function() {

        var isActive = $.trim($('#filter').val());
        
        $('tr').show();
    
        $('tr td.isActive').each(function() {
            if (isActive == "all")
            {
                $(this).parent().show();
            }
            else if ($.trim($(this).text()) != isActive)
            {
                $(this).parent().hide();
            }
        });
    
    });
});
