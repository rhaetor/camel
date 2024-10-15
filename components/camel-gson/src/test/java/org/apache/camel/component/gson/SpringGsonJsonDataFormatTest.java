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
package org.apache.camel.component.gson;

import java.text.SimpleDateFormat;

import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpringGsonJsonDataFormatTest extends CamelSpringTestSupport {

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
        assertEquals("{\"name\":\"Camel\"}", marshaledAsString);

        template.sendBody("direct:backPojo", marshaled);

        mock.assertIsSatisfied();
    }

    @Test
    public void testMarshalAndUnmarshalPojoWithPrettyPrint() throws Exception {
        TestPojo in = new TestPojo();
        in.setName("Camel");

        MockEndpoint mock = getMockEndpoint("mock:reversePojo");
        mock.expectedMessageCount(1);
        mock.message(0).body().isInstanceOf(TestPojo.class);
        mock.message(0).body().isEqualTo(in);

        Object marshaled = template.requestBody("direct:inPretty", in);
        String marshaledAsString = context.getTypeConverter().convertTo(String.class, marshaled);
        String expected = "{\n"
                          + "  \"name\": \"Camel\""
                          + "\n}";
        assertEquals(expected, marshaledAsString);

        template.sendBody("direct:backPretty", marshaled);

        mock.assertIsSatisfied();
    }

    @Test
    public void testMarshalAndUnmarshalPojoWithDateFormatPattern() throws Exception {
        TestPojo in = new TestPojo();
        in.setName("Camel");
        in.setDob(new SimpleDateFormat("yyyyMMdd").parse("20230204"));

        MockEndpoint mock = getMockEndpoint("mock:reversePojo");
        mock.expectedMessageCount(1);
        mock.message(0).body().isInstanceOf(TestPojo.class);
        mock.message(0).body().isEqualTo(in);

        Object marshaled = template.requestBody("direct:inFormatDate", in);
        String marshaledAsString = context.getTypeConverter().convertTo(String.class, marshaled);
        String expected = "{\"name\":\"Camel\",\"dob\":\"2023-02-04\"}";
        assertEquals(expected, marshaledAsString);

        template.sendBody("direct:backFormatDate", marshaled);

        mock.assertIsSatisfied();
    }

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("org/apache/camel/component/gson/SpringGsonJsonDataFormatTest.xml");
    }

}
