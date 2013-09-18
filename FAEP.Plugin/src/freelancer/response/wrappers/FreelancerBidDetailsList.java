package freelancer.response.wrappers;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FreelancerBidDetailsList {

	private List<FreelancerBidDetail> items;

	public List<FreelancerBidDetail> getItems() {
		return items;
	}

	public void setItems(List<FreelancerBidDetail> items) {
		this.items = items;
	}

}
