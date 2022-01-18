package com.example.demo.repository.impl;

import com.example.demo.entity.Image;
import com.example.demo.entity.enums.DeleteStatus;
import com.example.demo.entity.enums.Role;
import com.example.demo.repository.custom.ImageRepositoryCustom;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.example.demo.entity.QImage.*;

@Slf4j
@RequiredArgsConstructor
public class ImageRepositoryImpl implements ImageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Image> getImageList(String uuid) {
        return queryFactory
                .selectFrom(image)
                .where(image.uuid.eq(uuid))
                .fetch();
    }

    @Override
    public List<Image> getImageListByStatus(Enum<DeleteStatus> status) {
        return queryFactory
                .selectFrom(image)
                .where(image.deleteStatus.eq((DeleteStatus) status))
                .fetch();
    }

    @Override
    public void deleteImageListByStatus() {
        queryFactory
            .delete(image)
            .where(image.deleteStatus.eq(DeleteStatus.Deleted))
            .execute();
    }
}
