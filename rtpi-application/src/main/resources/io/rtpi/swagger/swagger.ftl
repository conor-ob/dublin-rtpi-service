<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Dublin RTPI Service</title>
    <link rel="stylesheet" type="text/css" href="//unpkg.com/swagger-ui-dist@3/swagger-ui.css">
</head>
<body>

<div id="swagger-ui"></div>

<script src="//unpkg.com/swagger-ui-dist@3/swagger-ui-bundle.js"></script>
<script src="//unpkg.com/swagger-ui-dist@3/swagger-ui-standalone-preset.js"></script>
<script type="text/javascript">
    SwaggerUIBundle({
        url: 'swagger.yaml',
        dom_id: '#swagger-ui',
        presets: [
            SwaggerUIBundle.presets.apis,
            SwaggerUIStandalonePreset
        ],
        layout: 'StandaloneLayout'
    });
</script>

</body>
</html>
