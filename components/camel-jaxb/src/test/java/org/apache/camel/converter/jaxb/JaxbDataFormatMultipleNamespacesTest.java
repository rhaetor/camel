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

import jakarta.xml.bind.JAXBContext;

import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.example.Address;
import org.apache.camel.example.Order;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JaxbDataFormatMultipleNamespacesTest extends CamelTestSupport {

    private static final Logger LOG = LoggerFactory.getLogger(JaxbDataFormatMultipleNamespacesTest.class);

    @EndpointInject("mock:marshal")
    private MockEndpoint mockMarshal;

    @EndpointInject("mock:unmarshal")
    private MockEndpoint mockUnmarshal;

    @Test
    public void testMarshalMultipleNamespaces() throws Exception {
        mockMarshal.expectedMessageCount(1);

        Order order = new Order();
        order.setId("1");
        Address address = new Address();
        address.setStreet("Main Street");
        address.setStreetNumber("3a");
        address.setZip("65843");
        address.setCity("Sulzbach");
        order.setAddress(address);
        template.sendBody("direct:marshal", order);

        MockEndpoint.assertIsSatisfied(context);

        String payload = mockMarshal.getExchanges().get(0).getIn().getBody(String.class);
        LOG.info(payload);

        assertTrue(payload.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"));
        assertTrue(payload.contains("<order:order"));
        assertTrue(payload.contains("<order:id>1</order:id>"));
        assertTrue(payload.contains("<address:address>"));
        assertTrue(payload.contains("<address:street>Main Street</address:street>"));
        assertTrue(payload.contains("<address:streetNumber>3a</address:streetNumber>"));
        assertTrue(payload.contains("<address:zip>65843</address:zip>"));
        assertTrue(payload.contains("<address:city>Sulzbach</address:city>"));
        assertTrue(payload.contains("</address:address>"));
        assertTrue(payload.contains("</order:order>"));

        // the namespaces
        assertTrue(payload.contains("xmlns:address=\"http://www.camel.apache.org/jaxb/example/address/1\""));
        assertTrue(payload.contains("xmlns:order=\"http://www.camel.apache.org/jaxb/example/order/1\""));
    }

    @Test
    public void testUnarshalMultipleNamespaces() throws Exception {
        mockUnmarshal.expectedMessageCount(1);

        String payload
                = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ns1:order xmlns:ns2=\"http://www.camel.apache.org/jaxb/example/address/1\""
                  + " xmlns:ns1=\"http://www.camel.apache.org/jaxb/example/order/1\"><ns1:id>1</ns1:id><ns2:address><ns2:street>Main Street</ns2:street>"
                  + "<ns2:streetNumber>3a</ns2:streetNumber><ns2:zip>65843</ns2:zip><ns2:city>Sulzbach</ns2:city></ns2:address></ns1:order>";
        template.sendBody("direct:unmarshal", payload);

        MockEndpoint.assertIsSatisfied(context);

        Order order = (Order) mockUnmarshal.getExchanges().get(0).getIn().getBody();
        Address address = order.getAddress();
        assertEquals("1", order.getId());
        assertEquals("Main Street", address.getStreet());
        assertEquals("3a", address.getStreetNumber());
        assertEquals("65843", address.getZip());
        assertEquals("Sulzbach", address.getCity());
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                JaxbDataFormat jaxbDataFormat = new JaxbDataFormat(JAXBContext.newInstance(Order.class, Address.class));

                from("direct:marshal")
                        .marshal(jaxbDataFormat)
                        .to("mock:marshal");

                from("direct:unmarshal")
                        .unmarshal(jaxbDataFormat)
                        .to("mock:unmarshal");
            }
        };
    }
}
