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
package org.apache.camel.spi;

/**
 * The last error that happened during changing the route lifecycle, that is, such as when an exception was thrown during
 * starting the route.
 * <p/>
 * This is only errors for route lifecycle changes, it is not exceptions thrown during routing messsages with the Camel
 * routing engine.
 */
public interface RouteError {

    enum Phase {
        START,
        STOP,
        SUSPEND,
        RESUME,
        SHUTDOWN,
        REMOVE
    }

    /**
     * Gets the phase associated with the error.
     *
     * @return the phase.
     */
    Phase getPhase();

    /**
     * Gets the error.
     *
     * @return the error.
     */
    Throwable getException();

    /**
     * Whether the route is regarded as unhealthy.
     */
    boolean isUnhealthy();
}
