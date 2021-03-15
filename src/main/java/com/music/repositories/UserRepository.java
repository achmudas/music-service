package com.music.repositories;

import com.music.models.internal.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
}
