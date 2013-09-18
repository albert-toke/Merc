package freelancer.test.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import mapper.FreelancerMapper;

import common.wrappers.Project;

import exceptions.BusinessException;

public class ImporterClass {

	
	public static Project getProjectFromFile(String dir) throws IOException, BusinessException{
		String jsonProject = readFile(dir+"/project-test-data.json",StandardCharsets.UTF_8);
		FreelancerMapper mapper = new FreelancerMapper();
		Project project = mapper.convertProjectDetailsJsonToSystem(jsonProject);
		return project;
	}
	
	private static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}
	
}
