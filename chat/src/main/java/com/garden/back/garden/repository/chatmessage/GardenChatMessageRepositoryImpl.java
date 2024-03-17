package com.garden.back.garden.repository.chatmessage;

import com.garden.back.garden.domain.GardenChatMessage;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class GardenChatMessageRepositoryImpl implements GardenChatMessageRepository {

    private final GardenChatMessageMongoRepository gardenChatMessageMongoRepository;
    private final GardenChatMessageQueryRepository gardenChatMessageQueryRepository;

    public GardenChatMessageRepositoryImpl(GardenChatMessageJpaRepository gardenChatMessageJpaRepository, GardenChatMessageMongoRepository gardenChatMessageMongoRepository, GardenChatMessageQueryRepository gardenChatMessageQueryRepository) {
        this.gardenChatMessageMongoRepository = gardenChatMessageMongoRepository;
        this.gardenChatMessageQueryRepository = gardenChatMessageQueryRepository;
    }

    @Override
    public void markMessagesAsRead(Long roomId,
                                   Long partnerId) {
        GardenChatMessage gardenChatMessage = gardenChatMessageMongoRepository.getByChatRoomIdAndPartnerId(roomId, partnerId);
        gardenChatMessage.readMessage();
        gardenChatMessageMongoRepository.save(gardenChatMessage);
    }

    @Override
    public GardenChatMessage save(GardenChatMessage chatMessage) {
        return gardenChatMessageMongoRepository.save(chatMessage);
    }

    @Override
    public List<GardenChatMessage> findAll() {
        return gardenChatMessageMongoRepository.findAll();
    }

    @Override
    public Optional<GardenChatMessage> findById(Long chatMessageId) {
        return gardenChatMessageMongoRepository.findByChatMessageId(chatMessageId);
    }

    @Override
    public GardenChatMessage getById(Long chatMessageId) {
        return findById(chatMessageId).orElseThrow(()-> new EntityNotFoundException("해당하는 아이디의 메세지는 존재하지 않습니다."));
    }

    @Override
    public Slice<GardenChatMessage> getGardenChatMessage(Long chatRoomId, Pageable pageable) {
        return gardenChatMessageMongoRepository.getGardenChatMessage(chatRoomId, pageable);
    }

    @Override
    public Slice<ChatRoomFindRepositoryResponse> findChatRooms(Long memberId, Pageable pageable) {
        return gardenChatMessageQueryRepository.findChatRooms(memberId, pageable);
    }

    @Override
    public String getContentsById(Long chatMessageId) {
        return gardenChatMessageMongoRepository.getContentsByChatMessageId(chatMessageId);
    }
}
