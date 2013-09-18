package business.services;

import java.util.List;
import java.util.Scanner;

import proxy.Proxy;
import exceptions.BusinessException;

/**
 * Responsible for the authentication process
 * 
 * @author Jindster
 * 
 */
public class AccountBS {

	private Proxy adapter;

	
	//TODO temproray fix,dont know if it works
	public AccountBS() {
		this.adapter = Proxy.getInstance();
	}

	public void initializeGateways() {
		List<String> notFoundLocally = this.adapter.getTokenLocally();
		for (String provider : notFoundLocally) {
			try {
				String verificationAddress = this.adapter.getRequestTokenFromProvider(provider);
				System.out.println("Please visit the following site to acquire verif code:"
						+ verificationAddress);
				Scanner in = new Scanner(System.in);

				this.adapter.getAccessTokenFromProvider(provider, in.nextLine());
				// TODO implement Business and Technical Exceptions
			} catch(BusinessException e){
				e.printStackTrace();
			}
		}
	}
}
