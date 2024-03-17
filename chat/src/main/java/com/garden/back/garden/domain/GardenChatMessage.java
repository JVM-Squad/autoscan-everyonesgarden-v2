package com.garden.back.garden.domain;

import com.garden.back.garden.domain.dto.GardenChatMessageDomainParam;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class)
@Document(collection = "garden_chat_messages")
public class GardenChatMessage {

    protected GardenChatMessage() {
    }

    @Id
    private Long chatMessageId;

    @Field(name = "chat_room_id")
    private Long chatRoomId;

    @Field(name = "member_id")
    private Long memberId;

    @Field(name = "chat_contents")
    private String contents;

    @Field(name = "read_or_not")
    private boolean readOrNot;

    @CreatedDate
    @Field(name = "created_at")
    private LocalDateTime createdAt;

    private GardenChatMessage(
        Long chatMessageId,
        Long chatRoomId,
        Long memberId,
        String contents,
        boolean readOrNot
    ) {
        Assert.notNull(chatMessageId, "chatMessageId는 null일 수 없습니다.");
        Assert.notNull(chatRoomId, "Chat Room은 null일 수 없습니다.");
        Assert.isTrue(memberId > 0, "유저 아이디는 0이거나 0보다 작을 수 없습니다.");
        Assert.notNull(contents, "메세지 내용은 null일 수 없습니다.");

        this.chatMessageId = chatMessageId;
        this.chatRoomId = chatRoomId;
        this.memberId = memberId;
        this.contents = contents;
        this.readOrNot = readOrNot;
    }

    public static GardenChatMessage of(
        Long chatMessageId,
        Long chatRoomId,
        Long memberId,
        String contents,
        boolean readOrNot
    ) {
        return new GardenChatMessage(
            chatMessageId,
            chatRoomId,
            memberId,
            contents,
            readOrNot
        );

    }

    public static GardenChatMessage toReadGardenChatMessage(
        Long chatMessageId,
        GardenChatMessageDomainParam gardenChatMessageDomainParam
    ) {
        return new GardenChatMessage(
            chatMessageId,
            gardenChatMessageDomainParam.roomId(),
            gardenChatMessageDomainParam.memberId(),
            gardenChatMessageDomainParam.contents(),
            true
        );
    }

    public static GardenChatMessage toNotReadGardenChatMessage(
        Long chatMessageId,
        GardenChatMessageDomainParam gardenChatMessageDomainParam
    ) {
        return new GardenChatMessage(
            chatMessageId,
            gardenChatMessageDomainParam.roomId(),
            gardenChatMessageDomainParam.memberId(),
            gardenChatMessageDomainParam.contents(),
            false
        );
    }

    public GardenChatMessage readMessage() {
        this.readOrNot = true;
        return this;
    }

}
