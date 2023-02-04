package pl.piomin.services.quarkus.employee.config;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.orbitz.consul.Consul;
import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.arc.properties.IfBuildProperty;

@ApplicationScoped
public class EmployeeBeansProducer {

	@Produces
	@IfBuildProperty(name = "quarkus.consul-discovery.enabled", stringValue = "true")
	Consul consulClient = Consul.builder().build();

}
