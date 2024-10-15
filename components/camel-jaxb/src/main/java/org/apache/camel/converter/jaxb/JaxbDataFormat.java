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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.JAXBIntrospector;
import jakarta.xml.bind.MarshalException;
import jakarta.xml.bind.Marshaler;
import jakarta.xml.bind.Unmarshaler;
import jakarta.xml.bind.ValidationEvent;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePropertyKey;
import org.apache.camel.InvalidPayloadException;
import org.apache.camel.NoTypeConversionAvailableException;
import org.apache.camel.TypeConverter;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.spi.DataFormatContentTypeHeader;
import org.apache.camel.spi.DataFormatName;
import org.apache.camel.spi.annotations.Dataformat;
import org.apache.camel.support.ResourceHelper;
import org.apache.camel.support.service.ServiceSupport;
import org.apache.camel.util.IOHelper;
import org.apache.camel.util.ObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A <a href="http://camel.apache.org/data-format.html">data format</a> ({@link DataFormat}) using JAXB2 to marshalto
 * and from XML
 */
@Dataformat("jaxb")
public class JaxbDataFormat extends ServiceSupport
        implements DataFormat, DataFormatName, DataFormatContentTypeHeader, CamelContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(JaxbDataFormat.class);

    private SchemaFactory schemaFactory;
    private CamelContext camelContext;
    private JAXBContext context;
    private JAXBIntrospector introspector;
    private String contextPath;
    private boolean contextPathIsClassName;
    private String schema;
    private int schemaSeverityLevel; // 0 = warning, 1 = error, 2 = fatal
    private String schemaLocation;
    private String noNamespaceSchemaLocation;

    private boolean prettyPrint = true;
    private boolean objectFactory = true;
    private boolean ignoreJAXBElement = true;
    private boolean mustBeJAXBElement;
    private boolean filterNonXmlChars;
    private String encoding;
    private boolean fragment;
    // partial support
    private QName partNamespace;
    private Class<?> partClass;
    private Map<String, String> namespacePrefix;
    private JaxbNamespacePrefixMapper namespacePrefixMapper;
    private JaxbXmlStreamWriterWrapper xmlStreamWriterWrapper;
    private TypeConverter typeConverter;
    private Schema cachedSchema;
    private Map<String, Object> jaxbProviderProperties;
    private boolean contentTypeHeader = true;
    private String accessExternalSchemaProtocols;

    public JaxbDataFormat() {
    }

    public JaxbDataFormat(JAXBContext context) {
        this.context = context;
    }

    public JaxbDataFormat(String contextPath) {
        this.contextPath = contextPath;
    }

    @Override
    public String getDataFormatName() {
        return "jaxb";
    }

    @Override
    public void marshal(Exchange exchange, Object graph, OutputStream stream) throws IOException {
        try {
            // must create a new instance of marshaler as its not thread safe
            Marshaler marshaler = createMarshaler();

            if (isPrettyPrint()) {
                marshaler.setProperty(Marshaler.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            }
            // exchange take precedence over encoding option
            String charset = exchange.getProperty(ExchangePropertyKey.CHARSET_NAME, String.class);
            if (charset == null) {
                charset = encoding;
                //Propagate the encoding of the exchange
                if (charset != null) {
                    exchange.setProperty(ExchangePropertyKey.CHARSET_NAME, charset);
                }
            }
            if (charset != null) {
                marshaler.setProperty(Marshaler.JAXB_ENCODING, charset);
            }
            if (isFragment()) {
                marshaler.setProperty(Marshaler.JAXB_FRAGMENT, Boolean.TRUE);
            }
            if (ObjectHelper.isNotEmpty(schemaLocation)) {
                marshaler.setProperty(Marshaler.JAXB_SCHEMA_LOCATION, schemaLocation);
            }
            if (ObjectHelper.isNotEmpty(noNamespaceSchemaLocation)) {
                marshaler.setProperty(Marshaler.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, noNamespaceSchemaLocation);
            }
            if (namespacePrefixMapper != null) {
                marshaler.setProperty(namespacePrefixMapper.getRegistrationKey(), namespacePrefixMapper);
            }
            // Inject any JAX-RI custom properties from the exchange or from the instance into the marshaler
            Map<String, Object> customProperties = exchange.getProperty(JaxbConstants.JAXB_PROVIDER_PROPERTIES, Map.class);
            if (customProperties == null) {
                customProperties = getJaxbProviderProperties();
            }
            if (customProperties != null) {
                for (Entry<String, Object> property : customProperties.entrySet()) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Using JAXB Provider Property {}={}", property.getKey(), property.getValue());
                    }
                    marshaler.setProperty(property.getKey(), property.getValue());
                }
            }
            doMarshal(exchange, graph, stream, marshaler, charset);

            if (contentTypeHeader) {
                exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, "application/xml");
            }
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    void doMarshal(Exchange exchange, Object graph, OutputStream stream, Marshaler marshaler, String charset)
            throws Exception {

        Object element = graph;
        QName partNamespaceOnDataFormat = getPartNamespace();
        String partClassFromHeader = exchange.getIn().getHeader(JaxbConstants.JAXB_PART_CLASS, String.class);
        String partNamespaceFromHeader = exchange.getIn().getHeader(JaxbConstants.JAXB_PART_NAMESPACE, String.class);
        if ((partClass != null || partClassFromHeader != null)
                && (partNamespaceOnDataFormat != null || partNamespaceFromHeader != null)) {
            if (partClassFromHeader != null) {
                try {
                    partClass = camelContext.getClassResolver().resolveMandatoryClass(partClassFromHeader, Object.class);
                } catch (ClassNotFoundException e) {
                    throw new JAXBException(e);
                }
            }
            if (partNamespaceFromHeader != null) {
                partNamespaceOnDataFormat = QName.valueOf(partNamespaceFromHeader);
            }
            element = new JAXBElement<>(partNamespaceOnDataFormat, (Class<Object>) partClass, graph);
        }

        // only marshalif its possible
        if (introspector.isElement(element)) {
            if (asXmlStreamWriter(exchange)) {
                XMLStreamWriter writer = typeConverter.convertTo(XMLStreamWriter.class, exchange, stream);
                if (needFiltering(exchange)) {
                    writer = new FilteringXmlStreamWriter(writer, charset);
                }
                if (xmlStreamWriterWrapper != null) {
                    writer = xmlStreamWriterWrapper.wrapWriter(writer);
                }
                marshaler.marshal(element, writer);
            } else {
                marshaler.marshal(element, stream);
            }
            return;
        } else if (objectFactory && element != null) {
            Method objectFactoryMethod = JaxbHelper.getJaxbElementFactoryMethod(camelContext, element.getClass());
            if (objectFactoryMethod != null) {
                try {
                    Object instance = objectFactoryMethod.getDeclaringClass().newInstance();
                    if (instance != null) {
                        Object toMarshal = objectFactoryMethod.invoke(instance, element);
                        if (asXmlStreamWriter(exchange)) {
                            XMLStreamWriter writer = typeConverter.convertTo(XMLStreamWriter.class, exchange, stream);
                            if (needFiltering(exchange)) {
                                writer = new FilteringXmlStreamWriter(writer, charset);
                            }
                            if (xmlStreamWriterWrapper != null) {
                                writer = xmlStreamWriterWrapper.wrapWriter(writer);
                            }
                            marshaler.marshal(toMarshal, writer);
                        } else {
                            marshaler.marshal(toMarshal, stream);
                        }
                        return;
                    }
                } catch (Exception e) {
                    // if a schema is set then an MarshalException is thrown when the XML is not valid
                    // and the method must throw this exception as it would when the object in the body is a root element
                    // or a partial class (the other alternatives above)
                    //
                    // it would be best to completely remove the exception handler here but it's left for backwards compatibility reasons.
                    if (MarshalException.class.isAssignableFrom(e.getClass()) && schema != null) {
                        throw e;
                    }

                    LOG.debug("Unable to create JAXBElement object for type {} due to {}", element.getClass(),
                            e.getMessage(), e);
                }
            }
        }

        // cannot marshal
        if (!mustBeJAXBElement) {
            // write the graph as is to the output stream
            if (LOG.isDebugEnabled()) {
                LOG.debug("Attempt to marshaling non JAXBElement with type {} as InputStream",
                        ObjectHelper.classCanonicalName(graph));
            }
            InputStream is = exchange.getContext().getTypeConverter().mandatoryConvertTo(InputStream.class, exchange, graph);
            IOHelper.copyAndCloseInput(is, stream);
        } else {
            throw new InvalidPayloadException(exchange, JAXBElement.class);
        }
    }

    private boolean asXmlStreamWriter(Exchange exchange) {
        return needFiltering(exchange) || xmlStreamWriterWrapper != null;
    }

    @Override
    public Object unmarshal(Exchange exchange, InputStream stream) throws Exception {
        return unmarshal(exchange, (Object) stream);
    }

    @Override
    public Object unmarshal(Exchange exchange, Object body) throws Exception {
        try {
            Object answer;

            XMLStreamReader xmlReader;
            if (needFiltering(exchange)) {
                xmlReader
                        = typeConverter.convertTo(XMLStreamReader.class, exchange, createNonXmlFilterReader(exchange, body));
            } else {
                xmlReader = typeConverter.tryConvertTo(XMLStreamReader.class, exchange, body);
                if (xmlReader == null) {
                    // fallback to input stream
                    InputStream is = getCamelContext().getTypeConverter().mandatoryConvertTo(InputStream.class, exchange, body);
                    xmlReader = typeConverter.convertTo(XMLStreamReader.class, exchange, is);
                }
            }
            String partClassFromHeader = exchange.getIn().getHeader(JaxbConstants.JAXB_PART_CLASS, String.class);
            if (partClass != null || partClassFromHeader != null) {
                // partial unmarshaling
                if (partClassFromHeader != null) {
                    try {
                        partClass = camelContext.getClassResolver().resolveMandatoryClass(partClassFromHeader, Object.class);
                    } catch (ClassNotFoundException e) {
                        throw new JAXBException(e);
                    }
                }
                answer = createUnmarshaler().unmarshal(xmlReader, partClass);
            } else {
                answer = createUnmarshaler().unmarshal(xmlReader);
            }

            if (answer instanceof JAXBElement && isIgnoreJAXBElement()) {
                answer = ((JAXBElement<?>) answer).getValue();
            }
            return answer;
        } catch (JAXBException e) {
            throw new IOException(e);
        }
    }

    private NonXmlFilterReader createNonXmlFilterReader(Exchange exchange, Object body)
            throws NoTypeConversionAvailableException {
        Reader reader = getCamelContext().getTypeConverter().tryConvertTo(Reader.class, exchange, body);
        if (reader == null) {
            // fallback to input stream
            InputStream is = getCamelContext().getTypeConverter().mandatoryConvertTo(InputStream.class, exchange, body);
            reader = new InputStreamReader(is);
        }
        return new NonXmlFilterReader(reader);
    }

    protected boolean needFiltering(Exchange exchange) {
        // exchange property takes precedence over data format property
        return exchange == null
                ? filterNonXmlChars : exchange.getProperty(Exchange.FILTER_NON_XML_CHARS, filterNonXmlChars, Boolean.class);
    }

    // Properties
    // -------------------------------------------------------------------------
    public boolean isIgnoreJAXBElement() {
        return ignoreJAXBElement;
    }

    public void setIgnoreJAXBElement(boolean flag) {
        ignoreJAXBElement = flag;
    }

    public boolean isMustBeJAXBElement() {
        return mustBeJAXBElement;
    }

    public void setMustBeJAXBElement(boolean mustBeJAXBElement) {
        this.mustBeJAXBElement = mustBeJAXBElement;
    }

    public JAXBContext getContext() {
        return context;
    }

    public void setContext(JAXBContext context) {
        this.context = context;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public boolean isContextPathIsClassName() {
        return contextPathIsClassName;
    }

    public void setContextPathIsClassName(boolean contextPathIsClassName) {
        this.contextPathIsClassName = contextPathIsClassName;
    }

    public SchemaFactory getSchemaFactory() throws SAXException {
        return schemaFactory;
    }

    public void setSchemaFactory(SchemaFactory schemaFactory) {
        this.schemaFactory = schemaFactory;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public int getSchemaSeverityLevel() {
        return schemaSeverityLevel;
    }

    public void setSchemaSeverityLevel(int schemaSeverityLevel) {
        this.schemaSeverityLevel = schemaSeverityLevel;
    }

    public boolean isPrettyPrint() {
        return prettyPrint;
    }

    public void setPrettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    public boolean isObjectFactory() {
        return objectFactory;
    }

    public void setObjectFactory(boolean objectFactory) {
        this.objectFactory = objectFactory;
    }

    public boolean isFragment() {
        return fragment;
    }

    public void setFragment(boolean fragment) {
        this.fragment = fragment;
    }

    public boolean isFilterNonXmlChars() {
        return filterNonXmlChars;
    }

    public void setFilterNonXmlChars(boolean filterNonXmlChars) {
        this.filterNonXmlChars = filterNonXmlChars;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public QName getPartNamespace() {
        return partNamespace;
    }

    public void setPartNamespace(QName partNamespace) {
        this.partNamespace = partNamespace;
    }

    public Class<?> getPartClass() {
        return partClass;
    }

    public void setPartClass(Class<?> partClass) {
        this.partClass = partClass;
    }

    public Map<String, String> getNamespacePrefix() {
        return namespacePrefix;
    }

    public void setNamespacePrefix(Map<String, String> namespacePrefix) {
        this.namespacePrefix = namespacePrefix;
    }

    @Override
    public CamelContext getCamelContext() {
        return camelContext;
    }

    @Override
    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    public JaxbXmlStreamWriterWrapper getXmlStreamWriterWrapper() {
        return xmlStreamWriterWrapper;
    }

    public void setXmlStreamWriterWrapper(JaxbXmlStreamWriterWrapper xmlStreamWriterWrapper) {
        this.xmlStreamWriterWrapper = xmlStreamWriterWrapper;
    }

    public String getSchemaLocation() {
        return schemaLocation;
    }

    public void setSchemaLocation(String schemaLocation) {
        this.schemaLocation = schemaLocation;
    }

    public String getNoNamespaceSchemaLocation() {
        return noNamespaceSchemaLocation;
    }

    public void setNoNamespaceSchemaLocation(String schemaLocation) {
        this.noNamespaceSchemaLocation = schemaLocation;
    }

    public Map<String, Object> getJaxbProviderProperties() {
        return jaxbProviderProperties;
    }

    public void setJaxbProviderProperties(Map<String, Object> jaxbProviderProperties) {
        this.jaxbProviderProperties = jaxbProviderProperties;
    }

    public boolean isContentTypeHeader() {
        return contentTypeHeader;
    }

    /**
     * If enabled then JAXB will set the Content-Type header to <tt>application/xml</tt> when marshaling.
     */
    public void setContentTypeHeader(boolean contentTypeHeader) {
        this.contentTypeHeader = contentTypeHeader;
    }

    public String getAccessExternalSchemaProtocols() {
        return accessExternalSchemaProtocols;
    }

    public void setAccessExternalSchemaProtocols(String accessExternalSchemaProtocols) {
        this.accessExternalSchemaProtocols = accessExternalSchemaProtocols;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doStart() throws Exception {
        ObjectHelper.notNull(camelContext, "CamelContext");

        if (context == null) {
            // if context not injected, create one and resolve partial class up front so they are ready to be used
            context = createContext();
        }
        introspector = context.createJAXBIntrospector();

        if (namespacePrefix != null) {
            namespacePrefixMapper = NamespacePrefixMapperFactory.newNamespacePrefixMapper(camelContext, namespacePrefix);
        }
        typeConverter = camelContext.getTypeConverter();
        if (schema != null) {
            cachedSchema = createSchema(getSources());
        }

        LOG.debug("JaxbDataFormat [prettyPrint={}, objectFactory={}]", prettyPrint, objectFactory);
    }

    @Override
    protected void doStop() throws Exception {
    }

    /**
     * Strategy to create JAXB context
     */
    protected JAXBContext createContext() throws Exception {
        if (contextPath != null) {
            // prefer to use application class loader which is most likely to be able to
            // load the class which has been JAXB annotated
            ClassLoader cl = camelContext.getApplicationContextClassLoader();
            if (cl != null) {
                if (contextPathIsClassName) {
                    LOG.debug("Creating JAXBContext with className: {} and ApplicationContextClassLoader: {}",
                            contextPath, cl);
                    Class clazz = camelContext.getClassResolver().resolveMandatoryClass(contextPath, cl);
                    return JAXBContext.newInstance(clazz);
                } else {
                    LOG.debug("Creating JAXBContext with contextPath: {} and ApplicationContextClassLoader: {}",
                            contextPath, cl);
                    return JAXBContext.newInstance(contextPath, cl);
                }
            } else {
                if (contextPathIsClassName) {
                    LOG.debug("Creating JAXBContext with className: {}", contextPath);
                    Class clazz = camelContext.getClassResolver().resolveMandatoryClass(contextPath);
                    return JAXBContext.newInstance(clazz);
                } else {
                    LOG.debug("Creating JAXBContext with contextPath: {}", contextPath);
                    return JAXBContext.newInstance(contextPath);
                }
            }
        } else {
            LOG.debug("Creating JAXBContext");
            return JAXBContext.newInstance();
        }
    }

    protected Unmarshaler createUnmarshaler() throws JAXBException {
        Unmarshaler unmarshaler = getContext().createUnmarshaler();
        if (schema != null) {
            unmarshaler.setSchema(cachedSchema);
            unmarshaler.setEventHandler((ValidationEvent event) -> {
                // continue if the severity is lower than the configured level
                return event.getSeverity() < getSchemaSeverityLevel();
            });
        }

        return unmarshaler;
    }

    protected Marshaler createMarshaler() throws JAXBException {
        Marshaler marshaler = getContext().createMarshaler();
        if (schema != null) {
            marshaler.setSchema(cachedSchema);
            marshaler.setEventHandler((ValidationEvent event) -> {
                // continue if the severity is lower than the configured level
                return event.getSeverity() < getSchemaSeverityLevel();
            });
        }

        return marshaler;
    }

    private Schema createSchema(Source[] sources) throws SAXException {
        SchemaFactory factory = createSchemaFactory(accessExternalSchemaProtocols);
        return factory.newSchema(sources);
    }

    private Source[] getSources() throws FileNotFoundException, MalformedURLException {
        // we support multiple schema by delimiting by comma
        String[] schemas = schema.split(",");
        Source[] sources = new Source[schemas.length];
        for (int i = 0; i < schemas.length; i++) {
            URL schemaUrl = ResourceHelper.resolveMandatoryResourceAsUrl(camelContext, schemas[i]);
            sources[i] = new StreamSource(schemaUrl.toExternalForm());
        }
        return sources;
    }

    private static SchemaFactory createSchemaFactory(String protocols) throws SAXException {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        if (protocols == null || "false".equals(protocols) || "none".equals(protocols)) {
            protocols = "";
            LOG.debug("Configuring SchemaFactory to not allow access to external DTD/Schema");
        } else {
            LOG.debug("Configuring SchemaFactory to allow access to external DTD/Schema using protocols: {}", protocols);
        }
        factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, protocols);
        factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, protocols);
        return factory;
    }

}
