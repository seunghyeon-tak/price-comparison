package com.github.seunghyeon_tak.price_comparison.api.service.user;

import com.github.seunghyeon_tak.price_comparison.api.service.store.StoreApiService;
import com.github.seunghyeon_tak.price_comparison.db.domain.StoreEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.UserEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.UserPreferredStoreEntity;
import com.github.seunghyeon_tak.price_comparison.db.repository.UserPreferredStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPreferredStoreService {
    private final UserPreferredStoreRepository userPreferredStoreRepository;
    private final UserApiService userApiService;
    private final StoreApiService storeApiService;

    @Transactional
    public void updatePreferredStores(Long userId, List<Long> storeIds) {
        userPreferredStoreRepository.deleteByUserId(userId);

        UserEntity userEntity = userApiService.getUserReference(userId);
        List<UserPreferredStoreEntity> entities = storeIds.stream()
                .map(storeId -> {
                    StoreEntity storeEntity = storeApiService.getStoreReference(storeId);
                    return UserPreferredStoreEntity.builder()
                            .user(userEntity)
                            .store(storeEntity)
                            .build();
                })
                .toList();

        userPreferredStoreRepository.saveAll(entities);
    }
}
