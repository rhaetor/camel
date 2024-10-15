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
package org.apache.camel.builder.component.dsl;

import javax.annotation.processing.Generated;
import org.apache.camel.Component;
import org.apache.camel.builder.component.AbstractComponentBuilder;
import org.apache.camel.builder.component.ComponentBuilder;
import org.apache.camel.component.olingo2.Olingo2Component;

/**
 * Communicate with OData 2.0 services using Apache Olingo.
 * 
 * Generated by camel-package-maven-plugin - do not edit this file!
 */
@Generated("org.apache.camel.maven.packaging.ComponentDslMojo")
public interface Olingo2ComponentBuilderFactory {

    /**
     * Olingo2 (camel-olingo2)
     * Communicate with OData 2.0 services using Apache Olingo.
     * 
     * Category: cloud
     * Since: 2.14
     * Maven coordinates: org.apache.camel:camel-olingo2
     * 
     * @return the dsl builder
     */
    static Olingo2ComponentBuilder olingo2() {
        return new Olingo2ComponentBuilderImpl();
    }

    /**
     * Builder for the Olingo2 component.
     */
    interface Olingo2ComponentBuilder
            extends
                ComponentBuilder<Olingo2Component> {
        /**
         * To use the shared configuration.
         * 
         * The option is a:
         * &lt;code&gt;org.apache.camel.component.olingo2.Olingo2Configuration&lt;/code&gt; type.
         * 
         * Group: common
         * 
         * @param configuration the value to set
         * @return the dsl builder
         */
        default Olingo2ComponentBuilder configuration(
                org.apache.camel.component.olingo2.Olingo2Configuration configuration) {
            doSetProperty("configuration", configuration);
            return this;
        }
        /**
         * HTTP connection creation timeout in milliseconds, defaults to 30,000
         * (30 seconds).
         * 
         * The option is a: &lt;code&gt;int&lt;/code&gt; type.
         * 
         * Default: 30000
         * Group: common
         * 
         * @param connectTimeout the value to set
         * @return the dsl builder
         */
        default Olingo2ComponentBuilder connectTimeout(int connectTimeout) {
            doSetProperty("connectTimeout", connectTimeout);
            return this;
        }
        /**
         * Content-Type header value can be used to specify JSON or XML message
         * format, defaults to application/json;charset=utf-8.
         * 
         * The option is a: &lt;code&gt;java.lang.String&lt;/code&gt; type.
         * 
         * Default: application/json;charset=utf-8
         * Group: common
         * 
         * @param contentType the value to set
         * @return the dsl builder
         */
        default Olingo2ComponentBuilder contentType(java.lang.String contentType) {
            doSetProperty("contentType", contentType);
            return this;
        }
        /**
         * Custom entity provider read properties applied to all read
         * operations.
         * 
         * The option is a:
         * &lt;code&gt;org.apache.olingo.odata2.api.ep.EntityProviderReadProperties&lt;/code&gt; type.
         * 
         * Group: common
         * 
         * @param entityProviderReadProperties the value to set
         * @return the dsl builder
         */
        default Olingo2ComponentBuilder entityProviderReadProperties(
                org.apache.olingo.odata2.api.ep.EntityProviderReadProperties entityProviderReadProperties) {
            doSetProperty("entityProviderReadProperties", entityProviderReadProperties);
            return this;
        }
        /**
         * Custom entity provider write properties applied to create, update,
         * patch, batch and merge operations. For instance users can skip the
         * Json object wrapper or enable content only mode when sending request
         * data. A service URI set in the properties will always be overwritten
         * by the serviceUri configuration parameter. Please consider to using
         * the serviceUri configuration parameter instead of setting the
         * respective write property here.
         * 
         * The option is a:
         * &lt;code&gt;org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties&lt;/code&gt; type.
         * 
         * Group: common
         * 
         * @param entityProviderWriteProperties the value to set
         * @return the dsl builder
         */
        default Olingo2ComponentBuilder entityProviderWriteProperties(
                org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties entityProviderWriteProperties) {
            doSetProperty("entityProviderWriteProperties", entityProviderWriteProperties);
            return this;
        }
        /**
         * Set this to true to filter out results that have already been
         * communicated by this component.
         * 
         * The option is a: &lt;code&gt;boolean&lt;/code&gt; type.
         * 
         * Default: false
         * Group: common
         * 
         * @param filterAlreadySeen the value to set
         * @return the dsl builder
         */
        default Olingo2ComponentBuilder filterAlreadySeen(
                boolean filterAlreadySeen) {
            doSetProperty("filterAlreadySeen", filterAlreadySeen);
            return this;
        }
        /**
         * Custom HTTP headers to inject into every request, this could include
         * OAuth tokens, etc.
         * 
         * The option is a: &lt;code&gt;java.util.Map&amp;lt;java.lang.String,
         * java.lang.String&amp;gt;&lt;/code&gt; type.
         * 
         * Group: common
         * 
         * @param httpHeaders the value to set
         * @return the dsl builder
         */
        default Olingo2ComponentBuilder httpHeaders(
                java.util.Map<java.lang.String, java.lang.String> httpHeaders) {
            doSetProperty("httpHeaders", httpHeaders);
            return this;
        }
        /**
         * HTTP proxy server configuration.
         * 
         * The option is a: &lt;code&gt;org.apache.http.HttpHost&lt;/code&gt;
         * type.
         * 
         * Group: common
         * 
         * @param proxy the value to set
         * @return the dsl builder
         */
        default Olingo2ComponentBuilder proxy(org.apache.http.HttpHost proxy) {
            doSetProperty("proxy", proxy);
            return this;
        }
        /**
         * Target OData service base URI, for example
         * http://services.odata.org/OData/OData.svc.
         * 
         * The option is a: &lt;code&gt;java.lang.String&lt;/code&gt; type.
         * 
         * Group: common
         * 
         * @param serviceUri the value to set
         * @return the dsl builder
         */
        default Olingo2ComponentBuilder serviceUri(java.lang.String serviceUri) {
            doSetProperty("serviceUri", serviceUri);
            return this;
        }
        /**
         * HTTP request timeout in milliseconds, defaults to 30,000 (30
         * seconds).
         * 
         * The option is a: &lt;code&gt;int&lt;/code&gt; type.
         * 
         * Default: 30000
         * Group: common
         * 
         * @param socketTimeout the value to set
         * @return the dsl builder
         */
        default Olingo2ComponentBuilder socketTimeout(int socketTimeout) {
            doSetProperty("socketTimeout", socketTimeout);
            return this;
        }
        /**
         * Allows for bridging the consumer to the Camel routing Error Handler,
         * which mean any exceptions (if possible) occurred while the Camel
         * consumer is trying to pickup incoming messages, or the likes, will
         * now be processed as a message and handled by the routing Error
         * Handler. Important: This is only possible if the 3rd party component
         * allows Camel to be alerted if an exception was thrown. Some
         * components handle this internally only, and therefore
         * bridgeErrorHandler is not possible. In other situations we may
         * improve the Camel component to hook into the 3rd party component and
         * make this possible for future releases. By default the consumer will
         * use the org.apache.camel.spi.ExceptionHandler to deal with
         * exceptions, that will be logged at WARN or ERROR level and ignored.
         * 
         * The option is a: &lt;code&gt;boolean&lt;/code&gt; type.
         * 
         * Default: false
         * Group: consumer
         * 
         * @param bridgeErrorHandler the value to set
         * @return the dsl builder
         */
        default Olingo2ComponentBuilder bridgeErrorHandler(
                boolean bridgeErrorHandler) {
            doSetProperty("bridgeErrorHandler", bridgeErrorHandler);
            return this;
        }
        /**
         * For endpoints that return an array or collection, a consumer endpoint
         * will map every element to distinct messages, unless splitResult is
         * set to false.
         * 
         * The option is a: &lt;code&gt;boolean&lt;/code&gt; type.
         * 
         * Default: true
         * Group: consumer
         * 
         * @param splitResult the value to set
         * @return the dsl builder
         */
        default Olingo2ComponentBuilder splitResult(boolean splitResult) {
            doSetProperty("splitResult", splitResult);
            return this;
        }
        /**
         * Whether the producer should be started lazy (on the first message).
         * By starting lazy you can use this to allow CamelContext and routes to
         * startup in situations where a producer may otherwise fail during
         * starting and cause the route to fail being started. By deferring this
         * startup to be lazy then the startup failure can be handled during
         * routing messages via Camel's routing error handlers. Beware that when
         * the first message is processed then creating and starting the
         * producer may take a little time and prolong the total processing time
         * of the processing.
         * 
         * The option is a: &lt;code&gt;boolean&lt;/code&gt; type.
         * 
         * Default: false
         * Group: producer
         * 
         * @param lazyStartProducer the value to set
         * @return the dsl builder
         */
        default Olingo2ComponentBuilder lazyStartProducer(
                boolean lazyStartProducer) {
            doSetProperty("lazyStartProducer", lazyStartProducer);
            return this;
        }
        /**
         * Whether autowiring is enabled. This is used for automatic autowiring
         * options (the option must be marked as autowired) by looking up in the
         * registry to find if there is a single instance of matching type,
         * which then gets configured on the component. This can be used for
         * automatic configuring JDBC data sources, JMS connection factories,
         * AWS Clients, etc.
         * 
         * The option is a: &lt;code&gt;boolean&lt;/code&gt; type.
         * 
         * Default: true
         * Group: advanced
         * 
         * @param autowiredEnabled the value to set
         * @return the dsl builder
         */
        default Olingo2ComponentBuilder autowiredEnabled(
                boolean autowiredEnabled) {
            doSetProperty("autowiredEnabled", autowiredEnabled);
            return this;
        }
        /**
         * Custom HTTP async client builder for more complex HTTP client
         * configuration, overrides connectionTimeout, socketTimeout, proxy and
         * sslContext. Note that a socketTimeout MUST be specified in the
         * builder, otherwise OData requests could block indefinitely.
         * 
         * The option is a:
         * &lt;code&gt;org.apache.http.impl.nio.client.HttpAsyncClientBuilder&lt;/code&gt; type.
         * 
         * Group: advanced
         * 
         * @param httpAsyncClientBuilder the value to set
         * @return the dsl builder
         */
        default Olingo2ComponentBuilder httpAsyncClientBuilder(
                org.apache.http.impl.nio.client.HttpAsyncClientBuilder httpAsyncClientBuilder) {
            doSetProperty("httpAsyncClientBuilder", httpAsyncClientBuilder);
            return this;
        }
        /**
         * Custom HTTP client builder for more complex HTTP client
         * configuration, overrides connectionTimeout, socketTimeout, proxy and
         * sslContext. Note that a socketTimeout MUST be specified in the
         * builder, otherwise OData requests could block indefinitely.
         * 
         * The option is a:
         * &lt;code&gt;org.apache.http.impl.client.HttpClientBuilder&lt;/code&gt; type.
         * 
         * Group: advanced
         * 
         * @param httpClientBuilder the value to set
         * @return the dsl builder
         */
        default Olingo2ComponentBuilder httpClientBuilder(
                org.apache.http.impl.client.HttpClientBuilder httpClientBuilder) {
            doSetProperty("httpClientBuilder", httpClientBuilder);
            return this;
        }
        /**
         * To configure security using SSLContextParameters.
         * 
         * The option is a:
         * &lt;code&gt;org.apache.camel.support.jsse.SSLContextParameters&lt;/code&gt; type.
         * 
         * Group: security
         * 
         * @param sslContextParameters the value to set
         * @return the dsl builder
         */
        default Olingo2ComponentBuilder sslContextParameters(
                org.apache.camel.support.jsse.SSLContextParameters sslContextParameters) {
            doSetProperty("sslContextParameters", sslContextParameters);
            return this;
        }
        /**
         * Enable usage of global SSL context parameters.
         * 
         * The option is a: &lt;code&gt;boolean&lt;/code&gt; type.
         * 
         * Default: false
         * Group: security
         * 
         * @param useGlobalSslContextParameters the value to set
         * @return the dsl builder
         */
        default Olingo2ComponentBuilder useGlobalSslContextParameters(
                boolean useGlobalSslContextParameters) {
            doSetProperty("useGlobalSslContextParameters", useGlobalSslContextParameters);
            return this;
        }
    }

    class Olingo2ComponentBuilderImpl
            extends
                AbstractComponentBuilder<Olingo2Component>
            implements
                Olingo2ComponentBuilder {
        @Override
        protected Olingo2Component buildConcreteComponent() {
            return new Olingo2Component();
        }
        private org.apache.camel.component.olingo2.Olingo2Configuration getOrCreateConfiguration(
                org.apache.camel.component.olingo2.Olingo2Component component) {
            if (component.getConfiguration() == null) {
                component.setConfiguration(new org.apache.camel.component.olingo2.Olingo2Configuration());
            }
            return component.getConfiguration();
        }
        @Override
        protected boolean setPropertyOnComponent(
                Component component,
                String name,
                Object value) {
            switch (name) {
            case "configuration": ((Olingo2Component) component).setConfiguration((org.apache.camel.component.olingo2.Olingo2Configuration) value); return true;
            case "connectTimeout": getOrCreateConfiguration((Olingo2Component) component).setConnectTimeout((int) value); return true;
            case "contentType": getOrCreateConfiguration((Olingo2Component) component).setContentType((java.lang.String) value); return true;
            case "entityProviderReadProperties": getOrCreateConfiguration((Olingo2Component) component).setEntityProviderReadProperties((org.apache.olingo.odata2.api.ep.EntityProviderReadProperties) value); return true;
            case "entityProviderWriteProperties": getOrCreateConfiguration((Olingo2Component) component).setEntityProviderWriteProperties((org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties) value); return true;
            case "filterAlreadySeen": getOrCreateConfiguration((Olingo2Component) component).setFilterAlreadySeen((boolean) value); return true;
            case "httpHeaders": getOrCreateConfiguration((Olingo2Component) component).setHttpHeaders((java.util.Map) value); return true;
            case "proxy": getOrCreateConfiguration((Olingo2Component) component).setProxy((org.apache.http.HttpHost) value); return true;
            case "serviceUri": getOrCreateConfiguration((Olingo2Component) component).setServiceUri((java.lang.String) value); return true;
            case "socketTimeout": getOrCreateConfiguration((Olingo2Component) component).setSocketTimeout((int) value); return true;
            case "bridgeErrorHandler": ((Olingo2Component) component).setBridgeErrorHandler((boolean) value); return true;
            case "splitResult": getOrCreateConfiguration((Olingo2Component) component).setSplitResult((boolean) value); return true;
            case "lazyStartProducer": ((Olingo2Component) component).setLazyStartProducer((boolean) value); return true;
            case "autowiredEnabled": ((Olingo2Component) component).setAutowiredEnabled((boolean) value); return true;
            case "httpAsyncClientBuilder": getOrCreateConfiguration((Olingo2Component) component).setHttpAsyncClientBuilder((org.apache.http.impl.nio.client.HttpAsyncClientBuilder) value); return true;
            case "httpClientBuilder": getOrCreateConfiguration((Olingo2Component) component).setHttpClientBuilder((org.apache.http.impl.client.HttpClientBuilder) value); return true;
            case "sslContextParameters": getOrCreateConfiguration((Olingo2Component) component).setSslContextParameters((org.apache.camel.support.jsse.SSLContextParameters) value); return true;
            case "useGlobalSslContextParameters": ((Olingo2Component) component).setUseGlobalSslContextParameters((boolean) value); return true;
            default: return false;
            }
        }
    }
}