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
import org.apache.camel.component.seda.SedaComponent;

/**
 * Asynchronously call another endpoint from any Camel Context in the same JVM.
 * 
 * Generated by camel-package-maven-plugin - do not edit this file!
 */
@Generated("org.apache.camel.maven.packaging.ComponentDslMojo")
public interface SedaComponentBuilderFactory {

    /**
     * SEDA (camel-seda)
     * Asynchronously call another endpoint from any Camel Context in the same
     * JVM.
     * 
     * Category: core,messaging
     * Since: 1.1
     * Maven coordinates: org.apache.camel:camel-seda
     * 
     * @return the dsl builder
     */
    static SedaComponentBuilder seda() {
        return new SedaComponentBuilderImpl();
    }

    /**
     * Builder for the SEDA component.
     */
    interface SedaComponentBuilder extends ComponentBuilder<SedaComponent> {
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
        default SedaComponentBuilder bridgeErrorHandler(
                boolean bridgeErrorHandler) {
            doSetProperty("bridgeErrorHandler", bridgeErrorHandler);
            return this;
        }
        /**
         * Sets the default number of concurrent threads processing exchanges.
         * 
         * The option is a: &lt;code&gt;int&lt;/code&gt; type.
         * 
         * Default: 1
         * Group: consumer
         * 
         * @param concurrentConsumers the value to set
         * @return the dsl builder
         */
        default SedaComponentBuilder concurrentConsumers(int concurrentConsumers) {
            doSetProperty("concurrentConsumers", concurrentConsumers);
            return this;
        }
        /**
         * The timeout (in milliseconds) used when polling. When a timeout
         * occurs, the consumer can check whether it is allowed to continue
         * running. Setting a lower value allows the consumer to react more
         * quickly upon shutdown.
         * 
         * The option is a: &lt;code&gt;int&lt;/code&gt; type.
         * 
         * Default: 1000
         * Group: consumer (advanced)
         * 
         * @param defaultPollTimeout the value to set
         * @return the dsl builder
         */
        default SedaComponentBuilder defaultPollTimeout(int defaultPollTimeout) {
            doSetProperty("defaultPollTimeout", defaultPollTimeout);
            return this;
        }
        /**
         * Whether a thread that sends messages to a full SEDA queue will block
         * until the queue's capacity is no longer exhausted. By default, an
         * exception will be thrown stating that the queue is full. By enabling
         * this option, the calling thread will instead block and wait until the
         * message can be accepted.
         * 
         * The option is a: &lt;code&gt;boolean&lt;/code&gt; type.
         * 
         * Default: false
         * Group: producer
         * 
         * @param defaultBlockWhenFull the value to set
         * @return the dsl builder
         */
        default SedaComponentBuilder defaultBlockWhenFull(
                boolean defaultBlockWhenFull) {
            doSetProperty("defaultBlockWhenFull", defaultBlockWhenFull);
            return this;
        }
        /**
         * Whether a thread that sends messages to a full SEDA queue will be
         * discarded. By default, an exception will be thrown stating that the
         * queue is full. By enabling this option, the calling thread will give
         * up sending and continue, meaning that the message was not sent to the
         * SEDA queue.
         * 
         * The option is a: &lt;code&gt;boolean&lt;/code&gt; type.
         * 
         * Default: false
         * Group: producer
         * 
         * @param defaultDiscardWhenFull the value to set
         * @return the dsl builder
         */
        default SedaComponentBuilder defaultDiscardWhenFull(
                boolean defaultDiscardWhenFull) {
            doSetProperty("defaultDiscardWhenFull", defaultDiscardWhenFull);
            return this;
        }
        /**
         * Whether a thread that sends messages to a full SEDA queue will block
         * until the queue's capacity is no longer exhausted. By default, an
         * exception will be thrown stating that the queue is full. By enabling
         * this option, where a configured timeout can be added to the block
         * case. Utilizing the .offer(timeout) method of the underlining java
         * queue.
         * 
         * The option is a: &lt;code&gt;long&lt;/code&gt; type.
         * 
         * Group: producer
         * 
         * @param defaultOfferTimeout the value to set
         * @return the dsl builder
         */
        default SedaComponentBuilder defaultOfferTimeout(
                long defaultOfferTimeout) {
            doSetProperty("defaultOfferTimeout", defaultOfferTimeout);
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
        default SedaComponentBuilder lazyStartProducer(boolean lazyStartProducer) {
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
        default SedaComponentBuilder autowiredEnabled(boolean autowiredEnabled) {
            doSetProperty("autowiredEnabled", autowiredEnabled);
            return this;
        }
        /**
         * Sets the default queue factory.
         * 
         * The option is a:
         * &lt;code&gt;org.apache.camel.component.seda.BlockingQueueFactory&amp;lt;org.apache.camel.Exchange&amp;gt;&lt;/code&gt; type.
         * 
         * Group: advanced
         * 
         * @param defaultQueueFactory the value to set
         * @return the dsl builder
         */
        default SedaComponentBuilder defaultQueueFactory(
                org.apache.camel.component.seda.BlockingQueueFactory<org.apache.camel.Exchange> defaultQueueFactory) {
            doSetProperty("defaultQueueFactory", defaultQueueFactory);
            return this;
        }
        /**
         * Sets the default maximum capacity of the SEDA queue (that is, the number
         * of messages it can hold).
         * 
         * The option is a: &lt;code&gt;int&lt;/code&gt; type.
         * 
         * Default: 1000
         * Group: advanced
         * 
         * @param queueSize the value to set
         * @return the dsl builder
         */
        default SedaComponentBuilder queueSize(int queueSize) {
            doSetProperty("queueSize", queueSize);
            return this;
        }
    }

    class SedaComponentBuilderImpl
            extends
                AbstractComponentBuilder<SedaComponent>
            implements
                SedaComponentBuilder {
        @Override
        protected SedaComponent buildConcreteComponent() {
            return new SedaComponent();
        }
        @Override
        protected boolean setPropertyOnComponent(
                Component component,
                String name,
                Object value) {
            switch (name) {
            case "bridgeErrorHandler": ((SedaComponent) component).setBridgeErrorHandler((boolean) value); return true;
            case "concurrentConsumers": ((SedaComponent) component).setConcurrentConsumers((int) value); return true;
            case "defaultPollTimeout": ((SedaComponent) component).setDefaultPollTimeout((int) value); return true;
            case "defaultBlockWhenFull": ((SedaComponent) component).setDefaultBlockWhenFull((boolean) value); return true;
            case "defaultDiscardWhenFull": ((SedaComponent) component).setDefaultDiscardWhenFull((boolean) value); return true;
            case "defaultOfferTimeout": ((SedaComponent) component).setDefaultOfferTimeout((long) value); return true;
            case "lazyStartProducer": ((SedaComponent) component).setLazyStartProducer((boolean) value); return true;
            case "autowiredEnabled": ((SedaComponent) component).setAutowiredEnabled((boolean) value); return true;
            case "defaultQueueFactory": ((SedaComponent) component).setDefaultQueueFactory((org.apache.camel.component.seda.BlockingQueueFactory) value); return true;
            case "queueSize": ((SedaComponent) component).setQueueSize((int) value); return true;
            default: return false;
            }
        }
    }
}