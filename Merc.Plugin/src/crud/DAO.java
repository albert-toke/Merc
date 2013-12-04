package crud;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import mapper.impl.CommonMapper;

import org.scribe.model.Token;


import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import common.wrappers.TokenDTO;


public class DAO {
	
	
	private static DAO dao;
	
	private static final String EMPTY_FILE = "No content to map due to end-of-input\n at [Source: resources\\tokens.json; line: 1, column: 1]";
	private ObjectMapper mapper;
	
	private DAO(){
		this.mapper = new ObjectMapper();
	}
	
	public static DAO getInstance(){
		if(dao == null){
			dao = new DAO();
		}
		return dao;
	}
	

	public static void writeToFileJSON(String dir,String content) throws IOException{
		FileWriter output = null;
		try {
			output = new FileWriter(dir);
			BufferedWriter writer = new BufferedWriter(output);
			writer.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			output.close();
		}
	}
	
	
	public void exportToken(String key,Token token) throws JsonGenerationException, JsonMappingException, IOException{
		List<TokenDTO> tokenDtoList = getExportedTokens();
		boolean foundToken = false;
		boolean writeNeeded = true;
		Iterator<TokenDTO> iter = tokenDtoList.iterator();
		while(iter.hasNext() && !foundToken){
			TokenDTO dto = iter.next();
			if(key.equals(dto.getProvider())){
				if(token.getSecret().equals(dto.getSecret()) && token.getToken().equals(dto.getToken())){
					writeNeeded = false;
					foundToken = true;
				}
			}
		}
		if(writeNeeded){
			tokenDtoList.add(CommonMapper.convertTokenToDTO(key, token));
			mapper = new ObjectMapper();
			mapper.writeValue(new File("resources/tokens.json"), tokenDtoList);
		}
	}
	
	private List<TokenDTO> getExportedTokens() throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		List<TokenDTO> tokenDtoList;
		try{
			tokenDtoList = mapper.readValue(new File("resources/tokens.json"), new TypeReference<List<TokenDTO>>() { });
		}catch(JsonMappingException ex){
			if(ex.getMessage().equals(EMPTY_FILE)){
				tokenDtoList = new ArrayList<TokenDTO>();
			}
			else{
				throw new JsonMappingException(ex.getMessage());
			}
		}
		return tokenDtoList;
	}
	
	public Token getTokenByProvider(String provider) throws JsonParseException, JsonMappingException, IOException{
		Token token = null;
		List<TokenDTO> tokenDtoList = getExportedTokens();
		for(TokenDTO tempToken:tokenDtoList){
			if(provider.equals(tempToken.getProvider())){
				token = CommonMapper.convertDtoToToken(tempToken);
				break;
			}
		}
		return token;
	}
	
//	public static void main(String[] args) {
//		DAO d = DAO.getInstance();
//		try {
//			Token accessToken = new Token("4aae739dc6afeb6c2a1ff97c0feba40e9b046c2e","6d6d12c96d34cef50fca1ce43bd4e0739af728fb","");
//			d.exportToken("elance",accessToken);
//		} catch (JsonGenerationException e) {
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}
