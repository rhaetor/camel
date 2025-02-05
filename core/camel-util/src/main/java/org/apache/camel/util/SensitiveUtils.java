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
package org.apache.camel.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public final class SensitiveUtils {

    private static final Set<String> SENSITIVE_KEYS = Collections.unmodifiableSet(new HashSet<>(
            Arrays.asList(
                    // Generated by camel build tools - do NOT edit this list!
                    // SENSITIVE-KEYS: START
                    "accesskey",
                    "accesstoken",
                    "accesstokensecret",
                    "accountkey",
                    "accountsid",
                    "acltoken",
                    "api_key",
                    "api_secret",
                    "apikey",
                    "apipassword",
                    "apiuser",
                    "apiusername",
                    "authkey",
                    "authorizationtoken",
                    "blobaccesskey",
                    "blobstoragesharedkeycredential",
                    "certresourcepassword",
                    "clientid",
                    "clientsecret",
                    "clientsecretcredential",
                    "connectionstring",
                    "consumerkey",
                    "consumersecret",
                    "emailaddress",
                    "httpproxypassword",
                    "kerberosauthticket",
                    "keystorePassword",
                    "keystorepassword",
                    "login",
                    "oauthclientid",
                    "oauthclientsecret",
                    "oauthtoken",
                    "oauthtokenurl",
                    "p12filename",
                    "passcode",
                    "passphrase",
                    "password",
                    "personalaccesstoken",
                    "privatekey",
                    "privatekeyfile",
                    "privatekeyname",
                    "privatekeypassword",
                    "proxyauthpassword",
                    "proxyauthusername",
                    "proxypassword",
                    "proxyuser",
                    "publickeyid",
                    "publishkey",
                    "queueownerawsaccountid",
                    "realm",
                    "refreshtoken",
                    "sascredential",
                    "sasljaasconfig",
                    "sassignature",
                    "secret",
                    "secretkey",
                    "securerandom",
                    "sessiontoken",
                    "sharedaccesskey",
                    "sourceblobaccesskey",
                    "sslkeypassword",
                    "sslkeystorepassword",
                    "sslpassword",
                    "ssltruststorepassword",
                    "subscribekey",
                    "systemid",
                    "token",
                    "tokencredential",
                    "truststorepassword",
                    "user",
                    "username",
                    "userpassword",
                    "verificationcode",
                    "webhookverifytoken",
                    "zookeeperpassword"
            // SENSITIVE-KEYS: END
            )));

    private static final String SENSITIVE_PATTERN = ""
                                                    // Generated by camel build tools - do NOT edit this list!
                                                    // SENSITIVE-PATTERN: START
                                                    + "\\Qaccesskey\\E"
                                                    + "|\\Qaccesstoken\\E"
                                                    + "|\\Qaccesstokensecret\\E"
                                                    + "|\\Qaccountkey\\E"
                                                    + "|\\Qaccountsid\\E"
                                                    + "|\\Qacltoken\\E"
                                                    + "|\\Qapi_key\\E"
                                                    + "|\\Qapi_secret\\E"
                                                    + "|\\Qapikey\\E"
                                                    + "|\\Qapipassword\\E"
                                                    + "|\\Qapiuser\\E"
                                                    + "|\\Qapiusername\\E"
                                                    + "|\\Qauthkey\\E"
                                                    + "|\\Qauthorizationtoken\\E"
                                                    + "|\\Qblobaccesskey\\E"
                                                    + "|\\Qblobstoragesharedkeycredential\\E"
                                                    + "|\\Qcertresourcepassword\\E"
                                                    + "|\\Qclientid\\E"
                                                    + "|\\Qclientsecret\\E"
                                                    + "|\\Qclientsecretcredential\\E"
                                                    + "|\\Qconnectionstring\\E"
                                                    + "|\\Qconsumerkey\\E"
                                                    + "|\\Qconsumersecret\\E"
                                                    + "|\\Qemailaddress\\E"
                                                    + "|\\Qhttpproxypassword\\E"
                                                    + "|\\Qkerberosauthticket\\E"
                                                    + "|\\QkeystorePassword\\E"
                                                    + "|\\Qkeystorepassword\\E"
                                                    + "|\\Qlogin\\E"
                                                    + "|\\Qoauthclientid\\E"
                                                    + "|\\Qoauthclientsecret\\E"
                                                    + "|\\Qoauthtoken\\E"
                                                    + "|\\Qoauthtokenurl\\E"
                                                    + "|\\Qp12filename\\E"
                                                    + "|\\Qpasscode\\E"
                                                    + "|\\Qpassphrase\\E"
                                                    + "|\\Qpassword\\E"
                                                    + "|\\Qpersonalaccesstoken\\E"
                                                    + "|\\Qprivatekey\\E"
                                                    + "|\\Qprivatekeyfile\\E"
                                                    + "|\\Qprivatekeyname\\E"
                                                    + "|\\Qprivatekeypassword\\E"
                                                    + "|\\Qproxyauthpassword\\E"
                                                    + "|\\Qproxyauthusername\\E"
                                                    + "|\\Qproxypassword\\E"
                                                    + "|\\Qproxyuser\\E"
                                                    + "|\\Qpublickeyid\\E"
                                                    + "|\\Qpublishkey\\E"
                                                    + "|\\Qqueueownerawsaccountid\\E"
                                                    + "|\\Qrealm\\E"
                                                    + "|\\Qrefreshtoken\\E"
                                                    + "|\\Qsascredential\\E"
                                                    + "|\\Qsasljaasconfig\\E"
                                                    + "|\\Qsassignature\\E"
                                                    + "|\\Qsecret\\E"
                                                    + "|\\Qsecretkey\\E"
                                                    + "|\\Qsecurerandom\\E"
                                                    + "|\\Qsessiontoken\\E"
                                                    + "|\\Qsharedaccesskey\\E"
                                                    + "|\\Qsourceblobaccesskey\\E"
                                                    + "|\\Qsslkeypassword\\E"
                                                    + "|\\Qsslkeystorepassword\\E"
                                                    + "|\\Qsslpassword\\E"
                                                    + "|\\Qssltruststorepassword\\E"
                                                    + "|\\Qsubscribekey\\E"
                                                    + "|\\Qsystemid\\E"
                                                    + "|\\Qtoken\\E"
                                                    + "|\\Qtokencredential\\E"
                                                    + "|\\Qtruststorepassword\\E"
                                                    + "|\\Quser\\E"
                                                    + "|\\Qusername\\E"
                                                    + "|\\Quserpassword\\E"
                                                    + "|\\Qverificationcode\\E"
                                                    + "|\\Qwebhookverifytoken\\E"
                                                    + "|\\Qzookeeperpassword\\E"
    // SENSITIVE-PATTERN: END
    ;

    private SensitiveUtils() {
    }

    /**
     * All the sensitive keys (unmodifiable) in lower-case
     */
    public static Set<String> getSensitiveKeys() {
        return SENSITIVE_KEYS;
    }

    /**
     * All the sensitive keys (unmodifiable) in lower-case for regular expression matching
     */
    public static String getSensitivePattern() {
        return SENSITIVE_PATTERN;
    }

    /**
     * Whether the given configuration property contains a sensitive key (such as password, accesstoken, etc.)
     *
     * @param  text the configuration property
     * @return      true if sensitive, false otherwise
     */
    public static boolean containsSensitive(String text) {
        int lastPeriod = text.lastIndexOf('.');
        if (lastPeriod >= 0) {
            text = text.substring(lastPeriod + 1);
        }
        text = text.toLowerCase(Locale.ENGLISH);
        text = text.replace("-", "");
        return SENSITIVE_KEYS.contains(text);
    }

}
