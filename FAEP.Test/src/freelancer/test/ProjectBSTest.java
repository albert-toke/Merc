package freelancer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import proxy.Proxy;
import common.wrappers.Job;
import common.wrappers.JobSearch;
import common.wrappers.Project;
import common.wrappers.ProjectPostMessage;
import exceptions.BusinessException;
import freelancer.test.util.GatewaysInitializer;
import gateway.AbstractApiGateway;

public class ProjectBSTest {

	private static final String PROVIDER = "freelancer";
	private static final long MYPROJECT = 959;
	private Proxy proxy;
	
	@Before
	public void initalizeProxyWithGateways(){
		List<AbstractApiGateway> gateways = GatewaysInitializer.initGatewaySelecter();
		proxy = Proxy.getInstance(gateways);
	}
	
	@Test
	public void searchProjectTest() {
		assertTrue(proxy.getTokenLocally().isEmpty());
		JobSearch jobSearchDTO = new JobSearch();
		jobSearchDTO.setSearchJobTypeCSV(".NET");
		jobSearchDTO.setSearchKeyword("");
		List<Job> jobs = proxy.searchJobs(jobSearchDTO);
		assertFalse(jobs.isEmpty());
	}

	@Test
	public void getJobListTest() {
		assertTrue(proxy.getTokenLocally().isEmpty());
		try {
			proxy.getJobList();
		} catch (RuntimeException ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void getPublicMessagesForProjectTest() {
		assertTrue(proxy.getTokenLocally().isEmpty());
		try {
			assertFalse(proxy.getPublicMessagesForProject(MYPROJECT, PROVIDER).isEmpty());
		} catch (BusinessException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	@Ignore
	public void postPublicMessageOnProject() {
		assertTrue(proxy.getTokenLocally().isEmpty());
		ProjectPostMessage message = new ProjectPostMessage();
		message.setMessage("nyest");
		message.setProjectId(MYPROJECT);
		message.setProvider(PROVIDER);
		try {
			proxy.postPublicMessageOnProject(message);
		} catch (BusinessException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void getBidsForProject() {
		assertTrue(proxy.getTokenLocally().isEmpty());
		try {
			assertFalse(proxy.getBidsForProject(MYPROJECT, PROVIDER).isEmpty());
		} catch (BusinessException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void getProjectDetailsTest() {
		assertTrue(proxy.getTokenLocally().isEmpty());
		Project project;
		try {
			//project = proxy.getProjectDetails(977, PROVIDER);
			project = proxy.getProjectDetails(MYPROJECT, PROVIDER);
			assertNotNull(project);
			assertEquals(MYPROJECT, project.getId());
		} catch (BusinessException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}

	@Test
	public void getProjectBiddedOnTest() {
		assertTrue(proxy.getTokenLocally().isEmpty());
		try {
			proxy.getBiddedProjects();
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
