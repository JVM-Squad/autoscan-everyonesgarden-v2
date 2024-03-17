package com.garden.back.garden.repository.chatmessage;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GardenChatMessageQueryRepository {

    private MongoTemplate mongoTemplate;

    private static final String COLLECTION_NAME = "garden_chat_messages";

    public GardenChatMessageQueryRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Slice<ChatRoomFindRepositoryResponse> findChatRooms(Long memberId, Pageable pageable){
        AggregationOperation subQuery = Aggregation.group("chatRoomId");
        AggregationOperation matchSubQuery = Aggregation.match(Criteria.where("memberId").is(memberId));

        AggregationOperation matchOperation = Aggregation.match(
            Criteria.where("chatRoomId").in(
                Aggregation.newAggregation(matchSubQuery, subQuery)
            )
        );

        AggregationOperation groupOperation = Aggregation.group("chatRoomId")
            .max("chatMessageId").as("chatMessageId")
            .max("createdAt").as("createdAt")
            .sum(
                ConditionalOperators.when(
                    Criteria.where("readOrNot").is(false).and("memberId").ne(memberId)
                ).then(1).otherwise(0)
            ).as("notReadCount")
            .max("chatRoomId").as("chatRoomId");

        AggregationOperation sortOperation = sort(pageable.getSort());
        AggregationOperation skipOperation = skip((long) pageable.getPageNumber() * pageable.getPageSize());
        AggregationOperation limitOperation = limit(pageable.getPageSize());

        Aggregation aggregation = newAggregation(
            matchOperation,
            groupOperation,
            sortOperation,
            skipOperation,
            limitOperation
        );

        List<ChatRoomFindRepositoryResponse> results = mongoTemplate.aggregate(
            aggregation, COLLECTION_NAME, ChatRoomFindRepositoryResponse.class
        ).getMappedResults();

        return new PageImpl<>(results, pageable, results.size());
    }
}
