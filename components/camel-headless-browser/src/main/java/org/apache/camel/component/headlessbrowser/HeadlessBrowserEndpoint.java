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
package org.apache.camel.component.headlessbrowser;

import org.apache.camel.Category;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.support.DefaultEndpoint;

@UriEndpoint(firstVersion = "4.24.0", scheme = "headless-browser", title = "Headless Browser",
             syntax = "headless-browser://service/path", category = { Category.SEARCH })
public class HeadlessBrowserEndpoint extends DefaultEndpoint {
    private final String path;
    @UriParam
    private String serviceUrl;
    @UriParam(defaultValue = "10000")
    private int timeout = 10000;

    public HeadlessBrowserEndpoint(String uri, HeadlessBrowserComponent component, String path) {
        super(uri, component);
        this.path = path;
    }

    public String getPath() { return path; }
    public String getServiceUrl() { return serviceUrl; }
    public void setServiceUrl(String serviceUrl) { this.serviceUrl = serviceUrl; }
    public int getTimeout() { return timeout; }
    public void setTimeout(int timeout) { this.timeout = timeout; }

    @Override
    public Producer createProducer() throws Exception { return new HeadlessBrowserProducer(this); }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        throw new UnsupportedOperationException("headless-browser is producer only");
    }
}
