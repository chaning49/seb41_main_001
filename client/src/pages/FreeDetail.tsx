/* eslint-disable no-nested-ternary */
/* eslint-disable @typescript-eslint/no-unused-expressions */
/* eslint-disable no-restricted-globals */
/* eslint-disable no-lone-blocks */
/* eslint-disable operator-linebreak */
import styled from 'styled-components';
import axios from 'axios';
import { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import FreeDataProps from '../interfaces/FreeDataProps';
import timeDifference from '../utils/timeDifference';
import CreatorCard from '../components/CreatorCard';
import Loading from './Loading';
import KakaoMap from '../components/KakaoMap';
import CommentBox from '../components/CommentBox';
import CommentSubmitBox from '../components/CommentSubmitBox';
import ButtonLink from '../components/ButtonLink';
import Button from '../components/Button';
// import preview from '../img/preview.jpeg';

const FDContainer = styled.main`
  background-color: var(--gray);
  color: white;
  display: flex;
  justify-content: center;
  margin-top: 5rem;
  height: 100%;
`;

const BoardContainer = styled.div`
  width: 35rem;
  height: auto;
  margin: 2rem;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  h1 {
    width: 35rem;
  }

  > div:first-child {
    width: 5rem;
    height: 2rem;
    border: 0.05rem solid white;
    border-radius: 0.3rem;
    background-color: white;
    color: black;
    display: flex;
    justify-content: center;
    align-items: center;
    margin-right: 30rem;
    /* margin-top: 0.5rem;
    margin-bottom: 0.5rem; */
    i {
      margin-right: 0.3rem;
    }
    a {
    }
  }

  > div:nth-child(3) {
    display: flex;
    width: 35rem;
    justify-content: space-between;
    > div:first-child {
      width: 5.3rem;
      display: flex;
      align-items: center;
      justify-content: center;
      i {
        margin-right: 0.3rem;
      }
    }
  }

  > div:nth-child(4) {
    width: 35rem;
    margin: 1rem 0;
  }

  .commentCount {
    border-bottom: 1px solid white;
    width: 35rem;
    margin-bottom: 1rem;
    padding: 1rem 0;
  }

  .likeDiv {
    border: 1px solid white;
    border-radius: 0.3rem;
    padding: 0.5rem;
  }

  .btnCon {
    width: 35rem;
    display: flex;
    justify-content: space-between;
    > div:nth-child(2) {
      display: flex;
      > button:first-child {
        margin-right: 0.5rem;
      }
    }
  }
`;

const ContentContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  .body {
    height: auto;
    min-height: 3rem;
    line-height: 150%;
  }

  > div {
    /* border: 0.1rem solid white; */
    width: 35rem;
    height: auto;
    min-height: 3rem;

    img {
      width: 35rem;
      height: 20rem;
      display: flex;
      align-items: center;
      justify-content: center;
      text-align: center;
      /* padding: 1rem; */
      margin: 1rem 0;
    }

    .map {
      width: 35rem;
      height: 10rem;
      display: flex;
      margin: 1rem 0;
    }
  }
`;

const CountContainer = styled.div`
  display: flex;
  border-radius: 0.3rem;
  height: 1rem;
  display: flex;
  align-items: center;

  > div {
    margin-right: 0.5rem;
    i {
      margin-right: 0.3rem;
    }
  }

  .view {
    color: var(--neon-blue);
  }

  .like {
    color: var(--neon-yellow);
  }

  .comment {
    color: var(--neon-green);
  }
`;

const Category = styled('div')<{ color: string }>`
  width: 4.5rem;
  height: 2rem;
  border-radius: 1rem;
  display: flex;
  text-align: center;
  justify-content: center;
  align-items: center;
  background-color: ${(props) => props.color};
`;

const FreeDetail = () => {
  const { freeId } = useParams();
  const [post, setPost] = useState<FreeDataProps | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    axios
      .get(`${process.env.REACT_APP_API_URL}/freeboards/${freeId}`)
      .then((res) => {
        setPost(res.data.data);
        console.log(post);
        setIsLoading(false);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);

  const handleDeleteFree = () => {
    {
      confirm('삭제하시겠습니까?') === true
        ? axios
            .delete(
              `${process.env.REACT_APP_API_URL}/freeboards/${freeId}`,
              // {
              //   headers: {
              //     Authorization: `${localStorage.getItem('AccessToken')}`,
              //     Refresh: `${localStorage.getItem('RefreshToken')}`,
              //   },
              // },
            )
            .then((res) => {
              console.log(res);
              navigate(`/freeboards`);
            })
            .catch((err) => {
              console.log(err);
            })
        : '';
    }
  };

  return (
    <FDContainer>
      {!isLoading ? (
        <BoardContainer>
          <div>
            <Link to={`/freeboard?category=${post?.category}`}>
              <Category
                color={
                  post?.category === '운동'
                    ? '5aa1f1'
                    : post?.category === '정보'
                    ? 'ee8834'
                    : post?.category === '질문'
                    ? '3fb950'
                    : '7dede1'
                }
              >
                {post?.category === '운동' ? (
                  <i className="fa-solid fa-dumbbell" />
                ) : post?.category === '정보' ? (
                  <i className="fa-solid fa-bullhorn" />
                ) : post?.category === '질문' ? (
                  <i className="fa-regular fa-comments" />
                ) : (
                  <i className="fa-solid fa-hand-holding-heart" />
                )}
                {post?.category}
              </Category>
            </Link>
          </div>
          <h1>{post?.freeTitle}</h1>
          <div>
            <div>
              <i className="fa-regular fa-clock" />
              {timeDifference(`${post?.createdAt}`)}
            </div>
            <CountContainer>
              <div>
                <i className="fa-solid fa-eye view" />
                {post?.views}
              </div>
              <div>
                <i className="fa-regular fa-thumbs-up like" />
                {post?.freeLikes.length}
              </div>
              <div>
                <i className="fa-regular fa-comment-dots comment" />
                {post?.freeComments.length}
              </div>
            </CountContainer>
          </div>
          <CreatorCard memberId={1} nickname="aaa" heart={100} />
          <ContentContainer>
            {/* <div>
              <img src={preview} alt="preview" />
            </div> */}
            <div className="body">{post?.freeBody}</div>
            {post?.location === undefined ? (
              ''
            ) : (
              <div>
                <div className="map">
                  <KakaoMap
                    latitude={37.7424074}
                    longitude={127.042215}
                    overlayvalue="운동 장소"
                  />
                </div>
              </div>
            )}
          </ContentContainer>
          <div className="btnCon">
            <Button
              value="좋아요"
              onClick={() => {
                axios
                  .patch(
                    `${process.env.REACT_APP_API_URL}/freeboards/${freeId}/likes`,
                  )
                  .then((res) => {
                    console.log(res);
                  })
                  .catch((err) => {
                    console.log(err);
                  });
              }}
              icon={<i className="fa-solid fa-heart" />}
            />
            <div>
              <ButtonLink
                to={`/freeboard/${freeId}/edit`}
                value="수정"
                icon={<i className="fa-solid fa-pen-to-square" />}
              />
              <Button
                value="삭제"
                onClick={handleDeleteFree}
                icon={<i className="fa-solid fa-trash" />}
              />
            </div>
          </div>
          <div className="commentCount">
            {post?.freeComments.length}
            개의 댓글이 있습니다
          </div>
          {post?.freeComments &&
            post?.freeComments.map((el) => (
              <CommentBox
                key={el.commentId}
                memberId={el.memberId}
                data={el}
                board="freeboards"
                boardId={post.freeId}
              />
            ))}
          <CommentSubmitBox submitComment={`/freeboards/${post?.freeId}`} />
        </BoardContainer>
      ) : (
        <Loading />
      )}
    </FDContainer>
  );
};

export default FreeDetail;
