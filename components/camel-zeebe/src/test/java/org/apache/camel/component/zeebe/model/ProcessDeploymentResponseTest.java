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

package org.apache.camel.component.zeebe.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProcessDeploymentResponseTest {

    private static final String MARSHAL_TEST_RESULT_1
            = "{\"success\":true,\"version\":2,\"bpmn_process_id\":\"testProcess\",\"process_definition_key\":111,\"resource_name\":\"testProcess.bpmn\"}";
    private static final String MARSHAL_TEST_RESULT_2
            = "{\"success\":false,\"version\":2,\"error_message\":\"Test Error\",\"error_code\":\"TestCode\",\"bpmn_process_id\":\"testProcess\",\"process_definition_key\":111,\"resource_name\":\"testProcess.bpmn\"}";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void marshalTest() {
        ProcessDeploymentResponse message = new ProcessDeploymentResponse();
        message.setBpmnProcessId("testProcess");
        message.setProcessDefinitionKey(111);
        message.setResourceName("testProcess.bpmn");
        message.setVersion(2);
        message.setSuccess(true);

        String messageString = assertDoesNotThrow(() -> objectMapper.writeValueAsString(message));
        assertEquals(MARSHAL_TEST_RESULT_1, messageString);

        message.setSuccess(false);
        message.setErrorMessage("Test Error");
        message.setErrorCode("TestCode");

        messageString = assertDoesNotThrow(() -> objectMapper.writeValueAsString(message));
        assertEquals(MARSHAL_TEST_RESULT_2, messageString);
    }

    @Test
    public void unmarshalTest() {
        ProcessDeploymentResponse unmarshaledMessage1
                = assertDoesNotThrow(() -> objectMapper.readValue(MARSHAL_TEST_RESULT_1, ProcessDeploymentResponse.class));

        ProcessDeploymentResponse message = new ProcessDeploymentResponse();
        message.setBpmnProcessId("testProcess");
        message.setProcessDefinitionKey(111);
        message.setResourceName("testProcess.bpmn");
        message.setVersion(2);
        message.setSuccess(true);

        assertEquals(message, unmarshaledMessage1);

        ProcessDeploymentResponse unmarshaledMessage2
                = assertDoesNotThrow(() -> objectMapper.readValue(MARSHAL_TEST_RESULT_2, ProcessDeploymentResponse.class));

        message.setSuccess(false);
        message.setErrorMessage("Test Error");
        message.setErrorCode("TestCode");

        assertEquals(message, unmarshaledMessage2);
    }
}
