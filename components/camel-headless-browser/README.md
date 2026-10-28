# Headless Browser component

Producer endpoint: `headless-browser://service/path?serviceUrl=https://example.invalid/api`.
The exchange body is sent as JSON to the configured service and its response becomes the exchange body.
The service URL must be supplied by the application; no provider or tenant is built in.
`timeout` sets the request and connection timeout in milliseconds (default: 10000).
The component is producer only and propagates HTTP errors as exchange exceptions.
