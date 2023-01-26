import styled from 'styled-components';
import { Link, useLocation } from 'react-router-dom';
import ButtonLink from './ButtonLink';

const HeaderContainer = styled.header`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.5rem;
  background-color: black;
  color: white;
  width: 100%;
  height: 100px;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 99;
`;

const Logo = styled(Link)`
  text-decoration: none;
  color: var(--neon-yellow);
  display: flex;
  align-items: center;
  font-family: 'Rubik Bubbles', cursive;
  font-size: 220%;
  text-shadow: var(--neon-yellow) 0 0 1rem;
  i {
    font-size: xx-large;
    margin-right: 0.5rem;
  }
`;

const ButtonsContainer = styled.div`
  display: flex;
  align-items: center;
  form {
    display: flex;
    align-items: center;
    color: var(--gray);
    i {
      margin-right: 0.3rem;
      font-size: 150%;
    }
    input {
      width: 12rem;
      padding: 0.5rem;
      background-color: rgba(1, 1, 1, 0);
      outline: none;
      border: none;
      border-bottom: 0.1rem solid var(--gray);
      color: white;
      font-size: 100%;
    }
    &:focus-within {
      color: white;
      input {
        border-bottom: 0.05rem solid white;
      }
    }
  }
`;

// const Button = styled(Link)`
//   text-decoration: none;
//   background-color: var(--gray);
//   color: white;
//   border-radius: 5px;
//   margin-left: 10px;
//   padding: 8px 14px;
//   transition: 0.2s ease-in-out;
//   font-size: 16px;
//   white-space: nowrap;
//   &:hover {
//     cursor: pointer;
//     background-color: var(--neon-yellow);
//     color: black;
//     transition: 0.2s ease-in-out;
//   }
// `;

const Board = styled.nav`
  display: flex;
  align-items: center;
  margin-top: 10px;
`;

const BoardLink = styled(Link)<{ path: string; to: string }>`
  text-decoration: none;
  font-size: 100%;
  transition: 0.2s ease-in-out;
  width: auto;
  margin-right: 15px;
  white-space: nowrap;
  display: flex;
  align-items: flex-start;
  color: ${(props) =>
    props.path === props.to ? 'var(--neon-yellow)' : 'white'};
  text-shadow: ${(props) => (props.path === props.to ? 'white 0 0 1rem' : '')};
  font-weight: ${(props) => (props.path === props.to ? 'bold' : '')};
  &:hover {
    color: var(--neon-yellow);
    text-shadow: white 0 0 1rem;
    font-weight: bold;
    transition: 0.2s ease-in-out;
  }
`;

const Header = () => {
  const { pathname: path } = useLocation();

  return (
    <HeaderContainer>
      <div>
        <Logo to="/">
          <i className="fa-solid fa-heart-pulse" />
          HEART
        </Logo>
        <Board>
          <BoardLink path={path} to="/freeboard">
            자유게시판
          </BoardLink>
          <BoardLink path={path} to="/freeboard/tags">
            자유게시판 태그
          </BoardLink>
          <BoardLink path={path} to="/recruits">
            모집게시판
          </BoardLink>
          <BoardLink path={path} to="/recruits/tags">
            모집게시판 태그
          </BoardLink>
        </Board>
      </div>
      <ButtonsContainer>
        <form>
          <i className="fa-solid fa-magnifying-glass" />
          <input placeholder="Search here..." />
        </form>
        <ButtonLink value="로그인" to="/login" />
        <ButtonLink value="회원가입" to="/signup" />
      </ButtonsContainer>
    </HeaderContainer>
  );
};

export default Header;
