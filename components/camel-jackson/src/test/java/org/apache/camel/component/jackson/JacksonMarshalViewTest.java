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
package org.apache.camel.component.jackson;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JacksonMarshalViewTest extends CamelTestSupport {

    @Test
    public void testMarshalAndUnmarshalPojoWithView() throws Exception {

        TestPojoView in = new TestPojoView();

        MockEndpoint mock = getMockEndpoint("mock:reversePojoAgeView");
        mock.expectedMessageCount(1);
        mock.message(0).body().isInstanceOf(TestPojoView.class);
        mock.message(0).body().isEqualTo(in);

        Object marshaled = template.requestBody("direct:inPojoAgeView", in);
        String marshaledAsString = context.getTypeConverter().convertTo(String.class, marshaled);
        assertEquals("{\"age\":30,\"height\":190}", marshaledAsString);

        template.sendBody("direct:backPojoAgeView", marshaled);

        mock.assertIsSatisfied();
    }

    @Test
    public void testMarshalAndUnmarshalPojoWithAnotherView() throws Exception {

        TestPojoView in = new TestPojoView();

        MockEndpoint mock = getMockEndpoint("mock:reversePojoWeightView");
        mock.expectedMessageCount(1);
        mock.message(0).body().isInstanceOf(TestPojoView.class);
        mock.message(0).body().isEqualTo(in);

        Object marshaled = template.requestBody("direct:inPojoWeightView", in);
        String marshaledAsString = context.getTypeConverter().convertTo(String.class, marshaled);
        assertEquals("{\"height\":190,\"weight\":70}", marshaledAsString);

        template.sendBody("direct:backPojoWeightView", marshaled);

        mock.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {

            @Override
            public void configure() {

                // START SNIPPET: format
                from("direct:inPojoAgeView").marshal().json(TestPojoView.class, Views.Age.class);
                // END SNIPPET: format
                from("direct:backPojoAgeView").unmarshal().json(JsonLibrary.Jackson, TestPojoView.class)
                        .to("mock:reversePojoAgeView");

                from("direct:inPojoWeightView").marshal().json(TestPojoView.class, Views.Weight.class);
                from("direct:backPojoWeightView").unmarshal().json(JsonLibrary.Jackson, TestPojoView.class)
                        .to("mock:reversePojoWeightView");
            }
        };
    }

}
