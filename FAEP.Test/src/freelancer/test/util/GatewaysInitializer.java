package freelancer.test.util;

import gateway.AbstractApiGateway;
import gateway.FreelancerGateway;

import java.util.ArrayList;
import java.util.List;

public class GatewaysInitializer {

	private static final String FREELANCER_API = "7dc7ee059324b47ef9c248183279ea0d436c8ec0";
	private static final String FREELANCER_SECRET = "9be5f0f5f81e9de087fd437c25e9007d0e57d6a7";
	
	public static List<AbstractApiGateway> initGatewaySelecter(){
		List<AbstractApiGateway> gateways = new ArrayList<AbstractApiGateway>();
		FreelancerGateway gateway = new FreelancerGateway();
		gateway.initGateway(FREELANCER_API, FREELANCER_SECRET);
		gateways.add(gateway);
		return gateways;
	}
	
}
