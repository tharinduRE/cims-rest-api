package com.cheminv.app.domain.enumeration;

/**
 * The StockStore enumeration.
 */
public enum StockStore {
    ORG("Organic"),
    INORG("Inorganic"),
    ACIDS("Acids"),
    NORM_GLASS("Normal Glassware"),
    Q_FIT_GLASS("Normal Glassware"),
    ORG_USED("Organic Used"),
    INORG_USED("Inorganic Used");

    public String getName() {
        return name;
    }

    private String name;

    StockStore(String name) {
        this.name = name;
    }
}
