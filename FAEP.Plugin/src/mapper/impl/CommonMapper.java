package mapper.impl;

import org.scribe.model.Token;

import common.wrappers.TokenDTO;


public class CommonMapper {
	
	
	public static TokenDTO convertTokenToDTO(String provider,Token token){
		TokenDTO dto = new TokenDTO(provider, token.getToken(), token.getSecret(),token.getRawResponse());
		return dto;
	}
	
	public static Token convertDtoToToken(TokenDTO tokenDto){
		Token token = new Token(tokenDto.getToken(), tokenDto.getSecret(),tokenDto.getRawResponse());
		return token;
	}
}
