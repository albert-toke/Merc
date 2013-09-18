package business.services;

import java.util.List;

import proxy.Proxy;
import common.wrappers.Bid;
import common.wrappers.Job;
import common.wrappers.JobSearch;
import exceptions.BusinessException;

public class ProjectBS {

	private Proxy adapter;

	
	//TODO temporary fix
	public ProjectBS() {
		this.adapter = Proxy.getInstance();
	}

	public List<Job> getJobs(JobSearch jobSearchDTO) {

		List<Job> jobs = this.adapter.searchJobs(jobSearchDTO);
		return jobs;
	}

	public List<Bid> getBids(long projectId, String provider) {
		List<Bid> bids = null;
		try {
			bids = this.adapter.getBidsForProject(projectId, provider);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bids;
	}
}
