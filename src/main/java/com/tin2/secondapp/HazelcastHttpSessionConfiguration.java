package com.tin2.secondapp;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapAttributeConfig;
import com.hazelcast.config.MapIndexConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.hazelcast.HazelcastSessionRepository;
import org.springframework.session.hazelcast.PrincipalNameExtractor;
import org.springframework.session.hazelcast.config.annotation.web.http.EnableHazelcastHttpSession;

/**
 * Created by VAN_TIEN on 09/04/2017.
 */
@EnableHazelcastHttpSession
@Configuration
public class HazelcastHttpSessionConfiguration {
    public static final String NAMESPACE = "spring:session:sessions";

    @Value( "${hazelcast.publicAddress:localhost}" )
    private String publicAddress;

    @Value( "${hazelcast.port:5701}" )
    private int port;

    @Value( "${hazelcast.portCount:100}" )
    private int portCount;

    @Bean
    public HazelcastInstance hazelcastInstance()
    {
        final MapAttributeConfig attributeConfig =
                new MapAttributeConfig().setName( HazelcastSessionRepository.PRINCIPAL_NAME_ATTRIBUTE )
                        .setExtractor( PrincipalNameExtractor.class.getName() );

        final Config config = new Config();
        config.getMapConfig( NAMESPACE ).addMapAttributeConfig( attributeConfig ).addMapIndexConfig(
                new MapIndexConfig( HazelcastSessionRepository.PRINCIPAL_NAME_ATTRIBUTE, false ) );

        config.getNetworkConfig().setPublicAddress( publicAddress );
        config.getNetworkConfig().setPort( port );
        config.getNetworkConfig().setPortCount( portCount );

        return Hazelcast.newHazelcastInstance( config );
    }

}
