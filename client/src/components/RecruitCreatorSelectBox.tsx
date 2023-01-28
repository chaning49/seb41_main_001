import { useParams } from 'react-router-dom';
import styled from 'styled-components';
import Button from './Button';
import ButtonLink from './ButtonLink';

const CreatorSelectBox = styled.div`
  display: flex;
  justify-content: flex-end;
  button {
    margin-left: 10px;
  }
`;

interface ApplicantsDataProps {
  applies: { memberId: number; nickname: string; heart: number }[];
  modifiedAt: string;
}

const RecruitCreatorSelectBox = ({
  applies,
  modifiedAt,
}: ApplicantsDataProps) => {
  const { recruitId } = useParams();

  const checkIfBringUpPossible = (d: string) => {
    const TIME_MODIFIED = new Date(d).getTime();
    const TIME_NOW = new Date().getTime();
    if (TIME_NOW - TIME_MODIFIED > 24 * 60 * 60 * 1000) return true;
    return false;
  };

  return (
    <CreatorSelectBox>
      <ButtonLink
        value="수정"
        to={`/recruit/${recruitId}/edit`}
        icon={<i className="fa-solid fa-pen-to-square" />}
      />
      <Button
        value="삭제"
        onClick={() => console.log(`DELETE /recruits/${recruitId}`)}
        disabled={applies.length >= 1}
        icon={<i className="fa-solid fa-trash" />}
      />
      <Button
        value="끌어올리기"
        onClick={() => console.log(`PATCH /recruits/${recruitId}/bringup`)}
        disabled={!checkIfBringUpPossible(modifiedAt)}
        icon={<i className="fa-solid fa-circle-up" />}
      />
    </CreatorSelectBox>
  );
};

export default RecruitCreatorSelectBox;

// 글쓴 사람이 할 수 있는 것. 모집글 수정, 삭제, 끌올
// 글쓴 사람이 아니면 디폴트로 좋아요 버튼만 노출