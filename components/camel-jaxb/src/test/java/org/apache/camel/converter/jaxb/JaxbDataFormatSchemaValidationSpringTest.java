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
package org.apache.camel.converter.jaxb;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;

import org.xml.sax.InputSource;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.EndpointInject;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.converter.jaxb.address.Address;
import org.apache.camel.converter.jaxb.message.Message;
import org.apache.camel.converter.jaxb.message.ObjectFactory;
import org.apache.camel.converter.jaxb.person.Person;
import org.apache.camel.test.spring.junit5.CamelSpringTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.apache.camel.test.junit5.TestSupport.assertIsInstanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JaxbDataFormatSchemaValidationSpringTest extends CamelSpringTestSupport {

    private static final Logger LOG = LoggerFactory.getLogger(JaxbDataFormatSchemaValidationSpringTest.class);

    @EndpointInject("mock:marshal")
    private MockEndpoint mockMarshal;

    @EndpointInject("mock:unmarshal")
    private MockEndpoint mockUnmarshal;

    private JAXBContext jbCtx;

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        jbCtx = JAXBContext.newInstance(Person.class, Message.class);
    }

    @Test
    public void testMarshalSuccess() throws Exception {
        mockMarshal.expectedMessageCount(1);

        Address address = new Address();
        address.setAddressLine1("Hauptstr. 1; 01129 Entenhausen");
        Person person = new Person();
        person.setFirstName("Christian");
        person.setLastName("Mueller");
        person.setAge(Integer.valueOf(36));
        person.setAddress(address);

        template.sendBody("direct:marshal", person);

        MockEndpoint.assertIsSatisfied(context);

        String payload = mockMarshal.getExchanges().get(0).getIn().getBody(String.class);
        LOG.info(payload);

        Person unmarshaledPerson = (Person) jbCtx.createUnmarshaler().unmarshal(new InputSource(new StringReader(payload)));

        assertNotNull(unmarshaledPerson);
        assertEquals(person.getFirstName(), unmarshaledPerson.getFirstName());
        assertEquals(person.getLastName(), unmarshaledPerson.getLastName());
        assertEquals(person.getAge(), unmarshaledPerson.getAge());
        assertNotNull(unmarshaledPerson.getAddress());
        assertEquals(person.getAddress().getAddressLine1(), unmarshaledPerson.getAddress().getAddressLine1());
    }

    @Test
    public void testMarshalWithValidationException() {
        Person person = new Person();

        Exception ex = assertThrows(CamelExecutionException.class,
                () -> template.sendBody("direct:marshal", person));

        Throwable cause = ex.getCause();
        assertIsInstanceOf(IOException.class, cause);
        assertTrue(cause.getMessage().contains("jakarta.xml.bind.MarshalException"));
        assertTrue(cause.getMessage().contains("org.xml.sax.SAXParseException"));
        assertTrue(cause.getMessage().contains("cvc-complex-type.2.4.a"));
    }

    @Test
    public void testUnmarshalSuccess() throws Exception {
        mockUnmarshal.expectedMessageCount(1);

        String xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>")
                .append("<person xmlns=\"person.jaxb.converter.camel.apache.org\" xmlns:ns2=\"address.jaxb.converter.camel.apache.org\">")
                .append("<firstName>Christian</firstName>")
                .append("<lastName>Mueller</lastName>")
                .append("<age>36</age>")
                .append("<address>")
                .append("<ns2:addressLine1>Hauptstr. 1; 01129 Entenhausen</ns2:addressLine1>")
                .append("</address>")
                .append("</person>")
                .toString();
        template.sendBody("direct:unmarshal", xml);

        MockEndpoint.assertIsSatisfied(context);

        Person person = mockUnmarshal.getExchanges().get(0).getIn().getBody(Person.class);

        assertEquals("Christian", person.getFirstName());
        assertEquals("Mueller", person.getLastName());
        assertEquals(Integer.valueOf(36), person.getAge());
    }

    @Test
    public void testUnmarshalWithValidationException() {
        String xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>")
                .append("<person xmlns=\"person.jaxb.converter.camel.apache.org\" />")
                .toString();

        Exception ex = assertThrows(CamelExecutionException.class,
                () -> template.sendBody("direct:unmarshal", xml));

        Throwable cause = ex.getCause();
        assertIsInstanceOf(IOException.class, cause);
        assertTrue(cause.getMessage().contains("jakarta.xml.bind.UnmarshalException"));
        assertTrue(cause.getMessage().contains("org.xml.sax.SAXParseException"));
        assertTrue(cause.getMessage().contains("cvc-complex-type.2.4.b"));
    }

    @Test
    public void testMarshalOfNonRootElementWithValidationException() {
        Message message = new Message();
        Exception ex = assertThrows(CamelExecutionException.class,
                () -> template.sendBody("direct:marshal", message));

        Throwable cause = ex.getCause();
        assertIsInstanceOf(IOException.class, cause);
        assertTrue(cause.getMessage().contains("jakarta.xml.bind.MarshalException"));
        assertTrue(cause.getMessage().contains("org.xml.sax.SAXParseException"));
        assertTrue(cause.getMessage().contains("cvc-complex-type.2.4.b"));
    }

    @Test
    public void testUnmarshalOfNonRootWithValidationException() throws Exception {
        JAXBElement<Message> message = new ObjectFactory().createMessage(new Message());

        String xml;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            jbCtx.createMarshaler().marshal(message, baos);
            xml = new String(baos.toByteArray(), "UTF-8");
        }

        Exception ex = assertThrows(CamelExecutionException.class,
                () -> template.sendBody("direct:unmarshal", xml));

        Throwable cause = ex.getCause();
        assertIsInstanceOf(IOException.class, cause);
        assertTrue(cause.getMessage().contains("jakarta.xml.bind.UnmarshalException"));
        assertTrue(cause.getMessage().contains("org.xml.sax.SAXParseException"));
        assertTrue(cause.getMessage().contains("cvc-complex-type.2.4.b"));
    }

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("org/apache/camel/converter/jaxb/context.xml");
    }
}
