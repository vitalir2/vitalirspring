package io.vitalir.vitalirspring.common;

public interface DataMapper<DATA, DOMAIN> {
    DATA domainToDataModel(DOMAIN domainModel);
    DOMAIN dataToDomainModel(DATA dataModel);
}
