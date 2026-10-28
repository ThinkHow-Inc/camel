/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.webindex;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultProducer;

public class WebIndexProducer extends DefaultProducer {
    private final WebIndexEndpoint endpoint;

    public WebIndexProducer(WebIndexEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        if (endpoint.getServiceUrl() == null || endpoint.getServiceUrl().isBlank()) {
            throw new IllegalArgumentException("serviceUrl is required");
        }
        String body = exchange.getIn().getBody(String.class);
        String path = endpoint.getPath();
        String requestUrl = endpoint.getServiceUrl().replaceAll("/$", "") + "/"
                + URLEncoder.encode(path, StandardCharsets.UTF_8).replace("%2F", "/");
        HttpRequest request = HttpRequest.newBuilder(URI.create(requestUrl))
                .timeout(Duration.ofMillis(endpoint.getTimeout()))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body == null ? "{}" : body))
                .build();
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(endpoint.getTimeout())).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, response.statusCode());
        exchange.getMessage().setBody(response.body());
        if (response.statusCode() >= 400) {
            throw new IllegalStateException("web-index service returned HTTP " + response.statusCode());
        }
    }
}
