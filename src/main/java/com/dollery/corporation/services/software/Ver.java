package com.dollery.corporation.services.software;

import com.dollery.corporation.services.catalog.SittingCommittee;

public class Ver {
    Sem sem;
    int value = 0;
    SittingCommittee committees;

    Ver(Sem sem) {
        this(sem, 0, null);
    }


    private Ver(Sem sem, int value, SittingCommittee committees) {
        this.sem = sem;
        this.value = value;
        this.committees = committees;
    }

    Ver inc() {
        return new Ver(sem, value + 1, committees);
    }

    public Sem getSem() {
        return sem;
    }

    public void setSem(Sem sem) {
        this.sem = sem;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
