package com.dollery.corporation.services.org;

import java.util.Set;

public class SoftwareSupplyChain {
    private Set<String> drivers;
    private Set<String> requirements;

    /**
     * Sustainable: Leave only footprints
     * Responsible: Act in the best interests of every stakeholder
     * Trustworthy: Never lie or mislead, and always fulfil our commitments
     * Wise: Our advice should be based on expertise and understanding of our customer's position and the context
     * Profitable: We should profit from our labours, but not excessively or unfairly
     * <p>
     * Taken together these are our product and our USP.
     */
    enum values {
        sustainable, responsible, trustworthy, wise, profitable
    }

    enum kpi {
        profits, losses
    }

    enum drivers {
        financial, time, risk, regulatory, competition, security, social
    }
}

