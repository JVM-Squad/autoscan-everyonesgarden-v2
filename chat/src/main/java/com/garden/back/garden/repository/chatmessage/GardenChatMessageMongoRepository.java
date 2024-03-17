package com.garden.back.garden.repository.chatmessage;

import com.garden.back.garden.domain.GardenChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface GardenChatMessageMongoRepository extends MongoRepository<GardenChatMessage, String> {

    GardenChatMessage getByChatRoomIdAndPartnerId(Long chatRoomId, Long partnerId);

    @Query(value = "{ 'chat_room_id' : ?0 }", sort = "{ 'chat_message_id' : -1 }")
    Slice<GardenChatMessage> getGardenChatMessage(Long chatRoomId, Pageable pageable);

    @Query(value = "{ 'chat_message_id' : ?0 }", fields = "{ 'contents' : 1, '_id' : 0 }")
    String getContentsByChatMessageId(Long chatMessageId);

    @Query(value = "{ 'chat_message_id' : ?0 }")
    Optional<GardenChatMessage> findByChatMessageId(Long chatMessageId);

}
