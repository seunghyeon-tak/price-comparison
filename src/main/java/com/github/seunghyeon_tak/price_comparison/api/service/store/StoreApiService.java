package com.github.seunghyeon_tak.price_comparison.api.service.store;

import com.github.seunghyeon_tak.price_comparison.common.exception.ApiException;
import com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.store.StoreResponseCode;
import com.github.seunghyeon_tak.price_comparison.db.domain.StoreEntity;
import com.github.seunghyeon_tak.price_comparison.db.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreApiService {
    private final StoreRepository storeRepository;

    public void validateStoresExist(List<Long> storeIds) {
        List<StoreEntity> existingStoreIds = storeRepository.findByIdIn(storeIds);

        List<Long> invalidStoreIds = storeIds.stream()
                .filter(id -> !existingStoreIds.contains(id))
                .toList();

        // 존재하지 않는 storeId가 있으면 예외발생
        if (!invalidStoreIds.isEmpty()) {
            throw new ApiException(StoreResponseCode.STORE_IS_EMPTY);
        }
    }

    public StoreEntity getStoreReference(Long storeId) {
        return storeRepository.getReferenceById(storeId);
    }
}
