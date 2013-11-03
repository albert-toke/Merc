package faep.controller.provider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.fieldassist.ContentProposal;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;

import business.services.HistoryBS;

public class SearchHistoryContentProposal implements IContentProposalProvider {

    private HistoryBS historyBs;

    public SearchHistoryContentProposal() {
	historyBs = HistoryBS.getInstance();
    }

    @Override
    public IContentProposal[] getProposals(String contents, int position) {
	List<ContentProposal> contentList = new ArrayList<ContentProposal>();
	List<String> searchHistory = historyBs.getSearchHistoryString();
	for (String history : searchHistory) {
	    if (history.contains(contents)) {
		contentList.add(new ContentProposal(history));
	    }
	}

	return contentList.toArray(new IContentProposal[contentList.size()]);
    }
}
