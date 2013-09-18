package freelancer.response.wrappers;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FreelancerProjectPublicMessageList {

	private int count;
	private List<FreelancerProjectPublicMessage> items;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<FreelancerProjectPublicMessage> getItems() {
		return items;
	}

	public void setItems(List<FreelancerProjectPublicMessage> items) {
		this.items = items;
	}

}
