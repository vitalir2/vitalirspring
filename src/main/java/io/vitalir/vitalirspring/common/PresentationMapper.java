package io.vitalir.vitalirspring.common;

public interface PresentationMapper<DOMAIN, PRESENTATION> {
    PRESENTATION domainToPresentationModel(DOMAIN domainModel);
    DOMAIN presentationToDomainModel(PRESENTATION presentationModel);
}
