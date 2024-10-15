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
package org.apache.camel.dataformat.bindy.fixed.headerfooter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.dataformat.bindy.fixed.BindyFixedLengthDataFormat;
import org.apache.camel.model.dataformat.BindyDataFormat;
import org.apache.camel.model.dataformat.BindyType;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This test validates that header and footer records are successfully marshaled / unmarshaled in conjunction with the
 * primary data records defined for the bindy data format.
 */
public class BindySimpleFixedLengthHeaderFooterTest extends CamelTestSupport {

    public static final String URI_DIRECT_MARSHALL = "direct:marshal";
    public static final String URI_DIRECT_UNMARSHALL = "direct:unmarshal";
    public static final String URI_MOCK_MARSHALL_RESULT = "mock:marshal-result";
    public static final String URI_MOCK_UNMARSHALL_RESULT = "mock:unmarshal-result";

    private static final String TEST_HEADER = "101-08-2009\r\n";
    private static final String TEST_RECORD = "10A9  PaulineM    ISINXD12345678BUYShare000002500.45USD01-08-2009\r\n";
    private static final String TEST_FOOTER = "9000000001\r\n";

    @EndpointInject(URI_MOCK_MARSHALL_RESULT)
    private MockEndpoint marshalResult;

    @EndpointInject(URI_MOCK_UNMARSHALL_RESULT)
    private MockEndpoint unmarshalResult;

    // *************************************************************************
    // TESTS
    // *************************************************************************

    @SuppressWarnings("unchecked")
    @Test
    public void testUnmarshalMessage() throws Exception {

        StringBuilder sb = new StringBuilder();
        sb.append(TEST_HEADER).append(TEST_RECORD).append(TEST_FOOTER);

        unmarshalResult.expectedMessageCount(1);

        template.sendBody(URI_DIRECT_UNMARSHALL, sb.toString());

        unmarshalResult.assertIsSatisfied();

        // check the model
        Exchange exchange = unmarshalResult.getReceivedExchanges().get(0);
        Order order = (Order) exchange.getIn().getBody();
        assertEquals(10, order.getOrderNr());
        // the field is not trimmed
        assertEquals("  Pauline", order.getFirstName());
        assertEquals("M    ", order.getLastName());

        Map<String, Object> receivedHeaderMap
                = (Map<String, Object>) exchange.getIn().getHeader(BindyFixedLengthDataFormat.CAMEL_BINDY_FIXED_LENGTH_HEADER);

        Map<String, Object> receivedFooterMap
                = (Map<String, Object>) exchange.getIn().getHeader(BindyFixedLengthDataFormat.CAMEL_BINDY_FIXED_LENGTH_FOOTER);

        assertNotNull(receivedHeaderMap);
        assertNotNull(receivedFooterMap);

        OrderHeader receivedHeader = (OrderHeader) receivedHeaderMap.get(OrderHeader.class.getName());
        OrderFooter receivedFooter = (OrderFooter) receivedFooterMap.get(OrderFooter.class.getName());

        assertNotNull(receivedHeader);
        assertNotNull(receivedFooter);

        OrderHeader expectedHeader = new OrderHeader();
        Calendar calendar = new GregorianCalendar();
        calendar.set(2009, 7, 1, 0, 0, 0);
        calendar.clear(Calendar.MILLISECOND);
        expectedHeader.setRecordDate(calendar.getTime());

        assertEquals(receivedHeader.getRecordType(), expectedHeader.getRecordType());
        assertEquals(expectedHeader.getRecordDate(), receivedHeader.getRecordDate());
    }

    /**
     * Verifies that header & footer provided as part of message body are marshaled successfully
     */
    @Test
    public void testMarshalMessageWithDirectHeaderAndFooterInput() throws Exception {
        Order order = new Order();
        order.setOrderNr(10);
        order.setOrderType("BUY");
        order.setClientNr("A9");
        order.setFirstName("Pauline");
        order.setLastName("M");
        order.setAmount(new BigDecimal("2500.45"));
        order.setInstrumentCode("ISIN");
        order.setInstrumentNumber("XD12345678");
        order.setInstrumentType("Share");
        order.setCurrency("USD");
        Calendar calendar = new GregorianCalendar();
        calendar.set(2009, 7, 1, 0, 0, 0);
        order.setOrderDate(calendar.getTime());

        List<Map<String, Object>> input = new ArrayList<>();
        Map<String, Object> bodyRow = new HashMap<>();
        bodyRow.put(Order.class.getName(), order);
        input.add(createHeaderRow());
        input.add(bodyRow);
        input.add(createFooterRow());

        marshalResult.expectedMessageCount(1);
        StringBuilder sb = new StringBuilder();
        sb.append(TEST_HEADER).append(TEST_RECORD).append(TEST_FOOTER);
        marshalResult.expectedBodiesReceived(Arrays.asList(new String[] { sb.toString() }));
        template.sendBody(URI_DIRECT_MARSHALL, input);
        marshalResult.assertIsSatisfied();
    }

    /**
     * Verifies that header & footer provided via message headers are marshaled successfully
     */
    @Test
    public void testMarshalMessageWithIndirectHeaderAndFooterInput() throws Exception {
        Order order = new Order();
        order.setOrderNr(10);
        order.setOrderType("BUY");
        order.setClientNr("A9");
        order.setFirstName("Pauline");
        order.setLastName("M");
        order.setAmount(new BigDecimal("2500.45"));
        order.setInstrumentCode("ISIN");
        order.setInstrumentNumber("XD12345678");
        order.setInstrumentType("Share");
        order.setCurrency("USD");
        Calendar calendar = new GregorianCalendar();
        calendar.set(2009, 7, 1, 0, 0, 0);
        order.setOrderDate(calendar.getTime());

        List<Map<String, Object>> input = new ArrayList<>();
        Map<String, Object> bodyRow = new HashMap<>();
        bodyRow.put(Order.class.getName(), order);

        input.add(bodyRow);

        Map<String, Object> headers = new HashMap<>();
        headers.put(BindyFixedLengthDataFormat.CAMEL_BINDY_FIXED_LENGTH_HEADER, createHeaderRow());
        headers.put(BindyFixedLengthDataFormat.CAMEL_BINDY_FIXED_LENGTH_FOOTER, createFooterRow());

        marshalResult.reset();
        marshalResult.expectedMessageCount(1);
        StringBuilder sb = new StringBuilder();
        sb.append(TEST_HEADER).append(TEST_RECORD).append(TEST_FOOTER);
        marshalResult.expectedBodiesReceived(Arrays.asList(new String[] { sb.toString() }));
        template.sendBodyAndHeaders(URI_DIRECT_MARSHALL, input, headers);
        marshalResult.assertIsSatisfied();
    }

    private Map<String, Object> createHeaderRow() {
        Map<String, Object> headerMap = new HashMap<>();
        OrderHeader header = new OrderHeader();
        Calendar calendar = new GregorianCalendar();
        calendar.set(2009, 7, 1, 0, 0, 0);
        header.setRecordDate(calendar.getTime());
        headerMap.put(OrderHeader.class.getName(), header);
        return headerMap;
    }

    private Map<String, Object> createFooterRow() {
        Map<String, Object> footerMap = new HashMap<>();
        OrderFooter footer = new OrderFooter();
        footer.setNumberOfRecordsInTheFile(1);
        footerMap.put(OrderFooter.class.getName(), footer);
        return footerMap;
    }

    // *************************************************************************
    // ROUTES
    // *************************************************************************

    @Override
    protected RouteBuilder createRouteBuilder() {
        RouteBuilder routeBuilder = new RouteBuilder() {

            @Override
            public void configure() {
                BindyDataFormat bindy = new BindyDataFormat()
                        .classType(Order.class)
                        .locale("en")
                        .type(BindyType.Fixed);

                from(URI_DIRECT_MARSHALL)
                        .marshal(bindy)
                        .to(URI_MOCK_MARSHALL_RESULT);

                from(URI_DIRECT_UNMARSHALL)
                        .unmarshal().bindy(BindyType.Fixed, Order.class)
                        .to(URI_MOCK_UNMARSHALL_RESULT);
            }
        };

        return routeBuilder;
    }
}
