package org.estatio.dom.index;

import java.math.BigDecimal;
import java.util.SortedSet;
import java.util.TreeSet;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.Immutable;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Title;

import org.estatio.dom.WithNameUnique;
import org.estatio.dom.WithReferenceComparable;
import org.estatio.dom.EstatioRefDataObject;
import org.estatio.dom.WithNameGetter;

@javax.jdo.annotations.PersistenceCapable
@javax.jdo.annotations.Query(name = "findByReference", language = "JDOQL", value = "SELECT FROM org.estatio.dom.index.Index WHERE reference == :reference")
@Immutable
public class Index extends EstatioRefDataObject<Index> implements WithReferenceComparable<Index>, WithNameUnique {

    public Index() {
        super("reference");
    }
    
    // //////////////////////////////////////

    @javax.jdo.annotations.Unique(name = "INDEX_REFERENCE_UNIQUE_IDX")
    private String reference;

    @MemberOrder(sequence = "1")
    public String getReference() {
        return reference;
    }

    public void setReference(final String reference) {
        this.reference = reference;
    }

    // //////////////////////////////////////

    @javax.jdo.annotations.Unique(name = "INDEX_NAME_UNIQUE_IDX")
    private String name;

    @Title
    @MemberOrder(sequence = "2")
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    // //////////////////////////////////////

    @javax.jdo.annotations.Persistent(mappedBy = "index")
    private SortedSet<IndexBase> indexBases = new TreeSet<IndexBase>();

    @MemberOrder(name = "Bases", sequence = "3")
    public SortedSet<IndexBase> getIndexBases() {
        return indexBases;
    }

    public void setIndexBases(final SortedSet<IndexBase> indexBases) {
        this.indexBases = indexBases;
    }

    public void addToIndexBases(final IndexBase indexBase) {
        if (indexBase == null || getIndexBases().contains(indexBase)) {
            return;
        }
        indexBase.clearIndex();
        indexBase.setIndex(this);
        getIndexBases().add(indexBase);

    }

    public void removeFromIndexBases(final IndexBase indexBase) {
        if (indexBase == null || !getIndexBases().contains(indexBase)) {
            return;
        }
        indexBase.setIndex(null);
        getIndexBases().remove(indexBase);
    }

    // //////////////////////////////////////

    @Programmatic
    public BigDecimal getIndexValueForDate(LocalDate date) {
        if (date != null) {
            IndexValue indexValue = indexValues.findIndexValueByIndexAndStartDate(this, date);
            return indexValue == null ? null : indexValue.getValue();
        }
        return null;
    }

    @Programmatic
    public BigDecimal getRebaseFactorForDates(LocalDate baseIndexStartDate, LocalDate nextIndexStartDate) {
        if (baseIndexStartDate == null || nextIndexStartDate == null)
            return null;
        IndexValue nextIndexValue = indexValues.findIndexValueByIndexAndStartDate(this, nextIndexStartDate);
        // TODO: check efficiency.. seems to retrieve every single index value
        // for the last 15 years...
        if (nextIndexValue != null) {
            BigDecimal rebaseFactor = nextIndexValue.getIndexBase().factorForDate(baseIndexStartDate);
            return rebaseFactor;
        }
        return null;
    }

    @Programmatic
    public void initialize(IndexationCalculator indexationCalculator, LocalDate baseIndexStartDate, LocalDate nextIndexStartDate) {
        indexationCalculator.setBaseIndexValue(getIndexValueForDate(baseIndexStartDate));
        indexationCalculator.setNextIndexValue(getIndexValueForDate(nextIndexStartDate));
        indexationCalculator.setRebaseFactor(getRebaseFactorForDates(baseIndexStartDate, nextIndexStartDate));
    }

    // //////////////////////////////////////


    private IndexValues indexValues;

    public void injectIndexValues(IndexValues indices) {
        this.indexValues = indices;
    }

}
