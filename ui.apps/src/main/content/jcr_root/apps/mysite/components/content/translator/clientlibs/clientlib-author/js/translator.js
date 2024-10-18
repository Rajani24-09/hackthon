(function(document, $) {
    "use strict";     
    $(document).on("dialog-ready", function(event) {
        $(document).on('click', '.translate-btn', function() {
            let inputText = $(".input-field").val();
            let locale = $(".locale").val();
            let outputTextField = $(".output-field");

            fetch(`/bin/copilotIntegration?prompt=${encodeURIComponent(inputText)}&locale=${encodeURIComponent(locale)}`, {
                method: 'GET',
            })
            .then(response => {
                if (!response.ok) {
                $(outputTextField).val('');
                    throw new Error('Network response was not ok');
                }
                return response.text(); // Or response.json() if the response is in JSON format
            })
            .then(data => {
                $(outputTextField).val(data);
            })
            .catch(error => {
            });
        });
    });
}(document, Granite.$));
