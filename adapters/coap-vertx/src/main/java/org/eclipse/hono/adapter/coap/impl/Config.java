/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.hono.adapter.coap.impl;

import org.eclipse.hono.adapter.coap.CoapAdapterProperties;
import org.eclipse.hono.service.AbstractAdapterConfig;
import org.eclipse.hono.service.metric.MetricsTags;
import org.eclipse.hono.util.Constants;
import org.springframework.beans.factory.config.ObjectFactoryCreatingFactoryBean;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import io.micrometer.core.instrument.MeterRegistry;

/**
 * Spring Boot configuration for the COAP adapter.
 */
@Configuration
public class Config extends AbstractAdapterConfig {

    private static final String CONTAINER_ID_HONO_COAP_ADAPTER = "Hono CoAP Adapter";
    private static final String BEAN_NAME_VERTX_BASED_COAP_ADAPTER = "vertxBasedCoapAdapter";

    /**
     * Exposes the CoAP adapter's configuration properties as a Spring bean.
     *
     * @return The configuration properties.
     */
    @Bean
    @ConfigurationProperties(prefix = "hono.coap")
    public CoapAdapterProperties adapterProperties() {
        return new CoapAdapterProperties();
    }

    /**
     * Creates a new COAP adapter instance.
     *
     * @return The new instance.
     */
    @Bean(name = BEAN_NAME_VERTX_BASED_COAP_ADAPTER)
    @Scope("prototype")
    public VertxBasedCoapAdapter vertxBasedCoapAdapter() {
        return new VertxBasedCoapAdapter();
    }

    @Override
    protected String getAdapterName() {
        return CONTAINER_ID_HONO_COAP_ADAPTER;
    }

    /**
     * Customizer for meter registry.
     *
     * @return The new meter registry customizer.
     */
    @Bean
    public MeterRegistryCustomizer<MeterRegistry> commonTags() {
        return r -> r.config().commonTags(
                MetricsTags.forProtocolAdapter(Constants.PROTOCOL_ADAPTER_TYPE_COAP));
    }

    /**
     * Exposes a factory for creating COAP adapter instances.
     *
     * @return The factory bean.
     */
    @Bean
    public ObjectFactoryCreatingFactoryBean serviceFactory() {
        final ObjectFactoryCreatingFactoryBean factory = new ObjectFactoryCreatingFactoryBean();
        factory.setTargetBeanName(BEAN_NAME_VERTX_BASED_COAP_ADAPTER);
        return factory;
    }
}
