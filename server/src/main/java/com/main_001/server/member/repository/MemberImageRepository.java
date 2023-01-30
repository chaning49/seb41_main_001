package com.main_001.server.member.repository;

import com.main_001.server.member.entity.MemberImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberImageRepository extends JpaRepository<MemberImage, Long> {
}