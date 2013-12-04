package business.services;

import java.util.ArrayList;
import java.util.List;

import common.wrappers.History;

import exceptions.BusinessException;
import gateway.HistoryGateway;

public class HistoryBS {

    private static HistoryBS historyBS;

    private List<History> searchHistory;

    // TODO Multy user implementation. Temporary solution, erase the history when new authentification.
    private HistoryBS() {

    }

    public static HistoryBS getInstance() {
	if (historyBS == null) {
	    historyBS = new HistoryBS();
	}
	return historyBS;
    }

    /**
     * Returns the searchHistory for the User.
     * 
     * @return The list of search history entries.
     * @throws BusinessException
     */
    public List<History> getSearchHistory() {
	if (searchHistory == null) {
	    try {
		searchHistory = HistoryGateway.getSearchHistory();
	    } catch (BusinessException ex) {
		searchHistory = new ArrayList<History>();
	    }
	}
	return searchHistory;
    }

    /**
     * Returns the search history in string.
     * 
     * @return The search history in string format.
     */
    public List<String> getSearchHistoryString() {
	List<String> stringSearchHistory = new ArrayList<String>();
	if (searchHistory == null) {
	    try {
		searchHistory = HistoryGateway.getSearchHistory();
	    } catch (BusinessException ex) {
		searchHistory = new ArrayList<History>();
	    }
	}
	for (History history : searchHistory) {
	    stringSearchHistory.add(history.getSearchTerm());
	}
	return stringSearchHistory;
    }

    /**
     * Saves the current search history into an external file.
     * 
     * @throws BusinessException
     */
    public void saveSearchHistory() throws BusinessException {
	if (searchHistory != null) {
	    HistoryGateway.writeHistory(searchHistory);
	}
    }

    /**
     * Adds a new search term to search history, if the term doesen't already exist in the current search history.
     * 
     * @param newSearch
     *            The new search term.
     * @throws BusinessException
     */
    public void addNewSearchEntry(String newSearch) throws BusinessException {
	boolean alreadySearched = false;
	for (History history : searchHistory) {
	    if (history.getSearchTerm().equals(newSearch)) {
		history.setCount(history.getCount() + 1);
		alreadySearched = true;
		break;
	    }
	}
	if (!alreadySearched) {
	    History newHistory = new History();
	    newHistory.setCount(1);
	    newHistory.setSearchTerm(newSearch);
	    searchHistory.add(newHistory);
	    saveSearchHistory();
	}
    }

    /**
     * Deletes the history file.
     */
    public void eraseHistory() {
	HistoryGateway.eraseHistoryFile();
    }
}
