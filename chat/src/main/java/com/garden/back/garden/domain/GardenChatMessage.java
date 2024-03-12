package com.garden.back.garden.domain;

import com.garden.back.garden.domain.dto.GardenChatMessageDomainParam;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "garden_chat_messages")
@Entity
public class GardenChatMessage {

    protected GardenChatMessage() {
    }

    @Id
    @Column(name = "chat_message_id", nullable = false)
    private Long chatMessageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private GardenChatRoom chatRoom;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "chat_contents", columnDefinition = "TEXT")
    private String contents;

    @Column(name = "read_or_not", nullable = false)
    private boolean readOrNot;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    private GardenChatMessage(
        Long chatMessageId,
        GardenChatRoom chatRoom,
        Long memberId,
        String contents,
        boolean readOrNot
    ) {
        Assert.notNull(chatMessageId, "chatMessageId는 null일 수 없습니다.");
        Assert.notNull(chatRoom, "Chat Room은 null일 수 없습니다.");
        Assert.isTrue(memberId > 0, "유저 아이디는 0이거나 0보다 작을 수 없습니다.");
        Assert.notNull(contents, "메세지 내용은 null일 수 없습니다.");

        this.chatMessageId = chatMessageId;
        this.chatRoom = chatRoom;
        this.memberId = memberId;
        this.contents = contents;
        this.readOrNot = readOrNot;
    }

    public static GardenChatMessage of(
        Long chatMessageId,
        GardenChatRoom chatRoom,
        Long memberId,
        String contents,
        boolean readOrNot
    ) {
        return new GardenChatMessage(
            chatMessageId,
            chatRoom,
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
            GardenChatRoom.of(gardenChatMessageDomainParam.roomId()),
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
            GardenChatRoom.of(gardenChatMessageDomainParam.roomId()),
            gardenChatMessageDomainParam.memberId(),
            gardenChatMessageDomainParam.contents(),
            false
        );
    }

}
