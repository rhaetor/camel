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
package org.apache.camel.component.jacksonxml;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpringJacksonXmlDataFormatTest extends CamelSpringTestSupport {

    private static final String LS = System.lineSeparator();

    @Test
    public void testMarshalAndUnmarshalMap() throws Exception {
        Map<String, Object> in = new HashMap<>();
        in.put("name", "Camel");

        MockEndpoint mock = getMockEndpoint("mock:reverse");
        mock.expectedMessageCount(1);
        mock.message(0).body().isInstanceOf(Map.class);
        mock.message(0).body().isEqualTo(in);

        Object marshaled = template.requestBody("direct:in", in);
        String marshaledAsString = context.getTypeConverter().convertTo(String.class, marshaled);
        assertEquals("<HashMap><name>Camel</name></HashMap>", marshaledAsString);

        template.sendBody("direct:back", marshaled);

        mock.assertIsSatisfied();
    }

    @Test
    public void testMarshalAndUnmarshalMapWithPrettyPrint() throws Exception {
        Map<String, Object> in = new HashMap<>();
        in.put("name", "Camel");

        MockEndpoint mock = getMockEndpoint("mock:reverse");
        mock.expectedMessageCount(1);
        mock.message(0).body().isInstanceOf(Map.class);
        mock.message(0).body().isEqualTo(in);

        Object marshaled = template.requestBody("direct:inPretty", in);
        String marshaledAsString = context.getTypeConverter().convertTo(String.class, marshaled);
        String expected = "<HashMap>" + LS + "  <name>Camel</name>" + LS + "</HashMap>" + LS;
        assertEquals(expected, marshaledAsString);

        template.sendBody("direct:back", marshaled);

        mock.assertIsSatisfied();
    }

    @Test
    public void testMarshalAndUnmarshalPojo() throws Exception {
        TestPojo in = new TestPojo();
        in.setName("Camel");

        MockEndpoint mock = getMockEndpoint("mock:reversePojo");
        mock.expectedMessageCount(1);
        mock.message(0).body().isInstanceOf(TestPojo.class);
        mock.message(0).body().isEqualTo(in);

        Object marshaled = template.requestBody("direct:inPojo", in);
        String marshaledAsString = context.getTypeConverter().convertTo(String.class, marshaled);
        assertEquals("<TestPojo><name>Camel</name></TestPojo>", marshaledAsString);

        template.sendBody("direct:backPojo", marshaled);

        mock.assertIsSatisfied();
    }

    @Test
    public void testMarshalAndUnmarshalAgeView() throws Exception {
        TestPojoView in = new TestPojoView();

        MockEndpoint mock = getMockEndpoint("mock:reverseAgeView");
        mock.expectedMessageCount(1);
        mock.message(0).body().isInstanceOf(TestPojoView.class);
        mock.message(0).body().isEqualTo(in);

        Object marshaled = template.requestBody("direct:inAgeView", in);
        String marshaledAsString = context.getTypeConverter().convertTo(String.class, marshaled);
        // Weight is not written because it is not in the Age view
        assertEquals("<TestPojoView><age>30</age><height>190</height></TestPojoView>", marshaledAsString);

        template.sendBody("direct:backAgeView", marshaled);

        mock.assertIsSatisfied();
    }

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("org/apache/camel/component/jacksonxml/SpringJacksonXmlDataFormatTest.xml");
    }

}
