package sop.modelviews;

import java.util.List;

import sop.models.QuoteItems;

public class QuoteItemsWrapper {

    public List<QuoteItems> quotes;

    public List<QuoteItems> getQuotes() {
        return quotes;
    }

    public void setQuotes(List<QuoteItems> quotes) {
        this.quotes = quotes;
    }
}
