(function(document, $) {
    "use strict";     
    $(document).on("dialog-ready", function(event) {
        $(document).on('click', '.translate-btn', function() {
            let inputText = $(".input-field").val();
            let locale = $(".locale").val();
            let outputTextField = $(".output-field");

            $.ajax({
                url: '/bin/copilotIntegration',
                method: 'GET',
                data: { prompt: inputText, locale: locale }
            })
            .done(function(response) {
                // Assuming response is a JSON object with a 'message' field
                try {
                    const parsedResponse = JSON.parse(response);
                    $(outputTextField).val(parsedResponse.message);
                } catch (e) {
                    console.error("Error parsing JSON: ", e);
                }
                alert('Success: ' + response);
            })
            .fail(function(response) {
                alert('Error occurred!');
            });
        });
    });
}(document, Granite.$));
