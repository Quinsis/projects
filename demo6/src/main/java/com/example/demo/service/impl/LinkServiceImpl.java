package com.example.demo.service.impl;

import com.example.demo.model.mongodb.OfflineLink;
import com.example.demo.model.mongodb.OnlineLink;
import com.example.demo.repository.OfflineLinkRepository;
import com.example.demo.repository.OnlineLinkRepository;
import com.example.demo.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {
    private final OnlineLinkRepository onlineLinkRepository;
    private final OfflineLinkRepository offlineLinkRepository;

    @Override
    public Optional<OnlineLink> getOnlineLinkByCode(String code) {
        return onlineLinkRepository.getOnlineLinkByCode(code);
    }
    @Override
    public Optional<OfflineLink> getOfflineLinkByCode(String code) {
        return offlineLinkRepository.getOfflineLinkByCode(code);
    }
    @Override
    public Optional<OnlineLink> getOnlineLinkBySchemaId(String schemaId) {
        return onlineLinkRepository.getOnlineLinkBySchemaId(schemaId);
    }
    @Override
    public Optional<List<OfflineLink>> getOfflineLinksBySchemaId(String schemaId) {
        return offlineLinkRepository.getOfflineLinksBySchemaId(schemaId);
    }

    @Override
    public OnlineLink saveOnlineLink(OnlineLink onlineLink) {
        return onlineLinkRepository.save(onlineLink);
    }
    @Override
    public OfflineLink saveOfflineLink(OfflineLink offlineLink) {
        return offlineLinkRepository.save(offlineLink);
    }
    @Override
    public void deleteOnlineLink(OnlineLink onlineLink) {
        onlineLinkRepository.delete(onlineLink);
    }
    @Override
    public void deleteOfflineLink(OfflineLink offlineLink) {
        offlineLinkRepository.delete(offlineLink);
    }
}
