package com.main_001.server.free.controller;

import com.main_001.server.dto.MultiResponseDto;
import com.main_001.server.dto.SingleResponseDto;
import com.main_001.server.free.dto.FreeDto;
import com.main_001.server.free.entity.Free;
import com.main_001.server.free.entity.FreeComment;
import com.main_001.server.free.entity.FreeLike;
import com.main_001.server.free.mapper.FreeMapper;
import com.main_001.server.free.service.FreeService;
import com.main_001.server.recruit.dto.RecruitLikeDto;
import com.main_001.server.recruit.entity.RecruitLike;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/freeboards")
@Validated
public class FreeController {
    private FreeMapper freeMapper;
    private FreeService freeService;

    public FreeController(FreeMapper freeMapper, FreeService freeService) {
        this.freeMapper = freeMapper;
        this.freeService = freeService;
    }
    @ApiOperation(value = "자유 게시글 작성", notes = "작성자 id, 제목, 본문, 태그, 위치정보를 입력하여 자유 게시글을 작성한다.")
    @PostMapping
    public ResponseEntity createFreeboard(@RequestBody FreeDto.PostFreeboard postFreeboaedDto){
        Free free = freeService.createFreeboard(freeMapper.freeboardPostToFree(postFreeboaedDto));
        return new ResponseEntity<>(freeMapper.FreeToFreeResponseDto(free), HttpStatus.CREATED);
    }
    @ApiOperation(value = "자유 게시글에 대한 댓글 작성", notes = "자유 게시글에 댓글을 작성한다.")
    @PostMapping("/{free-id}")
    public ResponseEntity createComment(@PathVariable("free-id") @Positive long id,
                                        FreeDto.PostComment postCommentDto){
        FreeComment freeComment = freeService.createFreeComment(freeMapper.commentPostToFreeComment(postCommentDto));
        return new ResponseEntity<>( new SingleResponseDto<>(freeMapper.FreeToFreeCommentResponseDto(freeComment)), HttpStatus.CREATED);
    }
    @ApiOperation(value = "자유 게시글 조회", notes = "자유 게시글 id를 path에 붙여서 자유 게시글을 조회한다.")
    @GetMapping("/{free-id}")
    public ResponseEntity getFreeboard(@PathVariable("free-id") @Positive long freeId){
        Free free = freeService.findFreeboard(freeId);
        return new ResponseEntity<>(
                new SingleResponseDto<>(free), HttpStatus.OK);
    }
    @ApiOperation(value = "자유 게시글 전체 조회", notes = "page, size, searchDto(type=[category, tag, keyword], keyword) path에 작성하여 필터링한 전체 자유 개시글을 조회한다.")
    @GetMapping
    public ResponseEntity getFreeboardPage(@RequestParam int page,
                                           @RequestParam int size,
                                           @RequestParam FreeDto.Search searchDto){
        Page<Free> freeboardsPage = freeService.findFreeboards(page - 1, size, searchDto);
        List<Free> freeboardsList = freeboardsPage.getContent();
        return new ResponseEntity<>(
                new MultiResponseDto<>(freeMapper.FreeToFreeMultiRespnseDto(freeboardsList),freeboardsPage), HttpStatus.OK);
    }

    @ApiOperation(value = "자유 게시글 수정", notes = "자유 게시글 내용의 값을 변경하여 자유 게시글 내용을 수정한다.")
    @PatchMapping("/{free-id}")
    public ResponseEntity patchFreeboard(@PathVariable("free-id") @Positive long freeId,
                                        @RequestBody FreeDto.PatchFreeboard patchFreeboardDto){
        Free freeboard = freeService.findFreeboard(freeId);
        Free free = freeService.updateFreeboard(freeboard, patchFreeboardDto);
        return new ResponseEntity<>(
                new SingleResponseDto<>(freeMapper.FreeToFreeResponseDto(free)), HttpStatus.OK);
    }

    @ApiOperation(value = "자유 게시글 뎃글 수정", notes = "자유 게시글 뎃글 내용의 값을 변경하여 자유 게시글 뎃글 내용을 수정한다.")
    @PatchMapping("/{free-id}/{comment-id}")
    public ResponseEntity patchComment(@PathVariable("free-id") @Positive long freeId,
                                      @PathVariable("comment-id") @Positive long commentId,
                                      @RequestBody FreeDto.PatchComment patchCommentDto){
        FreeComment updateFreeComment = freeService.updateFreeComment(freeId, commentId, patchCommentDto);
        return new ResponseEntity<>(
                new SingleResponseDto<>(freeMapper.FreeToFreeCommentResponseDto(updateFreeComment)), HttpStatus.OK);
    }

    @ApiOperation(value = "자유 게시글 좋아요 표시", notes = "자유 게시글에 좋아요를 누른 member id를 추가하거나 삭제한다.")
    @PatchMapping("/{recruit-id}/likes")
    public ResponseEntity patchLike(@PathVariable("recruit-id") long freeId,
                                    @RequestBody FreeDto.Like freeLikeDto) {
        List<FreeLike> freeLikes = freeService.updateLike(freeId, freeMapper.freeLikeDtoToFreeLike(freeLikeDto)).getFreeLikes();

        return new ResponseEntity<>(
                new SingleResponseDto<>(freeMapper.freeLikesToFreeLikeResponseDtos(freeLikes)), HttpStatus.OK);
    }
    @ApiOperation(value = "자유 게시글 삭제", notes = "글을 작성한 작성자 id를 입력해서 자유 게시글을 삭제한다.")
    @DeleteMapping("/{free-id}")
    public ResponseEntity deleteFreeboard(@PathVariable("free-id") @Positive long freeId,
                                          @RequestBody FreeDto.Delete requestBody){
        freeService.deleteFreeboard(freeId, requestBody.getMemberId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @ApiOperation(value = "자유 게시글 뎃글 삭제", notes = "글을 작성한 작성자 id를 입력해서 자유 게시글 뎃을 삭제한다.")
    @DeleteMapping("/{free-id}/{comment-id}")
    public ResponseEntity deleteComment(@PathVariable("free-id") @Positive long freeId,
                                        @PathVariable("comment-id") @Positive long commentId){
        FreeComment freeComment = freeService.findFreeComment(freeId, commentId);
        freeService.deleteFreeComment(freeComment);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}