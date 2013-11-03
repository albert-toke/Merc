package gateway;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.wrappers.History;
import common.wrappers.TokenDTO;

import exceptions.BusinessException;

public class HistoryGateway {

    private static final String FILE_DIR = "history.json";

    private HistoryGateway() {

    }

    public static List<History> getSearchHistory() throws BusinessException {
	List<History> historyList = null;
	ObjectMapper mapper = new ObjectMapper();
	try {
	    historyList = mapper.readValue(new File(FILE_DIR), new TypeReference<List<TokenDTO>>() {
	    });
	} catch (IOException e) {
	    throw new BusinessException(e);
	}
	return historyList;
    }

    public static void writeHistory(List<History> searchHistory) throws BusinessException {
	ObjectMapper mapper = new ObjectMapper();
	try {
	    mapper.writeValue(new File(FILE_DIR), searchHistory);
	} catch (IOException e) {
	    throw new BusinessException(e);
	}
    }

    public static void eraseHistoryFile() {
	File file = new File(FILE_DIR);
	file.delete();
    }
}
