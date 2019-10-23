package com.dollery.corporation.services.catalog;

class Ver {
    Sem sem;
    int value;
    SittingCommittee committees;

    public Ver(Sem sem) {
        this.sem = sem;
    }

    private Ver(Sem sem, int value, SittingCommittee committees) {
        this(sem);
        this.value = value;
        this.committees = committees;
    }

    public Ver inc() {
        return new Ver(sem, value + 1, committees);
    }
}
