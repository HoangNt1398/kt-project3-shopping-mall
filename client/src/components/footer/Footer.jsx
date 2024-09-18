import React from "react";
import styled from "styled-components";
import github from "../../assets/img/github.png";

export default function Footer() {
  return (
    <Wrapper>
      <LeftWrapperWhole>
        <LeftWrapper>
          <LeftTitle>Soleilique</LeftTitle>
        </LeftWrapper>   
      </LeftWrapperWhole>

      <RightWrapper>
        <RightTitle>C O N T A C T</RightTitle>
        <BestMaker>
          <a href="https://github.com/HoangNt1398" target="_blank">
            <img src={github} alt="githubImg" />
            NT_Hoang
          </a>
        </BestMaker>
        <p>Copyrightⓒ 2024 All rights reserved by NT Hoang</p>
      </RightWrapper>
      <EndWrapper></EndWrapper>
    </Wrapper>
  );
}

const Wrapper = styled.div`
  width: 100%;
  height: 150px;
  background-color: #f9e8e8;
  display: flex;
  color: #363636;
  font-family: 'Dovemayo_gothic';
`;

const LeftWrapperWhole = styled.div`
  flex: 2.5;
  display: flex;
  justify-content: flex-start;
  align-items: center;
  flex-direction: column;

  /* 이미지- 타이틀 아래 설명 p */
  p {
    font-size: 0.9rem;
    position: relative;
    right: 0.6rem;
    margin-bottom: 5px;
  }
`;

const LeftWrapper = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;

  img {
    width: 100px;
    height: 100px;
  }

  @media screen and (max-width: 768px) {
    flex-direction: column;
    margin-bottom: 2rem;

    img {
      margin-right: 3rem;
      margin-top: 0.5rem;
    }
  }
`;

const LeftTitle = styled.div`
  font-size: 1.5rem;
  position: relative;
  right: 17px;
  top: 35px;
`;

const LeftParagraph = styled.div``;

const RightWrapper = styled.div`
  flex: 4;
  display: flex;
  justify-content: flex-start;
  align-items: flex-start;
  flex-direction: column;
  margin-top: 2.7rem;

  /* contact 하단 copyright */
  p {
    font-size: 0.7rem;
    margin-top: 0.8rem;
  }
  @media screen and (max-width: 500px) {
    margin-left: 1rem;
  }
`;

const RightTitle = styled.div`
  font-size: 1rem;
  margin-bottom: 15px;
`;

const BestMaker = styled.div`
  margin-bottom: 10px;
  font-size: 0.8rem;

  a {
    color: #363636;
    text-decoration-line: none;
    cursor: pointer;

    :hover {
      color: #7c7c7c;
    }
  }

  img {
    width: 0.9rem;
    height: 0.9rem;
    margin-right: 6px;
  }
`;

// 공간 주기~
const EndWrapper = styled.div`
  flex: 2;
`;
