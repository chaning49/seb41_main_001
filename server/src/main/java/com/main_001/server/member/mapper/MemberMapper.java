package com.main_001.server.member.mapper;

import com.main_001.server.free.dto.FreeDto;
import com.main_001.server.free.entity.Free;
import com.main_001.server.free.entity.FreeComment;
import com.main_001.server.free.entity.FreeLike;
import com.main_001.server.free.entity.FreeTag;
import com.main_001.server.member.dto.*;
import com.main_001.server.member.entity.Member;
import com.main_001.server.member.entity.MemberImage;
import com.main_001.server.member.entity.MemberTag;
import com.main_001.server.recruit.dto.RecruitDto;
import com.main_001.server.recruit.dto.ResponseDto;
import com.main_001.server.recruit.entity.*;
import com.main_001.server.tag.entity.Tag;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    default Member memberPostToMember(MemberDto.MemberPostDto memberPost) {
        Member member = new Member();
        member.setEmail(memberPost.getEmail());
        member.setPassword(memberPost.getPassword());
        member.setName(memberPost.getName());
        member.setBirth(memberPost.getBirth());
        member.setNickname(memberPost.getNickname());
        member.setPhone(memberPost.getPhone());
        member.setSex(memberPost.getSex());
        member.setHeart(50); // controller에서 mapper로 값을 빼주었음
        member.setLocation(memberPost.getLocation());
        member.setLat(memberPost.getLat());
        member.setLon(memberPost.getLon());
        List<MemberTag> memberTags = memberPost.getMemberTags().stream()
                .map(memberTagDto -> {
                    MemberTag memberTag = new MemberTag();
                    Tag tag = new Tag();
                    tag.setTagId(memberTagDto.getTagId());
                    tag.setTagName(memberTagDto.getTagName());
                    tag.setEmoji(memberTagDto.getEmoji());
                    memberTag.addMember(member);
                    memberTag.setTag(tag);
                    return memberTag;
                }).collect(Collectors.toList());
        member.setMemberTags(memberTags);

        return member;
    }

    default Member memberPatchToMember(MemberDto.MemberPatchDto memberPatch) {
        if (memberPatch == null) {
            return null;
        }
        Member member = new Member();
        member.setMemberId(memberPatch.getMemberId());
        member.setNickname(memberPatch.getNickname());
//        member.setPassword(memberPatch.getNewPassword());
        member.setPhone(memberPatch.getPhone());
        member.setLocation(memberPatch.getLocation());
        member.setLat(memberPatch.getLat());
        member.setLon(memberPatch.getLon());
        List<MemberTag> memberTags = memberPatch.getMemberTags()
                .stream()
                .map(memberTagDto -> {
                    MemberTag memberTag = new MemberTag();
                    Tag tag = new Tag();
                    tag.setTagId(memberTagDto.getTagId());
                    tag.setTagName(memberTagDto.getTagName());
                    tag.setEmoji(memberTagDto.getEmoji());
                    memberTag.addMember(member);
                    memberTag.setTag(tag);
                    return memberTag;
                }).collect(Collectors.toList());
        member.setMemberTags(memberTags);
        member.setSex(memberPatch.getSex());
        member.setBirth(memberPatch.getBirth());

        return member;
    }

    default MemberDto.MyResponse memberToMemberMyResponse(Member member) {
        // profile image
        MemberImage memberImage = member.getMemberImage();

        // Tag
        List<MemberTag> memberTags = member.getMemberTags();

        // member가 Recruit에서 작성한 데이터들
        List<Apply> applies = member.getApplies();
        List<Recruit> recruits = member.getRecruits();
        List<RecruitComment> recruitComments = member.getRecruitComments();
        List<RecruitLike> recruitLikes = member.getRecruitLikes();
        List<Review> reviews = member.getReviews();

        // member가 FreeBoard에서 작성한 데이터들
        List<Free> frees = member.getFrees();
        List<FreeLike> freeLikes = member.getFreeLikes();
        List<FreeComment> freeComments = member.getFreeComments();

        MemberDto.MyResponse myResponse = MemberDto.MyResponse.builder()
                .memberId(member.getMemberId())
                .name(member.getName())
                .birth(member.getBirth())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .phone(member.getPhone())
                .sex(member.getSex())
                .createdAt(member.getCreatedAt())
                .heart(member.getHeart())
                .location(member.getLocation())
                .lat(member.getLat())
                .lon(member.getLon())
                .provider(member.getProvider())
                .picture(member.getPicture())

                .memberTags(memberTagsToMemberTagResponseDtos(memberTags))

                .applies(appliesToApplyResponseDtos(applies))
                .recruits(recruitsToRecruitResponseDtos(recruits))
                .recruitComments(recruitCommentsToRecruitCommentResponseDtos(recruitComments))
                .recruitLikes(recruitLikesToMemberRecruitLikeResponseDtos(recruitLikes))
                .reviews(reviewsToReviewResponseDtos(reviews))

                .frees(freesToFreeResponseDtos(frees))
                .freeLikes(freeLikesToMemberFreeLikeResponseDtos(freeLikes))
                .freeComments(freeCommentsToFreeCommentResponseDtos(freeComments))
                .build();

        // 이미지가 존재하는 경우
        if (member.getMemberImage() != null)
            myResponse.setMemberImage(memberImageToMemberImageResponseDto(memberImage));

        return myResponse;
    }

    // member가 가지고 있는 tag 정보
    default List<MemberTagResponseDto> memberTagsToMemberTagResponseDtos(List<MemberTag> memberTags) {
        return memberTags
                .stream()
                .map(memberTag -> MemberTagResponseDto
                        .builder()
                        .tagId(memberTag.getTag().getTagId())
                        .tagName(memberTag.getTag().getTagName())
                        .emoji(memberTag.getTag().getEmoji())
                        .build())
                .collect(Collectors.toList());
    }

    // member의 프로필 이미지
    default MemberImageResponseDto memberImageToMemberImageResponseDto(MemberImage memberImage) {
        return MemberImageResponseDto.builder()
                .memberImageId(memberImage.getMemberImageId())
                .memberId(memberImage.getMember().getMemberId())
                .originalFileName(memberImage.getOriginalFileName())
                .storedFileName(memberImage.getStoredFileName())
                .filePath(memberImage.getFilePath())
                .fileSize(memberImage.getFileSize())
                .build();
    }

    // member의 지원 정보
    default List<ResponseDto.Apply> appliesToApplyResponseDtos(List<Apply> applies) {
        return applies
                .stream()
                .map(apply -> ResponseDto.Apply
                        .builder()
                        .recruitId(apply.getRecruit().getRecruitId())
                        .memberId(apply.getMember().getMemberId())
                        .nickname(apply.getMember().getNickname())
                        .heart(apply.getMember().getHeart())
                        .build())
                .collect(Collectors.toList());
    }

    // member가 작성한 모집글
    default List<MemberRecruitResponseDto> recruitsToRecruitResponseDtos(List<Recruit> recruits) {
        if (recruits == null) {
            return null;
        }

        List<MemberRecruitResponseDto> list = new ArrayList<>(recruits.size());
        for (Recruit recruit : recruits) {
            list.add(recruitToRecruitResponseDto(recruit));
        }

        return list;
    }

    default MemberRecruitResponseDto recruitToRecruitResponseDto(Recruit recruit) {
        if (recruit == null) {
            return null;
        }

        String newString = recruit.getAgeGroupString().replaceAll("[\\[\\]\\s]", "");
        StringTokenizer st = new StringTokenizer(newString, ",");
        List<String> ageGroup = new ArrayList<>();
        while (st.hasMoreTokens()) {
            ageGroup.add(st.nextToken());
        }

        // TODO postman에서 값 안넘어오는 부분 builder 타입으로 한번에 return 한다.
        return MemberRecruitResponseDto.builder()
                .recruitId(recruit.getRecruitId())
                .title(recruit.getTitle())
                .body(recruit.getBody())
                .createdAt(recruit.getCreatedAt())
                .modifiedAt(recruit.getModifiedAt())
                .require(recruit.getRequire())
                .minRequire(recruit.getMinRequire())
                .recruitStatus(recruit.getRecruitStatus().getStepDescription())
                .star(recruit.getStar())
                .views(recruit.getViews())
                .heartLimit(recruit.getHeartLimit())
                .ageGroup(ageGroup)
                .memberId(recruit.getMember().getMemberId())
                .nickname(recruit.getMember().getNickname())
                .authorHeart(recruit.getMember().getHeart())
                .sex(recruit.getSex())
                .date(recruit.getDate())
                .location(recruit.getLocation())
                .lat(recruit.getLat())
                .lon(recruit.getLon())
                .distance(recruit.getDistance())
                .applies(appliesToApplyResponseDtos(recruit.getApplies()))
                .recruitComments(recruitCommentsToRecruitCommentResponseDtos(recruit.getRecruitComments()))
                .recruitLikes(recruitLikesToMemberRecruitLikeResponseDtos(recruit.getRecruitLikes()))
                .Likes(recruit.getRecruitLikes().size())
                .recruitTags(recruitTagsToRecruitTagResponseDtos(recruit.getRecruitTags()))
                .reviews(reviewsToReviewResponseDtos(recruit.getReviews()))
                .build();
    }

    // member가 작성한 모집글에 대한 댓글
    default List<ResponseDto.RecruitComment> recruitCommentsToRecruitCommentResponseDtos(List<RecruitComment> recruitComments) {
        return recruitComments
                .stream()
                .map(recruitComment -> ResponseDto.RecruitComment
                        .builder()
                        .recruitId(recruitComment.getRecruit().getRecruitId())
                        .recruitCommentId(recruitComment.getId())
                        .memberId(recruitComment.getMember().getMemberId())
                        .nickname(recruitComment.getMember().getNickname())
                        .heart(recruitComment.getMember().getHeart())
                        .body(recruitComment.getBody())
                        .createdAt(recruitComment.getCreatedAt())
                        .modifiedAt(recruitComment.getModifiedAt())
                        .build())
                .collect(Collectors.toList());
    }

    // member가 누른 좋아요(Deprecated)
    default List<ResponseDto.RecruitLike> recruitLikesToRecruitLikeResponseDtos(List<RecruitLike> recruitLikes) {
        return recruitLikes
                .stream()
                .map(recruitLike -> ResponseDto.RecruitLike
                        .builder()
                        .recruitId(recruitLike.getRecruit().getRecruitId())
                        .memberId(recruitLike.getMember().getMemberId())
                        .build())
                .collect(Collectors.toList());
    }

    // 좋아요 마이페이지 표시용
    default List<MemberRecruitLikeResponseDto> recruitLikesToMemberRecruitLikeResponseDtos(List<RecruitLike> recruitLikes) {
        return recruitLikes
                .stream()
                .map(recruitLike -> MemberRecruitLikeResponseDto
                        .builder()
                        .recruitId(recruitLike.getRecruit().getRecruitId())
                        .memberId(recruitLike.getMember().getMemberId())
                        .title(recruitLike.getRecruit().getTitle())
                        .require(recruitLike.getRecruit().getRequire())
                        .applyCount(recruitLike.getRecruit().getApplies().size())
                        .date(recruitLike.getRecruit().getDate())
                        .recruitTags(recruitTagsToRecruitTagResponseDtos(recruitLike.getRecruit().getRecruitTags()))
                        .build())
                .collect(Collectors.toList());
    }

    // member가 작성한 review
    default List<ResponseDto.Review> reviewsToReviewResponseDtos(List<Review> reviews) {
        return reviews
                .stream()
                .map(review -> ResponseDto.Review
                        .builder()
                        .reviewId(review.getId())
                        .memberId(review.getMember().getMemberId())
                        .nickname(review.getMember().getNickname())
                        .heart(review.getMember().getHeart())
                        .body(review.getBody())
                        .star(review.getStar())
                        .build())
                .collect(Collectors.toList());
    }

    default List<ResponseDto.RecruitTag> recruitTagsToRecruitTagResponseDtos(List<RecruitTag> recruitTags) {
        return recruitTags
                .stream()
                .map(recruitTag -> ResponseDto.RecruitTag
                        .builder()
                        .tagId(recruitTag.getTag().getTagId())
                        .tagName(recruitTag.getTag().getTagName())
                        .emoji(recruitTag.getTag().getEmoji())
                        .build())
                .collect(Collectors.toList());
    }
    // free 작성되면 입력
    default List<MemberFreeResponseDto> freesToFreeResponseDtos(List<Free> frees) {
        if ( frees == null ) {
            return null;
        }

        List<MemberFreeResponseDto> list = new ArrayList<>( frees.size() );
        for (Free free : frees) {
            list.add(freeToFreeResponseDto(free));
        }

        return list;
    }

    default MemberFreeResponseDto freeToFreeResponseDto(Free free) {
        List<FreeTag> freeTags = free.getFreeTags();
        List<FreeComment> freeComments = free.getFreeComments();
        List<FreeLike> freeLikes = free.getFreeLikes();

        return MemberFreeResponseDto.builder()
                .freeId(free.getFreeId())
                .freeTitle(free.getFreeTitle())
                .freeBody(free.getFreeBody())
                .createdAt(free.getCreatedAt())
                .modifiedAt(free.getModifiedAt())
                .freeTags(freeTagsToFreeTagResponseDtos(freeTags))
                .freeLikes(freeLikesToMemberFreeLikeResponseDtos(freeLikes))
                .freeComments(freeCommentsToFreeCommentResponseDtos(freeComments))
                .views(free.getViews())
                .memberId(free.getMember().getMemberId())
                .nickname(free.getMember().getNickname())
                .authorHeart(free.getMember().getHeart())
                .category(free.getCategory())
                .build();
    }

    default List<com.main_001.server.free.dto.ResponseDto.FreeLike> freeLikesToFreeLikeResponseDtos(List<FreeLike> freeLikes) {
        return freeLikes
                .stream()
                .map(freeLike -> com.main_001.server.free.dto.ResponseDto.FreeLike
                        .builder()
                        .freeId(freeLike.getFree().getFreeId())
                        .memberId(freeLike.getMember().getMemberId())
                        .build())
                .collect(Collectors.toList());
    }

    // free 좋아요
    default List<MemberFreeLikeResponseDto> freeLikesToMemberFreeLikeResponseDtos(List<FreeLike> freeLikes) {
        return freeLikes
                .stream()
                .map(freeLike -> MemberFreeLikeResponseDto
                        .builder()
                        .freeId(freeLike.getFree().getFreeId())
                        .memberId(freeLike.getMember().getMemberId())
                        .title(freeLike.getFree().getFreeTitle())
                        .freeTags(freeTagsToFreeTagResponseDtos(freeLike.getFree().getFreeTags()))
                        .build())
                .collect(Collectors.toList());
    }

    default List<com.main_001.server.free.dto.ResponseDto.FreeComment> freeCommentsToFreeCommentResponseDtos(List<FreeComment> freeComments) {
        return freeComments
                .stream()
                .map(freeComment -> com.main_001.server.free.dto.ResponseDto.FreeComment
                        .builder()
                        .freeId(freeComment.getFree().getFreeId())
                        .freeCommentId(freeComment.getCommentId())
                        .memberId(freeComment.getMember().getMemberId())
                        .nickname(freeComment.getMember().getNickname())
                        .heart(freeComment.getMember().getHeart())
                        .body(freeComment.getBody())
                        .createdAt(freeComment.getCreatedAt())
                        .modifiedAt(freeComment.getModifiedAt())
                        .build())
                .collect(Collectors.toList());
    }

    default List<com.main_001.server.free.dto.ResponseDto.FreeTag> freeTagsToFreeTagResponseDtos(List<FreeTag> freeTags) {
        return freeTags
                .stream()
                .map(freeTag -> com.main_001.server.free.dto.ResponseDto.FreeTag
                        .builder()
                        .tagId(freeTag.getTag().getTagId())
                        .tagName(freeTag.getTag().getTagName())
                        .emoji(freeTag.getTag().getEmoji())
                        .build())
                .collect(Collectors.toList());
    }

    default MemberDto.OtherResponse memberToMemberOtherResponse(Member member) {
        MemberImage memberImage = member.getMemberImage();

        // Tag
        List<MemberTag> memberTags = member.getMemberTags();

        // Recruit
        List<Recruit> recruits = member.getRecruits();

        // Free
        List<Free> frees = member.getFrees();

        List<String> roles = member.getRoles();

        MemberDto.OtherResponse otherResponse = MemberDto.OtherResponse.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .sex(member.getSex())
                .heart(member.getHeart())
                .location(member.getLocation())
                .lat(member.getLat())
                .lon(member.getLon())
                .provider(member.getProvider())
                .picture(member.getPicture())
                .memberTags(memberTagsToMemberTagResponseDtos(memberTags))
                .recruits(recruitsToRecruitResponseDtos(recruits))
                .frees(freesToFreeResponseDtos(frees))
                .roles(roles)
                .build();

        // 프로필 사진이 있는 경우
        if (member.getMemberImage() != null)
            otherResponse.setMemberImage(memberImageToMemberImageResponseDto(memberImage));

        return otherResponse;
    }
}
