package com.paymentconcentrator.cp.service.impl;

import com.netflix.discovery.shared.Applications;
import com.netflix.eureka.EurekaServerContextHolder;

import com.paymentconcentrator.cp.dto.PaymentDto;
import com.paymentconcentrator.cp.service.DiscoveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscoveryServiceImpl implements DiscoveryService {

	private final DiscoveryClient discoveryClient;

	@Override
	public List<PaymentDto> discoveryPayments() {
		List<String> services = discoveryClient.getServices();

		List<PaymentDto> paymentDtoList = new ArrayList<>();
		List<ServiceInstance> instances = new ArrayList<ServiceInstance>();
		services.forEach(serviceName -> {
			this.discoveryClient.getInstances(serviceName).forEach(instance -> {
				if(!serviceName.equals("eureka")&&!serviceName.equals("zuul")&&!serviceName.equals("payment-concentrator")&&!serviceName.equals("pcc")){
					instances.add(instance);
				}
			});
		});
		for (ServiceInstance serviceInstance : instances) {
			PaymentDto paymentDto = new PaymentDto();
			String[] instanceString = serviceInstance.getInstanceId().split(":");
			paymentDto.setName(instanceString[1]);
			paymentDto.setUrl("http://localhost:" + String.valueOf(serviceInstance.getPort()));
			paymentDtoList.add(paymentDto);
		}
		return paymentDtoList;
	}

	@Override
	public String getServicePort(String serviceName) {
		List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
		String port="";
		for(ServiceInstance serviceInstance : instances){
			port= String.valueOf(serviceInstance.getPort());
		}
		return port;
	}
}
